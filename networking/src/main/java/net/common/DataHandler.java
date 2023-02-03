public interface RequestHandler <RequestT, ResponseT> {
    public ResponseT handle(RequestT request);
}
