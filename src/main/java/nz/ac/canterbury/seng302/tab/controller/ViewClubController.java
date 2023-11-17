package nz.ac.canterbury.seng302.tab.controller;

import nz.ac.canterbury.seng302.tab.entity.Club;
import nz.ac.canterbury.seng302.tab.service.ClubService;
import nz.ac.canterbury.seng302.tab.service.LocationService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Basic form controller, gets the profile for a particular club
 */
@Controller
public class ViewClubController {
    private static final String VIEW_CLUB = "viewClub";

    /**
     * Allows to log
     */
    Logger logger = LoggerFactory.getLogger(ViewClubController.class);

    @Autowired
    TabUserService tabUserService;

    @Autowired
    ClubService clubService;

    @Autowired
    private LocationService locationService;

    /**
     * End point for view club page
     *
     * @param id    club id
     * @param model thymeleaf model
     * @return view club html page
     */
    @GetMapping("/viewClub")
    public String viewClub(@RequestParam("id") long id, Model model) {
        logger.info("GET /viewClub");
        Club club1 = clubService.getById(id);
        model.addAttribute("club", club1);
        model.addAttribute("owner", club1.getOwner());
        model.addAttribute("isOwner",
                club1.getOwner().equals(tabUserService.getCurrentlyLoggedIn()));

        return VIEW_CLUB;
    }

}
