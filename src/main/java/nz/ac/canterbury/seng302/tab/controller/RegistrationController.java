package nz.ac.canterbury.seng302.tab.controller;

import static nz.ac.canterbury.seng302.tab.utility.HeaderHelper.addLoggedInAttr;
import static nz.ac.canterbury.seng302.tab.utility.TokenGenerator.genToken;
import static nz.ac.canterbury.seng302.tab.utility.TokenGenerator.getCurrentTime;
import static nz.ac.canterbury.seng302.tab.utility.TokenGenerator.getExpiryTimeRegistration;

import java.sql.Timestamp;
import java.util.Arrays;
import javassist.NotFoundException;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.formobjects.UserRegistrationForm;
import nz.ac.canterbury.seng302.tab.service.LocationService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.ValidationService;
import nz.ac.canterbury.seng302.tab.utility.EmailHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for form example.
 * Note the @link{Autowired} annotation giving us access to the
 * link{FormService} class automatically
 */
@Controller
public class RegistrationController {
    /**
     * Used to encrypt the password string inputted by the user
     */
    private final BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
    /**
     * To produce logs
     */
    Logger logger = LoggerFactory.getLogger(RegistrationController.class);
    /**
     * To use the email-confirmation functions
     */
    @Autowired
    EmailHelper emailHelper;
    /**
     * To use the tab user functionalities with the database
     */
    @Autowired
    private TabUserService tabUserService;
    /**
     * Used to validate the user input
     */
    @Autowired
    private ValidationService validationservice;

    /**
     * To use the location functionalities with the database
     */
    @Autowired
    private LocationService locationService;

    @Autowired
    private AuthenticationManager authenticationManager;


    /**
     * Gets the forms to be displayed, includes the ability to display results
     * of previous form when linked to from POST form
     *
     * @param model (map-like) representation of name, language and
     *              isJava boolean for use in thymeleaf
     * @return thymeleaf registration
     */
    @GetMapping("/registration")
    public String form(Model model) {
        logger.info("GET /registration");
        model.addAttribute("userRegistration", new UserRegistrationForm());
        addLoggedInAttr(model);
        return "registration";
    }

    /**
     * Posts a registration form to add a new user to the DB.
     *
     * @param userRegistration a form object holding all the users input data
     * @param url              the url of that the application is running on
     * @param model            (map-like) representation of name, language and
     *                         isJava boolean for use in thymeleaf
     * @return thymeleaf registration if any inputs were invalid,
     *         thymeleaf profilePage if all inputs were valid
     * @throws Exception thrown if an error occurs when validating the registration form
     */
    @PostMapping("/registration")
    public String submitForm(@ModelAttribute UserRegistrationForm userRegistration,
                             @RequestParam(name = "url") String url,
                             Model model) throws NotFoundException {
        logger.info("POST /registration");
        addLoggedInAttr(model);
        model.addAttribute("userRegistration", userRegistration);

        // Remove users with expired registration link
        Timestamp currentTimestamp = getCurrentTime();
        tabUserService.removeExpiredUsers(currentTimestamp);
        if (validationservice.validateRegistrationForm(userRegistration, model)) {
            TabUser user =
                tabUserService.createFullTabUserInDatabase(
                        Arrays.asList(userRegistration.getFirstName(),
                                userRegistration.getLastName()),
                       new Location(userRegistration.getCity(),
                               userRegistration.getCountry()), userRegistration.getEmail(),
                    userRegistration.getDateOfBirth(),
                    bcryptPasswordEncoder.encode(userRegistration.getPassword()),
                    "images/default.jpg", "");
            locationService.updateOptionalInDatabase(user.getLocationId(),
                userRegistration.getAddress1(), userRegistration.getAddress2(),
                userRegistration.getSuburb(), userRegistration.getPostcode());
            user.grantAuthority("ROLE_USER");
            UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(),
                    user.getAuthorities());
            // Authenticate the token properly with the CustomAuthenticationProvider
            Authentication authentication = authenticationManager.authenticate(token);
            // Check if the authentication is actually authenticated
            // (in this example any username/password is accepted so this should never be false)
            if (authentication.isAuthenticated()) {
                // Add the authentication to the current security context (Stateful)
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            // Set Registration Details in the database
            String registerToken = genToken();
            Timestamp expiryTimeStamp = getExpiryTimeRegistration();
            tabUserService.setConfirmation(user.getEmail(),
                registerToken, expiryTimeStamp);

            // Send registration email
            String urlStart = EmailHelper.getUrl(url);
            String endPoint = "registrationConfirmation?token=";
            emailHelper.sendmail(userRegistration.getEmail(), "TAB Registration Confirmation",
                urlStart + endPoint + registerToken);
            model.addAttribute("registrationEmailSent",
                "You must confirm your registration before logging in. Please check your email.");
            return "login";
        }
        return "registration";
    }
}
