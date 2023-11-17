package nz.ac.canterbury.seng302.tab.unit.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.transaction.Transactional;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.entity.TeamRoles;
import nz.ac.canterbury.seng302.tab.service.LocationService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.TeamRoleService;
import nz.ac.canterbury.seng302.tab.service.TeamService;
import nz.ac.canterbury.seng302.tab.service.TokenDumpService;
import nz.ac.canterbury.seng302.tab.utility.TokenGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Test class for TeamService
 */
@SpringBootTest
@Transactional
class TeamServiceTest {

    /**
     * Allows us to access team functionality with the database
     */
    @Autowired
    TeamService teamService;
    @Autowired
    TeamRoleService teamRoleService;
    @Autowired
    TokenDumpService tokenDumpService;

    @Autowired
    LocationService locationService;

    /**
     * Allows us to access user functionality with the database
     */
    @Autowired
    TabUserService tabUserService;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Refreshes database after each run through
     */
    @BeforeEach
    void setUp() {

        for (Team team : teamService.getTeamResults()) {
            team.getTeamRoles().clear();
        }
        teamService.deleteAllData();
        tabUserService.deleteAllData();

        TabUser user = tabUserService.createFullTabUserInDatabase(Arrays.asList("test",
            "person"), new Location("Christchurch",
            "New Zealand"), "email@email.com",
            new Date(2002 + 1900, 11, 31).toString(), "*****", "image.jpg",
            "Baseball");
        tabUserService.addTabUser(user);
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
    }

    /**
     * Create a team with specific data and test the outcome
     */
    @Test
    void createTeamInDatabaseTest() {
        int initialSize = teamService.getTeamResults().size();

        Team team =
            teamService.createTeamInDatabase("Swan", "Basketball", "Christchurch", "NotJapan",
                "default_team_image.png");
        List<Team> teams = teamService.getTeamResults();
        assertEquals(initialSize + 1, teams.size());
        assertEquals("Swan", team.getName());
        assertEquals("Basketball", team.getSport());
        assertEquals("Christchurch", team.getLocation().getCity());
    }

    /**
     * Checks that a creation date is added to the database when a team is created
     * The test checks if the date is present and if the time is correct to the minute.
     * (time changes between creating and asserting)
     */
    @Test
    void checkCreationTime() {
        Team team =
            teamService.createTeamInDatabase("Swan", "Basketball",
                "Christchurch", "NotJapan",
                "default_team_image.png");

        assertNotNull(team.getCreationDate());
        assertEquals(TokenGenerator.getCurrentTime().toString().split("\\.")[0],
            team.getCreationDate().toString().split("\\.")[0]);
    }

    /**
     * Adds teams to the database, then attempts to retrieves them by id.
     * Checks if the correct team is extracted.
     *
     * @throws Exception if team cant be found
     */
    @Test
    void getTeamByIdTest() throws Exception {
        Team team =
            teamService.createTeamInDatabase("Swan", "Basketball",
                "Christchurch", "NotJapan",
                "default_team_image.png");
        Team teamResult = teamService.getTeamById(team.getId());
        assertEquals("Swan", teamResult.getName());
        assertEquals("Basketball", teamResult.getSport());
        assertEquals("Christchurch", teamResult.getLocation().getCity());
    }

    /**
     * Attempts to update an existing team in the database.
     * Checks if the teams details have been updated.
     *
     * @throws Exception if team cant be found
     */
    @Test
    void updateTeamInDatabaseTest() throws Exception {
        Team team =
            teamService.createTeamInDatabase("Swan", "Basketball",
                "Christchurch", "NotJapan",
                "default_team_image.png");
        teamService.updateTeamInDatabase(Long.toString(team.getId()),
            "Sam", "Football", "Auckland",
            "NotJapan");
        Team team2 = teamService.getTeamById(team.getId());
        assertEquals("Sam", team2.getName());
        assertEquals("Football", team2.getSport());
        assertEquals("Auckland", team2.getLocation().getCity());
    }

    @Test
    void updateProfilePictureInDatabaseTest() throws Exception {
        Team team =
            teamService.createTeamInDatabase("Swan", "Basketball",
                "Christchurch", "NotJapan",
                "default_team_image.png");
        teamService.updateProfilePictureInDatabase(Long.toString(team.getId()),
            "download.png");
        Team team2 = teamService.getTeamById(team.getId());
        assertEquals("download.png", team2.getImage());
    }

