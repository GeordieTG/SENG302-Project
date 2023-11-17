package nz.ac.canterbury.seng302.tab.service;

import static com.fasterxml.jackson.core.io.NumberInput.parseInt;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import nz.ac.canterbury.seng302.tab.entity.Activity;
import nz.ac.canterbury.seng302.tab.entity.GameActivity;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.entity.datatransferobjects.ClubDto;
import nz.ac.canterbury.seng302.tab.entity.enums.ActivityResult;
import nz.ac.canterbury.seng302.tab.entity.enums.SupportedSports;
import nz.ac.canterbury.seng302.tab.entity.garminentities.GarminActivity;
import nz.ac.canterbury.seng302.tab.formobjects.ActivityForm;
import nz.ac.canterbury.seng302.tab.formobjects.ChangePasswordForm;
import nz.ac.canterbury.seng302.tab.formobjects.CreateTeamForm;
import nz.ac.canterbury.seng302.tab.formobjects.EditTeamForm;
import nz.ac.canterbury.seng302.tab.formobjects.ExerciseGoalsForm;
import nz.ac.canterbury.seng302.tab.formobjects.FactForm;
import nz.ac.canterbury.seng302.tab.formobjects.PlayerScoreForm;
import nz.ac.canterbury.seng302.tab.formobjects.ResetPasswordForm;
import nz.ac.canterbury.seng302.tab.formobjects.ScoreForm;
import nz.ac.canterbury.seng302.tab.formobjects.StatisticsForm;
import nz.ac.canterbury.seng302.tab.formobjects.SubstitutionForm;
import nz.ac.canterbury.seng302.tab.formobjects.UserEditProfilePageForm;
import nz.ac.canterbury.seng302.tab.formobjects.UserRegistrationForm;
import nz.ac.canterbury.seng302.tab.utility.ConvertingUtil;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

/**
 * Functionality for all our validation
 */
@Service
public class ValidationService {

    public static final String INVALID_LAST_NAME = "invalidLastName";
    public static final String INVALID_CHARACTERS = "Please do not include invalid "
        + "characters e.g.!@#$";
    public static final String MATCH_PASSWORDS = "matchPasswords";
    private static final String INVALID_FIRST_NAME = "invalidFirstName";
    private static final String INVALID_DOB = "invalidDOB";
    private static final String ERROR_DOB = "errorDOBMessage";
    private static final String VALID_CITY = "validCity";
    private static final String VALID_COUNTRY = "validCountry";
    private static final String VALID_SPORT = "validSport";
    private static final String INVALID_TIME = "Time must be positive";

    static List<String> invalidTabList = List.of("invalidFirstTab", "invalidSecondTab",
        "invalidThirdTab", "invalidTeamSecondTab");
    @Autowired
    TeamService teamService;

    @Autowired
    ActivityService activityService;

