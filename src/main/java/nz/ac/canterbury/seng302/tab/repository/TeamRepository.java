package nz.ac.canterbury.seng302.tab.repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nz.ac.canterbury.seng302.tab.entity.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Access to the team database
 */
public interface TeamRepository extends CrudRepository<Team, Long> {
    Optional<Team> findById(long id);

    /**
     * Gets the team with the provided current token
     *
     * @param currentToken the team invitation token which is used to find the team
     * @return the team corresponding to the provided currentToken
     */
    Optional<Team> findByCurrentToken(String currentToken);

    List<Team> findAll();

    /**
     * Gets all the information for all the users in a page structure
     *
     * @param pageable a page request specifying how to structure the data
     * @return Team page, the pages containing all the users
     */
    Page<Team> findAll(Pageable pageable);

    /**
     * Gets all the information for all the team in a page structure
     *
     * @param pageable     a page request specifying how to structure the data
     * @param searchString a string that is used to match city, country or team name
     * @return Team page, the pages containing all the users
     */
    @Query(
        value = "SELECT team.* FROM team, team_roles, location WHERE "
            + "location.location_id = team.address_id AND team.team_id = team_roles.team_id AND "
            + "(name LIKE %:searchString% OR city LIKE %:searchString% OR country "
            + "LIKE %:searchString%)",
        nativeQuery = true)
    Page<Team> searchTeams(Pageable pageable, String searchString);

    /**
     * Gets all teams information based on the ids
     *
     * @param pageable     a page request specifying how to structure the data
     * @param searchString a string that is used to match city, country or team nam
     * @param myId         collection of ids tabuser, team, and location
     * @return all the teams info based on the ids
     */
    @Query(
        value = "SELECT team.* FROM team, team_roles, location WHERE user_id = :myId AND "
            + "team.team_id = team_roles.team_id AND location.location_id = team.address_id AND"
            + " (name LIKE %:searchString% OR city LIKE %:searchString% OR "
            + "country LIKE %:searchString%)",
        nativeQuery = true)
    Page<Team> searchTeamsId(Pageable pageable, String searchString, long myId);

    /**
     * Get the team ids where the team roles id matches the team id and
     * where the team roles id matches the user id
     *
     * @param teamId the team id
     * @param userId the user id
     * @return team id
     */
    @Query(value = "SELECT team_id FROM team_roles WHERE team_roles.team_id = :teamId AND "
        + "team_roles.user_id = :userId",
        nativeQuery = true)
    List<String> checkUsersTeam(long teamId, long userId);

    /**
     * Gets the current token of the team
     *
     * @param teamId Long representing the team id
     * @return String representing the current token
     */
    @Query(value = "SELECT current_token FROM team WHERE team_id = :teamId",
        nativeQuery = true)
    String getCurrentToken(long teamId);

    /**
     * Updates the Current Token
     *
     * @param teamId Long representing team id
     * @param token  String representing the token
     */
    @Transactional
    @Modifying
    @Query(value = "UPDATE team set current_token = :token WHERE team_id = :teamId",
        nativeQuery = true)
    void setCurrentToken(long teamId, String token);

    /**
     * Gets the current token regeneration time
     *
     * @param teamId Long representing the teams id
     * @return a timestamp of the token regeneration time
     */
    @Query(value = "SELECT current_Token_Regen_Time FROM team WHERE team_id = :teamId",
        nativeQuery = true)
    Timestamp getRegenTime(long teamId);

    /**
     * Updates the current token regeneration time
     *
     * @param teamId Long representing the teams id
     * @param time   Timestamp representing the new token's regeneration time
     */
    @Transactional
    @Modifying
    @Query(value = "UPDATE team set current_Token_Regen_Time = :time WHERE team_id = :teamId",
        nativeQuery = true)
    void setRegenTime(long teamId, Timestamp time);

    @Query(value = "SELECT * FROM team WHERE current_token = :token", nativeQuery = true)
    ArrayList<Team> getTeamByToken(String token);
}