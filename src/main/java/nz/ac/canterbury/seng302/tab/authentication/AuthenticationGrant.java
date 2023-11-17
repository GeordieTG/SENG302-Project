package nz.ac.canterbury.seng302.tab.authentication;

import nz.ac.canterbury.seng302.tab.entity.TabUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Class that grants the user authority on the website, used mainly in tests
 */
public class AuthenticationGrant {

    private AuthenticationGrant() {
        // Empty Private constructor to hide publicly implicit constructor
    }

    /**
     * Grants authentication to a given users
     *
     * @param user                  user entity that requires authentication
     * @param authenticationManager Manager to grant authority
     */
    public static void authenticateUser(TabUser user, AuthenticationManager authenticationManager) {
        user.grantAuthority("ROLE_USER");
        UsernamePasswordAuthenticationToken token =
            new UsernamePasswordAuthenticationToken(user.getEmail(), "*****",
                user.getAuthorities());
        // Authenticate the token properly with the CustomAuthenticationProvider
        Authentication authentication = authenticationManager.authenticate(token);
        // Check if the authentication is actually authenticated
        // (in this example any username/password is accepted so this should never be false)
        if (authentication.isAuthenticated()) {
            // Add the authentication to the current security context (Stateful)
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }
}
