package finki.db.tasty_tabs.repository;

import finki.db.tasty_tabs.entity.Reservation;
import finki.db.tasty_tabs.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByUser(User user);
    List<Reservation> findAllByDatetimeBetween(LocalDateTime start, LocalDateTime end);
}
