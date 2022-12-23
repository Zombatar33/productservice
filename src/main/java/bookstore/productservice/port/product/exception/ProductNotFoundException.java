package bookstore.productservice.port.product.exception;

import java.util.UUID;

public class ProductNotFoundException extends Exception{

    public ProductNotFoundException (UUID isbn) {
        super("There is no product with the ISBN "+ isbn);
    }

}
