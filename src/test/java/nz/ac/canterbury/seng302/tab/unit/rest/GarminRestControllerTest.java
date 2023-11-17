package nz.ac.canterbury.seng302.tab.unit.rest;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import nz.ac.canterbury.seng302.tab.controller.rest.GarminRestController;
import nz.ac.canterbury.seng302.tab.entity.Activity;
import nz.ac.canterbury.seng302.tab.entity.garminentities.GarminActivity;
import nz.ac.canterbury.seng302.tab.service.ActivityService;
import nz.ac.canterbury.seng302.tab.service.GarminService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.ValidationService;
import nz.ac.canterbury.seng302.tab.unit.UnitCommonTestSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;


/**
 * Test class for creatTeam controller
 */
@SpringBootTest
@AutoConfigureMockMvc
class GarminRestControllerTest {

    @Mock
    TabUserService tabUserService;
    @Mock
    GarminService garminService;
    @Mock
    ValidationService validationService;
    @Mock
    ActivityService activityService;

    @InjectMocks
    private GarminRestController garminRestController;

    @Test
    void testSaveGarminActivity_ValidationSuccess() {
        long activityId = 1L;
        GarminActivity garminActivity = new GarminActivity();
        garminActivity.setStartTime(LocalDateTime.now());

        when(activityService.getActivityById(activityId)).thenReturn(new Activity());
        when(validationService.validateGarminActivity(garminActivity))
            .thenReturn(new ArrayList<>());
        when(tabUserService.getCurrentlyLoggedIn())
            .thenReturn(UnitCommonTestSetup.createTestUser());

        ResponseEntity<List<String>> response = garminRestController
            .saveGarminActivity(activityId, garminActivity, mock(BindingResult.class));

        verify(garminService).saveGarminActivity(garminActivity);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testSaveGarminActivity_ValidationFailure() {
        long activityId = 1L;
        GarminActivity garminActivity = new GarminActivity();
        garminActivity.setStartTime(LocalDateTime.now());

        when(activityService.getActivityById(activityId)).thenReturn(new Activity());
        when(validationService.validateGarminActivity(garminActivity))
            .thenReturn(List.of("error"));

        ResponseEntity<List<String>> response = garminRestController
            .saveGarminActivity(activityId, garminActivity, mock(BindingResult.class));

        verify(garminService, never()).saveGarminActivity(garminActivity);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}