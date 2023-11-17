package nz.ac.canterbury.seng302.tab.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


/**
 * Entity to represent a sport object
 */
@Entity
public class Sport {
    /**
     * name of the sport assumed to be unique
     */

    @Column(nullable = false)
    private String name;
    /**
     * ID of the Sport
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    protected Sport() {
    }

    public Sport(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Sport{" + "id=" + id + ", name='" + name;
    }
}
