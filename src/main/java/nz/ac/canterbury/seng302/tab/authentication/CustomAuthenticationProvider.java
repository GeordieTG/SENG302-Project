package nz.ac.canterbury.seng302.tab.authentication;


import static java.util.Objects.isNull;

import jakarta.transaction.Transactional;
import java.util.Objects;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


/**
 * Custom Authentication Provider class, to allow for handling authentication in any way we see fit.
 * In this case using our existing {@link TabUser}
 */
@Component
@Transactional
public class CustomAuthenticationProvider implements AuthenticationProvider {

    /**
     * Autowired user service for custom authentication using our own user objects
     */
    @Autowired
    private TabUserService userService;

    public CustomAuthenticationProvider() {
        super();
    }

    /**
     * Custom authentication implementation
     *
     * @param authentication An implementation object that must have
     *                       non-empty email (name) and password (credentials)
     * @return A new {@link UsernamePasswordAuthenticationToken} if email and
     *         password are valid with users authorities
     */
    @Override
    public Authentication authenticate(Authentication authentication) {
        String badCreds = "Bad Credentials";
        String email = String.valueOf(authentication.getName());
        String password = String.valueOf(authentication.getCredentials());

        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            throw new BadCredentialsException(badCreds);
        }

        if (isNull(userService.getInfoByEmail(email))) {
            throw new BadCredentialsException(badCreds);
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(password, userService.getInfoByEmail(email).getPassword())
            && (!Objects.equals(password, userService.getInfoByEmail(email).getPassword()))) {
            throw new BadCredentialsException("Invalid username or password");
        }

        if ((userService.getTokenFromEmail(email) != null)) {
            throw new BadCredentialsException(badCreds);
        }

        return new UsernamePasswordAuthenticationToken(userService.getInfoByEmail(email).getEmail(),
            null, userService.getInfoByEmail(email).getAuthorities());
    }

    /**
     * Indicates whether this AuthenticationProvider supports the indicated authentication object.
     *
     * @param authentication the authentication request object to check if it is supported
     * @return true if the implementation can process the specified authentication object,
     */


    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }


}
