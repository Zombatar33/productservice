package bookstore.productservice.core.domain.service.implementation;

import bookstore.productservice.core.domain.model.Product;
import bookstore.productservice.core.domain.service.interfaces.IProductRepository;
import bookstore.productservice.core.domain.service.interfaces.IProductService;
import bookstore.productservice.port.product.exception.EmptySearchResultException;
import bookstore.productservice.port.product.exception.ProductAlreadyExistsException;
import bookstore.productservice.port.product.exception.ProductNotFoundException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ProductService implements IProductService {

    @Autowired
    @Setter
    private IProductRepository productRepository;

    @Value("${stripe.apiKey}")
    private String stripeApiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }


    @Override
    public Product createProduct(Product product) throws ProductAlreadyExistsException {
        if (productRepository.findByIsbn13(product.getIsbn13()) == null) {
            Product savedProduct = productRepository.save(product);
            long priceInCents = (long) product.getPrice() * 100;
            addProductToStripe(
                    savedProduct.getId(),
                    savedProduct.getTitle(),
                    savedProduct.getDescription(),
                    priceInCents,
                    "eur"
            );
            return savedProduct;
        }
        throw new ProductAlreadyExistsException();
    }

    @Override
    public Product getProduct(UUID uuid) {
        return productRepository.findById(uuid).orElse(null);
    }

    @Override
    public List<Product> getProducts() {
        return productRepository.findAll();
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
            int tempStock = tempProduct.getStock();
            tempProduct.setStock(tempStock+quantity);
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

    @Override
    public List<Product> searchProduct(String query) throws EmptySearchResultException {
        if (!productRepository.findByTitleContainingIgnoreCase(query).isEmpty()) {
            return productRepository.findByTitleContainingIgnoreCase(query);
        } else if (!productRepository.findByIsbn13ContainingIgnoreCase(query).isEmpty()) {
            return productRepository.findByIsbn13ContainingIgnoreCase(query);
        }
        throw new EmptySearchResultException();
    }

    public void addProductToStripe(UUID id, String productName, String description, long priceInCents, String currency) {

        String idAsString = id.toString();

        com.stripe.model.Product product = null;
        try {
            ProductCreateParams params = ProductCreateParams.builder()
                    .setId(idAsString)
                    .setName(productName)
                    .setDescription(description)
                    .setActive(true)
                    .build();

            product = com.stripe.model.Product.create(params);

            PriceCreateParams priceParams = PriceCreateParams.builder()
                    .setProduct(product.getId())
                    .setUnitAmount(priceInCents)
                    .setCurrency(currency)
                    .build();

            Price price = Price.create(priceParams);
            Map<String, Object> updatedParams = new HashMap<>();
            updatedParams.put("default_price", price.getId());
            product.update(updatedParams);

        } catch (StripeException e) {
            e.printStackTrace();
        }
    }
}
