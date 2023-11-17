package nz.ac.canterbury.seng302.tab.integration;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javassist.NotFoundException;
import nz.ac.canterbury.seng302.tab.entity.Activity;
import nz.ac.canterbury.seng302.tab.entity.GameActivity;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.entity.enums.ActivityResult;
import nz.ac.canterbury.seng302.tab.formobjects.ActivityForm;
import nz.ac.canterbury.seng302.tab.formobjects.PlayerScoreForm;
import nz.ac.canterbury.seng302.tab.formobjects.StatisticsForm;
import nz.ac.canterbury.seng302.tab.formobjects.SubstitutionForm;
import nz.ac.canterbury.seng302.tab.repository.ActivityRepository;
import nz.ac.canterbury.seng302.tab.service.ActivityService;
import nz.ac.canterbury.seng302.tab.service.ActivityStatisticService;
import nz.ac.canterbury.seng302.tab.service.LocationService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.TeamRoleService;
import nz.ac.canterbury.seng302.tab.service.TeamService;
import nz.ac.canterbury.seng302.tab.unit.UnitCommonTestSetup;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

/**
 * View aggregated personal stats
 */

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ViewAggregatedPersonalStatisticsFeature {

    @Autowired
    ActivityService activityService;

    @Autowired
    TeamService teamService;

    @Autowired
    TabUserService tabUserService;
    @Autowired
    TeamRoleService teamRoleService;

    @Autowired
    LocationService locationService;
    @Autowired
    ActivityStatisticService activityStatisticService;

    @Autowired
    ActivityRepository activityRepository;
    MvcResult result;
    Team team;
    Activity activity;
    Long userId;
    GameActivity gameActivity;
    @Autowired
    private MockMvc mockMvc;

    @And("I have a game activity for one of my teams")
    public void have_a_game_activity_for_one_of_my_teams() throws NotFoundException {

        team = teamService.createTeamInDatabase(
            "Bishops",
            "BasketBall",
            "Christchurch",
            "New Zealand",
            "image.jpg");

        ActivityForm form = new ActivityForm();
        form.setDescription("Please bring your mouth guard");
        form.setTeam(team.getId());
        form.setType("Game");
        form.setCity("A City");
        form.setCountry("A Country");
        form.setStartTime("2003-03-25T12:00");
        form.setEndTime("2003-03-25T13:00");

        activity = activityService.createActivityInDatabase(form);
        activity.setPosition(tabUserService.getCurrentlyLoggedIn().getId() + ",2-3");
        activityService.save(activity);
    }

    @And("I have scored points in that game")
    public void have_scored_points_in_that_game() throws NotFoundException {

        List<PlayerScoreForm> scoringEvents = Arrays.asList(
            new PlayerScoreForm(activity.getId(),
                tabUserService.getCurrentlyLoggedIn().getId(), 3, 20),
            new PlayerScoreForm(activity.getId(),
                tabUserService.getCurrentlyLoggedIn().getId(), 1, 10),
            new PlayerScoreForm(activity.getId(),
                tabUserService.getCurrentlyLoggedIn().getId(), 5, 17));

        activityService.createPlayerScoreStatisticList((GameActivity) activity, scoringEvents);
    }

    @When("I have scored {int}, {int}, {int} points in that game")
    public void have_scored_points_in_that_game(Integer int1, Integer int2, Integer int3)
        throws NotFoundException {
        List<PlayerScoreForm> scoringEvents = Arrays.asList(
            new PlayerScoreForm(activity.getId(),
                tabUserService.getCurrentlyLoggedIn().getId(), int1, 20),
            new PlayerScoreForm(activity.getId(),
                tabUserService.getCurrentlyLoggedIn().getId(), int2, 10),
            new PlayerScoreForm(activity.getId(),
                tabUserService.getCurrentlyLoggedIn().getId(), int3, 17));

        activityService.createPlayerScoreStatisticList((GameActivity) activity, scoringEvents);
    }

    @When("I am viewing that activities page")
    public void am_viewing_that_activities_page() throws Exception {

        result = mockMvc.perform(get("/viewActivity?id=" + activity.getId()).with(
                    user(tabUserService.getCurrentlyLoggedIn().getEmail()).password(
                        tabUserService.getCurrentlyLoggedIn()
                            .getPassword()).roles("USER"))
                .contentType("text/html; charset=UTF-8"))
            .andExpect(status().isOk()).andReturn();
    }


    @Then("I can see how many goals I scored with the time in that activity")
    public void can_see_how_many_goals_i_scored_with_the_time_in_that_activity()
        throws UnsupportedEncodingException {
        Assertions.assertTrue(result.getResponse().getContentAsString().contains("Scored 3 at 20"));
        Assertions.assertTrue(result.getResponse().getContentAsString().contains("Scored 1 at 10"));
        Assertions.assertTrue(result.getResponse().getContentAsString().contains("Scored 5 at 17"));
    }


    @Then("I can see no points")
    public void can_see_no_points() throws UnsupportedEncodingException {
        Assertions.assertTrue(result.getResponse().getContentAsString().contains("No Points"));

    }

    @And("I have substitution events in that game")
    public void have_substitution_events_in_that_game() {
        TabUser john = UnitCommonTestSetup.createTestUser("John");
        john.setId(3L);
        team = teamService.registerTabUserToTeam(team, john);

        List<SubstitutionForm> substitutionEvents = List.of(
            new SubstitutionForm(activity.getId(),
                tabUserService.getCurrentlyLoggedIn().getId(),
                john.getId(), 5),
            new SubstitutionForm(activity.getId(),
                john.getId(),
                tabUserService.getCurrentlyLoggedIn().getId(), 30),
            new SubstitutionForm(activity.getId(),
                tabUserService.getCurrentlyLoggedIn().getId(),
                john.getId(), 44),
            new SubstitutionForm(activity.getId(),
                john.getId(),
                tabUserService.getCurrentlyLoggedIn().getId(), 50));

        userId = tabUserService.getCurrentlyLoggedIn().getId();
        activityService.createSubstitutionStatisticList((GameActivity) activity,
            substitutionEvents, true);
    }

    @Then("I can see the total time I played")
    public void can_see_the_total_time_i_played() throws UnsupportedEncodingException {
        Assertions.assertTrue(result.getResponse().getContentAsString()
            .contains("Total Time Played (Minutes): 29"));
    }

    @Then("I have the total score {int} score in that team statistics")
    public void have_the_total_score_score_in_that_team_statistics(Integer int1)
        throws Exception {

        Assertions.assertTrue(result.getResponse().getContentAsString()
            .contains("Points Scored: " + int1.toString()));
    }

    @When("I have substituted out at {int} minutes")
    public void have_substituted_out_at_minutes(Integer minute) {

        TabUser john = tabUserService.createFullTabUserInDatabase(Arrays.asList("John", "Doe"),
                new Location("Christchurch", "New Zealand"),
            "email" + tabUserService.getFormResults().size() + "@email.com",
            new Date(2002 - 1900, 11, 31).toString(),
            "*****", "image.jpg", "Baseball");

        team = teamService.registerTabUserToTeam(team, john);

        List<SubstitutionForm> substitutionForms = List.of(
            new SubstitutionForm(activity.getId(), tabUserService.getCurrentlyLoggedIn().getId(),
                john.getId(), minute)
        );
        activityService.createSubstitutionStatisticList((GameActivity) activity, substitutionForms,
            true);
        activityRepository.save(activity);
    }

    @When("I view my profile page")
    public void view_my_profile_page() throws Exception {
        result = mockMvc.perform(get("/profilePage").with(
                    user(tabUserService.getCurrentlyLoggedIn().getEmail()).password(
                        tabUserService.getCurrentlyLoggedIn().getPassword()).roles("USER"))
                .contentType("text/html; charset=UTF-8"))
            .andExpect(status().isOk()).andReturn();
    }

    @Then("I have the overall play time of {int} minutes")
    public void have_the_overall_play_time_of_minutes(Integer int1)
        throws UnsupportedEncodingException {
        Assertions.assertTrue(result.getResponse().getContentAsString()
            .contains("Overall: " + int1.toString()));
    }

    @When("the team played for {int} activity and I played in {int} of team")
    public void the_team_played_for_activity_and_i_played_in_of_team(Integer int1, Integer int2)
        throws Exception {
        team = teamService.getTeamById(1);
        teamService.registerTabUserToTeam(team, tabUserService.getById(1));
        teamService.registerTabUserToTeam(team, tabUserService.getById(2));
        teamService.registerTabUserToTeam(team, tabUserService.getById(3));
        teamService.registerTabUserToTeam(team, tabUserService.getById(4));
        teamService.registerTabUserToTeam(team, tabUserService.getById(5));
        ActivityForm form = new ActivityForm();
        form.setDescription("Please bring your mouth guard");
        form.setTeam(team.getId());
        form.setType("Game");
        form.setCity("A City");
        form.setCountry("A Country");
        form.setStartTime("2003-03-25T12:00");
        form.setEndTime("2003-03-25T13:00");

        gameActivity = (GameActivity) activityService.createActivityInDatabase(form);
        gameActivity.setOutcome(ActivityResult.WON);
        gameActivity.setPosition("1-2");
        gameActivity = (GameActivity) activityService.save(gameActivity);


        SubstitutionForm substitutionForm = new SubstitutionForm();
        substitutionForm.setSubstitutePlayerId(2L);
        substitutionForm.setSubstitutedPlayerId(3L);
        substitutionForm.setSubstituteTime(10);
        substitutionForm.setActivityId(gameActivity.getId());
        ArrayList<SubstitutionForm> substitutionForms = new ArrayList<>();
        substitutionForms.add(substitutionForm);

        activityService.createSubstitutionStatisticList(gameActivity,
            substitutionForms, true);
    }

    @Then("I have {int} matches with the {int} matches for the team.")
    public void i_have_matches_with_the_matches_for_the_team(Integer int1, Integer int2) {
        Assertions.assertEquals(1, activityService.getTotalGamesTeamPlayed(team.getId()));
    }


    @And("I have another game activity for my team")
    public void i_have_another_game_activity_for_my_team() throws NotFoundException {

        TabUser john = tabUserService.createFullTabUserInDatabase(Arrays.asList("John", "Doe"),
                new Location("Christchurch", "New Zealand"),
            "email" + tabUserService.getFormResults().size() + "@email.com",
            new Date(2002 - 1900, 11, 31).toString(),
            "*****", "image.jpg", "Baseball");

        team = teamService.registerTabUserToTeam(team, john);


        ActivityForm form = new ActivityForm();
        form.setDescription("Please bring your mouth guard and water bottle");
        form.setTeam(team.getId());
        form.setType("Game");
        form.setCity("A City");
        form.setCountry("A Country");
        form.setStartTime("2003-03-25T12:00");
        form.setEndTime("2003-03-25T13:00");

        activity = activityService.createActivityInDatabase(form);
        activity.setPosition(tabUserService.getCurrentlyLoggedIn().getId() + "-" + john.getId());
        StatisticsForm statisticsForm = new StatisticsForm(activity.getId());
        activityService.addStatisticsForm(statisticsForm);
        activityService.save(activity);

    }

    @Then("I see the average playtime of {int} minutes")
    public void i_see_the_average_playtime_of_minutes(int minutes)
        throws UnsupportedEncodingException {
        Assertions.assertTrue(result.getResponse().getContentAsString()
            .contains("Average: " + minutes));
    }

    @Then("I see how many matches compared to all matches the team played")
    public void i_see_how_many_matches_compared_to_all_matches_the_team_played()
        throws UnsupportedEncodingException {
        Assertions.assertTrue(result.getResponse().getContentAsString()
            .contains("Matches Played: 2/2"));
    }

    @Then("I can see that I have statistics to show")
    public void i_can_see_that_i_have_statistics_to_show() {

        Assertions.assertEquals(true, Objects.requireNonNull(result.getModelAndView()).getModelMap()
            .getAttribute("hasStatistics"));
    }

    @Then("I can see that I do not have statistics to show")
    public void i_can_see_that_i_do_not_have_statistics_to_show() {
        Assertions.assertEquals(false, Objects.requireNonNull(
            result.getModelAndView()).getModelMap().getAttribute("hasStatistics"));
    }
}


