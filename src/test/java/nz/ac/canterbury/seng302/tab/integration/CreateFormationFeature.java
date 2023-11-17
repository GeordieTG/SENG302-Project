package nz.ac.canterbury.seng302.tab.integration;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import nz.ac.canterbury.seng302.tab.entity.Formation;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.formobjects.FormationForm;
import nz.ac.canterbury.seng302.tab.service.FormationService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.TeamService;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

/**
 * Tests for create formation
 */
@SpringBootTest
@AutoConfigureMockMvc
public class CreateFormationFeature {

    @Autowired
    FormationService formationService;

    @Autowired
    TabUserService tabUserService;
    @Autowired
    TeamService teamService;
    MvcResult result;
    Formation formation;
    Team team;
    @Autowired
    private MockMvc mockMvc;

    @When("I create a formation for my team")
    public void i_create_a_formation_for_my_team() throws Exception {

        FormationForm formationForm = new FormationForm();
        formationForm.setFormation("4-2-1-3");
        formationForm.setSport("Football");

        team = teamService.createTeamInDatabase("Test", "Test", "Test", "Test", "Test");

        result =
            mockMvc.perform(post("/createFormation").with(
                        user(tabUserService.getCurrentlyLoggedIn().getEmail()).password(
                            tabUserService.getCurrentlyLoggedIn().getPassword()).roles("USER"))
                    .with(csrf())
                    .contentType("text/html; charset=UTF-8")
                    .flashAttr("formationForm", formationForm)
                    .param("id", String.valueOf(team.getId()))
                )
                .andExpect(status().is2xxSuccessful()).andReturn();
    }

    @Then("my formation is persisted in the database")
    public void my_formation_is_persisted_in_the_database() {
        Formation formation = formationService.findAllFormationsByTeamId(team.getId()).get(0);
        Assertions.assertNotNull(formation);
        Assertions.assertEquals("4-2-1-3", formation.getFormation());
        Assertions.assertEquals("Football", formation.getField());

    }

    @When("I enter a formation form with an invalid formation")
    public void i_enter_a_formation_form_with_an_invalid_formation() throws Exception {
        FormationForm formationForm = new FormationForm();
        formationForm.setFormation("4-2-");
        formationForm.setSport("Football");

        team = teamService.createTeamInDatabase("Test", "Test", "Test", "Test", "Test");

        result = mockMvc.perform(post("/createFormation").with(
                    user(tabUserService.getCurrentlyLoggedIn().getEmail()).password(
                        tabUserService.getCurrentlyLoggedIn().getPassword()).roles("USER"))
                .with(csrf())
                .contentType("text/html; charset=UTF-8")
                .flashAttr("formationForm", formationForm)
                .param("id", String.valueOf(team.getId()))
            )
            .andExpect(status().is2xxSuccessful()).andReturn();
    }

    @Then("An error message tells me that the formation is invalid")
    public void an_error_message_tells_me_that_the_formation_is_invalid()
        throws UnsupportedEncodingException {

        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertTrue(responseBody.contains("Please follow the pattern e.g 4-3-3"));
    }

    @When("I enter a formation form with an invalid field")
    public void i_enter_a_formation_with_an_invalid_field() throws Exception {

        FormationForm formationForm = new FormationForm();
        formationForm.setFormation("4-2-2");
        formationForm.setSport("none");

        team = teamService.createTeamInDatabase("Test", "Test", "Test", "Test", "Test");

        result = mockMvc.perform(post("/createFormation").with(
                    user(tabUserService.getCurrentlyLoggedIn().getEmail()).password(
                        tabUserService.getCurrentlyLoggedIn().getPassword()).roles("USER"))
                .with(csrf())
                .contentType("text/html; charset=UTF-8")
                .flashAttr("formationForm", formationForm)
                .param("id", String.valueOf(team.getId()))
            )
            .andExpect(status().is2xxSuccessful()).andReturn();
    }

    @Then("I can view my formation graphically")
    public void i_can_view_my_formation_graphically() throws Exception {

        String htmlContent = result.getResponse().getContentAsString();
        int imageCount = count_images(htmlContent);
        //Check background and all players pfp is there
        Assertions.assertEquals(11, imageCount);
    }

    private int count_images(String htmlContent) {
        String imgPattern = "<img[^>]+>";
        int count = 0;

        Pattern pattern = Pattern.compile(imgPattern);
        Matcher matcher = pattern.matcher(htmlContent);

        while (matcher.find()) {
            count++;
        }

        return count;
    }

    @Then("An error message tells me that the field is invalid")
    public void an_error_message_tells_me_that_the_field_is_invalid()
        throws UnsupportedEncodingException {

        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertTrue(responseBody.contains("Please select a field"));
    }
}
