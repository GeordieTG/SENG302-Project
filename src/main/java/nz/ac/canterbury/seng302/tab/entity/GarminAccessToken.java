package nz.ac.canterbury.seng302.tab.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

/**
 * Created in the third and final step of the OAuth process.
 * It is used as a user credential to access the Garmin API for all requests after set up. Made
 * up of a public token and a secret token. There will only ever be one token per Garmin Account.
 */
@Entity
public class GarminAccessToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private TabUser user;

    @Column(name = "token")
    private String token;
    /**
     * the field
     */
    @Column(name = "secret_token")
    private String secretToken;


    public GarminAccessToken() {
    }

    public GarminAccessToken(TabUser user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public TabUser getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSecretToken() {
        return secretToken;
    }

    public void setSecretToken(String secretToken) {
        this.secretToken = secretToken;
    }
}
