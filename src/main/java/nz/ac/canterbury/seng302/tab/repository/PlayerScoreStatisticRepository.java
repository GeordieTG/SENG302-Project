package nz.ac.canterbury.seng302.tab.repository;

import java.util.List;
import nz.ac.canterbury.seng302.tab.entity.StatisticPlayerScore;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Access to the statistic_player_score database
 */
public interface PlayerScoreStatisticRepository extends CrudRepository<StatisticPlayerScore, Long> {

    /**
     * Get the player score of the given activity id
     *
     * @param activityId the id of the activity
     * @return Statistic player score list
     */
    @Query(value = "SELECT * FROM statistic_player_score WHERE "
        + "game_activity_id = :activityId order by scored_time asc",
        nativeQuery = true)
    List<StatisticPlayerScore> getStatisticPlayerScore(long activityId);

    /**
     * Get the currently logged in players scoring events of a given activity
     *
     * @param activityId the id of the activity
     * @param userId     the id of the currently logged-in user
     * @return Statistic player score list
     */
    @Query(value = "SELECT * FROM statistic_player_score WHERE "
        + "game_activity_id = :activityId and user_id = :userId order by scored_time asc",
        nativeQuery = true)
    List<StatisticPlayerScore> getPersonalScoringEventsForActivity(long activityId, long userId);

}
