package nz.ac.canterbury.seng302.tab.unit.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.Date;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
import nz.ac.canterbury.seng302.tab.service.ValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Unit testing the Club Rest Controller End Points in ClubRestController.java
 */
@SpringBootTest
class ClubRestControllerTest {

    Club club;
    ClubDto clubDto;
    TabUser user;
    Location location;
    @Mock
    private TeamService teamService;
    @Mock
    private LocationService locationService;
    @Mock
    private ValidationService validationService;
    @Mock
    private TabUserService tabUserService;
    @Mock
    private ClubService clubService;
    @InjectMocks
    private ClubRestController clubRestController;

    /**
     * Set up basic information for each test
     */
    @BeforeEach
    void setup() {
        location = new Location("City", "Country");
        TabUser user =
            new TabUser(Arrays.asList("test", "person"), location, "email@email.com",
                new Date(2002, 12, 12).toString(),
                "*****", "profile_picture.jpg", "Baseball");
        user.setId(1L);
        club = new Club("Chelsea", SupportedSports.Football, location, user, "");
        clubDto = new ClubDto(club);
    }

    @Test
    void getTeam() throws Exception {

        String profilePictureSource = "profile_picture.jpg";
        Team team = new Team("Chelsea", "Football", location, profilePictureSource);
        when(teamService.getTeamById(1L)).thenReturn(team);

        ResponseEntity<Team> response = clubRestController.getTeam(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(team, response.getBody());
    }

    /**
     * Ensures that the creation club end point returns a 201 CREATED when passed a valid form
     */
    @Test
    void createClub_valid() throws Exception {

        // Mock the service calls
        Map<String, String> body = new HashMap<>();
        when(validationService.validateClub(clubDto, body)).thenReturn(true);
        when(clubService.saveClubDto(clubDto, user)).thenReturn(club);
        when(tabUserService.getCurrentlyLoggedIn()).thenReturn(user);

        ResponseEntity<Map<String, String>> response = clubRestController.createClub(clubDto,
            null);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    /**
     * Ensures that the creation club end point returns a 400 BAD_REQUEST
     * when passed an invalid form
     */
    @Test
    void createClub_invalid() throws Exception {

        // Mock the service calls
        Map<String, String> body = new HashMap<>();
        when(validationService.validateClub(clubDto, body)).thenReturn(false);
        when(clubService.saveClubDto(clubDto, user)).thenReturn(club);
        when(tabUserService.getCurrentlyLoggedIn()).thenReturn(user);

        ResponseEntity<Map<String, String>> response = clubRestController.createClub(clubDto,
            null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Ensures that the creation club end point returns a 400 BAD_REQUEST
     * when an exception is thrown
     */
    @Test
    void createClub_exception() throws Exception {

        // Mock the service calls
        Map<String, String> body = new HashMap<>();
        when(validationService.validateClub(clubDto, body)).thenReturn(true);
        when(clubService.saveClubDto(clubDto, user)).thenReturn(null);
        when(tabUserService.getCurrentlyLoggedIn()).thenReturn(null);

        ResponseEntity<Map<String, String>> response = clubRestController.createClub(clubDto,
            null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Checks if name can be updated.
     */
    @Test
    void editClub_update_name() throws NotFoundException, IOException {
        //Mock setup
        Map<String, String> body = new HashMap<>();
        clubDto.getClub().setId(100L);
        clubDto.getClub().setName("Changed Name");
        when(validationService.validateClub(clubDto, body)).thenReturn(true);
        when(clubService.saveClubDto(clubDto, user)).thenReturn(club);
        when(clubService.getById(100L)).thenReturn(club);
        when(tabUserService.getCurrentlyLoggedIn()).thenReturn(user);
        when(locationService.getLocationByLocationId(club.getLocation().getLocationId()))
            .thenReturn(location);
        when(clubService.saveClub(club)).thenReturn(club);
        ResponseEntity<Map<String, String>> response = clubRestController.editClub(100L, clubDto,
            null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Checks if location can be updated.
     */
    @Test
    void editClub_update_location() throws NotFoundException, IOException {
        //Mock setup
        Map<String, String> body = new HashMap<>();
        clubDto.getClub().setId(100L);
        Location updatedLocation = new Location("Updated City", "Updated Country");
        clubDto.getClub().setLocation(updatedLocation);
        when(validationService.validateClub(clubDto, body)).thenReturn(true);
        when(clubService.saveClubDto(clubDto, user)).thenReturn(club);
        when(clubService.getById(100L)).thenReturn(club);
        when(tabUserService.getCurrentlyLoggedIn()).thenReturn(user);
        when(locationService.getLocationByLocationId(club.getLocation().getLocationId()))
            .thenReturn(location);
        when(clubService.saveClub(club)).thenReturn(club);
        ResponseEntity<Map<String, String>> response = clubRestController.editClub(100L, clubDto,
            null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * List of invalid entries are checked.
     */
    @Test
    void editClub_invalid() {
        //Mock setup
        Map<String, String> body = new HashMap<>();
        clubDto.getClub().setId(100L);
        when(validationService.validateClub(clubDto, body)).thenReturn(false);
        ResponseEntity<Map<String, String>> response = clubRestController.editClub(100L, clubDto,
            null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}
