package nz.ac.canterbury.seng302.tab.controller;

import java.util.EnumSet;
import java.util.List;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.entity.enums.SupportedSports;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.TeamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller class for creating a club
 */
@Controller
public class CreateClubController {

    /**
     * Allows us to log
     */
    Logger logger = LoggerFactory.getLogger(CreateClubController.class);

    @Autowired
    TabUserService tabUserService;

    @Autowired
    TeamService teamService;

    /**
     * Get request for the createClub Thymeleaf template
     *
     * @param model Thymeleaf model
     * @return HTML for the create club page
     */
    @GetMapping("/createClub")
    public String getCreateClubForm(Model model) {
        logger.info("GET /createClub");
        model.addAttribute("sports", EnumSet.range(SupportedSports.Baseball,
            SupportedSports.Volleyball));
        model.addAttribute("owner", tabUserService.getCurrentlyLoggedIn().getId());


        //Retrieve teams managed or coached by the currently logged-in user
        try {
            List<Team> ownerTeams = teamService
                .getTeamsForClubs(tabUserService.getCurrentlyLoggedIn());

            model.addAttribute("ownerTeams", ownerTeams);
        } catch (Exception e) {
            logger.info("CREATE CLUB: owner has no teams");
        }
        return "createClub";
    }
}
