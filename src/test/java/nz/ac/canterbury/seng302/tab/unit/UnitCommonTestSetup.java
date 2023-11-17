package nz.ac.canterbury.seng302.tab.unit;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import nz.ac.canterbury.seng302.tab.entity.Activity;
import nz.ac.canterbury.seng302.tab.entity.Club;
import nz.ac.canterbury.seng302.tab.entity.Food;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.RecommendedMeal;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.entity.TeamRoles;
import nz.ac.canterbury.seng302.tab.entity.enums.SupportedSports;
import nz.ac.canterbury.seng302.tab.entity.garminentities.GarminActivity;
import nz.ac.canterbury.seng302.tab.formobjects.CreateTeamForm;
import nz.ac.canterbury.seng302.tab.formobjects.EditTeamForm;
import nz.ac.canterbury.seng302.tab.service.RoleId;
import nz.ac.canterbury.seng302.tab.utility.TokenGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Common data needed for the test
 */
public class UnitCommonTestSetup {

    private static final BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();

    /**
     * Location for the test
     *
     * @return location
     */
    public static Location createTestLocation() {
        Location location = new Location("City", "Country");
        location.setLine1("line1");
        location.setLine2("line2");
        location.setPostcode("8041");
        location.setSuburb("Ilam");
        return location;
    }

    /**
     * create a user for testing
     *
     * @return a user
     */
    public static TabUser createTestUser() {

        Location location = createTestLocation();
        TabUser user = new TabUser(Arrays.asList("test", "person"), location,
            "email@email.com",
            new Date(2002, 12, 12).toString(),
            bcryptPasswordEncoder.encode("*****"),
            "profile_picture.jpg",
            "Baseball");
        user.setId(1L);
        return user;
    }

    /**
     * create a user for testing
     *
     * @return a user
     */
    public static TabUser createTestUser(String firstName) {
        Location location = createTestLocation();
        TabUser user = new TabUser(Arrays.asList(firstName, "person"), location, "email@email.com",
                new Date(2002, 12, 12).toString(), "*****", "profile_picture.jpg",
                "Baseball");
        user.setId(1L);
        return user;
    }

    /**
     * Create test for the team
     *
     * @return test team
     */
    public static Team createTestTeam() {
        Location location = createTestLocation();
        Team team = new Team("Bishops", "Basketball", location, "pfp");
        team.setId(1L);
        team.setCreationDate(TokenGenerator.getCurrentTime());
        return team;
    }

    /**
     * Create test for the team
     *
     * @return test team
     */
    public static Team createTestTeamWithClub() {
        Location location = createTestLocation();
        Team team = new Team("Bishops", "Basketball", location, "pfp");
        team.setClub(createTestClub());
        team.setId(1L);
        team.setCreationDate(TokenGenerator.getCurrentTime());
        return team;
    }

    /**
     * Create activity for test
     *
     * @return activity object
     */
    public static Activity createTestActivity() {

        TabUser user = createTestUser();
        Team team = createTestTeam();
        Location location = createTestLocation();

        Activity activity = new Activity(user, team, "Game", "2023-07-17T14:18", "2023-07-17T15:18",
            "Description", location);
        activity.setPosition("1,2");
        activity.setId(1L);
        return activity;
    }

    /**
     * Create the test activity with different type.
     *
     * @param type string representing the type of activity i.e. Game, Friendly
     * @return an entity representing an activity
     */
    public static Activity createTestActivityWithType(String type) {

        TabUser user = createTestUser();
        Team team = createTestTeam();
        Location location = createTestLocation();

        Activity activity = new Activity(user, team, type, "2023-07-17T14:18", "2023-07-17T15:18",
            "Description", location);
        activity.setPosition("1,2");
        activity.setId(1L);
        return activity;
    }

    /**
     * Create a club for testing
     *
     * @return the club object
     */
    public static Club createTestClub() {
        Location location = createTestLocation();
        TabUser user = createTestUser();
        return new Club("Chelsea", SupportedSports.Football, location, user, "");
    }

