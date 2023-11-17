package nz.ac.canterbury.seng302.tab.formobjects;

import java.util.Objects;
import nz.ac.canterbury.seng302.tab.entity.ExerciseGoals;

/**
 * Used to pass the users goals from the front-end to the back-end
 */
public class ExerciseGoalsForm {

    /**
     * Users step goal
     */
    private Integer steps;

    private Long id;

    /**
     * Users calories burnt goal
     */
    private Float caloriesBurnt;

    /**
     * Users distance travelled goal
     */
    private Float distanceTravelled;

    /**
     * Users total activity time goal
     */
    private Float totalActivityTime;

    /**
     * Users progress towards completing their steps goal
     */
    private Integer stepsProgress;

    /**
     * Users progress towards completing their calories goal
     */
    private Integer caloriesProgress;

    /**
     * Users progress towards completing their distance goal
     */
    private Integer distanceProgress;

    /**
     * Users progress towards completing their time goal
     */
    private Integer timeProgress;

    /**
     * Keeps track of how many goals the user has
     */
    private Integer goalCount = 0;

    /**
     * Constructor for exercise goal form
     * @param exerciseGoals exercise goal entity
     */
    public ExerciseGoalsForm(ExerciseGoals exerciseGoals) {
        this.id = exerciseGoals.getId();
        this.steps = exerciseGoals.getSteps();
        this.caloriesBurnt = exerciseGoals.getCaloriesBurnt();
        this.distanceTravelled = exerciseGoals.getDistanceTravelled();
        this.totalActivityTime = exerciseGoals.getTotalActivityTime();
    }

    public ExerciseGoalsForm() {
        // Empty constructor for JPA
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Integer getSteps() {
        return steps;
    }

    public void setSteps(Integer steps) {
        this.steps = steps;
    }

    public Float getCaloriesBurnt() {
        return caloriesBurnt;
    }

    public void setCaloriesBurnt(Float caloriesBurnt) {
        this.caloriesBurnt = caloriesBurnt;
    }

    public Float getDistanceTravelled() {
        return distanceTravelled;
    }

    public void setDistanceTravelled(Float distanceTravelled) {
        this.distanceTravelled = distanceTravelled;
    }

    public Float getTotalActivityTime() {
        return totalActivityTime;
    }

    public void setTotalActivityTime(Float totalActivityTime) {
        this.totalActivityTime = totalActivityTime;
    }

    public Integer getStepsProgress() {
        return stepsProgress;
    }

    public void setStepsProgress(Integer stepsProgress) {
        this.stepsProgress = stepsProgress;
    }

    public Integer getCaloriesProgress() {
        return caloriesProgress;
    }

    public void setCaloriesProgress(Integer caloriesProgress) {
        this.caloriesProgress = caloriesProgress;
    }

    public Integer getDistanceProgress() {
        return distanceProgress;
    }

    public void setDistanceProgress(Integer distanceProgress) {
        this.distanceProgress = distanceProgress;
    }

    public Integer getTimeProgress() {
        return timeProgress;
    }

    public void setTimeProgress(Integer timeProgress) {
        this.timeProgress = timeProgress;
    }


    /**
     * Gets the total progress towards daily goal completions
     * @return Integer representing the percentage of progress
     */
    public Integer getTotalProgress() {
        return this.goalCount == 0 ? 0 : (this.stepsProgress
            + this.caloriesProgress
            + this.distanceProgress
            + this.timeProgress) / this.goalCount;
    }

    @Override
    public String toString() {
        return "ExerciseGoalsForm{Steps:" + steps
            + ", Calories Burnt: " + caloriesBurnt
            +  ", Distance Travelled: " + distanceTravelled
            + ", Total Activity Time: " + totalActivityTime + "}";
    }

    /**
     * Checks if the user has no goals
     * @return true if the user has no goals, false otherwise
     */
    public boolean isEmpty() {
        return Objects.isNull(this.steps)
            && Objects.isNull(this.caloriesBurnt)
            && Objects.isNull(this.distanceTravelled)
            && Objects.isNull(this.totalActivityTime);
    }

    public Integer getGoalCount() {
        return goalCount;
    }

    public void increaseGoalCount(Integer goalCount) {
        this.goalCount += goalCount;
    }
}
