package nz.ac.canterbury.seng302.tab.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Entity to represent a team
 */
@Entity
@Inheritance
public class Team implements Searchable {

    @ManyToMany
    private final Set<TabUser> members = new HashSet<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private final Set<Activity> activities = new HashSet<>();
    /**
     * Primary key of the table. Automatically generated when a team is added to the database
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;


    /**
     * Name of the team
     */
    @Column(nullable = false)
    private String name;
    /**
     * Sport the team plays
     */
    @Column(nullable = false)
    private String sport;
    /**
     * path to the image
     */
    @Column(nullable = false)
    private String image;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

    /**
     * Creation time of the team
     */
    @Column
    private Timestamp creationDate;

    /**
     * String of current token
     */
    @Column
    private String currentToken;

    /**
     * String of timeout between generations of invite tokens
     */
    @Column
    private String currentTokenRegenTime;


    /**
     * Integer of the wins this team has had
     */
    @Column
    private int wins;


    /**
     * Integer of the draws this team has had
     */
    @Column
    private int draws;
    /**
     * Integer of the losses this team has had
     */
    @Column
    private int losses;

    /**
     * Location of the team
     */
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "address_id", referencedColumnName = "location_id")
    private Location location;

    /**
     * The roles of all the users in the team
     */
    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    private Set<TeamRoles> teamRoles = new HashSet<>();

    /**
     * The total game that the team played
     */
    @Transient
    private int totalGamesPlayed;


    /**
     * JPA required no-args constructor
     */
    protected Team() {
    }

    /**
     * Constructor
     *
     * @param name     name of the team
     * @param sport    sport the team plays
     * @param location location id of the team's Location
     * @param image    string represent the teams profile picture
     */
    public Team(String name, String sport, Location location, String image) {
        this.name = name;
        this.sport = sport;
        this.image = image;
        this.location = location;
    }

    public boolean hasUser(TabUser user) {
        List<TabUser> list = teamRoles.stream().map(TeamRoles::getUser).toList();
        return list.contains(user);
    }


    /**
     * Used to update the team if the user wants to edit its details
     *
     * @param name  new name of the team
     * @param sport new sport the team plays
     */
    public void updateTeam(String name, String sport) {
        this.name = name;
        this.sport = sport;
    }

    public void updateProfilePicture(String image) {
        this.image = image;
    }

    public String getSport() {
        return this.sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getLocationId() {
        return location.getLocationId();
    }

    @Override
    public Location getLocation() {
        return location;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getDraws() {
        return draws;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public Set<TeamRoles> getTeamRoles() {
        return teamRoles;
    }

    public int getTotalGamesPlayed() {
        return totalGamesPlayed;
    }

    public void setTotalGamesPlayed(int totalGamesPlayed) {
        this.totalGamesPlayed = totalGamesPlayed;
    }

    /**
     * Creates csv string in "ID,Name,ProfileImage" format
     *
     * @return created brief csv string
     */
    public String getBriefString() {
        return this.id.toString() + "," + this.name + "," + this.image;
    }

    /**
     * Used to print the team as a string
     *
     * @return String representation of the Team Object
     */
    @Override
    public String toString() {
        return "Team{" + "id=" + id + ", name='" + name + '\'' + ", sport='" + sport + '\''
            + ", locationID='" + location.getLocationId() + '\'' + "}";
    }

    public String getCurrentToken() {
        return currentToken;
    }

    public void setCurrentToken(String currentToken) {
        this.currentToken = currentToken;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    /**
     * Adds a team role to the teamRoles list
     *
     * @param teamRole an entity representing a user belonging to a team with a role
     */
    public void addTeamRole(TeamRoles teamRole) {
        Set<TeamRoles> newTeamRoles = getTeamRoles();
        newTeamRoles.add(teamRole);
        this.teamRoles = newTeamRoles;
    }

    /**
     * Registers the passed in user to the team
     *
     * @param tabUser the user being registered
     */
    public void registerTabUser(TabUser tabUser) {
        members.add(tabUser);
    }

    /**
     * Removes the passed in user to the team
     *
     * @param tabUser the user being registered
     */
    public void removeTabUser(TabUser tabUser) {
        members.removeIf(n -> (Objects.equals(n.getId(), tabUser.getId())));
    }

    public Set<TabUser> getMembers() {
        return members;
    }

}
