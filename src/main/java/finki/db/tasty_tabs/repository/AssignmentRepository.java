package finki.db.tasty_tabs.repository;

import finki.db.tasty_tabs.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    Optional<Assignment> findByEmployeeIdAndShiftId(Long employeeId, Long shiftId);
}
