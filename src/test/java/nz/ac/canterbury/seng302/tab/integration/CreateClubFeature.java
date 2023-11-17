package nz.ac.canterbury.seng302.tab.integration;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
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
import nz.ac.canterbury.seng302.tab.service.ValidationService;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

/**
 * Test for Joining a Team using E2E testing
 */
@AutoConfigureMockMvc
@SpringBootTest
public class CreateClubFeature {

    @Autowired
    ClubService clubService;

    @Autowired
    LocationService locationService;
    @Autowired
    TabUserService tabUserService;

    @Autowired
    ValidationService validationService;
    @Autowired
    TeamService teamService;
    MvcResult result;
    JSONObject clubBody;
    Long clubId;
    @Autowired
    private MockMvc mockMvc;

    @When("The club is created")
    public void the_club_is_created() throws Exception {
        String locationBody = "{\"line1\": \"address1\", \"line2\": \"address2\", \"country\": "
            + "\"country\", \"city\": \"city\", \"suburb\": \"suburb\", \"postcode\": "
            + "\"postcode\"}";
        String clubBody = "{\"club\":{\"name\":\"TestClubName\", \"sport\" : \"Futsal\", "
            + "\"location\" : " + locationBody + "}, \"teamIds\" : [1]}";


        MvcResult clubPostResult = mockMvc.perform(post("/create-club")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON).content(clubBody)).andReturn();
        MockHttpServletResponse clubPostResponse = clubPostResult.getResponse();
        JSONObject returnedClubBody = new JSONObject(clubPostResponse.getContentAsString());
        clubId = Long.parseLong(returnedClubBody.get("id").toString());
    }

    @Then("I become the club owner")
    public void i_become_the_club_owner() {
        Assertions.assertNotNull(clubService.getById(clubId).getOwner().getId());
    }

    @When("I click a dedicated UI element to create a club")
    public void i_click_a_dedicated_ui_element_to_create_a_club() throws Exception {
        result = mockMvc.perform(get("/createClub").with(csrf())
                .contentType("text/html; charset=UTF-8"))
            .andExpect(status().isOk()).andReturn();
    }

    @Then("I am presented with a form to create a club")
    public void i_am_presented_with_a_form_to_create_a_club() throws UnsupportedEncodingException {
        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertTrue(responseBody.contains("Create Club"));
    }

    @Then("I must specify a name, sport type, city and country")
    public void i_must_specify_a_name_sport_type_city_and_country() throws Exception {

        String locationBody = "{\"line1\": \"address1\", \"line2\": \"address2\", \"country\": "
            + "\"1\", \"city\": \"1\", \"suburb\": \"suburb\", \"postcode\": \"postcode\"}";
        String clubBody = "{\"club\":{\"name\":\"TestClubName\", \"sport\" : \"NONE\", "
            + "\"location\" : " + locationBody + "}, \"teamIds\" : [1]}";
        MvcResult clubPostResult = mockMvc.perform(post("/create-club")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(clubBody))
            .andReturn();
        MockHttpServletResponse clubPostResponse = clubPostResult.getResponse();
        JSONObject returnedClubBody = new JSONObject(clubPostResponse.getContentAsString());
        Assertions.assertNotNull(returnedClubBody.get("country").toString());
        Assertions.assertNotNull(returnedClubBody.get("country").toString());
        Assertions.assertNotNull(returnedClubBody.get("sport").toString());
    }

    @And("I can optionally add an address line 1, address line 2, suburb and postcode")
    public void i_can_optionally_add_an_address_line1_address_line2_suburb_and_postcode()
        throws Exception {
        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertTrue(responseBody.contains("Address"));
        Assertions.assertTrue(responseBody.contains("Suburb"));
        Assertions.assertTrue(responseBody.contains("Postcode"));
    }

    @When("I input an empty country")
    public void i_input_an_empty_country() throws Exception {

        String locationBody = "{\"line1\": \"address1\", \"line2\": \"address2\", \"country\": "
            + "\"\", \"city\": \"city\", \"suburb\": \"suburb\", \"postcode\": \"postcode\"}";
        String clubBody = "{\"club\":{\"name\":\"TestClubName\", \"sport\" : \"Futsal\", "
            + "\"location\" : " + locationBody + "}, \"teamIds\" : [1]}";
        MvcResult clubPostResult = mockMvc.perform(post("/create-club")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(clubBody))
            .andReturn();
        MockHttpServletResponse clubPostResponse = clubPostResult.getResponse();
        this.clubBody = new JSONObject(clubPostResponse.getContentAsString());

    }

    @When("I input an empty sport")
    public void i_input_an_empty_sport() throws Exception {

        String locationBody = "{\"line1\": \"address1\", \"line2\": \"address2\", \"country\": "
            + "\"country\", \"city\": \"city\", \"suburb\": \"suburb\", \"postcode\": "
            + "\"postcode\"}";
        String clubBody = "{\"club\":{\"name\":\"TestClubName\", \"sport\" : \"NONE\", "
            + "\"location\" : " + locationBody + "}, \"teamIds\" : [1]}";
        MvcResult clubPostResult = mockMvc.perform(post("/create-club")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(clubBody))
            .andReturn();
        MockHttpServletResponse clubPostResponse = clubPostResult.getResponse();
        this.clubBody = new JSONObject(clubPostResponse.getContentAsString());

    }

    @When("I input an empty city")
    public void i_input_an_empty_city() throws Exception {

        String locationBody = "{\"line1\": \"address1\", \"line2\": \"address2\", \"country\": "
            + "\"country\", \"city\": \"\", \"suburb\": \"suburb\", \"postcode\": \"postcode\"}";
        String clubBody = "{\"club\":{\"name\":\"TestClubName\", \"sport\" : \"Futsal\", "
            + "\"location\" : " + locationBody + "}, \"teamIds\" : [1]}";
        MvcResult clubPostResult = mockMvc.perform(post("/create-club")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(clubBody))
            .andReturn();
        MockHttpServletResponse clubPostResponse = clubPostResult.getResponse();
        this.clubBody = new JSONObject(clubPostResponse.getContentAsString());

    }

    @When("I input an empty name")
    public void i_input_an_empty_name() throws Exception {

        String locationBody = "{\"line1\": \"address1\", \"line2\": \"address2\", \"country\": "
            +
            "\"country\", \"city\": \"city\", \"suburb\": \"suburb\", \"postcode\": \"postcode\"}";
        String clubBody = "{\"club\":{\"name\":\"\", \"sport\" : \"Futsal\", "
            + "\"location\" : " + locationBody + "}, \"teamIds\" : [1]}";
        MvcResult clubPostResult = mockMvc.perform(post("/create-club")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(clubBody))
            .andReturn();
        MockHttpServletResponse clubPostResponse = clubPostResult.getResponse();
        this.clubBody = new JSONObject(clubPostResponse.getContentAsString());

    }

    @Then("I see an error message telling me that the input is invalid")
    public void i_see_an_error_message_telling_me_that_the_input_is_invalid() {
        Assertions.assertEquals(1, clubBody.length());
    }

    @And("I can add an optional logo")
    public void able_to_add_optional_logo() throws UnsupportedEncodingException {
        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertTrue(responseBody.contains("profilePicture"));
    }

    @And("If none is given, a generic club image is used")
    public void if_no_logo_is_provided_generic_club_image_is_used()
        throws UnsupportedEncodingException {
        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertTrue(responseBody.contains("images/default_club.png"));
    }

    @And("There are two teams I am a manager of")
    public void there_are_two_teams_i_am_a_manager_of() throws NotFoundException {
        Location location =
            locationService.createLocationInDatabase("Nelson", "New Zealand");
        Location location1 =
            locationService.createLocationInDatabase("Christchurch", "New Zealand");
        Team teamWithClub = new Team("TeamWithClub", "Football", location, "null");
        Club club = new Club("Club", SupportedSports.Football,
            location1, tabUserService.getCurrentlyLoggedIn(), "null");
        Team team1 = teamService.save(teamWithClub);
        List<Long> teamId = new ArrayList<>();
        teamId.add(team1.getId());
        ClubDto clubDto = new ClubDto(club, teamId);
        clubService.saveClubDto(clubDto, tabUserService.getCurrentlyLoggedIn());
        teamService.createTeamInDatabase("TeamWithoutClub", "Football",
            "Nelson", "New Zealand", "null");
    }

    @Then("only the team without a club is available")
    public void only_the_team_without_a_club_is_available()
        throws UnsupportedEncodingException {
        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertTrue(responseBody.contains("TeamWithoutClub"));
        Assertions.assertFalse(responseBody.contains("TeamWithClub"));
    }
}
