package nz.ac.canterbury.seng302.tab.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Authority entity
 */
@Entity
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authority_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private TabUser user;

    @Column()
    private String role;

    /**
     * Authority empty JPA constructor
     */
    protected Authority() {
        // JPA empty constructor
    }

    /**
     * Add authority role.
     *
     * @param role this users roll
     */
    public Authority(String role) {
        this.role = role;
    }

    /**
     * Get role
     *
     * @return Authority Role
     */
    public String getRole() {
        return role;
    }
}
