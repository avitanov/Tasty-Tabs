package finki.db.tasty_tabs.web.controllers;

import finki.db.tasty_tabs.entity.Shift;
import finki.db.tasty_tabs.service.ShiftService;
import finki.db.tasty_tabs.web.dto.CreateShiftDto;
import finki.db.tasty_tabs.web.dto.ShiftDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/shifts")
@Tag(name = "Shift API", description = "Endpoints for managing work shifts (Manager only)")
public class ShiftController {

    private final ShiftService shiftService;

    public ShiftController(ShiftService shiftService) {
        this.shiftService = shiftService;
    }

    @Operation(summary = "Get all shifts")
    @GetMapping
    public List<ShiftDto> getAllShifts() {
        return shiftService.getAllShifts()
                .stream()
                .map(ShiftDto::fromShift)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get shift by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ShiftDto> getShiftById(@PathVariable Long id) {
        Shift shift = shiftService.getShiftById(id);
        return ResponseEntity.ok(ShiftDto.fromShift(shift));
    }

    @Operation(summary = "Create a shift (Manager only)")
    @PostMapping
    public ResponseEntity<ShiftDto> createShift(@RequestBody CreateShiftDto dto, Authentication authentication) {
        String username = authentication.getName();
        Shift shift = shiftService.createShift(dto, username);
        return new ResponseEntity<>(ShiftDto.fromShift(shift), HttpStatus.CREATED);
    }

    @Operation(summary = "Update a shift (Manager only)")
    @PutMapping("/{id}")
    public ResponseEntity<ShiftDto> updateShift(@PathVariable Long id, @RequestBody CreateShiftDto dto, Authentication authentication) {
        String username = authentication.getName();
        Shift updatedShift = shiftService.updateShift(id, dto, username);
        return ResponseEntity.ok(ShiftDto.fromShift(updatedShift));
    }

    @Operation(summary = "Delete a shift (Manager only)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShift(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        shiftService.deleteShift(id, username);
        return ResponseEntity.noContent().build();
    }
}
