package bookstore.productservice.port.product.exception;

public class ProductAlreadyExistsException extends Exception {


    public ProductAlreadyExistsException() {
        super("Product already exists.");
    }
}
