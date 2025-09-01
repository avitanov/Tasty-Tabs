package finki.db.tasty_tabs.repository;

import finki.db.tasty_tabs.entity.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {

    List<RestaurantTable> findAllBySeatCapacity(Integer seatCapacity);
}
