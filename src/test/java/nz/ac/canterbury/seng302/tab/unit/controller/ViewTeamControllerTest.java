package nz.ac.canterbury.seng302.tab.unit.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Date;
import java.util.Arrays;
import java.util.Map;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Test class for ViewTeamController
 */
@SpringBootTest
@AutoConfigureMockMvc
class ViewTeamControllerTest {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TeamService teamService;
    @Autowired
    private TabUserService tabUserService;
    @Autowired
    private WebApplicationContext webApp;

    /**
     * Set up before each test
     */
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApp).build();
        if (!tabUserService.getByEmail("email@email.com")) {
            TabUser user =
                tabUserService.createFullTabUserInDatabase(Arrays.asList("test",
                    "person"), new Location("Christchurch", "New Zealand"),
                    "email@email.com",
                    new Date(2002 + 1900, 11, 31).toString(), "*****",
                    "image.jpg", "Baseball");
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
    }


    /**
     * Test that the join team handler correctly accepts a valid token
     *
     * @throws Exception throws exception if team not found
     */
    @Test
    @WithMockUser(username = "email@email.com", password = "*****", authorities = "USER")
    void givenValidToken_whenRegisterUserToTeamViaToken_thenRedirectedToTeamPage()
        throws Exception {
        Team team = teamService.createTeamInDatabaseNoRegister("Bishops",
            "Basketball", "Greymouth",
            "NotJapan", "default_team_image.png");
        String invitationToken = teamService.changeToken(team.getId());
        String viewName = mockMvc.perform(post("/joinTeam")
                .contentType("text/html; charset=UTF-8")
                .param("invitation-token", invitationToken))
            .andExpect(status().is(302))
            .andExpect(redirectedUrl("./viewTeams?&showAllTeams=0&validToken=true"))
            .andReturn()
            .getModelAndView().getViewName();
    }

    /**
     * Test that the join team handler correctly denys a invitation token which is malformed
     *
     * @throws Exception throws exception if team not found
     */
    @Test
    @WithMockUser(username = "email@email.com", password = "*****", authorities = "USER")
    void givenMalformedToken_whenRegisterTeamViaToken_thenRedirToCurrentPage_andErrorShown()
        throws Exception {
        Team team = teamService.createTeamInDatabaseNoRegister("Bishops",
            "Basketball", "Greymouth",
            "NotJapan", "default_team_image.png");
        String invitationToken = teamService.changeToken(team.getId());
        Map model = mockMvc.perform(post("/joinTeam")
                .contentType("text/html; charset=UTF-8")
                .param("invitation-token", "")
                .param("current-page", "/profilePage"))
            .andExpect(status().is(302)).andExpect(redirectedUrl(
                "./viewTeams?&showAllTeams=0&validToken=false"
                    + "&tokenErrorText=Please+enter+a+valid+token"))
            .andReturn().getModelAndView().getModel();

    }

    /**
     * Test that the join team handler correctly denys a invitation token which is invalid
     * (doesn't correspond to a team)
     *
     * @throws Exception throws exception if team not found
     */
    @Test
    @WithMockUser(username = "email@email.com", password = "*****", authorities = "USER")
    void givenInvalidtoken_whenRegisterToTeamViaToken_thenRedirToCurrentPage_andErrorShown()
        throws Exception {
        Team team = teamService.createTeamInDatabaseNoRegister("Bishops", "Basketball", "Greymouth",
            "NotJapan", "default_team_image.png");
        Map model = mockMvc.perform(post("/joinTeam")
                .contentType("text/html; charset=UTF-8")
                .param("invitation-token", "A8D5965f059F")
                .param("current-page", "/profilePage"))
            .andExpect(status().is(302)).andExpect(redirectedUrl(
                "./viewTeams?&showAllTeams=0&validToken=true"
                    + "&tokenErrorText=The+Invite+is+invalid"))
            .andReturn().getModelAndView().getModel();

    }
}
