package nz.ac.canterbury.seng302.tab.controller;


import static nz.ac.canterbury.seng302.tab.utility.HeaderHelper.addLoggedInAttr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javassist.NotFoundException;
import nz.ac.canterbury.seng302.tab.entity.Activity;
import nz.ac.canterbury.seng302.tab.entity.Club;
import nz.ac.canterbury.seng302.tab.entity.Formation;
import nz.ac.canterbury.seng302.tab.entity.GameActivity;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.entity.TeamRoles;
import nz.ac.canterbury.seng302.tab.service.ActivityService;
import nz.ac.canterbury.seng302.tab.service.FormationService;
import nz.ac.canterbury.seng302.tab.service.LocationService;
import nz.ac.canterbury.seng302.tab.service.RoleVerifier;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.TeamRoleService;
import nz.ac.canterbury.seng302.tab.service.TeamService;
import nz.ac.canterbury.seng302.tab.service.TeamStatsService;
import nz.ac.canterbury.seng302.tab.service.ValidationService;
import nz.ac.canterbury.seng302.tab.utility.ImageHelper;
import nz.ac.canterbury.seng302.tab.utility.ModelHelper;
import nz.ac.canterbury.seng302.tab.utility.ValidationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Basic form controller, gets the profile for a particular team
 */
@Controller
public class ViewTeamController {
    public static final String VIEW_NAME = "redirect:./viewTeams?&showAllTeams=0";
    private static final String REDIRECT_VIEW_TEAM = "redirect:./viewTeam?id=";
    private static final String VIEW_TEAM = "viewTeam";
    private static final String VIEW_TEAM_NO_EDIT = "viewTeamNoEdit";
    private static final String SHOW_ALL_TEAMS = "showAllTeams";
    private static final String IN_TEAM = "inTeam";
    private static final String TEAM_MEMBER_ROLE_LIST = "teamMemberAndRoleList";
    private static final String SHOW_ALL_TEAMS_URL = "&showAllTeams=0";
    /**
     * Allows to log
     */
    Logger logger = LoggerFactory.getLogger(ViewTeamController.class);
    @Autowired
    TabUserService tabUserService;
    /**
     * Access validation
     */
    @Autowired
    ValidationService validationService;
    /**
     * Allows us to access image functionality
     */
    private ImageHelper imageHelper = new ImageHelper();
    /**
     * Allows access to Team entity in backend
     */
    @Autowired
    private TeamService teamService;
    /**
     * Allows access to team roles entity in backend
     */
    @Autowired
    private TeamRoleService teamRoleService;
    /**
     * Allows access to activity entity in backend
     */
    @Autowired
    private ActivityService activityService;
    /**
     * Allows access to location entity in backend
     */
    @Autowired
    private LocationService locationService;
    @Autowired
    private FormationService formationService;
    @Autowired
    private TeamStatsService teamStatsService;
    /**
     * Allows access to role verifier in backend
     */
    @Autowired
    private RoleVerifier roleVerifier;
    /**
     * Id of the current team
     */
    private long teamId;

    private ViewTeamController() {
        // Private Constructor

    }

