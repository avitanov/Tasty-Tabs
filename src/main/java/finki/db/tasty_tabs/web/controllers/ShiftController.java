package finki.db.tasty_tabs.web.controllers;

import finki.db.tasty_tabs.entity.Shift;
import finki.db.tasty_tabs.service.ShiftService;
import finki.db.tasty_tabs.web.dto.CreateShiftDto;
import finki.db.tasty_tabs.web.dto.ShiftDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<Shift> getAllShifts() {
        return shiftService.getAllShifts();
    }

    @Operation(summary = "Get shift by ID")
    @GetMapping("/{id}")
    public Shift getShiftById(@PathVariable Long id) {
        return shiftService.getShiftById(id);
    }

    @Operation(summary = "Create a shift (Manager only)")
    @PostMapping
    public Shift createShift(@RequestBody CreateShiftDto dto, Authentication authentication) {
        String username = authentication.getName();
        return shiftService.createShift(dto, username);
    }

    @Operation(summary = "Update a shift (Manager only)")
    @PutMapping("/{id}")
    public Shift updateShift(@PathVariable Long id, @RequestBody CreateShiftDto dto, Authentication authentication) {
        String username = authentication.getName();
        return shiftService.updateShift(id, dto, username);
    }

    @Operation(summary = "Delete a shift (Manager only)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShift(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        shiftService.deleteShift(id, username);
        return ResponseEntity.noContent().build();
    }
}
