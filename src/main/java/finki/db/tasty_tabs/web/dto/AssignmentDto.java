package finki.db.tasty_tabs.web.dto;

import finki.db.tasty_tabs.entity.Assignment;
import finki.db.tasty_tabs.entity.Employee;
import finki.db.tasty_tabs.entity.Manager;

import java.time.LocalDateTime;

public record AssignmentDto(
        Long id,
        LocalDateTime clockInTime,
        LocalDateTime clockOutTime,
        Manager manager,
        Employee employee
) {
    public static AssignmentDto fromAssignment(Assignment assignment) {
        return new AssignmentDto(
                assignment.getId(),
                assignment.getClockInTime(),
                assignment.getClockOutTime(),
                assignment.getManager(),
                assignment.getEmployee()
        );
    }
}