    /**
     * Gets all form responses
     *
     * @param id           the id of the requested team
     * @param showAllTeams determines which set of teams are shown, if its 0 only the user's
     *                     teams will be shown, if its 1 all the teams will be shown
     * @param model        (map-like) representation of results to be used by thymeleaf
     * @return thymeleaf demoResponseTemplate
     */
    @GetMapping("/viewTeam")
    public String responses(@RequestParam("id") long id,
                            @RequestParam(name = "tab", required = false,
                                defaultValue = "Profile") String tab,
                            @RequestParam(name = SHOW_ALL_TEAMS, required = false,
                                defaultValue = "1") int showAllTeams,
                            @RequestParam(name = "rolesChanged", required = false,
                                defaultValue = "false") boolean rolesChanged,
                            Model model) throws NotFoundException, IOException {
        logger.info("GET /viewTeam");
        teamId = id;
        if (imageHelper.getErrorMessage() != null) {
            model.addAttribute("uploadMsg", imageHelper.getErrorMessage());
        }
        TabUser user = tabUserService.getCurrentlyLoggedIn();
        imageHelper.setErrorMessage("");
        addAttributes(model, id);
        List<Activity> pages = activityService.searchTeamActivity(id);
        List<List<String>> teamEvents = activityService.createTeamEventsFromActivities(pages,
            teamId, user);
        // Done because the JS used to extract events requires List<List<List<String>>>
        List<List<List<String>>> finalEventsList = new ArrayList<>();
        finalEventsList.add(teamEvents);
        model.addAttribute("rolesChanged", rolesChanged);
        model.addAttribute("teamEvents", finalEventsList);
        addLoggedInAttr(model);

        //formations
        List<Formation> formations = formationService.findAllFormationsByTeamId(teamId);
        model.addAttribute("formationSize", formations.size());
        model.addAttribute("formations", formations);
        model.addAttribute("tab", tab);
        model.addAttribute(SHOW_ALL_TEAMS, showAllTeams);
        model.addAttribute("teamActivities", pages);
        model.addAttribute("id", id);
        model.addAttribute("inviteToken", teamService.getCurrentToken(teamId));
        Team team = teamService.getTeamById(teamId);
        model.addAttribute(IN_TEAM, teamRoleService.tabUserInTeam(team.getId(),
            tabUserService.getCurrentlyLoggedIn().getId()));
        model.addAttribute("managerOrCoach",
            teamRoleService.tabUserManagerOrCoachOfTeam(team.getId(),
                tabUserService.getCurrentlyLoggedIn().getId()));
        Club teamClub = team.getClub();
        if (teamClub != null) {
            model.addAttribute("teamClub", teamClub);
        }

        boolean isManager =
            roleVerifier.verifyManager(tabUserService.getCurrentlyLoggedIn().getId(), id);
        roleVerifier.verifyCoach(tabUserService.getCurrentlyLoggedIn().getId(), id, model);
        List<TeamRoles> teamMemberAndRoles = teamRoleService.getUsersAndRoles(teamId);
        model.addAttribute(TEAM_MEMBER_ROLE_LIST, teamMemberAndRoles);
        model.addAttribute("isManager", isManager);

        // Team Record
        model.addAttribute("teamWins", teamStatsService.getTeamWins(teamId));
        model.addAttribute("teamDraws", teamStatsService.getTeamDraws(teamId));
        model.addAttribute("teamLosses", teamStatsService.getTeamLosses(teamId));

        // Teams Total Games
        model.addAttribute("totalGames", teamStatsService.getGamePlayed(teamId).size());

        // Teams Recent Games
        model.addAttribute("recentGames", teamStatsService.getTeamRecentGames(teamId));

        // Last Five Trend
        List<GameActivity> last5 = teamStatsService.getTeamRecentGames(teamId);
        Collections.reverse(last5);
        model.addAttribute("lastFiveTrend", last5);

        // Top Five Scorers for the team
        model.addAttribute("topScorers", teamRoleService.getTeamTop5Scorers(teamId));

        // Top Five Play Time for the team
        model.addAttribute("topPlayTime", teamRoleService.getTopFivePlaytimeForTeam(teamId));

        return VIEW_TEAM;
    }

    /**
     * Generates a new token on a button press
     *
     * @param model (map-like) representation of results to be used by thymeleaf
     * @return thymeleaf page of the viewTeam
     * @throws NotFoundException throws an exception if a team isn't found with that ID
     * @throws IOException       throws an exception if a team isn't found with that ID
     */
    @PostMapping("/generate")
    public ModelAndView regenerateToken(Model model) throws NotFoundException, IOException {
        logger.info("/generate");
        addAttributes(model, teamId);
        Team team = teamService.getTeamById(teamId);

        model.addAttribute("page", 1);
        model.addAttribute(SHOW_ALL_TEAMS, 0);
        model.addAttribute("id", teamId);
        model.addAttribute("inviteToken", teamService.getCurrentToken(teamId));
        model.addAttribute("inviteTokenHidden", teamService.getCurrentToken(teamId));
        model.addAttribute(IN_TEAM, team.hasUser(tabUserService.getCurrentlyLoggedIn()));
        boolean isManager =
            roleVerifier.verifyManager(tabUserService.getCurrentlyLoggedIn().getId(), teamId);
        List<TeamRoles> teamMemberAndRoles = teamRoleService.getUsersAndRoles(teamId);
        model.addAttribute(TEAM_MEMBER_ROLE_LIST, teamMemberAndRoles);
        String token = teamService.changeToken(teamId);
        validationService.validateToken(teamId, token, model, isManager);
        model.addAttribute("generatedToken", "true");
        return new ModelAndView(REDIRECT_VIEW_TEAM + teamId + SHOW_ALL_TEAMS_URL,
            model.asMap());
    }

    /**
     * Add values to html elements
     *
     * @param model  thymeleaf model
     * @param teamId id of the team
     * @throws IOException       exception error
     * @throws NotFoundException exception error
     */
    public void addAttributes(Model model, long teamId) throws IOException, NotFoundException {
        Team team = teamService.getTeamById(teamId);
        addLoggedInAttr(model);
        Location location = locationService.getLocationByLocationId(team.getLocationId());
        model.addAttribute("team", teamService.getTeamById(teamId));
        model.addAttribute("city", location.getCity());
        model.addAttribute("country", location.getCountry());
        model.addAttribute("streetAddress", location.getStreetAddressString());
    }

