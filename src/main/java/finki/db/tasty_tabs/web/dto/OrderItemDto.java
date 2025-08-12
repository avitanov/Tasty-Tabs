package finki.db.tasty_tabs.web.dto;


import finki.db.tasty_tabs.entity.OrderItem;
import java.time.LocalDateTime;

public record OrderItemDto(
        Long id,
        Integer quantity,
        Double price,
        Boolean isProcessed,
        LocalDateTime timestamp,
        Long productId
) {
    public static OrderItemDto from(OrderItem orderItem) {
        return new OrderItemDto(
                orderItem.getId(),
                orderItem.getQuantity(),
                orderItem.getPrice(),
                orderItem.getIsProcessed(),
                orderItem.getTimestamp(),
                orderItem.getProduct().getId()
        );
    }
}
