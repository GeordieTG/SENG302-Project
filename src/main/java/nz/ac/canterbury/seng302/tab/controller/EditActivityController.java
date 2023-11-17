package nz.ac.canterbury.seng302.tab.controller;

import static java.lang.Integer.parseInt;

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
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for edit activity
 */
@Controller
public class EditActivityController {
    private static final String ERROR = "error";
    Logger logger = LoggerFactory.getLogger(EditActivityController.class);
    @Autowired
    TabUserService tabUserService;
    /**
     * Allows us to access activity functionality with the database
     */
    @Autowired
    private ActivityService activityService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private ValidationService validationService;

    /**
     * Get request for retrieving the edit activity form
     *
     * @param id    id of the activity
     * @param model thymeleaf model
     * @return the edit activity page
     */
    @GetMapping("/editActivity")
    public String responses(@RequestParam("id") String id, Model model) {

        logger.info("GET /editActivity");

        Activity activity = activityService.getActivityById(parseInt(id));


        ActivityForm activityForm = new ActivityForm(activity);
        model.addAttribute("activityForm", activityForm);
        model.addAttribute("id", id);
        try {
            model.addAttribute("usersTeams", teamService.getTeamsThatUserManagesOrCoaches(
                tabUserService.getCurrentlyLoggedIn()));
        } catch (Exception e) {
            model.addAttribute(ERROR, "user not found");
        }


        return "editActivity";
    }

    /**
     * Post request for the activity form
     *
     * @param activityForm activity form that is being edited
     * @param model        thymeleaf model
     * @return the view activity page if successful, stays on page if unsuccessful
     */
    @PostMapping("/editActivity")
    public String updateActivity(@ModelAttribute ActivityForm activityForm, Model model) {
        logger.info("POST /editActivity");
        model.addAttribute("activityForm", activityForm);
        model.addAttribute("id", activityForm.getId());
        Activity activity = new Activity();

        //Validation
        if (!validationService.validateActivityForm(activityForm, model)) {
            try {
                model.addAttribute("usersTeams", teamService.getTeamsThatUserManagesOrCoaches(
                    tabUserService.getCurrentlyLoggedIn()));
            } catch (Exception e) {
                model.addAttribute(ERROR, "user not found");
            }
            return "editActivity";
        } else {
            // Update activity details
            try {
                activity = activityService.updateActivityInDatabase(activityForm);
            } catch (Exception e) {
                model.addAttribute(ERROR, "failed to update activity");
            }
            return "redirect:/viewActivity?id=" + activity.getId();
        }
    }
}
