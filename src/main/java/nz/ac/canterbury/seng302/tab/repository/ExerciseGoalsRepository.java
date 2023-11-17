package nz.ac.canterbury.seng302.tab.repository;

import nz.ac.canterbury.seng302.tab.entity.ExerciseGoals;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for ExerciseGoals
 */
public interface ExerciseGoalsRepository extends CrudRepository<ExerciseGoals, Long> {

    @Query(value = "SELECT * FROM exercise_goals WHERE exercise_goals_id = :id",
        nativeQuery = true)
    ExerciseGoals findExerciseGoalsById(long id);

    ExerciseGoals findByUser(TabUser user);

}
