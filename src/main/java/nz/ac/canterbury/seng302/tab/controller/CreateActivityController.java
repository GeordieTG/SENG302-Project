package nz.ac.canterbury.seng302.tab.controller;

import nz.ac.canterbury.seng302.tab.entity.Activity;
import nz.ac.canterbury.seng302.tab.formobjects.ActivityForm;
import nz.ac.canterbury.seng302.tab.service.ActivityService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.TeamService;
import nz.ac.canterbury.seng302.tab.service.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Controller class for creating an activity
 */
@Controller
public class CreateActivityController {

    private static final String ERROR = "error";
    Logger logger = LoggerFactory.getLogger(CreateActivityController.class);
    @Autowired
    TabUserService tabUserService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ValidationService validationService;
    @Autowired
    private TeamService teamService;

    /**
     * Get request for creating an activity
     *
     * @param model thymeleaf model
     * @return create activity
     */
    @GetMapping("/createActivity")
    public String getCreateActivityForm(Model model) {
        logger.info("GET /createActivity");

        model.addAttribute("activityForm", new ActivityForm());
        try {
            model.addAttribute("usersTeams", teamService.getTeamsThatUserManagesOrCoaches(
                tabUserService.getCurrentlyLoggedIn()));
        } catch (Exception e) {
            model.addAttribute(ERROR, "user not found");
        }

        return "createActivity";
    }

    /**
     * Post request for creating an activity
     *
     * @param activityForm activity form
     * @param model        thymeleaf model
     * @return create activity page if unsuccessful, view activity if successful
     */
    @PostMapping("/createActivity")
    public String createActivity(@ModelAttribute("activityForm") ActivityForm activityForm,
                                 Model model) {
        logger.info("POST /createActivity");
        model.addAttribute("activityForm", activityForm);

        // Validate
        if (!validationService.validateActivityForm(activityForm, model)) {
            try {
                model.addAttribute("usersTeams", teamService.getTeamsThatUserManagesOrCoaches(
                    tabUserService.getCurrentlyLoggedIn()));
            } catch (Exception e) {
                model.addAttribute(ERROR, "user not found");
            }
            return "createActivity";
        } else {
            // Create Activity
            Activity activity = null;
            try {
                activity = activityService.createActivityInDatabase(activityForm);
            } catch (Exception e) {
                model.addAttribute(ERROR, "failed to create activity");
            }
            assert activity != null;
            model.addAttribute(activity);
            return "redirect:/viewActivity?id=" + activity.getId();
        }

    }


}
