package nz.ac.canterbury.seng302.tab.unit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import nz.ac.canterbury.seng302.tab.controller.CreateTeamController;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.formobjects.CreateTeamForm;
import nz.ac.canterbury.seng302.tab.service.LocationService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.TeamService;
import nz.ac.canterbury.seng302.tab.service.ValidationService;
import nz.ac.canterbury.seng302.tab.unit.UnitCommonTestSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

/**
 * Test class for creatTeam controller
 */
@SpringBootTest
@AutoConfigureMockMvc
class CreateTeamControllerTest {

    UnitCommonTestSetup unitCommonTestSetup = new UnitCommonTestSetup();
    @Mock
    TabUserService tabUserService;

    @Mock
    LocationService locationService;

    @Mock
    TeamService teamService;

    @Mock
    ValidationService validationService;

    @InjectMocks
    CreateTeamController createTeamController;

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test the project load the correct page when run
     *
     * @throws Exception will throw there is an error on load page
     */
    @Test
    @WithMockUser(username = "test@gmail.com", password = "Qwerty10!", authorities = "USER")
    void contextLoads() throws Exception {

        mockMvc.perform(get("/createTeam")
                .contentType("text/html; charset=UTF-8"))
            .andExpect(status().isOk());
    }

    /**
     * Test the creation of a team with valid input and check whether redirect to another page
     *
     * @throws Exception will throw there is an error on load page
     */
    @Test
    @WithMockUser(username = "test@gmail.com", password = "Qwerty10!", authorities = "USER")
    void whenValidInput_thenReturns200() throws Exception {

        TabUser user = UnitCommonTestSetup.createTestUser();
        Team team = UnitCommonTestSetup.createTestTeam();
        CreateTeamForm createTeamForm = unitCommonTestSetup.createTestTeamForm();

        // Mock service calls
        when(tabUserService.getCurrentlyLoggedIn()).thenReturn(user);
        when(validationService.validateCreateTeamForm(any(), any())).thenReturn(true);
        when(teamService.createTeamInDatabase(anyString(), anyString(), anyString(),
            anyString(), anyString())).thenReturn(team);
        doNothing().when(locationService).updateOptionalInDatabase(any(), any(), any(), any(),
            any());

        String result = createTeamController.submitForm(createTeamForm, mock(Model.class));

        Assertions.assertTrue(result.contains("/viewTeam"));
    }

    /**
     * Test the creation of a team with invalid input and check whether error message come out
     *
     * @throws Exception will throw there is an error on load page
     */
    @Test
    void whenInvalidInput_thenErrorMessage() throws Exception {

        TabUser user = UnitCommonTestSetup.createTestUser();
        Team team = UnitCommonTestSetup.createTestTeam();
        CreateTeamForm createTeamForm = unitCommonTestSetup.createTestTeamForm();

        // Mock service calls
        when(tabUserService.getCurrentlyLoggedIn()).thenReturn(user);
        when(validationService.validateCreateTeamForm(any(), any())).thenReturn(false);
        when(teamService.createTeamInDatabase(anyString(), anyString(), anyString(),
            anyString(), anyString())).thenReturn(team);
        doNothing().when(locationService).updateOptionalInDatabase(any(), any(), any(), any(),
            any());

        String result = createTeamController.submitForm(createTeamForm, mock(Model.class));

        Assertions.assertTrue(result.contains("createTeam"));
    }
}
