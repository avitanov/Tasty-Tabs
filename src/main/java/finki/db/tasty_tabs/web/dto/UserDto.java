package finki.db.tasty_tabs.web.dto;
import finki.db.tasty_tabs.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String street;
    private String city;
    private String phoneNumber;

    public static UserDto from(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getStreet(),
                user.getCity(),
                user.getPhoneNumber()
        );
    }
}
