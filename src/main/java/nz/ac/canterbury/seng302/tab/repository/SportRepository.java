package nz.ac.canterbury.seng302.tab.repository;

import java.util.List;
import java.util.Optional;
import nz.ac.canterbury.seng302.tab.entity.Sport;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Access to the 'Sport' database
 */
public interface SportRepository extends CrudRepository<Sport, Long> {
    Optional<Sport> findById(long id);

    @Query(value = "SELECT * FROM sport WHERE name LIKE %?1%",
        nativeQuery = true)
    List<Sport> findByName(String name);

    @Query(value = "SELECT * FROM sport WHERE name LIKE ?1",
        nativeQuery = true)
    Optional<Sport> checkExists(String name);

    List<Sport> findAll();
}
