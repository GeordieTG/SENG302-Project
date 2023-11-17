package nz.ac.canterbury.seng302.tab.formobjects;


/**
 * Creates a form object to be used to create a Player Score Statistic.
 */
public class PlayerScoreForm {

    /**
     * The activity id of the activity the  Player Score is associated with
     */
    private Long activityId;

    /**
     * The id of the player who scored
     */
    private Long scoredPlayerId;

    /**
     * The amount the player scored
     */
    private int score;


    /**
     * The time the player scored in minutes
     */
    private int scoreTime;

    /**
     * Constructor for the Player Score Form
     *
     * @param activityId     The activity the Player Score is associated with
     * @param scoredPlayerId The TabUser id of the player who scored
     * @param score          The amount the player scored
     * @param scoreTime      The time the player scored
     */
    public PlayerScoreForm(Long activityId, Long scoredPlayerId, int score, int scoreTime) {
        this.activityId = activityId;
        this.scoredPlayerId = scoredPlayerId;
        this.score = score;
        this.scoreTime = scoreTime;
    }

    public PlayerScoreForm() {

    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getScoredPlayerId() {
        return scoredPlayerId;
    }

    public void setScoredPlayerId(Long scoredPlayerId) {
        this.scoredPlayerId = scoredPlayerId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScoreTime() {
        return scoreTime;
    }

    public void setScoreTime(int scoreTime) {
        this.scoreTime = scoreTime;
    }

    /**
     * Checks if the form is empty or consists of default values
     *
     * @return returns true if form is empty
     */
    public boolean isEmpty() {
        return scoreTime == 0 && score == 0 && scoredPlayerId == -1;
    }
}
