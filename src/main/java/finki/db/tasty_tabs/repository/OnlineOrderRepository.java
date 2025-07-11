package finki.db.tasty_tabs.repository;

import finki.db.tasty_tabs.entity.OnlineOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OnlineOrderRepository extends JpaRepository<OnlineOrder, Long> {}
