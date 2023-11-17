package nz.ac.canterbury.seng302.tab.repository;

import java.util.List;
import nz.ac.canterbury.seng302.tab.entity.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Access to the club database
 */
public interface ClubRepository extends CrudRepository<Club, Long> {

    List<Club> findAll();

    /**
     * get all the clubs with the pagination size
     *
     * @param pageable the size of pagination
     * @return he paged club entities that matched the search
     */
    Page<Club> findAll(Pageable pageable);

    Club findById(long id);

    /**
     * Search a club with the pagination size
     *
     * @param pageable     the size of pagination
     * @param searchString the string that match the name of the
     *                     club or the locaiton (city and country)
     * @return the paged club entities that matched the search
     */
    @Query(
        value = "SELECT club.* FROM club, location WHERE "
            + "location.location_id = club.address_id AND "
            + "(name LIKE %:searchString% OR city LIKE %:"
            + "searchString% OR country "
            + "LIKE %:searchString%)",
        nativeQuery = true)
    Page<Club> searchClubs(Pageable pageable, String searchString);

}
