package nz.ac.canterbury.seng302.tab.controller;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javassist.NotFoundException;
import nz.ac.canterbury.seng302.tab.entity.Activity;
import nz.ac.canterbury.seng302.tab.entity.Formation;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.entity.enums.ActivityResult;
import nz.ac.canterbury.seng302.tab.entity.enums.SupportedSports;
import nz.ac.canterbury.seng302.tab.entity.garminentities.GarminActivity;
import nz.ac.canterbury.seng302.tab.exception.GarminPermissionException;
import nz.ac.canterbury.seng302.tab.formobjects.FormationForm;
import nz.ac.canterbury.seng302.tab.service.ActivityService;
import nz.ac.canterbury.seng302.tab.service.ActivityStatisticService;
import nz.ac.canterbury.seng302.tab.service.FormationService;
import nz.ac.canterbury.seng302.tab.service.GarminService;
import nz.ac.canterbury.seng302.tab.service.RoleVerifier;
import nz.ac.canterbury.seng302.tab.service.SportService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.TeamRoleService;
import nz.ac.canterbury.seng302.tab.service.TeamService;
import nz.ac.canterbury.seng302.tab.utility.ImageHelper;
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
 * Controller class for viewing an activity
 */
@Controller
public class ViewActivityController {

    public static final String FORMATION_ID = "formationId";
    public static final String BG_IMAGE = "bgImage";

    public static final String BG_IMAGE_EDIT = "bgImageEdit";
    public static final String FORMATION_FORM = "formationForm";
    @Autowired
    ActivityService activityService;

    @Autowired
    ActivityStatisticService activityStatisticService;

    @Autowired
    FormationService formationService;

    @Autowired
    TabUserService tabUserService;

    @Autowired
    TeamService teamService;
    @Autowired
    GarminService garminService;

    @Autowired
    SportService sportService;

    @Autowired
    TeamRoleService teamRoleService;

    Logger logger = LoggerFactory.getLogger(ViewActivityController.class);

    @Autowired
    RoleVerifier roleVerifier;


