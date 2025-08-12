package finki.db.tasty_tabs.repository;

import finki.db.tasty_tabs.entity.Order;
import finki.db.tasty_tabs.entity.OrderItem;
import finki.db.tasty_tabs.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> findByOrderAndProduct(Order order, Product product);

}
