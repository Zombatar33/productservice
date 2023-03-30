package bookstore.productservice;

import bookstore.productservice.core.domain.model.Product;
import bookstore.productservice.core.domain.service.implementation.ProductService;
import bookstore.productservice.port.product.ProductController;
import bookstore.productservice.port.product.exception.ProductNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.ServletException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class ProductControllerTests {
    private static final String ADMIN_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJmaXJzdE5hbWUiOiJKb2huIiwibGFzdE5hbWUiOiJEb2UiLCJjb3VudHJ5IjoiVm9pZCIsInppcENvZGUiOiIxMDAwMDEiLCJhZGRyZXNzIjoiTWFpbiBTdCAxMiIsInJvbGUiOiJBRE1JTiIsImNpdHkiOiJBbW9ndXMiLCJ1c2VyaWQiOiI1NDdkYmQxMy0zZDIxLTQ2YjktYWMxZC05ZjEzZjc1ZDIzNGUiLCJzdWIiOiJhZG1pbi51c2VyQG5pY2UuZGUiLCJpYXQiOjE2ODAxMzA1NzEsImV4cCI6OTY4MDE1MjE3MX0.81vOS_RtV7pRruls1XxPI2tvHRQMGKVjgDIHrpLTjsI";

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;

    private UUID userId;
    private String token;

    TestRoleInterceptor testInterceptor = new TestRoleInterceptor("ADMIN", "3979244226452948404D6351665468576D5A7134743777217A25432A462D4A614E645267556B586E3272357538782F413F4428472B4B6250655368566D5971337336763979244226452948404D635166546A576E5A7234753777217A25432A462D4A614E645267556B58703273357638792F413F4428472B4B6250655368566D");


    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .addInterceptors(testInterceptor)
                .build();

        userId = UUID.randomUUID();

        HashMap<String, Object> claims = new HashMap<>();
        claims.put("role", "ADMIN");
        claims.put("userid", userId);

        token = "Bearer " + Jwts.builder()
                .setSubject(userId.toString())
                .setClaims(claims)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, Keys.hmacShaKeyFor("3979244226452948404D6351665468576D5A7134743777217A25432A462D4A614E645267556B586E3272357538782F413F4428472B4B6250655368566D5971337336763979244226452948404D635166546A576E5A7234753777217A25432A462D4A614E645267556B58703273357638792F413F4428472B4B6250655368566D".getBytes()))
                .compact();
    }

    public Product setupProduct(UUID uuid, String isbn) {
        UUID id = UUID.randomUUID();
        String isbn13 = "9780141396316";
        if (uuid != null) {
            id = uuid;
        }
        if (isbn != "") {
            isbn13 = isbn;
        }

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
    public void testCreateCart() throws Exception {
        List<Product> products = new ArrayList<>();
        Product product = setupProduct(null, "");
        products.add(product);
        Mockito.when(productService.getProducts()).thenReturn(products);

        mockMvc.perform(get("/api/v1/products")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    public void testGetExistingProduct() throws Exception {
        UUID id = UUID.fromString("ebfdddf4-25e7-4591-94fe-df177b989efe");
        Product product = setupProduct(id, "");
        Mockito.when(productService.getProduct(id)).thenReturn(product);

        mockMvc.perform(get("/api/v1/products/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(product.getId().toString()))
                .andExpect(jsonPath("$.title").value(product.getTitle()));
    }

    @Test(expected = ServletException.class)
    public void testGetNonExistingProduct() throws Exception {
        UUID id = UUID.fromString("4bd123f0-3abd-4a97-8b63-63a4cf264123");
        Mockito.when(productService.getProduct(id)).thenReturn(null);

        mockMvc.perform(get("/api/v1/products/" + id))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException().getCause() instanceof ProductNotFoundException))
                .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"))
                .andExpect(content().string("There is no product with the given id."));
    }

    @Test(expected = ServletException.class)
    public void testCreateProductNotAdmin() throws Exception {
        UUID id = UUID.randomUUID();
        Product product = setupProduct(id, "");
        String productJson = new ObjectMapper().writeValueAsString(product);

        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Not authorized."));

        assertNull(productService.getProduct(id));
    }

    @Test
    public void testCreateProductAdmin() throws Exception {
        UUID id = UUID.randomUUID();
        Product product = setupProduct(id, "");
        String productJson = new ObjectMapper().writeValueAsString(product);

        mockMvc.perform(post("/api/v1/products")
                .header("Authorization", "Bearer " + ADMIN_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson))
                .andExpect(status().isOk());
    }


    @Test(expected = ServletException.class)
    public void testDeleteExistingProductNotAdmin() throws Exception {
        UUID id = UUID.randomUUID();
        Product product = setupProduct(id, "");
        mockMvc.perform(delete("/api/v1/products/" + id))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Not authorized."));

    }

    @Test(expected = ServletException.class)
    public void testDeleteNonExistingProductNotAdmin() throws Exception {
        UUID id = UUID.randomUUID();
        mockMvc.perform(delete("/api/v1/products/" + id))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Not authorized."));
    }

    @Test
    public void testDeleteExistingProductAdmin() throws Exception {
        UUID id = UUID.randomUUID();
        Product product = setupProduct(id, "");
        Mockito.when(productService.getProduct(id)).thenReturn(product);

        mockMvc.perform(delete("/api/v1/products/" + id)
                .header("Authorization", "Bearer " + ADMIN_TOKEN))
                .andExpect(status().isOk());

        assertEquals(product, productService.getProduct(id));
    }

    @Test
    public void testDeleteNonExistingProductAdmin() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.when(productService.getProduct(id)).thenReturn(null);

        mockMvc.perform(delete("/api/v1/products/" + id)
                .header("Authorization", "Bearer " + ADMIN_TOKEN))
                .andExpect(status().isOk());

        assertNull(productService.getProduct(id));
    }



    @Test(expected = ServletException.class)
    public void testUpdateProductNoAdmin() throws Exception {
        mockMvc.perform(put("/api/v1/products"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Not authorized."));
    }

    @Test
    public void testUpdateProductAdmin() throws Exception {
        UUID id = UUID.randomUUID();
        Product productOld = setupProduct(id, "");
        String productOldJson = new ObjectMapper().writeValueAsString(productOld);

        mockMvc.perform(put("/api/v1/products")
                .header("Authorization", "Bearer " + ADMIN_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(productOldJson))
                .andExpect(status().isOk());
    }

    @Test(expected = ServletException.class)
    public void testAddStockNoAdmin() throws Exception {
        UUID id = UUID.fromString("4bd783f0-3abd-4a97-8b63-63a4cf2643d0");
        Product product = setupProduct(id, "");
        int quantity = 10;

        mockMvc.perform(post("/api/v1/stock/" + id + "/" + quantity))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Not authorized."));
    }

    @Test
    public void testAddStockAdmin() throws Exception {
        UUID id = UUID.fromString("4bd783f0-3abd-4a97-8b63-63a4cf2643d0");
        Product product = setupProduct(id, "");
        int quantity = 10;

        mockMvc.perform(post("/api/v1/stock/" + id + "/" + quantity)
                .header("Authorization", "Bearer " + ADMIN_TOKEN))
                .andExpect(status().isOk());

        Mockito.verify(productService, Mockito.times(1)).addStock(id, 10);

    }


    @Test
    public void testGetStockExistingProduct() throws Exception {
        UUID id = UUID.randomUUID();
        Product product = setupProduct(id, "");
        Mockito.when(productService.getStock(id)).thenReturn(50);

        int expectedStock = 50;

        mockMvc.perform(get("/api/v1/stock/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").value(expectedStock));
    }

    @Test
    public void testGetStockNonExistingProduct() throws Exception {
        UUID id = UUID.fromString("4bd783f0-3abd-4a97-8b63-63a4cf264123");

        mockMvc.perform(get("/api/v1/stock/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").value(0));
    }

    @Test
    public void testSearchExistingProduct() throws Exception {
        UUID id = UUID.randomUUID();
        List<Product> products = new ArrayList<>();
        Product product = setupProduct(null, "1234567890");
        products.add(product);
        String query = "1234567890";
        Mockito.when(productService.searchProduct(query)).thenReturn(products);

        mockMvc.perform(get("/api/v1/products/search/" + query))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[0].title").value("The Tragedy of Macbeth"))
                .andExpect(jsonPath("$.[0].isbn13").value("1234567890"))
                .andExpect(jsonPath("$.[0].stock").value(50));
    }

}