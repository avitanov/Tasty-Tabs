package finki.db.tasty_tabs.web.dto;
public record CreateOrderItemDto(
        Long productId,
        Integer quantity,
        Double price,
        Boolean isProcessed
) { }
