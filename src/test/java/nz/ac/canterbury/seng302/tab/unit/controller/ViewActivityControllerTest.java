package nz.ac.canterbury.seng302.tab.unit.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javassist.NotFoundException;
import nz.ac.canterbury.seng302.tab.controller.ViewActivityController;
import nz.ac.canterbury.seng302.tab.entity.Activity;
import nz.ac.canterbury.seng302.tab.entity.Formation;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.entity.garminentities.GarminActivity;
import nz.ac.canterbury.seng302.tab.exception.GarminPermissionException;
import nz.ac.canterbury.seng302.tab.formobjects.FormationForm;
import nz.ac.canterbury.seng302.tab.service.ActivityService;
import nz.ac.canterbury.seng302.tab.service.ActivityStatisticService;
import nz.ac.canterbury.seng302.tab.service.FormationService;
import nz.ac.canterbury.seng302.tab.service.GarminService;
import nz.ac.canterbury.seng302.tab.service.RoleVerifier;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

/**
 * View Activity controller test class
 */
@ExtendWith(MockitoExtension.class)
class ViewActivityControllerTest {


    static TabUser user;
    private static Activity activity;
    private static Activity activity2;
    private static List<Formation> formations;
    @Mock
    private ActivityService activityService;
    @Mock
    private GarminService garminService;
    @Mock
    private ActivityStatisticService activityStatisticService;
    @Mock
    private TabUserService tabUserService;
    @Mock
    private RoleVerifier roleVerifier;
    @Mock
    private FormationService formationService;
    @InjectMocks
    private ViewActivityController controller;

    /**
     * Set up formation, team, etc
     */
    @BeforeAll
    static void setUp() {
        Location location = new Location("City", "Country");
        user =
            new TabUser(Arrays.asList("test", "person"), location, "email@email.com",
                new Date(2002, 12, 12).toString(),
                "*****", "image.jpg", "Baseball");
        user.setId(1L);
        Team team = new Team("team", "team", location, "team");
        activity = new Activity(user, team, "Game", "2023-05-27T14:30:00",
            "2023-05-27T14:30:00", "description", location);

        activity2 = new Activity(user, team, "Other", "2023-05-27T14:30:00",
            "2023-05-27T14:30:00", "description", location);
        Formation formation = new Formation("4-2-3-1", "Football", team);
        Formation formation2 = new Formation("4-6-5", "Rugby", team);
        formations = new ArrayList<>();
        formations.add(formation);
        formations.add(formation2);
    }

    /**
     * Messy test for get mapping.
     */
    @Test
    void testGetViewActivityPage_ActivityExists_ReturnsViewActivityPage() throws Exception {
        long teamId = 123L;
        // Mock the behavior of the team
        Team team2 = Mockito.mock(Team.class);
        // Set a non-null value for the id field
        when(team2.getId()).thenReturn(teamId);
        activity.setTeam(team2);
        long activityId = 321L;
        activity.setId(activityId);
        when(activityService.getActivityById(activityId)).thenReturn(activity);
        Model model = new ConcurrentModel();
        // Mock the behavior of the roleVerifier
        boolean isManager = true;
        when(roleVerifier.verifyManager(1L, teamId))
            .thenReturn(isManager);
        TabUser user2 = Mockito.mock(TabUser.class);
        when(user2.getId()).thenReturn(1L);
        // Mock the behavior of the tabUserService
        when(tabUserService.getCurrentlyLoggedIn()).thenReturn(user2);
        // Create a list of formations as needed
        when(formationService.findAllFormationsByTeamId(anyLong()))
            .thenReturn(formations);
        String result = controller.getViewActivityPage(activityId, model);

        Assertions.assertEquals("viewActivity", result);
        Assertions.assertEquals(321L, model.getAttribute("id"));
        Assertions.assertEquals(model.getAttribute("formations"), formations);
        Assertions.assertEquals(2, model.getAttribute("formationSize"));
        Assertions.assertEquals(model.getAttribute("activity"), activity);

    }

