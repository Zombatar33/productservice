package bookstore.productservice.port.product.advice;

import bookstore.productservice.port.product.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ProductNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(value = ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    String productNotFoundHandler(ProductNotFoundException exception){
        return exception.getMessage();
    }

}
