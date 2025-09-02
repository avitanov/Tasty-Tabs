package finki.db.tasty_tabs.repository;

import finki.db.tasty_tabs.entity.ReservationManagedFrontStaff;
import finki.db.tasty_tabs.entity.composite_keys.ReservationManagedFrontStaffId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservationManagedFrontStaffRepository extends JpaRepository<ReservationManagedFrontStaff, ReservationManagedFrontStaffId> {
    boolean existsByReservation_Id(Long reservationId);
    Optional<ReservationManagedFrontStaff> findFirstByReservation_Id(Long reservationId);

}
