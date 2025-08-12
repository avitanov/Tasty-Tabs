package finki.db.tasty_tabs.repository;

import finki.db.tasty_tabs.entity.OnlineOrder;
import finki.db.tasty_tabs.entity.TabOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TabOrderRepository extends JpaRepository<TabOrder, Long> {
    List<TabOrder> findByRestaurantTable_TableNumberAndTimestampBetween(Integer tableNumber, LocalDateTime startOfDay, LocalDateTime endOfDay);
    List<TabOrder> findAllByFrontStaffId(Long frontStaffId);
    List<TabOrder> findAllByStatus(String status);

}
