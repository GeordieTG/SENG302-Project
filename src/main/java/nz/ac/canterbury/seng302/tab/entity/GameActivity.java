package nz.ac.canterbury.seng302.tab.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import nz.ac.canterbury.seng302.tab.entity.enums.ActivityResult;

/**
 * A subclass of Activity, used to hold statistics for Activities with type Game or Friendly
 */
@Entity
@DiscriminatorValue("Game")
public class GameActivity extends Activity {

    /**
     * The substitution statistics associated with the activity
     */
    @OneToMany(mappedBy = "gameActivity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<StatisticSubstitution> substitutionStatistics = new ArrayList<>();
    /**
     * The current score of the home team of the activity
     */
    @Column
    private String homeScore;
    /**
     * The current score of the opposition team of the activity optional
     */
    @Column
    private String oppositionScore;
    /**
     * The score statistics associated with the activity
     */
    @OneToMany(mappedBy = "gameActivity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<StatisticPlayerScore> playerScoreStatistics = new ArrayList<>();
    /**
     * The activity's outcome
     */
    @Column
    @Enumerated(EnumType.STRING)
    private ActivityResult outcome = ActivityResult.UNDECIDED;

    public GameActivity() {
        // empty constructor for JPA
    }

    /**
     * Game Activity Constructor
     *
     * @param user        the user the activity is associated with
     * @param team        the team associated with the activity
     * @param type        the activity's activity type
     * @param startTime   the start time of the activity
     * @param endTime     the end time of the activity
     * @param description the description of the activity
     * @param location    the location of the activity
     */
    public GameActivity(TabUser user, Team team, String type, String startTime,
                        String endTime, String description, Location location) {
        super(user, team, type, startTime, endTime, description, location);

    }

    /**
     * Game Activity Constructor
     *
     * @param activity the activity to cast
     */
    public GameActivity(Activity activity) {
        super(activity.getUser(), activity.getTeam(), activity.getType(), activity.getStartTime(),
            activity.getEndTime(), activity.getDescription(), activity.getLocation());

    }

    /**
     * Game Activity Constructor
     *
     * @param user            the user the activity is associated with
     * @param team            the team associated with the activity
     * @param type            the activity's activity type
     * @param startAndEndTime the start and end time of the activity
     * @param description     the description of the activity
     * @param location        the location of the activity
     * @param score           the score of the home and opposition team of the activity
     */
    public GameActivity(TabUser user, Team team, String type, List<String> startAndEndTime,
                        String description, Location location,
                        List<String> score) {
        super(user, team, type, startAndEndTime.get(0),
                startAndEndTime.get(1), description, location);
        this.homeScore = score.get(0);
        this.oppositionScore = score.get(1);

    }

    public String getOppositionScore() {
        return oppositionScore;
    }

    public void setOppositionScore(String oppositionScore) {
        this.oppositionScore = oppositionScore;
    }

    public List<StatisticPlayerScore> getPlayerScoreStatistics() {
        return playerScoreStatistics;
    }

    // Getters and setters

    public void setPlayerScoreStatistics(List<StatisticPlayerScore> statistics) {
        this.playerScoreStatistics = statistics;
    }

    public List<StatisticSubstitution> getSubstitutionStatistics() {
        return substitutionStatistics;
    }

    public String getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(String score) {
        this.homeScore = score;
    }

    /**
     * Adds a score statistic to the activity's list of score statistics
     *
     * @param playerScoreStatistics A list of players score statistics to be added
     */
    public void addPlayerScoreStatistic(List<StatisticPlayerScore> playerScoreStatistics) {
        this.playerScoreStatistics = playerScoreStatistics;
    }

    /**
     * Adds a score statistic to the activity's list of substitution statistics
     *
     * @param substitutionStatistics List of Substitution Statistics
     */
    public void addSubstitutionStatistic(List<StatisticSubstitution> substitutionStatistics) {
        this.substitutionStatistics = substitutionStatistics;
    }

    public ActivityResult getOutcome() {
        return outcome;
    }

    public void setOutcome(ActivityResult outcome) {
        this.outcome = outcome;
    }


}
