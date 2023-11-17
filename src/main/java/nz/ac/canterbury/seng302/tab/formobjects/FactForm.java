package nz.ac.canterbury.seng302.tab.formobjects;


/**
 * Creates a form object to be used to create a Fact Statistic.
 */
public class FactForm {

    /**
     * The activity id of the activity the fact is associated with
     */
    private Long activityId;

    /**
     * The textual description of the fact
     */
    private String description;

    /**
     * The optional time the fact occurred in minutes
     */
    private Integer timeOccurred;

    /**
     * Constructor for the Fact Form (With the optional timeOccurred parameter)
     *
     * @param activityId   The activity id of the activity the fact is associated with
     * @param description  The description of the fact
     * @param timeOccurred The time the fact occurred (Optional)
     */
    public FactForm(Long activityId, String description, Integer timeOccurred) {
        this.activityId = activityId;
        this.description = description;
        this.timeOccurred = timeOccurred;
    }

    public FactForm() {
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTimeOccurred() {
        return timeOccurred;
    }

    public void setTimeOccurred(Integer timeOccurred) {
        this.timeOccurred = timeOccurred;
    }

    public boolean isEmpty() {
        return description.isEmpty() && timeOccurred == 0;
    }

    @Override
    public String toString() {
        return "Fact{" + "ActivityId: " + activityId + ", Description: " + description + ", Time: "
            + timeOccurred + "}";
    }
}
