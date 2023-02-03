package net.common;
import net.exceptions.*;
import java.net.InetSocketAddress;

public interface SenderReceiver{
   
    public final int BUFFER_SIZE = 1024;
}
/*public interface SenderReceiver <Request, Response>{
    public void send(InetSocketAddress clientAddress,Request request) throws ConnectionException, InvalidDataException;
    public Response receive() throws ConnectionException, InvalidDataException;
    public final int BUFFER_SIZE = 1024;
}
*/