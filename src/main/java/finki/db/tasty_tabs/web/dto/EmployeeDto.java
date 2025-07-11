package finki.db.tasty_tabs.web.dto;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
@Data
class EmployeeDto extends UserDto {
    private Double netSalary;
    private Double grossSalary;
}
