package net.client;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.exceptions.*;
import net.common.*;
import net.logwrapper.*;
/**
 * server class
 */
public class Client<RequestT extends Serializable, ResponseT extends Serializable> extends Thread implements SenderReceiver, DataHandler<RequestT, ResponseT>, AutoCloseable{
    public final int MAX_RESPONSES = 10;



    private int port;
    private DatagramChannel channel;
    private InetSocketAddress serverAddress;
    private ExecutorService receiverThreadPool;
    private ExecutorService senderThreadPool;
    private ExecutorService requestHandlerThreadPool;

    private Queue< RequestT> requestQueue;
    private Queue< ResponseT> responseQueue;

    private volatile boolean running;

    private Selector selector;

    private DataHandler<RequestT, ResponseT> dataHandler;
    private Logger logger;

    private void init(int local, String a, int p) throws ConnectionException {
        running = true;
        
        receiverThreadPool = Executors.newFixedThreadPool(MAX_RESPONSES);
        senderThreadPool = Executors.newFixedThreadPool(MAX_RESPONSES);
        requestHandlerThreadPool = Executors.newFixedThreadPool(MAX_RESPONSES);

        requestQueue = new ConcurrentLinkedQueue<>();
        responseQueue = new ConcurrentLinkedQueue<>();
        logger = new DefaultLogger("client");

        connect(local,a,p);
        setName("server thread");
        logger.trace("starting server");
    }

    private void connect(int local, String addr, int p) throws ConnectionException {
        try {
            try {
                serverAddress = new InetSocketAddress(InetAddress.getByName(addr), p);
            } catch (UnknownHostException e) {
                throw new InvalidAddressException();
            } catch (IllegalArgumentException e) {
                throw new InvalidPortException();
            }

            if (channel != null && channel.isOpen()) channel.close();
            port = p;
            channel = DatagramChannel.open();
            channel.configureBlocking(false);
            channel.bind(new InetSocketAddress(local));
            channel.connect(serverAddress);
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
    
    public Client(int local,String addr,int p) throws ConnectionException {
        init(local, addr,p);
    }

    /**
     * receives request from client
     *
     * @throws ConnectionException
     * @throws InvalidDataException
     */
    private void receive() throws ConnectionException, InvalidDataException {
        ByteBuffer buf = ByteBuffer.allocate(BUFFER_SIZE);
       
        RequestT request = null;
        try {

            if (channel.receive(buf) == null) return; //no data to read
            logger.trace("received response from server " + serverAddress.toString());
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

        requestQueue.offer( request);

    }

    /**
     * sends response
     *
     * @param clientAddress
     * @param response
     * @throws ConnectionException
     */
    public void send( ResponseT response) throws ConnectionException {
        if (serverAddress == null) throw new InvalidAddressException("no client address found");
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(BUFFER_SIZE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(response);
            channel.send(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()), serverAddress);
            logger.trace("sent response to server" + serverAddress.toString());
        } catch (IOException e) {
            throw new ConnectionException("something went wrong during sending response");
        }
    }

    public ResponseT handle(RequestT request) throws InvalidDataException{
        return null;
    }
    private void handleRequest( RequestT request) {
        if(dataHandler==null) dataHandler = this::handle;
        ResponseT ans = null;
        try{
            ans = dataHandler.handle(request);
            if(ans==null) throw new DataHandleException("data handler returned null");
            
        } catch (InvalidDataException e){
            logger.error("something went wrong during processing request:"+ e.getMessage());
        }      
        
    }

    public void addRequest(ResponseT request) {
        responseQueue.offer(request);
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

        public RequestHandler( RequestT requestEntry) {
            request = requestEntry;
        }

        public void run() {
            handleRequest( request);
        }
    }

    private class Sender implements Runnable {
        private final ResponseT response;

        public Sender( ResponseT responseEntry) {
            response = responseEntry;
        }

        public void run() {
            try {
                send(response);
            } catch (ConnectionException e) {
                logger.error(e.getMessage());
            }
        }
    }
}