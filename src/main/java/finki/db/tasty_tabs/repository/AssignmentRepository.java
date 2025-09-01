package finki.db.tasty_tabs.repository;

import finki.db.tasty_tabs.entity.Assignment;
import finki.db.tasty_tabs.web.dto.AssignmentDto;
import finki.db.tasty_tabs.web.dto.ShiftDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    Optional<Assignment> findByEmployeeIdAndShiftId(Long employeeId, Long shiftId);

    Optional<Assignment> findFirstByEmployee_IdOrderByShiftStartAsc(Long employeeId);
}
