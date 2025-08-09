package finki.db.tasty_tabs.web.controllers;

import finki.db.tasty_tabs.entity.RestaurantTable;
import finki.db.tasty_tabs.service.RestaurantTableService;
import finki.db.tasty_tabs.web.dto.CreateRestaurantTableDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
@Tag(name = "Restaurant Table API", description = "Endpoints for managing restaurant tables")
public class RestaurantTableController {

    private final RestaurantTableService restaurantTableService;

    public RestaurantTableController(RestaurantTableService restaurantTableService) {
        this.restaurantTableService = restaurantTableService;
    }

    @Operation(summary = "Get all tables", description = "Retrieves a list of all restaurant tables.")
    @GetMapping
    public List<RestaurantTable> findAll() {
        return restaurantTableService.getAll();
    }

    @Operation(summary = "Get a table by table number", description = "Finds a table by its table number.")
    @GetMapping("/{tableNumber}")
    public RestaurantTable findById(@PathVariable Integer tableNumber) {
        return restaurantTableService.findById(tableNumber);
    }

    @Operation(summary = "Add a new table", description = "Creates a new restaurant table.")
    @PostMapping("/add")
    public RestaurantTable save(@RequestBody CreateRestaurantTableDto dto) {
        return restaurantTableService.createTable(dto.toRestaurantTable());
    }

    @Operation(summary = "Update an existing table", description = "Updates a restaurant table by its table number.")
    @PutMapping("/edit/{tableNumber}")
    public RestaurantTable update(
            @PathVariable Integer tableNumber,
            @RequestBody CreateRestaurantTableDto dto
    ) {
        return restaurantTableService.updateTable(tableNumber, dto.toRestaurantTable());
    }

    @Operation(summary = "Delete a table", description = "Deletes a restaurant table by its table number.")
    @DeleteMapping("/delete/{tableNumber}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer tableNumber) {
        restaurantTableService.deleteTable(tableNumber);
        return ResponseEntity.noContent().build();
    }
}
