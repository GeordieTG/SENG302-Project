package nz.ac.canterbury.seng302.tab.controller;

import static nz.ac.canterbury.seng302.tab.utility.HeaderHelper.addLoggedInAttr;

import nz.ac.canterbury.seng302.tab.formobjects.ChangePasswordForm;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.ValidationService;
import nz.ac.canterbury.seng302.tab.utility.EmailHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller class for changing a password
 */
@Controller
public class ChangePasswordController {

    /**
     * To generate logs
     */
    Logger logger = LoggerFactory.getLogger(ChangePasswordController.class);

    /**
     * To use tab user functionalities with the database
     */
    @Autowired
    TabUserService tabUserService;

    /**
     * To use the email confirmation
     */
    @Autowired
    EmailHelper emailHelper;

    @Autowired
    ValidationService validationService;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Get handler for changing the user's password,
     * takes one optional request parameter passwordChanged
     * which determines if the user's password has changed
     *
     * @param passwordChanged Used to display a message to the user in changePassword template,
     *                        when it is 1 the old password the user supplied was correct,
     *                        if it is 2 then the old password the user supplied was incorrect.
     * @param model           (map-like) representation of name, language and
     *                        isJava boolean for use in thymeleaf
     * @return thymeleaf changePassword page
     */
    @GetMapping("/changePassword")
    public String updatePassword(
        @RequestParam(name = "passwordChanged", defaultValue = "0") int passwordChanged,
        Model model) {
        logger.info("GET /changePassword");
        addLoggedInAttr(model);
        model.addAttribute("changePasswordForm", new ChangePasswordForm());
        return "changePassword";
    }


    /**
     * Post handler for submitting a update password form, if OldPassword is not equal to
     * the user's current password, the user's password is not updated
     *
     * @param changePasswordForm Form object containing all fields in the change password form
     * @param model              (map-like) representation of name, language and
     *                           isJava boolean for use in thymeleaf
     * @return thymeleaf changePassword page
     */
    @PostMapping("/changePassword")
    public String submitUpdatedPassword(@ModelAttribute ChangePasswordForm changePasswordForm,
                                        Model model) {
        // Validate the form
        boolean validationSuccess =
            validationService.validateChangePasswordForm(changePasswordForm, model);

        // Act Accordingly
        if (!validationSuccess) {
            return "changePassword";
        } else {
            String email = tabUserService.getCurrentlyLoggedIn().getEmail();
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            tabUserService.updatePassword(tabUserService.getCurrentlyLoggedIn().getEmail(),
                encoder.encode(changePasswordForm.getPassword()));
            emailHelper.sendmail(email, "TAB Password Change", "You have updated your password!");
            return "redirect:./profilePage";
        }
    }
}
