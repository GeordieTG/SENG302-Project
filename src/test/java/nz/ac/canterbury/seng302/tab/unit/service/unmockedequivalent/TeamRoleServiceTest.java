package nz.ac.canterbury.seng302.tab.unit.service.unmockedequivalent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import nz.ac.canterbury.seng302.tab.authentication.AuthenticationGrant;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.entity.TeamRoles;
import nz.ac.canterbury.seng302.tab.repository.TeamRoleRepository;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.TeamRoleService;
import nz.ac.canterbury.seng302.tab.service.TeamService;
import nz.ac.canterbury.seng302.tab.unit.UnitCommonTestSetup;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;

/**
 * Test class for ViewTeamController
 */
@SpringBootTest
class TeamRoleServiceTest {
    @MockBean
    TeamRoleRepository teamRoleRepository;
    @Autowired
    private TeamService teamService;
    @Autowired
    private TeamRoleService teamRoleService;
    @Autowired
    private TabUserService tabUserService;
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Create a team with specific data, create a user, and then test the outcome
     * of creating a role for the user on the team.
     */
    @Test
    void createTeamRoleTest() {
        TabUser user = UnitCommonTestSetup.createTestUser();
        Team team = UnitCommonTestSetup.createTestTeam();

        List<TeamRoles> teamRoles = new ArrayList<>();
        TeamRoles teamRole = new TeamRoles(team, user, "Manager");
        teamRoles.add(teamRole);

        when(teamRoleRepository.findAllMemberTeamsAndRoles(user.getId())).thenReturn(teamRoles);

        teamRoleService.getTeamsAndRoles(user.getId());

        assertEquals(team, teamRoles.get(0).getTeam());
        assertEquals(user, teamRoles.get(0).getUser());
        assertEquals("Manager", teamRoles.get(0).getRole());
    }

    /**
     * Create a team, create a user, and then test the outcome of creating a role
     * for the user on the team.
     */
    @Test
    void addTabUserToTeamTest() {
        TabUser user = UnitCommonTestSetup.createTestUser();
        Team team = UnitCommonTestSetup.createTestTeam();

        teamRoleService.tabUserJoinsTeam(team, user);

        ArgumentCaptor<TeamRoles> teamRolesArgumentCaptor =
            ArgumentCaptor.forClass(TeamRoles.class);
        Mockito.verify(teamRoleRepository).save(teamRolesArgumentCaptor.capture());

        TeamRoles role = teamRolesArgumentCaptor.getValue();

        assertEquals(team, role.getTeam());
        assertEquals(user.getId(), role.getUser().getId());
        assertEquals("Member", role.getRole());
    }

    /**
     * Create a team, create a user, and then test the outcome of
     * updating the role of the user on the team.
     */
    @Test
    void updateTeamRoleTest() {
        TabUser user = UnitCommonTestSetup.createTestUser();
        Team team = UnitCommonTestSetup.createTestTeam();

        TeamRoles teamRole = new TeamRoles(team, user);
        teamRole.setRole("Manager");
        teamRoleService.updateTeamRole(teamRole);

        assertEquals("Manager", teamRole.getRole());
    }

    /**
     * Create a team, create a user, add the user to the team,
     * and then test the outcome of
     * deleting the user's role on the team.
     */
    @Test
    void deleteTeamRoleTest() {
        TabUser user = tabUserService
            .createFullTabUserInDatabase(Arrays.asList("test", "person"),
                    new Location("Christchurch", "New Zealand"),
                    "email@email.com",
                new Date(2002 + 1900, 11, 31).toString(), "*****",
                "image.jpg", "sport");
        AuthenticationGrant.authenticateUser(user, authenticationManager);
        Team team =
            teamService.createTeamInDatabase("Swan", "Basketball",
                "Christchurch", "NotJapan",
                "default_team_image.png");
        teamRoleService.tabUserJoinsTeam(team, user);
        teamRoleService.removeUserFromTeam(user.getId(), team.getId());
        List<TeamRoles> teamRoles = teamRoleService.getUsersAndRoles(team.getId());
        assertEquals(0, teamRoles.size());
    }

    /**
     * Adds additional 7 points to a user in a team who already has 20 recorded.
     */
    @Test
    void updateUsersTotalPointsWith20Points() {
        int points = 20;

        TabUser user = UnitCommonTestSetup.createTestUser();
        Team team = UnitCommonTestSetup.createTestTeam();
        TeamRoles teamRole = new TeamRoles(team, user);
        teamRole.setTotalPoints(points);

        int additionalPoints = 7;
        when(teamRoleRepository.getTeamRoles(anyLong(), anyLong())).thenReturn(teamRole);
        when(teamRoleRepository.save(any())).thenReturn(teamRole);
        int totalPoints = teamRoleService.updateTotalPoints(team.getId(),
            user.getId(), additionalPoints);
        int expectedPoints = 27;
        assertEquals(expectedPoints, totalPoints);
    }

    /**
     * Creates a user and team and joins them,
     * then adds 0 points checking if the total points equal 0
     */
    @Test
    void updateUsersTotalPointsWithNoPoints() {
        int points = 0;

        TabUser user = UnitCommonTestSetup.createTestUser();
        Team team = UnitCommonTestSetup.createTestTeam();
        TeamRoles teamRole = new TeamRoles(team, user);
        teamRole.setTotalPoints(points);

        int additionalPoints = 0;
        when(teamRoleRepository.getTeamRoles(anyLong(), anyLong())).thenReturn(teamRole);
        when(teamRoleRepository.save(any())).thenReturn(teamRole);
        int totalPoints = teamRoleService.updateTotalPoints(team.getId(), user.getId(),
            additionalPoints);
        assertEquals(0, totalPoints);
    }

    /**
     * Creates a user and team and joins them,
     * then adds a full football game of 90 minutes to total time then checks
     * if it updated in Database
     */
    @Test
    void updateUsersTotalTimePlayedAtTeamFullGameOfFootball() {
        TabUser user = UnitCommonTestSetup.createTestUser();
        Team team = UnitCommonTestSetup.createTestTeam();
        TeamRoles teamRole = new TeamRoles(team, user);
        teamRole.setTotalTime(0);

        when(teamRoleRepository.getTeamRoles(anyLong(), anyLong())).thenReturn(teamRole);
        when(teamRoleRepository.save(any())).thenReturn(teamRole);

        int time = 90;
        int totalTime = teamRoleService.updateTotalTime(team.getId(), user.getId(), time);
        assertEquals(time, totalTime);
    }

    /**
     * Creates a user and team and joins them,
     * then adds 0 minutes to database and checks if total time still equals 0
     */
    @Test
    void updateUserTotalTimePlayedNoGameTime() {
        TabUser user = UnitCommonTestSetup.createTestUser();
        Team team = UnitCommonTestSetup.createTestTeam();
        team.setName("Greyhounds");
        TeamRoles teamRole = new TeamRoles(team, user);
        int time = 0;
        teamRole.setTotalTime(time);

        when(teamRoleRepository.getTeamRoles(anyLong(), anyLong())).thenReturn(teamRole);
        when(teamRoleRepository.save(any())).thenReturn(teamRole);

        int totalTime = teamRoleService.updateTotalTime(team.getId(), user.getId(), time);
        assertEquals(time, totalTime);
    }
}
