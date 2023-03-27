package bookstore.productservice.core.domain.service.interfaces;

import bookstore.productservice.core.domain.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

public interface IProductRepository extends CrudRepository<Product, UUID> {

    List<Product> findByTitle(String title);

    Product findByIsbn13(String isbn13);

    List<Product> findAll();

    List<Product> findByTitleContainingIgnoreCase(String title);

    List<Product> findByIsbn13ContainingIgnoreCase(String isbn13);

}
