package bookstore.productservice.core.domain.service.interfaces;

import bookstore.productservice.core.domain.model.Product;

import java.util.UUID;

//Schnittstelle zur Datenbank, CRUD
public interface IProductService {

    public Product createProduct(Product product);

    public Product getProduct(UUID uuid);

    public Product getProduct(String isbn13);

    public Product[] getProducts(String searchQuery);

    public Product updateProduct(Product product);

    public boolean removeProduct(Product product);

}
