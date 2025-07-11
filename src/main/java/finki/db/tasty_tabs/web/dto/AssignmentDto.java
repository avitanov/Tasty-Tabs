package finki.db.tasty_tabs.web.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AssignmentDto {
    private Long id;
    private LocalDateTime clockInTime;
    private LocalDateTime clockOutTime;
    private Long managerId;
    private Long employeeId;
    private Long shiftId;
}
