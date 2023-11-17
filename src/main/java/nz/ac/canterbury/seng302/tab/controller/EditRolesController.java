package nz.ac.canterbury.seng302.tab.controller;

import nz.ac.canterbury.seng302.tab.service.TeamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for edit team role
 */
@Controller
public class EditRolesController {
    /**
     * Allows to log
     */
    Logger logger = LoggerFactory.getLogger(EditRolesController.class);
    /**
     * Allows access to Team entity in backend
     */
    @Autowired
    private TeamService teamService;

    /**
     * Get request for retrieving the member roles
     *
     * @param model  map-like) representation of results to be used by thymeleaf
     * @param teamId the id of the requested team
     * @return thymeleaf demoResponseTemplate
     * @throws Exception the exception could be thrown
     */
    @GetMapping("/editMemberRoles")
    public String responses(Model model, @RequestParam("id") long teamId) {
        logger.info("GET /editMemberRoles");
        return "editMemberRoles";
    }

}

