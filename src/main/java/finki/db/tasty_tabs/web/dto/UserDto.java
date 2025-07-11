package finki.db.tasty_tabs.web.dto;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
@Data
class UserDto {
    private Long id;
    private String email;
    private String street;
    private String city;
    private String phoneNumber;
}
