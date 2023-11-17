package nz.ac.canterbury.seng302.tab.repository;


import java.util.ArrayList;
import java.util.List;
import nz.ac.canterbury.seng302.tab.entity.TeamRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * Access to the 'Team role' database
 */
public interface TeamRoleRepository extends JpaRepository<TeamRoles, Long> {

    @Query(value = "SELECT * from team_roles WHERE team_id = :teamId and "
        + "user_id = :userId", nativeQuery = true)
    TeamRoles getTeamRoles(long teamId, long userId);

    /**
     * Find all teams and roles the user is enrolled with
     *
     * @param userId Long userId
     * @return List of all teams (team_id) the user is in and the role
     */
    @Query(value = "SELECT * from team_roles WHERE user_id = :userId", nativeQuery = true)
    List<TeamRoles> findAllMemberTeamsAndRoles(Long userId);


    /**
     * Find all members and their roles within a given team
     *
     * @param teamId Long team_id
     * @return List of all members (user_id) and their role
     */
    @Query(value = "SELECT * from team_roles WHERE team_id = :teamId", nativeQuery = true)
    List<TeamRoles> findAllTeamsUsersAndRoles(Long teamId);

    /**
     * Find all teams that a user manages or coaches, used for create activity dropdown
     *
     * @param userId the id of the user wishing to see their teams
     * @return List of all team ids that the user manages or coaches
     */
    @Query(value = "SELECT team_id from team_roles WHERE user_id = :userId AND "
        + "(role = 'Manager' or role='Coach')", nativeQuery = true)
    ArrayList<Long> teamsUserManagesOrCoaches(Long userId);

    /**
     * Find the user's role within a given team
     *
     * @return String role i.e. "manager"
     */
    @Query(value = "SELECT role FROM team_roles WHERE user_id = :userId AND team_id = :teamId",
        nativeQuery = true)
    String getUserRole(Long teamId, Long userId);


    /**
     * Delete user role by team and user id
     * i.e. No longer a member of a team
     *
     * @param teamId Long teamId
     * @param userId Long userId
     */
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM team_roles WHERE user_id = :userId AND team_id = :teamId",
        nativeQuery = true)
    void deleteRoleBy(Long teamId, Long userId);

    /**
     * Updates the total points for a specific user playing in a team
     */
    @Transactional
    @Modifying
    @Query(value = "UPDATE team_roles set total_points = total_points + :points WHERE user_id = "
        + ":userId AND team_id = :teamId",
        nativeQuery = true)
    void updateUserPoints(Long teamId, Long userId, int points);

    /**
     * Updates the total time played for a specific user playing in a team
     */
    @Transactional
    @Modifying
    @Query(value = "UPDATE team_roles set total_time = total_time + :time WHERE user_id = :userId"
        + " AND team_id = :teamId",
        nativeQuery = true)
    void updateUserTimePlayed(Long teamId, String userId, Long time);

    /**
     * Retrieves the top five players in terms of minutes played in a team
     *
     * @param teamId ID of the team
     * @return the top five players in terms of minutes played in a team
     */
    @Query(value = "SELECT * FROM team_roles where team_id = :teamId order by total_time desc "
        + "limit 5",
        nativeQuery = true)
    List<TeamRoles> getTopFivePlayTimeInTeam(Long teamId);

    /**
     * Query for AC3
     *
     * @param teamId id of team
     * @return list of teams top 5 scorers
     */
    @Query(value = "SELECT * FROM team_roles where team_id = :teamId order by total_points desc"
        + " limit 5",
        nativeQuery = true)
    List<TeamRoles> getTeamTop5Scorers(long teamId);

    /**
     * Updates the total games played for a specific user playing in a team
     */
    @Transactional
    @Modifying
    @Query(value = "UPDATE team_roles set total_games = total_games + 1 WHERE user_id = :userId AND"
        + " team_id = :teamId",
        nativeQuery = true)
    void updateUserGamesPlayed(Long teamId, String userId);

    /**
     * Decreases the total games played for a specific user playing in a team by one
     */
    @Transactional
    @Modifying
    @Query(value = "UPDATE team_roles set total_games = total_games - 1 WHERE user_id = :userId AND"
            + " team_id = :teamId",
            nativeQuery = true)
    void decreaseUserGamesPlayed(Long teamId, String userId);
}


