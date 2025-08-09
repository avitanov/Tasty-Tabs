package finki.db.tasty_tabs.web.dto;

import finki.db.tasty_tabs.entity.Assignment;
import finki.db.tasty_tabs.entity.Employee;
import finki.db.tasty_tabs.entity.Manager;
import finki.db.tasty_tabs.entity.Shift;

import java.time.LocalDateTime;

public record CreateAssignmentDto(
        LocalDateTime clockInTime,
        LocalDateTime clockOutTime,
        Long managerId,
        Long employeeId,
        Long shiftId
) {
    public Assignment toAssignment(Manager manager,Employee employee,Shift shift) {
        return new Assignment(
                clockInTime,
                clockOutTime,
                manager,
                employee,
                shift
        );
    }
}