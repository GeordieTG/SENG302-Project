package nz.ac.canterbury.seng302.tab.controller;

import static nz.ac.canterbury.seng302.tab.utility.HeaderHelper.addLoggedInAttr;
import static nz.ac.canterbury.seng302.tab.utility.TokenGenerator.isTokenExpired;

import jakarta.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.formobjects.ResetPasswordForm;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.ValidationService;
import nz.ac.canterbury.seng302.tab.utility.EmailHelper;
import nz.ac.canterbury.seng302.tab.utility.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * End Points for the Reset Password Page.
 * Accessible when the user clicks the reset password link from their email.
 * Prompts user to enter their new password, and retyped password.
 * Resets the user password after validation passes.
 */
@Controller
public class ResetPasswordController {

    /**
     * Allows us to access email sending functionality
     */
    @Autowired
    EmailHelper emailHelper;
    /**
     * Used to encrypt the new password before entering it in the database
     */
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * Allows us to access user functionality with the database
     */
    @Autowired
    TabUserService tabUserService;

    @Autowired
    private ValidationService validationservice;

    /**
     * Get end point for the reset password page
     *
     * @param token    the users unique reset password token stored in the database
     *                 to identify it is them attempting to
     *                 reset their password.
     * @param response Used to modify the 404 error
     * @param model    The thymeleaf model
     * @return The reset password thymeleaf page
     */
    @GetMapping("/resetPassword")
    public String getResetPasswordPage(@RequestParam("token") String token,
                                       HttpServletResponse response, Model model) {
        addLoggedInAttr(model);
        TabUser user = tabUserService.getUserbyResetPasswordToken(token);
        // Check token is not expired
        Timestamp expiryTime = tabUserService.getResetPasswordExpiryTime(token);
        // Remove Reset Password Attempts older than one hour
        tabUserService.removeResetPasswordAttempt(TokenGenerator.getCurrentTime());
        if (expiryTime == null || isTokenExpired(expiryTime) || user == null) {
            response.setStatus(404);
            model.addAttribute("status", response.getStatus());
            model.addAttribute("message",
                "Token missing or expired, request to reset your password again");
            return ("error");
        }
        model.addAttribute("resetPasswordForm", new ResetPasswordForm(user.getEmail(), token));
        return "resetPassword";
    }


    /**
     * Post end point for the reset password page.
     * The validation is done in JavaScript/HTML so this is only called once
     * successful
     *
     * @return The login page
     */
    @PostMapping("/resetPassword")
    public String resetPassword(@ModelAttribute ResetPasswordForm resetPasswordInfo, Model model) {
        if (validationservice.validateResetPassword(resetPasswordInfo, model)) {

            String hashedPassword = encoder.encode(resetPasswordInfo.getPassword());
            tabUserService.resetPassword(hashedPassword, resetPasswordInfo.getToken());
            emailHelper.sendmail(resetPasswordInfo.getEmail(), "Reset Password Update",
                "You have successfully reset your password!");
            return "login";
        } else {
            return "resetPassword";
        }

    }
}