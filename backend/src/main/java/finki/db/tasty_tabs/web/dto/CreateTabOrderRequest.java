package finki.db.tasty_tabs.web.dto;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
@Data
public class CreateTabOrderRequest {
    private Integer tableNumber;
    private Long frontStaffId;
    private List<OrderItemRequest> items;
}