    /**
     * Get request for viewing an activity
     *
     * @param id    id of the activity
     * @param model thymeleaf model
     * @return view activity page of the activity
     */
    @GetMapping("/viewActivity")
    public String getViewActivityPage(@RequestParam("id") long id, Model model) throws
        NotFoundException, GarminPermissionException {
        logger.info("GET /viewActivity");
        long userId = tabUserService.getCurrentlyLoggedIn().getId();
        Activity activity = activityService.getActivityById(id);
        List<String> facts = activityService.getActivityFacts(id);
        model.addAttribute("id", id);
        if (activity != null) {
            model.addAttribute("activity", activity);
            model.addAttribute("startTimeFormatted",
                activity.getStartTime().split("T")[0] + ' '
                    + activity.getStartTime().split("T")[1]);

            model.addAttribute("endTimeFormatted",
                activity.getEndTime().split("T")[0] + ' '
                    + activity.getEndTime().split("T")[1]);
            model.addAttribute("afterStartTime",
                activityService.checkActivityStartTime(activity));
            if (activity.getTeam() != null) {
                boolean isManager =
                    roleVerifier.verifyManager(userId,
                        activity.getTeam().getId())
                        || roleVerifier.verifyCoach(userId,
                        activity.getTeam().getId(), model);
                model.addAttribute("isManager", isManager);
                model.addAttribute("afterStartTime",
                    activityService.checkActivityStartTime(activity));
            } else {
                model.addAttribute("isManager", true);
            }
            model.addAttribute("startTimeFormatted",
                activity.getStartTime().split("T")[0]
                    + ' '
                    + activity.getStartTime().split("T")[1]);
            model.addAttribute("endTimeFormatted",
                activity.getEndTime().split("T")[0]
                    + ' '
                    + activity.getEndTime().split("T")[1]);
            model.addAttribute("type", activity.getType());
            model.addAttribute("facts", facts);

            // Personal Statistics
            model.addAttribute("personalPoints",
                activityStatisticService.getPersonalScoringEventsForActivity(activity.getId(),
                    userId));
            model.addAttribute("playerTotalTime", activityStatisticService
                .getPersonalTotalTimePlayedForActivity(activity.getId(),
                    userId));
            model.addAttribute("playerID", userId);
        }
        //formations
        addFormations(model, activity);


        if (model.getAttribute(FORMATION_FORM) == null) {
            model.addAttribute(FORMATION_FORM, new FormationForm());
        }
        if (!Objects.isNull(Objects.requireNonNull(activity).getFormation())
            &&
            !Objects.isNull(formationService.getFormationByIdNotOptional(activity.getFormation()))
            && Objects.isNull(model.getAttribute(BG_IMAGE))) {
            Formation formation =
                formationService.getFormationByIdNotOptional(activity.getFormation());
            model.addAttribute(BG_IMAGE,
                ImageHelper.getFieldImageLink(formation.getField()));
            addFormationForm(formation.getFormationId(), model, String.valueOf(id));
        }

        if (Objects.isNull(model.getAttribute("lineup"))) {
            String position = activity.getPosition();
            if (position.equals("0")) {
                formationService.addLineUpToModel(null, model);
            } else {
                formationService.addLineUpToModel(activity.getPosition(), model);
            }
        }

        // Team Statistics
        model.addAttribute("subFacts",
            activityStatisticService.getStatisticSubstitution(activity.getId()));
        model.addAttribute("scoreFacts",
            activityStatisticService.getStatisticPlayerScore(activity.getId()));
        model.addAttribute("score", activityService.getGameActivity(activity.getId()));

        // Personal Statistics
        model.addAttribute("personalSubFacts",
                activityStatisticService.getPersonalStatisticSubstitution(activity.getId(),
                        tabUserService.getCurrentlyLoggedIn()));
        List<GarminActivity> garminActivities = garminService.getGarminActivities();
        model.addAttribute("connected",
                garminService.checkGarminConnected(userId));
        model.addAttribute("garminActivities", garminActivities);


        // Garmin Activity Statistics
        GarminActivity garminActivity = garminService
                .getByActivityAndUser(activity.getId(),
                        tabUserService.getCurrentlyLoggedIn().getId());

        if (garminActivity != null) {
            model.addAttribute("userGarminStatistics", garminActivity);

            int totalExerciseTime = garminActivity.getDurationInSeconds();

            int exerciseHours = (int) TimeUnit.SECONDS.toHours(totalExerciseTime);
            int exerciseMinutes = (int) TimeUnit.SECONDS.toMinutes(totalExerciseTime) % 60;
            int exerciseSeconds = totalExerciseTime % 60;

            model.addAttribute("exerciseHours", exerciseHours);
            model.addAttribute("exerciseMinutes", exerciseMinutes);
            model.addAttribute("exerciseSeconds", exerciseSeconds);
        }
        model.addAttribute("intakePreference", tabUserService.getCalorieIntakePreference(userId));
        return "viewActivity";
    }

    /**
     * Adds formations information to the model for rendering in the view.
     *
     * @param model    The Model used for adding formation-related data.
     * @param activity The Activity for which formations information is being added.
     */
    private void addFormations(Model model, Activity activity) {
        if (Objects.requireNonNull(activity).getTeam() != null) {
            List<Formation> formations =
                formationService.findAllFormationsByTeamId(activity.getTeam().getId());
            model.addAttribute("formationSize", formations.size());
            model.addAttribute("formations", formations);
        }
    }


