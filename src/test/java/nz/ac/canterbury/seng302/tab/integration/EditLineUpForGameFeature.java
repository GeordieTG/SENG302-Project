package nz.ac.canterbury.seng302.tab.integration;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javassist.NotFoundException;
import nz.ac.canterbury.seng302.tab.entity.Activity;
import nz.ac.canterbury.seng302.tab.entity.GameActivity;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.entity.enums.ActivityResult;
import nz.ac.canterbury.seng302.tab.formobjects.ActivityForm;
import nz.ac.canterbury.seng302.tab.formobjects.FormationForm;
import nz.ac.canterbury.seng302.tab.service.ActivityService;
import nz.ac.canterbury.seng302.tab.service.ActivityStatisticService;
import nz.ac.canterbury.seng302.tab.service.FormationService;
import nz.ac.canterbury.seng302.tab.service.LocationService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.TeamRoleService;
import nz.ac.canterbury.seng302.tab.service.TeamService;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.ModelAndView;

/**
 * Edit formation for game E2E tests
 */
@SpringBootTest
@AutoConfigureMockMvc
public class EditLineUpForGameFeature {

    @Autowired
    FormationService formationService;

    @Autowired
    TabUserService tabUserService;
    @Autowired
    TeamService teamService;

    @Autowired
    ActivityService activityService;

    @Autowired
    TeamRoleService teamRoleService;

    @Autowired
    LocationService locationService;

    @Autowired
    ActivityStatisticService activityStatisticService;

    int totalGamesBefore;

    MvcResult result;
    Team team;
    Activity activity;
    GameActivity gameActivity;
    Long activityId;
    Long formationId;
    @Autowired
    private MockMvc mockMvc;

    @Given("I have activity data")
    public void have_activity_data() throws Exception {
        team = teamService.createTeamInDatabase("Test", "Test",
            "Test", "Test", "Test");

        TabUser
            secondUser = tabUserService.createFullTabUserInDatabase(Arrays.asList("test",
            "person"), new Location("Christchurch", "New Zealand"), "email@email.com",
                new Date(2002 + 1900, 11, 31).toString(), "*****",
            "image.jpg", "Baseball");

        teamService.registerTabUserToTeam(team, secondUser);
        FormationForm formationForm = new FormationForm();
        formationForm.setFormation("4-3-3");
        formationForm.setSport("Rugby");
        formationService.addFormation(formationForm, team.getId());
        formationId = formationService.findAllFormationsByTeamId(team.getId())
            .get(0).getFormationId();
        formationForm.setFormation("5-2-3");
        formationService.addFormation(formationForm, team.getId());

        ActivityForm form = new ActivityForm();
        form.setDescription("Please bring your mouth guard");
        form.setTeam(team.getId());
        form.setType("Game");
        Location location =
            locationService.createLocationInDatabase("Japan", "Not Japan");
        form.setCity(location.getCity());
        form.setCountry(location.getCountry());
        form.setStartTime("2003-03-25T12:00");
        form.setEndTime("2003-03-26T12:00");
        Activity activity = activityService.createActivityInDatabase(form);
        activityId = activity.getId();
        activityService.updateHomeScore(activityId, "3");
        activityService.updateOppositionScore(activityId, "1");
    }

    @When("I am viewing a game activity")
    public void i_am_viewing_a_game_activity() throws Exception {
        result = mockMvc.perform(get("/viewActivity")
                .contentType("text/html; charset=UTF-8")
                .param("id", String.valueOf(activityId)))
            .andExpect(status().isOk()).andReturn();
    }

    @Then("I am given the option to see the members of my team to put in the lineup")
    public void i_am_given_the_option_to_see_the_members_of_my_team_to_put_in_the_lineup() {

        ModelAndView mav = result.getModelAndView();

        Activity activity = (Activity) mav.getModel().get("activity");
        assertTrue(activity.getTeam().getTeamRoles().size() >= 2);

    }

    @When("I set up a lineup for the activity and click the save button")
    public void i_set_up_a_lineup_for_the_activity_and_click_the_save_button() throws Exception {
        team = teamService.createTeamInDatabase("Test", "Test", "Test", "Test", "Test");

        TabUser secondUser =
            tabUserService.createFullTabUserInDatabase(Arrays.asList("test", "person"),
                    new Location("Christchurch", "New Zealand"),
                    "email@email.com", new Date(2002 + 1900, 11, 31).toString(),
                "*****", "image.jpg", "Baseball");

        teamService.registerTabUserToTeam(team, secondUser);

        ActivityForm form = new ActivityForm();
        form.setDescription("Please bring your mouth guard");
        form.setTeam(team.getId());
        form.setType("Game");
        Location location = locationService.createLocationInDatabase("Japan", "Not Japan");
        form.setCity(location.getCity());
        form.setCountry(location.getCountry());
        form.setStartTime("2003-03-25T12:00");
        form.setEndTime("2003-03-26T12:00");
        String formation = "1-2,3-4,5,6";

        activity = activityService.createActivityInDatabase(form);

        mockMvc.perform(post("/createLineup").with(
                    user(tabUserService.getCurrentlyLoggedIn().getEmail()).password(
                        tabUserService.getCurrentlyLoggedIn().getPassword()).roles("USER"))
                .with(csrf())
                .contentType("text/html; charset=UTF-8")
                .param("saveFormation", formation)
                .param("formationId", "1")
                .param("activityId", String.valueOf(activity.getId())))

            .andExpect(status().isOk());

    }

