package bookstore.productservice.core.domain.service.interfaces;

import bookstore.productservice.core.domain.model.Product;
import bookstore.productservice.port.product.exception.EmptySearchResultException;
import bookstore.productservice.port.product.exception.ProductAlreadyExistsException;
import bookstore.productservice.port.product.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface IProductService {

    public Product createProduct(Product product) throws ProductAlreadyExistsException;

    public Product getProduct(UUID id);

    public List<Product> getProducts();

    public void updateProduct(Product product);

    public void removeProduct(UUID id);

    public void addStock(UUID id, int quantity) throws ProductNotFoundException;

    public int getStock(UUID id) throws ProductNotFoundException;

    public List<Product> searchProduct(String query) throws EmptySearchResultException;



}
