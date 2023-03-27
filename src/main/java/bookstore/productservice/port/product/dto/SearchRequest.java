package bookstore.productservice.port.product.dto;

import lombok.Data;

@Data
public class SearchRequest {
    private String query;
}