    /**
     * Gets all form responses
     *
     * @param id           the id of the requested team
     * @param page         the page number of the page the user was previously on
     * @param showAllTeams determines which set of teams are shown, if its 0 only the user's
     *                     teams will be shown, if its 1 all of the teams will be shown
     * @param model        (map-like) representation of results to be used by thymeleaf
     * @return thymeleaf demoResponseTemplate
     */
    @GetMapping("/viewTeamNoEdit")
    public String viewTeamNoEdit(@RequestParam("id") long id,
                                 @RequestParam(name = "page", required = false,
                                     defaultValue = "1")
                                 int page,
                                 @RequestParam(name = SHOW_ALL_TEAMS, required = false,
                                     defaultValue = "1")
                                 int showAllTeams, Model model) throws IOException,
        NotFoundException {
        logger.info("GET /viewTeamNoEdit");
        teamId = id;
        if (imageHelper.getErrorMessage() != null) {
            model.addAttribute("uploadMsg", imageHelper.getErrorMessage());
        }
        imageHelper.setErrorMessage("");
        Team team = teamService.getTeamById(id);
        Location location = locationService.getLocationByLocationId(team.getLocationId());
        addLoggedInAttr(model);
        model.addAttribute("team", teamService.getTeamById(id));
        ModelHelper.addLocationAttributes(location, model);
        model.addAttribute("page", page);
        model.addAttribute(SHOW_ALL_TEAMS, showAllTeams);
        model.addAttribute("id", id);
        //To be replaced with check to see if user in the team
        model.addAttribute(IN_TEAM,
            teamRoleService.tabUserInTeam(teamId, tabUserService.getCurrentlyLoggedIn().getId()));
        List<TeamRoles> teamMemberAndRoles = teamRoleService.getUsersAndRoles(teamId);
        model.addAttribute(TEAM_MEMBER_ROLE_LIST, teamMemberAndRoles);
        return VIEW_TEAM_NO_EDIT;
    }

    /**
     * Gets all form responses
     *
     * @param inviteToken the team invitation token provided by the user
     * @param currentPage (Optional) the page (or url) that the user was on when
     *                    they submitted the form
     * @param model       (map-like) representation of results to be used by thymeleaf
     * @return thymeleaf redirect to view team page
     */
    @PostMapping("/joinTeam")
    public ModelAndView joinTeam(@RequestParam(name = "invitation-token") String inviteToken,
                                 @RequestParam(name = "current-page", defaultValue = "/")
                                 String currentPage, Model model) {
        //Code to validate inviteToken, and redirect to team page
        String[] currentPageSplit;
        if (currentPage.contains("/prod")) {
            currentPageSplit = currentPage.split("/prod");
        } else if (currentPage.contains("/test")) {
            currentPageSplit = currentPage.split("/test");
        } else {
            currentPageSplit = new String[] {"", currentPage};
        }

        try {
            if (!ValidationHelper.validateToken(inviteToken, model)) {
                return new ModelAndView(VIEW_NAME, model.asMap());
            }
            Team team = teamService.registerTabUserToTeam(inviteToken,
                tabUserService.getCurrentlyLoggedIn());

            if (team == null) {
                logger.info("User is already a member of this team");
                model.addAttribute("tokenErrorText", "You are already a member of this team");
                return new ModelAndView("redirect:" + currentPageSplit[1], model.asMap());
            }
            return new ModelAndView(VIEW_NAME, model.asMap());
        } catch (Exception e) {
            logger.info("Token is invalid");
            model.addAttribute("tokenErrorText", "The Invite is invalid");
        }
        return new ModelAndView(VIEW_NAME, model.asMap());
    }

    /**
     * Handler for leaving a team, returns the given team's page or
     * the all teams page if the id doesn't correspond to a team
     *
     * @param id the id of the team the user wants to leave
     * @return thymeleaf redirect to view team page or
     *         the all teams page if the id doesn't correspond to a team
     */
    @PostMapping("/leaveTeam")
    public String leaveTeam(@RequestParam(name = "id") long id) {

        try {
            Team team = teamService.getTeamById(id);
            teamService.removeTabUserFromTeam(team, tabUserService.getCurrentlyLoggedIn());
        } catch (Exception e) {
            logger.info("Team not found");
            return "redirect:./viewMyTeams";
        }
        return REDIRECT_VIEW_TEAM + id + SHOW_ALL_TEAMS_URL;
    }


}
