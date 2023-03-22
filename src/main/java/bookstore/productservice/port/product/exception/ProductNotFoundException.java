package bookstore.productservice.port.product.exception;

import java.util.UUID;

public class ProductNotFoundException extends Exception{

    public ProductNotFoundException () {
        super("There is no product with the given id.");
    }

}
