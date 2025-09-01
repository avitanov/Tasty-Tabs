package finki.db.tasty_tabs.web.dto;

import finki.db.tasty_tabs.entity.Assignment;
import finki.db.tasty_tabs.entity.Employee;
import finki.db.tasty_tabs.entity.Manager;

import java.time.LocalDateTime;

public record AssignmentDto(
        Long id,
        LocalDateTime clockInTime,
        LocalDateTime clockOutTime,
        ManagerDto manager,
        EmployeeDto employee,
        ShiftDto shift
) {
    public static AssignmentDto fromAssignment(Assignment assignment) {
        return new AssignmentDto(
                assignment.getId(),
                assignment.getClockInTime(),
                assignment.getClockOutTime(),
                ManagerDto.from(assignment.getManager()),
                EmployeeDto.from(assignment.getEmployee()),
                ShiftDto.fromShiftBasic(assignment.getShift())
        );
    }
}
