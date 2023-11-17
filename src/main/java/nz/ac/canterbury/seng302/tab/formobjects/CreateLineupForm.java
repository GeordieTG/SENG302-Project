package nz.ac.canterbury.seng302.tab.formobjects;

/**
 * A form object to pass the lineup creation details from the frontend to the backend
 */
public class CreateLineupForm {
    private String activityId;
    private String teamId;
    private String formationId;
    private String lineup;

    /**
     * Create Line-Up form for Passing data from front end to back end
     *
     * @param activityId  activity Id
     * @param teamId      Team's Id
     * @param formationId formations Id
     * @param lineup      LineUp's Id
     */
    public CreateLineupForm(String activityId, String teamId, String formationId, String lineup) {
        this.activityId = activityId;
        this.teamId = teamId;
        this.formationId = formationId;
        this.lineup = lineup;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getFormationId() {
        return formationId;
    }

    public void setFormationId(String formationId) {
        this.formationId = formationId;
    }

    public String getLineup() {
        return lineup;
    }

    public void setLineup(String lineup) {
        this.lineup = lineup;
    }
}
