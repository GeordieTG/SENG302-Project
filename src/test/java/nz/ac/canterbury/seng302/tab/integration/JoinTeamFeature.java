package nz.ac.canterbury.seng302.tab.integration;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.sql.Date;
import java.util.Arrays;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.TeamRoleService;
import nz.ac.canterbury.seng302.tab.service.TeamService;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Test for Joining a Team using E2E testing
 */
@SpringBootTest
public class JoinTeamFeature {

    @Autowired
    TabUserService tabUserService;

    @Autowired
    TeamService teamService;

    @Autowired
    TeamRoleService teamRoleService;

    @Autowired
    AuthenticationManager authenticationManager;

    TabUser user;

    Team team;

    String inviteToken;

    @Given("I'm logged in")
    public void im_logged_in() {
        if (!tabUserService.getByEmail("aragorn@strider.com")) {
            user = tabUserService.createFullTabUserInDatabase(Arrays.asList("Aragorn", "Strider"),
                    new Location("Seoul", "South Korea"), "aragorn@strider.com",
                new Date(2002 + 1900, 11, 31).toString(), "*****",
                "default.jpg", "Striding");

            user.grantAuthority("ROLE_USER");
            UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(user.getEmail(), "*****",
                    user.getAuthorities());
            // Authenticate the token properly with the CustomAuthenticationProvider
            Authentication authentication = authenticationManager.authenticate(token);
            // Check if the authentication is actually authenticated (in this example any
            // username/password is accepted so this should never be false)
            if (authentication.isAuthenticated()) {

                // Add the authentication to the current security context (Stateful)
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            Assertions.assertNotNull(tabUserService.getCurrentlyLoggedIn());
        }
    }

    @Given("I have a team called {string}")
    public void have_a_team_called(String teamName) {
        team = teamService.createTeamInDatabase(teamName, "Basketball", "Greymouth", "New Zealand",
            "default.jpg");
        Assertions.assertNotNull(team);
    }

    @When("I go to join a team with my own teams token")
    public void go_to_join_a_team_with_my_own_teams_token() throws Exception {
        teamService.registerTabUserToTeam(inviteToken, user);
    }

    @Then("My role in my team does not change")
    public void my_role_in_my_team_does_not_change() {
        Assertions.assertEquals("Manager", teamRoleService.getUserRole(team.getId(), user.getId()));
    }

    @Given("I generate an invite token for my team")
    public void generate_an_invite_token_for_my_team() {
        inviteToken = teamService.changeToken(team.getId());
        Assertions.assertNotNull(inviteToken);

    }
}