    /**
     * Tests that a person can register to a team and can extract their details through the team
     */
    @Test
    void registerPersonTest() {
        Team team =
            teamService.createTeamInDatabase("Swan", "Basketball",
                "Christchurch", "NotJapan",
                "default_team_image.png");
        TeamRoles teamRole =
            teamRoleService.tabUserCreatesTeam(team, tabUserService.getCurrentlyLoggedIn());
        List<TabUser> result = tabUserService.getUsers(teamRole.getTeam().getId());
        assertEquals("test", result.get(0).getFirstName());
        assertEquals("person", result.get(0).getLastName());
    }

    /**
     * Tests that a person can leave a team they are currently in
     */
    @Test
    void removePersonTest() {
        TabUser user = tabUserService.getCurrentlyLoggedIn();
        Team team =
            teamService.createTeamInDatabase("Swan", "Basketball",
                "Christchurch", "NotJapan",
                "default_team_image.png");
        Team teamUserAdded = teamService.registerTabUserToTeam(team, user);
        String result = teamRoleService.getUserRole(team.getId(), user.getId());
        Assertions.assertEquals("Member", result);
        teamService.removeTabUserFromTeam(teamUserAdded, user);
        String removed = teamRoleService.getUserRole(team.getId(), user.getId());
        Assertions.assertEquals("", removed);
    }


    /**
     * Attempts to get a team by id, however a invalid id is passed through.
     * Expected to throw an exception
     */
    @Test
    void getTeamByIdTest_InvalidId() {
        try {
            teamService.getTeamById(10);
        } catch (Exception e) {
            Assertions.assertNotNull(e);
        }
    }

    /**
     * Test for .searchUserTeams() using invalid ID
     */
    @Test
    void searchTeamsByUserIdTest_InvalidId() {
        Page<Team> teams = teamService.searchUserTeams("", 999, 1);
        assertTrue(teams.getContent().isEmpty());
    }

    /**
     * Test for .searchUserTeams() using valid ID
     */
    @Test
    void searchTeamsByUserIdTestValidId() {
        List<Team> addedTeams = new ArrayList<>();
        addedTeams.add(
            teamService.createTeamInDatabase("Swan", "Basketball",
                "Christchurch", "NotJapan",
                "default_team_image.png"));
        addedTeams.add(
            teamService.createTeamInDatabase("Swan", "Basketball",
                "Christchurch", "NotJapan",
                "default_team_image.png"));
        addedTeams.add(
            teamService.createTeamInDatabase("Swan", "Basketball",
                "Christchurch", "NotJapan",
                "default_team_image.png"));
        addedTeams.add(
            teamService.createTeamInDatabase("Swan", "Basketball",
                "Christchurch", "NotJapan",
                "default_team_image.png"));
        teamService.createTeamInDatabase("Swan", "Basketball",
            "Istanbul", "IsCool",
            "default_team_image.png");
        teamService.createTeamInDatabase("Swan", "Basketball",
            "Istanbul", "IsCool",
            "default_team_image.png");
        Page<Team> teams =
            teamService.searchUserTeams("",
                tabUserService.getCurrentlyLoggedIn().getId(), 1);

        assertNotEquals(teams.getContent(), addedTeams);
    }

    /**
     * Test for .searchteams() with empty search query, ordered by name, location
     */
    @Test
    void searchAllTeamsBlankSearchOrderedNameOrderedLocation() {
        List<Team> returnedTeams = teamService.searchTeams("",
            1).getContent();
        int initialSize = teamService.getTeamResults().size();
        int size = Math.min((initialSize), 10);
        assertEquals(size, returnedTeams.size());
    }

