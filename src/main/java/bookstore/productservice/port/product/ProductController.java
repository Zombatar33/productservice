package bookstore.productservice.port.product;

import bookstore.productservice.core.domain.model.Product;
import bookstore.productservice.core.domain.service.implementation.ProductService;
import bookstore.productservice.core.domain.service.interfaces.IProductService;
import bookstore.productservice.port.product.exception.EmptySearchResultException;
import bookstore.productservice.port.product.exception.NoProductsException;
import bookstore.productservice.port.product.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public List<Product> getProducts() throws NoProductsException {
        List<Product> products = productService.getProducts();

        if (products == null || products.size() == 0) {
            throw new NoProductsException();
        }

        return products;
    }

    @GetMapping("/products/{id}")
    public Product getProduct(@PathVariable UUID id) throws ProductNotFoundException {
        Product product = productService.getProduct(id);

        if (product == null) {
            throw new ProductNotFoundException(id);
        }

        return product;
    }

    @GetMapping("/products/search/{query}")
    public List<Product> productSearch(@PathVariable String query) throws EmptySearchResultException {
        List<Product> searchResult = productService.getProducts(query);

        if (searchResult.size() == 0) {
            throw new EmptySearchResultException();
        }

        return searchResult;
    }

    //TODO: Find a way to test in PM
    @PostMapping("/products")
    public @ResponseBody Product createProduct (@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @DeleteMapping("/products/{id}")
    public void delete (@PathVariable UUID id) {
        productService.removeProduct(id);
    }

    //TODO: Find a way to test in PM
    @PutMapping(path="/products")
    public void update (@RequestBody Product product) {
        productService.updateProduct(product);
    }

}
