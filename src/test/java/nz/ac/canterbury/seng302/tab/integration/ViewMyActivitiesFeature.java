package nz.ac.canterbury.seng302.tab.integration;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import nz.ac.canterbury.seng302.tab.entity.Activity;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.repository.ActivityRepository;
import nz.ac.canterbury.seng302.tab.service.LocationService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.TeamService;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

/**
 * Test for Viewing a users activities using E2E testing
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ViewMyActivitiesFeature {
    @Autowired
    AuthenticationManager authenticationManager;
    MvcResult result;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TabUserService tabUserService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private LocationService locationService;
    private TabUser user;
    @Autowired
    private WebApplicationContext webApp;
    private Team teamBlaBla;
    private Team teamFooFoo;
    private int blaBlaActivityCounter;
    private Team teamAhhAhh;

    @Given("I logged in")
    public void logged_in() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApp)
            .apply(springSecurity())
            .build();
        // "email"+tabUserService.getFormResults().size()+"@email.com" so there is an
        // individual user with different emails for each AC...
        user = tabUserService.createFullTabUserInDatabase(Arrays.asList("test", "person"),
                new Location("Christchurch",
            "New Zealand"), "email" + tabUserService.getFormResults().size()
                        + "@email.com",
            new Date(2002 - 1900, 11, 31).toString(), "*****",
                "image.jpg", "Baseball");
        user.grantAuthority("ROLE_USER");
        UsernamePasswordAuthenticationToken token =
            new UsernamePasswordAuthenticationToken(user.getEmail(), "*****",
                user.getAuthorities());
        // Authenticate the token properly with the CustomAuthenticationProvider
        Authentication authentication = authenticationManager.authenticate(token);
        // Check if the authentication is actually authenticated
        // (in this example any username/password is accepted so this should never be false)
        if (authentication.isAuthenticated()) {

            // Add the authentication to the current security context (Stateful)
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        IntegrationTestConfigurations.loggedInUser = user;
        Assertions.assertNotNull(tabUserService.getCurrentlyLoggedIn());

    }

    @Given("I am a manager team Bla Bla")
    public void am_a_manager_team_bla_bla() throws Exception {
        teamBlaBla =
            teamService.createTeamInDatabase("Bla Bla", "BasketBall", "Christchurch", "New Zealand",
                "image.jpg");
        Assertions.assertNotNull(teamService.getTeamById(teamBlaBla.getId()));
    }

    @Given("I have created the activities for team Bla Bla:")
    public void have_created_the_activities_for_team_bla_bla(
        io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> activities = dataTable.asMaps(String.class, String.class);
        blaBlaActivityCounter = 0;
        for (Map<String, String> activity : activities) {
            String type = activity.get("type");
            String startDate = activity.get("startDate");
            String endDate = activity.get("endDate");
            String description = activity.get("description");
            Location location = locationService.createLocationInDatabase("Japan", "New Zealand");
            Activity activity1 =
                new Activity(user, teamBlaBla, type, startDate, endDate, description, location);
            blaBlaActivityCounter++;
            activityRepository.save(activity1);
            Assertions.assertNotNull(activityRepository.findById(activity1.getId()));
        }
    }

    @When("I get on a my activities page")
    public void get_on_a_my_activities_page() throws Exception {
        result = mockMvc.perform(get("/viewMyActivities").with(
                    user(tabUserService.getCurrentlyLoggedIn().getEmail()).password(
                        tabUserService.getCurrentlyLoggedIn().getPassword()).roles("USER"))
                .contentType("text/html; charset=UTF-8"))
            .andExpect(status().isOk()).andReturn();
    }

    @Then("I see my activity page with the activities for Bla Bla")
    public void see_my_activity_page_with_the_activities_for_bla_bla() {
        // Write code here that turns the phrase above into concrete actions
        ModelAndView mav = result.getModelAndView();

        Assertions.assertEquals("viewMyActivities", mav.getViewName());

        List<List<List<String>>> teamActivitiesFull = (List<List<List<String>>>) mav.getModel().get(
            "teamEvents");
        List<List<String>> teamActivities = teamActivitiesFull.get(0);

        Assertions.assertNotNull(teamActivities);
        Assertions.assertFalse(teamActivities.isEmpty());
        Assertions.assertEquals(2, teamActivities.size());

        Assertions.assertEquals(blaBlaActivityCounter, teamActivities.size());
        for (List<String> activity : teamActivities) {
            Assertions.assertEquals(teamBlaBla.getName(), activity.get(5));
        }
    }

    @Then("I see my activity page with no activities")
    public void see_my_activity_page_with_no_activities() {
        ModelAndView mav = result.getModelAndView();

        Assertions.assertEquals("viewMyActivities", mav.getViewName());

        List<List<String>> teamActivities = (List<List<String>>) mav.getModel().get("teamEvents");

        Assertions.assertNotNull(teamActivities);
        Assertions.assertTrue(teamActivities.isEmpty());
        Assertions.assertEquals(0, blaBlaActivityCounter);
    }

    @Given("I am a manager team Foo Foo")
    public void am_a_manager_team_foo_foo() throws Exception {
        teamFooFoo =
            teamService.createTeamInDatabase("Foo Foo", "Baseball", "Christchurch", "New Zealand",
                "image.jpg");
        Assertions.assertNotNull(teamService.getTeamById(teamFooFoo.getId()));
    }

    @Then("I see the activities are in ascending order by time")
    public void see_the_activities_are_in_ascending_order_by_time() {
        ModelAndView mav = result.getModelAndView();

        Assertions.assertEquals("viewMyActivities", mav.getViewName());

        List<List<List<String>>> teamActivitiesFull = (List<List<List<String>>>) mav.getModel().get(
            "teamEvents");
        List<List<String>> teamActivities = teamActivitiesFull.get(0);

        Assertions.assertNotNull(teamActivities);
        Assertions.assertFalse(teamActivities.isEmpty());

        Assertions.assertEquals(2, teamActivities.size());

        List<String> first = teamActivities.get(0);
        List<String> second = teamActivities.get(1);
        // second activity is scheduled before the first so compare should return
        Assertions.assertTrue(second.get(1).compareTo(first.get(1)) > 0);
    }

    @Given("I am a manager team ahh ahh")
    public void am_a_manager_team_ahh_ahh() throws Exception {
        teamAhhAhh =
            teamService.createTeamInDatabase("Ahh Ahh", "BasketBall", "Christchurch", "New Zealand",
                "image.jpg");
        Assertions.assertNotNull(teamService.getTeamById(teamAhhAhh.getId()));
    }

    @Given("I have create the activities for team ahh ahh:")
    public void have_create_the_activities_for_team_ahh_ahh(
        io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> activities = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> activity : activities) {
            String type = activity.get("type");
            String startDate = activity.get("startDate");
            String endDate = activity.get("endDate");
            String description = activity.get("description");
            Location location = locationService.createLocationInDatabase("Japan", "New Zealand");
            Activity activity1 =
                new Activity(user, teamAhhAhh, type, startDate, endDate, description, location);
            activityRepository.save(activity1);
            Assertions.assertNotNull(activityRepository.findById(activity1.getId()));
        }
    }

    @Then("I see my activity page with the activities for Bla Bla and Foo Foo and ahh ahh")
    public void see_my_activity_page_with_the_activities_for_bla_bla_and_foo_foo_and_ahh_ahh() {
        ModelAndView mav = result.getModelAndView();

        Assertions.assertEquals("viewMyActivities", mav.getViewName());

        List<List<List<Activity>>> teamActivities =
            (List<List<List<Activity>>>) mav.getModel().get("team_activities");
        Assertions.assertNotNull(teamActivities);
        Assertions.assertFalse(teamActivities.isEmpty());
        Assertions.assertEquals(3, teamActivities.size());
    }

    @Then("I see activities for ahh ahh first")
    public void see_activities_for_ahh_ahh_first() {
        ModelAndView mav = result.getModelAndView();

        Assertions.assertEquals("viewMyActivities", mav.getViewName());

        List<List<List<Activity>>> teamActivities =
            (List<List<List<Activity>>>) mav.getModel().get("team_activities");
        Assertions.assertNotNull(teamActivities);
        Assertions.assertFalse(teamActivities.isEmpty());

        List<List<Activity>> activityChucksTeamAhhAhh = teamActivities.get(0);
        Assertions.assertEquals(1, activityChucksTeamAhhAhh.size());
        List<List<Activity>> activityChucksTeamBlaBla = teamActivities.get(1);
        Assertions.assertEquals(1, activityChucksTeamBlaBla.size());
        List<List<Activity>> activityChucksTeamFooFoo = teamActivities.get(2);
        Assertions.assertEquals(1, activityChucksTeamFooFoo.size());

        List<Activity> activitiesTeamAhhAhh = activityChucksTeamAhhAhh.get(0);
        for (Activity activity : activitiesTeamAhhAhh) {
            Assertions.assertEquals(teamAhhAhh.getName(), activity.getTeam().getName());
        }
        List<Activity> activitiesTeamBlaBla = activityChucksTeamBlaBla.get(0);
        for (Activity activity : activitiesTeamBlaBla) {
            Assertions.assertEquals(teamBlaBla.getName(), activity.getTeam().getName());
        }
        List<Activity> activitiesTeamFooFoo = activityChucksTeamFooFoo.get(0);
        for (Activity activity : activitiesTeamFooFoo) {
            Assertions.assertEquals(teamFooFoo.getName(), activity.getTeam().getName());
        }
    }
}








