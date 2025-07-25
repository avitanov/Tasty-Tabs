package finki.db.tasty_tabs.service.impl;

import finki.db.tasty_tabs.entity.User;
import finki.db.tasty_tabs.repository.UserRepository;
import finki.db.tasty_tabs.service.AuthService;
import finki.db.tasty_tabs.utils.JwtProvider;
import finki.db.tasty_tabs.web.dto.AuthDto;
import finki.db.tasty_tabs.web.dto.RegisterRequest;
import finki.db.tasty_tabs.web.dto.UserDto;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, JwtProvider jwtProvider, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthDto authenticate(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtProvider.generateToken(user.getEmail());

        return new AuthDto(
                token,
                UserDto.from(user)
        );
    }


    @Override
    @Transactional
    public AuthDto register(RegisterRequest request) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public UserDto getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return UserDto.from(userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

}
