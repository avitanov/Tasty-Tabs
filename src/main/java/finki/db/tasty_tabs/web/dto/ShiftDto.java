package finki.db.tasty_tabs.web.dto;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
@Data
public class ShiftDto {
    private Long id;
    private LocalDateTime date;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long managerId;
}