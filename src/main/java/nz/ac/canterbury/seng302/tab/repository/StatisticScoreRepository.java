package nz.ac.canterbury.seng302.tab.repository;

import nz.ac.canterbury.seng302.tab.entity.StatisticPlayerScore;
import org.springframework.data.repository.CrudRepository;

/**
 * Allows access to the 'StatisticFact' entities in the DB
 */
public interface StatisticScoreRepository extends CrudRepository<StatisticPlayerScore, Long> {

    /**
     * Gets all the information for a score statistic based on their id
     *
     * @param id long representing the id of the target statistic
     * @return information of the target statistic
     */
    StatisticPlayerScore findById(long id);
}
