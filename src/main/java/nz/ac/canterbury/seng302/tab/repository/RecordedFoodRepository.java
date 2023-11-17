package nz.ac.canterbury.seng302.tab.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import nz.ac.canterbury.seng302.tab.entity.RecordedFood;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Access to the 'Food' database
 */
public interface RecordedFoodRepository extends CrudRepository<RecordedFood, Long> {


    RecordedFood findById(long id);

    List<RecordedFood> findAll();

    /**
     * Retrieve top x most recent RecordedFood entities recorded for the given user
     * @param userId user id
     * @param limitBy x
     * @return ArrayList of RecordedFood in length of x
     */
    @Query(value = "SELECT * FROM recorded_food WHERE user_id = :userId AND meal_id IS null "
            + "ORDER BY date_recorded DESC LIMIT :limitBy", nativeQuery = true)
    ArrayList<RecordedFood> getRecentFoods(long userId, int limitBy);


    @Query(value = "SELECT * FROM recorded_food WHERE user_id = :userId AND meal_id IS null "
        + "AND CAST(date_recorded AS DATE) = :specifiedDate", nativeQuery = true)
    List<RecordedFood> getUsersRecordedFoodsWhereDate(long userId, LocalDate specifiedDate);
}
