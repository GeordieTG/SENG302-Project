package nz.ac.canterbury.seng302.tab.formobjects;

import nz.ac.canterbury.seng302.tab.entity.enums.ActivityResult;


/**
 * Creates a form object to be used on the editActivity page
 */
public class ScoreForm {

    private String homeScore;
    private Long activityId;
    private String oppositionScore;
    private ActivityResult activityResult;

    public ScoreForm(String homeScore, Long activityId) {
        this.homeScore = homeScore;
        this.activityId = activityId;
    }


    /**
     * Form constructor
     *
     * @param homeScore       score for the home team
     * @param activityId      id of an activity
     * @param oppositionScore score for the opposition team
     */
    public ScoreForm(String homeScore, Long activityId, String oppositionScore) {
        this.homeScore = homeScore;
        this.activityId = activityId;
        this.oppositionScore = oppositionScore;
    }

    /**
     * Form constructor
     *
     * @param homeScore       score for the home team
     * @param activityId      id of an activity
     * @param oppositionScore score for the opposition team
     * @param activityResult  outcome of the activity
     */
    public ScoreForm(String homeScore, Long activityId, String oppositionScore,
                     ActivityResult activityResult) {
        this.homeScore = homeScore;
        this.activityId = activityId;
        this.oppositionScore = oppositionScore;
        this.activityResult = activityResult;
    }

    public ScoreForm() {

    }

    public String getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(String homeScore) {
        this.homeScore = homeScore;
    }

    public String getOppositionScore() {
        return oppositionScore;
    }

    public void setOppositionScore(String oppositionScore) {
        this.oppositionScore = oppositionScore;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }


    public ActivityResult getActivityResult() {
        return activityResult;
    }

    public void setActivityResult(ActivityResult activityResult) {
        this.activityResult = activityResult;
    }
}
