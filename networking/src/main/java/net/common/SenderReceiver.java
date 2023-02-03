package net.common;

public interface SenderReceiver <Request, Response>{
    public void send(Request request);
    public Response receive();
    public final int BUFFER_SIZE = 1024;
}
