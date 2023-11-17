package nz.ac.canterbury.seng302.tab.service;


import java.text.DecimalFormat;
import java.util.List;
import nz.ac.canterbury.seng302.tab.entity.Food;
import nz.ac.canterbury.seng302.tab.entity.RecommendedMeal;
import nz.ac.canterbury.seng302.tab.repository.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Access to the 'Meal' database
 */
@Service
public class MealService {
    @Autowired
    public MealRepository mealRepository;

    @Autowired
    public RecordedFoodService recordedFoodService;

    /**
     * The multiplier to use when calculating the quantity of a meal for a user who is cutting
     */
    private static final double CUT_MEAL_CALORIE_MULTIPLIER = 0.8;

    /**
     * The multiplier to use when calculating the quantity of a meal for a user who is bulking
     */
    private static final double BULK_MEAL_CALORIE_MULTIPLIER = 1.2;

    /**
     * The multiplier to use when calculating the quantity of a meal for a user who is maintaining
     */
    private static final double MAINTAIN_MEAL_CALORIE_MULTIPLIER = 1.0;


    /**
     * Gets a list of randomly selected meals which match the given caloric preference
     * from the database.
     * @param preference the caloric preference of a user
     *
     * @return a list of all meals
     */
    public List<RecommendedMeal> getAllMealsByPreference(String preference) {
        return mealRepository.getAllMealsByCaloriePreference(preference);
    }


    /**
     * Returns the quantity of a meal that should be eaten based on the calories burned and
     * the caloric preference
     * @param meal the meal to calculate the quantity for
     * @param caloriesBurned the calories burned by the user
     * @param preference the caloric preference of the user
     * @return the quantity of the meal that should be eaten
     */
    public double calculateRequiredMealQuantity(RecommendedMeal meal, Double caloriesBurned,
                                           String preference) {
        Double totalCalories = calculateCalorieTotal(meal);
        double calorieMultiplier = switch (preference) {
            case "cut" -> CUT_MEAL_CALORIE_MULTIPLIER;
            case "bulk" -> BULK_MEAL_CALORIE_MULTIPLIER;
            default -> MAINTAIN_MEAL_CALORIE_MULTIPLIER;
        };
        double result = (caloriesBurned * calorieMultiplier) / totalCalories;

        // Create a DecimalFormat with two decimal places
        DecimalFormat df = new DecimalFormat("#.#####");

        // Format the result to two decimal places
        String formattedResult = df.format(result);

        // Parse the formatted result back to a double
        return Double.parseDouble(formattedResult);
    }

    /**
     * Calculates the total calories of a meal
     * @param meal the meal to calculate the calories for
     * @return the total calories of the meal
     */
    public Double calculateCalorieTotal(RecommendedMeal meal) {
        return meal.getFoods().stream()
            .mapToDouble(Food::getCalories)
            .sum();
    }

    /**
     * Calculates the total protein of a meal
     * @param meal the meal to calculate the protein for
     * @return the total protein of the meal
     */
    public Double calculateProteinTotal(RecommendedMeal meal) {
        return meal.getFoods().stream()
            .mapToDouble(Food::getProtein)
            .sum();
    }

    /**
     * Calculates the total fat of a meal
     * @param meal the meal to calculate the fat for
     * @return the total fat of the meal
     */
    public Double calculateFatTotal(RecommendedMeal meal) {
        return meal.getFoods().stream()
            .mapToDouble(Food::getFat)
            .sum();
    }

    /**
     * Calculates the total sugar of a meal
     * @param meal the meal to calculate the sugar for
     * @return the total sugar of the meal
     */
    public Double calculateSugarTotal(RecommendedMeal meal) {
        return meal.getFoods().stream()
            .mapToDouble(Food::getSugar)
            .sum();
    }

    public RecommendedMeal getRecommendedMealById(Long id) {
        return mealRepository.findUsingId(id);
    }
}
