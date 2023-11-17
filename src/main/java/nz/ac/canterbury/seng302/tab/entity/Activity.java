package nz.ac.canterbury.seng302.tab.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import nz.ac.canterbury.seng302.tab.entity.garminentities.GarminActivity;
import nz.ac.canterbury.seng302.tab.utility.ConvertingUtil;

/**
 * Entity to represent an activity
 */
@Entity
public class Activity {

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "activity", cascade = CascadeType.ALL)
    private List<StatisticFact> factStatistics = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL)
    private List<GarminActivity> savedGarminActivities;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private TabUser user;

    @Column
    private Long formation;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String startTime;

    @Column(nullable = false)
    private String endTime;

    @Column(nullable = false)
    private String description;


    /**
     * Position string: i.e. "1,56,34,8-33,2,4-85,90,32,31"
     */
    @Column(name = "position")
    private String position;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "address_id", referencedColumnName = "location_id")
    private Location location;

    /**
     * JPA required no-args constructor
     */
    public Activity() {
    }

    /**
     * Constructor for activity object
     *
     * @param user        user that the activity is related to
     * @param team        team that the activity is related to
     * @param type        type of activity
     * @param startTime   start time of the activity
     * @param endTime     end time of the activity
     * @param description description of the activity
     * @param location    location of the activity
     */
    public Activity(TabUser user, Team team, String type, String startTime, String endTime,
                    String description, Location location) {
        this.user = user;
        this.team = team;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.location = location;
    }

    /**
     * Used to update the activity if the user wants to edit its details
     *
     * @param description Activity description
     * @param team        Team the activity is associated with
     * @param type        Type of activity
     * @param startTime   Start time of the activity
     * @param endTime     End time of the activity
     * @param location    Location of the activity
     */
    public void updateActivity(Team team, String type, String startTime, String endTime,
                               String description, Location location) {
        this.team = team;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.location = location;
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void addFactStatistic(StatisticFact factStatistic) {
        factStatistics.add(factStatistic);
    }

    public List<StatisticFact> getFactStatistics() {
        return factStatistics;
    }

    public void setFactStatistics(List<StatisticFact> statisticFacts) {
        this.factStatistics = statisticFacts;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TabUser getUser() {
        return user;
    }

    public void setUser(TabUser user) {
        this.user = user;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    /** Returns "0" if the position is null.
     */
    public String getPosition() {
        if (position == null) {
            return "0";
        }
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Long getFormation() {
        return formation;
    }

    public void setFormation(Long formation) {
        this.formation = formation;
    }

    public long getDuration() {
        return ConvertingUtil.getActivityDuration(startTime, endTime);
    }
}
