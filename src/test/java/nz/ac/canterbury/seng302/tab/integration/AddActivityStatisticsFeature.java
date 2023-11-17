package nz.ac.canterbury.seng302.tab.integration;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import nz.ac.canterbury.seng302.tab.entity.Activity;
import nz.ac.canterbury.seng302.tab.entity.GameActivity;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.StatisticFact;
import nz.ac.canterbury.seng302.tab.entity.StatisticPlayerScore;
import nz.ac.canterbury.seng302.tab.entity.StatisticSubstitution;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.entity.TeamRoles;
import nz.ac.canterbury.seng302.tab.entity.enums.ActivityResult;
import nz.ac.canterbury.seng302.tab.formobjects.ActivityForm;
import nz.ac.canterbury.seng302.tab.formobjects.FactForm;
import nz.ac.canterbury.seng302.tab.formobjects.PlayerScoreForm;
import nz.ac.canterbury.seng302.tab.formobjects.ScoreForm;
import nz.ac.canterbury.seng302.tab.formobjects.StatisticsForm;
import nz.ac.canterbury.seng302.tab.formobjects.SubstitutionForm;
import nz.ac.canterbury.seng302.tab.repository.ActivityRepository;
import nz.ac.canterbury.seng302.tab.service.ActivityService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.TeamRoleService;
import nz.ac.canterbury.seng302.tab.service.TeamService;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

