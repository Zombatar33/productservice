package bookstore.productservice.port.product.exception;

public class NotAuthorizedException extends Exception {


    public NotAuthorizedException() {
        super("Not authorized.");
    }
}
