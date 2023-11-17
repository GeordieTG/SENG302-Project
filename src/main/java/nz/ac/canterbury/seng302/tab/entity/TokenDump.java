package nz.ac.canterbury.seng302.tab.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


/**
 * Entity representing a token
 */
@Entity
public class TokenDump {
    /**
     * Id of the token
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long id;

    /**
     * String representation of token
     */
    @Column
    private String token;

    /**
     * Empty Constructor
     */
    protected TokenDump() {
    }

    /**
     * Constructor
     *
     * @param token String representation of token
     */
    public TokenDump(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

}
