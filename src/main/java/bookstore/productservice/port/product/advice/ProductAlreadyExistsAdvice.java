package bookstore.productservice.port.product.advice;

import bookstore.productservice.port.product.exception.ProductAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ProductAlreadyExistsAdvice {

    @ResponseBody
    @ExceptionHandler(value = ProductAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String productNotFoundHandler(ProductAlreadyExistsException exception){
        return exception.getMessage();
    }

}
