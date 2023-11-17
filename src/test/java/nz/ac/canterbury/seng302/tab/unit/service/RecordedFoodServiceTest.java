package nz.ac.canterbury.seng302.tab.unit.service;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javassist.NotFoundException;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.RecordedFood;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.repository.RecordedFoodRepository;
import nz.ac.canterbury.seng302.tab.service.NutritionApi;
import nz.ac.canterbury.seng302.tab.service.RecordedFoodService;
import org.javatuples.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

@ExtendWith(MockitoExtension.class)
class RecordedFoodServiceTest {

    @InjectMocks
    @Spy
    RecordedFoodService recordedFoodService;

    @Mock
    RecordedFoodRepository recordedFoodRepository;

    @Mock
    NutritionApi nutritionApi;

    @Mock
    Model model;

    /**
     * Tests that the correct exception is thrown when a food is not found in the database
     */
    @Test
    void addFoodInfoToModel_whenNoRecordedFoods_ThenExceptionThrown() {
        when(recordedFoodRepository.getUsersRecordedFoodsWhereDate(
            Mockito.anyLong(), Mockito.any())).thenReturn(new ArrayList<>());
        Assertions.assertThrows(NotFoundException.class, () -> {
            recordedFoodService.addNutritionInfoToModel(1L,
                null);
        });
    }

    /**
     * tests that when calling getTotalNutrientAmounts, with one food, the correct values
     * are correct
     */
    @Test
    void getTotalNutrientAmounts_whenOneFoodRecorded_ReturnedValuesCorrect() {

        JSONArray exampleResponse = new JSONArray();
        JSONObject exampleFood = new JSONObject();
        JSONArray foodNutrients = new JSONArray();
        JSONObject foodNutrient = new JSONObject();

        exampleFood.put("foodNutrients", foodNutrients);
        exampleResponse.add(exampleFood);
        foodNutrients.add(foodNutrient);
        foodNutrients.add(foodNutrient);
        foodNutrients.add(foodNutrient);
        foodNutrients.add(foodNutrient);
        foodNutrients.add(foodNutrient);


        AtomicInteger callCount = new AtomicInteger();
        Mockito.doReturn(291).when(recordedFoodService)
            .getPortionSizeGramWeight(Mockito.any(), Mockito.any());
        Mockito.doAnswer(invocation -> {
            callCount.getAndIncrement();

            switch (callCount.get()) {
                case 1:
                    return Pair.with(RecordedFoodService.PROTEIN, 50.0);
                case 2:
                    return Pair.with(RecordedFoodService.FAT, 100.0);
                case 3:
                    return Pair.with(RecordedFoodService.CARBOHYDRATES, 150.0);
                case 4:
                    return Pair.with(RecordedFoodService.SUGAR, 200.0);
                case 5:
                    return Pair.with(RecordedFoodService.ENERGY, 250.0);
                default:
                    return Pair.with("Null", 0.0);
            }
        }
        ).when(recordedFoodService)
            .getFoodNutrientAmount(Mockito.any(JSONObject.class), Mockito.anyLong(),
                Mockito.anyInt());

        Map<String, Double> returnedDictionary = recordedFoodService.getTotalNutrientAmounts(
            List.of(Pair.with(1L, 1L)), exampleResponse);

        Assertions.assertEquals(50.0, returnedDictionary.get(RecordedFoodService.PROTEIN));
        Assertions.assertEquals(100.0, returnedDictionary.get(RecordedFoodService.FAT));
        Assertions.assertEquals(150.0, returnedDictionary.get(RecordedFoodService.CARBOHYDRATES));
        Assertions.assertEquals(200.0, returnedDictionary.get(RecordedFoodService.SUGAR));
        Assertions.assertEquals(250.0, returnedDictionary.get(RecordedFoodService.ENERGY));
        Mockito.verify(recordedFoodService, Mockito.times(5))
            .getFoodNutrientAmount(Mockito.any(JSONObject.class), Mockito.anyLong(),
                Mockito.anyInt());

    }


