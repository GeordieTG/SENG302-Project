package nz.ac.canterbury.seng302.tab.repository;

import java.util.List;
import java.util.Optional;
import nz.ac.canterbury.seng302.tab.entity.TokenDump;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Access to the Token dumb database
 */
public interface TokenDumpRepository extends CrudRepository<TokenDump, Long> {
    /**
     * Finds Token by id
     *
     * @param id long representing the token id
     * @return Token Entity
     */
    Optional<TokenDump> findById(long id);

    /**
     * Finds all tokens in database
     *
     * @return List of all token entities
     */
    List<TokenDump> findAll();

    /**
     * Finds token by the token string
     *
     * @param token String representing token
     * @return String of the token if found
     */
    @Query(value = "SELECT token FROM token_dump WHERE token = :token",
        nativeQuery = true)
    Optional<String> findByToken(String token);

    @Query(value = "SELECT COUNT(token) FROM token_dump WHERE token = :token", nativeQuery = true)
    int findInstanceOfToken(String token);
}
