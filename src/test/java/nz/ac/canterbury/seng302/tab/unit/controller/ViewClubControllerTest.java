package nz.ac.canterbury.seng302.tab.unit.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.io.IOException;
import javassist.NotFoundException;
import nz.ac.canterbury.seng302.tab.controller.ViewClubController;
import nz.ac.canterbury.seng302.tab.entity.Club;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.service.ClubService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.unit.UnitCommonTestSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

/**
 * Test class for view Club controller
 */
@SpringBootTest
@AutoConfigureMockMvc
class ViewClubControllerTest {
    @Mock
    TabUserService tabUserService;

    @InjectMocks
    private ViewClubController viewClubController;

    @Mock
    private ClubService clubService;

    /**
     * Test the page loads the correct content
     */
    @Test
    void contextLoads() throws NotFoundException, IOException {

        Long id = 123L;
        TabUser user = UnitCommonTestSetup.createTestUser();
        Club club = UnitCommonTestSetup.createTestClub();
        club.setId(id);
        Model model = new ConcurrentModel();
        when(tabUserService.getCurrentlyLoggedIn()).thenReturn(user);
        when(clubService.getById(anyLong())).thenReturn(club);
        String response = viewClubController.viewClub(id, model);
        Assertions.assertTrue(response.contains("viewClub"));
        Assertions.assertEquals(model.getAttribute("club"), club);
    }
}
