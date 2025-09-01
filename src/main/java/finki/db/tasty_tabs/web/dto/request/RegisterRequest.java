package finki.db.tasty_tabs.web.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotNull(message = "Email is required")
    private String email;
    @NotNull(message = "Password is required")
    private String password;
    private String passwordConfirmation;
    private String firstName;
    private String lastName;


}
