package nz.ac.canterbury.seng302.tab.controller;


import static nz.ac.canterbury.seng302.tab.utility.HeaderHelper.addLoggedInAttr;

import java.util.EnumSet;
import javassist.NotFoundException;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.entity.enums.SupportedSports;
import nz.ac.canterbury.seng302.tab.formobjects.CreateTeamForm;
import nz.ac.canterbury.seng302.tab.service.LocationService;
import nz.ac.canterbury.seng302.tab.service.TeamService;
import nz.ac.canterbury.seng302.tab.service.ValidationService;
import nz.ac.canterbury.seng302.tab.utility.ModelHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Controller class for create team page
 */
@Controller
public class CreateTeamController {
    /**
     * Allows us to log
     */
    Logger logger = LoggerFactory.getLogger(CreateTeamController.class);

    /**
     * perform input validation text
     */
    @Autowired
    private ValidationService validationService;


    /**
     * Allows us to access team functionality with the database
     */
    @Autowired
    private TeamService teamService;

    /**
     * to save the location in the database
     */
    @Autowired
    private LocationService locationService;


    /**
     * Gets the thymeleaf page representing the /createTeam page (a page where the
     * user fills out a team registration form)
     *
     * @param model (map-like) representation of data to be used in thymeleaf display
     * @return thymeleaf createTeam
     */
    @GetMapping("/createTeam")
    public String getTemplate(Model model) {
        logger.info("GET /createTeam");
        addLoggedInAttr(model);
        model.addAttribute("location_index", 1);
        model.addAttribute("createTeamForm", new CreateTeamForm());
        model.addAttribute("supportedSports",
            EnumSet.range(SupportedSports.Baseball, SupportedSports.Volleyball));
        return "createTeam";
    }

    /**
     * Post request for creating a team
     *
     * @param model (map-like) representation of data to be used in thymeleaf display
     * @return the same page if validation fails, else return the team profile page
     */
    @PostMapping("/createTeam")
    public String submitForm(@ModelAttribute CreateTeamForm createTeamForm, Model model)
        throws NotFoundException {
        logger.info("POST /createTeam");
        boolean validTeamDetails = validationService.validateCreateTeamForm(createTeamForm, model);
        // Validate user entered data
        if (validTeamDetails) {
            Team team = teamService.createTeamInDatabase(createTeamForm.getName(),
                createTeamForm.getSport().toString(), createTeamForm.getCity(),
                createTeamForm.getCountry(),
                "images/default_team.png");
            locationService.updateOptionalInDatabase(team.getLocationId(),
                createTeamForm.getAddress1(), createTeamForm.getAddress2(),
                createTeamForm.getSuburb(), createTeamForm.getPostcode());
            model.addAttribute("validCity", true);
            model.addAttribute("validCountry", true);
            return "redirect:/viewTeam?id=" + team.getId() + "&showAllTeams=0";
        } else {
            Location location = new Location(createTeamForm.getCity(), createTeamForm.getCountry());
            location.setLine1(createTeamForm.getAddress1());
            location.setLine2(createTeamForm.getAddress2());
            location.setSuburb(createTeamForm.getSuburb());
            location.setPostcode(createTeamForm.getPostcode());
            addAttributes(createTeamForm.getName(), createTeamForm.getSport().toString(),
                location, model);
            return "createTeam";
        }
    }

    /**
     * Adds attributes to the Model object for use in a view, including the first name,
     * last name, email, date of birth, and emailExists status.
     *
     * @param name     the name of the team
     * @param sport    the sport the team plays
     * @param location location object containing necessary attributes
     * @param model    the Model object used to add attributes to the view
     */
    public void addAttributes(String name, String sport, Location location,
                              Model model) {

        model.addAttribute("name", name);
        model.addAttribute("sport", sport);
        ModelHelper.addLocationAttributes(location, model);
        model.addAttribute("supportedSports",
            EnumSet.range(SupportedSports.Baseball, SupportedSports.Volleyball));
    }
}
