package nz.ac.canterbury.seng302.tab.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.ui.Model;

/**
 * Class to undertake user input validation
 */
public class ValidationHelper {

    private static final String NAME_REGEX = "^[a-zA-Z{}. ]{1,30}$";

    private ValidationHelper() {
    }

    /**
     * Validates team details. Used when creating or editing a team
     *
     * @param model   model
     * @param name    updated team name
     * @param sport   updated team sport
     * @param city    updated team city location
     * @param country updated team country location
     * @return true if valid, false if not
     */
    public static boolean validateBasicTeamDetails(Model model, String name, String sport,
                                                   String city, String country) {
        boolean validity = true;
        if (!validateName(name)) {
            model.addAttribute("validName", false);
            validity = false;
        }
        if (!validateOnlyLetters(sport)) {
            model.addAttribute("validSport", false);
            validity = false;
        }
        if (!validateCity(city)) {
            model.addAttribute("validCity", false);
            validity = false;
        }
        if (!validateCountry(country)) {
            model.addAttribute("validCountry", false);
            validity = false;
        }

        return validity;
    }

    /**
     * Validates the users input for team name
     *
     * @param text the users inputted text
     * @return true if valid, false if not
     */
    public static boolean validateName(String text) {
        return text.matches(NAME_REGEX);
    }

    /**
     * Validation function for city names.
     * Accounts for whitespaces
     *
     * @param cityString string of city name
     * @return true if valid
     */
    public static boolean validateCity(String cityString) {
        return cityString.matches(NAME_REGEX);
    }


    /**
     * Validation function for country names.
     * Accounts for whitespaces
     *
     * @param countryString string of country name
     * @return true if valid
     */
    public static boolean validateCountry(String countryString) {
        return countryString.matches(NAME_REGEX);
    }

    /**
     * Validates the users input for the sport and location fields
     *
     * @param text the users inputted text
     * @return true if valid, false if not
     */
    public static boolean validateOnlyLetters(String text) {
        return text.matches("[a-zA-Z]+");
    }

    /**
     * check the token is alphanumeric string with no white space and exactly 12 character long
     *
     * @param token the string token
     * @param model model
     * @return boolean value, true if token is valid, false otherwise
     */
    public static boolean isAlphaNumeric(String token, Model model) {
        String tokenRegex = "^\\w{12}$";
        Pattern pattern = Pattern.compile(tokenRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcherFirst = pattern.matcher(token);
        boolean matchFoundToken = matcherFirst.find();
        if (!matchFoundToken) {
            model.addAttribute("validToken", false);
            model.addAttribute("tokenErrorText", "Please enter a valid token");
        } else {
            model.addAttribute("validToken", true);
        }
        return matchFoundToken;
    }

    /**
     * validate the token using the isAlphaNumeric function
     *
     * @param token the string token to check
     * @param model model
     * @return boolean value, true if token is valid, false otherwise
     */
    public static boolean validateToken(String token, Model model) {
        return isAlphaNumeric(token, model);
    }

}
