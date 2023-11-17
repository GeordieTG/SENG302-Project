package nz.ac.canterbury.seng302.tab.formobjects;

import java.util.ArrayList;
import java.util.List;
import nz.ac.canterbury.seng302.tab.entity.StatisticFact;

/**
 * THe statistics form hold the information of the activity
 * The info will pass from the front end to the backend
 * holds a collection of the different activity statistic types
 */
public class StatisticsForm {

    private Long activityId;
    private List<PlayerScoreForm> playerScoreForms = new ArrayList<>();

    private ScoreForm scoreForm;
    private boolean firstTime = true;

    private List<SubstitutionForm> substitutionForms = new ArrayList<>();
    private List<FactForm> factForms = new ArrayList<>();

    public StatisticsForm(Long activityId) {
        this.activityId = activityId;
    }

    /**
     * Constructor an object to hold activity forms while they are passed to the DB
     *
     * @param activityId     the id of the activity
     * @param statisticFacts a list of activity facts
     */
    public StatisticsForm(Long activityId, List<StatisticFact> statisticFacts) {
        this.activityId = activityId;
        for (StatisticFact statisticFact : statisticFacts) {
            this.factForms.add(new FactForm(statisticFact.getActivity().getId(),
                statisticFact.getDescription(), statisticFact.getTimeOccurred()));
        }
    }

    public StatisticsForm() {
    }

    public List<FactForm> getFactForms() {
        return factForms;
    }

    public void setFactForms(List<FactForm> factForms) {
        this.factForms = factForms;
    }

    public List<PlayerScoreForm> getPlayerScoreForms() {
        return playerScoreForms;
    }

    public void setPlayerScoreForms(List<PlayerScoreForm> playerScoreForms) {
        this.playerScoreForms = playerScoreForms;
    }

    public ScoreForm getScoreForm() {
        return this.scoreForm;
    }

    public void setScoreForm(ScoreForm scoreForm) {
        this.scoreForm = scoreForm;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public List<SubstitutionForm> getSubstitutionForms() {
        return substitutionForms;
    }

    public void setSubstitutionForms(
        List<SubstitutionForm> substitutionForms) {
        this.substitutionForms = substitutionForms;
    }

    public boolean isFirstTime() {
        return firstTime;
    }

    public void setFirstTime(boolean firstTime) {
        this.firstTime = firstTime;
    }

}
