package finki.db.tasty_tabs.repository;

import finki.db.tasty_tabs.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {}
