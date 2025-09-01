package finki.db.tasty_tabs.web.controllers;

import finki.db.tasty_tabs.entity.RestaurantTable;
import finki.db.tasty_tabs.service.RestaurantTableService;
import finki.db.tasty_tabs.web.dto.CreateRestaurantTableDto;
import finki.db.tasty_tabs.web.dto.RestaurantTableDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<RestaurantTableDto> findAll() {
        return restaurantTableService.getAll()
                .stream()
                .map(RestaurantTableDto::from)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get a table by table number", description = "Finds a table by its table number.")
    @GetMapping("/{tableNumber}")
    public ResponseEntity<RestaurantTableDto> findById(@PathVariable Integer tableNumber) {
        return ResponseEntity.ok(RestaurantTableDto.from(restaurantTableService.findById(tableNumber)));
                }

    @Operation(summary = "Add a new table", description = "Creates a new restaurant table.")
    @PostMapping("/add")
    public ResponseEntity<RestaurantTableDto> save(@RequestBody CreateRestaurantTableDto dto) {
        RestaurantTable newTable = restaurantTableService.createTable(dto.toRestaurantTable());
        return ResponseEntity.status(HttpStatus.CREATED).body(RestaurantTableDto.from(newTable));
    }

    @Operation(summary = "Update an existing table", description = "Updates a restaurant table by its table number.")
    @PutMapping("/edit/{tableNumber}")
    public ResponseEntity<RestaurantTableDto> update(
            @PathVariable Integer tableNumber,
            @RequestBody CreateRestaurantTableDto dto
    ) {
        RestaurantTable updatedTable = restaurantTableService.updateTable(tableNumber, dto.toRestaurantTable());
        return ResponseEntity.ok(RestaurantTableDto.from(updatedTable));
    }

    @Operation(summary = "Delete a table", description = "Deletes a restaurant table by its table number.")
    @DeleteMapping("/delete/{tableNumber}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer tableNumber) {
        restaurantTableService.deleteTable(tableNumber);
        return ResponseEntity.noContent().build();
    }
}