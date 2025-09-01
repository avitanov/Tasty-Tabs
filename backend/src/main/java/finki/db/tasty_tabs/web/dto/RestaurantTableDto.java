package finki.db.tasty_tabs.web.dto;

import finki.db.tasty_tabs.entity.RestaurantTable;

public record RestaurantTableDto(
        Long tableNumber,
        Integer seatCapacity
) {
    public static RestaurantTableDto from(RestaurantTable table){
        return new RestaurantTableDto(table.getTableNumber(), table.getSeatCapacity());
    }
}
