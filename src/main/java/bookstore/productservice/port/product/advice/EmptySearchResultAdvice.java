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
    @ResponseStatus(HttpStatus.OK) // 200 makes more sense because the request is (hopefully) valid, and "nothing" is still a valid response
    String productNotFoundHandler(EmptySearchResultException exception){
        return exception.getMessage();
    }

}
