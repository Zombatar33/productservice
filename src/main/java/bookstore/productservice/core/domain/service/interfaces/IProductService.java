package bookstore.productservice.core.domain.service.interfaces;

import bookstore.productservice.core.domain.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface IProductService {

    public Product createProduct(Product product);

    public Product getProduct(UUID uuid);

    public Product getProduct(String isbn13);

    public List<Product> getProducts();

    public List<Product> getProducts(String searchQuery);

    public void updateProduct(Product product);

    public void removeProduct(UUID id);

}
