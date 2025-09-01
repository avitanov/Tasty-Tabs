package finki.db.tasty_tabs.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class HttpLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse resp,
                                    FilterChain chain)
            throws ServletException, IOException {

        long start = System.currentTimeMillis();
        try {
            chain.doFilter(req, resp);
        } finally {
            MDC.put("userId", resolveUserId());

            // — compute duration —
            long timeMs = System.currentTimeMillis() - start;
            MDC.put("timeMs", String.valueOf(timeMs));

            // — log it —
//            log.info("Request: {} {} {} from {} – {}ms",
//                    resp.getStatus(),
//                    req.getMethod(),
//                    req.getRequestURI(),
//                    req.getRemoteAddr(),
//                    timeMs);

            // — cleanup —
            MDC.clear();
        }
    }

    private String resolveUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "ANONYMOUS";
        }
        return ((UserDetails) authentication.getPrincipal()).getUsername();
    }
}

