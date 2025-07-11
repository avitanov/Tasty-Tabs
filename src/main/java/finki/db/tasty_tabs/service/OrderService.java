package finki.db.tasty_tabs.service;

import finki.db.tasty_tabs.entity.OnlineOrder;
import finki.db.tasty_tabs.entity.OrderItem;
import finki.db.tasty_tabs.entity.TabOrder;
import finki.db.tasty_tabs.web.dto.CreateOnlineOrderRequest;
import finki.db.tasty_tabs.web.dto.CreateTabOrderRequest;
import finki.db.tasty_tabs.web.dto.OrderItemRequest;
import finki.db.tasty_tabs.web.dto.TransferTabRequest;

public interface OrderService {
    TabOrder createTabOrder(CreateTabOrderRequest request);
    OnlineOrder createOnlineOrder(CreateOnlineOrderRequest request);
    OrderItem addItemToOrder(Long orderId, OrderItemRequest itemRequest);
    TabOrder transferTab(Long orderId, TransferTabRequest request);
}