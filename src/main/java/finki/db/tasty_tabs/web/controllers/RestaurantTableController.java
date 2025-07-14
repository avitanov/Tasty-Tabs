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
    public ResponseEntity<RestaurantTable> findById(@PathVariable Integer tableNumber) {
        return restaurantTableService.findById(tableNumber)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Add a new table", description = "Creates a new restaurant table.")
    @PostMapping("/add")
    public ResponseEntity<RestaurantTable> save(@RequestBody CreateRestaurantTableDto dto) {
        try{
            return restaurantTableService.createTable(dto.toRestaurantTable())
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.badRequest().build());
        }
        catch(RuntimeException ex){
            return ResponseEntity.badRequest().build();

        }
    }

    @Operation(summary = "Update an existing table", description = "Updates a restaurant table by its table number.")
    @PutMapping("/edit/{tableNumber}")
    public ResponseEntity<RestaurantTable> update(
            @PathVariable Integer tableNumber,
            @RequestBody CreateRestaurantTableDto dto
    ) {
        try{
            return restaurantTableService.updateTable(tableNumber, dto.toRestaurantTable())
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }
        catch(RuntimeException ex){
            return ResponseEntity.badRequest().build();
        }

    }

    @Operation(summary = "Delete a table", description = "Deletes a restaurant table by its table number.")
    @DeleteMapping("/delete/{tableNumber}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer tableNumber) {
        if (restaurantTableService.findById(tableNumber).isPresent()) {
            restaurantTableService.deleteTable(tableNumber);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}