/**
 * Test for Joining a Team using E2E testing
 */
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class AddActivityStatisticsFeature {

    private final StatisticsForm statisticsForm = new StatisticsForm();
    private final PlayerScoreForm playerScoreForm = new PlayerScoreForm();
    private final SubstitutionForm substitutionForm = new SubstitutionForm();
    private final FactForm factForm = new FactForm();
    @Autowired
    TabUserService tabUserService;
    @Autowired
    TeamService teamService;
    @Autowired
    TeamRoleService teamRoleService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    ActivityService activityService;
    @Autowired
    ActivityRepository activityRepository;
    Activity activity;
    Team team;
    MvcResult result;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApp;
    private boolean addingSubsitutions = false;

    private boolean addingPlayerScores = false;

    private boolean addingFact = false;

    private Long substitutePlayerId;

    private Long substitutedPlayerId;

    @Given("I have an activity with the type {string}")
    public void i_have_an_activity_with_the_type(String type) throws Exception {
        team =
            teamService.createTeamInDatabase("Bla Bla", "BasketBall", "Christchurch", "New Zealand",
                "image.jpg");
        ActivityForm form = new ActivityForm();
        form.setDescription("Please bring your mouth guard");
        form.setTeam(team.getId());
        form.setType(type);
        form.setCity("A City");
        form.setCountry("A Country");
        form.setStartTime("2003-03-25T12:00");
        form.setEndTime("2003-03-26T12:00");

        activity = activityService.createActivityInDatabase(form);
        activity.setPosition(2 * team.getId() + "-" + (2 * team.getId() - 1));
        activityService.save(activity);
    }

    @Given("I have at least two players in my team")
    public void i_have_at_least_two_players_in_my_team() {
        if (team.getTeamRoles().stream().toList().size() < 2) {
            TabUser tabUser =
                tabUserService.createFullTabUserInDatabase(Arrays.asList("test", "person"),
                        new Location("Christchurch", "New Zealand"),
                        "email" + tabUserService.getFormResults().size() + "@email.com",
                    String.valueOf(new Date(2002 - 1900, 11, 31)),
                    "*****", "image.jpg", "Baseball");
            team = teamService.registerTabUserToTeam(team, tabUser);
        }
    }


    @Given("I have an activity")
    public void i_have_an_activity() throws Exception {
        team =
            teamService.createTeamInDatabase("Bla Bla", "BasketBall", "Christchurch", "New Zealand",
                "image.jpg");
        ActivityForm form = new ActivityForm();
        form.setDescription("Please bring your mouth guard");
        form.setTeam(team.getId());
        form.setType("Game");
        form.setCity("A City");
        form.setCountry("A Country");
        form.setStartTime("2003-03-25T12:00");
        form.setEndTime("2003-03-26T12:00");

        activity = activityService.createActivityInDatabase(form);

    }

    @Given("The current time is {string} the activity's start time")
    public void the_current_time_is_the_activity_start_time(String time) {
        if (time.equals("after")) {
            activity.setStartTime("2020-07-13T22:16");
        } else if (time.equals("before")) {
            activity.setStartTime("9999-07-13T22:16");
        }
        activity = activityRepository.save(activity);
    }


    @When("I navigate to my activity's view activity page")
    public void i_navigate_to_my_activitys_view_activity_page() throws Exception {
        result = mockMvc.perform(get("/viewMyActivities").with(
                    user(tabUserService.getCurrentlyLoggedIn().getEmail()).password(
                        tabUserService.getCurrentlyLoggedIn().getPassword()).roles("USER"))
                .contentType("text/html; charset=UTF-8"))
            .andExpect(status().isOk()).andReturn();
    }

    @Then("I {string} see a button for adding activity statistics")
    public void i_see_button_for_adding_activity_statistics(String able)
        throws UnsupportedEncodingException {
        if (able.equals("can")) {
            Assertions.assertTrue(
                result.getResponse().getContentAsString().contains("addStatisticsBtn"));
        } else if (able.equals("cannot")) {
            Assertions.assertFalse(
                result.getResponse().getContentAsString().contains("addStatisticsBtn"));
        }
    }

    @Then("I {string} see a button for adding activity facts")
    public void i_see_button_for_adding_activity_facts(String able)
        throws UnsupportedEncodingException {
        if (able.equals("can")) {
            Assertions.assertTrue(
                result.getResponse().getContentAsString().contains("addFactsButton"));
        } else if (able.equals("cannot")) {
            Assertions.assertFalse(
                result.getResponse().getContentAsString().contains("addFactsButton"));
        }
    }

    @When("I attempt to submit a score with two matching and valid formats")
    public void i_attempt_to_submit_a_score_with_two_matching_and_valid_formats() throws Exception {
        ScoreForm scoreForm = new ScoreForm();
        scoreForm.setActivityId(activity.getId());
        scoreForm.setHomeScore("1-1");
        scoreForm.setOppositionScore("2-1");
        scoreForm.setActivityResult(ActivityResult.LOSS);
        StatisticsForm statisticsForm = new StatisticsForm();
        statisticsForm.setScoreForm(scoreForm);
        statisticsForm.setActivityId(activity.getId());
        mockMvc.perform(post("/addStatistics")
            .with(csrf())
            .contentType("text/html; charset=UTF-8")
            .flashAttr("statisticsForm", statisticsForm)
        ).andReturn();

    }

    @Then("the score is persisted in the system")
    public void the_score_is_persisted_in_the_system() {
        GameActivity gameActivity =
            (GameActivity) activityService.getActivityById(activity.getId());
        Assertions.assertEquals("1-1", gameActivity.getHomeScore());
        Assertions.assertEquals("2-1", gameActivity.getOppositionScore());
    }

    @When("I select the name of a person who scored")
    public void i_select_the_name_of_a_person_who_scored() {
        addingPlayerScores = true;
        playerScoreForm.setActivityId(activity.getId());
        List<TabUser> players = new ArrayList<>();
        for (TeamRoles teamRoles : team.getTeamRoles()) {
            players.add(teamRoles.getUser());
        }
        playerScoreForm.setScoredPlayerId(players.get(0).getId());

    }


    @When("I input {int} as time of when they scored")
    public void i_input_time_as_time_of_when_they_scored(int time) {
        playerScoreForm.setScoreTime(time);
    }

    @When("I input {int} as how much they scored")
    public void i_input_score_as_how_much_they_scored(int score) {
        playerScoreForm.setScore(score);
    }

    @When("I submit the form")
    public void i_submit_the_form() throws Exception {
        statisticsForm.setActivityId(activity.getId());
        if (addingPlayerScores) {
            statisticsForm.getPlayerScoreForms().add(playerScoreForm);
        }
        if (addingSubsitutions) {
            statisticsForm.getSubstitutionForms().add(substitutionForm);
        }
        if (addingFact) {
            statisticsForm.getFactForms().add(factForm);
        }
        ScoreForm scoreForm = new ScoreForm();
        scoreForm.setActivityId(activity.getId());
        scoreForm.setHomeScore("100");
        scoreForm.setOppositionScore("0");
        scoreForm.setActivityResult(ActivityResult.DRAW);
        statisticsForm.setScoreForm(scoreForm);
        mockMvc.perform(post("/addStatistics")
            .with(csrf())
            .contentType("text/html; charset=UTF-8")
            .flashAttr("statisticsForm", statisticsForm)
        ).andReturn();
    }

    @When("I submit the facts form")
    public void i_submit_the_facts_form() throws Exception {
        statisticsForm.setActivityId(activity.getId());
        if (addingFact) {
            factForm.setTimeOccurred(1);
            statisticsForm.getFactForms().add(factForm);
        }
        mockMvc.perform(post("/addActivityFacts")
            .with(csrf())
            .contentType("text/html; charset=UTF-8")
            .flashAttr("statisticsForm", statisticsForm)
        ).andReturn();
    }

    @Then("The player scored details are correctly persisted in the database, "
        + "with the time, score, being {int}, {int}")
    public void the_player_scored_details_are_correctly_persisted_in_the_database(int time,
                                                                                  int score) {
        activity = activityService.getActivityById(activity.getId());
        List<StatisticPlayerScore> returnedStatisticPlayerScores =
            ((GameActivity) activity).getPlayerScoreStatistics();
        Assertions.assertFalse(returnedStatisticPlayerScores.isEmpty());
        Assertions.assertEquals(time, returnedStatisticPlayerScores.get(0).getScoredTime());
        Assertions.assertEquals(score, returnedStatisticPlayerScores.get(0).getScore());
        Assertions.assertEquals(team.getTeamRoles().iterator().next().getUser(),
            returnedStatisticPlayerScores.get(0).getUser());
    }


    @When("I select the name of the substitute")
    public void i_select_the_name_of_the_substitute() {
        substitutionForm.setActivityId(activity.getId());
        substitutePlayerId = team.getTeamRoles().stream().toList().get(0).getUser().getId();
        substitutionForm.setSubstitutePlayerId(substitutePlayerId);
    }


    @When("I select the name of the substituted player")
    public void i_select_the_name_of_the_substituted_player() {
        addingSubsitutions = true;
        substitutionForm.setActivityId(activity.getId());
        substitutedPlayerId = team.getTeamRoles().stream().toList().get(1).getUser().getId();
        activity.setPosition(substitutedPlayerId + "-" + substitutePlayerId);
        activity = activityService.save(activity);
        substitutionForm.setSubstitutedPlayerId(substitutedPlayerId);
    }


    @When("I input {int} as the minute when the substitution occurred")
    public void i_input_time_as_the_minute_when_the_substitution_occurred(int time) {
        substitutionForm.setSubstituteTime(time);
    }


    @Then("The details of the substitution are persisted in the database, with time being {int}")
    public void the_details_of_the_substitution_are_persisted_in_the_database(int expectedTime) {
        activity = activityService.getActivityById(activity.getId());
        List<StatisticSubstitution> returnedStatisticSubstitutions =
            ((GameActivity) activity).getSubstitutionStatistics();
        Assertions.assertFalse(returnedStatisticSubstitutions.isEmpty());
        Assertions.assertNotEquals(returnedStatisticSubstitutions.get(0).getSubstitutePlayer(),
            returnedStatisticSubstitutions.get(0).getSubstitutedPlayer());
        Assertions.assertEquals(substitutedPlayerId,
            returnedStatisticSubstitutions.get(0).getSubstitutedPlayer().getId());
        Assertions.assertEquals(substitutePlayerId,
            returnedStatisticSubstitutions.get(0).getSubstitutePlayer().getId());
        Assertions.assertEquals(expectedTime,
            returnedStatisticSubstitutions.get(0).getSubstituteTime());
    }

    @When("I enter {string} as the fact description")
    public void i_enter_description_as_the_fact_description(String description) {
        addingFact = true;
        factForm.setActivityId(activity.getId());
        factForm.setDescription(description);
    }

    @Then("The details of the fact are persisted in the database, with {string} as the description")
    public void details_of_the_fact_are_persisted_in_the_db_with_description_as_the_description(
        String expectedDescription) {
        activity = activityService.getActivityById(activity.getId());
        List<StatisticFact> returnedStatisticFacts = activity.getFactStatistics();
        Assertions.assertFalse(returnedStatisticFacts.isEmpty());
        Assertions.assertEquals(expectedDescription,
            returnedStatisticFacts.get(0).getDescription());
    }

    @When("I attempt to submit a score with two matching and invalid formats")
    public void i_attempt_to_submit_a_score_with_two_matching_and_invalid_formats()
        throws Exception {
        ScoreForm scoreForm = new ScoreForm();
        scoreForm.setActivityId(activity.getId());
        scoreForm.setHomeScore("10000000000000000000000000000000000000000000000");
        scoreForm.setOppositionScore("2");
        scoreForm.setActivityResult(ActivityResult.LOSS);
        StatisticsForm statisticsForm = new StatisticsForm();
        statisticsForm.setScoreForm(scoreForm);
        statisticsForm.setActivityId(activity.getId());
        mockMvc.perform(post("/addStatistics")
            .with(csrf())
            .contentType("text/html; charset=UTF-8")
            .flashAttr("statisticsForm", statisticsForm)
        ).andReturn();
    }

    @Then("data is not saved to database.")
    public void data_is_not_saved_to_database() {
        GameActivity gameActivity =
            (GameActivity) activityService.getActivityById(activity.getId());

        Assertions.assertNull(gameActivity.getHomeScore());
        Assertions.assertNull(gameActivity.getOppositionScore());
    }


}
