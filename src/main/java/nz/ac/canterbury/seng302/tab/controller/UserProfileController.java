package nz.ac.canterbury.seng302.tab.controller;

import static nz.ac.canterbury.seng302.tab.utility.HeaderHelper.addLoggedInAttr;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import javassist.NotFoundException;
import nz.ac.canterbury.seng302.tab.entity.Activity;
import nz.ac.canterbury.seng302.tab.entity.ExerciseGoals;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.entity.TeamRoles;
import nz.ac.canterbury.seng302.tab.entity.enums.GarminState;
import nz.ac.canterbury.seng302.tab.entity.enums.SupportedSports;
import nz.ac.canterbury.seng302.tab.entity.garminentities.GarminDaily;
import nz.ac.canterbury.seng302.tab.entity.garminentities.HeartRateData;
import nz.ac.canterbury.seng302.tab.exception.GarminPermissionException;
import nz.ac.canterbury.seng302.tab.formobjects.ExerciseGoalsForm;
import nz.ac.canterbury.seng302.tab.formobjects.UserEditProfilePageForm;
import nz.ac.canterbury.seng302.tab.service.ActivityService;
import nz.ac.canterbury.seng302.tab.service.ExerciseGoalsService;
import nz.ac.canterbury.seng302.tab.service.GarminService;
import nz.ac.canterbury.seng302.tab.service.LocationService;
import nz.ac.canterbury.seng302.tab.service.MealService;
import nz.ac.canterbury.seng302.tab.service.RecordedFoodService;
import nz.ac.canterbury.seng302.tab.service.SportService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.TeamRoleService;
import nz.ac.canterbury.seng302.tab.service.ValidationService;
import nz.ac.canterbury.seng302.tab.utility.EmailHelper;
import nz.ac.canterbury.seng302.tab.utility.ModelHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Controller for the user profile
 */
@Controller
public class UserProfileController {

    private static final String PROFILE_PICTURE = "profilePicture";
    private static final String PROFILE_PAGE = "profilePage";
    private static final String EDIT_PROFILE = "editProfile";
    private static final String IGNORE = "ignore";
    private static final int NO_FOOD_ADDED = 0;
    /**
     * To generate logs
     */
    Logger logger = LoggerFactory.getLogger(UserProfileController.class);
    /**
     * To use the email confirmation
     */
    @Autowired
    EmailHelper emailHelper;
    @Autowired
    MealService mealService;

    List<String>
        invalidTabList = List.of("invalidFirstTab", "invalidSecondTab", "invalidThirdTab");
    /**
     * To use tab user functionalities with the database
     */
    @Autowired
    private TabUserService tabUserService;
    /**
     * To use the team role service with the database
     * where the user related to as a member of the team
     */
    @Autowired
    private TeamRoleService teamRoleService;
    @Autowired
    private GarminService garminService;
    /**
     * To validate the user input
     */
    @Autowired
    private ValidationService validationservice;
    /**
     * To use the sport functionalities with the database
     */
    @Autowired
    private SportService sportService;
    /**
     * To use the location functionalities with the database
     */
    @Autowired
    private LocationService locationService;
    @Autowired
    private ActivityService activityService;

    @Autowired
    private ExerciseGoalsService exerciseGoalsService;

    @Autowired
    private RecordedFoodService recordedFoodService;

    private UserProfileController() {
        // Private constructor
    }

