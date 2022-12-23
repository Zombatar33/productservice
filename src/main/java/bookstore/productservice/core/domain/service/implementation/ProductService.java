package bookstore.productservice.core.domain.service.implementation;

import bookstore.productservice.core.domain.model.Product;
import bookstore.productservice.core.domain.service.interfaces.IProductRepository;
import bookstore.productservice.core.domain.service.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService implements IProductService {

    @Autowired
    private IProductRepository productRepository;


    @Override
    public Product createProduct(Product product) {
         return productRepository.save(product);
    }

    @Override
    public Product getProduct(UUID uuid) {
        return productRepository.findById(uuid).orElseGet(null);
    }

    @Override
    public Product getProduct(String isbn13) {
        return productRepository.findByIsbn13(isbn13);
    }

    @Override
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProducts(String searchQuery) {
        return productRepository.findByTitle(searchQuery); //TODO: only searches via title right now
    }


    @Override
    public void updateProduct(Product product) {
        boolean exists = productRepository.existsById(product.getId());
        if (exists) {
            productRepository.deleteById(product.getId());
            productRepository.save(product);
        }
    }

    @Override
    public void removeProduct(UUID id) {
        productRepository.deleteById(id);
    }
}
