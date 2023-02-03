package net.server;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * server class
 */
public class Server<RequestT, ResponseT> extends Thread implements SenderReceiver<RequestT, ResponseT>, DataHandler<RequestT, ResponseT>{
    public final int MAX_CLIENTS = 10;



    private int port;
    private DatagramChannel channel;

    private ExecutorService receiverThreadPool;
    private ExecutorService senderThreadPool;
    private ExecutorService requestHandlerThreadPool;

    private Queue<Map.Entry<InetSocketAddress, RequestT>> requestQueue;
    private Queue<Map.Entry<InetSocketAddress, ResponseT>> responseQueue;

    private volatile boolean running;

    private Selector selector;

    private DataHandler<RequestT, ResponseT> dataHandler;
    private Logger logger;
    private void init(int p) throws ConnectionException, DatabaseException {
        running = true;
        port = p;

        receiverThreadPool = Executors.newFixedThreadPool(MAX_CLIENTS);
        senderThreadPool = Executors.newFixedThreadPool(MAX_CLIENTS);
        requestHandlerThreadPool = Executors.newFixedThreadPool(MAX_CLIENTS);

        requestQueue = new ConcurrentLinkedQueue<>();
        responseQueue = new ConcurrentLinkedQueue<>();
        logger = new DefaultLogger();

        host(port);
        setName("server thread");
        logger.trace("starting server");
    }

    private void host(int p) throws ConnectionException {
        try {
            if (channel != null && channel.isOpen()) channel.close();
            port = p;
            channel = DatagramChannel.open();
            channel.configureBlocking(false);
            channel.bind(new InetSocketAddress(port));
            selector = Selector.open();
            channel.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ);
        } catch (AlreadyBoundException e) {
            throw new PortAlreadyInUseException();
        } catch (IllegalArgumentException e) {
            throw new InvalidPortException();
        } catch (IOException e) {
            throw new ConnectionException("something went wrong during server initialization");
        }
    }

    public void setDataHandler(DataHandler<RequestT, ResponseT> dataHandler) {
        this.dataHandler = dataHandler;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
    
    public Server(int p) throws ConnectionException {
        init(p);
    }

    /**
     * receives request from client
     *
     * @throws ConnectionException
     * @throws InvalidDataException
     */
    private void receive() throws ConnectionException, InvalidDataException {
        ByteBuffer buf = ByteBuffer.allocate(BUFFER_SIZE);
        InetSocketAddress clientAddress = null;
        RequestT request = null;
        try {
            clientAddress = (InetSocketAddress) channel.receive(buf);
            if (clientAddress == null) return; //no data to read
            logger.trace("received request from " + clientAddress.toString());
        } catch (ClosedChannelException e) {
            throw new ClosedConnectionException();
        } catch (IOException e) {
            throw new ConnectionException("something went wrong during receiving request");
        }
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(buf.array()));
            request = (RequestT) objectInputStream.readObject();
        } catch (ClassNotFoundException | ClassCastException | IOException e) {
            throw new InvalidReceivedDataException();
        }

        requestQueue.offer(new AbstractMap.SimpleEntry<>(clientAddress, request));

    }

    /**
     * sends response
     *
     * @param clientAddress
     * @param response
     * @throws ConnectionException
     */
    public void send(InetSocketAddress clientAddress, ResponseT response) throws ConnectionException {
        if (clientAddress == null) throw new InvalidAddressException("no client address found");
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(BUFFER_SIZE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(response);
            channel.send(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()), clientAddress);
            logger.trace("sent response to " + clientAddress.toString());
        } catch (IOException e) {
            throw new ConnectionException("something went wrong during sending response");
        }
    }

    public ResponseT handle(RequestT request) throws Exception{

    }
    private void handleRequest(InetSocketAddress address, RequestT request) {
        if(dataHandler==null) dataHandler = this::handle;
        try(ResponseT ans = dataHandler.handle(request)){
            responseQueue.offer(new AbstractMap.SimpleEntry<>(address, ans));
        } catch (Exception e){
            logger.error("something went wrong during processing request", e.getMessage());
        }
        
        
    }

    /**
     * runs server in multithreading mode
     */
    public void run() {
        while (running) {
            try {
                selector.select();
            } catch (IOException e) {
                continue;
            }
            Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
            while (selectedKeys.hasNext()) {
                SelectionKey key = selectedKeys.next();
                selectedKeys.remove();
                if (!key.isValid()) {
                    continue;
                }
                if (key.isReadable()) {
                    receiverThreadPool.submit(new Receiver());
                    continue;
                }
                if (key.isWritable() && responseQueue.size() > 0) {
                    senderThreadPool.submit(new Sender(responseQueue.poll()));
                }
            }
            if (requestQueue.size() > 0) {
                requestHandlerThreadPool.submit(new RequestHandler(requestQueue.poll()));
            }
        }
    }


    /**
     * close server and connection
     */
    public void close() {
        try {
            running = false;
            receiverThreadPool.shutdown();
            requestHandlerThreadPool.shutdown();
            senderThreadPool.shutdown();
            databaseHandler.closeConnection();
            channel.close();
        } catch (IOException e) {
            logger.error("cannot close channel");
        }
    }

    private class Receiver implements Runnable {
        public void run() {
            try {
                receive();
            } catch (ConnectionException | InvalidDataException e) {
                logger.error(e.getMessage());
            }
        }
    }

    private class RequestHandler implements Runnable {
        private final RequestT request;
        private final InetSocketAddress address;

        public RequestHandler(Map.Entry<InetSocketAddress, RequestT> requestEntry) {
            request = requestEntry.getValue();
            address = requestEntry.getKey();
        }

        public void run() {
            handleRequest(address, request);
        }
    }

    private class Sender implements Runnable {
        private final ResponseT response;
        private final InetSocketAddress address;

        public Sender(Map.Entry<InetSocketAddress, ResponseT> responseEntry) {
            response = responseEntry.getValue();
            address = responseEntry.getKey();
        }

        public void run() {
            try {
                send(address, response);
            } catch (ConnectionException e) {
                logger.error(e.getMessage());
            }
        }
    }
}