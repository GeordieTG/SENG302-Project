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
 * Garmin Data feature class
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ViewGarminDataFeature {

    ResultActions result;
    @Autowired
    private MockMvc mockMvc;

    @When("I am on my profile page without being connected to my garmin watch")
    public void i_am_on_my_profile_page_without_being_connected_to_my_garmin_watch()
        throws Exception {
        result = mockMvc.perform(get("/profilePage").with(
                user(IntegrationTestConfigurations.loggedInUser))
            .contentType("text/html; charset=UTF-8"));

        result.andExpect(status().isOk()).andReturn();

    }

    @Then("the garmin statistics display is overlayed with a blur effect")
    public void the_garmin_statistics_display_is_overlayed_with_a_blur_effect()
        throws UnsupportedEncodingException {
        Assertions.assertTrue(result.andReturn().getResponse()
            .getContentAsString().contains("garminOverlay"));
    }
}
