package nz.ac.canterbury.seng302.tab.integration;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import nz.ac.canterbury.seng302.tab.controller.rest.NutritionRestController;
import nz.ac.canterbury.seng302.tab.entity.Food;
import nz.ac.canterbury.seng302.tab.entity.RecommendedMeal;
import nz.ac.canterbury.seng302.tab.entity.RecordedFood;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.entity.datatransferobjects.MealDto;
import nz.ac.canterbury.seng302.tab.repository.FoodRepository;
import nz.ac.canterbury.seng302.tab.repository.MealRepository;
import nz.ac.canterbury.seng302.tab.service.MealService;
import nz.ac.canterbury.seng302.tab.service.RecordedFoodService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

/**
 * Common Steps for integration tests
 */
@SpringBootTest
@AutoConfigureMockMvc
public class RecommendIntakeFeature {

    @Autowired
    MockMvc mockMvc;
    MvcResult result;
    @Autowired
    private TabUserService tabUserService;
    @Autowired
    RecordedFoodService recordedFoodService;

    @Autowired
    MealService mealService;

    @Autowired
    FoodRepository foodRepository;

    @Autowired
    MealRepository mealRepository;

    @Autowired
    NutritionRestController nutritionRestController = new NutritionRestController();

    @And("I have set my calorie intake preference to {string}")
    public void i_have_set_my_calorie_intake_preference_to_calorie_intake_preference(
        String caloriePreference) {
        TabUser tabUser = tabUserService.getCurrentlyLoggedIn();
        tabUser.setCalorieIntakePreference(caloriePreference);
        tabUserService.addTabUser(tabUser);
    }

    @When("I click a dedicated UI element to see recommended food based on my {double}")
    public void i_click_a_dedicated_ui_element_to_see_recommended_food_based_on_my(
        double caloriesBurnt)
        throws Exception {
        result = mockMvc.perform(get("/get-recommended-meals/" + caloriesBurnt)
                .with(csrf()))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Then("I am presented with a set meals based on my {string} {double}")
    public void i_am_presented_with_a_set_meals_based_on_my_calorie_intake_preference(
        String caloriePreference, double caloriesBurnt)
        throws UnsupportedEncodingException, ParseException {
        String response = result.getResponse().getContentAsString();
        JSONArray actualResponse = (JSONArray) new JSONParser()
            .parse(response);
        for (Object individualMeal : actualResponse) {
            JSONObject meal = (JSONObject) individualMeal;
            Double calculatedCalories =
                ((Double) meal.get("calories")) * ((Double) meal.get("quantity"));
            switch (caloriePreference) {
                case "cut" -> Assertions.assertTrue(calculatedCalories < caloriesBurnt);
                case "maintain" -> Assertions.assertTrue(calculatedCalories > caloriesBurnt - 10
                        && calculatedCalories < caloriesBurnt + 10);
                case "bulk" ->
                    Assertions.assertTrue(calculatedCalories > caloriesBurnt);
                default ->
                    throw new IllegalStateException("Unexpected value: " + caloriePreference);
            }
        }
    }

    @When("I add one recommended meal")
    public void i_add_one_recommended_meal() {

        RecommendedMeal recommendedMeal = create_meal(
            "Banana", "cut", 50.00, 13027L);

        MealDto mealDto = new MealDto();
        mealDto.setMealIds(List.of(recommendedMeal.getId()));
        mealDto.setMealQuantities(List.of(1L));
        mealDto.setActivityId(1L);

        nutritionRestController.saveMeals(mealDto);
    }

    @Then("I am able to see the one meal in the database")
    public void i_am_able_to_see_them_in_the_database() {
        List<RecordedFood> recordedFood =
            recordedFoodService.getRecent(IntegrationTestConfigurations.loggedInUser.getId(),
            1);
        Assertions.assertEquals(13027L, (long) recordedFood.get(0).getFdcId());
    }

    /**
     * Creates a RecommendedMeal to be used with testing
     * @param mealName The name of the meal
     * @param caloriePreference The type of meal
     * @param calories The total number of calories
     * @param fdcId The fdcId of the food
     * @return The RecommendedMeal
     */
    public RecommendedMeal create_meal(String mealName, String caloriePreference, double calories,
                                       Long fdcId) {
        RecommendedMeal recommendedMeal = new RecommendedMeal();
        recommendedMeal.setName(mealName);
        recommendedMeal.setCaloriePreference(caloriePreference);
        recommendedMeal = mealRepository.save(recommendedMeal);
        Food food = new Food();
        food.setMeal(recommendedMeal);
        food.setName("food");
        food.setCalories(calories);
        food.setFat(calories);
        food.setCarbs(calories);
        food.setSugar(calories);
        food.setProtein(calories);
        food.setfdcId(fdcId);
        food.setPortion(1.00);
        foodRepository.save(food);
        List<Food> foods = new ArrayList<>();
        foods.add(food);
        recommendedMeal.setFoods(foods);
        return mealRepository.save(recommendedMeal);
    }

    @When("I add multiple recommended meals")
    public void i_add_multiple_recommended_meals() {
        RecommendedMeal recommendedMeal = create_meal(
            "Banana", "cut", 50.00, 12345L);

        RecommendedMeal recommendedMeal1 = create_meal(
            "Orange", "cut", 40.00, 23456L);

        RecommendedMeal recommendedMeal2 = create_meal(
            "Apple", "cut", 50.00, 34567L);

        MealDto mealDto = new MealDto();
        mealDto.setMealIds(List.of(recommendedMeal.getId(),
            recommendedMeal1.getId(), recommendedMeal2.getId()));
        mealDto.setMealQuantities(List.of(1L, 1L, 1L));
        mealDto.setActivityId(1L);

        nutritionRestController.saveMeals(mealDto);
    }

    @Then("I am able to see multiple meals in the database")
    public void i_am_able_to_see_multiple_meals_in_the_database() {
        List<RecordedFood> recordedFood =
            recordedFoodService.getRecent(IntegrationTestConfigurations.loggedInUser.getId(),
                3);

        Assertions.assertEquals(34567L, (long) recordedFood.get(0).getFdcId());
        Assertions.assertEquals(23456L, (long) recordedFood.get(1).getFdcId());
        Assertions.assertEquals(12345L, (long) recordedFood.get(2).getFdcId());
    }
}
