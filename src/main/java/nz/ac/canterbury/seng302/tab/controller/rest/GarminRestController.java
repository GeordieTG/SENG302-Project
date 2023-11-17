package nz.ac.canterbury.seng302.tab.controller.rest;

import java.util.List;
import nz.ac.canterbury.seng302.tab.entity.Activity;
import nz.ac.canterbury.seng302.tab.entity.garminentities.GarminActivity;
import nz.ac.canterbury.seng302.tab.service.ActivityService;
import nz.ac.canterbury.seng302.tab.service.GarminService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * A rest controller.
 * Used save garmin activities
 */
@RestController
public class GarminRestController {

    @Autowired
    GarminService garminService;

    @Autowired
    ActivityService activityService;

    @Autowired
    ValidationService validationService;

    @Autowired
    TabUserService tabUserService;


    Logger logger = LoggerFactory.getLogger(GarminRestController.class);


    /**
     * Post request called from JS to save a Garmin Activity in the DB
     *
     * @param garminActivity Sent in the HTTP request as JSON ,
     *                       gets mapped to a GarminActivity automatically
     * @param bindingResult  Used to ensure empty fields are bound as null
     * @return A ResponseEntity containing the HTTP status and any error messages to display
     */
    @PostMapping("/saveGarminActivity/{activityId}")
    public ResponseEntity<List<String>> saveGarminActivity(
        @PathVariable("activityId") Long activityId, @RequestBody GarminActivity garminActivity,
        BindingResult bindingResult) {
        logger.info("POST /saveGarminActivity/{}", activityId);
        Activity activity = activityService.getActivityById(activityId);
        garminActivity.setActivity(activity);
        List<String> errors = validationService.validateGarminActivity(garminActivity);
        if (errors.isEmpty()) {
            garminActivity.setTabUser(tabUserService.getCurrentlyLoggedIn());
            GarminActivity existingGarminActivity = garminService.getByActivityAndUser(
                activityId, tabUserService.getCurrentlyLoggedIn().getId());
            if (existingGarminActivity != null) {
                garminService.delete(existingGarminActivity);
            }

            garminService.saveGarminActivity(garminActivity);
            return ResponseEntity.status(200).build();
        } else {
            logger.error("User changed in html");
            return ResponseEntity.status(400).body(errors);

        }
    }
}