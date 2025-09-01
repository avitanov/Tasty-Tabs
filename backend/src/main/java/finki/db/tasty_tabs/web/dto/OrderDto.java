package finki.db.tasty_tabs.web.dto;

import finki.db.tasty_tabs.entity.OnlineOrder;
import finki.db.tasty_tabs.entity.Order;
import finki.db.tasty_tabs.entity.TabOrder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record OrderDto(
        Long id,
        LocalDateTime timestamp,
        String status,
        String type,
        String customerName,
        String deliveryAddress,
        Integer tableNumber,
        String frontStaffName,
        List<OrderItemDto> orderItems
) {
    public static OrderDto from(Order order) {
        if (order instanceof OnlineOrder onlineOrder) {
            return new OrderDto(
                    onlineOrder.getId(),
                    onlineOrder.getTimestamp(),
                    onlineOrder.getStatus(),
                    "ONLINE",
                    onlineOrder.getCustomer().getEmail(),
                    onlineOrder.getDeliveryAddress(),
                    null,
                    null,
                    onlineOrder.getOrderItems().stream()
                            .map(OrderItemDto::from)
                            .collect(Collectors.toList())
            );
        } else if (order instanceof TabOrder tabOrder) {
            return new OrderDto(
                    tabOrder.getId(),
                    tabOrder.getTimestamp(),
                    tabOrder.getStatus(),
                    "TAB",
                    null,
                    null,
                    tabOrder.getRestaurantTable().getTableNumber(),
                    tabOrder.getFrontStaff().getEmail(),
                    tabOrder.getOrderItems().stream()
                            .map(OrderItemDto::from)
                            .collect(Collectors.toList())
            );
        } else {
            // Handle the base Order entity if needed
            return new OrderDto(
                    order.getId(),
                    order.getTimestamp(),
                    order.getStatus(),
                    "base",
                    null,
                    null,
                    null,
                    null,
                    order.getOrderItems().stream()
                            .map(OrderItemDto::from)
                            .collect(Collectors.toList())
            );
        }
    }
}