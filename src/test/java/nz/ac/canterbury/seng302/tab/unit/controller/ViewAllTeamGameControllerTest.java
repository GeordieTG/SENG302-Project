package nz.ac.canterbury.seng302.tab.unit.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javassist.NotFoundException;
import nz.ac.canterbury.seng302.tab.controller.ViewAllTeamsGamesController;
import nz.ac.canterbury.seng302.tab.entity.Activity;
import nz.ac.canterbury.seng302.tab.entity.GameActivity;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.service.ClubService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.TeamStatsService;
import nz.ac.canterbury.seng302.tab.unit.UnitCommonTestSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

/**
 * Test class for view all team controller test
 */
@SpringBootTest
@AutoConfigureMockMvc
class ViewAllTeamGameControllerTest {

    @Mock
    TabUserService tabUserService;

    @InjectMocks
    private ViewAllTeamsGamesController viewAllTeamsGamesController;

    @Mock
    private ClubService clubService;

    @Mock
    private TeamStatsService teamStatsService;

    /**
     * Test the page loads the other activity of the team
     */
    @Test
    void viewAllteamGameControllerTest() throws NotFoundException, IOException {

        Long id = 123L;
        TabUser user = UnitCommonTestSetup.createTestUser();
        List<Activity> act = new ArrayList<Activity>();
        Activity activity = UnitCommonTestSetup.createTestActivityWithType("Training");
        Activity activity1 = UnitCommonTestSetup.createTestActivityWithType("Competition");
        act.add(activity);
        act.add(activity1);
        List<GameActivity> act1 = new ArrayList<GameActivity>();
        Team team = activity.getTeam();
        team.setId(id);
        Model model = new ConcurrentModel();
        when(teamStatsService.getTeamOtherActivities(anyLong())).thenReturn(act);
        String response = viewAllTeamsGamesController.getCreateActivityForm(id, model);
        Assertions.assertTrue(response.contains("viewAllTeamsGames"));
        Assertions.assertEquals(model.getAttribute("allOtherGame"), act);
    }
}
