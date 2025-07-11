package finki.db.tasty_tabs.web.dto;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
@Data
class FrontStaffDto extends EmployeeDto {
    private Double tipPercent;
    private String staffRoleName;
}