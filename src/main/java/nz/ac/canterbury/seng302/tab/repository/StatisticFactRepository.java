package nz.ac.canterbury.seng302.tab.repository;

import nz.ac.canterbury.seng302.tab.entity.StatisticFact;
import org.springframework.data.repository.CrudRepository;

/**
 * Allows access to the 'StatisticFact' entities in the DB
 */
public interface StatisticFactRepository extends CrudRepository<StatisticFact, Long> {

    /**
     * Gets all the information for a user based on their id
     *
     * @param id the if of the target user
     * @return FormResult the information of the target user
     */
    StatisticFact findById(long id);
}
