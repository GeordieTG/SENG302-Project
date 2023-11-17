package nz.ac.canterbury.seng302.tab.unit.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.ParseException;
import javassist.NotFoundException;
import nz.ac.canterbury.seng302.tab.controller.UserProfileController;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.formobjects.UserEditProfilePageForm;
import nz.ac.canterbury.seng302.tab.service.GarminService;
import nz.ac.canterbury.seng302.tab.service.LocationService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.ValidationService;
import nz.ac.canterbury.seng302.tab.unit.UnitCommonTestSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;


@ExtendWith(MockitoExtension.class)
class UserProfileControllerMockedTest {
    @Mock
    TabUserService tabUserService;
    @Mock
    LocationService locationService;
    @Mock
    ValidationService validationService;
    @Mock
    GarminService garminService;
    @Mock
    Authentication authentication;

    @InjectMocks
    UserProfileController userProfileController;
    /**
     * To produce logs
     */
    Logger logger = LoggerFactory.getLogger(UserProfileControllerMockedTest.class);


    @Test
    void userProfileEdit() throws IOException, ParseException {
        TabUser user = UnitCommonTestSetup.createTestUser();
        SecurityContextHolder.setContext(new SecurityContextImpl());
        when(authentication.getName()).thenReturn("mockedUser");
        when(tabUserService.findByEmail(any())).thenReturn(user);
        when(tabUserService.getInfoByEmail(any())).thenReturn(user);
        when(locationService.getLocationByLocationId(anyLong()))
            .thenReturn(UnitCommonTestSetup.createTestLocation());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Model model = new ConcurrentModel();
        Assertions.assertEquals("editProfile",
            userProfileController.userProfileEdit("IGNORE",
                "IGNORE", "IGNORE", "IGNORE", "Profile", model));
    }

    @Test
    void getResetConnectStateAlertTestWithCorrectValue() {
        ResponseEntity<Void> response = userProfileController
            .getResetConnectStateAlert("True");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getResetConnectStateAlertTestWithIncorrectValue() {
        ResponseEntity<Void> response = userProfileController
            .getResetConnectStateAlert("False");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void submitUserTest() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContextImpl securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
        TabUser user = UnitCommonTestSetup.createTestUser();
        when(tabUserService.findByEmail(null)).thenReturn(user);
        when(tabUserService.getByEmail(anyString())).thenReturn(false);
        UserEditProfilePageForm userEditProfilePageForm = new UserEditProfilePageForm(user,
                UnitCommonTestSetup.createTestLocation());
        Model testModel = new ConcurrentModel();
        when(validationService.validateEditUserProfileForm(userEditProfilePageForm, user.getEmail(),
                testModel)).thenReturn(true);
        try {
            when(locationService.getLocationByLocationId(user.getLocationId()))
                    .thenReturn(UnitCommonTestSetup.createTestLocation());
            String result = userProfileController.submitUser(userEditProfilePageForm, testModel);
            Assertions.assertEquals("redirect:./profilePage", result);
        } catch (NotFoundException | IOException e) {
            logger.error(e.getMessage());
        }
        Assertions.assertEquals(false, testModel.getAttribute("invalidEmail"));
    }

    @Test
    void submitUserEmailExists() {
        TabUser user = UnitCommonTestSetup.createTestUser();
        user.setEmail("testEmail@email.com");
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContextImpl securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(tabUserService.findByEmail(null)).thenReturn(user);
        when(tabUserService.getByEmail(anyString())).thenReturn(false);
        UserEditProfilePageForm userEditProfilePageForm = new UserEditProfilePageForm(user,
                UnitCommonTestSetup.createTestLocation());
        Model testModel = new ConcurrentModel();
        when(validationService.validateEditUserProfileForm(userEditProfilePageForm, user.getEmail(),
                testModel)).thenReturn(true);
        try {
            when(locationService.getLocationByLocationId(user.getLocationId()))
                    .thenReturn(UnitCommonTestSetup.createTestLocation());
            String result = userProfileController.submitUser(userEditProfilePageForm, testModel);
            Assertions.assertEquals("redirect:./profilePage", result);
        } catch (NotFoundException | IOException e) {
            logger.error(e.getMessage());
        }
        Assertions.assertEquals(false, testModel.getAttribute("invalidEmail"));
    }

}
