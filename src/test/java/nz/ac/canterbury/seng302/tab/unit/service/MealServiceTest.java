package nz.ac.canterbury.seng302.tab.unit.service;

import java.util.List;
import nz.ac.canterbury.seng302.tab.entity.Food;
import nz.ac.canterbury.seng302.tab.entity.RecommendedMeal;
import nz.ac.canterbury.seng302.tab.service.MealService;
import nz.ac.canterbury.seng302.tab.unit.UnitCommonTestSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MealServiceTest {

    @Autowired
    MealService mealService;
    RecommendedMeal recommendedMeal;

    @BeforeEach
    void setup() {
        recommendedMeal = UnitCommonTestSetup.createTestMeal();
        Food food = UnitCommonTestSetup.createTestFood();
        Food food1 = UnitCommonTestSetup.createTestFood();
        Food food2 = UnitCommonTestSetup.createTestFood();
        recommendedMeal.setFoods(List.of(food, food1, food2));
    }

    @Test
    void testCalculateCalorieTotal() {

        Double actual = mealService.calculateCalorieTotal(recommendedMeal);
        Assertions.assertEquals(729, actual);

    }

    @Test
    void testCalculateProteinTotal() {

        Double actual = mealService.calculateProteinTotal(recommendedMeal);
        Assertions.assertEquals(36.0, actual);

    }

    @Test
    void testCalculateFatTotal() {

        Double actual = mealService.calculateFatTotal(recommendedMeal);
        Assertions.assertEquals(46.05, actual);

    }

    @Test
    void testCalculateSugarTotal() {

        Double actual = mealService.calculateSugarTotal(recommendedMeal);
        Assertions.assertEquals(0, actual);

    }

    @Test
    void testCalculateRequiredMealQuantityWithCut() {
        double actual = mealService.calculateRequiredMealQuantity(recommendedMeal, 1000.00, "cut");
        Assertions.assertEquals(1.09739, actual);
    }

    @Test
    void testCalculateRequiredMealQuantityWithBulk() {
        double actual = mealService.calculateRequiredMealQuantity(recommendedMeal, 1000.00, "bulk");
        Assertions.assertEquals(1.64609, actual);
    }

    @Test
    void testCalculateRequiredMealQuantityWithMaintain() {
        double actual =
            mealService.calculateRequiredMealQuantity(recommendedMeal, 1000.00, "maintain");
        Assertions.assertEquals(1.37174, actual);
    }
}
