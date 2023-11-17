package nz.ac.canterbury.seng302.tab.unit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import nz.ac.canterbury.seng302.tab.controller.EditTeamController;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.entity.TeamRoles;
import nz.ac.canterbury.seng302.tab.formobjects.EditTeamForm;
import nz.ac.canterbury.seng302.tab.service.ClubService;
import nz.ac.canterbury.seng302.tab.service.LocationService;
import nz.ac.canterbury.seng302.tab.service.TeamRoleService;
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
import org.springframework.ui.Model;


/**
 * Test class for edit team controller
 */
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin", authorities = "USER")
class EditTeamControllerTest {

    Model model = mock(Model.class);

    Team team = UnitCommonTestSetup.createTestTeam();

    Team teamWithClub = UnitCommonTestSetup.createTestTeamWithClub();

    @Mock
    private TeamRoleService teamRoleService;

    @Mock
    private TeamService teamService;

    @Mock
    private ValidationService validationService;

    @Mock
    private LocationService locationService;

    @Mock
    private ClubService clubService;

    @InjectMocks
    private EditTeamController editTeamController;

    /**
     * Test to check when the edit page is loaded with the correct data.
     *
     * @throws Exception will throw there is an error on load page.
     */
    @Test
    void loadedTeamDetailOfId1() throws Exception {


        List<TeamRoles> roles = UnitCommonTestSetup.createTestListTeamRoles();

        when(teamService.getTeamById(anyLong())).thenReturn(team);
        when(locationService.getLocationByLocationId(anyLong())).thenReturn(team.getLocation());
        when(teamRoleService.getUsersAndRoles(anyLong())).thenReturn(roles);

        String response = editTeamController.editTeam(anyLong(), "first", model);
        Assertions.assertTrue(response.contains("editTeamProfile"));
    }

    /**
     * Test the update a team with invalid input and check whether error message show on the page.
     *
     * @throws Exception will throw there is an error on load page.
     */
    @Test
    void inputInvalidThenUpdateTheTeam() throws Exception {

        EditTeamForm editTeamForm = UnitCommonTestSetup.createTestInvalidEditTeamForm();
        List<TeamRoles> roles = UnitCommonTestSetup.createTestListTeamRoles();

        when(validationService.validateEditTeamForm(any(), any())).thenReturn(false);
        when(teamService.getTeamById(anyLong())).thenReturn(team);
        when(teamRoleService.getUsersAndRoles(anyLong())).thenReturn(roles);

        String response = editTeamController.updateTeamDetail(editTeamForm, model);
        Assertions.assertTrue(response.contains("editTeamProfile"));
    }

    /**
     * Test the edit team with valid input and check whether redirect to another page.
     *
     * @throws Exception will throw there is an error on load page.
     */
    @Test
    void inputValidThenUpdateTheTeam() throws Exception {

        EditTeamForm editTeamForm = UnitCommonTestSetup.createTestValidEditTeamForm();
        List<TeamRoles> roles = UnitCommonTestSetup.createTestListTeamRoles();

        when(validationService.validateEditTeamForm(any(), any())).thenReturn(true);
        when(teamService.getTeamById(anyLong())).thenReturn(team);
        when(teamService.updateTeamInDatabase(anyString(), anyString(), anyString(), anyString(),
            anyString())).thenReturn(team);
        when(teamRoleService.getUsersAndRoles(anyLong())).thenReturn(roles);
        when(teamRoleService.updateTeamRole(roles.get(anyInt()))).thenReturn(roles.get(0));
        doNothing().when(locationService).updateOptionalInDatabase(any(), any(), any(), any(),
            any());
        when(locationService.updateMandatoryInDatabase(anyLong(), anyString(),
            anyString())).thenReturn(team.getLocation());

        String response = editTeamController.updateTeamDetail(editTeamForm, model);
        Assertions.assertTrue(response.contains("viewTeam"));

    }

    /**
     * Test the edit team with valid input and check whether redirect to another page.
     *
     * @throws Exception will throw there is an error on load page.
     */
    @Test
    void givenTeamHasClubThenSportDisabled() throws Exception {

        List<TeamRoles> roles = UnitCommonTestSetup.createTestListTeamRoles();

        when(teamService.getTeamById(anyLong())).thenReturn(teamWithClub);
        when(locationService.getLocationByLocationId(anyLong())).thenReturn(team.getLocation());
        when(teamRoleService.getUsersAndRoles(anyLong())).thenReturn(roles);

        String response = editTeamController.editTeam(anyLong(), "first", model);
        Assertions.assertTrue(response.contains("editTeamProfile"));


    }

    /**
     * Test the edit team with valid input and check whether redirect to another page.
     *
     * @throws Exception will throw there is an error on load page.
     */
    @Test
    void givenTeamHasNoClubThenSportNotDisabled() throws Exception {

        List<TeamRoles> roles = UnitCommonTestSetup.createTestListTeamRoles();

        when(teamService.getTeamById(anyLong())).thenReturn(team);
        when(locationService.getLocationByLocationId(anyLong())).thenReturn(team.getLocation());
        when(teamRoleService.getUsersAndRoles(anyLong())).thenReturn(roles);

        String response = editTeamController.editTeam(anyLong(), "first", model);
        Assertions.assertTrue(response.contains("editTeamProfile"));


    }

}