    /**
     * Test for .searchTeams() with blank search query, ordered by location
     */
    @Test
    void searchAllTeamsBlankSearchUnorderedNameOrderedLocation() {
        List<Team> addedTeams = new ArrayList<>();
        addedTeams.add(
            teamService.createTeamInDatabase("B", "Basketball",
                "Christchurch", "NotJapan",
                "default_team_image.png"));
        addedTeams.add(
            teamService.createTeamInDatabase("C", "Basketball",
                "Christchurch", "NotJapan",
                "default_team_image.png"));
        addedTeams.add(
            teamService.createTeamInDatabase("A", "Basketball",
                "Christchurch", "NotJapan",
                "default_team_image.png"));
        addedTeams.add(
            teamService.createTeamInDatabase("D", "Basketball",
                "Christchurch", "NotJapan",
                "default_team_image.png"));
        addedTeams.sort(Comparator.comparing(Team::getName));
        List<Team> returnedTeams = teamService.searchTeams("",
            1).getContent();
        int initialSize = teamService.getTeamResults().size();
        int size;
        size = Math.min((initialSize), 10);
        assertEquals(size, returnedTeams.size());
    }

    /**
     * Test for .searchTeams() with blank search query without ordering
     */
    @Test
    void searchAllTeamsBlankSearchUnorderedNameUnorderedLocation() {
        List<Team> addedTeams = new ArrayList<>();
        addedTeams.add(teamService.createTeamInDatabase("D", "Basketball",
            "A City", "NotJapan",
            "default_team_image.png"));
        addedTeams.add(teamService.createTeamInDatabase("C", "Basketball",
            "A City", "NotJapan",
            "default_team_image.png"));
        addedTeams.add(teamService.createTeamInDatabase("B", "Basketball",
            "B City", "NotJapan",
            "default_team_image.png"));
        addedTeams.add(teamService.createTeamInDatabase("A", "Basketball",
            "B City", "NotJapan",
            "default_team_image.png"));
        addedTeams.sort(Comparator.comparing(Team::getLocationId).thenComparing(Team::getName));
        List<Team> returnedTeams = teamService.searchTeams("", 1)
            .getContent();
        int initialSize = teamService.getTeamResults().size();
        int size;
        size = Math.min((initialSize), 10);
        assertEquals(size, returnedTeams.size());

        //        for (int i = 0; i < addedTeams.size(); i++) {
        //            assertEquals(addedTeams.get(i).getId(),
        //                returnedTeams.get(addedTeams.size() - 1 - i).getId());
        //            assertEquals(addedTeams.get(i).getName(),
        //                returnedTeams.get(addedTeams.size() - 1 - i).getName());
        //            assertEquals(addedTeams.get(i).getSport(),
        //                returnedTeams.get(addedTeams.size() - 1 - i).getSport());
        //            assertEquals(addedTeams.get(i).getImage(),
        //                returnedTeams.get(addedTeams.size() - 1 - i).getImage());
        //            assertEquals(addedTeams.get(i).getLocationID(),
        //                returnedTeams.get(addedTeams.size() - 1 - i).getLocationID());
        //        }
    }

    /**
     * Tests if new token is added to team entity
     */
    @Test
    void givenNoTokenSet_WhenNewTokenAdded_ThenCurrentTokenIsNewToken() {
        Team team =
            teamService.createTeamInDatabase("Swan", "Basketball",
                "Christchurch", "NotJapan",
                "default_team_image.png");
        String token = teamService.changeToken(team.getId());
        assertEquals(token, teamService.getCurrentToken(team.getId()));
    }

    /**
     * Check if token is not pre-made
     */
    @Test
    void givenNoTokenSet_WhenNoTokenSet_ThenTokenIsNull() {
        Team team =
            teamService.createTeamInDatabase("Swan", "Basketball",
                "Christchurch", "NotJapan",
                "default_team_image.png");
        assertNull(teamService.getCurrentToken(team.getId()));
    }

    /**
     * Check if new token is added to Token Dump Table
     */
    @Test
    void givenNoTokenSet_WhenNewTokenAdded_ThenNewTokenIsAddedToDump() {
        Team team =
            teamService.createTeamInDatabase("Swan", "Basketball",
                "Christchurch", "NotJapan",
                "default_team_image.png");
        teamService.changeToken(team.getId());
        assertTrue(tokenDumpService.checkIfTokenExists(teamService
            .getCurrentToken(team.getId())));
    }

    /**
     * Check if new token is added and check again when new token is added
     */
    @Test
    void givenTokenSet_WhenNewTokenAdded_ThenCurrentTokenIsNewToken() {
        Team team =
            teamService.createTeamInDatabase("Swan", "Basketball",
                "Christchurch", "NotJapan",
                "default_team_image.png");
        String token1 = teamService.changeToken(team.getId());
        assertEquals(token1, teamService.getCurrentToken(team.getId()));
        teamService.resetRegenTime(team.getId());
        String token2 = teamService.changeToken(team.getId());
        assertEquals(token2, teamService.getCurrentToken(team.getId()));
    }

