package nz.ac.canterbury.seng302.tab.integration;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.UnsupportedEncodingException;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

/**
 * Integration test for connecting to Garmin
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ConnectGarminWatchFeature {

    ResultActions result;
    @Autowired
    private MockMvc mockMvc;

    @When("I am on my User Profile page")
    public void am_on_my_user_profile_page() throws Exception {
        result = mockMvc.perform(get("/profilePage").with(
                user(IntegrationTestConfigurations.loggedInUser))
            .contentType("text/html; charset=UTF-8"));

        result.andExpect(status().isOk()).andReturn();

    }

    @Then("I can see a dedicated UI element, for connecting to the Garmin watch")
    public void can_see_dedicated_ui_element_for_connecting_to_the_garmin_watch()
        throws UnsupportedEncodingException {
        Assertions.assertTrue(result.andReturn().getResponse()
            .getContentAsString().contains("garminBtn"));
    }
}
