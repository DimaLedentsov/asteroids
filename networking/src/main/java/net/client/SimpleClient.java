package net.client;



import net.exceptions.*;
import net.logwrapper.DefaultLogger;
import net.logwrapper.Logger;
import net.common.*;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;



/**
 * client class
 */
public class SimpleClient<RequestT,ResponseT> extends Thread implements SenderReceiver,  AutoCloseable {
    private SocketAddress address;
    private DatagramSocket socket;
    public final int MAX_TIME_OUT = 1000;
    public final int MAX_ATTEMPTS = 3;
    private boolean running;
    private Logger logger;
    private DataHandler<ResponseT, RequestT> dataHandler;
    /**
     * initialize client
     *
     * @param addr Address
     * @param p port
     * @throws ConnectionException
     */
    private void init(String addr, int p) throws ConnectionException {
        connect(addr, p);
        running = true;
        logger = new DefaultLogger("");
        setName("client thread");
    }

    public SimpleClient(String addr, int p) throws ConnectionException {
        init(addr, p);
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    /**
     * connects client to server
     *
     * @param addr Address
     * @param p port
     * @throws ConnectionException
     */
    public void connect(String addr, int p) throws ConnectionException {
        try {
            address = new InetSocketAddress(InetAddress.getByName(addr), p);
        } catch (UnknownHostException e) {
            throw new InvalidAddressException();
        } catch (IllegalArgumentException e) {
            throw new InvalidPortException();
        }
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(MAX_TIME_OUT);
        } catch (IOException e) {
            throw new ConnectionException("cannot open socket");
        }
    }

    /**
     * sends request to server
     *
     * @param request request
     * @throws ConnectionException
     */
    public void send(RequestT request) throws ConnectionException {
        try {
            
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(BUFFER_SIZE);
            ObjectOutputStream objOutput = new ObjectOutputStream(byteArrayOutputStream);
            objOutput.writeObject(request);
            DatagramPacket requestPacket = new DatagramPacket(byteArrayOutputStream.toByteArray(), byteArrayOutputStream.size(), address);
            socket.send(requestPacket);
            byteArrayOutputStream.close();
        } catch (IOException e) {
            throw new ConnectionException("something went wrong while sending request");
        }
    }

    /**
     * receive message from server
     *
     * @return response
     * @throws ConnectionException
     * @throws InvalidDataException
     */
    public ResponseT receive() throws ConnectionException, InvalidDataException {

        ByteBuffer bytes = ByteBuffer.allocate(BUFFER_SIZE);
        DatagramPacket receivePacket = new DatagramPacket(bytes.array(), bytes.array().length);
        try {
            socket.receive(receivePacket);
        } catch (SocketTimeoutException e) {
            for (int attempts = MAX_ATTEMPTS; attempts > 0; attempts--) {
                logger.error("server response timeout exceeded, trying to reconnect. " + attempts + " attempts left");
                try {
                    socket.receive(receivePacket);
                    break;
                } catch (IOException ignored) {

                }
            }

            throw new ConnectionTimeoutException();
        } catch (IOException e) {
            throw new ConnectionException("something went wrong while receiving response");
        }

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes.array()));
            return (ResponseT) objectInputStream.readObject();
        } catch (ClassNotFoundException | ClassCastException | IOException e) {
            throw new InvalidReceivedDataException();
        }
    }

    /**
     * runs client until interrupt
     */
    @Override
    public void run() {

        while (running) {
            try {
                ResponseT resp = receive();
                RequestT request = dataHandler.handle(resp);
                send(request);
            } catch (ConnectionException | InvalidDataException e) {
                logger.error(e.getMessage());
            }
        }
    }

    /**
     * close client
     */
    public void close() {
        running = false;

        socket.close();
    }
}