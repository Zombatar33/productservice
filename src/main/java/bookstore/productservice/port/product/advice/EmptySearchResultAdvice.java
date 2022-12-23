package bookstore.productservice.port.product.advice;

import bookstore.productservice.port.product.exception.EmptySearchResultException;
import bookstore.productservice.port.product.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class EmptySearchResultAdvice {

    @ResponseBody
    @ExceptionHandler(value = EmptySearchResultException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    String productNotFoundHandler(EmptySearchResultException exception){
        return exception.getMessage();
    }

}
