package finki.db.tasty_tabs.aspect.annotation;

import finki.db.tasty_tabs.aspect.annotation.CheckOnDuty;
import finki.db.tasty_tabs.entity.exceptions.EmployeeNotOnDutyException;
import finki.db.tasty_tabs.service.EmployeeService;
import finki.db.tasty_tabs.web.security.AuthenticatedUser;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class OnDutyAspect {

    private final EmployeeService employeeService;

    public OnDutyAspect(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * This advice runs before any method annotated with @CheckOnDuty.
     * It retrieves the currently logged-in user from the SecurityContext,
     * checks if they are on an active shift, and throws an exception if not.
     */
    @Before("@annotation(finki.db.tasty_tabs.aspect.annotation.CheckOnDuty)")
    public void checkIsOnDuty() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the user is authenticated at all
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof AuthenticatedUser currentUser)) {
            throw new InsufficientAuthenticationException("User must be authenticated to perform this action.");
        }

        // Get the custom principal object
        Long employeeId = currentUser.getId();

        if (!employeeService.isOnActiveShift(employeeId)) {
            throw new EmployeeNotOnDutyException("Employee with ID " + employeeId + " is not on an active shift.");
        }
    }
}