    @Then("I can see the lineup saved in the database")
    public void i_can_see_the_lineup_saved_in_the_database() {
        Activity activityFromDatabase = activityService.getActivityById(activity.getId());
        Assertions.assertEquals("1-2,3-4,5,6", activityFromDatabase.getPosition());
    }

    @Then("I can add a line-up from the list of existing formations for that team.")
    public void can_add_line_up_from_list_of_existing_formations_for_that_team()
        throws UnsupportedEncodingException {
        String content = result.getResponse().getContentAsString();
        Assertions.assertTrue(content.contains("<option value=\"3\">4-3-3 (Rugby)</option>"));
        Assertions.assertTrue(content.contains("<option value=\"4\">5-2-3 (Rugby)</option>"));
    }

    @When("I select a formation")
    public void i_select_a_formation() throws Exception {
        result = mockMvc.perform(post("/previewLineup")
                .with(user(tabUserService.getCurrentlyLoggedIn().getEmail())
                    .password(tabUserService.getCurrentlyLoggedIn().getPassword()).roles("USER"))
                .with(csrf())
                .contentType("text/html; charset=UTF-8")
                .flashAttr("formation", "4-3-3:Rugby")
                .param("formationId", String.valueOf(formationId))
                .param("id", String.valueOf(activityId)))
            .andExpect(status().isOk()).andReturn();

    }

    @Then("the formation is displayed in the activity page.")
    public void the_formation_is_displayed_in_the_activity_page()
        throws UnsupportedEncodingException {
        String content = result.getResponse().getContentAsString();
        // Assert 16 as 4-3-3 = 4+3+3 profile images = 10
        // + background image = 11
        // + 5 Substitutes
        // + 2 for home and away team logos
        Assertions.assertEquals(18, count_images(content));
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

    @And("I have an activity with statistics and no lineup")
    public void have_an_activity_with_statistics_and_no_lineup() throws NotFoundException {
        team = teamService.createTeamInDatabase("Test", "Test", "Test", "Test", "Test");

        teamService.registerTabUserToTeam(team, IntegrationTestConfigurations.loggedInUser);

        ActivityForm form = new ActivityForm();
        form.setDescription("Please bring your mouth guard");
        form.setTeam(team.getId());
        form.setType("Game");
        Location location = locationService.createLocationInDatabase("Japan", "Not Japan");
        form.setCity(location.getCity());
        form.setCountry(location.getCountry());
        form.setStartTime("2003-03-25T12:00");
        form.setEndTime("2003-03-26T12:00");

        activity = activityService.createActivityInDatabase(form);

        ((GameActivity) activity).setOutcome(ActivityResult.LOSS);
        activity = activityService.save(activity);

        GameActivity gameActivity = activityService.getGameActivity(activity.getId());
        Assertions.assertEquals(ActivityResult.LOSS, gameActivity.getOutcome());
    }

    @When("I set up a lineup for my activity")
    public void set_up_a_lineup_for_my_activity() throws Exception {

        totalGamesBefore = teamRoleService.getTeamRoles(team.getId(),
            IntegrationTestConfigurations.loggedInUser.getId()).getTotalGames();

        String formation = "1-2,3-4,5,6";

        mockMvc.perform(post("/createLineup").with(
                    user(tabUserService.getCurrentlyLoggedIn().getEmail()).password(
                        tabUserService.getCurrentlyLoggedIn().getPassword()).roles("USER"))
                .with(csrf())
                .contentType("text/html; charset=UTF-8")
                .param("saveFormation", formation)
                .param("formationId", "1")
                .param("activityId", String.valueOf(activity.getId())))

            .andExpect(status().isOk());
    }

    @Then("my total games should increase")
    public void my_total_games_should_increase() {

        int totalGamesAfter = teamRoleService.getTeamRoles(team.getId(),
            IntegrationTestConfigurations.loggedInUser.getId()).getTotalGames();

        Assertions.assertEquals(totalGamesBefore + 1, totalGamesAfter);
    }
}