    String nameRegex = "^([a-zA-Zあ-ヿ㐀-䶿一-鿿豈-﫿ｦ-ﾟ가-힣Ā-ſ]{1,30}[\\s]{0,1})+$";

    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:.[a-zA-Z0-9_+&*-]+)"
        + "*@(?:[a-zA-Z0-9-]+[.])+[a-zA-Z]{2,7}$";
    String formationRegex = "^[0-8]([-][0-8])*$";

    String cityRegex = "^(?=.{1,85}$)[\\p{L}]+(?:[\\s-][\\p{L}]+)*$";

    String countryRegex = "^(?=.{1,56}$)[\\p{L}]+(?:[\\s-'][\\p{L}]+)*$";

    String streetRegex = "^[a-zA-Z0-9.&-:' '/]{0,100}$";

    String suburbRegex = "^([a-z-A-Z][' ']{0,1}){0,30}$";

    String postcodeRegex = "^[a-zA-Z0-9-]{0,15}$";

    String blankRegex = "^[' ']*$";

    @Autowired
    private TabUserService tabUserService;

    /**
     * Validates the users input for team name
     *
     * @param text the users inputted text
     * @return true if valid, false if not
     */
    public static boolean validateName(String text, Model model) {
        String pattern = "^[a-zA-Z{}. ]{1,30}$";
        if (!text.matches(pattern)) {
            model.addAttribute("validName", false);
            model.addAttribute("teamNameErrorMessage",
                "Team Name must contain only letters, or the following "
                    + "special characters '{', '}', '.'");
            model.addAttribute(invalidTabList.get(0), true);
            return false;
        }
        return true;
    }

    /**
     * Validates the users input for the sport and location fields
     *
     * @param text the users inputted text
     * @return true if valid, false if not
     */
    public static boolean validateOnlyLetters(String text, Model model) {
        if (!text.matches("[a-zA-Z]+")) {
            model.addAttribute("teamSportErrorMessage", "Sport field must contain only letters!");
            model.addAttribute(invalidTabList.get(0), true);
        }
        return text.matches("[a-zA-Z]+");
    }

    /**
     * Validates the location fields of an ActivityForm.
     *
     * @param createActivityForm The ActivityForm containing location information to be validated.
     * @param model              The Model used for adding error messages.
     * @param validForm          The current validation status of the form.
     * @return The updated validation status of the form after location validation.
     */
    private static boolean validateLocation(ActivityForm createActivityForm,
                                            Model model, boolean validForm) {
        if (createActivityForm.getAddress1().isEmpty()) {
            validForm = false;
            model.addAttribute("validAddress", false);
        }

        if (createActivityForm.getPostcode().isEmpty()) {
            validForm = false;
            model.addAttribute("validPostcode", false);
        }

        if (createActivityForm.getCity().isEmpty()) {
            validForm = false;
            model.addAttribute(VALID_CITY, false);
        }

        if (createActivityForm.getCountry().isEmpty()) {
            validForm = false;
            model.addAttribute(VALID_COUNTRY, false);
        }
        return validForm;
    }

    /**
     * Validates a single score form for an activity
     *
     * @param validForm the current validity of all the forms
     * @param count     the index of the score form
     * @param duration  the duration of the activity
     * @param errors    the list of all the current errors
     * @param scoreForm the score for to validate
     * @return true if valid, false otherwise
     */
    private static boolean isValidScoreForm(boolean validForm, int count, long duration,
                                            List<Pair<Integer, String>> errors,
                                            PlayerScoreForm scoreForm) {
        // Checks if a player has been selected
        if (Objects.equals(scoreForm.getScoredPlayerId(), -1L)) {
            errors.add(new Pair<>(count, "Please select a player"));
            validForm = false;
        }
        // Checks if the score is negative
        if (scoreForm.getScore() < 1) {
            errors.add(new Pair<>(count, "Score must be positive"));
            validForm = false;
        }
        // Checks if the time is negative
        if (scoreForm.getScoreTime() <= 0) {
            errors.add(new Pair<>(count, INVALID_TIME));
            validForm = false;
        }
        // Checks if the time is greater than the activity
        if (scoreForm.getScoreTime() > duration) {
            errors.add(new Pair<>(count, INVALID_TIME));
            validForm = false;
        }
        return validForm;
    }

    /**
     * Checks if the given first name and last name contain any invalid characters.
     *
     * @param firstName the first name to be checked
     * @param lastName  the last name to be checked
     * @param model     the Model object to add attributes to in case of invalid names
     * @return true if the first and last names contain no invalid characters, false otherwise
     */
    public boolean checkNames(String firstName, String lastName, Model model) {
        boolean matchFoundFirst = firstName.matches(nameRegex);
        boolean matchFoundLast = lastName.matches(nameRegex);
        model.addAttribute(INVALID_FIRST_NAME, false);
        model.addAttribute(INVALID_LAST_NAME, false);
        if (!matchFoundFirst && !matchFoundLast) {
            model.addAttribute(INVALID_FIRST_NAME, true);
            model.addAttribute("invalidFirstNameErrorMessage", "Please enter a valid first name!");
            model.addAttribute(INVALID_LAST_NAME, true);
            model.addAttribute("invalidLastNameErrorMessage", "Please enter a valid last name!");
            model.addAttribute(invalidTabList.get(1), true);

            return false;
        }
        if (!matchFoundFirst) {
            model.addAttribute(INVALID_FIRST_NAME, true);
            model.addAttribute("invalidFirstNameErrorMessage", "Please enter a valid first name!");
            model.addAttribute(invalidTabList.get(1), true);
            return false;
        }
        if (!matchFoundLast) {
            model.addAttribute(INVALID_LAST_NAME, true);
            model.addAttribute("invalidLastNameErrorMessage", "Please enter a valid last name!");
            model.addAttribute(invalidTabList.get(1), true);
            return false;
        }
        return true;
    }

    /**
     * Validates a user entered password is strong enough to be used
     *
     * @param password requested new password
     * @return True if valid, false if not
     */
    public boolean validateStrongPassword(String password) {
        String strongPassword =
            "(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*=_+?><~`])"
                + "[a-zA-Z-0-9!@#$%^&*=_+?><~`]{8,}";
        return password.matches(strongPassword);
    }

    /**
     * Checks if the size of the uploaded profile picture file is within the allowed limit.
     *
     * @param profilePicture the MultipartFile object representing the profile picture file
     * @param model          the Model object used to add attributes to the view
     * @return true if the size of the file is greater than 10MB, false otherwise
     */
    public boolean checkFileSize(MultipartFile profilePicture, Model model) {
        if (profilePicture.getSize() <= 10000000) {
            model.addAttribute("invalidFileSize", false);
            return false;
        }
        model.addAttribute("invalidFileSize", true);
        return true;
    }

    /**
     * Checks if the new email is valid
     *
     * @param email the new email for the user
     * @param model (map-like) representation of name, language and isJava
     *              boolean for use in thymeleaf
     * @return boolean true if the email is valid, false otherwise
     */
    public boolean checkEmail(String email, Model model) {
        boolean matchFound = email.matches(emailRegex);
        if (!matchFound) {
            model.addAttribute("invalidEmail", true);
            model.addAttribute("emailErrorMessage", "Please enter a valid email.");
            model.addAttribute(invalidTabList.get(1), true);
        }
        return matchFound;
    }

    /**
     * Checks if the new date of birth is valid
     *
     * @param dob   the new date of birth for the user
     * @param model (map-like) representation of name, language and
     *              isJava boolean for use in thymeleaf
     * @return boolean true if the edate of birth is valid, false if otherwise
     */
    public boolean checkAge(String dob, Model model) {
        List<String> seperated = List.of(dob.split("-"));
        if (seperated.size() < 3) {
            model.addAttribute(INVALID_DOB, true);
            model.addAttribute(ERROR_DOB, "Please enter a valid date");
            model.addAttribute(invalidTabList.get(1), true);
            return false;
        }
        int year;
        int month;
        int day;
        try {
            year = LocalDate.now().getYear() - parseInt(seperated.get(0));
            month = LocalDate.now().getMonthValue() - parseInt(seperated.get(1));
            day = LocalDate.now().getDayOfMonth() - parseInt(seperated.get(2));
        } catch (NumberFormatException e) {
            model.addAttribute(INVALID_DOB, true);
            model.addAttribute(ERROR_DOB, "Please enter a valid date");
            model.addAttribute(invalidTabList.get(1), true);
            return false;
        }

        if (year > 13 && year <= 130) {
            return true;
        }
        if (year == 13) {
            if (month > 0 || (month == 0 && day >= 0)) {
                return true;
            }
        } else {
            model.addAttribute(INVALID_DOB, true);
            model.addAttribute(ERROR_DOB,
                "You must be between the ages of 13 and 130 to register.");
            model.addAttribute(invalidTabList.get(1), true);
        }
        model.addAttribute(INVALID_DOB, true);
        model.addAttribute(ERROR_DOB,
            "You must be between the ages of 13 and 130 to register.");
        model.addAttribute(invalidTabList.get(1), true);
        return false;
    }

    /**
     * The input validation check of the city text box
     *
     * @param city  the input of the city text box is checking valid input
     * @param model (map-like) representation of name, language and
     *              isJava boolean for use in thymeleaf
     * @return true if the city input is valid otherwise false
     */
    public boolean checkCity(String city, Model model) {
        boolean matchFoundCity = city.matches(cityRegex);
        if (!matchFoundCity) {
            model.addAttribute(VALID_CITY, false);
            model.addAttribute("cityErrorText", "Please enter a valid city name");
            model.addAttribute(invalidTabList.get(2), true);
            model.addAttribute(invalidTabList.get(3), true);
        } else {
            model.addAttribute(VALID_CITY, true);
        }
        return matchFoundCity;
    }

    /**
     * The input validation check of the country box
     *
     * @param country the country input is check for the alphabat input only
     * @param model   (map-like) representation of name, language and
     *                isJava boolean for use in thymeleaf
     * @return true if the country input is valid otherwise false
     */
    public boolean checkCountry(String country, Model model) {
        boolean matchFoundCountry = country.matches(countryRegex);
        if (!matchFoundCountry) {
            model.addAttribute(VALID_COUNTRY, false);
            model.addAttribute("countryErrorText", "Please enter a valid country name");
            model.addAttribute(invalidTabList.get(2), true);
            model.addAttribute(invalidTabList.get(3), true);
        } else {
            model.addAttribute(VALID_COUNTRY, true);
        }
        return matchFoundCountry;
    }

    /**
     * Input validation check for street address entry.
     * Checks for alphanumeric
     *
     * @param streetAddressOne String input of street address
     * @param streetAddressTwo String input of street address
     * @param model            (map-like) representation of name, language and
     *                         isJava boolean for use in thymeleaf
     * @return true if the street address input is valid otherwise false
     */
    public boolean checkStreetAddress(String streetAddressOne, String streetAddressTwo,
                                      Model model) {
        boolean matchFoundStreetAddressOne = streetAddressOne.matches(streetRegex);
        boolean matchFoundStreetAddressTwo = streetAddressTwo.matches(streetRegex);
        if (matchFoundStreetAddressOne) {
            model.addAttribute("invalidStreetAddressOne", false);
        } else {
            model.addAttribute("invalidStreetAddressOne", true);
            model.addAttribute("streetAddressOneErrorMessage",
                INVALID_CHARACTERS);
        }
        if (matchFoundStreetAddressTwo) {
            model.addAttribute("invalidStreetAddressTwo", false);
        } else {
            model.addAttribute("invalidStreetAddressTwo", true);
            model.addAttribute("streetAddressTwoErrorMessage",
                INVALID_CHARACTERS);
        }
        model.addAttribute(invalidTabList.get(2),
            !(matchFoundStreetAddressOne && matchFoundStreetAddressTwo));
        model.addAttribute(invalidTabList.get(3),
            !(matchFoundStreetAddressOne && matchFoundStreetAddressTwo));
        return matchFoundStreetAddressOne && matchFoundStreetAddressTwo;
    }

    /**
     * Input validation check for postcode address entry
     * Checks for alphanumeric
     *
     * @param postcode String input of postcode
     * @param model    (map-like) representation of name, language and
     *                 isJava boolean for use in thymeleaf
     * @return true if the postcode is valid, otherwise false
     */
    public boolean checkPostcode(String postcode, Model model) {
        Pattern pattern = Pattern.compile(postcodeRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcherPostcode = pattern.matcher(postcode);
        boolean matchFoundPostcode = matcherPostcode.find();
        if (matchFoundPostcode) {
            model.addAttribute("invalidPostcode", false);
        } else {
            model.addAttribute("invalidPostcode", true);
            model.addAttribute("postcodeErrorMessage",
                "Please enter a postcode consisting of only numbers and letters");
            model.addAttribute(invalidTabList.get(2), true);
            model.addAttribute(invalidTabList.get(1), true);
            model.addAttribute(invalidTabList.get(3), true);

        }
        return matchFoundPostcode;
    }

    /**
     * The input validation check of the country box
     *
     * @param password        the password input
     * @param confirmPassword the confirm password input
     * @param model           (map-like) representation of name, language and
     *                        isJava boolean for use in thymeleaf
     * @return true if the country input is valid otherwise false
     */
    public boolean matchPasswords(String password, String confirmPassword, Model model) {
        boolean matchPasswords = false;

        if (password.equals(confirmPassword)) {
            matchPasswords = true;
            model.addAttribute(MATCH_PASSWORDS, false);
        } else {
            model.addAttribute(MATCH_PASSWORDS, true);
            model.addAttribute("matchPasswordsMessage", "Passwords do not match");
        }
        return matchPasswords;
    }

    /**
     * Compares a plaintext password entry to the users hashed password
     *
     * @param plainTextPassword the users inputted password
     * @param hashedPassword    the users password that's stored in the DB
     * @return True if equal, false if not
     */
    public boolean validatePassword(String plainTextPassword, String hashedPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return encoder.matches(plainTextPassword, hashedPassword);
    }

    /**
     * Complete error checking for ChangePasswordForm
     *
     * @param changePasswordForm the object returned from the change password form
     *                           containing all fields the user filled out
     * @param model              the thymeleaf model
     * @return True if valid, false if not
     */
    public boolean validateChangePasswordForm(ChangePasswordForm changePasswordForm, Model model) {

        boolean validationSuccess = true;
        boolean currentPasswordMatches = validatePassword(changePasswordForm.getOldPassword(),
            tabUserService.getCurrentlyLoggedIn().getPassword());

        if (!currentPasswordMatches) {
            model.addAttribute("invalidOldPassword", true);
            validationSuccess = false;
        }
        if (!validateStrongPassword(changePasswordForm.getPassword())) {
            model.addAttribute("weakPassword", true);
            validationSuccess = false;
        }
        if (!changePasswordForm.getPassword().equals(changePasswordForm.getConfirmPassword())) {
            model.addAttribute("passwordsDoNotMatch", true);
            validationSuccess = false;
        }
        return validationSuccess;
    }

    /**
     * Input validation check for street address entry.
     * Checks for alphanumeric
     *
     * @param streetAddress String input of street address
     * @param model         (map-like) representation of name, language and
     *                      isJava boolean for use in thymeleaf
     * @return true if the street address input is valid otherwise false
     */
    public boolean checkSuburb(String streetAddress, Model model) {
        Pattern pattern = Pattern.compile(suburbRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcherStreetAddress = pattern.matcher(streetAddress);
        boolean matchFoundStreetAddress = matcherStreetAddress.find();
        if (matchFoundStreetAddress) {
            model.addAttribute("invalidSuburb", false);
        } else {
            model.addAttribute("invalidSuburb", true);
            model.addAttribute("suburbErrorMessage",
                INVALID_CHARACTERS);
            model.addAttribute(invalidTabList.get(2), true);
            model.addAttribute(invalidTabList.get(1), true);
            model.addAttribute(invalidTabList.get(3), true);
        }
        return matchFoundStreetAddress;
    }

    /**
     * Check that the email exists withing the database
     *
     * @param newEmail new email
     * @param oldEmail old email
     * @param model    thymeleaf model
     * @return true if email doesn't exist and false if email exists
     */
    public boolean checkEmailDoesntExist(String newEmail, String oldEmail, Model model) {
        boolean emailExists = tabUserService.getByEmail(newEmail);
        if (emailExists && !newEmail.equals(oldEmail)) {
            model.addAttribute("invalidEmail", true);
            model.addAttribute("emailErrorMessage",
                "This email is already registered to an account.");
            model.addAttribute(invalidTabList.get(1), true);
            return false;
        } else if (newEmail.equals(oldEmail)) {
            return true;
        }
        return true;
    }

    /**
     * Complete error checking for UserRegistrationForm
     *
     * @param userRegistrationForm the object returned from the registration form
     *                             containing all fields the user filled out
     * @param model                the thymeleaf model
     * @return True if valid, false if not
     */
    public boolean validateRegistrationForm(UserRegistrationForm userRegistrationForm,
                                            Model model) {
        boolean validForm =
            checkNames(userRegistrationForm.getFirstName(), userRegistrationForm.getLastName(),
                model);
        if (!checkEmail(userRegistrationForm.getEmail(), model)) {
            validForm = false;
        }
        if (!checkAge(userRegistrationForm.getDateOfBirth(), model)) {
            validForm = false;
        }
        if (!checkStreetAddress(userRegistrationForm.getAddress1(),
            userRegistrationForm.getAddress2(), model)) {
            validForm = false;
        }
        if (!checkSuburb(userRegistrationForm.getSuburb(), model)) {
            validForm = false;
        }
        if (!checkCity(userRegistrationForm.getCity(), model)) {
            validForm = false;
        }
        if (!checkPostcode(userRegistrationForm.getPostcode(), model)) {
            validForm = false;
        }
        if (!checkCountry(userRegistrationForm.getCountry(), model)) {
            validForm = false;
        }
        if (!matchPasswords(userRegistrationForm.getPassword(),
            userRegistrationForm.getConfirmPassword(), model)) {
            validForm = false;
        }
        if (!validateStrongPassword(userRegistrationForm.getPassword())) {
            model.addAttribute(MATCH_PASSWORDS, true);
            model.addAttribute("matchPasswordsMessage",
                "Please enter a stronger password");
            validForm = false;
        }
        if (!checkEmailDoesntExist(userRegistrationForm.getEmail(), null, model)) {
            validForm = false;
        }
        return validForm;
    }

    /**
     * Validation for the edit user profile form
     *
     * @param userEditProfilePageForm form that needs to be validated
     * @param oldEmail                old email of the user
     * @param model                   thymeleaf model
     * @return true if validation passes, false if fails
     */
    public boolean validateEditUserProfileForm(UserEditProfilePageForm userEditProfilePageForm,
                                               String oldEmail, Model model) {
        boolean validForm = checkNames(userEditProfilePageForm.getFirstName(),
            userEditProfilePageForm.getLastName(), model);
        if (!checkEmail(userEditProfilePageForm.getEmail(), model)) {
            validForm = false;
        }
        if (!checkAge(userEditProfilePageForm.getDateOfBirth(), model)) {
            validForm = false;
        }
        if (!checkStreetAddress(userEditProfilePageForm.getAddress1(),
            userEditProfilePageForm.getAddress2(), model)) {
            validForm = false;
        }
        if (!checkSuburb(userEditProfilePageForm.getSuburb(), model)) {
            validForm = false;
        }
        if (!checkCity(userEditProfilePageForm.getCity(), model)) {
            validForm = false;
        }
        if (!checkPostcode(userEditProfilePageForm.getPostcode(), model)) {
            validForm = false;
        }
        if (!checkCountry(userEditProfilePageForm.getCountry(), model)) {
            validForm = false;
        }
        if (!checkEmailDoesntExist(userEditProfilePageForm.getEmail(), oldEmail, model)) {
            validForm = false;
        }
        return validForm;
    }

    /**
     * check the validation of the reset password process
     *
     * @param resetPasswordInfo object take all the attribute in the reset html
     * @param model             the thymeleaf model
     * @return yes if they are valid and no if they are not
     */
    public boolean validateResetPassword(ResetPasswordForm resetPasswordInfo, Model model) {

        return matchPasswords(resetPasswordInfo.getPassword(),
            resetPasswordInfo.getConfirmPassword(), model);
    }

    /**
     * Validation for the activity form
     *
     * @param createActivityForm activity form that needs validating
     * @param model              thymeleaf model
     * @return true if activity is valid, false if not
     */
    public boolean validateActivityForm(ActivityForm createActivityForm, Model model) {

        boolean validForm = true;

        // AC5
        if ((createActivityForm.getType().equals("Game")
            || createActivityForm.getType().equals("Friendly"))
            && createActivityForm.getTeamId() == -1) {
            validForm = false;
            model.addAttribute("teamError", true);

        }

        // AC6
        if (createActivityForm.getType().equals("none")) {
            validForm = false;
            model.addAttribute("typeError", true);
        }

        // AC7
        if (createActivityForm.getDescription().isBlank()
            || createActivityForm.getDescription().length() > 150
            || !createActivityForm.getDescription().matches("[\\s\\S]*[a-zA-Z]+[\\s\\S]*")) {
            validForm = false;
            model.addAttribute("descriptionError", true);
        }

        // AC8
        if (createActivityForm.getStartTime().isBlank()) {
            validForm = false;
            model.addAttribute("startTimeError", true);
        }

        if (createActivityForm.getEndTime().isBlank()) {
            validForm = false;
            model.addAttribute("endTimeError", true);
        }

        // AC9
        if (createActivityForm.getEndTime().compareTo(createActivityForm.getStartTime()) < 0
            && !createActivityForm.getEndTime().equals(createActivityForm.getStartTime())) {
            validForm = false;
            model.addAttribute("endTimeBeforeStartTimeError", true);

        }
        // AC10
        validForm = validateTeamCreatedBeforeActivity(createActivityForm, model, validForm);

        validForm = validateLocation(createActivityForm, model, validForm);

        return validForm;
    }

    /**
     * Validates Start Time and End Time against the creation time of the associated team.
     *
     * @param createActivityForm The ActivityForm to be validated.
     * @param model              The Model used for adding error messages.
     * @param validForm          The current validation status of the form.
     * @return The updated validation status of the form after AC10 validation.
     */
    private boolean validateTeamCreatedBeforeActivity(ActivityForm createActivityForm,
                                                      Model model, boolean validForm) {
        if (!createActivityForm.getEndTime().isBlank()
            && !createActivityForm.getStartTime().isBlank()) {
            Team team;
            if (createActivityForm.getTeamId() != -1) {
                try {
                    team = teamService.getTeamById(createActivityForm.getTeamId());
                    if (createActivityForm.getEndTime()
                        .compareTo(team.getCreationDate().toString()) < 0) {
                        validForm = false;
                        model.addAttribute("endCreationTimeError",
                            "End time prior to team creation ("
                                + team.getCreationDate().toString().split("\\.")[0] + ")");
                    }
                    if (createActivityForm.getStartTime()
                        .compareTo(team.getCreationDate().toString()) < 0) {
                        validForm = false;
                        model.addAttribute("startCreationTimeError",
                            "Start time prior to team creation ("
                                + team.getCreationDate().toString().split("\\.")[0] + ")");
                    }
                } catch (Exception e) {
                    validForm = false;
                }
            }
        }
        return validForm;
    }

    /**
     * Validation for the edit team form
     *
     * @param editTeamForm form that needs to be validated
     * @param model        thymeleaf model
     * @return true if edit form is valid, false if not
     */
    public boolean validateEditTeamForm(EditTeamForm editTeamForm, Model model) {

        model.addAttribute(invalidTabList.get(0), false);
        model.addAttribute(invalidTabList.get(3), false);

        boolean validForm = validateName(editTeamForm.getName(), model);

        if (!checkStreetAddress(editTeamForm.getAddress1(), editTeamForm.getAddress2(), model)) {
            validForm = false;
        }
        if (!checkSuburb(editTeamForm.getSuburb(), model)) {
            validForm = false;
        }
        if (!checkCity(editTeamForm.getCity(), model)) {
            validForm = false;
        }
        if (!checkPostcode(editTeamForm.getPostcode(), model)) {
            validForm = false;
        }
        if (!checkCountry(editTeamForm.getCountry(), model)) {
            validForm = false;
        }
        if (!validateOnlyLetters(editTeamForm.getSport(), model)) {
            validForm = false;
        }
        if (editTeamForm.getSport().equals("NONE")) {
            validForm = false;
            model.addAttribute(VALID_SPORT, false);
        }

        return validForm;
    }

    /**
     * Validation for the create team form
     *
     * @param createTeamForm team form that needs validation
     * @param model          thymeleaf model
     * @return true if form is valid, false if not
     */
    public boolean validateCreateTeamForm(CreateTeamForm createTeamForm, Model model) {

        model.addAttribute(invalidTabList.get(0), false);
        model.addAttribute(invalidTabList.get(3), false);

        boolean validForm = validateName(createTeamForm.getName(), model);

        if (!checkStreetAddress(createTeamForm.getAddress1(), createTeamForm.getAddress2(),
            model)) {
            validForm = false;
        }
        if (!checkSuburb(createTeamForm.getSuburb(), model)) {
            validForm = false;
        }
        if (!checkCity(createTeamForm.getCity(), model)) {
            validForm = false;
        }
        if (!checkPostcode(createTeamForm.getPostcode(), model)) {
            validForm = false;
        }
        if (!checkCountry(createTeamForm.getCountry(), model)) {
            validForm = false;
        }
        if (createTeamForm.getSport().equals(SupportedSports.NONE)) {
            model.addAttribute(VALID_SPORT, false);
            validForm = false;
        }
        return validForm;
    }

    /**
     * check the validation of the create formation forms
     *
     * @param formationForm the object returned from the create formation form
     *                      containing all fields the user filled out
     * @param model         the thymeleaf model
     * @return yes if they are valid and no if they are not
     */
    public boolean validateFormationForm(
        nz.ac.canterbury.seng302.tab.formobjects.FormationForm formationForm, Model model) {

        boolean validForm;

        if (formationForm.getFormation().matches(formationRegex)) {
            String[] split = formationForm.getFormation().split("-");
            model.addAttribute("formationError", split.length > 6);
            validForm = split.length <= 6;
        } else {
            model.addAttribute("formationError", true);
            validForm = false;
        }
        if (formationForm.getSport().equals("none")) {
            model.addAttribute("fieldError", true);
            validForm = false;
        }
        return validForm;
    }

    /**
     * check the validation of the player score form
     *
     * @param playerScoreForm the form containing the details of the player score
     *                        statistic provided by the user
     * @return yes if they are valid and no if they are not
     */
    public boolean validatePlayerScoreForm(PlayerScoreForm playerScoreForm) {
        boolean validForm = playerScoreForm.getScoreTime() > 0;

        //Add error message

        if (playerScoreForm.getScore() == 0) {
            validForm = false;
            //Add error message
        }

        if (Objects.isNull(playerScoreForm.getScoredPlayerId())
            || playerScoreForm.getScoredPlayerId() <= 0) {
            validForm = false;
            //Add error message
        }

        if (Objects.isNull(playerScoreForm.getActivityId())
            || playerScoreForm.getActivityId() <= 0) {
            validForm = false;
            //Add error message
        }

        return validForm;
    }

    /**
     * Validates the Substitution forms
     *
     * @param statisticsForm The entity passed from the HTML which holds the statistic forms
     * @param activity       an entity representation of a game activity
     * @param model          the thymeleaf model
     * @return true if all forms are valid else false
     */
    public boolean validateSubstitutionForms(StatisticsForm statisticsForm, GameActivity activity,
                                             Model model) {
        boolean validForm = true;
        String position = activity.getPosition();
        long duration = activity.getDuration();
        String[] rows = position.split("-");
        String substitutions = rows[rows.length - 1];
        String[] substitution = substitutions.split(",");
        String[] starters = Arrays.copyOfRange(rows, 0, rows.length - 1);
        HashMap<Long, Integer> playerPositionMap = new HashMap<>();
        List<SubstitutionForm> substitutionForms = statisticsForm.getSubstitutionForms();
        // Create a map of player IDs to their positions
        for (String row : starters) {
            String[] playerIds = row.split(",");
            for (String playerId : playerIds) {
                playerPositionMap.put(Long.valueOf(playerId), 1);
            }
        }
        for (String playerId : substitution) {
            playerPositionMap.put(Long.valueOf(playerId), 0);
        }

        // Validate substitution forms
        int counter = 0;
        List<Integer> goodIndexes = new ArrayList<>();
        List<SubstitutionForm> validForms = new ArrayList<>();
        List<Pair<Integer, String>> errors = new ArrayList<>();
        for (SubstitutionForm form : substitutionForms) {
            boolean individualFormValid = true;
            Long incomingPlayerId = form.getSubstitutedPlayerId();
            Long outgoingPlayerId = form.getSubstitutePlayerId();
            // Base Case empty substitution form
            if (incomingPlayerId == -1 && outgoingPlayerId == -1) {
                continue;
            }

            if (form.getSubstituteTime() <= 0 || form.getSubstituteTime() > duration) {
                // Check if subtime is greater than 0 and below the duration of the activity
                errors.add(new Pair<>(counter, "SubtimeError"));
                individualFormValid = false;
            }
            if (!playerPositionMap.containsKey(incomingPlayerId)
                || playerPositionMap.get(incomingPlayerId) != 1) {
                // Check if incoming player is in the lineup and not on the bench already
                errors.add(new Pair<>(counter, "outgoingPlayerError"));
                individualFormValid = false;
            }
            if (!playerPositionMap.containsKey(outgoingPlayerId)
                || playerPositionMap.get(outgoingPlayerId) != 0) {
                // Check if outgoing player is in the lineup and not on the field already
                errors.add(new Pair<>(counter, "incomingPlayerError"));
                individualFormValid = false;
            }
            if (individualFormValid) {
                // Valid form
                playerPositionMap.put(incomingPlayerId, 0);
                playerPositionMap.put(outgoingPlayerId, 1);
            } else {
                validForm = false;
            }
            validForms.add(form);
            goodIndexes.add(counter);
            counter++;
        }
        errors.removeIf(pair -> !goodIndexes.contains(pair.getValue0()));
        statisticsForm.setSubstitutionForms(validForms);
        model.addAttribute("errorsSubstitutions", errors);

        return validForm; // All substitutions are valid
    }

    /**
     * Validates the entire statistic form on substitution, player score, and game score,
     *
     * @param statisticsForm the form containing the details of the statistics
     *                       provided by the user
     * @param activity       an entity object representing an activity
     * @param model          map like object for front end
     * @return yes if they are valid and no if they are not
     */
    public boolean validateStatisticsForm(StatisticsForm statisticsForm, Activity activity,
                                          Model model) {
        statisticsForm.getSubstitutionForms().sort(Comparator
            .comparing(SubstitutionForm::getSubstituteTime));
        boolean validSubForm = validateSubstitutionForms(statisticsForm,
            (GameActivity) activity, model);
        boolean validPlayerScoreForm = validateScoreForms(statisticsForm, activity, model);
        boolean validScoreForm = validateScoreForm(statisticsForm, model);

        return validSubForm && validPlayerScoreForm && validScoreForm;
    }

    /**
     * Validates the score form
     *
     * @param statisticsForm the form containing the details of the statistics
     *                       provided by the user
     * @param model          map like object for front end
     * @return true if it is valid else false
     */
    public boolean validateScoreForm(StatisticsForm statisticsForm, Model model) {
        boolean validScoreForm = true;
        ScoreForm scoreForm = statisticsForm.getScoreForm();
        if (scoreForm != null) {
            // Regular expressions for "x" and "x-x" formats
            String regex = "^\\d+$"; // Matches "3 vs 4" format
            String regexDashX = "^\\d+-\\d+$"; // Matches "3-4 vs 4-5" format
            String homeScore = scoreForm.getHomeScore();
            String oppositionScore = scoreForm.getOppositionScore();
            boolean validScore = (homeScore.matches(regex) && oppositionScore.matches(regex))
                || (homeScore.matches(regexDashX) && oppositionScore.matches(regexDashX));
            if (!validScore || scoreForm.getHomeScore().isEmpty()
                || scoreForm.getOppositionScore().isEmpty()
                || scoreForm.getActivityResult() == null) {
                validScoreForm = false;
                model.addAttribute("validForm", false);
            }
        }
        if (statisticsForm.getScoreForm().getActivityResult() == ActivityResult.UNDECIDED) {
            validScoreForm = false;
        }
        return validScoreForm;
    }

    /**
     * check the validation of the player score form
     *
     * @param substitutionForm the form containing the details of the substitution
     *                         statistic provided by the user
     * @return yes if they are valid and no if they are not
     */
    public boolean validateSubstitutionForm(SubstitutionForm substitutionForm, Model model) {
        boolean validForm = substitutionForm.getSubstitutedPlayerId() > 0;

        //Add error message

        if (substitutionForm.getSubstitutePlayerId() <= 0) {
            validForm = false;
            //Add error message
        }

        if (substitutionForm.getSubstituteTime() <= 0) {
            validForm = false;
            //Add error message
        }

        if (substitutionForm.getSubstitutePlayerId()
            .equals(substitutionForm.getSubstitutedPlayerId())) {
            validForm = false;
            model.addAttribute("substituteError",
                "Must be two different players");
        }

        return validForm;
    }

    /**
     * Validates new token being made
     *
     * @param teamId long representing the team's id
     * @param token  string representing new token
     * @param model  Map like object
     */
    public void validateToken(long teamId, String token, Model model, Boolean isManager) {
        if (!Objects.equals(token, teamService.getCurrentToken(teamId))) {
            model.addAttribute("tokenError", "Only 1 regeneration of a token every 10 minutes!");
        } else if (Boolean.FALSE.equals(isManager)) {
            model.addAttribute("tokenError", "Only team managers can generate token");
        }
    }

    /**
     * validate the club information
     *
     * @param clubDto the data transfer object
     * @param body    the string info that need to be updated
     * @return true if all the infos are correct
     */
    public boolean validateClub(ClubDto clubDto, Map<String, String> body) {
        boolean valid = true;

        if (!clubDto.getClub().getName().matches(nameRegex)) {
            valid = false;
            body.put("name", "Name can not be empty");
        }
        if (clubDto.getClub().getSport().equals(SupportedSports.NONE)) {
            valid = false;
            body.put("sport", "Must select a sport");
        }
        if (!clubDto.getClub().getLocation().getCity().matches(cityRegex)) {
            valid = false;
            body.put("city", "Please enter a valid city");
        }
        if (!clubDto.getClub().getLocation().getCountry().matches(countryRegex)) {
            valid = false;
            body.put("country", "Please enter a valid country");
        }
        if (!clubTeamsIsValid(clubDto.getClub().getTeams(), clubDto.getClub().getSport())) {
            valid = false;
            body.put("team", "All teams must have the same sport");
        }
        return valid;
    }

    /**
     * Validate teams all have the same sport is mainly a back-up
     * in-case frontend validation does not work.
     *
     * @param teams teams to be added to the club
     * @return teams all have the same sport or not
     */
    private boolean clubTeamsIsValid(Set<Team> teams, SupportedSports sport) {
        if (teams == null || teams.isEmpty()) {
            return true;
        }
        for (Team team : teams) {
            if (!team.getSport().equals(sport.toString())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Validates every fact form for an activity
     *
     * @param statisticsForm The entity passed from the HTML which holds the fact forms
     * @param model          Thymeleaf Map like object
     * @return true if all the forms are valid, false otherwise
     */
    public boolean validateFactForms(StatisticsForm statisticsForm, Model model) {
        boolean validForm = true;
        int count = 0;
        List<Integer> goodIndexes = new ArrayList<>();
        List<FactForm> validForms = new ArrayList<>();
        List<Pair<Integer, String>> errors = new ArrayList<>();
        for (FactForm factForm : statisticsForm.getFactForms()) {
            boolean badTime = false;
            boolean badDescription = false;
            // Base case - empty fact form
            if ((Objects.isNull(factForm.getTimeOccurred())
                && factForm.getDescription().matches(blankRegex))) {
                continue;
            } // Check if the time is empty
            if (Objects.isNull(factForm.getTimeOccurred())) {
                errors.add(new Pair<>(count, "Please enter a time"));
                validForm = false;
                badTime = true;
            } else if (factForm.getTimeOccurred() <= 0) { // Check if time is negative
                errors.add(new Pair<>(count, INVALID_TIME));
                validForm = false;
            }
            // Check if description is empty
            if (factForm.getDescription().matches(blankRegex)) {
                errors.add(new Pair<>(count, "Must have a description"));
                validForm = false;
                badDescription = true;
            }
            // True if either time or description have data (aren't empty)
            if (!(badTime && badDescription)) {
                validForms.add(factForm);
                goodIndexes.add(count);
            }
            count += 1;
        }
        errors.removeIf(pair -> !goodIndexes.contains(pair.getValue0()));
        statisticsForm.setFactForms(validForms);
        model.addAttribute("errors", errors);
        return validForm;
    }

    /**
     * Validates every score form for an activity
     *
     * @param statisticsForm The entity passed from the HTML which holds the score forms
     * @param model          Thymeleaf Map like object
     * @return true if all the forms are valid, false otherwise
     */
    public boolean validateScoreForms(StatisticsForm statisticsForm, Activity activity,
                                      Model model) {
        boolean validForm = true;
        int count = 0;
        int totalScored = 0;
        long duration = ConvertingUtil.getActivityDuration(activity.getStartTime(),
            activity.getEndTime());
        List<Integer> goodIndexes = new ArrayList<>();
        List<PlayerScoreForm> validForms = new ArrayList<>();
        List<Pair<Integer, String>> errors = new ArrayList<>();
        for (PlayerScoreForm scoreForm : statisticsForm.getPlayerScoreForms()) {
            // Base case - empty score form
            if (scoreForm.isEmpty()) {
                continue;
            }
            validForm = isValidScoreForm(validForm, count, duration, errors, scoreForm);
            validForms.add(scoreForm);
            goodIndexes.add(count);
            count += 1;
            totalScored += scoreForm.getScore();
        }
        errors.removeIf(pair -> !goodIndexes.contains(pair.getValue0()));
        // Checks if the total inputted points scored is more than the activity outcome (home score)
        try {
            if (!statisticsForm.getScoreForm().getHomeScore().contains("-")
                && !statisticsForm.getScoreForm().getHomeScore().matches(blankRegex)
                && totalScored > Integer.parseInt(statisticsForm.getScoreForm().getHomeScore())) {
                errors.add(new Pair<>(count, "Too many points"));
                validForm = false;
            }
        } catch (NumberFormatException e) {
            errors.add(new Pair<>(count, "Score too large"));
            validForm = false;
        }
        statisticsForm.setPlayerScoreForms(validForms);
        model.addAttribute("errors", errors);
        return validForm;
    }

    /**
     * Validates an add food form
     *
     * @param quantity          the quantity of the food
     * @param fdcId             the fdcId of the food
     * @param portionSizeNumber the portion size number of the food
     * @return true if the form is valid, false otherwise
     */
    public boolean validateAddFoodForm(Long quantity, Long fdcId, Long portionSizeNumber) {
        boolean validForm = quantity != null && quantity > 0;
        if (fdcId == null || fdcId <= 0) {
            validForm = false;
        }
        if (portionSizeNumber == null || portionSizeNumber < 0) {
            validForm = false;
        }
        return validForm;
    }

    /**
     * Validates an ExerciseGoalsForm
     *
     * @param exerciseGoalsForm The entity passed from the HTML which holds the exercise goals
     * @return true if the form is valid, false otherwise
     */
    public boolean validateExerciseGoalForm(ExerciseGoalsForm exerciseGoalsForm) {
        boolean valid = Objects.isNull(exerciseGoalsForm.getSteps())
            || (exerciseGoalsForm.getSteps() <= 150000
            && exerciseGoalsForm.getSteps() >= 1);

        if (!Objects.isNull(exerciseGoalsForm.getCaloriesBurnt())
            && (exerciseGoalsForm.getCaloriesBurnt() > 50000
            || exerciseGoalsForm.getCaloriesBurnt() < 1)) {

            valid = false;
        }
        if (!Objects.isNull(exerciseGoalsForm.getDistanceTravelled())
            && (exerciseGoalsForm.getDistanceTravelled() > 500
            || exerciseGoalsForm.getDistanceTravelled() < 0.001)) {

            valid = false;
        }
        if (!Objects.isNull(exerciseGoalsForm.getTotalActivityTime())
            && (exerciseGoalsForm.getTotalActivityTime() > 1440
            || exerciseGoalsForm.getTotalActivityTime() < 1)) {

            valid = false;
        }
        return valid;
    }

    /**
     * Validates a garmin activity
     * @param garminActivity entity representing a garmin activity
     * @return true if valid otherwise false
     */
    public List<String> validateGarminActivity(GarminActivity garminActivity) {
        List<String> errors = new ArrayList<>();
        if (Objects.equals(garminActivity.getActivity(), null)) {
            errors.add("Activity is null");
        }
        if (garminActivity.getActiveKilocalories() < 0) {
            errors.add("Calories burnt must be greater than or equal to 0");
        }
        if (garminActivity.getDistanceInMeters() < 0) {
            errors.add("Distance must be greater than or equal to 0");
        }
        if (garminActivity.getDurationInSeconds() < 0) {
            errors.add("Duration must be greater than or equal to 0");
        }
        return errors;
    }
}

