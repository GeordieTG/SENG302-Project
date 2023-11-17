package nz.ac.canterbury.seng302.tab.repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * FormResult repository accessor using Spring's @link{CrudRepository}.
 * These (basic) methods are provided for us without the need to write our own implementations
 */
public interface TabUserRepository extends JpaRepository<TabUser, Long> {

    /**
     * Gets all the information for a user based on their id
     *
     * @param id the if of the target user
     * @return FormResult the information of the target user
     */
    TabUser findById(long id);

    /**
     * Gets all the information for all the users
     *
     * @return TabUser list containing all the users
     */
    List<TabUser> findAll();

    /**
     * Gets all the information for all the users in a page structure
     *
     * @param pageable a page request specifying how to structure the data
     * @return TabUser page containing all the users
     */
    Page<TabUser> findAll(Pageable pageable);

    /**
     * Gets all the information for all the users who match either the first or last name
     *
     * @param pageable  a page request specifying how to structure the data
     * @param firstName the first name of the target user
     * @param lastName  the last name of the target user
     * @return TabUser page containing the matching user
     */
    Page<TabUser> findFormResultsByFirstNameIsContainingIgnoreCaseOrLastNameIsContainingIgnoreCase(
        Pageable pageable, String firstName, String lastName);

    /**
     * Gets all information of a user based on their email if they are in the database
     *
     * @param email the email of the target user
     * @return TabUser, the information of the target user
     */
    @Query(value = "SELECT * FROM tab_user WHERE email = :email", nativeQuery = true)
    Optional<TabUser> findByEmail(String email);

    /**
     * Gets all information of a user based on their email
     *
     * @param email the email of the target user
     * @return TabUser the information of the target user
     */
    @Query(value = "SELECT * FROM tab_user WHERE email = :email", nativeQuery = true)
    TabUser findInfoByEmail(String email);

    /**
     * Updates the first name of a user
     *
     * @param email the email of the user who is updating their first name
     * @param name  the new first name of the user
     */
    @Transactional
    @Modifying
    @Query(value = "update tab_user set first_name = :name WHERE email = :email",
        nativeQuery = true)
    void setFirstName(String email, String name);

    /**
     * Updates the last name of a user
     *
     * @param email the email of the user who is updating their last name
     * @param name  the new last name of the user
     */
    @Transactional
    @Modifying
    @Query(value = "update tab_user set last_name = :name WHERE email = :email", nativeQuery = true)
    void setLastName(String email, String name);

    /**
     * Updates the email of a user
     *
     * @param oldEmail the previous email of the user who is updating their email
     * @param newEmail the new email that the user if changing to
     */
    @Transactional
    @Modifying
    @Query(value = "update tab_user set email = :newEmail WHERE email = :oldEmail",
        nativeQuery = true)
    void setEmail(String oldEmail, String newEmail);

    /**
     * Updates the password for a user
     *
     * @param email    the email of the user who is updating their password
     * @param password the hash of the new password
     */
    @Transactional
    @Modifying
    @Query(value = "update tab_user set password = :password WHERE email = :email",
        nativeQuery = true)
    void setPassword(String email, String password);

    /**
     * Updates the date of birth for a user
     *
     * @param email the email of the user who is updating their date of birth
     * @param dob   the new date of birth
     */
    @Transactional
    @Modifying
    @Query(value = "update tab_user set date_of_birth = :dob WHERE email = :email",
        nativeQuery = true)
    void setDoB(String email, String dob);

    /**
     * Updates the name of a user's profile picture
     *
     * @param email          the email of the user who is updating their profile picture
     * @param profilePicture the name of the new profile picture
     */
    @Transactional
    @Modifying
    @Query(value = "update tab_user set profile_picture = :profilePicture WHERE email = :email",
        nativeQuery = true)
    void setProfilePicture(String email, String profilePicture);

    /**
     * Updates the favourite sport of a user
     *
     * @param email the email of the user who is updating their sport
     * @param sport the new favourite sport for the user
     */
    @Transactional
    @Modifying
    @Query(value = "update tab_user set favourite_sport = :sport WHERE email = :email",
        nativeQuery = true)
    void setFavouriteSport(String email, String sport);

    /**
     * Updates the name of a user's profile picture
     *
     * @param email        the email of the user who is updating their profile picture
     * @param confirmation the users registration token
     */
    @Transactional
    @Modifying
    @Query(value = "update tab_user set confirmation = :confirmation, expiry_time = :expiryTime"
        + " WHERE email = :email", nativeQuery = true)
    void setConfirmation(String email, String confirmation, Timestamp expiryTime);

