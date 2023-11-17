package nz.ac.canterbury.seng302.tab.utility;

import java.util.Objects;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 * Handles logged in functionality
 */
@Component
public class HeaderHelper {

    private HeaderHelper() {
    }

    /**
     * Adds whether logged in or not to the model.
     *
     * @param model Current model being added to.
     * @return model with added logged in attribute.
     */
    public static Model addLoggedInAttr(Model model) {
        // Check if currently logged in and add attribute tp change header.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            model.addAttribute("loggedIn", !authentication.getAuthorities().isEmpty()
                && Objects.equals(authentication.getAuthorities().iterator().next().toString(),
                "ROLE_USER"));
            return model;
        }
        return model;
    }
}
