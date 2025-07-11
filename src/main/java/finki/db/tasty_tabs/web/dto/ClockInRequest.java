package finki.db.tasty_tabs.web.dto;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class ClockInRequest {
    private Long employeeId;
    private Long shiftId;
}
