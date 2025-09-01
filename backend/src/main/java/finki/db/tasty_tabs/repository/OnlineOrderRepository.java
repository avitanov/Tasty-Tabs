package finki.db.tasty_tabs.repository;

import finki.db.tasty_tabs.entity.Customer;
import finki.db.tasty_tabs.entity.OnlineOrder;
import finki.db.tasty_tabs.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface OnlineOrderRepository extends JpaRepository<OnlineOrder, Long> {

    List<OnlineOrder> findAllByCustomer_Id(Long id);

    List<OnlineOrder> findAllByStatusIn(Collection<String> statuses);

}
