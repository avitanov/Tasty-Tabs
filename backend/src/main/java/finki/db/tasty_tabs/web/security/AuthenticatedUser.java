package finki.db.tasty_tabs.web.security;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * An extension of Spring Security's UserDetails to include the user's primary ID.
 */
public interface AuthenticatedUser extends UserDetails {
    Long getId();
}