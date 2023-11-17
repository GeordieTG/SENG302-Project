package nz.ac.canterbury.seng302.tab.repository;

import java.util.ArrayList;
import java.util.List;
import nz.ac.canterbury.seng302.tab.entity.Activity;
import nz.ac.canterbury.seng302.tab.entity.GameActivity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Access to the 'Activity' database for team aggregated statistics
 */
public interface TeamStatsRepository extends CrudRepository<Activity, Long> {

    /**
     * Query for AC 1, get total games played
     *
     * @param teamId id of team
     * @return number of total games played
     */
    @Query(value = "SELECT count(*) FROM activity where teamId = :teamId and dtype = 'Game'",
        nativeQuery = true)
    int getTotalGamesPlayed(long teamId);

    /**
     * Query for AC1, get number of team wins
     *
     * @param teamId id of team
     * @return number of team wins
     */
    @Query(value = "SELECT count(*) FROM activity where team_id = :teamId and outcome = 'WON'",
        nativeQuery = true)
    int getTeamWins(long teamId);

    /**
     * Query for AC1, get number of team losses
     *
     * @param teamId id of team
     * @return number of team losses
     */
    @Query(value = "SELECT count(*) FROM activity where team_id = :teamId and outcome = 'LOSS'",
        nativeQuery = true)
    int getTeamLosses(long teamId);

    /**
     * Query for AC1, get number of team draws
     *
     * @param teamId id of team
     * @return number of team draws
     */
    @Query(value = "SELECT count(*) FROM activity where team_id = :teamId and outcome = 'DRAW'",
        nativeQuery = true)
    int getTeamDraws(long teamId);

    /**
     * Query for AC2, get number of team game wins
     *
     * @param teamId id of team
     * @return number of team game wins
     */
    @Query(value = "SELECT count(*) FROM team where teamId = :teamId and outcome = 'WIN' and "
        + "type ='game' LIMIT 5 order by start_time asc",
        nativeQuery = true)
    int getTeamGameWins(long teamId);

    /**
     * Query for AC2, get number of team game losses
     *
     * @param teamId id of team
     * @return number of team game losses
     */
    @Query(value = "SELECT count(*) FROM team where teamId = :teamId and outcome = 'LOSS' and "
        + "type ='game' LIMIT 5 order by start_time asc",
        nativeQuery = true)
    int getTeamGameLosses(long teamId);

    /**
     * Query for AC2, get number of team game draws
     *
     * @param teamId id of team
     * @return number of team game draws
     */
    @Query(value = "SELECT count(*) FROM team where teamId = :teamId and outcome = 'DRAW' and "
        + "type ='game' LIMIT 5 order by start_time asc",
        nativeQuery = true)
    int getTeamGameDraws(long teamId);

    /**
     * Query for AC2, get number of team friendly wins
     *
     * @param teamId id of team
     * @return number of team friendly wins
     */
    @Query(value = "SELECT count(*) FROM team where teamId = :teamId and outcome = 'WIN' and "
        + "type ='friendly' LIMIT 5 order by start_time asc",
        nativeQuery = true)
    int getTeamFriendlyWins(long teamId);

    /**
     * Query for AC2, get number of team friendly losses
     *
     * @param teamId id of team
     * @return number of team friendly losses
     */
    @Query(value = "SELECT count(*) FROM team where teamId = :teamId and outcome = 'LOSS' and "
        + "type ='friendly' LIMIT 5 order by start_time asc",
        nativeQuery = true)
    int getTeamFriendlyLosses(long teamId);

    /**
     * Query for AC2, get number of team friendly draws
     *
     * @param teamId id of team
     * @return number of team friendly draws
     */
    @Query(value = "SELECT count(*) FROM team where teamId = :teamId and outcome = 'DRAW' and "
        + "type ='friendly' LIMIT 5 order by start_time asc",
        nativeQuery = true)
    int getTeamFriendlyDraws(long teamId);

    /**
     * Query for AC4
     *
     * @param teamId id of team
     * @return list of teams top five players in terms of minutes played
     */
    @Query(value = "SELECT activity.user_id, count(*) from activity join statistic_player_score on "
        + "game_activity_id = activity_id where team_id = :teamId group by activity.user_id",
        nativeQuery = true)
    ArrayList<Object> getTeamTopMinutesPlayed(long teamId);

    /**
     * Query for AC5
     *
     * @param teamId id of team
     * @return list of last 5 recent games/friendlies
     */
    @Query(value = "SELECT * from activity where team_id = :teamId and dtype = 'Game' and "
        + "start_time < :currentTime order by start_time desc limit 5",
        nativeQuery = true)
    List<GameActivity> getTeamRecentGames(long teamId, String currentTime);

    /**
     * Query for AC7
     *
     * @param teamId id of team
     * @return list of activities the team has taken part in
     */
    @Query(value = "SELECT * from activity where team_id = :teamId and dtype='Game' or "
        + "dtype='Friendly' "
        + "order by start_time desc",
        nativeQuery = true)
    List<GameActivity> getTeamActivities(long teamId);

    /**
     * Query for AC7
     *
     * @param teamId id of team
     * @return list of activities the team has taken part in
     */
    @Query(value = "SELECT * from activity where team_id = :teamId and dtype !='Game' and dtype "
        + "!= 'Friendly' "
        + "order by start_time desc",
        nativeQuery = true)
    List<Activity> getTeamOtherActivities(long teamId);



    /**
     * Query for AC5
     *
     * @param teamId id of team
     * @return list of game played
     */
    @Query(value = "SELECT * from activity where team_id = :teamId and "
        + "(dtype = 'Game' or dtype ='Friendly') and start_time <= :currentTime",
        nativeQuery = true)
    List<GameActivity> getGamePlayed(long teamId, String currentTime);
}
