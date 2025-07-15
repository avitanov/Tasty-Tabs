package finki.db.tasty_tabs.web.security;

import finki.db.tasty_tabs.entity.*;
import finki.db.tasty_tabs.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class UserDetailsServiceImpl implements CustomUserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user by username: {}", username);
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        return getUserDetails(user);
    }

    @Override
    public UserDetails loadUserByUserId(Long userId) {
        log.debug("Loading user by ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));

        return getUserDetails(user);
    }

    private UserDetails getUserDetails(User user){


        // Convert roles to Spring Security's authorities
        Set<GrantedAuthority> authorities = new HashSet<>();
        GrantedAuthority grantedAuthority = getGrantedAuthority(user);

        authorities.add(grantedAuthority);
        log.debug("Applying role {} to user {}", grantedAuthority.getAuthority(), user.getId());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }

    private static GrantedAuthority getGrantedAuthority(User user) {
        GrantedAuthority grantedAuthority;

        if(user instanceof Employee){
            if(user instanceof Manager){
                grantedAuthority = new SimpleGrantedAuthority("ROLE_MANAGER");
            } else if(user instanceof FrontStaff){
                grantedAuthority = new SimpleGrantedAuthority("ROLE_FRONT_STAFF");
            } else if (user instanceof BackStaff){
                grantedAuthority = new SimpleGrantedAuthority("ROLE_BACKSTAFF");
            } else {
                grantedAuthority = new SimpleGrantedAuthority("ROLE_EMPLOYEE");
            }
        } else{
            grantedAuthority = new SimpleGrantedAuthority("ROLE_CUSTOMER");
        }
        return grantedAuthority;
    }
}

