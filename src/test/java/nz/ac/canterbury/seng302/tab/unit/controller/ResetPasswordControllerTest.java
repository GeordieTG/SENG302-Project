package nz.ac.canterbury.seng302.tab.unit.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.utility.TokenGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Tests the reset password controller
 */
@SpringBootTest
@AutoConfigureMockMvc
class ResetPasswordControllerTest {

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    String date = new Date(2002 + 1900, 11, 31).toString();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TabUserService tabUserService;


    @BeforeEach
    void setUp() {
        tabUserService.deleteAllData();
    }

    /**
     * Ensures that the 404 page is returned when in invalid token is entered
     *
     * @throws Exception Throws exception if MockMVC fails
     */
    @Test
    void resetPasswordInvalidToken() throws Exception {

        // Create random token thats not in the database
        String token = TokenGenerator.genToken();
        // Confirm registration
        mockMvc.perform(get("/resetPassword")
                .contentType("text/html; charset=UTF-8")
                .param("token", token))
            .andExpect(status().is4xxClientError());
    }

    /**
     * Ensures a 404 page is returned if an expired token is used
     *
     * @throws Exception Throws exception if MockMVC fails
     */
    @Test
    void resetPasswordExpiredToken() throws Exception {

        tabUserService.createFullTabUserInDatabase(Arrays.asList("Geordie", "Gibson"),
                new Location("Greymouth", "New Zealand"), "gibbowc@gmail.com",
                date, "Qwerty10!", "default_image.png",
                "Basketball");
        String token = TokenGenerator.genToken();
        Timestamp expiryTime = TokenGenerator.getCurrentTime();
        tabUserService.setResetPasswordDetails("gibbowc@gmail.com", token, expiryTime);

        mockMvc.perform(get("/resetPassword")
                .contentType("text/html; charset=UTF-8")
                .param("token", token))
            .andExpect(status().is4xxClientError());
    }

    /**
     * Checks that all expired users are removed after two hours
     *
     * @throws Exception Throws exception if MockMVC fails
     */
    @Test
    void resetPasswordCheckExpiredUsersRemoved() throws Exception {

        // User 1 - Expired
        tabUserService.createFullTabUserInDatabase(Arrays.asList("Geordie", "Gibson"),
                new Location("Greymouth", "New Zealand"),
            "gibbowc@gmail.com", date, "Qwerty10!",
                "default_image.png", "Basketball");
        String token = TokenGenerator.genToken();
        Timestamp expiryTime = TokenGenerator.getCurrentTime();
        tabUserService.setResetPasswordDetails("gibbowc@gmail.com", token, expiryTime);

        // User 2 - Expired
        tabUserService.createFullTabUserInDatabase(Arrays.asList("Tom", "Gibson"),
                new Location("Greymouth", "New Zealand"),
            "team400seng302@gmail.com", date, "Qwerty10!",
                "default_image.png", "Basketball");
        String token2 = TokenGenerator.genToken();
        Timestamp expiryTime2 = TokenGenerator.getCurrentTime();
        tabUserService.setResetPasswordDetails("team400seng302@gmail.com", token2, expiryTime2);

        // User 3 - Valid
        tabUserService.createFullTabUserInDatabase(Arrays.asList("Bob", "Gibson"),
                new Location("Greymouth", "New Zealand"),
            "test@gmail.com", date, "Qwerty10!",
                "default_image.png", "Basketball");
        String token3 = TokenGenerator.genToken();
        Timestamp expiryTime3 = TokenGenerator.getExpiryTimeOneHour();
        tabUserService.setResetPasswordDetails("test@gmail.com", token3, expiryTime3);

        List<TabUser> initialList = tabUserService.getFormResults();

        int initialSize = 0;
        for (TabUser tabUser : initialList) {
            if ((tabUser.getResetPasswordToken() == null)
                && (tabUser.getResetPasswordExpiryTime() == null)) {
                initialSize += 1;
            }
        }

        mockMvc.perform(get("/resetPassword")
                .contentType("text/html; charset=UTF-8")
                .param("token", token))
            .andExpect(status().is4xxClientError());

        List<TabUser> afterList = tabUserService.getFormResults();

        int counter = 0;
        for (TabUser tabUser : afterList) {
            if ((tabUser.getResetPasswordToken() == null)
                && (tabUser.getResetPasswordExpiryTime() == null)) {
                counter += 1;
            }
        }
        assertEquals(initialSize + 2, counter);
    }

    /**
     * Ensures that an OK response is returned on successful rest
     *
     * @throws Exception Throws exception if MockMVC fails
     */
    @Test
    void resetPasswordSuccess() throws Exception {
        tabUserService.createFullTabUserInDatabase(Arrays.asList("Geordie", "Gibson"),
                new Location("Greymouth", "New Zealand"),
            "gibbowc@gmail.com", date, "Qwerty10!",
                "default_image.png", "Basketball");
        String token = TokenGenerator.genToken();
        Timestamp expiryTime = TokenGenerator.getExpiryTimeOneHour();
        tabUserService.setResetPasswordDetails("gibbowc@gmail.com", token, expiryTime);

        mockMvc.perform(get("/resetPassword")
                .contentType("text/html; charset=UTF-8")
                .param("token", token))
            .andExpect(status().isOk());
    }

    /**
     * Ensures that the users password is reset to the new password
     *
     * @throws Exception Throws exception if MockMVC fails
     */
    @Test
    void resetPasswordTest() throws Exception {

        tabUserService.createFullTabUserInDatabase(Arrays.asList("Geordie", "Gibson"),
                new Location("Greymouth", "New Zealand"),
            "gibbowc@gmail.com", date, "Qwerty10!",
                "default_image.png", "Basketball");
        String token = TokenGenerator.genToken();
        Timestamp expiryTime = TokenGenerator.getExpiryTimeOneHour();
        tabUserService.setResetPasswordDetails("gibbowc@gmail.com", token, expiryTime);

        // Confirm registration
        mockMvc.perform(post("/resetPassword")
                .contentType("text/html; charset=UTF-8")
                .with(csrf())
                .param("token", token)
                .param("password", "Password123`")
                .param("confirmPassword", "Password123`")
                .param("email", "gibbowc@gmail.com"))
            .andExpect(status().isOk());

        TabUser user = tabUserService.getInfoByEmail("gibbowc@gmail.com");
        assertTrue(encoder.matches("Password123`", user.getPassword()));
    }
}