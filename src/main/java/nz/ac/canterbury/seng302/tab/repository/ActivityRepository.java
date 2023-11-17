package nz.ac.canterbury.seng302.tab.repository;

import jakarta.transaction.Transactional;
import java.util.List;
import nz.ac.canterbury.seng302.tab.entity.Activity;
import nz.ac.canterbury.seng302.tab.entity.GameActivity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Access to the 'Activity' database
 */
public interface ActivityRepository extends CrudRepository<Activity, Long> {


    Activity findById(long id);

    List<Activity> findAll();

    @Query(value = "SELECT * FROM activity WHERE team_id = :teamId order by start_time",
        nativeQuery = true)
    List<Activity> getTeamActivityById(long teamId);

    @Query(value = "SELECT * FROM activity WHERE user_id = :userId and "
        + "team_id is null order by start_time",
        nativeQuery = true)
    List<Activity> getPersonalActivityByUserId(long userId);

    @Query(value = "SELECT * FROM statistic_fact WHERE activity_id = :activityId order by "
        + "time_occurred",
        nativeQuery = true)
    List<Object> getActivityFacts(long activityId);


    @Query(value = "SELECT * FROM activity WHERE dtype = 'Game' and activity_id = :activityId",
        nativeQuery = true)
    GameActivity getGameActivityById(long activityId);


    @Query(value = "SELECT COUNT(*) FROM activity WHERE team_id = :teamId AND (DType = 'Game' OR "
        + "Dtype = 'Friendly')", nativeQuery = true)
    int getTotalGamesTeamPlayed(long teamId);

    @Query(value = "SELECT COUNT(*) FROM statistic_fact", nativeQuery = true)
    int getTotalFacts();

    @Query(value = "SELECT COUNT(*) FROM statistic_player_score", nativeQuery = true)
    int getTotalScoreStats();

    @Query(value = "SELECT COUNT(*) FROM ", nativeQuery = true)
    int getTotalSubstitutionStats();

    @Query(value = "delete from statistic_fact where activity_id = :activityId and "
        + "id not in :goodIds",
        nativeQuery = true)
    @Transactional
    @Modifying
    void deleteStatFactsByIdNotIn(@Param("activityId") long activityId,
                                  @Param("goodIds") List<Long> goodIds);

    @Query(value = "delete from statistic_player_score where game_activity_id = "
        + ":activityId and id not in :goodIds",
        nativeQuery = true)
    @Transactional
    @Modifying
    void deleteStatScoreByIdNotIn(@Param("activityId") long activityId,
                                  @Param("goodIds") List<Long> goodIds);

    @Query(value = "delete from statistic_substitution "
            + "where game_activity_id = :activityId and id not in :goodIds",
            nativeQuery = true)
    @Transactional
    @Modifying
    void deleteStatSubByIdNoIn(long activityId, List<Long> goodIds);

    @Query(value = "SELECT COUNT(*) FROM activity WHERE team_id = :teamId AND (DType = 'Game' OR "
        + "Dtype = 'Friendly') AND outcome != 'UNDECIDED'", nativeQuery = true)
    int getTotalGamesTeamPlayedWithStatistics(long teamId);
}
