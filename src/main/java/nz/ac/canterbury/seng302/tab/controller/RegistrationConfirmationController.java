package nz.ac.canterbury.seng302.tab.controller;

import static nz.ac.canterbury.seng302.tab.utility.HeaderHelper.addLoggedInAttr;
import static nz.ac.canterbury.seng302.tab.utility.TokenGenerator.isTokenExpired;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.utility.EmailHelper;
import nz.ac.canterbury.seng302.tab.utility.TokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * End Points for the Registration Confirmation functionality.
 */
@Controller
public class RegistrationConfirmationController {

    /**
     * Allows us to access email sending functionality
     */
    @Autowired
    EmailHelper emailHelper;
    Logger logger = LoggerFactory.getLogger(RegistrationConfirmationController.class);
    /**
     * Used to authorize the user
     */
    @Autowired
    private AuthenticationManager authenticationManager;
    /**
     * Used in the authorization process
     */
    @Autowired
    private HttpServletRequest request;
    /**
     * Allows us to access user functionality with the database
     */
    @Autowired
    private TabUserService tabUserService;

    /**
     * Authorizes the user after the link has been pushed
     *
     * @param user     The user to be authorized
     * @param password The users plaintext password
     */
    public void authorizeUser(TabUser user, String password) {
        user.grantAuthority("ROLE_USER");
        tabUserService.updateUser(user);

        // Create a new Authentication with Username and
        // Password (authorities here are optional as the following function fetches these anyway)
        new UsernamePasswordAuthenticationToken(user.getEmail(), password,
            user.getAuthorities());
        // Authenticate the token properly with the CustomAuthenticationProvider
        // Check if the authentication is actually authenticated
        // (in this example any username/password is accepted so this should never be false)

    }

    /**
     * Get end point for the registration confirmation functionality. Called when the link is pushed
     *
     * @param token    The users unique registration token.
     * @param response Used to modify the 404 error page
     * @param model    Thymeleaf model
     * @return Error page if link is invalid, or login page if valid.
     */
    @GetMapping("/registrationConfirmation")
    public String registrationConfirmation(@RequestParam("token") String token,
                                           HttpServletResponse response, Model model) {
        logger.info("GET /registrationConfirmation");
        Timestamp expiryTime = tabUserService.getTokenExpiryTime(token);
        addLoggedInAttr(model);
        tabUserService.removeExpiredUsers(TokenGenerator.getCurrentTime());

        if (expiryTime == null || isTokenExpired(expiryTime)) {
            logger.info("test");
            response.setStatus(404);
            model.addAttribute("status", response.getStatus());
            model.addAttribute("message", "Token missing or expired");
            return ("error");
        }

        TabUser user = tabUserService.getUserbyToken(token);
        tabUserService.removeConfirmationDetails(user.getEmail());
        String password = user.getPassword();
        authorizeUser(user, password);
        model.addAttribute("activatedEmail", "Account has been activated");
        return ("login");
    }
}
