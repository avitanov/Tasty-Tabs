package finki.db.tasty_tabs.web.dto;
import finki.db.tasty_tabs.entity.Manager;
import finki.db.tasty_tabs.entity.Shift;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
public record ShiftDto(
        Long id,
        LocalDate date,
        LocalDateTime start,
        LocalDateTime end,
        Manager manager,
        List<AssignmentDto> assignments
) {
    public static ShiftDto fromShift(Shift shift) {
        return new ShiftDto(
                shift.getId(),
                shift.getDate(),
                shift.getStart(),
                shift.getEnd(),
                shift.getManager(),
                shift.getAssignments().stream()
                        .map(AssignmentDto::fromAssignment)
                        .toList()
        );
    }
}