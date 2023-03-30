package bookstore.productservice;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.*;

import bookstore.productservice.core.domain.service.implementation.ProductService;
import jakarta.validation.ConstraintViolationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import bookstore.productservice.core.domain.model.Product;
import bookstore.productservice.core.domain.service.interfaces.IProductRepository;
import bookstore.productservice.port.product.exception.EmptySearchResultException;
import bookstore.productservice.port.product.exception.ProductAlreadyExistsException;
import bookstore.productservice.port.product.exception.ProductNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTests {

    @Mock
    private IProductRepository productRepository;

    private ProductService productService;

    @Before
    public void setUp() {
        productService = new ProductService();
        productService.setProductRepository(productRepository);
    }

    public Product setupProduct(UUID uuid) {
        UUID id = UUID.randomUUID();
        if (uuid != null) {
            id = uuid;
        }
        String isbn13 = "9780141396316";
        String title = "The Tragedy of Macbeth";
        String version = "1st";
        String[] authors = new String[]{"William Shakespeare"};
        Date publishingDate = new Date();
        String publishingHouse = "Penguin Classics";
        String description = "The Tragedy of Macbeth is a play by William Shakespeare about a regicide and its aftermath. It is Shakespeare's shortest tragedy and is believed to have been written sometime between 1603 and 1607.";
        String language = "English";
        int pages = 144;
        String coverUrl = "https://images-na.ssl-images-amazon.com/images/I/41RZGv1WcwL._SX331_BO1,204,203,200_.jpg";
        float price = 12.99f;
        int stock = 50;
        return new Product(id, isbn13, title, version, authors, publishingDate, publishingHouse, description, language, pages, coverUrl, price, stock);
    }

    @Test
    public void testCreateProductCorrectInput() throws ProductAlreadyExistsException {
        UUID productId = UUID.randomUUID();
        String isbn13 = "9780140714548";
        String title = "The Great Gatsby";
        String version = "First Edition";
        String[] authors = new String[]{"F. Scott Fitzgerald"};
        Date publishingDate = new Date();
        String publishingHouse = "Charles Scribner's Sons";
        String description = "The story primarily concerns the young and mysterious millionaire Jay Gatsby";
        String language = "English";
        int pages = 218;
        String coverUrl = "great-gatsby.jpg";
        float price = 9.99f;
        int stock = 100;

        Product product = new Product(productId, isbn13, title, version, authors, publishingDate,
                publishingHouse, description, language, pages, coverUrl, price, stock);

        when(productRepository.findByIsbn13(isbn13)).thenReturn(null);
        when(productRepository.save(product)).thenReturn(product);

        Product createdProduct = productService.createProduct(product);

        assertEquals(productId, createdProduct.getId());
        assertEquals(isbn13, createdProduct.getIsbn13());
        assertEquals(title, createdProduct.getTitle());
        assertEquals(version, createdProduct.getVersion());
        assertArrayEquals(authors, createdProduct.getAuthors());
        assertEquals(publishingDate, createdProduct.getPublishingDate());
        assertEquals(publishingHouse, createdProduct.getPublishingHouse());
        assertEquals(description, createdProduct.getDescription());
        assertEquals(language, createdProduct.getLanguage());
        assertEquals(pages, createdProduct.getPages());
        assertEquals(coverUrl, createdProduct.getCoverUrl());
        assertEquals(price, createdProduct.getPrice(), 0.0);
        assertEquals(stock, createdProduct.getStock());
        verify(productRepository).findByIsbn13(isbn13);
        verify(productRepository).save(product);
    }

    @Test
    public void testCreateProductWithExistingIsbn13() {
        Product firstProduct = setupProduct(null);
        Product secondProduct = setupProduct(null);
        when(productRepository.findByIsbn13(firstProduct.getIsbn13())).thenReturn(firstProduct);

        assertThrows(ProductAlreadyExistsException.class, () -> productService.createProduct(secondProduct));
        verify(productRepository, never()).save(secondProduct);
    }

    @Test
    public void testGetProductWithExistingId() {
        UUID uuid = UUID.randomUUID();
        Product product = setupProduct(uuid);
        when(productRepository.findById(uuid)).thenReturn(Optional.of(product));

        assertEquals(Optional.of(product), productRepository.findById(uuid));
    }

    @Test
    public void testGetProductWithNonExistingId() {
        UUID uuid = UUID.randomUUID();
        when(productRepository.findById(uuid)).thenReturn(Optional.empty());

        Product product = productService.getProduct(uuid);

        assertNull(product);
    }

    @Test
    public void testGetProductsWithProducts() {
        List<Product> products = new ArrayList<>();
        products.add(setupProduct(null));
        products.add(setupProduct(null));
        products.add(setupProduct(null));

        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getProducts();

        assertEquals(products, result);
    }

    @Test
    public void testGetProductsWithNoProducts() {
        when(productRepository.findAll()).thenReturn(null);

        assertNull(productRepository.findAll());
    }

    @Test
    public void testUpdateProductWithExistingId() {
        UUID uuid = UUID.randomUUID();
        Product product = setupProduct(uuid);
        when(productRepository.existsById(uuid)).thenReturn(true);

        productService.updateProduct(product);

        verify(productRepository, times(1)).deleteById(uuid);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testUpdateProductWithNonExistingId() {
        UUID uuid = UUID.randomUUID();
        Product product = setupProduct(uuid);
        when(productRepository.existsById(uuid)).thenReturn(false);

        productService.updateProduct(product);

        verify(productRepository, never()).deleteById(uuid);
        verify(productRepository, never()).save(product);
    }

    @Test
    public void testRemoveProductWithExistingId() {
        UUID uuid = UUID.randomUUID();
        Product product = setupProduct(uuid);

        productService.removeProduct(uuid);

        verify(productRepository, times(1)).deleteById(uuid);
    }

    @Test
    public void testRemoveProductWithNonExistingId() {
        UUID uuid = UUID.randomUUID();
        productService.removeProduct(uuid);

        verify(productRepository, times(1)).deleteById(uuid);
    }

    @Test
    public void testAddStockWithExistingProduct() throws ProductNotFoundException {
        UUID uuid = UUID.randomUUID();
        Product product = setupProduct(uuid);
        when(productRepository.existsById(uuid)).thenReturn(true);
        when(productRepository.findById(uuid)).thenReturn(Optional.of(product));

        productService.addStock(uuid, 50);

        verify(productRepository, times(1)).findById(uuid);
        verify(productRepository, times(1)).save(product);
        assertEquals(product.getStock(), 100);
    }

    @Test
    public void testAddStockWithNonExistingProduct() throws ProductNotFoundException {
        UUID uuid = UUID.randomUUID();
        when(productRepository.existsById(uuid)).thenReturn(false);

        assertThrows(ProductNotFoundException.class, () -> productService.addStock(uuid, 50));
    }

    @Test
    public void testGetStock() throws ProductNotFoundException {
        UUID uuid = UUID.randomUUID();
        Product product = setupProduct(uuid);

        when(productRepository.existsById(uuid)).thenReturn(true);
        when(productRepository.findById(uuid)).thenReturn(Optional.of(product));

        int stock = productService.getStock(uuid);

        assertEquals(stock, 50);
    }

    @Test(expected = ProductNotFoundException.class)
    public void testGetStockThrowsProductNotFoundException() throws ProductNotFoundException {
        UUID productId = UUID.randomUUID();

        when(productRepository.existsById(productId)).thenReturn(false);

        productService.getStock(productId);
    }

    @Test
    public void testSearchProductByName() throws EmptySearchResultException {
        List<Product> products = new ArrayList<>();
        Product product = setupProduct(null);
        products.add(product);

        when(productRepository.findByTitleContainingIgnoreCase("The Tragedy of Macbeth")).thenReturn(products);

        List<Product> result = productService.searchProduct("The Tragedy of Macbeth");

        assertEquals(result.size(), 1);
        assertEquals(result.get(0), product);
    }

    @Test
    public void testSearchProductByIsbn13() throws EmptySearchResultException {
        List<Product> products = new ArrayList<>();
        Product product = setupProduct(null);
        products.add(product);

        when(productRepository.findByIsbn13ContainingIgnoreCase("9780141396316")).thenReturn(products);

        List<Product> result = productService.searchProduct("9780141396316");

        assertEquals(result.size(), 1);
        assertEquals(result.get(0), product);
    }

    @Test(expected = EmptySearchResultException.class)
    public void testSearchProductThrowsEmptySearchResultException() throws EmptySearchResultException {
        when(productRepository.findByTitleContainingIgnoreCase("Romeo and Juliet")).thenReturn(new ArrayList<>());
        when(productRepository.findByIsbn13ContainingIgnoreCase("Romeo and Juliet")).thenReturn(new ArrayList<>());

        productService.searchProduct("Romeo and Juliet");
    }
}

