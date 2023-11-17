package nz.ac.canterbury.seng302.tab.service;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.repository.TabUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Service class for FormResults, defined by the @link{Service} annotation.
 * This class links automatically with @link{FormRepository},
 * see the @link{Autowired} annotation below
 */
@Service
public class TabUserService {
    /**
     * Allows us to access location functionality with the database
     */
    @Autowired
    LocationService locationService;
    @Autowired
    private TabUserRepository tabUserRepository;

    Logger logger = LoggerFactory.getLogger(TabUserService.class);

    String[] intakePreference = new String[]{"maintain", "cut", "bulk"};

    /**
     * Gets all FormResults from persistence
     *
     * @return all FormResults currently saved in persistence
     */
    public List<TabUser> getFormResults() {
        return tabUserRepository.findAll();
    }

    /**
     * Gets the information of multiple users in a page format
     *
     * @param pageNumber the next page to get
     * @return FormResult page of the user information
     */
    public Page<TabUser> getFormResultsPages(int pageNumber) {
        Pageable pageable =
            PageRequest.of(pageNumber - 1, 10, Sort.by("firstName").and(Sort.by("lastName")));
        return tabUserRepository.findAll(pageable);
    }

    /**
     * Gets whether an email exists in the DB already
     *
     * @param email the email to check
     * @return boolean true if the email is already in the DB, false otherwise
     */
    public boolean getByEmail(String email) {
        return tabUserRepository.findByEmail(email).isPresent();
    }

    /**
     * Gets the information of a user based on their id
     *
     * @param id the target users id
     * @return FormResult the users information
     */
    public TabUser getById(long id) {
        return tabUserRepository.findById(id);
    }