    /**
     * Create invalid edit team form for test
     *
     * @return edit team form
     */
    public static EditTeamForm createTestInvalidEditTeamForm() {

        EditTeamForm editTeamFormInvalid = new EditTeamForm();
        editTeamFormInvalid.setId("1");
        editTeamFormInvalid.setName("!");
        editTeamFormInvalid.setCity("!");
        editTeamFormInvalid.setCountry("!");
        editTeamFormInvalid.setSport("!");
        editTeamFormInvalid.setAddress1("!");
        editTeamFormInvalid.setAddress2("!");
        editTeamFormInvalid.setPostcode("!");
        editTeamFormInvalid.setSuburb("!");
        return editTeamFormInvalid;
    }

    /**
     * Create valid edit team form for test
     *
     * @return edit team form
     */
    public static EditTeamForm createTestValidEditTeamForm() {

        Team team = createTestTeam();
        List<TeamRoles> teamRolesList = createTestListTeamRoles();
        EditTeamForm editTeamFormValid = new EditTeamForm();
        editTeamFormValid.setId(Long.toString(team.getId()));
        editTeamFormValid.setName("Bishops");
        editTeamFormValid.setSport("Basketball");
        editTeamFormValid.setCity("Greymouth");
        editTeamFormValid.setCountry("Japan");
        editTeamFormValid.setPostcode("8041");
        editTeamFormValid.setAddress1("Here");
        editTeamFormValid.setAddress2("Here");
        editTeamFormValid.setSuburb("Sub");
        editTeamFormValid.setTeamRoles(teamRolesList);

        return editTeamFormValid;

    }

    /**
     * Create team role for test
     *
     * @return team role
     */
    public static TeamRoles createTestTeamRoles() {
        TeamRoles teamRoles = new TeamRoles();
        teamRoles.setId(new RoleId());
        return teamRoles;
    }

    /**
     * Create team role list for test
     *
     * @return team role list
     */
    public static List<TeamRoles> createTestListTeamRoles() {
        TeamRoles teamRoles1 = createTestTeamRoles();
        List<TeamRoles> teamRolesList = new ArrayList<>();
        teamRolesList.add(teamRoles1);
        return teamRolesList;
    }

    /**
     * Create a team form for class
     *
     * @return CreateTeamForm
     */
    public CreateTeamForm createTestTeamForm() {
        CreateTeamForm createTeamFormValid = new CreateTeamForm();
        createTeamFormValid.setName("Test");
        createTeamFormValid.setCity("Brighton");
        createTeamFormValid.setCountry("England");
        createTeamFormValid.setSport(SupportedSports.Basketball);
        createTeamFormValid.setSuburb("Test");
        createTeamFormValid.setAddress1("Test");
        createTeamFormValid.setAddress2("Test");
        createTeamFormValid.setPostcode("902");

        return createTeamFormValid;
    }

    /**
     * Creates a garmin activity
     * @return an entity representing a garmin activity
     */
    public static GarminActivity createGarminActivity() {
        GarminActivity garminActivity =  new GarminActivity();
        garminActivity.setActiveKilocalories(480);
        garminActivity.setActivityType("DISC_GOLF");
        garminActivity.setDistanceInMeters(100000.0);
        garminActivity.setDurationInSeconds(3600);
        garminActivity.setStartTime(LocalDateTime.now());
        garminActivity.setActivity(createTestActivity());
        garminActivity.setTabUser(createTestUser());
        return garminActivity;
    }

    /**
     * Creates a food object for unit testing
     * @return a food object
     */
    public static Food createTestFood() {
        Food food = new Food();
        food.setCalories(243.0);
        food.setCarbs(21.05);
        food.setProtein(12.0);
        food.setFat(15.35);
        food.setId(1L); // Set your food ID here
        food.setfdcId(170554L);
        food.setPortion(50.0);
        food.setSugar((double) 0);
        return food;
    }

    /**
     * Creates a recommended meal object
     * @return a recommended meal object
     */
    public static RecommendedMeal createTestMeal() {
        RecommendedMeal recommendedMeal = new RecommendedMeal();
        recommendedMeal.setName("Maintainance Meal");
        recommendedMeal.setId(1L);
        recommendedMeal.setCaloriePreference("maintain");
        recommendedMeal.setIcon("default");
        return recommendedMeal;
    }

}
