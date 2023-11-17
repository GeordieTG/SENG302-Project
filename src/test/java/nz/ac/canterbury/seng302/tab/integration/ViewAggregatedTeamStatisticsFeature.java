package nz.ac.canterbury.seng302.tab.integration;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javassist.NotFoundException;
import nz.ac.canterbury.seng302.tab.entity.Activity;
import nz.ac.canterbury.seng302.tab.entity.GameActivity;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.entity.TeamRoles;
import nz.ac.canterbury.seng302.tab.entity.enums.ActivityResult;
import nz.ac.canterbury.seng302.tab.formobjects.ActivityForm;
import nz.ac.canterbury.seng302.tab.formobjects.PlayerScoreForm;
import nz.ac.canterbury.seng302.tab.formobjects.StatisticsForm;
import nz.ac.canterbury.seng302.tab.formobjects.SubstitutionForm;
import nz.ac.canterbury.seng302.tab.service.ActivityService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.TeamRoleService;
import nz.ac.canterbury.seng302.tab.service.TeamService;
import nz.ac.canterbury.seng302.tab.service.TeamStatsService;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

/**
 * Integration tests for the calculation and display of aggregated team statistics
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ViewAggregatedTeamStatisticsFeature {

    @Autowired
    TabUserService tabUserService;
    @Autowired
    TeamService teamService;
    @Autowired
    ActivityService activityService;
    @Autowired
    TeamRoleService teamRoleService;
    @Autowired
    TeamStatsService teamStatsService;
    Team team;
    MvcResult result;
    @Autowired
    private MockMvc mockMvc;

    @When("I am viewing my teams page")
    public void i_am_viewing_my_teams_page() throws Exception {

        result = mockMvc.perform(get("/viewTeam")
                .with(csrf())
                .contentType("text/html; charset=UTF-8")
                .param("id", String.valueOf(team.getId())))
            .andExpect(status().isOk()).andReturn();
    }

    @Then("I can see their win, loss, draws and total games played all as 0")
    public void i_can_see_their_win_loss_draws_and_total_games_played_all_as_zero() {

        int wins = (int) result.getModelAndView().getModel().get("teamWins");
        int draws = (int) result.getModelAndView().getModel().get("teamDraws");
        int losses = (int) result.getModelAndView().getModel().get("teamLosses");

        Assertions.assertEquals(0, wins);
        Assertions.assertEquals(0, draws);
        Assertions.assertEquals(0, losses);
    }

    @Then("a {string} is recorded for my team")
    public void a_is_recorded_for_my_team(String outcome) throws Exception {

        ActivityForm form = new ActivityForm();
        form.setDescription("Please bring your mouth guard");
        form.setTeam(team.getId());
        form.setType("Game");
        form.setCity("A City");
        form.setCountry("A Country");
        form.setStartTime("2003-03-25T12:00");
        form.setEndTime("2003-03-26T12:00");

        GameActivity activity = (GameActivity) activityService.createActivityInDatabase(form);
        if (outcome.equals("win")) {
            activity.setOutcome(ActivityResult.WON);
        }
        if (outcome.equals("draw")) {
            activity.setOutcome(ActivityResult.DRAW);
        }
        if (outcome.equals("loss")) {
            activity.setOutcome(ActivityResult.LOSS);
        }
        activityService.save(activity);
    }

    @Then("my teams {string} should increase by one")
    public void my_teams_should_increase_by_one(String outcome) {

        int wins = (int) result.getModelAndView().getModel().get("teamWins");
        int draws = (int) result.getModelAndView().getModel().get("teamDraws");
        int losses = (int) result.getModelAndView().getModel().get("teamLosses");

        if (outcome.equals("win")) {
            Assertions.assertEquals(1, wins);
            Assertions.assertEquals(0, draws);
            Assertions.assertEquals(0, losses);
        }
        if (outcome.equals("draw")) {
            Assertions.assertEquals(0, wins);
            Assertions.assertEquals(1, draws);
            Assertions.assertEquals(0, losses);
        }
        if (outcome.equals("losse")) {
            Assertions.assertEquals(0, wins);
            Assertions.assertEquals(0, draws);
            Assertions.assertEquals(1, losses);
        }
    }

    @And("I have a team")
    public void i_have_a_team() {
        team = teamService.createTeamInDatabase("Test", "Test", "Test", "Test", "Test");
    }

    @Then("my teams last five trend should reflect the results")
    public void my_teams_last_five_trend_should_reflect_the_results() {

        List<GameActivity> trend = teamStatsService.getTeamRecentGames(team.getId());

        Assertions.assertEquals(5, trend.size());
        Assertions.assertEquals(ActivityResult.WON, trend.get(0).getOutcome());
        Assertions.assertEquals(ActivityResult.LOSS, trend.get(1).getOutcome());
        Assertions.assertEquals(ActivityResult.DRAW, trend.get(2).getOutcome());
        Assertions.assertEquals(ActivityResult.WON, trend.get(3).getOutcome());
        Assertions.assertEquals(ActivityResult.LOSS, trend.get(4).getOutcome());
    }

    @And("my teams last five trend should reflect the up to date results")
    public void my_teams_last_five_trend_should_reflect_the_up_to_date_results() {

        List<GameActivity> trend = teamStatsService.getTeamRecentGames(team.getId());

        Assertions.assertEquals(5, trend.size());

        Assertions.assertEquals(ActivityResult.LOSS, trend.get(0).getOutcome());
        Assertions.assertEquals(ActivityResult.DRAW, trend.get(1).getOutcome());
        Assertions.assertEquals(ActivityResult.WON, trend.get(2).getOutcome());
        Assertions.assertEquals(ActivityResult.LOSS, trend.get(3).getOutcome());
        Assertions.assertEquals(ActivityResult.LOSS, trend.get(4).getOutcome());
    }

    @When("a game is played with various goal scorers")
    public void a_game_is_played_with_various_goal_scorers() throws Exception {
        ActivityForm form = new ActivityForm();
        form.setDescription("Please bring your mouth guard");
        form.setTeam(team.getId());
        form.setType("Game");
        form.setCity("A City");
        form.setCountry("A Country");
        form.setStartTime("2003-03-25T12:00");
        form.setEndTime("2003-03-26T12:00");

        GameActivity activity = (GameActivity) activityService.createActivityInDatabase(form);
        activity.setOutcome(ActivityResult.WON);

        // Player One Scoring
        PlayerScoreForm playerScoreForm1 = new PlayerScoreForm();
        playerScoreForm1.setScore(24);
        playerScoreForm1.setScoredPlayerId(1L);
        playerScoreForm1.setActivityId(activity.getId());
        playerScoreForm1.setScoreTime(10);

        // Player Two Scoring
        PlayerScoreForm playerScoreForm2 = new PlayerScoreForm();
        playerScoreForm2.setScore(4);
        playerScoreForm2.setScoredPlayerId(2L);
        playerScoreForm2.setActivityId(activity.getId());
        playerScoreForm2.setScoreTime(10);

        // Player Three Scoring
        PlayerScoreForm playerScoreForm3 = new PlayerScoreForm();
        playerScoreForm3.setScore(3);
        playerScoreForm3.setScoredPlayerId(3L);
        playerScoreForm3.setActivityId(activity.getId());
        playerScoreForm3.setScoreTime(10);

        // Player Four Scoring
        PlayerScoreForm playerScoreForm4 = new PlayerScoreForm();
        playerScoreForm4.setScore(5);
        playerScoreForm4.setScoredPlayerId(4L);
        playerScoreForm4.setActivityId(activity.getId());
        playerScoreForm4.setScoreTime(10);

        StatisticsForm statisticsForm = new StatisticsForm();
        statisticsForm.getPlayerScoreForms().addAll(
            Arrays.asList(playerScoreForm1, playerScoreForm2, playerScoreForm3, playerScoreForm4));
        statisticsForm.setActivityId(activity.getId());
        activity.setPosition("1,2,3,4");
        activityService.save(activity);

        activityService.addStatisticsForm(statisticsForm);
    }

    @Then("I should be able to see the top five goal scorers of my team")
    public void i_should_be_able_to_see_the_top_five_goal_scorers_of_my_team() {
        List<TeamRoles> topFiveScorers = teamRoleService.getTeamTop5Scorers(team.getId());

        Assertions.assertEquals(5, topFiveScorers.size());
        Assertions.assertEquals(1, topFiveScorers.get(0).getUser().getId());
        Assertions.assertEquals(4, topFiveScorers.get(1).getUser().getId());
        Assertions.assertEquals(2, topFiveScorers.get(2).getUser().getId());
        Assertions.assertEquals(3, topFiveScorers.get(3).getUser().getId());
    }

    @Then("I have a team with many members")
    public void my_team_has_team_members() throws Exception {

        team = teamService.getTeamById(1);
        teamService.registerTabUserToTeam(team, tabUserService.getById(1));
        teamService.registerTabUserToTeam(team, tabUserService.getById(2));
        teamService.registerTabUserToTeam(team, tabUserService.getById(3));
        teamService.registerTabUserToTeam(team, tabUserService.getById(4));
        teamService.registerTabUserToTeam(team, tabUserService.getById(5));
    }

    @Then("my team plays {int} games")
    public void my_team_plays_five_games(int numberOfGames) throws Exception {

        for (int i = 1; i <= numberOfGames; i++) {

            ActivityForm form = new ActivityForm();
            form.setDescription("Please bring your mouth guard");
            form.setTeam(team.getId());
            form.setType("Game");
            form.setCity("A City");
            form.setCountry("A Country");
            form.setStartTime("2003-03-25T12:00");
            form.setEndTime("2003-03-26T12:00");

            GameActivity activity = (GameActivity) activityService.createActivityInDatabase(form);
            activity.setOutcome(ActivityResult.WON);
            activity.setHomeScore(String.valueOf(i));
            activityService.save(activity);
        }
    }

    @Then("I should be able to retrieve my teams five most recent games")
    public void i_should_be_able_to_retrieve_my_teams_five_most_recent_games() throws Exception {

        List<GameActivity> fiveMostRecentGames = teamStatsService.getTeamRecentGames(team.getId());

        Assertions.assertEquals("1", fiveMostRecentGames.get(0).getHomeScore());
        Assertions.assertEquals("2", fiveMostRecentGames.get(1).getHomeScore());
        Assertions.assertEquals("3", fiveMostRecentGames.get(2).getHomeScore());
        Assertions.assertEquals("4", fiveMostRecentGames.get(3).getHomeScore());
        Assertions.assertEquals("5", fiveMostRecentGames.get(4).getHomeScore());
    }

    @Then("I should be able to retrieve my teams game history")
    public void i_should_be_able_to_retrieve_my_teams_game_history() {

        List<GameActivity> recentGames = teamStatsService.getTeamActivities(team.getId());

        Assertions.assertEquals(10, recentGames.size());

        for (int i = 0; i < 10; i++) {
            Assertions.assertEquals(recentGames.get(i).getHomeScore(), String.valueOf(i + 1));
        }
    }

    @When("a game is played with one substitution")
    public void game_is_played_with_various_substitutions_with_and_and_and() throws Exception {
        ActivityForm form = new ActivityForm();
        form.setDescription("Please bring your mouth guard");
        form.setTeam(team.getId());
        form.setType("Game");
        form.setCity("A City");
        form.setCountry("A Country");
        form.setStartTime("2003-03-25T12:00");
        form.setEndTime("2003-03-25T13:00");

        GameActivity activity = (GameActivity) activityService.createActivityInDatabase(form);
        activity.setOutcome(ActivityResult.WON);
        activity.setPosition("1-2");
        activity = (GameActivity) activityService.save(activity);

        SubstitutionForm substitutionForm = new SubstitutionForm();
        substitutionForm.setSubstitutePlayerId(2L);
        substitutionForm.setSubstitutedPlayerId(1L);
        substitutionForm.setSubstituteTime(10);
        substitutionForm.setActivityId(activity.getId());
        ArrayList<SubstitutionForm> substitutionForms = new ArrayList<>();
        substitutionForms.add(substitutionForm);

        StatisticsForm statisticsForm = new StatisticsForm();
        statisticsForm.setSubstitutionForms(substitutionForms);
        statisticsForm.setActivityId(activity.getId());

        activityService.addStatisticsForm(statisticsForm);
    }

    @Then("I should be able to see the top five players with the greatest"
        + " playtime with one substitute")
    public void able_to_see_the_top_five_players_with_the_greatest_playtime_with_one_substitute() {
        List<TeamRoles> topFivePlaytimeForTeam =
            teamRoleService.getTopFivePlaytimeForTeam(team.getId());

        Assertions.assertEquals(50, topFivePlaytimeForTeam.get(0).getTotalTime());
        Assertions.assertEquals(10, topFivePlaytimeForTeam.get(1).getTotalTime());
    }


    @When("a game is played with multiple substitution")
    public void game_is_played_with_multiple_substitution() throws Exception {
        ActivityForm form = new ActivityForm();
        form.setDescription("Please bring your mouth guard");
        form.setTeam(team.getId());
        form.setType("Game");
        form.setCity("A City");
        form.setCountry("A Country");
        form.setStartTime("2003-03-25T12:00");
        form.setEndTime("2003-03-25T13:00");

        GameActivity activity = (GameActivity) activityService.createActivityInDatabase(form);
        activity.setOutcome(ActivityResult.WON);
        activity.setPosition("4-3");
        activity = (GameActivity) activityService.save(activity);

        List<SubstitutionForm> substitutionEvents = List.of(
            new SubstitutionForm(activity.getId(),
                3L,
                4L, 10),
            new SubstitutionForm(activity.getId(),
                4L,
                3L, 40));

        activityService.createSubstitutionStatisticList(activity,
            substitutionEvents, true);
        activityService.save(activity);
    }

    @When("a game is played with no substitution")
    public void a_game_is_played_with_no_substitution() throws Exception {
        ActivityForm form = new ActivityForm();
        form.setDescription("Please bring your mouth guard");
        form.setTeam(team.getId());
        form.setType("Game");
        form.setCity("A City");
        form.setCountry("A Country");
        form.setStartTime("2003-03-25T12:00");
        form.setEndTime("2003-03-25T13:00");

        GameActivity activity = (GameActivity) activityService.createActivityInDatabase(form);
        activity.setOutcome(ActivityResult.WON);
        activity.setPosition("5-6");
        activity = (GameActivity) activityService.save(activity);

        StatisticsForm statisticsForm = new StatisticsForm();
        statisticsForm.setActivityId(activity.getId());
        activityService.addStatisticsForm(statisticsForm);
    }

    @Then("I should be able to see the top five players with the greatest "
        + "playtime with no substitution")
    public void able_to_see_the_top_five_players_with_the_greatest_playtime_with_no_substitution() {
        List<TeamRoles> topFivePlaytimeForTeam =
            teamRoleService.getTopFivePlaytimeForTeam(team.getId());

        Assertions.assertEquals(60, topFivePlaytimeForTeam.get(0).getTotalTime());
    }


    @When("the team has played in the training and competition")
    public void the_team_has_played_in_the_training_and_competition() throws NotFoundException {
        ActivityForm form = new ActivityForm();
        form.setDescription("Please bring your mouth guard");
        form.setTeam(team.getId());
        form.setType("Training");
        form.setCity("A City");
        form.setCountry("A Country");
        form.setStartTime("2003-03-25T12:00");
        form.setEndTime("2003-03-25T13:00");

        activityService.createActivityInDatabase(form);


        ActivityForm form1 = new ActivityForm();
        form1.setDescription("Please bring your mouth guard");
        form1.setTeam(team.getId());
        form1.setType("Competition");
        form1.setCity("A City");
        form1.setCountry("A Country");
        form1.setStartTime("2003-03-25T12:00");
        form1.setEndTime("2003-03-25T13:00");

        activityService.createActivityInDatabase(form1);

    }

    @Then("i should be able to retrieve the team activity lists include "
        + "the training and competition")
    public void able_to_retrieve_the_team_activity_lists_include_the_training_and_competition() {

        List<Activity> recentGames = teamStatsService.getTeamOtherActivities(team.getId());

        Assertions.assertEquals(2, recentGames.size());

        Assertions.assertEquals("Training", recentGames.get(0).getType());
        Assertions.assertEquals("Competition", recentGames.get(1).getType());

    }
}
