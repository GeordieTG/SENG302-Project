package nz.ac.canterbury.seng302.tab.integration;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javassist.NotFoundException;
import nz.ac.canterbury.seng302.tab.entity.Club;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.entity.datatransferobjects.ClubDto;
import nz.ac.canterbury.seng302.tab.entity.enums.SupportedSports;
import nz.ac.canterbury.seng302.tab.service.ClubService;
import nz.ac.canterbury.seng302.tab.service.LocationService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.TeamService;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

/**
 * Integration Test for viewing a club
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ViewClubFeature {
    @Autowired
    TabUserService tabUserService;
    Team team;
    Team team1;
    Club club;
    MvcResult result;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private LocationService locationService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private ClubService clubService;

    @Given("I am on the team search page")
    public void i_am_on_the_team_search_page() throws Exception {
        result = mockMvc.perform(get("/viewTeams?showAllTeams=0").with(
                    user(IntegrationTestConfigurations.loggedInUser))
                .contentType("text/html; charset=UTF-8"))
            .andExpect(status().isOk()).andReturn();
        Assertions.assertTrue(result.getResponse().getContentAsString().contains("Join Team"));
    }

    @When("I search {string}")
    public void i_search(String searchString) throws Exception {
        result = mockMvc.perform(get("/viewTeams?currentPage=1&searchQuery=" + searchString
                + "&sportQuery=none&cityQuery=none&showAllTeams=0")
                .with(user(IntegrationTestConfigurations.loggedInUser))
                .contentType("text/html; charset=UTF-8"))
            .andExpect(status().isOk()).andReturn();
        Assertions.assertTrue(result.getResponse()
            .getContentAsString().contains("Liam&#39;s Club"));
        Assertions.assertTrue(result.getResponse()
            .getContentAsString().contains("Liam&#39;s Team"));

    }

    @Then("I see {int} results")
    public void i_see_results(int resultsSize) throws UnsupportedEncodingException {
        Assertions.assertTrue(result.getResponse()
            .getContentAsString().contains("Total Items " + resultsSize));
    }

    @Given("I have created a new club or viewing an existing club,")
    public void i_have_created_a_new_club_or_viewing_an_existing_club() throws NotFoundException {
        Location location =
            locationService.createLocationInDatabase("Nelson", "New Zealand");
        team1 = new Team("TeamWithClub", "Football", location, "null");
        club = new Club("Club", SupportedSports.Football,
            location, tabUserService.getCurrentlyLoggedIn(), "null");
        team1 = teamService.save(team1);
        List<Long> teamId = new ArrayList<>();
        teamId.add(team1.getId());
        ClubDto clubDto = new ClubDto(club, teamId);
        club = clubService.saveClubDto(clubDto, tabUserService.getCurrentlyLoggedIn());
        team = teamService.createTeamInDatabase("TeamWithoutClub", "Football",
            "Nelson", "New Zealand", "null");

    }

    @When("I am on the club's profile page,")
    public void i_am_on_the_club_s_profile_page() throws Exception {

        result = mockMvc.perform(get("/viewClub?id=" + club.getId()).with(csrf())
                .contentType("text/html; charset=UTF-8"))
            .andExpect(status().isOk()).andReturn();
    }

    @Then("I see the owner of the club")
    public void i_see_the_owner_of_the_club() throws UnsupportedEncodingException {
        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertTrue(responseBody.contains(
            club.getOwner().getFirstName() + " " + club.getOwner().getLastName()));
        Assertions.assertTrue(responseBody.contains(club.getOwner().getProfilePicture()));
    }

    @Then("I can see a list of team that are part of the club.")
    public void i_can_see_a_list_of_team_that_are_part_of_the_club()
        throws UnsupportedEncodingException {
        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertTrue(responseBody.contains(team1.getName()));
        Assertions.assertFalse(responseBody.contains(team.getName()));
    }


    @Then("I can see the club's name, club logo or profile picture, location, and sports")
    public void i_can_see_the_club_s_name_club_logo_profile_picture_location_and_sports()
        throws UnsupportedEncodingException {
        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertTrue(responseBody.contains(club.getName()));
        Assertions.assertTrue(responseBody.contains(club.getLocation().getCity()));
        Assertions.assertTrue(responseBody.contains(club.getLocation().getCountry()));
        Assertions.assertTrue(responseBody.contains(club.getSport().name()));
    }
}
