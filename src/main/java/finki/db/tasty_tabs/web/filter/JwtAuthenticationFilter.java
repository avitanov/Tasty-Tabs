package finki.db.tasty_tabs.web.filter;

import finki.db.tasty_tabs.utils.JwtProvider;
import finki.db.tasty_tabs.web.exception.FilterExceptionHandler;
import finki.db.tasty_tabs.web.security.CustomUserDetailsService;
import finki.db.tasty_tabs.web.security.PublicUrlProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService userDetailsService;
    private final PublicUrlProvider publicUrlProvider;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    // Update the constructor
    public JwtAuthenticationFilter(JwtProvider jwtProvider, CustomUserDetailsService userDetailsService, PublicUrlProvider publicUrlProvider) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
        this.publicUrlProvider = publicUrlProvider;
    }

    // --- NEW METHOD ---
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // This method tells Spring to SKIP this filter entirely if the path matches.
        return publicUrlProvider.getPublicPaths().stream()
                .anyMatch(p -> pathMatcher.match(p, request.getServletPath()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = resolveToken(request);
            Claims claims = jwtProvider.validateToken(token);
            if (claims != null) {
                String userId = claims.getSubject();
                UserDetails userDetails = userDetailsService.loadUserByUserId(Long.parseLong(userId));

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("JWT token validated");
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.debug("JWT token expired: {}", e.getMessage());
            FilterExceptionHandler.handleException(request, response, e);
        } catch (AuthorizationDeniedException e) {
            log.debug("Authorization denied: {}", e.getMessage());
            FilterExceptionHandler.handleException(request, response, e);
        } catch (JwtException e) {
            log.debug("JWT token invalid: {}", e.getMessage());
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            FilterExceptionHandler.handleException(request, response, e);
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
