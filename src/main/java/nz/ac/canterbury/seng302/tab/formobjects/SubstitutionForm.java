package nz.ac.canterbury.seng302.tab.formobjects;

/**
 * Creates a form object to be used to create a Substitution Statistic.
 */
public class SubstitutionForm {

    /**
     * The activity id of the activity the substitution is associated with
     */
    private Long activityId;

    /**
     * The TabUser id of the player who was substituted
     */
    private Long substitutedPlayerId;

    /**
     * The TabUser id of the substitute player
     */
    private Long substitutePlayerId;

    /**
     * The time the substitution took place in minutes
     */
    private int substituteTime;

    public SubstitutionForm() {
    }

    /**
     * Constructor for the Substitution form
     *
     * @param activityId          The activity id of the activity
     *                            the substitution is associated with
     * @param substitutedPlayerId The TabUser id of the player who was substituted
     * @param substitutePlayerId  The TabUser id of the substitute player
     * @param substituteTime      The time the substitution occurred
     */
    public SubstitutionForm(
        Long activityId,
        Long substitutedPlayerId,
        Long substitutePlayerId,
        int substituteTime) {
        this.activityId = activityId;
        this.substitutedPlayerId = substitutedPlayerId;
        this.substitutePlayerId = substitutePlayerId;
        this.substituteTime = substituteTime;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Long getSubstitutedPlayerId() {
        return substitutedPlayerId;
    }

    public void setSubstitutedPlayerId(Long substitutedPlayerId) {
        this.substitutedPlayerId = substitutedPlayerId;
    }

    public Long getSubstitutePlayerId() {
        return substitutePlayerId;
    }

    public void setSubstitutePlayerId(Long substitutePlayerId) {
        this.substitutePlayerId = substitutePlayerId;
    }

    public int getSubstituteTime() {
        return substituteTime;
    }

    public void setSubstituteTime(int substituteTime) {
        this.substituteTime = substituteTime;
    }

    /**
     * Checks if the form is "empty" (aka consists of default data),
     * used to skip blank forms when adding a collection of forms to the database
     *
     * @return boolean representing if the form is empty or not
     */
    public boolean isEmpty() {
        return substitutedPlayerId == -1 && substitutePlayerId == -1 && substituteTime == 0;
    }
}
