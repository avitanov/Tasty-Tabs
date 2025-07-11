package finki.db.tasty_tabs.service;

import finki.db.tasty_tabs.web.dto.CreateReservationRequest;
import finki.db.tasty_tabs.web.dto.ReservationDto;

public interface ReservationService {
    ReservationDto makeReservation(CreateReservationRequest request);
}
