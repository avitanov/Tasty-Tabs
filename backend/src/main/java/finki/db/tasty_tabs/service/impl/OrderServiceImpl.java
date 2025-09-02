package finki.db.tasty_tabs.service.impl;

import finki.db.tasty_tabs.aspect.annotation.CheckOnDuty;
import finki.db.tasty_tabs.entity.*;
import finki.db.tasty_tabs.entity.exceptions.*;
import finki.db.tasty_tabs.repository.*;
import finki.db.tasty_tabs.service.OrderService;
import finki.db.tasty_tabs.web.dto.CreateOrderDto;
import finki.db.tasty_tabs.web.dto.CreateOrderItemDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final RestaurantTableRepository tableRepository;
    private final UserRepository userRepository;
    private final TabOrderRepository tabOrderRepository;
    private final OnlineOrderRepository onlineOrderRepository;
    private final FrontStaffRepository frontStaffRepository;
    private final CustomerRepository customerRepository;

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Override
    @CheckOnDuty
    public Order updateOrder(Long id, CreateOrderDto dto) {
        Order existingOrder = findById(id);

        existingOrder.setStatus(dto.status());

        existingOrder.getOrderItems().clear();
        List<OrderItem> newOrderItems = dto.orderItems().stream().map(itemDto -> {
            OrderItem item = new OrderItem();
            item.setOrder(existingOrder);
            item.setQuantity(itemDto.quantity());
            item.setPrice(itemDto.price());
            item.setIsProcessed(itemDto.isProcessed());
            item.setTimestamp(LocalDateTime.now());
            Product product = productRepository.findById(itemDto.productId())
                    .orElseThrow(() -> new ProductNotFoundException(itemDto.productId()));
            item.setProduct(product);
            return item;
        }).collect(Collectors.toList());

        existingOrder.setOrderItems(newOrderItems);

        return orderRepository.save(existingOrder);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public double calculateTotalPrice(Long orderId) {
        Order order = findById(orderId);
        return order.getOrderItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getPrice())
                .sum();
    }

    @Override
    @CheckOnDuty
    public void updateOrderStatus(Long orderId, String newStatus) {
        Order order = findById(orderId);
        order.setStatus(newStatus);
        orderRepository.save(order);
    }

    // Methods for Order Items
    @Override
    @Transactional
    @CheckOnDuty
    public OrderItem addItemToOrder(Long orderId, CreateOrderItemDto itemDto) {
        Order order = findById(orderId);
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setQuantity(itemDto.quantity());
        orderItem.setPrice(itemDto.price());
        orderItem.setIsProcessed(itemDto.isProcessed());
        orderItem.setTimestamp(LocalDateTime.now());
        Product product = productRepository.findById(itemDto.productId())
                .orElseThrow(() -> new ProductNotFoundException(itemDto.productId()));
        orderItem.setProduct(product);
        return orderItemRepository.save(orderItem);
    }

    @Override
    @Transactional
    @CheckOnDuty
    public void decreaseOrderItemQuantity(Long orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(OrderItemNotFoundException::new);
        if (orderItem.getQuantity() > 1) {
            orderItem.setQuantity(orderItem.getQuantity() - 1);
            orderItemRepository.save(orderItem);
        } else {
            orderItemRepository.delete(orderItem);
        }
    }

    @Override
    @Transactional
    @CheckOnDuty
    public OrderItem updateOrderItem(Long orderItemId, CreateOrderItemDto itemDto) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(OrderItemNotFoundException::new);
        orderItem.setQuantity(itemDto.quantity());
        orderItem.setPrice(itemDto.price());
        orderItem.setIsProcessed(itemDto.isProcessed());
        Product product = productRepository.findById(itemDto.productId())
                .orElseThrow(() -> new ProductNotFoundException(itemDto.productId()));
        orderItem.setProduct(product);
        return orderItemRepository.save(orderItem);
    }

    @Override
    @Transactional
    @CheckOnDuty
    public void deleteOrderItem(Long orderItemId) {
        orderItemRepository.deleteById(orderItemId);
    }

    @Override
    @Transactional
    public OrderItem processOrderItem(Long orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(OrderItemNotFoundException::new);
        orderItem.setIsProcessed(true);
        return orderItemRepository.save(orderItem);
    }

    // Specific OnlineOrder methods
    @Override
    @Transactional
    public OnlineOrder createOnlineOrder(CreateOrderDto dto, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + userEmail + " not found."));
        if (!(user instanceof Customer)) {
            throw new SecurityException("User is not authorized to create online orders.");
        }
        OnlineOrder onlineOrder = new OnlineOrder();
        onlineOrder.setCustomer((Customer) user);
        onlineOrder.setDeliveryAddress(dto.deliveryAddress());
        onlineOrder.setTimestamp(LocalDateTime.now());
        onlineOrder.setStatus(dto.status());
        if (dto.orderItems() != null && !dto.orderItems().isEmpty()) {
            List<OrderItem> orderItems = dto.orderItems().stream().map(itemDto -> {
                OrderItem item = new OrderItem();
                item.setOrder(onlineOrder);
                item.setQuantity(itemDto.quantity());
                item.setPrice(itemDto.price());
                item.setIsProcessed(itemDto.isProcessed());
                item.setTimestamp(LocalDateTime.now());
                Product product = productRepository.findById(itemDto.productId())
                        .orElseThrow(() -> new ProductNotFoundException(itemDto.productId()));
                item.setProduct(product);
                return item;
            }).collect(Collectors.toList());
            onlineOrder.setOrderItems(orderItems);
        }
        return onlineOrderRepository.save(onlineOrder);
    }

    @Override
    public OnlineOrder findOnlineOrderById(Long id) {
        return onlineOrderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Override
    public List<OnlineOrder> findAllOnlineOrders() {
        return onlineOrderRepository.findAll();
    }

    @Override
    public List<OnlineOrder> findOnlineOrdersByCustomer(Long customerId) {
        return onlineOrderRepository.findAllByCustomer_Id(customerId);
    }

    // Specific TabOrder methods
    @Override
    @Transactional
    @CheckOnDuty
    public TabOrder createTabOrder(CreateOrderDto dto, String userEmail) {
        log.debug("User {} creating a tab order for table {}", userEmail, dto.tableNumber());
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + userEmail + " not found."));
        if (!(user instanceof FrontStaff)) {
            throw new SecurityException("User is not authorized to create tab orders.");
        }
        TabOrder tabOrder = new TabOrder();
        RestaurantTable table = tableRepository.findById(dto.tableNumber())
                .orElseThrow(() -> new TableNotFoundException(dto.tableNumber()));
        tabOrder.setRestaurantTable(table);
        tabOrder.setFrontStaff((FrontStaff) user);
        tabOrder.setTimestamp(LocalDateTime.now());
        tabOrder.setStatus(dto.status());
        if (dto.orderItems() != null && !dto.orderItems().isEmpty()) {
            log.debug("OrderItems is not empty, processing items...");
            List<OrderItem> orderItems = dto.orderItems().stream().map(itemDto -> {
                OrderItem item = new OrderItem();
                item.setOrder(tabOrder);
                item.setQuantity(itemDto.quantity());
                item.setPrice(itemDto.price());
                item.setIsProcessed(itemDto.isProcessed());
                item.setTimestamp(LocalDateTime.now());
                Product product = productRepository.findById(itemDto.productId())
                        .orElseThrow(() -> new ProductNotFoundException(itemDto.productId()));
                item.setProduct(product);
                return item;
            }).collect(Collectors.toList());
            tabOrder.setOrderItems(orderItems);
        }
        return tabOrderRepository.save(tabOrder);
    }

    @Override
    public TabOrder findTabOrderById(Long id) {
        return tabOrderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Override
    public List<TabOrder> findAllTabOrders() {
        return tabOrderRepository.findAll();
    }

    @Override
    public List<TabOrder> findTabOrdersByTableAndDate(Integer tableNumber, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        return tabOrderRepository.findByRestaurantTable_TableNumberAndTimestampBetween(tableNumber, startOfDay, endOfDay);
    }

    @Override
    public List<TabOrder> findTabOrdersByStaff(Long frontStaffId) {
        return tabOrderRepository.findAllByFrontStaffId(frontStaffId);
    }

    @Override
    @Transactional
    @CheckOnDuty
    public TabOrder assignOrderToStaff(Long orderId, Long frontStaffId) {
        TabOrder tabOrder = tabOrderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        FrontStaff frontStaff = frontStaffRepository.findById(frontStaffId)
                .orElseThrow(() -> new FrontStaffNotFoundException(frontStaffId));
        tabOrder.setFrontStaff(frontStaff);
        return tabOrderRepository.save(tabOrder);
    }

    // General methods for all order types
    @Override
    @Transactional
    @CheckOnDuty
    public Order cancelOrder(Long orderId) {
        Order order = findById(orderId);
        order.setStatus("CANCELED");
        return orderRepository.save(order);
    }

    @Override
    public List<Order> findOpenOrders() {

        // Query the OnlineOrderRepository for open orders
        List<OnlineOrder> onlineOrders = onlineOrderRepository.findAllByStatusIn(List.of("PENDING", "CONFIRMED"));

        // Query the TabOrderRepository for open orders
        List<TabOrder> tabOrders = tabOrderRepository.findAllByStatusIn(List.of("PENDING", "CONFIRMED"));

        // Combine the lists into a single List<Order>
        List<Order> combinedOrders = new java.util.ArrayList<>();
        combinedOrders.addAll(onlineOrders);
        combinedOrders.addAll(tabOrders);

        return combinedOrders;
    }

    @Override
    public List<Order> findClosedOrders() {

        // Query the OnlineOrderRepository for open orders
        List<OnlineOrder> onlineOrders = onlineOrderRepository.findAllByStatusIn(List.of("CLOSED"));

        // Query the TabOrderRepository for open orders
        List<TabOrder> tabOrders = tabOrderRepository.findAllByStatus("CLOSED");

        // Combine the lists into a single List<Order>
        List<Order> combinedOrders = new java.util.ArrayList<>();
        combinedOrders.addAll(onlineOrders);
        combinedOrders.addAll(tabOrders);

        return combinedOrders;
    }
}