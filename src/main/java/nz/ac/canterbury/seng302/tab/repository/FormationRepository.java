package nz.ac.canterbury.seng302.tab.repository;

import java.util.List;
import java.util.Optional;
import nz.ac.canterbury.seng302.tab.entity.Formation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Access to the 'location' database
 */
public interface FormationRepository extends CrudRepository<Formation, Long> {
    Optional<Formation> findById(long id);

    @Query(value = "SELECT * FROM formation where formation_id = :formationId", nativeQuery = true)
    Formation findByIdNotOptional(long formationId);

    @Query(value = "SELECT * FROM formation where team_id = :teamId", nativeQuery = true)
    List<Formation> findAllFormationsByTeamId(Long teamId);

    @Query(value = "UPDATE formation SET position = :positionString WHERE formation_id = "
        + ":formationId", nativeQuery = true)
    void updatePosition(Long formationId, String positionString);
}
