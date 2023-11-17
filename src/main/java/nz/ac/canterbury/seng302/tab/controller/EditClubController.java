package nz.ac.canterbury.seng302.tab.controller;

import java.util.EnumSet;
import java.util.List;
import nz.ac.canterbury.seng302.tab.entity.Club;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.entity.enums.SupportedSports;
import nz.ac.canterbury.seng302.tab.service.ClubService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.TeamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Controller for edit club
 */
@Controller
public class EditClubController {

    Logger logger = LoggerFactory.getLogger(EditClubController.class);

    @Autowired
    ClubService clubService;

    @Autowired
    TabUserService tabUserService;

    @Autowired
    TeamService teamService;

    /**
     * Get request for retrieving the club
     *
     * @param id    id of the club
     * @param model thymeleaf model
     * @return the edit club page
     */
    @GetMapping("/editClub")
    public String getClub(@RequestParam("id") String id, Model model) {

        logger.info("GET /editClub");

        Club club = clubService.getById(Long.parseLong(id));
        model.addAttribute("club", club);
        model.addAttribute("sports", EnumSet.range(SupportedSports.Baseball,
            SupportedSports.Volleyball));
        model.addAttribute("owner", tabUserService.getCurrentlyLoggedIn().getId());

        //Retrieve teams managed or coached by the currently logged-in user
        try {
            List<Team> ownerTeams = teamService
                .getTeamsForEditClub(tabUserService.getCurrentlyLoggedIn(), club);
            model.addAttribute("ownerTeams", ownerTeams);
        } catch (Exception e) {
            logger.info("CREATE CLUB: owner has no teams");
        }
        if (tabUserService.getCurrentlyLoggedIn() != club.getOwner()) {
            return "redirect:/viewClub?id=" + club.getId();
        }

        return "editClub";
    }
}
