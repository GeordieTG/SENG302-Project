package nz.ac.canterbury.seng302.tab.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import nz.ac.canterbury.seng302.tab.service.RoleId;

/**
 * Entity to represent the team roles
 */
@Entity
public class TeamRoles {

    /**
     * List of valid roles.
     * Update as required
     */
    private static final List<String> roleList =
        new ArrayList<>(Arrays.asList("Member", "Coach", "Manager"));
    // composite-id key
    @EmbeddedId
    RoleId id;
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    TabUser user;
    @ManyToOne
    @MapsId("teamId")
    @JoinColumn(name = "team_id")
    Team team;
    @Column(name = "ROLE")
    String role;


    /**
     * Total play time the user has played on this team
     */
    @Column(name = "total_time")
    int totalTime;
    /**
     * Total points/score of that user in this team
     */
    @Column(name = "total_points")
    int totalPoints;

    /**
     * Total games played by the user in this team
     */
    @Column(name = "total_games")
    int totalGames;

    /**
     * Default empty constructor for JPA
     */
    public TeamRoles() {
    }


    /**
     * Constructor for creating a non-member role
     *
     * @param team     Team entity
     * @param user     User entity
     * @param userRole String user role in lower case letters
     * @throws IllegalArgumentException Role must be within the accepted list of roles.
     *                                  Else throws IllegalArgumentsException
     */
    public TeamRoles(Team team, TabUser user, String userRole) throws IllegalArgumentException {
        this.id = new RoleId();
        if (roleList.contains(userRole)) {
            this.role = userRole;
        } else {
            throw new IllegalArgumentException("Invalid role");
        }
        this.team = team;
        this.user = user;
        id.setUserId(user.getId());
        id.setTeamId(team.getId());


    }

    /**
     * Constructor to create a default member role.
     * Role is defaulted into "member"
     *
     * @param user TabUser entity
     * @param team Team entity
     */
    public TeamRoles(Team team, TabUser user) {
        this.id = new RoleId();
        this.team = team;
        this.user = user;
        id.setUserId(team.getId());
        id.setTeamId(user.getId());
        role = "Member";
    }

    /**
     * Gets the user in this role
     *
     * @return TabUser user
     */
    public TabUser getUser() {
        return user;
    }

    /**
     * Gets the team in this roleShowToken
     *
     * @return Team team
     */
    public Team getTeam() {
        return team;
    }

    /**
     * Gets the role of the relation
     *
     * @return String role a users role in the team
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role of the relation
     *
     * @param role a users role in the team
     */
    public void setRole(String role) {
        this.role = role;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public int getTotalGames() {
        return totalGames;
    }

    public void setTotalGames(int totalGames) {
        this.totalGames = totalGames;
    }

    public void setId(RoleId id) {
        this.id = id;
    }
}
