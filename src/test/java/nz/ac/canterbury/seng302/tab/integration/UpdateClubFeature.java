package nz.ac.canterbury.seng302.tab.integration;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javassist.NotFoundException;
import nz.ac.canterbury.seng302.tab.controller.rest.ClubRestController;
import nz.ac.canterbury.seng302.tab.entity.Club;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.entity.datatransferobjects.ClubDto;
import nz.ac.canterbury.seng302.tab.entity.enums.SupportedSports;
import nz.ac.canterbury.seng302.tab.service.ClubService;
import nz.ac.canterbury.seng302.tab.service.LocationService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.TeamService;
import nz.ac.canterbury.seng302.tab.unit.UnitCommonTestSetup;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

/**
 * Update club
 */

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UpdateClubFeature {

    @Autowired
    TabUserService tabUserService;

    @Autowired
    TeamService teamService;

    @Autowired
    ClubService clubService;

    @Autowired
    LocationService locationService;

    @Autowired
    ClubRestController clubRestController;
    MvcResult result;
    Team teamWithClub;
    Club club;
    Team team;
    ClubDto clubDto;
    Location location;
    @Autowired
    private MockMvc mockMvc;

    @And("I have a team with a club")
    public void i_have_a_team_with_a_club() throws NotFoundException {

        teamWithClub = teamService.createTeamInDatabase(
            "Knights",
            "BASKETBALL",
            "Christchurch",
            "New Zealand",
            "image.jpg");

        List<Long> teamList = new ArrayList<>();
        teamList.add(teamWithClub.getId());


        location = new Location("Wellington", "Japan");
        locationService.createLocationInDatabase(location);
        club = new Club("Chelsea", SupportedSports.Basketball, location,
            tabUserService.getCurrentlyLoggedIn(), "");
        clubDto = new ClubDto(club, teamList);

        clubService.saveClubDto(clubDto, tabUserService.getCurrentlyLoggedIn());
        clubRestController.createClub(clubDto, null);
    }

    @And("There is a team with a club")
    public void there_is_have_a_team_with_a_club() throws NotFoundException {

        teamWithClub = teamService.createTeamInDatabase(
                "Knights",
                "BASKETBALL",
                "Christchurch",
                "New Zealand",
                "image.jpg");

        List<Long> teamList = new ArrayList<>();
        teamList.add(teamWithClub.getId());

        TabUser testManager = UnitCommonTestSetup.createTestUser();
        location = new Location("Wellington", "Japan");
        locationService.createLocationInDatabase(location);
        club = new Club("Chelsea", SupportedSports.Basketball, location,
                testManager, "");
        clubDto = new ClubDto(club, teamList);

        clubService.saveClubDto(clubDto, testManager);
        clubRestController.createClub(clubDto, null);
    }

    @When("I want to edit the club")
    public void i_want_to_edit_the_club() throws Exception {

        result = mockMvc.perform(get("/editClub?id=" + club.getId()).with(
                    user(tabUserService.getCurrentlyLoggedIn().getEmail()).password(
                        tabUserService.getCurrentlyLoggedIn()
                            .getPassword()).roles("USER"))
                .contentType("text/html; charset=UTF-8"))
            .andExpect(status().isOk()).andReturn();
    }

    @Then("I can see the field prepopulated")
    public void i_can_see_the_fields_prepopulated() throws UnsupportedEncodingException {
        Assertions.assertTrue(result.getResponse().getContentAsString().contains("Chelsea"));
        Assertions.assertTrue(result.getResponse().getContentAsString().contains("Basketball"));
        Assertions.assertTrue(result.getResponse().getContentAsString().contains("Wellington"));
        Assertions.assertTrue(result.getResponse().getContentAsString().contains("Japan"));
        Assertions.assertTrue(result.getResponse().getContentAsString().contains("Knights"));
    }

    @Then("I cannot edit the club")
    public void i_cannot_edit_the_club() throws Exception {
        result = mockMvc.perform(get("/editClub?id=" + club.getId()).with(
                                user(tabUserService.getCurrentlyLoggedIn().getEmail()).password(
                                        tabUserService.getCurrentlyLoggedIn()
                                                .getPassword()).roles("USER"))
                        .contentType("text/html; charset=UTF-8"))
                .andExpect(status().is3xxRedirection()).andReturn();
    }

    @And("I have a club")
    public void i_have_a_club() {
        club = new Club("Chelsea", SupportedSports.Basketball, location,
            tabUserService.getCurrentlyLoggedIn(), "");
        clubService.saveClub(club);
    }

    @And("I have a team for a club")
    public void i_have_a_team() {
        team = teamService.createTeamInDatabase("Test", "Test", "Test", "Test", "Test");
    }

    @When("I add to the clubs teams")
    public void i_modify_the_clubs_teams() {
        List<Long> teamList = new ArrayList<>();
        teamList.add(team.getId());
        clubService.updateClubTeams(club, teamList);
    }

    @Then("the clubs teams are updated")
    public void the_club_teams_are_updated() {
        Club retrievedClub = clubService.getById(club.getId());
        Assertions.assertEquals(1, retrievedClub.getTeams().size());
    }

    @When("I remove from the clubs teams")
    public void i_remove_from_the_clubs_teams() {
        List<Long> teamList = new ArrayList<>();
        clubService.updateClubTeams(club, teamList);
    }

    @Then("the clubs teams are updated to have no teams")
    public void the_clubs_teams_are_updated_to_have_no_teams() {
        Club retrievedClub = clubService.getById(club.getId());
        Assertions.assertEquals(0, retrievedClub.getTeams().size());
    }

    @Then("I am unable to change the sport of the club")
    public void am_unable_to_change_the_sport_of_the_club() throws UnsupportedEncodingException {
        String html = result.getResponse().getContentAsString();
        // only one disabled in the entire html page if true
        Assertions.assertTrue(result.getResponse().getContentAsString().contains("disabled"));
    }
}