    /**
     * Check if new token is added and check again when new token is added
     */
    @Test
    void givenTokenSet_WhenNewTokenAddedBeforeTimeExpired_ThenCurrentTokenIsFirstToken() {
        Team team =
            teamService.createTeamInDatabase("Swan", "Basketball",
                "Christchurch", "NotJapan",
                "default_team_image.png");
        String token1 = teamService.changeToken(team.getId());
        assertEquals(token1, teamService.getCurrentToken(team.getId()));
        String token2 = teamService.changeToken(team.getId());
        assertNotEquals(token2, teamService.getCurrentToken(team.getId()));
        assertEquals(token1, teamService.getCurrentToken(team.getId()));
    }

    /**
     * Check if both tokens are in the token dump table
     */
    @Test
    void givenTokenSet_WhenNewTokenAdded_ThenBothTokensInTokenDump() {
        Team team =
            teamService.createTeamInDatabase("Swan", "Basketball",
                "Christchurch", "NotJapan",
                "default_team_image.png");
        String token1 = teamService.changeToken(team.getId());
        assertTrue(tokenDumpService.checkIfTokenExists(token1));
        teamService.resetRegenTime(team.getId());
        String token2 = teamService.changeToken(team.getId());
        assertTrue(tokenDumpService.checkIfTokenExists(token2));

    }

    /**
     * Check if both tokens are in the token dump table
     */
    @Test
    void givenTokenSet_WhenNewTokenAddedBeforeExpiry_ThenFirstTokenInTokenDumpNotSecond() {
        Team team =
            teamService.createTeamInDatabase("Swan", "Basketball",
                "Christchurch", "NotJapan",
                "default_team_image.png");
        String token1 = teamService.changeToken(team.getId());
        assertTrue(tokenDumpService.checkIfTokenExists(token1));
        String token2 = teamService.changeToken(team.getId());
        assertFalse(tokenDumpService.checkIfTokenExists(token2));

    }

    /**
     * Check if all tokens are in the token dump
     */
    @Test
    void given2TokensSet_WhenNewTokenAdded_ThenAllTokensAreInDump() {
        Team team =
            teamService.createTeamInDatabase("Swan", "Basketball",
                "Christchurch", "NotJapan",
                "default_team_image.png");
        String token1 = teamService.changeToken(team.getId());
        teamService.resetRegenTime(team.getId());
        String token2 = teamService.changeToken(team.getId());
        teamService.resetRegenTime(team.getId());
        String token3 = teamService.changeToken(team.getId());
        assertTrue(tokenDumpService.checkIfTokenExists(token1));
        assertTrue(tokenDumpService.checkIfTokenExists(token2));
        assertTrue(tokenDumpService.checkIfTokenExists(token3));

    }

    /**
     * Check if last token is added correctly to team table
     */
    @Test
    void given2TokensSet_WhenNewTokenAdded_ThenCurrentTokenIsThirdToken() {
        Team team =
            teamService.createTeamInDatabase("Swan", "Basketball",
                "Christchurch", "NotJapan",
                "default_team_image.png");
        String token1 = teamService.changeToken(team.getId());
        assertEquals(token1, teamService.getCurrentToken(team.getId()));
        teamService.resetRegenTime(team.getId());
        String token2 = teamService.changeToken(team.getId());
        assertEquals(token2, teamService.getCurrentToken(team.getId()));
        teamService.resetRegenTime(team.getId());
        String token3 = teamService.changeToken(team.getId());
        assertEquals(token3, teamService.getCurrentToken(team.getId()));
    }

