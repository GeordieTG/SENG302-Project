package nz.ac.canterbury.seng302.tab.controller;

import com.github.scribejava.core.model.OAuth1RequestToken;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import nz.ac.canterbury.seng302.tab.entity.enums.GarminState;
import nz.ac.canterbury.seng302.tab.service.GarminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Garmin Controller
 */
@Controller
public class GarminController {

    Logger logger = LoggerFactory.getLogger(GarminController.class);

    /**
     * Garmin Service Class
     */
    @Autowired
    GarminService garminService;

    /**
     * Initiates the process of connecting with Garmin using OAuth1 authentication.
     *
     * @param response The HttpServletResponse object to handle the response.
     * @return A redirect URL for the user to authorize the application on the Garmin site.
     * @throws IOException          If an I/O error occurs while performing operations.
     * @throws ExecutionException   If an exception occurs during the execution of a task.
     * @throws InterruptedException If a thread is interrupted while waiting for
     *                              the task to complete.
     */
    @GetMapping("/connectGarmin")
    public String connectGarmin(HttpServletResponse response)
        throws IOException, ExecutionException, InterruptedException {
        logger.info("GET /connectGarmin");

        OAuth1RequestToken requestToken = garminService.getRequestToken();

        // Create a cookie with the private request token
        Cookie privateRequestTokenCookie = new Cookie("privateRequestToken",
            requestToken.getTokenSecret());

        // Add the cookie to the browser
        response.addCookie(privateRequestTokenCookie);

        return "redirect:" + garminService.getAuthorizationUrl(requestToken);
    }

    /**
     * Handles the verification of user authorization from the Garmin site and saving
     * the access token.
     *
     * @param oauth_verifier The OAuth1 verifier obtained from the Garmin site.
     * @param oauth_token    The OAuth1 token obtained from the Garmin site.
     * @param request        The HttpServletRequest object to retrieve cookies and
     *                       request information.
     * @return A redirect URL to the user's profile page.
     * @throws RuntimeException If an exception occurs during the process of saving the access token
     */
    // CHECKSTYLE:OFF
    @GetMapping("/verifyAuth")
    public String verifyAuth(@RequestParam String oauth_verifier, String oauth_token,
                             HttpServletRequest request) {
        logger.info("GET /verifyAuth");
        // CHECKSTYLE:ON
        // Retrieve all the cookies from the browser
        Cookie[] cookies = request.getCookies();

        if ((!oauth_verifier.equals("null")) && cookies != null) {
            // Get just the private request cookie
            Arrays.stream(cookies)
                .filter(privateRequestTokenCookie ->
                    privateRequestTokenCookie.getName().equals("privateRequestToken"))
                .findFirst()
                .ifPresent(privateRequestTokenCookie -> {
                    String secretToken = privateRequestTokenCookie.getValue();

                    // Delete the private request cookie from browser after use
                    privateRequestTokenCookie.setValue(null);
                    privateRequestTokenCookie.setMaxAge(0);

                    OAuth1RequestToken requestToken =
                        new OAuth1RequestToken(oauth_token, secretToken);
                    garminService.saveAccessToken(requestToken, oauth_verifier);
                    garminService.setGarminState(GarminState.CONNECTED);
                });
        } else {
            garminService.setGarminState(GarminState.FAIL);
        }

        return "redirect:/profilePage";
    }
}
