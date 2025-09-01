package finki.db.tasty_tabs.web.dto;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
@Data
public class OrderItemRequest {
    private Long productId;
    private int quantity;
}
