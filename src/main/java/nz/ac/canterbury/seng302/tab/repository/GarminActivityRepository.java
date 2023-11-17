package nz.ac.canterbury.seng302.tab.repository;

import nz.ac.canterbury.seng302.tab.entity.Activity;
import nz.ac.canterbury.seng302.tab.entity.garminentities.GarminActivity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


/**
 * Repository for Garmin Activities
 */
public interface GarminActivityRepository extends CrudRepository<GarminActivity, Long> {

    GarminActivity findById(long id);

    void delete(GarminActivity garminActivity);


    GarminActivity findByActivity(Activity activity);


    @Query(value = "SELECT * FROM garmin_activity WHERE "
            + "activity_id = :activityId and user_id = :userId",
            nativeQuery = true)
    GarminActivity findByActivityAndUser(Long activityId, Long userId);


}
