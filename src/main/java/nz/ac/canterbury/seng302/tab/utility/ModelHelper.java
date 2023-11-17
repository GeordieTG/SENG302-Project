package nz.ac.canterbury.seng302.tab.utility;

import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import org.springframework.ui.Model;

/**
 * Class to add model attributes
 */
public class ModelHelper {

    /**
     * Private constructor to hide publicly implicit one
     */
    private ModelHelper() {
    }

    /**
     * Used to add user attributes
     *
     * @param user  Entity object representing a user
     * @param model (map-like) representation of results to be used by thymeleaf
     */
    public static void addUserAttributes(TabUser user, Model model) {
        model.addAttribute("userID", user.getId());
        model.addAttribute("firstName", user.getFirstName());
        model.addAttribute("lastName", user.getLastName());
        model.addAttribute("DOB", user.getDateOfBirth());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("favouriteSport", user.getFavouriteSport());
    }

    /**
     * Used to add location attributes
     *
     * @param location Entity object representing a location
     * @param model    (map-like) representation of results to be used by thymeleaf
     */
    public static void addLocationAttributes(Location location, Model model) {
        model.addAttribute("location_line1", location.getLine1());
        model.addAttribute("location_line2", location.getLine2());
        model.addAttribute("location_suburb", location.getSuburb());
        model.addAttribute("location_city", location.getCity());
        model.addAttribute("location_country", location.getCountry());
        model.addAttribute("location_postcode", location.getPostcode());
    }
}
