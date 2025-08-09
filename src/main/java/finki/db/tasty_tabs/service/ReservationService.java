package finki.db.tasty_tabs.service;

import finki.db.tasty_tabs.entity.Reservation;
import finki.db.tasty_tabs.web.dto.CreateReservationDto;
import finki.db.tasty_tabs.web.dto.ReservationDto;

import java.util.List;

public interface ReservationService {
    List<Reservation> getAllReservations();
    Reservation getReservationById(Long id);
    Reservation createReservation(CreateReservationDto dto,String userEmail);
    Reservation updateReservation(Long id, CreateReservationDto dto,String userEmail);
    void deleteReservation(Long id);
}
