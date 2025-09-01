package finki.db.tasty_tabs.service;

import finki.db.tasty_tabs.web.dto.AuthDto;
import finki.db.tasty_tabs.web.dto.request.RegisterRequest;
import finki.db.tasty_tabs.web.dto.UserDto;

public interface AuthService {
    AuthDto authenticate(String username, String password);
    AuthDto register(RegisterRequest request);
    UserDto getAuthenticatedUser();
}
