package finki.db.tasty_tabs.service;

import finki.db.tasty_tabs.entity.RestaurantTable;

import java.util.List;

public interface RestaurantTableService {

    RestaurantTable findById(Long id);
    List<RestaurantTable> getAll();
    RestaurantTable updateTable(Long id, RestaurantTable restaurantTable);
    void deleteTable(Long id);
    List<RestaurantTable> getAllBySeatCapacity(Integer seatCapacity);
    RestaurantTable createTable(RestaurantTable restaurantTable);
}
