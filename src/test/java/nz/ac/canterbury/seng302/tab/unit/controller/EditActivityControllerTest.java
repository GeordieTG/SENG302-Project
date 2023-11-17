package nz.ac.canterbury.seng302.tab.unit.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javassist.NotFoundException;
import nz.ac.canterbury.seng302.tab.controller.EditActivityController;
import nz.ac.canterbury.seng302.tab.entity.Activity;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.formobjects.ActivityForm;
import nz.ac.canterbury.seng302.tab.service.ActivityService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.TeamService;
import nz.ac.canterbury.seng302.tab.service.ValidationService;
import nz.ac.canterbury.seng302.tab.unit.UnitCommonTestSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin", authorities = "USER")
class EditActivityControllerTest {

    @Mock
    private ValidationService validationService;
    @Mock
    private TabUserService tabUserService;
    @Mock
    private ActivityService activityService;
    @Mock
    private TeamService teamService;
    @InjectMocks
    private EditActivityController editActivityController;

    @Test
    void getEditActivity() throws NotFoundException {
        Activity activity = UnitCommonTestSetup.createTestActivity();
        when(activityService.getActivityById(anyLong())).thenReturn(activity);
        TabUser user = UnitCommonTestSetup.createTestUser();
        List<Team> teams = new ArrayList<>();
        Team team = UnitCommonTestSetup.createTestTeam();
        teams.add(team);
        when(tabUserService.getCurrentlyLoggedIn()).thenReturn(user);
        when(teamService.getTeamsThatUserManagesOrCoaches(user)).thenReturn(teams);
        Model model = new ConcurrentModel();
        String response = editActivityController.responses(String.valueOf(1L), model);
        Assertions.assertTrue(response.contains("editActivity"));
        Assertions.assertEquals(teams, model.getAttribute("usersTeams"));
    }

    @Test
    void updateActivity() throws NotFoundException, IOException {
        Model model = new ConcurrentModel();
        Activity activity = UnitCommonTestSetup.createTestActivity();
        ActivityForm activityForm = new ActivityForm(activity);
        when(validationService.validateActivityForm(activityForm, model)).thenReturn(true);
        when(activityService.updateActivityInDatabase(activityForm)).thenReturn(activity);
        String response = editActivityController.updateActivity(activityForm, model);
        Assertions.assertTrue(response.contains("redirect:/viewActivity?id=1"));
        Assertions.assertEquals(activityForm, model.getAttribute("activityForm"));
    }

    @Test
    void updateActivityInvalidForm() throws NotFoundException {
        TabUser user = UnitCommonTestSetup.createTestUser();
        List<Team> teams = new ArrayList<>();
        Team team = UnitCommonTestSetup.createTestTeam();
        teams.add(team);
        when(tabUserService.getCurrentlyLoggedIn()).thenReturn(user);
        when(teamService.getTeamsThatUserManagesOrCoaches(user)).thenReturn(teams);
        Model model = new ConcurrentModel();
        Activity activity = UnitCommonTestSetup.createTestActivity();
        ActivityForm activityForm = new ActivityForm(activity);
        String response = editActivityController.updateActivity(activityForm, model);
        Assertions.assertTrue(response.contains("editActivity"));
        Assertions.assertEquals(teams, model.getAttribute("usersTeams"));
    }
}
