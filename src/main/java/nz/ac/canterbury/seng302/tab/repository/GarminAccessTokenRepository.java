package nz.ac.canterbury.seng302.tab.repository;

import nz.ac.canterbury.seng302.tab.entity.GarminAccessToken;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for the Garmin Access Token. Made up of a public token and a secret token.
 */
public interface GarminAccessTokenRepository extends CrudRepository<GarminAccessToken, Long> {
    @Query(value = "SELECT * FROM garmin_access_token WHERE user_id = :userId",
        nativeQuery = true)
    GarminAccessToken checkIfConnected(Long userId);


}
