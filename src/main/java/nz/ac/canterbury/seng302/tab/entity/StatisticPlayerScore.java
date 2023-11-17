package nz.ac.canterbury.seng302.tab.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * An entity, representing a Score Statistic
 */
@Entity
public class StatisticPlayerScore {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * The activity the score statistic is associated with
     */
    @ManyToOne
    @JoinColumn(name = "game_activity_id")
    private GameActivity gameActivity;

    /**
     * The Player that scored
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private TabUser user;

    /**
     * The amount scored
     */
    @Column(nullable = false)
    private int score;

    /**
     * The time the score took place in minutes
     */
    @Column(nullable = false)
    private int scoredTime;

    public StatisticPlayerScore() {
        // ...
    }

    /**
     * Constructor for Score Entity
     *
     * @param gameActivity The activity the score is from
     * @param user         The Player that scored
     * @param score        The amount of points the player scored
     * @param scoredTime   The time the Score took place
     */
    public StatisticPlayerScore(GameActivity gameActivity, TabUser user, int score,
                                int scoredTime) {
        this.gameActivity = gameActivity;
        this.user = user;
        this.score = score;
        this.scoredTime = scoredTime;
    }

    public GameActivity getGameActivity() {
        return gameActivity;
    }

    public void setGameActivity(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
    }

    public TabUser getUser() {
        return user;
    }

    public void setUser(TabUser user) {
        this.user = user;
    }

    public int getScoredTime() {
        return scoredTime;
    }

    public void setScoredTime(int scoredTime) {
        this.scoredTime = scoredTime;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Long getId() {
        return id;
    }
}

