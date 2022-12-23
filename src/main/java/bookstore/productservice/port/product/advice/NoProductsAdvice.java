package bookstore.productservice.port.product.advice;

import bookstore.productservice.port.product.exception.NoProductsException;
import bookstore.productservice.port.product.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class NoProductsAdvice {

    @ResponseBody
    @ExceptionHandler(value = NoProductsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    String productNotFoundHandler(NoProductsException exception){
        return exception.getMessage();
    }

}
