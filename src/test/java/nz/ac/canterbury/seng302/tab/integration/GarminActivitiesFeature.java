package nz.ac.canterbury.seng302.tab.integration;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.UnsupportedEncodingException;
import nz.ac.canterbury.seng302.tab.entity.Activity;
import nz.ac.canterbury.seng302.tab.service.ActivityService;
import nz.ac.canterbury.seng302.tab.service.GarminService;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

/**
 * Integration Test for garmin activities feature
 */
@SpringBootTest
@AutoConfigureMockMvc
public class GarminActivitiesFeature {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ActivityService activityService;
    @Autowired
    GarminService garminService;
    Activity activity;
    MvcResult mvcResult;
    @Autowired
    CommonStepsIntegration commonStepsIntegration;

    @And("I have an activity for garmin activity")
    public void i_have_an_activity_for_garmin_activity() {
        activity = activityService.save(commonStepsIntegration.createTestActivity(
            "Boys", "Mustapha",
            commonStepsIntegration.createTestLocation("chch", "nz")));
    }

    @When("I save a selected Garmin Activity")
    public void i_save_a_selected_garmin_activity() throws Exception {
        String json = "{"
            + "\"activityId\": 12345,"
            + "\"durationInSeconds\": 3600,"
            + "\"activityType\": \"Running\","
            + "\"activeKilocalories\": 500,"
            + "\"startTime\": \"2023-09-22T12:00:00Z\","
            + "\"distanceInMeters\": 10000.0"
            + "}";
        mvcResult = mockMvc.perform(post("/saveGarminActivity/" + activity.getId())
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
        ).andReturn();

    }

    @Then("the garmin activity is persisted in the database")
    public void the_garmin_activity_is_persisted_in_the_database()
        throws UnsupportedEncodingException {
        Assertions.assertEquals("", mvcResult.getResponse().getContentAsString());
        Assertions.assertEquals(200, mvcResult.getResponse().getStatus());

    }
}