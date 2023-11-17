package nz.ac.canterbury.seng302.tab.integration;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import nz.ac.canterbury.seng302.tab.entity.Club;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.entity.enums.SupportedSports;
import nz.ac.canterbury.seng302.tab.service.ActivityService;
import nz.ac.canterbury.seng302.tab.service.ClubService;
import nz.ac.canterbury.seng302.tab.service.LocationService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.TeamService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration test for the ClubService
 */
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class ClubServiceTest {
    //TODO modify into the given when then integration after the front end has been set up
    @Autowired
    ClubService clubService;
    @Autowired
    TeamService teamService;
    @Autowired
    TabUserService tabUserService;

    @Autowired
    ActivityService activityService;
    @Autowired
    LocationService locationService;
    @Autowired
    AuthenticationManager authenticationManager;
    Location location;

    TabUser user;

    TabUser user1;

    @BeforeEach
    void setup() {
        location = locationService.createLocationInDatabase("Japan", "Not Japan");
        user =
            tabUserService.createFullTabUserInDatabase(Arrays.asList("Bob", "Smith"),
                    new Location("Greymouth", "New Zealand"),
                "bob@email.com", new Date(2002, 11, 31).toString(),
                "*****", "default.jpg", "basketball");
        user1 =
            tabUserService.createFullTabUserInDatabase(Arrays.asList("John", "Smith"),
                    new Location("Greymouth", "New Zealand"),
                "bob2@email.com", new Date(2002, 11, 31).toString(),
                "*****", "default.jpg", "basketball");
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

    @Test
    void testCreateClub() {
        Club club = new Club("Hero Baseball Club", SupportedSports.Baseball, location, user, "");
        Club saveClub = clubService.saveClub(club);
        Assertions.assertEquals("Hero Baseball Club", saveClub.getName());
        Assertions.assertEquals(SupportedSports.Baseball, saveClub.getSport());
        Assertions.assertEquals(location.getCity(), saveClub.getLocation().getCity());
        Assertions.assertEquals(location.getCountry(), saveClub.getLocation().getCountry());

    }

    @Test
    void findAllClub() {
        Club club1 = new Club("Hero Rugby Club", SupportedSports.League, location, user, "");
        clubService.saveClub(club1);
        Club club2 = new Club("Miro Football Club", SupportedSports.Football, location, user, "");
        clubService.saveClub(club2);
        Club club3 = new Club("Zero Hockey Club", SupportedSports.Hockey, location, user, "");
        clubService.saveClub(club3);

        List<Club> saveClubs = clubService.getAllClub();
        Assertions.assertEquals(3, saveClubs.size());
    }

    @Test
    void testSearchClubWithValidString() {
        Club club1 = new Club("Hero Rugby Club", SupportedSports.League, location, user, "");
        clubService.saveClub(club1);
        Club club2 = new Club("Miro Football Club", SupportedSports.Football, location, user, "");
        clubService.saveClub(club2);
        Club club3 = new Club("Zero Hockey Club", SupportedSports.Hockey, location, user, "");
        clubService.saveClub(club3);

        Page<Club> result = clubService.searchClubs("Hockey", 1);
        Assertions.assertEquals("Zero Hockey Club", result.toList().get(0).getName());
        Assertions.assertEquals(SupportedSports.Hockey, result.toList().get(0).getSport());
        Assertions.assertEquals(1, result.toList().size());

    }

    @Test
    void testSearchClubWithInvalidString() {
        Club club1 = new Club("Hero Rugby Club", SupportedSports.League, location, user, "");
        clubService.saveClub(club1);
        Club club2 = new Club("Miro Football Club", SupportedSports.Football, location, user, "");
        clubService.saveClub(club2);
        Club club3 = new Club("Zero Hockey Club", SupportedSports.Hockey, location, user, "");
        clubService.saveClub(club3);

        Page<Club> result = clubService.searchClubs("Size", 1);
        Assertions.assertEquals(0, result.toList().size());
    }

    @Test
    void testSearchClubWithValidLocation() {
        Club club1 = new Club("Hero Rugby Club", SupportedSports.League, location, user, "");
        clubService.saveClub(club1);
        Club club2 = new Club("Miro Football Club", SupportedSports.Football, location, user, "");
        clubService.saveClub(club2);
        Club club3 = new Club("Zero Hockey Club", SupportedSports.Hockey, location, user, "");
        clubService.saveClub(club3);

        Page<Club> result = clubService.searchClubs("Japan", 1);
        Assertions.assertEquals(3, result.toList().size());
        List<Club> clubList = result.toList();
        for (Club club : clubList) {
            Assertions.assertEquals("Japan", club.getLocation().getCity());
        }
    }

    @Test
    void testGetClubByIdWithInvalidId() {
        Assertions.assertNull(clubService.getById(100_000_000));
    }
}
