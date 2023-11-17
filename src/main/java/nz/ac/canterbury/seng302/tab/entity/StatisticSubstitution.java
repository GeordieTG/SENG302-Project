package nz.ac.canterbury.seng302.tab.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Objects;

/**
 * Entity object representing statistics of substiutions
 */
@Entity
public class StatisticSubstitution {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * The activity the substitution statistic is associated with
     */
    @ManyToOne
    @JoinColumn(name = "game_activity_id")
    private GameActivity gameActivity;

    /**
     * The player that was substituted
     */
    @ManyToOne
    @JoinColumn(name = "substituted_player_user_id")
    private TabUser substitutedPlayer;

    /**
     * The player that was the substitute
     */
    @ManyToOne
    @JoinColumn(name = "substitute_player_user_id")
    private TabUser substitutePlayer;

    /**
     * The time the substitution took place in minutes
     */
    @Column(nullable = false)
    private int substituteTime;

    /**
     * Empty Constructor for JPA
     */
    public StatisticSubstitution() {
        // ...
    }

    /**
     * Constructor for StatisticSubstitution
     *
     * @param gameActivity      The activity associated with the statistic
     * @param substitutedPlayer The player that was substituted
     * @param substitutePlayer  The substitute player
     * @param substituteTime    The time the substitution took place
     */
    public StatisticSubstitution(GameActivity gameActivity, TabUser substitutedPlayer,
                                 TabUser substitutePlayer, int substituteTime) {
        this.gameActivity = gameActivity;
        this.substitutedPlayer = substitutedPlayer;
        this.substitutePlayer = substitutePlayer;
        this.substituteTime = substituteTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GameActivity getGameActivity() {
        return gameActivity;
    }

    public void setGameActivity(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
    }

    public TabUser getSubstitutedPlayer() {
        return substitutedPlayer;
    }

    public void setSubstitutedPlayer(TabUser substitutedPlayer) {
        this.substitutedPlayer = substitutedPlayer;
    }

    public TabUser getSubstitutePlayer() {
        return substitutePlayer;
    }

    public void setSubstitutePlayer(TabUser substitutePlayer) {
        this.substitutePlayer = substitutePlayer;
    }

    public int getSubstituteTime() {
        return substituteTime;
    }

    public void setSubstituteTime(int substituteTime) {
        this.substituteTime = substituteTime;
    }


    @Override
    public boolean equals(Object statistic) {
        if (statistic == this) {
            return true;
        }
        if (!(statistic instanceof StatisticSubstitution castedStatistic)) {
            return false;
        }
        return castedStatistic.getId() == this.id
            && castedStatistic.substitutedPlayer.equals(this.substitutedPlayer)
            && castedStatistic.substitutePlayer.equals(this.substitutePlayer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, substitutedPlayer, substitutePlayer);
    }
}
