package nz.ac.canterbury.seng302.tab.controller;


import static nz.ac.canterbury.seng302.tab.utility.HeaderHelper.addLoggedInAttr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller class for logging in
 */
@Controller
public class LoginController {

    //slf4j logger literally logs what has been logged by the class
    Logger logger = LoggerFactory.getLogger(LoginController.class);

    /**
     * Gets the thymeleaf page representing the /login page (a basic login screen)
     *
     * @return thymeleaf login
     */
    @GetMapping("/login") //this is the URL ending. i.e. localhost:8080/login
    public String login(Model model) {
        logger.info("GET /login");
        addLoggedInAttr(model);
        return "login";
    }
}
