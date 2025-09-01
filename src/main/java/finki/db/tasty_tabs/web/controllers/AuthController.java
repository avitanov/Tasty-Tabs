package finki.db.tasty_tabs.web.controllers;

import finki.db.tasty_tabs.service.AuthService;
import finki.db.tasty_tabs.web.dto.AuthDto;
import finki.db.tasty_tabs.web.dto.request.RegisterRequest;
import finki.db.tasty_tabs.web.dto.request.AuthRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Authentication and authorization endpoints")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthDto> authenticate(@Valid @RequestBody AuthRequest authDto) {
        return ResponseEntity.ok(authService.authenticate(authDto.username(), authDto.password()));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthDto> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }
}
