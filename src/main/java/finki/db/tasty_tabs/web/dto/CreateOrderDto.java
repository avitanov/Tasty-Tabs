package finki.db.tasty_tabs.web.dto;

import java.util.List;

public record CreateOrderDto(
        List<CreateOrderItemDto> orderItems,
        String status,
        String type, // "online" or "tab"
        String deliveryAddress, // For OnlineOrder
        Integer tableNumber // For TabOrder
) { }
