package nz.ac.canterbury.seng302.tab.integration;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import jakarta.transaction.Transactional;
import java.sql.Date;
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
import nz.ac.canterbury.seng302.tab.entity.enums.SupportedSports;
import nz.ac.canterbury.seng302.tab.repository.FoodRepository;
import nz.ac.canterbury.seng302.tab.repository.MealRepository;
import nz.ac.canterbury.seng302.tab.service.ActivityService;
import nz.ac.canterbury.seng302.tab.service.ClubService;
import nz.ac.canterbury.seng302.tab.service.LocationService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.TeamService;
import nz.ac.canterbury.seng302.tab.utility.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Common Steps for integration tests
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CommonStepsIntegration {
    @Autowired
    TabUserService tabUserService;
    @Autowired
    LocationService locationService;
    @Autowired
    TeamService teamService;
    @Autowired
    ClubService clubService;
    @Autowired
    ActivityService activityService;

    @Autowired
    MealRepository mealRepository;

    @Autowired
    FoodRepository foodRepository;

    public Location createTestLocation(String city, String country) {
        Location location = new Location(city, country);
        return locationService.createLocationInDatabase(location);
    }

    /**
     * create a user for testing
     *
     * @return a user
     */
    public TabUser createTestUser(String name, Location location) {

        TabUser user = new TabUser(Arrays.asList(name, "person"), location, "email@email.com",
            new Date(2002, 12, 12).toString(), "*****", "profile_picture.jpg",
            "Baseball");
        user.setId(1L);
        return tabUserService.addTabUser(user);
    }

    /**
     * Create test for the team
     *
     * @return a team that is created in the database
     */
    public Team createTestTeam(String name, Location location) {
        Team team = new Team(name, "Basketball", location, "pfp");
        team.setId(teamService.getTeamResults().size());
        team.setCreationDate(TokenGenerator.getCurrentTime());
        team = teamService.createTeam(team);
        return teamService.registerTabUserToTeam(team, IntegrationTestConfigurations.loggedInUser);
    }

    /**
     * Create activity for test
     *
     * @return a activity that is created in the database
     */
    public Activity createTestActivity(String teamName, String userName, Location location) {

        TabUser user = createTestUser(userName, location);
        Team team = createTestTeam(teamName, location);

        Activity activity = new Activity(user, team, "Game", "2023-07-17T14:18", "2023-07-17T15:18",
            "Description", location);
        activity.setPosition("1-2");

        return activityService.createActivity(activity);
    }

    /**
     * Create a club for testing
     *
     * @return the club object
     */
    public Club createTestClub(String clubName, Location location) {
        return clubService.saveClub(new Club(clubName, SupportedSports.Football, location,
            tabUserService.getCurrentlyLoggedIn(), ""));
    }

    /**
     * Creates multiple teams
     *
     * @param numberOfTeams the number of teams to create
     * @param locationList  a list of location entities to attach to the created teams
     */
    public void createMultipleTeams(int numberOfTeams, List<Location> locationList) {
        for (int i = 0; i < numberOfTeams; i++) {
            createTestTeam("Team" + teamService.getTeamResults().size(), locationList.get(i));
        }
    }

    /**
     * Creates multiple locations
     *
     * @param numberOfLocations the number of locations to create
     * @return a list of location entities
     */
    public List<Location> createMultipleLocations(int numberOfLocations) {
        List<Location> locationList = new ArrayList<>();

        for (int i = 0; i < numberOfLocations; i++) {
            int number = locationService.getAllLocationInDatabase().size();
            Location location = createTestLocation("City" + number, "Country" + number);
            locationList.add(location);
        }
        return locationList;
    }

    /**
     * Creates multiple clubs
     *
     * @param locationList A list of location entities
     */
    public void createMultipleClubs(List<Location> locationList) {
        List<String> clubNames = Arrays.asList("Club 1", "Club 2", "Club 3", "Club 4", "Club 5",
            "Club 6", "Club 7", "Club 8", "Club 9", "Club 10");
        for (int i = 0; i < clubNames.size(); i++) {
            createTestClub(clubNames.get(i), locationList.get(i));
        }
    }

    /**
     * Creates multiple users
     *
     * @param numberOfUsers the number of users
     * @param sport         a string representing a sport
     * @param city          a string representing a city
     */
    public void createMultipleUsers(int numberOfUsers, String sport, String city) {
        Date dateOfBirth = new Date(2002, 11, 31);
        for (int i = 0; i < numberOfUsers; i++) {
            tabUserService.createFullTabUserInDatabase(Arrays.asList("Mr", "TestMan"),
                new Location(city, "Jouzu"),
                "GoodTest" + tabUserService.getFormResults().size() + "@BestTests.com",
                dateOfBirth.toString(), "letMeIn:)",
                "coolImage", sport);
        }
    }

    @And("there are multiple teams")
    public void there_are_multiple_teams() {
        List<Location> locationList = createMultipleLocations(10);
        createMultipleTeams(10, locationList);
    }

    @And("there are multiple clubs")
    public void there_are_multiple_clubs() {
        List<Location> locationList = createMultipleLocations(10);
        createMultipleClubs(locationList);
    }

    @And("There are {int} users with sport {string} and city {string}")
    public void there_are_multiple_users_with_sport(int numberOfUsers, String sport, String city) {
        createMultipleUsers(numberOfUsers, sport, city);
    }

    @And("there is a team with name {string} and {string} and {string}")
    public void there_is_a_team_with_name_and_and(String teamName, String city, String country) {
        Location location = createTestLocation(city, country);
        createTestTeam(teamName, location);
    }

    @And("there is a club with name {string} and {string} and {string}")
    public void there_is_a_club_with_name_and_and(String clubName, String city, String country) {
        Location location = createTestLocation(city, country);
        createTestClub(clubName, location);
    }

    @Given("There is nothing in the DB")
    public void there_is_nothing_in_the_db() {
        tabUserService.deleteAllData();
        teamService.deleteAllData();
    }

    @And("There are meals")
    public void there_are_meals() {
        createMeal("Cut 1", "Cut", 50.0);
        createMeal("Cut 2", "Cut", 100.0);
        createMeal("Cut 3", "Cut", 150.0);
        createMeal("Cut 4", "Cut", 200.0);
        createMeal("Maintain 1", "Maintain", 150.0);
        createMeal("Maintain 2", "Maintain", 200.0);
        createMeal("Maintain 3", "Maintain", 250.0);
        createMeal("Maintain 4", "Maintain", 300.0);
        createMeal("Bulk 1", "Bulk", 250.0);
        createMeal("Bulk 2", "Bulk", 300.0);
        createMeal("Bulk 3", "Bulk", 350.0);
        createMeal("Bulk 4", "Bulk", 400.0);


    }

    /**
     * Creates a RecommendedMeal to be used with testing
     * @param mealName The name of the meal
     * @param caloriePreference The type of meal
     * @param calories The total number of calories
     * @return The RecommendedMeal
     */
    public RecommendedMeal createMeal(String mealName, String caloriePreference, double calories) {
        RecommendedMeal recommendedMeal = new RecommendedMeal();
        recommendedMeal.setName(mealName);
        recommendedMeal.setCaloriePreference(caloriePreference);
        recommendedMeal = mealRepository.save(recommendedMeal);
        Food food = new Food();
        food.setMeal(recommendedMeal);
        food.setName("food");
        food.setCalories(calories);
        food.setFat(calories);
        food.setCarbs(calories);
        food.setSugar(calories);
        food.setProtein(calories);
        food.setfdcId(13027L);
        foodRepository.save(food);
        List<Food> foods = new ArrayList<>();
        foods.add(food);
        recommendedMeal.setFoods(foods);
        return mealRepository.save(recommendedMeal);
    }
}
