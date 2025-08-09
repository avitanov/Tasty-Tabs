package finki.db.tasty_tabs.web.controllers;

import finki.db.tasty_tabs.entity.Reservation;
import finki.db.tasty_tabs.service.ReservationService;
import finki.db.tasty_tabs.web.dto.CreateReservationDto;
import finki.db.tasty_tabs.web.dto.ReservationDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservations")
@Tag(name = "Reservation API", description = "Endpoints for managing reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @Operation(summary = "Get all reservations")
    @GetMapping
    public List<ReservationDto> getAll() {
        return reservationService.getAllReservations()
                .stream()
                .map(ReservationDto::from)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get reservation by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ReservationDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ReservationDto.from(reservationService.getReservationById(id)));
    }

    @Operation(summary = "Create new reservation")
    @PostMapping("/add")
    public ResponseEntity<ReservationDto> create(@RequestBody CreateReservationDto dto, Authentication authentication) {
        String userEmail = authentication.getName();
        Reservation reservation = reservationService.createReservation(dto, userEmail);
        return ResponseEntity.ok(ReservationDto.from(reservation));
    }

    @Operation(summary = "Update reservation")
    @PutMapping("/edit/{id}")
    public ResponseEntity<ReservationDto> update(@PathVariable Long id, @RequestBody CreateReservationDto dto, Authentication authentication) {
        String userEmail = authentication.getName();
        Reservation reservation = reservationService.updateReservation(id, dto, userEmail);
        return ResponseEntity.ok(ReservationDto.from(reservation));
    }

    @Operation(summary = "Delete reservation")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
