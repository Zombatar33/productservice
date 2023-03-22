package bookstore.productservice.core.domain.service.implementation;

import bookstore.productservice.core.domain.model.Product;
import bookstore.productservice.core.domain.service.interfaces.IProductRepository;
import bookstore.productservice.core.domain.service.interfaces.IProductService;
import bookstore.productservice.port.product.exception.ProductAlreadyExistsException;
import bookstore.productservice.port.product.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService implements IProductService {

    @Autowired
    private IProductRepository productRepository;


    @Override
    public Product createProduct(Product product) throws ProductAlreadyExistsException {
        if (productRepository.findByIsbn13(product.getIsbn13()) == null) {
            return productRepository.save(product);
        }
        throw new ProductAlreadyExistsException();
    }

    @Override
    public Product getProduct(UUID uuid) {
        return productRepository.findById(uuid).orElse(null);
    }

    @Override
    public Product getProductByISBN(String isbn13) {
        return productRepository.findByIsbn13(isbn13);
    }

    @Override
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsBySearch(String searchQuery) {
        return productRepository.findByTitle(searchQuery); //only searches via title right now
    }


    @Override
    public void updateProduct(Product product) {
        if (productRepository.existsById(product.getId())) {
            productRepository.deleteById(product.getId());
            productRepository.save(product);
        }
    }

    @Override
    public void removeProduct(UUID id) {
        productRepository.deleteById(id);
    }

    @Override
    public void addStock(UUID id, int quantity) throws ProductNotFoundException {
        if (productRepository.existsById(id)) {
            Product tempProduct = productRepository.findById(id).get();
            tempProduct.setStock(quantity);
            productRepository.save(tempProduct);
            return;
        }
        throw new ProductNotFoundException();
    }

    @Override
    public int getStock(UUID id) throws ProductNotFoundException {
        if (productRepository.existsById(id)) {
            Product tempProduct = productRepository.findById(id).get();
            return tempProduct.getStock();
        }
        throw new ProductNotFoundException();
    }
}
