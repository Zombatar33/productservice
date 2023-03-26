package bookstore.productservice.port.product;

import bookstore.productservice.core.domain.model.Product;
import bookstore.productservice.core.domain.service.implementation.ProductService;
import bookstore.productservice.port.product.exception.EmptySearchResultException;
import bookstore.productservice.port.product.exception.NoProductsException;
import bookstore.productservice.port.product.exception.ProductAlreadyExistsException;
import bookstore.productservice.port.product.exception.ProductNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class ProductController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ProductService productService;

    @GetMapping("products")
    public ResponseEntity<List<Product>> getProducts(HttpServletRequest request) throws Exception {
        List<Product> products = productService.getProducts();

        if (products == null || products.size() == 0) {
            throw new NoProductsException();
        }

        return ResponseEntity.ok(products);
    }

    @GetMapping("products/{id}")
    public Product getProduct(@PathVariable UUID id) throws ProductNotFoundException {
        Product product = productService.getProduct(id);

        if (product == null) {
            throw new ProductNotFoundException();
        }

        return product;
    }

    @GetMapping("products/search/{query}")
    public List<Product> productSearch(@PathVariable String query) throws EmptySearchResultException {
        List<Product> searchResult = productService.getProductsBySearch(query);

        if (searchResult.size() == 0) {
            throw new EmptySearchResultException();
        }

        return searchResult;
    }

    @PostMapping("products")
    public @ResponseBody Product createProduct (@RequestBody Product product) throws ProductAlreadyExistsException {
        return productService.createProduct(product);
    }

    @DeleteMapping("products/{id}")
    public void delete (@PathVariable UUID id) {
        productService.removeProduct(id);
    }

    @PutMapping(path="products")
    public void update (@RequestBody Product product) {
        productService.updateProduct(product);
    }

    @PostMapping("stock/{id}/{quantity}")
    public void addStock(@PathVariable(name = "id") UUID id, @PathVariable(name = "quantity") int quantity) throws ProductNotFoundException {
        productService.addStock(id, quantity);
    }

    @GetMapping("stock/{id}")
    public int getStock(@PathVariable(name = "id") UUID id) throws ProductNotFoundException {
        return productService.getStock(id);
    }

    /*
    @RabbitListener(queues =  "#{queue.name}", concurrency = "5")
    public CustomReply checkStock(CustomMessage msg) {
        UUID id = msg.getProductId();
        int quantity = msg.getQuantity();
        Product product = productService.getProduct(id);

        if (product == null) {
            return null;
        }
        return CustomReply.builder()
                .productId(id)
                .quantity(quantity)
                .inStock(quantity < product.getStock())
                .build();
    }

     */

}
