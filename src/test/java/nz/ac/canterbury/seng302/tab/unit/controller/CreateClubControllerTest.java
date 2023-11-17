package nz.ac.canterbury.seng302.tab.unit.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import nz.ac.canterbury.seng302.tab.controller.CreateClubController;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.unit.UnitCommonTestSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;

/**
 * Test class for creatTeam controller
 */
@SpringBootTest
@AutoConfigureMockMvc
class CreateClubControllerTest {

    @Mock
    TabUserService tabUserService;

    @InjectMocks
    private CreateClubController createClubController;


    /**
     * Test the page loads the correct content
     */
    @Test
    void contextLoads() {

        TabUser user = UnitCommonTestSetup.createTestUser();
        Model model = mock(Model.class);
        when(tabUserService.getCurrentlyLoggedIn()).thenReturn(user);
        String response = createClubController.getCreateClubForm(model);
        Assertions.assertTrue(response.contains("createClub"));
    }

}
