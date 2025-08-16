package finki.db.tasty_tabs.web.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;

import java.io.IOException;
import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.security.SignatureException;

@Slf4j
public class FilterExceptionHandler {

    // Handle an exception within a filter using the same ErrorResponse structure.
    public static void handleException(HttpServletRequest request,
                                       HttpServletResponse response,
                                       Exception exception) throws IOException {
        // Convert exception -> ErrorResponse just like your GlobalExceptionHandler.
        ProblemDetail errorDetail = createErrorResponse(exception);
        errorDetail.setInstance(URI.create(request.getRequestURI()));

        // Write the ErrorResponse as JSON to response
        response.setStatus(errorDetail.getStatus());
        response.setContentType("application/json");

        // For JSON serialization, you can use Jackson's ObjectMapper if available.
        String json = new ObjectMapper().writeValueAsString(errorDetail);
        response.getWriter().write(json);
        response.getWriter().flush();
    }

    private static ProblemDetail createErrorResponse(Exception exception) {
        ProblemDetail detail = null;

        if (exception instanceof BadCredentialsException) {
            detail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), exception.getMessage());
            detail.setProperty("description", "The username or password is incorrect");
        } else if (exception instanceof AccountStatusException) {
            detail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
            detail.setProperty("description", "The account is locked");
        } else if (exception instanceof AccessDeniedException) {
            detail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
            detail.setProperty("description", "You are not authorized to access this resource");
        } else if (exception instanceof SignatureException) {
            detail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
            detail.setProperty("description", "The JWT signature is invalid");
        } else if (exception instanceof ExpiredJwtException) {
            detail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), exception.getMessage());
            detail.setProperty("description", "The JWT token has expired");
        } else if (exception instanceof AuthorizationDeniedException) {
            detail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
            detail.setProperty("description", "You are not authorized to access this resource");
        }

        if (detail == null) {
            // Unknown/unhandled exception
            detail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(500), exception.getMessage());
            detail.setProperty("description", "Unknown internal server error.");
        }

//        log.warn("An exception occurred: {}", exception.getMessage(), exception);

        return detail;
    }
}


