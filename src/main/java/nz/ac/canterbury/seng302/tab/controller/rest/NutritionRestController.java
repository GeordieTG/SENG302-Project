package nz.ac.canterbury.seng302.tab.controller.rest;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import nz.ac.canterbury.seng302.tab.entity.Food;
import nz.ac.canterbury.seng302.tab.entity.RecommendedMeal;
import nz.ac.canterbury.seng302.tab.entity.RecordedFood;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.entity.datatransferobjects.MealDto;
import nz.ac.canterbury.seng302.tab.entity.datatransferobjects.RecommendedMealDto;
import nz.ac.canterbury.seng302.tab.service.MealService;
import nz.ac.canterbury.seng302.tab.service.NutritionApi;
import nz.ac.canterbury.seng302.tab.service.RecordedFoodService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest Controller for the Nutrition Functionality
 */
@RestController
public class NutritionRestController {
    @Autowired
    NutritionApi nutritionApi;

    @Autowired
    RecordedFoodService recordedFoodService;
    @Autowired
    TabUserService tabUserService;
    @Autowired
    ValidationService validationService;
    @Autowired
    MealService mealService;
    private static final Integer RECENT_FOOD_COUNT = 5;

    public static final String CUT = "cut";

    public static final String MAINTAIN = "maintain";

    public static final String BULK = "bulk";
    Logger logger = LoggerFactory.getLogger(NutritionRestController.class);

    /**
     * Gets the food search results from the Nutrition API
     * @param query The query to search for
     * @return The food search results
     */
    @GetMapping("/search-foods/{query}")
    @ResponseBody
    public ResponseEntity<String> searchFoods(@PathVariable String query) {
        logger.info(String.format("GET /search-foods/%s", query));
        try {
            String foods = nutritionApi.searchFoods(query).toJSONString();
            return ResponseEntity.status(200).body(foods); // Return 200 OK with the JSON object
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(500).build(); // Return 500 Internal Server Error
        }
    }

    /**
     * Gets the details of a list food ids from the Nutrition API
     * @param fdcIds The fdcIds of the foods
     * @return The details of the foods
     */
    @GetMapping("/get-foods/{fdcIds}")
    @ResponseBody
    public ResponseEntity<String> getDetailsOfFoodList(@PathVariable Long[] fdcIds) {
        logger.info(String.format("GET /get-foods/%s", Arrays.toString(fdcIds)));
        try {
            String foods = nutritionApi.getFoods(fdcIds).toJSONString();
            return ResponseEntity.status(200).body(foods); // Return 200 OK with the JSON object
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(500).build(); // Return 500 Internal Server Error
        }
    }

    /**
     * Gets the details of a list food ids from the Nutrition API
     * @param userId The id of the user
     * @return The details of the foods
     */
    @GetMapping("/get-foods/last-7-days/{userId}")
    @ResponseBody
    public ResponseEntity<List<Map<String, Double>>> getDetailsOfFoodList(
        @PathVariable Long userId) {

        logger.info("GET /get-foods/last-7-days/");
        LocalDate currentDate = LocalDate.now();

        Map<String, Double> emptyFoodRecord = new HashMap<>();
        emptyFoodRecord.put("203", 0.0);
        emptyFoodRecord.put("269", 0.0);
        emptyFoodRecord.put("204", 0.0);
        emptyFoodRecord.put("205", 0.0);
        emptyFoodRecord.put("208", 0.0);

        List<Map<String, Double>> last7DayFoodRecord = new ArrayList<>();
        List<LocalDate> last7Days = IntStream.range(0, 7)
            .mapToObj(currentDate::minusDays)
            .toList();

        for (LocalDate specifiedDate : last7Days) {

            try {
                Map<String, Double> totalNutrientsDictionary =
                    recordedFoodService.addNutritionInfoToModel(userId, specifiedDate);
                last7DayFoodRecord.add(totalNutrientsDictionary);
            } catch (Exception e) {
                logger.info("No food Recorded , replaced by default value");
                last7DayFoodRecord.add(emptyFoodRecord);
            }

        }
        return new ResponseEntity<>(last7DayFoodRecord, HttpStatus.OK);
    }