    /**
     * tests that when calling getTotalNutrientAmounts, with two foods, 
     * the correct values are correct
     */
    @Test
    void getTotalNutrientAmounts_whenTwoFoodsRecorded_ReturnedValuesCorrect() {

        JSONArray exampleResponse = new JSONArray();
        JSONObject exampleFood = new JSONObject();

        exampleResponse.add(exampleFood);
        exampleResponse.add(exampleFood);

        JSONArray foodNutrients = new JSONArray();
        JSONObject foodNutrient = new JSONObject();

        foodNutrients.add(foodNutrient);
        foodNutrients.add(foodNutrient);
        foodNutrients.add(foodNutrient);
        foodNutrients.add(foodNutrient);
        foodNutrients.add(foodNutrient);
        exampleFood.put("foodNutrients", foodNutrients);


        AtomicInteger callCount = new AtomicInteger();
        Mockito.doReturn(291).when(recordedFoodService)
            .getPortionSizeGramWeight(Mockito.any(), Mockito.any());
        Mockito.doAnswer(invocation -> {
            // Increment the call count
            callCount.getAndIncrement();
            switch (callCount.get()) {
                case 1:
                    return Pair.with(RecordedFoodService.PROTEIN, 50.0);
                case 2:
                    return Pair.with(RecordedFoodService.FAT, 100.0);
                case 3:
                    return Pair.with(RecordedFoodService.CARBOHYDRATES, 150.0);
                case 4:
                    return Pair.with(RecordedFoodService.SUGAR, 200.0);
                case 5:
                    return Pair.with(RecordedFoodService.ENERGY, 250.0);
                case 6:
                    return Pair.with(RecordedFoodService.PROTEIN, 500.0);
                case 7:
                    return Pair.with(RecordedFoodService.FAT, 450.0);
                case 8:
                    return Pair.with(RecordedFoodService.CARBOHYDRATES, 300.0);
                case 9:
                    return Pair.with(RecordedFoodService.SUGAR, 250.0);
                case 10:
                    return Pair.with(RecordedFoodService.ENERGY, 200.0);
                default:
                    return Pair.with("Null", 0.0);
            }
        }
        ).when(recordedFoodService)
            .getFoodNutrientAmount(Mockito.any(JSONObject.class), Mockito.anyLong(),
                Mockito.anyInt());

        Map<String, Double> returnedDictionary = recordedFoodService.getTotalNutrientAmounts(
            List.of(Pair.with(1L, 1L), Pair.with(1L, 1L)), exampleResponse);

        Assertions.assertEquals(550.0, returnedDictionary.get(RecordedFoodService.PROTEIN));
        Assertions.assertEquals(550.0, returnedDictionary.get(RecordedFoodService.FAT));
        Assertions.assertEquals(450.0, returnedDictionary.get(RecordedFoodService.CARBOHYDRATES));
        Assertions.assertEquals(450.0, returnedDictionary.get(RecordedFoodService.SUGAR));
        Assertions.assertEquals(450.0, returnedDictionary.get(RecordedFoodService.ENERGY));
        Mockito.verify(recordedFoodService, Mockito.times(10))
            .getFoodNutrientAmount(Mockito.any(JSONObject.class), Mockito.anyLong(),
                Mockito.anyInt());

    }

    /**
     * tests that calling getFoodNutrientAmount returns the correct calculated nutrient amount
     *
     * @param nutrientAmount        The amount of the nutrient in the food
     * @param nutrientId            The id of the nutrient
     * @param portionSizeGramWeight The gram weight of the portion size
     * @param quantity              The quantity of the food
     */
    @ParameterizedTest
    @CsvSource({
        "15, 203,  320, 5",
        "20, 103,  0, 2",
        "5, 3, 243,  12",
    })
    void getFoodNutrientAmount_checkReturnedAmountValid(Double nutrientAmount,
                                                        int nutrientId,
                                                        int portionSizeGramWeight,
                                                        Long quantity) {
        JSONObject foodNutrient = new JSONObject();
        JSONObject nutrient = new JSONObject();
        nutrient.put("number", nutrientId);
        foodNutrient.put("nutrient", nutrient);
        foodNutrient.put("amount", nutrientAmount);
        Double calculatedAmount = (nutrientAmount / 100) * quantity * portionSizeGramWeight;
        Pair<String, Double> expectedPair = Pair.with(String.valueOf(nutrientId), calculatedAmount);
        Pair<String, Double> actualPair =
            recordedFoodService.getFoodNutrientAmount(foodNutrient, quantity,
                portionSizeGramWeight);
        Assertions.assertEquals(expectedPair, actualPair);
    }

