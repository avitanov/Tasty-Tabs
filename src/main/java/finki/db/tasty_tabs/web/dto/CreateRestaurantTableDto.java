package finki.db.tasty_tabs.web.dto;

import finki.db.tasty_tabs.entity.RestaurantTable;

public record CreateRestaurantTableDto(
        Integer tableNumber,
        Integer seatCapacity
) {
    public RestaurantTable toRestaurantTable(){
        return new RestaurantTable(tableNumber,seatCapacity);
    }
}
