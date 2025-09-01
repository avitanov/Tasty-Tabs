package finki.db.tasty_tabs.web.dto;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;


@Data
public class CreateOnlineOrderRequest {
    private Long customerId;
    private String deliveryAddress;
    private List<OrderItemRequest> items;
}