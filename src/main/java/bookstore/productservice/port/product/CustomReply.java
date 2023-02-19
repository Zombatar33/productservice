package bookstore.productservice.port.product;

import lombok.*;

import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomReply {

    private UUID productId;
    private int quantity;
    private boolean inStock;

}
