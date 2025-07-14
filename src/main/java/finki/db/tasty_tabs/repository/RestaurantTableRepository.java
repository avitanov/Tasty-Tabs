package finki.db.tasty_tabs.repository;

import finki.db.tasty_tabs.entity.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Integer> {

    List<RestaurantTable> findAllBySeatCapacity(Integer seatCapacity);
}
