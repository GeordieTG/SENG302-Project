package nz.ac.canterbury.seng302.tab.unit.controller;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.sql.Date;
import java.util.Arrays;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.ValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserProfileControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TabUserService tabUserService;


    @Autowired
    private ValidationService validationservice;

    @Autowired
    private WebApplicationContext webApp;

    @Autowired
    private AuthenticationManager authenticationManager;


    /**
     * Setup before each test
     */
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApp).build();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!tabUserService.getByEmail("email@email.com")) {
            TabUser user =
                tabUserService.createFullTabUserInDatabase(Arrays.asList("test", "person"),
                        new Location("City", "Country"),
                    "email@email.com", new Date(2002 + 1900, 11, 2)
                        .toString(), encoder.encode("*****"),
                    "image.jpg", "Baseball");

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

    /**
     * Test for invalid name string entries
     */
    @Test
    void testCheckNames() {
        // Setup
        String validFirstName = "John";
        String validLastName = "Doe";
        Model model = new ConcurrentModel();

        boolean result = validationservice.checkNames(validFirstName, validLastName, model);
        assertTrue(result);

        String invalidFirstName = "John@";
        result = validationservice.checkNames(invalidFirstName, validLastName, model);
        assertFalse(result);

        String invalidLastName = "Doe?";
        result = validationservice.checkNames(validFirstName, invalidLastName, model);
        assertFalse(result);

        // Test multiple invalid characters
        invalidFirstName = "John@Doe?";
        result = validationservice.checkNames(invalidFirstName, validLastName, model);
        assertFalse(result);
    }


    /**
     * Tests that the password controller for changing the password (updatePassword) correctly
     * changes the currently logged-in user's password to the password parameter, when the
     * oldPassword parameter is correct
     * (When oldPassword matches the current user's current password)
     *
     * @throws Exception throws if any errors occur
     */
    @Test
    @WithMockUser(username = "email@email.com", password = "*****", authorities = "USER")
    void givenCorrectOldPassword_changingPassword_PasswordChanged() throws Exception {
        String oldPassword = "*****";
        String newPassword = "Fartburger";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        mockMvc.perform(
            post("/changePassword").param("oldPassword", "Sukkon").param("password", newPassword)
                .contentType("text/html; charset=UTF-8"));
        boolean encoderCheck =
            encoder.matches(oldPassword, tabUserService.getCurrentlyLoggedIn().getPassword());
        assertTrue(encoderCheck);
    }


    /**
     * Tests that the password controller for changing the password (updatePassword) correctly
     * doesn't change thecurrently logged-in user's password to the password parameter,
     * when the oldPassword parameter is incorrect
     * (When oldPassword doesn't match the current user's current password)
     *
     * @throws Exception throws if any errors occur
     */
    @Test
    @WithMockUser(username = "email@email.com", password = "*****", authorities = "USER")
    void givenIncorrectOldPassword_changingPassword_PasswordUnchanged() throws Exception {
        String oldPassword = "WrongPassword";
        String newPassword = "Fartburger";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        mockMvc.perform(
            post("/changePassword").param("oldPassword", oldPassword).param("password", newPassword)
                .contentType("text/html; charset=UTF-8"));
        boolean encoderCheck =
            !encoder.matches(newPassword, tabUserService.getCurrentlyLoggedIn().getPassword());
        assertTrue(encoderCheck);
    }
}
