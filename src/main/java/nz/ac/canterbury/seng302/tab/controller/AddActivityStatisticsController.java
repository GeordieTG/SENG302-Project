package nz.ac.canterbury.seng302.tab.controller;

import java.util.List;
import nz.ac.canterbury.seng302.tab.entity.Activity;
import nz.ac.canterbury.seng302.tab.entity.StatisticFact;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.formobjects.StatisticsForm;
import nz.ac.canterbury.seng302.tab.service.ActivityService;
import nz.ac.canterbury.seng302.tab.service.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller class for creating the statistics for the activity
 */
@Controller
public class AddActivityStatisticsController {

    public static final String WRAPPER = "wrapper";
    Logger logger = LoggerFactory.getLogger(AddActivityStatisticsController.class);

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ValidationService validationService;


    /**
     * Get request for creating statistics form for the activity
     *
     * @param id    id of the activity
     * @param model thymeleaf model
     * @return create activity
     */
    @GetMapping("/addActivityStatistics")
    public String getCreateStatisticsForm(@RequestParam("id") long id, Model model) {
        logger.info("GET /addActivityStatistics");
        StatisticsForm statisticsForm = activityService.getStatistics(id);
        model.addAttribute(WRAPPER, statisticsForm);
        Activity activity = activityService.getActivityById(id);
        Team myTeam = activity.getTeam();
        List<TabUser> players = activityService.getPlayers(myTeam, activity);
        model.addAttribute("myTeam", myTeam);
        model.addAttribute("players", players);
        return "addActivityStatistics";
    }

    /**
     * Handler for post request for submitting a statistics form
     *
     * @param statisticsForm The form to be added to the database
     * @param result         handles binding errors related to the statisticsForm
     * @param model          thymeleaf model
     * @return add activity statistics page
     */
    @PostMapping("/addStatistics")
    public String handleStatisticForm(
        @ModelAttribute("statisticsForm") StatisticsForm statisticsForm,
        BindingResult result, Model model) {
        logger.info("POST /addStatistics");

        //retrieve existing activity details by Activity ID
        Activity oldActivity = activityService.getActivityById(statisticsForm.getActivityId());

        //retrieve existing activity stats by Activity ID
        StatisticsForm statisticsForm1 =
            activityService.getStatistics(statisticsForm.getActivityId());

        //Check if first time inputting stats
        statisticsForm.setFirstTime(statisticsForm1.isFirstTime());

        //if input is valid, update the stats
        if (validationService.validateStatisticsForm(statisticsForm, oldActivity, model)) {
            activityService.addStatisticsForm(statisticsForm);
            return "redirect:/viewActivity" + "?id=" + statisticsForm.getActivityId();
        } else {
            //if invalid, then refresh the page
            model.addAttribute(WRAPPER, statisticsForm);
            Activity activity = activityService.getActivityById(statisticsForm.getActivityId());
            Team myTeam = activity.getTeam();
            List<TabUser> players = activityService.getPlayers(myTeam, activity);
            model.addAttribute("myTeam", myTeam);
            model.addAttribute("players", players);
            model.addAttribute("activityDuration", activity.getDuration());
            return "addActivityStatistics";
        }
    }

    /**
     * Get mapping for the activity facts page
     *
     * @return the addActivityFacts page with a basic table for adding a fact
     *         consisting of a description and an optional timestamp
     */
    @GetMapping("/addActivityFacts")
    public String getFactForm(@RequestParam("id") long id, Model model) {
        logger.info("GET /addActivityFacts");
        List<StatisticFact> statisticFacts = activityService.getActivityById(id)
            .getFactStatistics();
        model.addAttribute(WRAPPER, new StatisticsForm(id, statisticFacts));

        return "addActivityFacts";
    }

    /**
     * Post mapping for saving activity facts
     *
     * @param statisticsForm The form containing the activity facts
     * @param result         The binding result of the form
     * @param model          thymeleaf model
     * @return redirect to add activity facts page
     */
    @PostMapping("/addActivityFacts")
    public String addFact(@ModelAttribute("statisticsForm") StatisticsForm statisticsForm,
                          BindingResult result, Model model) {
        logger.info("POST /addFact");
        long id = statisticsForm.getActivityId();
        if (validationService.validateFactForms(statisticsForm, model)) {
            try {
                activityService.updateFactStatistics(statisticsForm.getFactForms());
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        } else {
            model.addAttribute(WRAPPER, statisticsForm);
            return "addActivityFacts";
        }
        return "redirect:/viewActivity" + "?id=" + id;
    }
}
