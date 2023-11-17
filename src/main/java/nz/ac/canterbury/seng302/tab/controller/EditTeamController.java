package nz.ac.canterbury.seng302.tab.controller;

import static java.lang.Integer.parseInt;
import static nz.ac.canterbury.seng302.tab.utility.HeaderHelper.addLoggedInAttr;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import javassist.NotFoundException;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.entity.TeamRoles;
import nz.ac.canterbury.seng302.tab.entity.enums.SupportedSports;
import nz.ac.canterbury.seng302.tab.formobjects.EditTeamForm;
import nz.ac.canterbury.seng302.tab.service.LocationService;
import nz.ac.canterbury.seng302.tab.service.TeamRoleService;
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
 * Controller for edit team page
 */
@Controller
public class EditTeamController {
    /**
     * Allows us to log
     */
    Logger logger = LoggerFactory.getLogger(EditTeamController.class);

    /**
     * Allows us to access team functionality with the database
     */
    @Autowired
    private TeamService teamService;

    /**
     * Allows us to access team role functionality with the database
     */
    @Autowired
    private TeamRoleService teamRoleService;

    /**
     * Allows us to access location functionality with the database
     */
    @Autowired
    private LocationService locationService;

    @Autowired
    private ValidationService validationService;

    /**
     * Get request for retrieving the edit team
     *
     * @param id    id of the team being edited
     * @param model (map-like) representation of data to be used in thymeleaf display
     * @return thymeleaf edit team profile page
     * @throws NotFoundException Throws an exception if team id not found
     */
    @GetMapping("/editTeamProfile")
    public String editTeam(@RequestParam("id") long id,
                           @RequestParam(name = "tab", required = false, defaultValue = "Profile")
                           String tab,
                           Model model) throws NotFoundException, IOException {
        logger.info("GET /editTeam");
        Team team = teamService.getTeamById(id);


        boolean hasClub = false;
        hasClub = !Objects.isNull(team.getClub());
        if (!hasClub) {
            logger.info("Team has no Club!");
        }
        Location location = locationService.getLocationByLocationId(team.getLocationId());
        List<TeamRoles> teamRoleList = teamRoleService.getUsersAndRoles(team.getId());
        EditTeamForm editTeamForm = new EditTeamForm(team, location, teamRoleList);
        model.addAttribute("hasClub", hasClub);
        model.addAttribute("tab", tab);
        model.addAttribute("id", id);
        model.addAttribute("editTeamForm", editTeamForm);
        model.addAttribute("supportedSports",
            EnumSet.range(SupportedSports.Baseball, SupportedSports.Volleyball));
        addLoggedInAttr(model);
        return "editTeamProfile";
    }

    /**
     * Post request for the edit team
     *
     * @param editTeamForm form object with variables for all fields in the form
     * @param model        (map-like) representation of data to be used in thymeleaf display
     * @return Thymeleaf team page if edit is successful, Thymeleaf edit page if invalid
     * @throws Exception throws an exception if team id not found
     */
    @PostMapping("/editTeamProfile")
    public String updateTeamDetail(@ModelAttribute EditTeamForm editTeamForm, Model model)
        throws NotFoundException, IOException {
        logger.info("POST /editTeamProfile");
        model.addAttribute("id", editTeamForm.getId());
        //Validate and set image
        //Validate name, sport and location fields
        boolean validTeamDetails = validationService.validateEditTeamForm(editTeamForm, model);

        //Validation Results
        if (validTeamDetails) {
            Team team = teamService.getTeamById(Long.parseLong(editTeamForm.getId()));
            List<TeamRoles> teamRolesList = editTeamForm.getTeamRoles();
            List<TeamRoles> teamRolesActual =
                teamRoleService.getUsersAndRoles(Long.parseLong(editTeamForm.getId()));
            int i = 0;
            // This updates all the roles in the db, but is not a great way to do it.
            // Unsure how to make this better
            while (i < teamRolesActual.size()) {
                teamRolesActual.get(i).setRole(teamRolesList.get(i).getRole());
                teamRoleService.updateTeamRole(teamRolesActual.get(i));
                i++;
            }

            locationService.updateOptionalInDatabase(team.getLocationId(),
                editTeamForm.getAddress1(), editTeamForm.getAddress2(), editTeamForm.getSuburb(),
                editTeamForm.getPostcode());
            locationService.updateMandatoryInDatabase(team.getLocationId(), editTeamForm.getCity(),
                editTeamForm.getCountry());
            teamService.updateTeamInDatabase(editTeamForm.getId(), editTeamForm.getName(),
                editTeamForm.getSport(), editTeamForm.getCity(), editTeamForm.getCountry());
            return "redirect:./viewTeam?id=" + parseInt(editTeamForm.getId())
                + "&showAllTeams=0&rolesChanged=true";
        } else {
            model.addAttribute("team", teamService.getTeamById(parseInt(editTeamForm.getId())));
            Team team = teamService.getTeamById(parseInt(editTeamForm.getId()));
            List<TeamRoles> teamRoleList = teamRoleService.getUsersAndRoles(team.getId());
            editTeamForm.setTeamRoles(teamRoleList);
            model.addAttribute("editTeamForm", editTeamForm);
            model.addAttribute("supportedSports",
                EnumSet.range(SupportedSports.Baseball, SupportedSports.Volleyball));
            return "editTeamProfile";
        }
    }
}
