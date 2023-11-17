package nz.ac.canterbury.seng302.tab.unit.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import nz.ac.canterbury.seng302.tab.controller.AllUsersController;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.service.SportService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.ValidationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class RegistrationControllerTest {

    @Mock
    private TabUserService tabUserService;

    @Mock
    private SportService sportService;

    @InjectMocks
    private AllUsersController controller;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ValidationService validationservice;

    @Autowired
    private WebApplicationContext webApp;

    /**
     * Adds sports to the database and adds a test user to the UserManager for testing purposes
     */
    @BeforeEach
    void addSports() {
        List<String> sports = Arrays.asList("swimming", "cycling", "tennis", "boxing", "shooting",
            "equestrian jumping", "sailing", "rhythmic gymnastics", "judo", "golf", "snooker",
            "basketball", "football", "volleyball", "baseball", "ski jumping ", "figure skating",
            "bobsleigh", "synchronized swimming ", "rowing", "kayak slalom ", "triathalon",
            "biathlon", "curling", "table tennis", "gymnastics", "bowling");
        for (String sport : sports) {
            sportService.addNewSports(sport);
        }
        mockMvc = MockMvcBuilders.webAppContextSetup(webApp).build();
        TabUser tester =
            tabUserService.createFullTabUserInDatabase(Arrays.asList("test", "person"),
                    new Location("Christchurch",
                "New Zealand"), "email@email.com",
                    new Date(2002 + 1900, 11, 30).toString(), "*****",
                "image.jpg", "Baseball");
    }

    /**
     * Deletes all sports from the database
     */
    @AfterEach
    void tearDown() {
        sportService.deleteAllData();
    }

    /**
     * Test selected profile matches the db user
     */
    @Test
    void selectedProfileTest() {
        int id = 3;

        TabUser user = fillUsers().get(2);

        when(tabUserService.getById(id)).thenReturn(user);

        Model model = mock(Model.class);
        String viewName = controller.selectedProfile(model, id);

        assertEquals("selectedUser", viewName);
        verify(tabUserService).getById(id);
        verify(model).addAttribute("firstName", user.getFirstName());
        verify(model).addAttribute("lastName", user.getLastName());
        verify(model).addAttribute("email", user.getEmail());
        verify(model).addAttribute("DOB", user.getDateOfBirth());
        verify(model).addAttribute("profilePicture", user.getProfilePicture());
    }


    /**
     * Test invalid names are caught and an error is shown to user
     */
    @Test
    void testCheckNames() {
        // Setup
        String validFirstName = "John";
        String validLastName = "Doe";
        Model model = new ConcurrentModel();

        boolean result = validationservice.checkNames(validFirstName, validLastName, model);
        assertTrue(result);

        String invalidFirstName = "John@";
        result = validationservice.checkNames(invalidFirstName, validLastName, model);
        assertFalse(result);

        String invalidLastName = "Doe?";
        result = validationservice.checkNames(invalidFirstName, invalidLastName, model);
        assertFalse(result);

        result = validationservice.checkNames(validFirstName, invalidLastName, model);
        assertFalse(result);

        // Test multiple invalid characters
        invalidFirstName = "John@Doe?";
        result = validationservice.checkNames(invalidFirstName, validLastName, model);
        assertFalse(result);
    }

    /**
     * Test file size moderation allows image files under 10mb but not over
     */
    @Test
    void testCheckFileSize() {
        MultipartFile validFile = new MockMultipartFile("test.jpg", new byte[9999999]);
        Model model = new ExtendedModelMap();

        boolean result = validationservice.checkFileSize(validFile, model);
        assertFalse(result);

        MultipartFile invalidFile = new MockMultipartFile("test.jpg", new byte[10000001]);

        result = validationservice.checkFileSize(invalidFile, model);
        assertTrue(result);
    }

    /**
     * Test invalid emails are caught and an error is shown to user
     */
    @Test
    void testCheckEmail() {
        String invalidEmail = "greg@";
        String validEmail = "greg@gregsfood.com";
        Model model = new ExtendedModelMap();

        boolean result = validationservice.checkEmail(invalidEmail, model);
        assertFalse(result);


        result = validationservice.checkEmail(validEmail, model);
        assertTrue(result);
    }

    /**
     * Tests that invalid date of births are caught and an error is shown to user
     */
    @Test
    void testCheckAge() {
        Model model = new ExtendedModelMap();

        Date invalidDobDate = new Date(2020 - 1900, 2, 12);
        boolean result = validationservice.checkAge(invalidDobDate.toString(), model);
        assertFalse(result);

        Date validDob = new Date(2000 - 1900, 2, 12);
        result = validationservice.checkAge(validDob.toString(), model);
        assertTrue(result);

        Date validDobExactly = new Date(2023 - 1900 - 13, 3, 25);
        result = validationservice.checkAge(validDobExactly.toString(), model);
        assertTrue(result);

    }


    /**
     * Create a list of users to be used in testing.
     *
     * @return a list of the users to be added to the database
     */
    public List<TabUser> fillUsers() {
        List<TabUser> results = new ArrayList<>();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        for (int i = 0; i < 16; i++) {
            Location location = new Location("City", "Country");
            TabUser tester =
                new TabUser(Arrays.asList("test", "person"), location, "email@email.com",
                    new Date(2002, 12, 12).toString(),
                    encoder.encode("Sukkon"), "image.jpg",
                    "Baseball");
            results.add(tester);
        }
        return results;
    }
}
