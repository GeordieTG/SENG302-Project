package nz.ac.canterbury.seng302.tab.controller;

import static nz.ac.canterbury.seng302.tab.utility.HeaderHelper.addLoggedInAttr;
import static nz.ac.canterbury.seng302.tab.utility.TokenGenerator.getExpiryTimeOneHour;

import java.sql.Timestamp;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.utility.EmailHelper;
import nz.ac.canterbury.seng302.tab.utility.TokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * End Points for the Lost Password Page. Accessible off the login page.
 * Prompts user to enter an email address so that
 * an email can be sent with a link to the reset password page.
 */
@Controller
public class LostPasswordController {

    /**
     * To Log
     */
    private final Logger logger = LoggerFactory.getLogger(LostPasswordController.class);

    /**
     * Allows us to access user functionality with the database
     */
    @Autowired
    TabUserService tabUserService;
    /**
     * Allows us to access email sending functionality
     */
    @Autowired
    EmailHelper emailHelper;

    /**
     * Get End Point for the lost password page
     *
     * @return the lost password page
     */
    @GetMapping("/lostPassword") //this is the URL ending. i.e. localhost:8080/login
    public String getLostPassword(Model model) {
        addLoggedInAttr(model);
        return "lostPassword";
    }


    /**
     * Post End Point for the lost password page
     *
     * @param email users email
     * @param url   the url of the website obtained by the JavaScript code.
     *              Used to determine the environment.
     * @param model Thymeleaf model
     * @return the same page
     */
    @PostMapping("/lostPassword")
    public String sendEmail(@RequestParam(name = "email") String email,
                            @RequestParam(name = "url") String url, Model model) {
        logger.info("POST /lostPassword");
        addLoggedInAttr(model);
        // Send link depending on whether on which url the website is being run on
        String urlStart = EmailHelper.getUrl(url);
        String endPoint = "resetPassword?token=";
        // Check email is registered with TAB
        if (tabUserService.getByEmail(email)) {
            String token = TokenGenerator.genToken();
            Timestamp expiryTimeStamp = getExpiryTimeOneHour();
            tabUserService.setResetPasswordDetails(email, token, expiryTimeStamp);
            emailHelper.sendmail(email, "Reset Password", urlStart + endPoint + token);
        } else {
            logger.info("Email not registered");
        }
        model.addAttribute("confirmationMessage",
            "Email was sent to the address if it was recognised");
        return "lostPassword";
    }
}