    /**
     * Gets the information for the currently signed-in user
     *
     * @param model (map-like) representation of name, language and
     *              isJava boolean for use in thymeleaf
     * @return thymeleaf profilePage
     */
    @GetMapping("/editProfile")
    public String userProfileEdit(@RequestParam(name = "sportQuery", required = false,
        defaultValue = IGNORE) String sportQuery,
                                  @RequestParam(name = "cityQuery", required = false,
                                      defaultValue = IGNORE) String cityQuery,
                                  @RequestParam(name = "searchQuery", required = false,
                                      defaultValue = IGNORE) String searchQuery,
                                  @RequestParam(name = "pageNumber", required = false,
                                      defaultValue = IGNORE) String pageNumber,
                                  @RequestParam(name = "tab", required = false,
                                      defaultValue = "Profile") String tab,
                                  Model model) throws IOException {
        logger.info("GET /editProfile");
        addFilterAttributes(cityQuery, sportQuery, searchQuery, pageNumber, model);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        TabUser user = tabUserService.findByEmail(currentPrincipalName);
        model.addAttribute("userID", user.getId());
        if (tabUserService.getInfoByEmail(
            user.getEmail()).getProfilePicture().equals("")) {
            model.addAttribute(PROFILE_PICTURE, "default.jpg");
        } else {
            model.addAttribute(PROFILE_PICTURE,
                tabUserService.getInfoByEmail(user.getEmail()).getProfilePicture());
        }

        Location location = locationService.getLocationByLocationId(user.getLocationId());
        model.addAttribute("options",
            EnumSet.range(SupportedSports.Baseball, SupportedSports.Volleyball));
        addLoggedInAttr(model);
        UserEditProfilePageForm form = new UserEditProfilePageForm(user, location);
        model.addAttribute("userEditProfilePage", form);
        model.addAttribute("tab", tab);
        model.addAttribute(invalidTabList.get(1), false);
        model.addAttribute(invalidTabList.get(2), false);
        return EDIT_PROFILE;
    }


