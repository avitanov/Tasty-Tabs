package finki.db.tasty_tabs.repository;

import finki.db.tasty_tabs.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {}
