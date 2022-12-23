package bookstore.productservice.port.product.exception;

public class NoProductsException extends Exception {


    public NoProductsException() {
        super("There are no products available.");
    }
}
