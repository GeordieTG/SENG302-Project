package nz.ac.canterbury.seng302.tab.service;

import java.io.IOException;
import nz.ac.canterbury.seng302.tab.entity.ExerciseGoals;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.entity.garminentities.GarminDaily;
import nz.ac.canterbury.seng302.tab.formobjects.ExerciseGoalsForm;
import nz.ac.canterbury.seng302.tab.repository.ExerciseGoalsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for ExerciseGoals
 */
@Service
public class ExerciseGoalsService {

    @Autowired
    ExerciseGoalsRepository exerciseGoalsRepository;

    public void save(ExerciseGoals exerciseGoals) {
        exerciseGoalsRepository.save(exerciseGoals);
    }

    public ExerciseGoals getByUser(TabUser user) {
        return exerciseGoalsRepository.findByUser(user);
    }

    /**
     * Update the exercise goal form
     * @param exerciseGoalsForm the existing exercise goal form
     * @return the updated form in the database
     * @throws IOException exception error for input/output
     */
    public ExerciseGoals updateExerciseGoal(ExerciseGoalsForm exerciseGoalsForm)
        throws IOException {
        ExerciseGoals exerciseGoals =
            exerciseGoalsRepository.findExerciseGoalsById(exerciseGoalsForm.getId());
        if (exerciseGoals != null) {
            exerciseGoals.setSteps(exerciseGoalsForm.getSteps());
            exerciseGoals.setCaloriesBurnt(exerciseGoalsForm.getCaloriesBurnt());
            exerciseGoals.setTotalActivityTime(exerciseGoalsForm.getTotalActivityTime());
            exerciseGoals.setDistanceTravelled(exerciseGoalsForm.getDistanceTravelled());
            return exerciseGoalsRepository.save(exerciseGoals);
        } else {
            throw new IOException("Can not found the exercise goal with the id "
                    + exerciseGoalsForm.getId());
        }
    }

    /**
     * Calculates a users total progress to completing all their daily goals
     * @param exerciseGoalsForm The form holding all the users daily goals
     * @param garminDaily An object holding the garmin daily data
     */
    public void calculateGoalProgress(
            ExerciseGoalsForm exerciseGoalsForm, GarminDaily garminDaily) {


        if (exerciseGoalsForm == null || garminDaily == null) {
            return; // Exit early if either object is null
        }

        exerciseGoalsForm.setStepsProgress((int) (Double.min(1,
            exerciseGoalsForm.getSteps() == null
                ? 0 : (double) garminDaily.getSteps() / exerciseGoalsForm.getSteps()) * 100));
        exerciseGoalsForm.increaseGoalCount(exerciseGoalsForm.getSteps() == null ? 0 : 1);


        exerciseGoalsForm.setCaloriesProgress((int) (Double.min(1,
            exerciseGoalsForm.getCaloriesBurnt() == null
                ? 0 : garminDaily.getActiveKilocalories() / exerciseGoalsForm.getCaloriesBurnt())
                * 100));
        exerciseGoalsForm.increaseGoalCount(exerciseGoalsForm.getCaloriesBurnt() == null ? 0 : 1);


        exerciseGoalsForm.setDistanceProgress((int) (Double.min(1,
            exerciseGoalsForm.getDistanceTravelled() == null
                ? 0 : garminDaily.getDistanceInMeters()
                    / (exerciseGoalsForm.getDistanceTravelled() * 1000)) * 100));
        exerciseGoalsForm.increaseGoalCount(exerciseGoalsForm.getDistanceTravelled() == null
                ? 0 : 1);


        exerciseGoalsForm.setTimeProgress((int) (Double.min(1,
            exerciseGoalsForm.getTotalActivityTime() == null
                ? 0 : Double.parseDouble(garminDaily.getActiveTimeHrs())
                    / (exerciseGoalsForm.getTotalActivityTime() / 60)) * 100));
        exerciseGoalsForm.increaseGoalCount(exerciseGoalsForm.getTotalActivityTime() == null
                ? 0 : 1);

    }
}
