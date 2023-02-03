package net.common;
import net.exceptions.InvalidDataException;
public interface DataHandler <RequestT, ResponseT> {
    public ResponseT handle(RequestT request) throws InvalidDataException;
}