    /**
     * Checks that if a new user with no teams, inputs an invalid team invitation token,
     * that the user is not added to a team, and an error is thrown along with
     * an appropriate message
     */
    @Test
    void givenInvalidToken_whenRegisterUserToTeamViaToken_thenUserNotAddedToTeam_AndErrorThrown() {
        //Creates a new team using the user defined in setUP
        Team team =
            teamService.createTeamInDatabase("OnlyTeam", "Basketball",
                "Christchurch", "NotJapan",
                "default_team_image.png");
        TabUser user = tabUserService.createFullTabUserInDatabase(Arrays.asList("test",
            "person"), new Location("Christchurch",
            "New Zealand"), "email1@email.com",
            new Date(2002 + 1900, 11, 31).toString(), "*****", "image.jpg",
            "Baseball");
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
        teamService.changeToken(team.getId());
        Executable executable = () -> teamService.registerTabUserToTeam("", user);
        Exception exception = Assertions.assertThrows(Exception.class, executable);
        assertEquals("Team not found", exception.getMessage());
        assertEquals(0,
            teamService.searchUserTeams("", tabUserService
                    .getCurrentlyLoggedIn().getId(), 1)
                .getTotalElements());
    }

    /**
     * Checks that if a new user with no teams, inputs an invalid team invitation token,
     * that the user is not added to a team, and an error is thrown along with
     * an appropriate message
     */
    @Test
    void givenValidToken_whenRegisterUserToTeamViaToken_thenUserAddedToTeam() throws Exception {
        //Creates a new team using the user defined in setUP
        Team team =
            teamService.createTeamInDatabase("OnlyTeam", "Basketball",
                "Christchurch", "NotJapan",
                "default_team_image.png");

        String invitationToken = teamService.changeToken(team.getId());
        teamService.registerTabUserToTeam(invitationToken, tabUserService.getCurrentlyLoggedIn());
        assertEquals(1,
            teamService.searchUserTeams("", tabUserService.getCurrentlyLoggedIn()
                    .getId(), 1)
                .getTotalElements());
        assertEquals(teamService.getTeamById(team.getId()),
            teamService.searchUserTeams("", tabUserService.getCurrentlyLoggedIn()
                    .getId(), 1)
                .getContent().get(0));
    }

    /**
     * Checks if win occurs then wins column is updated
     *
     * @throws Exception If database can not find team
     */
    @Test
    void givenValidTeam_whenWinIsAdded_thenTheWinColumnIsUpdated() throws Exception {
        Team team =
            teamService.createTeamInDatabase("OnlyTeam", "Basketball",
                "Christchurch", "NotJapan",
                "default_team_image.png");
        int win = 0;
        String returned = teamService.addToWinLossDrawColumn(team.getId(), win);
        assertEquals("Win column is updated", returned);
        assertEquals(1, team.getWins());
        assertEquals(0, team.getDraws());
        assertEquals(0, team.getLosses());
    }

    /**
     * Checks if draw occurs then draws column is updated
     *
     * @throws Exception If database can not find team
     */
    @Test
    void givenValidTeam_whenDrawIsAdded_thenTheDrawColumnIsUpdated() throws Exception {
        Team team =
            teamService.createTeamInDatabase("OnlyTeam", "Basketball",
                "Christchurch", "NotJapan",
                "default_team_image.png");
        int draw = 1;
        String returned = teamService.addToWinLossDrawColumn(team.getId(), draw);
        assertEquals("Draw column is updated", returned);
        assertEquals(0, team.getWins());
        assertEquals(1, team.getDraws());
        assertEquals(0, team.getLosses());
    }

    /**
     * Checks if loss occurs then losses column is updated
     *
     * @throws Exception If database can not find team
     */
    @Test
    void givenValidTeam_whenLossIsAdded_thenTheLossColumnIsUpdated() throws Exception {
        Team team =
            teamService.createTeamInDatabase("OnlyTeam", "Basketball",
                "Christchurch", "NotJapan",
                "default_team_image.png");
        int loss = 2;
        String returned = teamService.addToWinLossDrawColumn(team.getId(), loss);
        assertEquals("Loss column is updated", returned);
        assertEquals(0, team.getWins());
        assertEquals(0, team.getDraws());
        assertEquals(1, team.getLosses());
    }

