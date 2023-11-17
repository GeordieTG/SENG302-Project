package nz.ac.canterbury.seng302.tab.repository;

import java.util.List;
import nz.ac.canterbury.seng302.tab.entity.StatisticSubstitution;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Access to the statistic_substitution database
 */
public interface SubstituteStatisticsRepository
    extends CrudRepository<StatisticSubstitution, Long> {
    /**
     * gets all information for a statistic substitution
     * @param id long representing the id of the substitution statistic
     * @return an entity representation of a substitution statistic
     */
    StatisticSubstitution findById(long id);

    /**
     * Database query to get the statistic substitutin list of the given activity id
     *
     * @param activityId the id of the activity.
     * @return Statistic Substitution list
     */
    @Query(value = "SELECT * FROM statistic_substitution WHERE "
            + "game_activity_id = :activityId order by substitute_time asc",
            nativeQuery = true)
    List<StatisticSubstitution> getStatisticSubstitution(long activityId);

    @Query(value = "SELECT * FROM statistic_substitution WHERE "
            + "game_activity_id = :activityId and "
            + "(substituted_player_user_id = :userId or substitute_player_user_id = :userId)"
            + " order by substitute_time asc",
            nativeQuery = true)
    List<StatisticSubstitution> getPersonalStatisticSubstitution(long activityId, long userId);
}
