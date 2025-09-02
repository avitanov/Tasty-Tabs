package finki.db.tasty_tabs.web.security;

import finki.db.tasty_tabs.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

// Implement the interface we defined earlier
public class CustomUserDetails implements AuthenticatedUser {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    // --- This is the custom method our Aspect needs ---
    @Override
    public Long getId() {
        return user.getId();
    }
    
    // --- Below are the standard UserDetails methods ---
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // You should map your employee type/role to a Spring Security authority
        // The "ROLE_" prefix is a standard convention.
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getUserType().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        // The "username" in this context is the email
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}