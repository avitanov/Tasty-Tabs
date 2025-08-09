package finki.db.tasty_tabs.service;

import finki.db.tasty_tabs.entity.RestaurantTable;

import java.util.List;
import java.util.Optional;

public interface RestaurantTableService {

    RestaurantTable findById(Integer id);
    List<RestaurantTable> getAll();
    RestaurantTable updateTable(Integer id,RestaurantTable restaurantTable);
    void deleteTable(Integer id);
    List<RestaurantTable> getAllBySeatCapacity(Integer seatCapacity);
    RestaurantTable createTable(RestaurantTable restaurantTable);
}
