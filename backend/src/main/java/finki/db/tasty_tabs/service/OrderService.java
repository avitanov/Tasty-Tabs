package finki.db.tasty_tabs.service;

import finki.db.tasty_tabs.entity.*;
import finki.db.tasty_tabs.web.dto.CreateOrderDto;
import finki.db.tasty_tabs.web.dto.CreateOrderItemDto;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    List<Order> findAll();
    Order findById(Long id);

    Order updateOrder(Long id, CreateOrderDto dto);
    void deleteOrder(Long id);

    double calculateTotalPrice(Long orderId);
    void updateOrderStatus(Long orderId, String newStatus);

    // Methods for Order Items
    OrderItem addItemToOrder(Long orderId, CreateOrderItemDto itemDto);
    void decreaseOrderItemQuantity(Long orderItemId);
    OrderItem updateOrderItem(Long orderItemId, CreateOrderItemDto itemDto);
    void deleteOrderItem(Long orderItemId);
    OrderItem processOrderItem(Long orderItemId);

    // Specific OnlineOrder methods
    OnlineOrder createOnlineOrder(CreateOrderDto dto, String userEmail);
    OnlineOrder findOnlineOrderById(Long id);
    List<OnlineOrder> findAllOnlineOrders();
    List<OnlineOrder> findOnlineOrdersByCustomer(Long customerId);

    // Specific TabOrder methods
    TabOrder createTabOrder(CreateOrderDto dto, String userEmail);
    TabOrder findTabOrderById(Long id);
    List<TabOrder> findAllTabOrders();
    List<TabOrder> findTabOrdersByTableAndDate(Integer tableNumber, LocalDate date);
    List<TabOrder> findTabOrdersByStaff(Long frontStaffId);
    TabOrder assignOrderToStaff(Long orderId, Long frontStaffId);

    List<Order> findOpenOrders();
    List<Order> findClosedOrders();

    // General methods for all order types
    Order cancelOrder(Long orderId);
}