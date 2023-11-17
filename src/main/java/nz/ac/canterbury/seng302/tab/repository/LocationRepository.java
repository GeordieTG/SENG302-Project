package nz.ac.canterbury.seng302.tab.repository;

import java.util.List;
import java.util.Optional;
import nz.ac.canterbury.seng302.tab.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Access to the 'location' database
 */
public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByLocationId(long id);

    /**
     * Gets all the cities known to the DB
     *
     * @return String list of the information of the target user
     */
    @Query(value = "SELECT DISTINCT(CITY) FROM location", nativeQuery = true)
    List<String> findCities();
}
