package finki.db.tasty_tabs.repository;

import finki.db.tasty_tabs.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {}