    /**
     * Query to find all teams belonging to the user
     *
     * @param id user id
     * @return a list of id's for each team the user is a member of
     */
    @Query(
        value = "SELECT team_id FROM team_roles WHERE user_id=?1",
        nativeQuery = true)
    ArrayList<Long> findTabUsersTeams(long id);

    /**
     * Get users registration token from their email
     *
     * @param email users email
     * @return the users unique registration token
     */
    @Query(value = "SELECT confirmation FROM tab_user WHERE email=?1", nativeQuery = true)
    String getTokenFromEmail(String email);

    /**
     * Get the expiry time of the users registration token
     *
     * @param token the users unique registration token
     * @return The tokens expiry time
     */
    @Query(value = "SELECT expiry_time FROM tab_user WHERE confirmation=?1", nativeQuery = true)
    Timestamp getTokenExpiryTime(String token);

    /**
     * Get the expiry time of the users reset password token
     *
     * @param token the users unique reset password token
     * @return the users reset password token
     */
    @Query(value = "SELECT reset_password_expiry_time FROM tab_user WHERE reset_password_token=?1",
        nativeQuery = true)
    Timestamp getResetPasswordExpiryTime(String token);


    /**
     * Updates the token of the user
     * depending on whether their token is valid of not
     *
     * @param currentTime the current time of the user
     */
    @Transactional
    @Modifying
    @Query(value = "DELETE from tab_user WHERE expiry_time <= :currentTime", nativeQuery = true)
    void removeExpiredUsers(Timestamp currentTime);


    /**
     * Gets all information of a user based on their token
     *
     * @param token the token of the target user
     * @return FormResult the information of the target user
     */
    @Query(value = "SELECT * FROM tab_user WHERE confirmation = :token", nativeQuery = true)
    TabUser getUserbyToken(String token);

    /**
     * Active a reset password request. Set the token and expiry times indicating a live request.
     *
     * @param email      Users email
     * @param token      Users unique reset password token
     * @param expiryTime expiry time of the token (1 hour ahead of the current time)
     */
    @Transactional
    @Modifying
    @Query(value = "update tab_user set reset_password_token = :token, "
        + "reset_password_expiry_time= :expiryTime WHERE EMAIL = :email", nativeQuery = true)
    void setResetPasswordDetails(String email, String token, Timestamp expiryTime);

    /**
     * Reset the users password and removes the active reset password attempt.
     *
     * @param password the new hashed password
     * @param token    the users unique reset password token
     */
    @Transactional
    @Modifying
    @Query(value = "update tab_user set password = :password, reset_password_token=null, "
        + "reset_password_expiry_time=null WHERE reset_password_token = :token",
        nativeQuery = true)
    void resetPassword(String password, String token);

    /**
     * Get user via their reset password token. Used when the link is clicked in their email.
     *
     * @param token The users unique reset password token
     * @return The user
     */
    @Query(value = "SELECT * FROM tab_user WHERE reset_password_token=:token", nativeQuery = true)
    TabUser getUserbyResetPasswordToken(String token);

    /**
     * Removes all users expired reset password attempts.
     * Null fields = No active reset password attempt
     *
     * @param currentTime the real world current time
     */
    @Transactional
    @Modifying
    @Query(value = "UPDATE tab_user SET reset_password_token=null, reset_password_expiry_time=null "
        + "WHERE reset_password_expiry_time <= :currentTime", nativeQuery = true)
    void removeResetPasswordAttempt(Timestamp currentTime);

    /**
     * Confirms the user registration in the database. Null fields = Confirmed registration
     *
     * @param email The users email
     */
    @Transactional
    @Modifying
    @Query(value = "UPDATE tab_user SET confirmation=null, expiry_time=null "
        + "WHERE email = :email", nativeQuery = true)
    void removeConfirmationDetails(String email);

    @Query(value = "SELECT tab_user.* FROM tab_user JOIN team_roles ON "
        + "tab_user.user_id = team_roles.user_id WHERE team_roles.team_id = :teamId",
        nativeQuery = true)
    ArrayList<TabUser> getUsers(long teamId);

    @Query(value = "SELECT team.team_id FROM team JOIN team_roles ON "
        + "team.team_id = team_roles.team_id WHERE user_id = :id ORDER BY LOWER(team.name) ASC",
        nativeQuery = true)
    List<Long> getTeamIdsAlphabetically(Long id);

    @Query(value = "SELECT calorie_intake_preference FROM tab_user WHERE user_id = :id",
            nativeQuery = true)
    String getCalorieIntakePreference(Long id);
}