    /**
     * Used to display the lineup when the user clicks the "Set Up" button
     *
     * @param formationId holds the formation that the user wants to use
     * @param id          activity id
     * @param model       thymeleaf model
     * @return the view activity page with the lineup displayed if valid
     */
    @PostMapping("/previewLineup")
    public String previewLineUp(@ModelAttribute(name = FORMATION_ID) String formationId,
                                @RequestParam(name = "id") String id, Model model)
        throws NotFoundException, GarminPermissionException {
        logger.info("POST /previewFormation");
        if (!formationId.equals("")) {
            Formation formation =
                formationService.getFormationByIdNotOptional(Long.parseLong(formationId));
            FormationForm formationForm = new FormationForm();
            formationForm.setFormation(formation.getFormation().split(":")[0]);
            formationForm.setFormationId(formationId);
            formationForm.setSport(formation.getField());
            String bgImage = formationService.getFormationImage(formationForm.getSport());
            model.addAttribute(BG_IMAGE_EDIT, bgImage);
            model.addAttribute(BG_IMAGE, bgImage);
            model.addAttribute("id", id);
            model.addAttribute(FORMATION_FORM, formationForm);
            model.addAttribute(FORMATION_ID, formationId);
            model.addAttribute("changedLineUp", true);
        }
        return getViewActivityPage(Long.parseLong(id), model);
    }

    /**
     * Adds a formation to a formation form
     * @param formationId Long representing the id of a form
     * @param model Map like object for thyemeleaf
     * @param id String representing an id
     * @throws NotFoundException if the formation is not found
     */
    public void addFormationForm(Long formationId, Model model, String id)
        throws NotFoundException {
        Formation formation =
            formationService.getFormationByIdNotOptional(formationId);
        FormationForm formationForm = new FormationForm();
        formationForm.setFormation(formation.getFormation().split(":")[0]);
        formationForm.setFormationId(formationId.toString());
        formationForm.setSport(formation.getField());
        String bgImage = formationService.getFormationImage(formationForm.getSport());
        model.addAttribute(BG_IMAGE_EDIT, bgImage);
        model.addAttribute("id", id);
        model.addAttribute(FORMATION_FORM, formationForm);
        model.addAttribute(FORMATION_ID, formationId);

    }


    /**
     * Post Mapping for saving a formation
     *
     * @param saveFormation position strings
     * @param id            activity's Id
     * @param model         thymeleaf model
     * @return the view activity page with the lineup displayed if valid
     */
    @PostMapping("/createLineup")
    public String createLineup(@ModelAttribute(name = FORMATION_ID) String formationId,
                               @RequestParam(name = "saveFormation") String saveFormation,
                               @RequestParam(name = "activityId") String id, Model model)
        throws NotFoundException, GarminPermissionException {
        logger.info("POST/createLineup");
        model.addAttribute("c", true);
        if (!activityService.getGameActivity(Long.parseLong(id)).getOutcome().equals(
            ActivityResult.UNDECIDED)) {
            teamRoleService.updateUsersGamesPlayed(activityService.getGameActivity(
                Long.parseLong(id)).getTeam().getId(),
                tabUserService.getCurrentlyLoggedIn().getId());
        }
        activityService.updateActivityPositionById(Long.parseLong(id), saveFormation,
            Long.parseLong(formationId));
        return getViewActivityPage(Long.parseLong(id), model);
    }


    /**
     * Get request for retrieving a list of activities
     *
     * @param model thymeleaf model
     * @return the view activities page
     */
    @GetMapping("/viewMyActivities")
    public String getMyActivitiesPage(Model model) {
        logger.info("GET /viewMyActivities");
        TabUser user = tabUserService.getCurrentlyLoggedIn();
        List<Long> teamIds = tabUserService.getTeamIdsAlphabetically(user.getId());


        List<Activity> personalActivities = activityService.searchPersonalActivity(user.getId());
        List<List<List<String>>> teamEvents = activityService.createTeamEventsFromTeamIds(teamIds,
            user);
        List<List<String>> personalEvents = activityService.createPersonalEvents(
            personalActivities);
        model.addAttribute("teamEvents", teamEvents);
        model.addAttribute("events", personalEvents);
        model.addAttribute("sports", EnumSet.range(SupportedSports.Baseball,
            SupportedSports.Volleyball));
        model.addAttribute("user", user);
        model.addAttribute("personalActivitiesSize", personalActivities.size());
        return "viewMyActivities";
    }
}
