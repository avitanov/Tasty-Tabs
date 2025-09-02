package finki.db.tasty_tabs.service;

import finki.db.tasty_tabs.entity.Reservation;
import finki.db.tasty_tabs.entity.ReservationManagedFrontStaff;
import finki.db.tasty_tabs.web.dto.CreateReservationDto;
import finki.db.tasty_tabs.web.dto.ReservationDto;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {
    List<ReservationDto> getAllReservationsWithStatus();
    Reservation getReservationById(Long id);
    Reservation createReservation(CreateReservationDto dto,String userEmail);
    Reservation updateReservation(Long id, CreateReservationDto dto,String userEmail);
    void deleteReservation(Long id);
    List<Reservation> getAllReservationsByUser(String userEmail);
    ReservationManagedFrontStaff acceptReservation(Long reservationId, String frontStaffEmail, Long tableNumber);

    List<Reservation> getAllReservationsForToday();
    List<Reservation> getAllReservationsForDate(LocalDate date);
}
