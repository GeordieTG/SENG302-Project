package nz.ac.canterbury.seng302.tab.unit.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javassist.NotFoundException;
import nz.ac.canterbury.seng302.tab.controller.rest.PlayerInformationController;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.entity.datatransferobjects.TabUserDto;
import nz.ac.canterbury.seng302.tab.service.ActivityService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.TeamService;
import nz.ac.canterbury.seng302.tab.unit.UnitCommonTestSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Player information controller tests
 */
@SpringBootTest
class PlayerInformationControllerTest {

    @Mock
    private TabUserService tabUserService;
    @Mock
    private ActivityService activityService;
    @Mock
    private TeamService teamService;

    @InjectMocks
    private PlayerInformationController playerInformationController;

    @BeforeEach
    void setup() {
        // Setup mock data or behavior if needed
    }

    @Test
    void testGetProfilePicture() {

        String profilePictureSource = "profile_picture.jpg";
        Location location = new Location("City", "Country");
        TabUser tabUser =
            new TabUser(Arrays.asList("test", "person"), location,
                "email@email.com", new Date(2002, 12, 12).toString(),
                "*****", "profile_picture.jpg", "Baseball");
        when(tabUserService.getById(1L)).thenReturn(tabUser);

        ResponseEntity<String> response = playerInformationController.getProfilePicture(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(profilePictureSource, response.getBody());
    }

    @Test
    void testGetProfilePictureNotFound() {
        Location location = new Location("City", "Country");
        TabUser tabUser =
            new TabUser(Arrays.asList("test", "person"), location,
                "email@email.com", new Date(2002, 12, 12).toString(),
                "*****", null, "Baseball");
        when(tabUserService.getById(1L)).thenReturn(tabUser);

        ResponseEntity<String> response = playerInformationController.getProfilePicture(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetTeamMembers() throws NotFoundException {
        TabUser tabUser1 = UnitCommonTestSetup.createTestUser("John");
        TabUser tabUser2 = UnitCommonTestSetup.createTestUser("Jane");
        List<TabUser> members = new ArrayList<>();
        members.add(tabUser1);
        members.add(tabUser2);
        Team team = UnitCommonTestSetup.createTestTeam();
        long mockId1 = 1L;
        long mockId2 = 2L;
        setId(tabUser1, mockId1);
        setId(tabUser2, mockId2);
        when(teamService.getTeamById(1L)).thenReturn(team);
        when(activityService.getPlayers(any(), any())).thenReturn(members);
        ResponseEntity<List<TabUserDto>> response = playerInformationController
                .getTeamMembers(1L, 2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        assertEquals("John", response.getBody().get(0).getFirstName());
        assertEquals("person", response.getBody().get(0).getLastName());
        assertEquals(1L, response.getBody().get(0).getId());
        assertEquals("Jane", response.getBody().get(1).getFirstName());
        assertEquals("person", response.getBody().get(1).getLastName());
        assertEquals(1L, response.getBody().get(0).getId());
        assertEquals(2L, response.getBody().get(1).getId());

    }

    /**
     * Helper method to set the ID field of a TabUser object using reflection kind of iffy
     * but cannot think of another way
     *
     * @param tabUser tab user
     * @param id      tab users Id to set
     */
    private void setId(TabUser tabUser, long id) {
        try {
            Field idField = TabUser.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(tabUser, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