    /**
     * Test that the formation is visible if a coach
     */
    @Test
    void testGetViewActivityPage_ActivityExistsIsCoach_ReturnsViewActivityPage()
        throws Exception {
        long teamId = 123L;
        // Mock the behavior of the team
        Team team2 = Mockito.mock(Team.class);
        // Set a non-null value for the id field
        when(team2.getId()).thenReturn(teamId);
        activity.setTeam(team2);
        long activityId = 321L;
        activity.setId(activityId);
        when(activityService.getActivityById(activityId)).thenReturn(activity);
        // Mock the behavior of the roleVerifier
        boolean isManager = false;
        boolean isCoach = true;
        Model model = new ConcurrentModel();
        when(roleVerifier.verifyManager(1L, teamId))
            .thenReturn(isManager);
        when(roleVerifier.verifyCoach(1L, teamId, model))
            .thenReturn(isCoach);
        TabUser user2 = Mockito.mock(TabUser.class);
        when(user2.getId()).thenReturn(1L);
        // Mock the behavior of the tabUserService
        when(tabUserService.getCurrentlyLoggedIn()).thenReturn(user2);
        // Create a list of formations as needed
        when(formationService.findAllFormationsByTeamId(anyLong()))
            .thenReturn(formations);
        String result = controller.getViewActivityPage(activityId, model);

        Assertions.assertEquals("viewActivity", result);
        Assertions.assertEquals(321L, model.getAttribute("id"));
        Assertions.assertEquals(model.getAttribute("formations"), formations);
        Assertions.assertEquals(2, model.getAttribute("formationSize"));
        Assertions.assertEquals(model.getAttribute("activity"), activity);
    }

    /**
     * Test if formations don't exist if the activity has no team
     */
    @Test
    void testGetViewActivityPage_ActivityExistsNoTeam_ReturnsViewActivityPage()
        throws Exception {
        // New Model to add attributes to.
        activity2.setTeam(null);
        long activityId = 321L;
        activity2.setId(activityId);
        when(activityService.getActivityById(activityId)).thenReturn(activity2);
        when(tabUserService.getCurrentlyLoggedIn()).thenReturn(user);
        when(activityStatisticService.getPersonalScoringEventsForActivity(anyLong(),
            anyLong())).thenReturn(null);

        Model model = new ConcurrentModel();
        String result = controller.getViewActivityPage(activityId, model);

        Assertions.assertEquals("viewActivity", result);
        Assertions.assertEquals(321L, model.getAttribute("id"));
        Assertions.assertNull(model.getAttribute("formations"));
        Assertions.assertNull(model.getAttribute("formationSize"));
        Assertions.assertEquals(model.getAttribute("activity"), activity2);
    }

    /**
     * Activity page displays formation
     */
    @Test
    void testPreviewLineUp_FormationNotEmpty_ReturnsViewActivityPage() throws Exception {
        // Mock the behavior of the formation parameter and request parameter
        long teamId = 123L;
        // Mock the behavior of the team
        Team team2 = Mockito.mock(Team.class);
        // Set a non-null value for the id field
        when(team2.getId()).thenReturn(teamId);
        activity.setTeam(team2);
        long activityId = 123L;
        activity.setId(activityId);
        when(activityService.getActivityById(activityId)).thenReturn(activity);
        // Mock the behavior of the roleVerifier
        boolean isManager = false;
        boolean isCoach = true;
        Model model = new ConcurrentModel();
        when(roleVerifier.verifyManager(1L, teamId))
            .thenReturn(isManager);
        when(roleVerifier.verifyCoach(1L, teamId, model))
            .thenReturn(isCoach);
        TabUser user2 = Mockito.mock(TabUser.class);
        when(user2.getId()).thenReturn(1L);
        // Mock the behavior of the tabUserService
        when(tabUserService.getCurrentlyLoggedIn()).thenReturn(user2);
        // Create a list of formations as needed
        when(formationService.findAllFormationsByTeamId(anyLong()))
            .thenReturn(formations);
        // Mock the behavior of the formationService
        String bgImage = "images/fields/Football.jpg";
        when(formationService.getFormationImage("Football")).thenReturn(bgImage);

        String formationId = "1";
        Formation formation = new Formation("4-3-2-1", "Football", team2);
        when(formationService.getFormationByIdNotOptional(1)).thenReturn(formation);
        String result = controller.previewLineUp(formationId, String.valueOf(activityId), model);


        Assertions.assertEquals("viewActivity", result);
        Assertions.assertEquals(activityId, model.getAttribute("id"));
        Assertions.assertEquals("4-3-2-1", ((FormationForm) Objects.requireNonNull(
            model.getAttribute("formationForm"))).getFormation());
        Assertions.assertEquals("Football", ((FormationForm) Objects.requireNonNull(
            model.getAttribute("formationForm"))).getSport());
        Assertions.assertEquals(bgImage, model.getAttribute("bgImageEdit"));
    }

