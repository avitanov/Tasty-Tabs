package finki.db.tasty_tabs.web.security;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PublicUrlProvider {
    // This list should exactly match the permitAll() paths in your SecurityConfig
    private static final List<String> PUBLIC_PATHS = List.of(
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/api/categories/**",
            "/api/**"
    );

    public List<String> getPublicPaths() {
        return PUBLIC_PATHS;
    }
}
