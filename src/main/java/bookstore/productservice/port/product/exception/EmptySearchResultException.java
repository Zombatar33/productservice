package bookstore.productservice.port.product.exception;

import java.util.UUID;

public class EmptySearchResultException extends Exception {
    public EmptySearchResultException () {
        super("Search result is empty.");
    }
}
