package nz.ac.canterbury.seng302.tab.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.Set;
import nz.ac.canterbury.seng302.tab.entity.enums.SupportedSports;

/**
 * Entity to represent an Club
 */
@Entity
@Inheritance
public class Club implements Searchable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_id")
    private Long id;

    /**
     * Location of the team
     */
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "owner_id", referencedColumnName = "user_id")
    private TabUser owner;

    /**
     * f
     * Name of the club
     */
    @Column
    private String name;


    /**
     * Image of the club
     */
    @Column
    private String image = "images/default_club.png";

    /**
     * Sport of the club
     */
    @Column
    @Enumerated(EnumType.STRING)
    private SupportedSports sport;

    /**
     * Location of the team
     */
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "address_id", referencedColumnName = "location_id")
    private Location location;
    /**
     * The teams of the club
     */
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "club_id")
    private Set<Team> teams;

    /**
     * JPA required no-args constructor
     */
    public Club() {
    }

    /**
     * Constructor for Club entity
     *
     * @param name     The name of the club
     * @param sport    The sport the club plays
     * @param location The location of the club
     */
    public Club(String name, SupportedSports sport, Location location,
                TabUser owner, String image) {
        this.name = name;
        this.sport = sport;
        this.location = location;
        this.owner = owner;
        this.image = image;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TabUser getOwner() {
        return owner;
    }

    public void setOwner(TabUser user) {
        this.owner = user;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SupportedSports getSport() {
        return sport;
    }

    public void setSport(SupportedSports sport) {

        this.sport = sport;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s %s", name, sport, teams, location);
    }
}
