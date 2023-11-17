package nz.ac.canterbury.seng302.tab.unit.rest;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.file.FileSystemException;
import javassist.NotFoundException;
import nz.ac.canterbury.seng302.tab.controller.rest.ImageRestController;
import nz.ac.canterbury.seng302.tab.entity.Club;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.service.ClubService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.TeamService;
import nz.ac.canterbury.seng302.tab.unit.UnitCommonTestSetup;
import nz.ac.canterbury.seng302.tab.utility.ImageHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

/**
 * Test class for the image rest controller
 */
@SpringBootTest
class ImageRestControllerTest {

    MultipartFile multipartFile = mock(MultipartFile.class);
    @Mock
    private TeamService teamService;
    @Mock
    private TabUserService tabUserService;
    @Mock
    private ClubService clubService;
    @Mock
    private ImageHelper imageHelper;
    @InjectMocks
    private ImageRestController imageRestController;


    @Test
    void testImageSavesSuccessUser() throws FileSystemException {


        TabUser user = UnitCommonTestSetup.createTestUser();

        when(tabUserService.getById(anyLong())).thenReturn(user);
        when(imageHelper.save(multipartFile, "user")).thenReturn("image");
        when(tabUserService.addTabUser(user)).thenReturn(user);

        String response = imageRestController.uploadImage(multipartFile, anyLong(), "user");

        Assertions.assertEquals("Success", response);

    }

    @Test
    void testImageSavesSuccessTeam() throws NotFoundException, FileSystemException {
        Team team = UnitCommonTestSetup.createTestTeam();
        when(teamService.getTeamById(anyLong())).thenReturn(team);
        when(imageHelper.save(multipartFile, "team")).thenReturn("image");
        when(teamService.save(team)).thenReturn(team);

        String response = imageRestController.uploadImage(multipartFile, anyLong(), "team");

        Assertions.assertEquals("Success", response);

    }

    @Test
    void testImageSavesSuccessClub() throws FileSystemException {

        Club club = UnitCommonTestSetup.createTestClub();

        when(clubService.getById(anyLong())).thenReturn(club);
        when(imageHelper.save(multipartFile, "club")).thenReturn("image");
        when(clubService.saveClub(club)).thenReturn(club);

        String response = imageRestController.uploadImage(multipartFile, anyLong(), "club");

        Assertions.assertEquals("Success", response);
    }

    @Test
    void testImageSaveFailure() throws FileSystemException {

        Club club = UnitCommonTestSetup.createTestClub();

        when(clubService.getById(anyLong())).thenReturn(club);
        when(imageHelper.save(multipartFile, "smurf")).thenReturn("image");
        when(clubService.saveClub(club)).thenReturn(club);

        String response = imageRestController.uploadImage(multipartFile, club.getId(), "smurf");

        Assertions.assertEquals("You are a failure", response);
    }

}
