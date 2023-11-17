package nz.ac.canterbury.seng302.tab.repository;


import java.util.List;
import nz.ac.canterbury.seng302.tab.entity.RecommendedMeal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Access the Meal database
 */
public interface MealRepository extends CrudRepository<RecommendedMeal, Long> {

    RecommendedMeal findById(long id);

    List<RecommendedMeal> findAll();

    @Query(value = "SELECT * FROM recommended_meal WHERE calorie_preference = :preference "
            + "ORDER BY RAND() LIMIT 3", nativeQuery = true)
    List<RecommendedMeal> getAllMealsByCaloriePreference(String preference);

    @Query(value = "SELECT * FROM recommended_meal WHERE meal_id = :id", nativeQuery = true)
    RecommendedMeal findUsingId(Long id);

}