    /**
     * tests that calling getPortionSizeGramWeight returns the correct gram weight
     *
     * @param gramWeight        The gram weight of the portion size
     * @param portionSizeNumber The number of the portion size
     */
    @ParameterizedTest
    @CsvSource({
        "15, 203",
        "20, 103",
        "5, 243",
    })
    void getPortionSizeGramWeight_checkReturnedAmountValid(Double gramWeight,
                                                           Long portionSizeNumber) {
        JSONArray foodPortions = new JSONArray();
        JSONObject foodPortion = new JSONObject();
        foodPortion.put("id", portionSizeNumber);
        foodPortion.put("gramWeight", gramWeight);
        foodPortions.add(foodPortion);
        int expectedGramWeight = gramWeight.intValue();
        int actualGramWeight =
            recordedFoodService.getPortionSizeGramWeight(foodPortions, portionSizeNumber);
        Assertions.assertEquals(expectedGramWeight, actualGramWeight);
    }

    /**
     * tests that when getFoods is called and an exception occurs, the correct exception is thrown
     */
    @Test
    void getFoods_whenNoFoodsRecorded_ExceptionThrown() {
        try {
            doThrow(new ParseException(0)).when(nutritionApi).getFoods(Mockito.any());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assertions.assertThrows(NotFoundException.class, () -> {
            recordedFoodService.getFoods(null);
        });
    }

    /**
     * tests that when getFoods is called and no exception occurs, no exception is thrown
     */
    @Test
    void getFoods_whenFoodsRecorded_ExceptionThrown() {
        try {
            doReturn(null).when(nutritionApi).getFoods(Mockito.any());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assertions.assertDoesNotThrow(() -> {
            recordedFoodService.getFoods(null);
        });
    }



    /**
     * Checks for correct response when null repository result
     */
    @Test
    void checkGetRecentWhenNull() {
        when(recordedFoodRepository.getRecentFoods(0L, 2))
                .thenReturn(null);
        List<RecordedFood> result = recordedFoodService.getRecent(0L, 2);
        Assertions.assertTrue(result.isEmpty());
    }

    /**
     * Checks if correct JSON string is generated
     */
    @Test
    void checkGetJsonString() {
        Location testLocation = new Location("city", "country");
        TabUser testUser = new TabUser("first", "name", testLocation);
        LocalDateTime testTime = LocalDateTime.now();
        RecordedFood one = new RecordedFood(testUser, 1L, testTime, 1L, 1L, null, null);
        RecordedFood two = new RecordedFood(testUser, 2L, testTime, 2L, 2L, null, null);
        RecordedFood three = new RecordedFood(testUser, 3L, testTime, 3L, 3L, null, null);

        List<RecordedFood> testList = new ArrayList<>();
        testList.add(one);
        testList.add(two);
        testList.add(three);

        String expected =
            "[{\"mealId\": null,\"fdcId\": 1,\"time\": \"" + testTime + "\","
            + "\"quantity\": 1,\"portionSizeNumber\": 1},{\"mealId\": null,"
            + "\"fdcId\": 2,\"time\": \"" + testTime + "\","
            + "\"quantity\": 2,\"portionSizeNumber\": 2},{\"mealId\": null,"
            + "\"fdcId\": 3,\"time\": \"" + testTime + "\","
            + "\"quantity\": 3,\"portionSizeNumber\": 3}]";

        String result = recordedFoodService.getJsonString(testList);
        Assertions.assertEquals(expected, result);
    }

}