    /**
     * Gets the information of a user based on their email
     *
     * @param email the target users email
     * @return FormResult the users information
     */
    public TabUser findByEmail(String email) {
        Optional<TabUser> userOptional = tabUserRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            // Handle the case when the user is not found
            // You can choose an appropriate way to handle it,
            // such as returning null or throwing an exception
            throw new NoSuchElementException("User not found");
        }
    }


    /**
     * Gets the information of a user based on their email
     *
     * @param email the target users email
     * @return FormResult the users information
     */
    public TabUser getInfoByEmail(String email) {
        return tabUserRepository.findInfoByEmail(email);
    }

    /**
     * Updates the first name of a target user
     *
     * @param email the target users email
     * @param name  the target users new first name
     */
    public void updateFirstName(String email, String name) {
        tabUserRepository.setFirstName(email, name);
    }

    /**
     * Updates the last name of a target user
     *
     * @param email the target users email
     * @param name  the target users new last name
     */
    public void updateLastName(String email, String name) {
        tabUserRepository.setLastName(email, name);
    }

    /**
     * Updates the email of a target user
     *
     * @param oldEmail the target users old email
     * @param newEmail the target users new email
     */
    public void updateEmail(String oldEmail, String newEmail) {
        tabUserRepository.setEmail(oldEmail, newEmail);
    }

    /**
     * Updates the password of a target user
     *
     * @param email    the target users email
     * @param password the target users new password
     */
    public void updatePassword(String email, String password) {
        tabUserRepository.setPassword(email, password);
    }

    public void setConfirmation(String email, String confirmation, Timestamp expiryTime) {
        tabUserRepository.setConfirmation(email, confirmation, expiryTime);
    }

    public void setResetPasswordDetails(String email, String token, Timestamp expiryTime) {
        tabUserRepository.setResetPasswordDetails(email, token, expiryTime);
    }

    /**
     * Updates the date of birth of a target user
     *
     * @param email the target users email
     * @param dob   the target users new date of birth
     */
    public void updatedob(String email, String dob) {
        tabUserRepository.setDoB(email, dob);
    }

    /**
     * Updates the profile picture of a target user
     *
     * @param email          the target users email
     * @param profilePicture the target users new profile picture
     */
    public void updateProfilePicture(String email, String profilePicture) {
        tabUserRepository.setProfilePicture(email, profilePicture);
    }

    /**
     * Updates the favourite sport of a target user
     *
     * @param email          the target users email
     * @param favouriteSport the target users new favourite sport
     */
    public void updateFavouriteSport(String email, String favouriteSport) {
        tabUserRepository.setFavouriteSport(email, favouriteSport);
    }

    /**
     * Saves a new user or updates the information of an old user
     *
     * @param user the user information to save
     */
    public void updateUser(TabUser user) {
        tabUserRepository.save(user);
    }

    /**
     * Finds all users whose first or last name match a search criteria
     *
     * @param searchQuery the search queries to be run
     * @return FormResult page of all the users who meet the search criteria
     */
    public Page<TabUser> findUser(String searchQuery, int pageNumber) {
        Pageable pageable =
            PageRequest.of(pageNumber - 1, 10, Sort.by("firstName").and(Sort.by("lastName")));
        List<String> searches = List.of(searchQuery.split(" "));
        String searchQuery1 = searches.get(0);
        String searchQuery2 = searches.get(0);
        if (searches.size() != 1) {
            searchQuery1 = searches.get(0);
            searchQuery2 = searches.get(1);
        }
        return tabUserRepository
            .findFormResultsByFirstNameIsContainingIgnoreCaseOrLastNameIsContainingIgnoreCase(
                pageable, searchQuery1, searchQuery2);
    }

    /**
     * Adds a formResult to persistence
     *
     * @param tabUser object to persist
     * @return the saved formResult object
     */
    public TabUser addTabUser(TabUser tabUser) {
        return tabUserRepository.save(tabUser);
    }

    /**
     * Creates a new TabUser class into the TabUser database
     * All details entered fully
     *
     * @param fullName      String list of first and last name
     * @param location       Location containing city and country
     * @param email          String
     * @param dateOfBirth    Date
     * @param password       String
     * @param profilePicture String
     * @param favouriteSport String
     * @return TabUser created
     */
    public TabUser createFullTabUserInDatabase(List<String> fullName, Location location,
                                               String email, String dateOfBirth,
                                               String password, String profilePicture,
                                               String favouriteSport) {
        Location userLocation = locationService.createLocationInDatabase(location.getCity(),
                location.getCountry());
        TabUser user =
            new TabUser(fullName, userLocation, email, dateOfBirth, password, profilePicture,
                favouriteSport);
        return tabUserRepository.save(user);
    }

    /**
     * Create and adds a TabUser into the database with only the mandatory fields
     *
     * @param firstName String
     * @param lastName  String
     * @param city      String
     * @param country   String
     * @return TabUser created
     */
    public TabUser createTabUserInDatabase(String firstName, String lastName, String city,
                                           String country) {
        Location location = locationService.createLocationInDatabase(city, country);
        TabUser user = new TabUser(firstName, lastName, location);
        return tabUserRepository.save(user);
    }

    public List<TabUser> getUsers(long teamId) {
        return tabUserRepository.getUsers(teamId);
    }


    /**
     * Deletes all data in user database
     */
    public void deleteAllData() {
        tabUserRepository.deleteAll();
    }

    /**
     * gets the currently logged-in user based on their cookies.
     * Warning: Doesn't work across two incognito tabs (to test user firefox and chrome for example)
     *
     * @return Currently logged-in user
     */
    public TabUser getCurrentlyLoggedIn() {
        return tabUserRepository.findInfoByEmail(
            SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public String getTokenFromEmail(String email) {
        return tabUserRepository.getTokenFromEmail(email);
    }

    public Timestamp getTokenExpiryTime(String token) {
        return tabUserRepository.getTokenExpiryTime(token);
    }


    public void removeExpiredUsers(Timestamp currentTime) {
        tabUserRepository.removeExpiredUsers(currentTime);
    }

    public TabUser getUserbyToken(String token) {
        return tabUserRepository.getUserbyToken(token);
    }

    public Timestamp getResetPasswordExpiryTime(String token) {
        return tabUserRepository.getResetPasswordExpiryTime(token);
    }

    public void resetPassword(String password, String token) {
        tabUserRepository.resetPassword(password, token);
    }

    public void removeResetPasswordAttempt(Timestamp currentTime) {
        tabUserRepository.removeResetPasswordAttempt(currentTime);
    }

    public TabUser getUserbyResetPasswordToken(String token) {
        return tabUserRepository.getUserbyResetPasswordToken(token);
    }

    public void removeConfirmationDetails(String email) {
        tabUserRepository.removeConfirmationDetails(email);
    }

    public List<Long> getTeamIdsAlphabetically(Long id) {
        return tabUserRepository.getTeamIdsAlphabetically(id);
    }

    /**
     * Retrieves and returns a given user's intake preference.
     * If preference string is not within the expected list, then return 'Maintain'
     * as the default value
     * @param id userid, Long
     * @return intake preference
     */
    public String getCalorieIntakePreference(Long id) {
        String preference = tabUserRepository.getCalorieIntakePreference(id);
        if (!Arrays.stream(intakePreference).toList().contains(preference)) {
            logger.info("Intake preference value is not within the "
                    + "expected values: {}", preference);
            logger.info("Intake Preference using default value `Maintain`");
            preference = "Maintain";
        }
        return preference;
    }
}
