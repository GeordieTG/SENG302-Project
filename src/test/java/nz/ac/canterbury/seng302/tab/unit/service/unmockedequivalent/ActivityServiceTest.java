package nz.ac.canterbury.seng302.tab.unit.service.unmockedequivalent;


import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import nz.ac.canterbury.seng302.tab.entity.Activity;
import nz.ac.canterbury.seng302.tab.entity.GameActivity;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.StatisticFact;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.formobjects.ActivityForm;
import nz.ac.canterbury.seng302.tab.formobjects.PlayerScoreForm;
import nz.ac.canterbury.seng302.tab.service.ActivityService;
import nz.ac.canterbury.seng302.tab.service.LocationService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.TeamService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

/**
 * Tests for functionality related to Activities
 */
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class ActivityServiceTest {

    @Autowired
    TeamService teamService;
    @Autowired
    TabUserService tabUserService;

    @Autowired
    ActivityService activityService;
    @Autowired
    LocationService locationService;
    @Autowired
    AuthenticationManager authenticationManager;


    TabUser user;

    Team team;

    ActivityForm form;
    ActivityForm form1;

    ActivityForm form2;
    TabUser user1;

    Location location;


    @BeforeEach
    void setup() {
        location = locationService.createLocationInDatabase("Japan", "Not Japan");
        user =
            tabUserService.createFullTabUserInDatabase(Arrays.asList("Bob", "Smith"),
                    new Location("Greymouth", "New Zealand"),
                "bob@email.com", new Date(2002, 11, 31).toString(),
                "*****", "default.jpg", "basketball");
        user1 =
            tabUserService.createFullTabUserInDatabase(Arrays.asList("John", "Smith"),
                    new Location("Greymouth", "New Zealand"),
                "bob2@email.com", new Date(2002, 11, 31).toString(),
                "*****", "default.jpg", "basketball");
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
        team = teamService.createTeamInDatabase("Bishops", "Basketball",
            "Greymouth", "Country",
            "default.jpg");

        form = new ActivityForm();
        form.setDescription("Please bring your mouth guard");
        form.setTeam(team.getId());
        form.setType("Training");
        form.setCity(location.getCity());
        form.setCountry(location.getCountry());
        form.setStartTime("2003-03-25T12:00");
        form.setEndTime("2003-03-26T12:00");
        form1 = new ActivityForm();
        form1.setDescription("Have some fun");
        form1.setTeam(team.getId());
        form1.setType("Game");
        form1.setStartTime("2003-04-25T12:00");
        form1.setEndTime("2003-04-26T12:00");
        form1.setCity(location.getCity());
        form1.setCountry(location.getCountry());

        form2 = new ActivityForm();
        form2.setDescription("Game for fun");
        form2.setTeam(team.getId());
        form2.setType("Game");
        form2.setStartTime("2003-01-25T12:00");
        form2.setEndTime("2003-01-26T12:00");
        form2.setCity(location.getCity());
        form2.setCountry(location.getCountry());


    }

    /**
     * Given no activities have been created by a user when searching for personal
     * activities then empty list returned.
     */
    @Test
    void searchPersonalActivityByIdWithNoPersonalActivityCreated() {
        Assertions.assertTrue(activityService.searchPersonalActivity(user.getId()).isEmpty());
    }

    /**
     * Given no poersonal activities have been created by a user when searching for
     * personal activities that no are returned.
     */
    @Test
    void searchPersonalActivityByIdWithNoPersonalActivityCreatedButTeamActivityCreated()
        throws Exception {
        activityService.createActivityInDatabase(form);
        Assertions.assertTrue(
            activityService.searchPersonalActivity(tabUserService.getCurrentlyLoggedIn().getId())
                .isEmpty());
    }

    /**
     * Given an activity does not exist when search then should return null
     */
    @Test
    void getPersonalActivityByIdWithActivityIdThatDoesNotExist() {
        Assertions.assertNull(activityService.getActivityById(100_000_000));
    }

    /**
     * Given a user does not exist when search then should return null
     */
    @Test
    void searchPersonalActivityByIdWithActivityIdThatDoesNotExist() {
        Assertions.assertTrue(activityService.searchPersonalActivity(100_000_000).isEmpty());
    }

    /**
     * Checks that an activity is created in the database correctly given all fields are filled out
     *
     * @throws Exception throws an exception if a team isn't found with that ID
     */
    @Test
    void createActivity() throws Exception {

        Activity activity = activityService.createActivityInDatabase(form);

        Assertions.assertNotNull(activity);
        Assertions.assertNotNull(activity.getUser());
        Assertions.assertNotNull(activity.getTeam());
        Assertions.assertEquals("Please bring your mouth guard", activity
            .getDescription());
        Assertions.assertEquals("Training", activity.getType());
        Assertions.assertEquals("2003-03-25T12:00", activity.getStartTime());
        Assertions.assertEquals("2003-03-26T12:00", activity.getEndTime());
    }

    /**
     * Checks that an activity is correctly linked to a user when created
     *
     * @throws Exception throws an exception if a team isn't found with that ID
     */
    @Test
    void createActivityCheckUser() throws Exception {

        Activity activity = activityService.createActivityInDatabase(form);

        Assertions.assertEquals(user.getId(), activity.getUser().getId());
        Assertions.assertEquals(user.getFirstName(), activity.getUser().getFirstName());
        Assertions.assertEquals(user.getLastName(), activity.getUser().getLastName());
        Assertions.assertEquals(user.getLocation().getCity(),
            activity.getUser().getLocation().getCity());
        Assertions.assertEquals(user.getLocation().getCountry(),
            activity.getUser().getLocation().getCountry());
    }

    /**
     * Checks that an activity is correctly linked to a team when created with a teamID
     *
     * @throws Exception throws an exception if a team isn't found with that ID
     */
    @Test
    void createActivityCheckTeam() throws Exception {

        Activity activity = activityService.createActivityInDatabase(form);

        Team teamResult = teamService.getTeamById(team.getId());
        Assertions.assertEquals(teamResult.getId(), activity.getTeam().getId());
        Assertions.assertEquals(teamResult.getName(), activity.getTeam().getName());
        Assertions.assertEquals(teamResult.getCreationDate(), activity.getTeam().getCreationDate());
    }

    /**
     * Checks that an activity can be created in a database even if a team isn't associated
     *
     * @throws Exception throws an exception if a team isn't found with that ID
     */
    @Test
    void createActivityNoTeam() throws Exception {

        form.setTeam(-1);
        Activity activity = activityService.createActivityInDatabase(form);

        Assertions.assertNotNull(activity);
        Assertions.assertNull(activity.getTeam());
    }

    /**
     * check that the team activities are returned in the correct ascending dates
     *
     * @throws Exception throws an exception if a team isn't found with that ID
     */
    @Test
    void checkTeamActivitiesInAscendingDateOrder() throws Exception {
        activityService.createActivityInDatabase(form);
        activityService.createActivityInDatabase(form1);
        activityService.createActivityInDatabase(form2);
        List<Activity> teamActivityList = activityService.searchTeamActivity(team.getId());
        Assertions.assertEquals(3, teamActivityList.size());
        LocalDateTime dateTimeForActivity =
            LocalDateTime.parse(teamActivityList.get(0).getStartTime());
        LocalDateTime dateTimeForActivity1 =
            LocalDateTime.parse(teamActivityList.get(1).getStartTime());
        LocalDateTime dateTimeForActivity2 =
            LocalDateTime.parse(teamActivityList.get(2).getStartTime());
        Assertions.assertTrue(dateTimeForActivity.isBefore(dateTimeForActivity1));
        Assertions.assertTrue(dateTimeForActivity1.isBefore(dateTimeForActivity2));
        Assertions.assertTrue(dateTimeForActivity.isBefore(dateTimeForActivity2));
    }

    /**
     * Checks that edited activity form is updated in the database
     * throws Exception throws an exception if the activity isn't updated with the edited values
     */
    @Test
    void editActivityCheckDatabase() throws Exception {
        Activity activity = activityService.createActivityInDatabase(form);

        form.setDescription("Please bring your mouth guard and your water bottle");
        form.setType("Game");
        form.setStartTime("2023-03-2T12:00");
        form.setEndTime("2023-03-16T12:00");
        form.setId(String.valueOf(activity.getId()));

        Activity updatedActivity = activityService.updateActivityInDatabase(form);

        Assertions.assertNotNull(updatedActivity);
        Assertions.assertNotNull(updatedActivity.getUser());
        Assertions.assertNotNull(updatedActivity.getTeam());
        Assertions.assertEquals("Please bring your mouth guard and your water bottle",
            updatedActivity.getDescription());
        Assertions.assertEquals("Game", updatedActivity.getType());
        Assertions.assertEquals("2023-03-2T12:00", updatedActivity.getStartTime());
        Assertions.assertEquals("2023-03-16T12:00", updatedActivity.getEndTime());
    }


    /**
     * Tests that a user can be successfully created and that its location is
     * correctly stored and can be retrieved.
     *
     * @throws Exception if the team associated with the activity cannot be found
     */
    @Test
    void createActivityWithLocation() throws Exception {
        form.setAddress1("123 Main Road");
        form.setAddress2("Wingham Park");
        form.setPostcode("7805");
        form.setSuburb("Gladstone");
        form.setCity("Greymouth");
        form.setCountry("New Zealand");

        Activity activity = activityService.createActivityInDatabase(form);

        Location location = activity.getLocation();
        Assertions.assertNotNull(location);

        Assertions.assertEquals("123 Main Road", location.getLine1());
        Assertions.assertEquals("Wingham Park", location.getLine2());
        Assertions.assertEquals("Gladstone", location.getSuburb());
        Assertions.assertEquals("7805", location.getPostcode());
        Assertions.assertEquals("Greymouth", location.getCity());
        Assertions.assertEquals("New Zealand", location.getCountry());
    }

    /**
     * Tests that an Activity, can correctly hold a Score Statistic
     *
     * @throws Exception if activity score statistic is not stored correctly
     */
    @Test
    void addsPlayerScoreStatisticToActivityCheckScoreStatisticStoredCorrectly() throws Exception {


        form.setAddress1("123 Main Road");
        form.setAddress2("Wingham Park");
        form.setPostcode("7805");
        form.setSuburb("Gladstone");
        form.setCity("Greymouth");
        form.setCountry("New Zealand");
        form.setType("Game");
        GameActivity activity = (GameActivity) activityService.createActivityInDatabase(form);

        activity = activityService.createPlayerScoreStatisticList(activity,
            List.of(new PlayerScoreForm(activity.getId(), user.getId(), 24, 1)));
        Assertions.assertFalse(activity.getPlayerScoreStatistics().isEmpty());
        Assertions.assertEquals(user, activity.getPlayerScoreStatistics().get(0).getUser());
    }


    /**
     * Tests that an Activity, can correctly hold a Fact Statistic with no Time Occurred
     *
     * @throws Exception if activity time statistic is not stored correctly
     */
    @Test
    void addFactWithNoTimeToActivityCheckFactStoredCorrectly() throws Exception {

        form.setAddress1("123 Main Road");
        form.setAddress2("Wingham Park");
        form.setPostcode("7805");
        form.setSuburb("Gladstone");
        form.setCity("Greymouth");
        form.setCountry("New Zealand");
        form.setType("Game");

        GameActivity activity = (GameActivity) activityService.createActivityInDatabase(form);

        activity = (GameActivity) activityService.addFactStatistic(activity,
            new StatisticFact(activity, "Injury"));
        Assertions.assertFalse(activity.getFactStatistics().isEmpty());
        Assertions.assertEquals("Injury", activity.getFactStatistics().get(0).getDescription());
    }


    /**
     * Tests that an Activity, can correctly hold a Fact Statistic with a Time Occurred
     *
     * @throws Exception if fact statistic is incorrect
     */
    @Test
    void addFactWithTimeToActivityCheckFactStoredCorrectly() throws Exception {

        form.setAddress1("123 Main Road");
        form.setAddress2("Wingham Park");
        form.setPostcode("7805");
        form.setSuburb("Gladstone");
        form.setCity("Greymouth");
        form.setCountry("New Zealand");
        form.setType("Game");

        Activity activity = activityService.createActivityInDatabase(form);

        activity = activityService.addFactStatistic(activity,
            new StatisticFact(activity, "Injury", 1));
        Assertions.assertFalse(activity.getFactStatistics().isEmpty());
        Assertions.assertEquals("Injury", activity.getFactStatistics().get(0).getDescription());
        Assertions.assertEquals(1,
            activity.getFactStatistics().get(0).getTimeOccurred());
    }

    /**
     * Test the  activity start time is befor the current time
     */
    @Test
    void testCheckActivityStartTime_ActivityStartTimeBeforeCurrentTime() {
        // Create a test activity with a start time before the current time
        Activity activity = new Activity();
        activity.setStartTime("2000-05-29T08:00:00");

        // Call the method being tested
        Boolean result = activityService.checkActivityStartTime(activity);

        // Perform assertions to verify the expected outcome
        Assertions.assertTrue(result);
    }

    /**
     * Test the  activity start time is befor the current time
     */
    @Test
    void testCheckActivityStartTime_ActivityStartTimeAfterCurrentTime() {
        // Create a test activity with a start time after the current time
        Activity activity = new Activity();
        activity.setStartTime("3000-05-29T12:00:00");

        // Call the method being tested
        Boolean result = activityService.checkActivityStartTime(activity);

        // Perform assertions to verify the expected outcome
        Assertions.assertFalse(result);
    }
}