    /**
     * Post request endpoint for adding food to the database
     * @param quantity The quantity of the food
     * @param fdcId The fdcId of the food
     * @param portionSizeNumber The portion size number of the food
     * @return The profile page
     */
    @PostMapping("/addFood")
    @ResponseBody
    public ResponseEntity<String> addFood(@RequestParam("quantity") Long quantity,
                          @RequestParam("fdcId") Long fdcId,
                          @RequestParam("portionSizeNumber") Long portionSizeNumber) {
        logger.info("POST /addFood");
        TabUser user = tabUserService.getCurrentlyLoggedIn();
        if (validationService.validateAddFoodForm(quantity, fdcId, portionSizeNumber)) {
            RecordedFood recordedFood =
                    new RecordedFood(user, fdcId, LocalDateTime.now(), quantity,
                        portionSizeNumber, null, null);
            recordedFoodService.saveFoodToDatabase(recordedFood);

            return new ResponseEntity<>(
                    recordedFoodService.getRecentJson(tabUserService.getCurrentlyLoggedIn().getId(),
                    RECENT_FOOD_COUNT), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Handles the HTTP POST request to update the calorie preference.
     *
     * @param caloriePreferenceRequestBody The request body containing the calorie preference.
     * @return ResponseEntity with an appropriate HTTP status code
     */
    @PostMapping("/update-calorie-preference")
    @ResponseBody
    public ResponseEntity<Map<String, String>> updateCaloriePreference(
            @RequestBody String caloriePreferenceRequestBody) {

        logger.info("POST /update-calorie-preference");
        try {
            TabUser user = tabUserService.getCurrentlyLoggedIn();

            if (caloriePreferenceRequestBody.contains(CUT)) {
                user.setCalorieIntakePreference(CUT);
            } else if (caloriePreferenceRequestBody.contains(MAINTAIN)) {
                user.setCalorieIntakePreference(MAINTAIN);
            } else if (caloriePreferenceRequestBody.contains(BULK)) {
                user.setCalorieIntakePreference(BULK);
            } else {
                logger.error("Could not update calorie preference: "
                        + "Invalid calorie preference {}", caloriePreferenceRequestBody);
                return ResponseEntity.status(400).build();
            }

            tabUserService.updateUser(user);
            return ResponseEntity.status(200).build();
        } catch (Exception e) {
            logger.error("Could not update calorie preference: " + e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Gets three random meals based on a users calorie intake preference.
     * @return ResponseEntity with an appropriate HTTP status code
     */
    @GetMapping("/get-recommended-meals/{calorieGoal}")
    @ResponseBody
    public ResponseEntity<List<RecommendedMealDto>> getRandomMeals(
        @PathVariable("calorieGoal") double caloriesBurned) {
        logger.info("GET /get-recommended-meals/{calorieGoal}");

        String preference = tabUserService.getCurrentlyLoggedIn().getCalorieIntakePreference();
        String prefCamelCase = preference.substring(0, 1).toUpperCase() + preference.substring(1);
        List<RecommendedMeal> recommendedMeals = mealService.getAllMealsByPreference(prefCamelCase);
        List<RecommendedMealDto> mealList = new ArrayList<>();
        for (RecommendedMeal meal : recommendedMeals) {
            double quantity = mealService.calculateRequiredMealQuantity(meal, caloriesBurned,
                preference);
            RecommendedMealDto mealDto =  new RecommendedMealDto(meal.getName(),
                mealService.calculateCalorieTotal(meal),
                mealService.calculateProteinTotal(meal),
                mealService.calculateFatTotal(meal),
                mealService.calculateSugarTotal(meal),
                meal.getFoods(), meal.getId(),
                    quantity,
                meal.getIcon());
            mealList.add(mealDto);
        }
        mealList.sort(Comparator.comparingDouble(RecommendedMealDto::getQuantity));
        return new ResponseEntity<>(mealList, HttpStatus.OK);
    }

    /**
     * Used to save meals, called from JS
     * @param mealDto Holds the information needed to save meals
     */
    @PostMapping("/save-meals")
    @ResponseBody
    @Transactional
    public void saveMeals(@RequestBody MealDto mealDto) {
        logger.info("POST /save-meals");
        for (int i = 0; i < mealDto.getMealIds().size(); i++) {

            List<Food> foods = mealService.getRecommendedMealById(
                mealDto.getMealIds().get(i)).getFoods();
            for (Food food : foods) {
                addFood(food.getPortion().longValue(), food.getfdcId(), null);
                RecordedFood recordedFood = new RecordedFood(
                    tabUserService.getCurrentlyLoggedIn(), food.getfdcId(), LocalDateTime.now(),
                    food.getPortion().longValue() * mealDto.getMealQuantities().get(i),
                    0L, null, mealDto.getActivityId());

                recordedFoodService.saveFoodToDatabase(recordedFood);
            }
            RecordedFood recordedFood =
                new RecordedFood(tabUserService.getCurrentlyLoggedIn(), null, LocalDateTime.now(),
                    mealDto.getMealQuantities().get(i), 0L, mealDto.getMealIds().get(i),
                    mealDto.getActivityId());
            recordedFoodService.saveFoodToDatabase(recordedFood);
        }
    }
}