    @Test
    void testPreviewLineUp_FormationEmpty_ReturnsViewActivityPage()
        throws NotFoundException, GarminPermissionException {
        // Mock the behavior of the formation parameter and request parameter
        long teamId = 123L;
        // Mock the behavior of the team
        Team team2 = Mockito.mock(Team.class);
        // Set a non-null value for the id field
        when(team2.getId()).thenReturn(teamId);
        activity.setTeam(team2);

        long activityId = 123L;
        activity.setId(activityId);
        when(activityService.getActivityById(activityId)).thenReturn(activity);
        // Mock the behavior of the roleVerifier
        boolean isManager = false;
        boolean isCoach = true;
        Model model = new ConcurrentModel();
        when(roleVerifier.verifyManager(1L, teamId))
            .thenReturn(isManager);
        when(roleVerifier.verifyCoach(1L, teamId, model))
            .thenReturn(isCoach);
        TabUser user2 = Mockito.mock(TabUser.class);
        when(user2.getId()).thenReturn(1L);
        // Mock the behavior of the tabUserService
        when(tabUserService.getCurrentlyLoggedIn()).thenReturn(user2);
        // Create a list of formations as needed
        when(formationService.findAllFormationsByTeamId(anyLong()))
            .thenReturn(formations);
        // Mock the behavior of the formationService
        String formationId = "1";
        Formation formation = new Formation("4-3-2-1", "Hockey", team2);
        when(formationService.getFormationByIdNotOptional(1)).thenReturn(formation);
        when(formationService.getFormationImage(any())).thenReturn("images/fields/Hockey.png");
        String result = controller.previewLineUp(formationId, String.valueOf(activityId), model);


        Assertions.assertEquals("viewActivity", result);
        Assertions.assertEquals(activityId, model.getAttribute("id"));
        Assertions.assertEquals("4-3-2-1", ((FormationForm) Objects.requireNonNull(
            model.getAttribute("formationForm"))).getFormation());
        Assertions.assertEquals("Hockey", ((FormationForm) Objects.requireNonNull(
            model.getAttribute("formationForm"))).getSport());
        Assertions.assertEquals("images/fields/Hockey.png", model.getAttribute("bgImageEdit"));
    }

    /**
     * Test the ability to format the Garmin Activity Time into a readable format when there is no
     * activity time information
     */
    @Test
    void testGetViewActivityPage_DisplayCorrectGarminActivityTime_NoTimeData()
        throws Exception {
        // Mock Irrelevant Functionality
        activity2.setTeam(null);
        long activityId = 321L;
        activity2.setId(activityId);
        when(activityService.getActivityById(activityId)).thenReturn(activity2);
        when(tabUserService.getCurrentlyLoggedIn()).thenReturn(user);
        when(activityStatisticService.getPersonalScoringEventsForActivity(anyLong(),
            anyLong())).thenReturn(null);

        // Return a Garmin Activity with no time data
        GarminActivity garminActivity = new GarminActivity();
        when(garminService.getByActivityAndUser(anyLong(), anyLong())).thenReturn(garminActivity);

        Model model = new ConcurrentModel();
        String result = controller.getViewActivityPage(activityId, model);

        Assertions.assertEquals("viewActivity", result);
        Assertions.assertEquals(model.getAttribute("userGarminStatistics"), garminActivity);
        Assertions.assertEquals(0, model.getAttribute("exerciseHours"));
        Assertions.assertEquals(0, model.getAttribute("exerciseMinutes"));
        Assertions.assertEquals(0, model.getAttribute("exerciseSeconds"));
    }

    /**
     * Test the ability to format the Garmin Activity Time into a readable format when there is no
     * activity time information
     */
    @Test
    void testGetViewActivityPage_DisplayCorrectGarminActivityTime()
        throws Exception {

        // Mock Irrelevant Functionality
        activity2.setTeam(null);
        long activityId = 321L;
        activity2.setId(activityId);
        when(activityService.getActivityById(activityId)).thenReturn(activity2);
        when(tabUserService.getCurrentlyLoggedIn()).thenReturn(user);
        when(activityStatisticService.getPersonalScoringEventsForActivity(anyLong(),
            anyLong())).thenReturn(null);

        // Return a Garmin Activity with no time data
        GarminActivity garminActivity = new GarminActivity();
        garminActivity.setDurationInSeconds(10000);
        when(garminService.getByActivityAndUser(anyLong(), anyLong())).thenReturn(garminActivity);

        // Ensure that the correctly formatted time has been added to the model
        Model model = new ConcurrentModel();
        String result = controller.getViewActivityPage(activityId, model);
        Assertions.assertEquals("viewActivity", result);
        Assertions.assertEquals(model.getAttribute("userGarminStatistics"), garminActivity);
        Assertions.assertEquals(2, model.getAttribute("exerciseHours"));
        Assertions.assertEquals(46, model.getAttribute("exerciseMinutes"));
        Assertions.assertEquals(40, model.getAttribute("exerciseSeconds"));
    }
}
