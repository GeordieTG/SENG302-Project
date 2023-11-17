package nz.ac.canterbury.seng302.tab.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

/**
 * Entity Object representing the score of an activity
 */
@Entity
public class StatisticScore {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_activity_id")
    private GameActivity gameActivity;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private TabUser user;

    @Column(nullable = false)
    private int score;

    @Column(nullable = false)
    private LocalDateTime scoredTime;

    /**
     * Empty Constructor for JPA
     */
    public StatisticScore() {
        // ...
    }

    /**
     * Constructor
     *
     * @param gameActivity entity object representing the Game Activity
     * @param user         Entity object representing the current user
     * @param score        Integer representing the amount scored
     * @param scoredTime   LocalDateTime representing the time goal/event was scored
     */
    public StatisticScore(GameActivity gameActivity, TabUser user, int score,
                          LocalDateTime scoredTime) {
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

    public LocalDateTime getScoredTime() {
        return scoredTime;
    }

    public void setScoredTime(LocalDateTime scoredTime) {
        this.scoredTime = scoredTime;
    }
}

