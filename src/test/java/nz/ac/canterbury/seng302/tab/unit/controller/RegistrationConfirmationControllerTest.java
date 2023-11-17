package nz.ac.canterbury.seng302.tab.unit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.unit.UnitCommonTestSetup;
import nz.ac.canterbury.seng302.tab.utility.TokenGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test the registration confirmation controller flows.
 * (What happens after a user confirms their registration with a
 * link from their email)
 */
@SpringBootTest
@AutoConfigureMockMvc
class RegistrationConfirmationControllerTest {

    String date = new Date(2002 + 1900, 11, 31).toString();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TabUserService tabUserService;

    /**
     * Ensures that a 404 error is returned if an invalid token is used
     *
     * @throws Exception Throws exception if MockMVC fails
     */
    @Test
    void registrationConfirmationInvalidToken() throws Exception {

        // Create random token thats not in the database
        String token = TokenGenerator.genToken();
        // Confirm registration
        mockMvc.perform(get("/registrationConfirmation")
                .contentType("text/html; charset=UTF-8")
                .param("token", token))
            .andExpect(status().is4xxClientError());
    }

    /**
     * Ensures a 404 error is returned if an expired token is used
     *
     * @throws Exception Throws exception if MockMVC fails
     */
    @Test
    void registrationConfirmationExpiredToken() throws Exception {

        tabUserService.createFullTabUserInDatabase(Arrays.asList("Geordie", "Gibson"),
                new Location("Greymouth", "New Zealand"),
            "gibbowc@gmail.com", date, "Qwerty10!",
                "default_image.png", "Basketball");
        String token = TokenGenerator.genToken();
        Timestamp expiryTime = TokenGenerator.getCurrentTime();
        tabUserService.setConfirmation("gibbowc@gmail.com", token, expiryTime);

        doNothing().when(tabUserService).removeExpiredUsers(any());

        // Confirm registration
        mockMvc.perform(get("/registrationConfirmation")
                .contentType("text/html; charset=UTF-8")
                .param("token", token))
            .andExpect(status().is4xxClientError());
    }

    /**
     * Ensures an OK status is returned after successful confirmation
     *
     * @throws Exception Throws exception if MockMVC fails
     */
    @Test
    void registrationConfirmationSuccess() throws Exception {

        String token = TokenGenerator.genToken();
        TabUser user = UnitCommonTestSetup.createTestUser();

        doNothing().when(tabUserService).removeExpiredUsers(any());
        when(tabUserService.getTokenExpiryTime(any())).thenReturn(TokenGenerator
            .getExpiryTimeRegistration());
        when(tabUserService.getUserbyToken(any())).thenReturn(user);
        doNothing().when(tabUserService).removeConfirmationDetails(any());

        // Confirm registration
        mockMvc.perform(get("/registrationConfirmation")
                .contentType("text/html; charset=UTF-8")
                .param("token", token))
            .andExpect(status().isOk());
    }
}