    /**
     * Updates the information for the currently signed-in user
     *
     * @param model (map-like) representation of name, language and
     *              isJava boolean for use in thymeleaf
     * @return thymeleaf profileTemplate
     */
    @PostMapping("/editProfile")
    public String submitUser(@ModelAttribute UserEditProfilePageForm userEditProfilePage,
                             Model model) throws NotFoundException, IOException {
        logger.info("POST /editProfile");
        model.addAttribute("userEditProfilePage", userEditProfilePage);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        TabUser user = tabUserService.findByEmail(currentPrincipalName);
        Location location = locationService.getLocationByLocationId(user.getLocationId());
        model.addAttribute(PROFILE_PICTURE, user.getProfilePicture());
        boolean emailExists = tabUserService.getByEmail(userEditProfilePage.getEmail());

        if (validationservice.validateEditUserProfileForm(userEditProfilePage, user.getEmail(),
            model)) {
            if ((user.getEmail()).equals(userEditProfilePage.getEmail())) {
                model.addAttribute("invalidEmail", false);
                //no change in email so no checks required
                updateUser(userEditProfilePage, location);
                model.addAttribute(PROFILE_PICTURE, user.getProfilePicture());
                return "redirect:./profilePage";
                //set user details for auth purposes
            } else {
                //change in email so must check it doesn't exist
                if (!emailExists) {
                    model.addAttribute("invalidEmail", false);
                    tabUserService.updateEmail(user.getEmail(), userEditProfilePage.getEmail());
                    updateUser(userEditProfilePage, location);
                    //Update email for Auth
                    Collection<SimpleGrantedAuthority> nowAuthorities =
                        (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext()
                            .getAuthentication()
                            .getAuthorities();
                    UsernamePasswordAuthenticationToken newAuth =
                        new UsernamePasswordAuthenticationToken(userEditProfilePage.getEmail(),
                            null, nowAuthorities);
                    SecurityContextHolder.getContext().setAuthentication(newAuth);
                    return "redirect:./profilePage";
                } else {
                    model.addAttribute(PROFILE_PICTURE, user.getProfilePicture());
                    return EDIT_PROFILE;
                }
            }
        } else {
            model.addAttribute("options", SupportedSports.values());
            model.addAttribute(PROFILE_PICTURE, user.getProfilePicture());
        }
        return EDIT_PROFILE;
    }

    private void updateUser(@ModelAttribute UserEditProfilePageForm userEditProfilePage,
                            Location location) throws NotFoundException, IOException {
        tabUserService.updatedob(userEditProfilePage.getEmail(),
            userEditProfilePage.getDateOfBirth());
        tabUserService.updateFirstName(userEditProfilePage.getEmail(),
            userEditProfilePage.getFirstName());
        tabUserService.updateLastName(userEditProfilePage.getEmail(),
            userEditProfilePage.getLastName());
        tabUserService.updateFavouriteSport(userEditProfilePage.getEmail(),
            userEditProfilePage.getFavouriteSport());
        locationService.updateOptionalInDatabase(location.getLocationId(),
            userEditProfilePage.getAddress1(), userEditProfilePage.getAddress2(),
            userEditProfilePage.getSuburb(), userEditProfilePage.getPostcode());
        locationService.updateMandatoryInDatabase(location.getLocationId(),
            userEditProfilePage.getCity(), userEditProfilePage.getCountry());
    }

    /**
     * Gets the information for the currently signed-in user
     *
     * @param model (map-like) representation of name, language and
     *              isJava boolean for use in thymeleaf
     * @return thymeleaf profilePage
     */
    @GetMapping("/profilePage")
    public String userProfile(@RequestParam(name = "sportQuery", required = false,
        defaultValue = IGNORE) String sportQuery,
                              @RequestParam(name = "cityQuery", required = false,
                                  defaultValue = IGNORE) String cityQuery,
                              @RequestParam(name = "searchQuery", required = false,
                                  defaultValue = IGNORE) String searchQuery,
                              @RequestParam(name = "pageNumber", required = false,
                                  defaultValue = IGNORE) String pageNumber,
                              @RequestParam(name = "tab", required = false,
                                  defaultValue = "Profile") String tab,
                              @RequestParam(name = "foodAdded", required = false, defaultValue =
                                  "" + NO_FOOD_ADDED) int foodAdded,
                              Model model) throws IOException {
        logger.info("GET /profilePage");
        addFilterAttributes(cityQuery, sportQuery, searchQuery, pageNumber, model);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        TabUser user = tabUserService.findByEmail(currentPrincipalName);
        addLoggedInAttr(model);
        Location location = locationService.getLocationByLocationId(user.getLocationId());
        ModelHelper.addUserAttributes(user, model);
        ModelHelper.addLocationAttributes(location, model);
        model.addAttribute("tab", tab);
        if (tabUserService.getInfoByEmail(user.getEmail()).getProfilePicture().equals("")) {
            model.addAttribute(PROFILE_PICTURE, "default.jpg");
        } else {
            model.addAttribute(PROFILE_PICTURE,
                tabUserService.getInfoByEmail(user.getEmail()).getProfilePicture());
        }
        List<TeamRoles> teamRoles = teamRoleService.getTeamsAndRoles(user.getId());
        for (TeamRoles teamRoles1 : teamRoles) {
            long id = teamRoles1.getTeam().getId();
            teamRoles1.getTeam().setTotalGamesPlayed(activityService
                .getTotalGamesTeamPlayedWithStatistics(id));
        }
        model.addAttribute("teamRoleData", teamRoleService.getTeamsAndRoles(user.getId()));
        List<Long> teamIds = tabUserService.getTeamIdsAlphabetically(user.getId());

        boolean hasStatistics = teamRoleService.getTeamsAndRoles(user.getId()).stream()
            .anyMatch(teamRole -> teamRole.getTotalGames() != 0);
        model.addAttribute("hasStatistics", hasStatistics);

        List<Activity> personalActivities = activityService.searchPersonalActivity(user.getId());
        List<List<List<String>>> teamEvents = activityService.createTeamEventsFromTeamIds(teamIds,
            user);
        List<List<String>> personalEvents = activityService.createPersonalEvents(
            personalActivities);
        model.addAttribute("teamEvents", teamEvents);
        model.addAttribute("events", personalEvents);
        model.addAttribute("sports", EnumSet.range(SupportedSports.Baseball,
            SupportedSports.Volleyball));
        model.addAttribute("user", user);
        model.addAttribute("personalActivitiesSize", personalActivities.size());
        String connectedWatch =
            garminService.fetchDailies(tabUserService.getCurrentlyLoggedIn(), model);
        GarminDaily garminDaily = new GarminDaily(0, 0, 0,
            0, 0, new HeartRateData(0, 0, 0, 0), false);
        model.addAttribute("garminDaily", garminDaily);
        if (connectedWatch.equals("True")) {

            try {
                garminDaily = garminService.getMostRecentDailies();
                if (garminDaily != null) {
                    model.addAttribute("garminDaily", garminDaily);
                    model.addAttribute("errorSharingData", "");
                }
            } catch (GarminPermissionException e) {
                model.addAttribute("errorSharingData",
                    "Please enable the data sharing permission"
                    + " in Garmin website");
            }

        }

        // Retrieve exerciseGoals
        ExerciseGoals existingExerciseGoal =
                exerciseGoalsService.getByUser(tabUserService.getCurrentlyLoggedIn());

        // Create exercise goal form
        ExerciseGoalsForm exerciseGoalsForm = existingExerciseGoal == null
            ? new ExerciseGoalsForm()
            : new ExerciseGoalsForm(existingExerciseGoal);


        addNutritionInformation(model);
        model.addAttribute("userGoalsForm", exerciseGoalsForm);
        exerciseGoalsService.calculateGoalProgress(exerciseGoalsForm, garminDaily);
        model.addAttribute("connected", connectedWatch);
        model.addAttribute("connectState", garminService.getGarminState());
        model.addAttribute("showFoodAdded", foodAdded);
        model.addAttribute("caloriePreference", user.getCalorieIntakePreference());
        return PROFILE_PAGE;
    }

    /**
     * Adds attributes the model for passing to HTML
     *
     * @param cityQuery   the city filter parameters
     * @param sportQuery  the sport filter parameters
     * @param searchQuery the search parameter
     * @param pageNumber  the current page number
     * @param model       (map-like) representation of name, language and
     *                    isJava boolean for use in thymeleaf
     */
    public void addFilterAttributes(String cityQuery, String sportQuery, String searchQuery,
                                    String pageNumber, Model model) {
        model.addAttribute("searchQueryCity", cityQuery);
        model.addAttribute("searchQuerySport", sportQuery);
        model.addAttribute("searchName", searchQuery);
        model.addAttribute("pageNumber", pageNumber);
        if (!sportQuery.equals(IGNORE) && !cityQuery.equals(IGNORE)
            && !searchQuery.equals(IGNORE)) {
            model.addAttribute("customRedirectNeeded", true);
        }
    }

    /**
     * update the garmin state form the page
     *
     * @param alert the alert state will be true if it sends
     * @return the http reponse status
     */
    @GetMapping("/resetConnectState")
    @ResponseBody
    public ResponseEntity<Void> getResetConnectStateAlert(
        @RequestParam(value = "reset") String alert) {
        if (alert.equals("True")) {
            garminService.setGarminState(GarminState.NOT_CONNECTED);
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Adds the nutrition information to the model
     *
     * @param model The model to add the info to
     */
    public void addNutritionInformation(Model model) {

        try {
            Map<String, Double> totalNutrientsDictionary =
                recordedFoodService.addNutritionInfoToModel(
                tabUserService.getCurrentlyLoggedIn().getId(), LocalDate.now());
            model.addAttribute("totalCalories",
                totalNutrientsDictionary.get(RecordedFoodService.ENERGY).intValue());
            model.addAttribute("totalFat",
                totalNutrientsDictionary.get(RecordedFoodService.FAT).intValue());
            model.addAttribute("totalCarbs",
                totalNutrientsDictionary.get(RecordedFoodService.CARBOHYDRATES).intValue());
            model.addAttribute("totalProtein",
                totalNutrientsDictionary.get(RecordedFoodService.PROTEIN).intValue());
            model.addAttribute("totalSugar",
                totalNutrientsDictionary.get(RecordedFoodService.SUGAR).intValue());
        } catch (Exception e) {
            model.addAttribute("totalCalories", 0);
            model.addAttribute("totalFat", 0);
            model.addAttribute("totalCarbs", 0);
            model.addAttribute("totalProtein", 0);
            model.addAttribute("totalSugar", 0);
        }
    }

}

