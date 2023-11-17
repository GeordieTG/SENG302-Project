package nz.ac.canterbury.seng302.tab.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import nz.ac.canterbury.seng302.tab.formobjects.ExerciseGoalsForm;

/**
 * Used to store the users exercise goals
 */
@Entity
public class ExerciseGoals {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_goals_id")
    private Long id;

    /**
     * Users step goal
     */
    @Column
    private Integer steps;

    /**
     * Users calories burnt goal
     */
    @Column
    private Float caloriesBurnt;

    /**
     * Users distance travelled goal
     */
    @Column
    private Float distanceTravelled;

    /**
     * Users total activity time goal
     */
    @Column
    private Float totalActivityTime;

    @OneToOne
    @JoinColumn(name = "user_id")
    private TabUser user;

    /**
     * Empty constructor for JPA
     */
    public ExerciseGoals() {}

    /**
     * Converts a form object to an entity
     * @param exerciseGoalsForm form submitted from the user in the Set Goal Modal
     */
    public ExerciseGoals(ExerciseGoalsForm exerciseGoalsForm, TabUser user) {
        this.steps = exerciseGoalsForm.getSteps();
        this.caloriesBurnt = exerciseGoalsForm.getCaloriesBurnt();
        this.distanceTravelled = exerciseGoalsForm.getDistanceTravelled();
        this.totalActivityTime = exerciseGoalsForm.getTotalActivityTime();
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getCaloriesBurnt() {
        return caloriesBurnt;
    }


    public Float getDistanceTravelled() {
        return distanceTravelled;
    }

    public Integer getSteps() {
        return steps;
    }

    public void setSteps(Integer steps) {
        this.steps = steps;
    }

    public Float getTotalActivityTime() {
        return totalActivityTime;
    }

    public void setCaloriesBurnt(Float caloriesBurnt) {
        this.caloriesBurnt = caloriesBurnt;
    }

    public void setDistanceTravelled(Float distanceTravelled) {
        this.distanceTravelled = distanceTravelled;
    }

    public void setTotalActivityTime(Float totalActivityTime) {
        this.totalActivityTime = totalActivityTime;
    }

    public void setUser(TabUser user) {
        this.user = user;
    }
}
