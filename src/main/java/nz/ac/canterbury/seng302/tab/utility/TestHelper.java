package nz.ac.canterbury.seng302.tab.utility;

import java.util.List;
import nz.ac.canterbury.seng302.tab.entity.enums.ActivityResult;
import nz.ac.canterbury.seng302.tab.entity.garminentities.GarminDaily;
import nz.ac.canterbury.seng302.tab.entity.garminentities.HeartRateData;
import nz.ac.canterbury.seng302.tab.formobjects.ExerciseGoalsForm;
import nz.ac.canterbury.seng302.tab.formobjects.PlayerScoreForm;
import nz.ac.canterbury.seng302.tab.formobjects.ScoreForm;
import nz.ac.canterbury.seng302.tab.formobjects.StatisticsForm;

/**
 * A utility class for creating different types of test cases for statistics forms.
 */
public class TestHelper {

    private TestHelper() {
    }

    /**
     * Creates a statistics form with invalid total points.
     *
     * @return A statistics form with invalid total points.
     */
    public static StatisticsForm createInvalidTotalPointsForm() {
        StatisticsForm statisticsForm = new StatisticsForm();
        statisticsForm.setPlayerScoreForms(List.of(new PlayerScoreForm(1L, 1L, 3, 1)));
        statisticsForm.setScoreForm(new ScoreForm("1", 1L, "1", ActivityResult.WON));
        return statisticsForm;
    }

    /**
     * Creates a statistics form with invalid timestamp.
     *
     * @return A statistics form with invalid timestamp.
     */
    public static StatisticsForm createInvalidTimeStampForm() {
        StatisticsForm statisticsForm = new StatisticsForm();
        statisticsForm.setPlayerScoreForms(List.of(new PlayerScoreForm(1L, 1L, 1, 61)));
        statisticsForm.setScoreForm(new ScoreForm("1", 1L, "1", ActivityResult.WON));
        return statisticsForm;
    }

    /**
     * Creates a statistics form with valid home score but no away score.
     *
     * @return A statistics form with valid home score but no away score.
     */
    public static StatisticsForm createValidNoHomeScoreForm() {
        StatisticsForm statisticsForm = new StatisticsForm();
        statisticsForm.setPlayerScoreForms(List.of(new PlayerScoreForm(1L, 1L, 1, 61)));
        statisticsForm.setScoreForm(new ScoreForm("", 1L, "1", ActivityResult.WON));
        return statisticsForm;
    }

    /**
     * Creates an exercise goals form with all valid values
     * @return An exercise goal form with valid values
     */
    public static ExerciseGoalsForm createFullValidExerciseGoalForm() {
        ExerciseGoalsForm exerciseGoalsForm = new ExerciseGoalsForm();
        exerciseGoalsForm.setSteps(3645);
        exerciseGoalsForm.setCaloriesBurnt(798f);
        exerciseGoalsForm.setDistanceTravelled(1.6f);
        exerciseGoalsForm.setTotalActivityTime(45f);
        return  exerciseGoalsForm;
    }

    /**
     * Creates an exercise goals form with a subset of valid values
     * @return An exercise goal form with a subset valid values
     */
    public static ExerciseGoalsForm createSubsetValidExerciseGoalForm() {
        ExerciseGoalsForm exerciseGoalsForm = new ExerciseGoalsForm();
        exerciseGoalsForm.setSteps(3645);
        exerciseGoalsForm.setTotalActivityTime(45f);
        return  exerciseGoalsForm;
    }

    /**
     * Creates an exercise goals form with all values being the lowest valid value
     * @return An exercise goals form with all values being the lowest valid value
     */
    public static ExerciseGoalsForm createLowerBoundValidExerciseGoalForm() {
        ExerciseGoalsForm exerciseGoalsForm = new ExerciseGoalsForm();
        exerciseGoalsForm.setSteps(1);
        exerciseGoalsForm.setCaloriesBurnt(1f);
        exerciseGoalsForm.setDistanceTravelled(0.001f);
        exerciseGoalsForm.setTotalActivityTime(1f);
        return  exerciseGoalsForm;
    }

    /**
     * Creates an exercise goals form with all values being the highest valid value
     * @return An exercise goals form with all values being the highest valid value
     */
    public static ExerciseGoalsForm createUpperBoundValidExerciseGoalForm() {
        ExerciseGoalsForm exerciseGoalsForm = new ExerciseGoalsForm();
        exerciseGoalsForm.setSteps(150000);
        exerciseGoalsForm.setCaloriesBurnt(50000f);
        exerciseGoalsForm.setDistanceTravelled(500f);
        exerciseGoalsForm.setTotalActivityTime(1440f);
        return  exerciseGoalsForm;
    }

    /**
     * Creates an ExerciseGoalForm
     * @param activeKilocalories Calories burnt goal
     * @param activeTimeMins Activity time goal
     * @param steps Daily steps goal
     * @param distanceInKm Distance goal in kms
     * @return The ExerciseGoalForm
     */
    public static ExerciseGoalsForm createNormalValidExerciseGoalForm(Float activeKilocalories,
                                                                      Float activeTimeMins,
                                                                      Integer steps,
                                                                      Float distanceInKm) {
        ExerciseGoalsForm exerciseGoalsForm = new ExerciseGoalsForm();
        exerciseGoalsForm.setSteps(steps);
        exerciseGoalsForm.setCaloriesBurnt(activeKilocalories);
        exerciseGoalsForm.setDistanceTravelled(distanceInKm);
        exerciseGoalsForm.setTotalActivityTime(activeTimeMins);
        return exerciseGoalsForm;
    }

    /**
     * Creates a GarminDaily object
     * @param activeKilocalories Calories burnt goal
     * @param activeTimeHrs Activity time goal
     * @param steps Daily steps goal
     * @param distanceInMetres Distance goal in metres
     * @return The GarminDaily object
     */
    public static GarminDaily createNormalValidGarminDaily(double activeKilocalories,
                                                           double activeTimeHrs,
                                                           int steps,
                                                           double distanceInMetres) {
        HeartRateData heartRateData = new HeartRateData(0, 0, 0, 0);
        return new GarminDaily(activeKilocalories,
            activeTimeHrs,
            steps,
            5000,
                distanceInMetres,
            heartRateData,
            true);
    }


}
