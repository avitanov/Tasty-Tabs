package finki.db.tasty_tabs.web.controllers;
import finki.db.tasty_tabs.entity.Order;
import finki.db.tasty_tabs.service.OrderService;
import finki.db.tasty_tabs.web.dto.CreateOrderDto;
import finki.db.tasty_tabs.web.dto.CreateOrderItemDto;
import finki.db.tasty_tabs.web.dto.OrderDto;
import finki.db.tasty_tabs.web.dto.OrderItemDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order API", description = "Endpoints for managing orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Get all orders (both online and tab)")
    @GetMapping
    public List<OrderDto> getAllOrders() {
        return orderService.findAll().stream()
                .map(OrderDto::from)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get any order by ID")
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(OrderDto.from(orderService.findById(id)));
    }

    @Operation(summary = "Update an existing order")
    @PutMapping("/{id}")
    public ResponseEntity<OrderDto> updateOrder(@PathVariable Long id, @RequestBody CreateOrderDto dto) {
        return ResponseEntity.ok(OrderDto.from(orderService.updateOrder(id, dto)));
    }

    @Operation(summary = "Delete an order by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Calculate total price of an order")
    @GetMapping("/{id}/total-price")
    public ResponseEntity<Double> calculateTotalPrice(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.calculateTotalPrice(id));
    }

    @Operation(summary = "Update the status of an order")
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable Long id, @RequestBody String status) {
        orderService.updateOrderStatus(id, status);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cancel an order")
    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<OrderDto> cancelOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(OrderDto.from(orderService.cancelOrder(orderId)));
    }

    // Order Item Endpoints
    @Operation(summary = "Add an item to an existing order")
    @PostMapping("/{orderId}/items")
    public ResponseEntity<OrderItemDto> addItemToOrder(@PathVariable Long orderId, @RequestBody CreateOrderItemDto itemDto) {
        return ResponseEntity.ok(OrderItemDto.from(orderService.addItemToOrder(orderId, itemDto)));
    }

    @Operation(summary = "Decrease the quantity of a specific order item")
    @DeleteMapping("/items/{orderItemId}")
    public ResponseEntity<Void> decreaseOrderItemQuantity(@PathVariable Long orderItemId) {
        orderService.decreaseOrderItemQuantity(orderItemId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update an existing order item")
    @PutMapping("/items/{orderItemId}")
    public ResponseEntity<OrderItemDto> updateOrderItem(@PathVariable Long orderItemId, @RequestBody CreateOrderItemDto itemDto) {
        return ResponseEntity.ok(OrderItemDto.from(orderService.updateOrderItem(orderItemId, itemDto)));
    }

    @Operation(summary = "Delete an order item by ID")
    @DeleteMapping("/items/{orderItemId}/delete")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long orderItemId) {
        orderService.deleteOrderItem(orderItemId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Mark an order item as processed")
    @PatchMapping("/items/{orderItemId}/process")
    public ResponseEntity<OrderItemDto> processOrderItem(@PathVariable Long orderItemId) {
        return ResponseEntity.ok(OrderItemDto.from(orderService.processOrderItem(orderItemId)));
    }

    // Online Order Endpoints
    @Operation(summary = "Create a new online order for a logged-in customer")
    @PostMapping("/online")
    public ResponseEntity<OrderDto> createOnlineOrder(@RequestBody CreateOrderDto dto, Authentication authentication) {
        String userEmail = authentication.getName();
        return ResponseEntity.ok(OrderDto.from(orderService.createOnlineOrder(dto, userEmail)));
    }

    @Operation(summary = "Get all online orders")
    @GetMapping("/online")
    public List<OrderDto> getAllOnlineOrders() {
        return orderService.findAllOnlineOrders().stream()
                .map(OrderDto::from)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get a specific online order by ID")
    @GetMapping("/online/{id}")
    public ResponseEntity<OrderDto> getOnlineOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(OrderDto.from(orderService.findOnlineOrderById(id)));
    }

    @Operation(summary = "Get all online orders for a specific customer")
    @GetMapping("/online/customer/{customerId}")
    public List<OrderDto> getOnlineOrdersByCustomer(@PathVariable Long customerId) {
        return orderService.findOnlineOrdersByCustomer(customerId).stream()
                .map(OrderDto::from)
                .collect(Collectors.toList());
    }

    // Tab Order Endpoints
    @Operation(summary = "Create a new tab order for a logged-in front staff member")
    @PostMapping("/tab")
    public ResponseEntity<OrderDto> createTabOrder(@RequestBody CreateOrderDto dto, Authentication authentication) {
        String userEmail = authentication.getName();
        return ResponseEntity.ok(OrderDto.from(orderService.createTabOrder(dto, userEmail)));
    }

    @Operation(summary = "Get all tab orders")
    @GetMapping("/tab")
    public List<OrderDto> getAllTabOrders() {
        return orderService.findAllTabOrders().stream()
                .map(OrderDto::from)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get a specific tab order by ID")
    @GetMapping("/tab/{id}")
    public ResponseEntity<OrderDto> getTabOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(OrderDto.from(orderService.findTabOrderById(id)));
    }

    @Operation(summary = "Get all tab orders for a specific table on a given date (defaults to today)")
    @GetMapping("/tab/table/{tableNumber}")
    public List<OrderDto> getTabOrdersByTableAndDate(
            @PathVariable Integer tableNumber,
            @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDate).now()}") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return orderService.findTabOrdersByTableAndDate(tableNumber, date).stream()
                .map(OrderDto::from)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Assign a TabOrder to a specific front staff member")
    @PatchMapping("/tab/{orderId}/assign-staff/{frontStaffId}")
    public ResponseEntity<OrderDto> assignOrderToStaff(@PathVariable Long orderId, @PathVariable Long frontStaffId) {
        return ResponseEntity.ok(OrderDto.from(orderService.assignOrderToStaff(orderId, frontStaffId)));
    }
    @Operation(summary = "Get all open orders (pending or in-progress)")
    @GetMapping("/open")
    public List<OrderDto> getOpenOrders() {
        return orderService.findOpenOrders().stream()
                .map(OrderDto::from)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get all closed orders (completed, canceled, or delivered)")
    @GetMapping("/closed")
    public List<OrderDto> getClosedOrders() {
        return orderService.findClosedOrders().stream()
                .map(OrderDto::from)
                .collect(Collectors.toList());
    }
}