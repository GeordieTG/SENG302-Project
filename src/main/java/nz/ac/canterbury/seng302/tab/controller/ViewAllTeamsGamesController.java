package nz.ac.canterbury.seng302.tab.controller;

import nz.ac.canterbury.seng302.tab.service.TeamStatsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Basic controller associated with the viewAllTeamsGames page.
 * Accessed off of the teams page when viewing aggregated
 * statistics.
 */
@Controller
public class ViewAllTeamsGamesController {

    Logger logger = LoggerFactory.getLogger(ViewAllTeamsGamesController.class);

    @Autowired
    TeamStatsService teamStatsService;

    /**
     * Basic Get Request of the page
     *
     * @return viewAllTeamsGames html
     */
    @GetMapping("/viewAllTeamsGames")
    public String getCreateActivityForm(@ModelAttribute("teamId") long teamId, Model model) {
        logger.info("GET /viewAllTeamsGames");

        model.addAttribute("allGames", teamStatsService.getTeamActivities(teamId));
        model.addAttribute("allOtherGame", teamStatsService.getTeamOtherActivities(teamId));

        return "viewAllTeamsGames";
    }
}