    /**
     * Checks if win occurs twice then wins column is updated to 2
     *
     * @throws Exception If database can not find team
     */
    @Test
    void givenValidTeam_whenTwoWinsIsAdded_thenTheWinColumnIsUpdated() throws Exception {
        Team team =
            teamService.createTeamInDatabase("OnlyTeam", "Basketball",
                "Christchurch", "NotJapan",
                "default_team_image.png");
        int win = 0;
        String returned = teamService.addToWinLossDrawColumn(team.getId(), win);
        assertEquals("Win column is updated", returned);
        assertEquals(1, team.getWins());
        assertEquals(0, team.getDraws());
        assertEquals(0, team.getLosses());
        returned = teamService.addToWinLossDrawColumn(team.getId(), win);
        assertEquals("Win column is updated", returned);
        assertEquals(2, team.getWins());
        assertEquals(0, team.getDraws());
        assertEquals(0, team.getLosses());
    }

    /**
     * Checks if win occurs and draw occurs then wins and draw column is updated
     *
     * @throws Exception If database can not find team
     */
    @Test
    void givenValidTeam_whenWinAndDrawIsAdded_thenTheWinAndDrawColumnIsUpdated() throws Exception {
        Team team =
            teamService.createTeamInDatabase("OnlyTeam", "Basketball",
                "Christchurch", "NotJapan",
                "default_team_image.png");
        int win = 0;
        String returned = teamService.addToWinLossDrawColumn(team.getId(), win);
        assertEquals("Win column is updated", returned);
        assertEquals(1, team.getWins());
        assertEquals(0, team.getDraws());
        assertEquals(0, team.getLosses());
        int draw = 1;
        returned = teamService.addToWinLossDrawColumn(team.getId(), draw);
        assertEquals("Draw column is updated", returned);
        assertEquals(1, team.getWins());
        assertEquals(1, team.getDraws());
        assertEquals(0, team.getLosses());
    }

    /**
     * Checks if draw occurs and loss occurs then draw and loss column is updated
     *
     * @throws Exception If database can not find team
     */
    @Test
    void givenValidTeam_whenDrawAndLossIsAdded_thenTheDrawAndLossColumnIsUpdated()
        throws Exception {
        Team team =
            teamService.createTeamInDatabase("OnlyTeam", "Basketball",
                "Christchurch", "NotJapan",
                "default_team_image.png");
        final int draw = 1;
        final int loss = 2;
        String returned = teamService.addToWinLossDrawColumn(team.getId(), draw);
        assertEquals("Draw column is updated", returned);
        assertEquals(0, team.getWins());
        assertEquals(1, team.getDraws());
        assertEquals(0, team.getLosses());
        returned = teamService.addToWinLossDrawColumn(team.getId(), loss);
        assertEquals("Loss column is updated", returned);
        assertEquals(0, team.getWins());
        assertEquals(1, team.getDraws());
        assertEquals(1, team.getLosses());
    }

    /**
     * Checks if win occurs and loss occurs then win and loss column is updated
     *
     * @throws Exception If database can not find team
     */
    @Test
    void givenValidTeam_whenWinAndLossIsAdded_thenTheDrawAndLossColumnIsUpdated() throws Exception {
        Team team =
            teamService.createTeamInDatabase("OnlyTeam", "Basketball",
                "Christchurch", "NotJapan",
                "default_team_image.png");
        int win = 0;
        String returned = teamService.addToWinLossDrawColumn(team.getId(), win);
        assertEquals("Win column is updated", returned);
        assertEquals(1, team.getWins());
        assertEquals(0, team.getDraws());
        assertEquals(0, team.getLosses());
        int loss = 2;
        returned = teamService.addToWinLossDrawColumn(team.getId(), loss);
        assertEquals("Loss column is updated", returned);
        assertEquals(1, team.getWins());
        assertEquals(0, team.getDraws());
        assertEquals(1, team.getLosses());
    }

    /**
     * Checks if nothing occurs then none of the win, loss, or draw column is updated
     * GivenValidTeam_whenInvalidIntegerGiven_thenTheWinDrawAndLossColumn
     * IsNotUpdatedAndReturnedErrorMessage
     */
    @Test
    void givenValidTeam_whenInvalidIntegerGiven_thenReturnedErrorMessage()
        throws Exception {
        Team team =
            teamService.createTeamInDatabase("OnlyTeam", "Basketball",
                "Christchurch", "NotJapan",
                "default_team_image.png");
        int random = 4917432;
        String returned = teamService.addToWinLossDrawColumn(team.getId(), random);
        assertEquals("Not a valid integer", returned);
        assertEquals(0, team.getWins());
        assertEquals(0, team.getDraws());
        assertEquals(0, team.getLosses());
    }
}