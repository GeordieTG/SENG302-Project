package nz.ac.canterbury.seng302.tab.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javassist.NotFoundException;
import nz.ac.canterbury.seng302.tab.controller.rest.NutritionRestController;
import nz.ac.canterbury.seng302.tab.entity.RecordedFood;
import nz.ac.canterbury.seng302.tab.repository.RecordedFoodRepository;
import org.javatuples.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Functionality for everything related to Activities
 */
@Service
public class RecordedFoodService {

    public static final String PROTEIN = "203";
    public static final String ENERGY = "208";
    public static final String SUGAR = "269";
    public static final String FAT = "204";
    public static final String CARBOHYDRATES = "205";
    Logger logger = LoggerFactory.getLogger(RecordedFoodService.class);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Autowired
    RecordedFoodRepository recordedFoodRepository;
    @Autowired
    NutritionApi nutritionApi;

    NutritionRestController nutritionRestController;

    /**
     * Saves food to the database
     * @param recordedFood The food to save
     * @return The saved food
     */
    public RecordedFood saveFoodToDatabase(RecordedFood recordedFood) {
        return recordedFoodRepository.save(recordedFood);
    }

    /**
     * Gets food from the database
     * @param id The id of the food
     * @return The food
     * @throws NotFoundException If the food is not found
     */
    public RecordedFood getFoodFromDatabase(Long id) throws NotFoundException {
        Optional<RecordedFood> recordedFoodOptional = recordedFoodRepository.findById(id);
        if (recordedFoodOptional.isPresent()) {
            return recordedFoodOptional.get();
        } else {
            throw new NotFoundException("RecordedFood not found");
        }
    }

    /**
     * Returns a list of recorded foods for a user on a specified date
     *
     * @param userId        The id of the user
     * @param specifiedDate The date to get the foods for
     * @return A list of recorded foods for a user on a specified date
     */
    public List<RecordedFood> getUsersRecordedFoodsWhereDate(long userId, LocalDate specifiedDate) {
        return recordedFoodRepository.getUsersRecordedFoodsWhereDate(userId, specifiedDate);
    }

    /**
     * Adds the nutrition info to the model
     *
     * @param userId        The id of the user to get the info for
     * @param specifiedDate The date to get the info for
     * @throws NotFoundException If the foods are not found
     */
    public Map<String, Double> addNutritionInfoToModel(Long userId, LocalDate specifiedDate)
        throws NotFoundException {
        List<RecordedFood> recordedFoods = getUsersRecordedFoodsWhereDate(userId, specifiedDate);
        if (recordedFoods.isEmpty()) {
            throw new NotFoundException(
                "No recorded foods found for user " + userId + " on date " + specifiedDate);
        } else {
            Long[] fdcIds = recordedFoods.stream().map(RecordedFood::getFdcId).toArray(Long[]::new);
            JSONArray getFoodsResult = getFoods(fdcIds);
            return addNutritionOverviewDetailsToModel(getFoodsResult, recordedFoods);
        }
    }

    /**
     * Adds the nutrition overview details to the model
     *
     * @param getFoodsResult The result of a get foods request
     * @param recordedFoods  The recorded foods
     */
    public Map<String, Double> addNutritionOverviewDetailsToModel(JSONArray getFoodsResult,
                                                   List<RecordedFood> recordedFoods) {
        Long[] quantities =
            recordedFoods.stream().map(RecordedFood::getQuantity).toArray(Long[]::new);
        Long[] portionSizeNumbers =
            recordedFoods.stream().map(RecordedFood::getPortionSizeNumber).toArray(Long[]::new);

        List<Pair<Long, Long>> pairs = new ArrayList<>();
        for (int i = 0; i < quantities.length; i++) {
            Pair<Long, Long> pair = Pair.with(quantities[i], portionSizeNumbers[i]);
            pairs.add(pair);
        }
        return getTotalNutrientAmounts(pairs, getFoodsResult);

    }

    /**
     * Gets the total nutrient amounts for a list of quantity portion size number pairs
     *
     * @param quantityPortionSizeNumberPairs A list of quantity portion size number pairs,
     *                                       where the first value is the quantity and the
     *                                       second value is the portion size number
     *
     * @param getFoodsResult                 The result of a get foods request
     * @return A dictionary of the total nutrient amounts
     */
    public Map<String, Double> getTotalNutrientAmounts(
        List<Pair<Long, Long>> quantityPortionSizeNumberPairs, JSONArray getFoodsResult) {


        Map<String, Double> dictionary = new HashMap<>();
        dictionary.put(ENERGY, 0.0);
        dictionary.put(FAT, 0.0);
        dictionary.put(CARBOHYDRATES, 0.0);
        dictionary.put(PROTEIN, 0.0);
        dictionary.put(SUGAR, 0.0);

        for (int i = 0; i < quantityPortionSizeNumberPairs.size(); i++) {
            JSONObject food = (JSONObject) getFoodsResult.get(i);
            JSONArray foodNutrients = (JSONArray) food.get("foodNutrients");
            JSONArray foodPortions = (JSONArray) food.get("foodPortions");
            int portionSizeGramWeight = getPortionSizeGramWeight(foodPortions,
                quantityPortionSizeNumberPairs.get(i).getValue1());
            for (int j = 0; j < foodNutrients.size(); j++) {
                Pair<String, Double> nutrientIdAmountPair =
                    getFoodNutrientAmount((JSONObject) foodNutrients.get(j),
                        quantityPortionSizeNumberPairs.get(i).getValue0(), portionSizeGramWeight);
                String nutrientId = nutrientIdAmountPair.getValue0();
                Double calculatedAmount = nutrientIdAmountPair.getValue1();
                dictionary.put(nutrientId, dictionary.get(nutrientId) + calculatedAmount);
            }
        }
        return dictionary;
    }

    /**
     * Gets the food nutrient amount for a food nutrient
     *
     * @param foodNutrient          The food nutrient to get the amount for
     * @param quantity              The quantity of the food
     * @param portionSizeGramWeight The portion size gram weight of the food
     * @return A pair of the nutrient id and the calculated amount of the nutrient
     */
    public Pair<String, Double> getFoodNutrientAmount(JSONObject foodNutrient, Long quantity,
                                                      int portionSizeGramWeight) {
        String nutrientId = ((JSONObject) foodNutrient.get("nutrient")).get("number").toString();
        Double amount = (Double) foodNutrient.get("amount") / 100; // Cast to Double
        Double calculatedAmount = amount * quantity * portionSizeGramWeight;
        return Pair.with(nutrientId, calculatedAmount);
    }

    /**
     * Gets the portion size gram weight for a given portion size number
     *
     * @param foodPortions           The list of food portions to get the gram weight from
     * @param givenPortionSizeNumber The given portion size number
     * @return The portion size gram weight for the given portion size number
     */
    public int getPortionSizeGramWeight(JSONArray foodPortions, Long givenPortionSizeNumber) {
        if (givenPortionSizeNumber.equals(0L)) {
            return 1;
        }
        for (Object portion : foodPortions) {
            JSONObject foodPortion = (JSONObject) portion;
            Long portionSizeNumber = (Long) foodPortion.get("id");
            if (portionSizeNumber.equals(givenPortionSizeNumber)) {
                return (int) (double) foodPortion.get("gramWeight");
            }
        }
        return 0;
    }

    /**
     * Gets the foods from the Nutrition API
     *
     * @param fdcIds The fdcIds of the foods to get
     * @return An array of the foods from the Nutrition API
     * @throws NotFoundException If the foods are not found or there is an error getting the foods
     */
    public JSONArray getFoods(Long[] fdcIds) throws NotFoundException {
        try {
            return (JSONArray) nutritionApi.getFoods(fdcIds);
        } catch (IOException | ParseException | InterruptedException e) {
            throw new NotFoundException("Could not get foods from Nutrition API");
        }
    }



    /**
     * Retrieve top x most recent food items recorded for the given user
     * Accounts for the null return
     * @param userId user id
     * @param numberToRetrieve x
     * @return Length x of RecordedFood ArrayList
     */
    public List<RecordedFood> getRecent(Long userId, int numberToRetrieve) {

        ArrayList<RecordedFood> recentFoods = recordedFoodRepository.getRecentFoods(userId,
            numberToRetrieve);

        if (recentFoods == null) {
            return new ArrayList<>();
        }

        return recentFoods;
    }

    /**
     * Wrapper method to wrap getRecent() and getJsonString() together
     * @param userId target TabUser id
     * @param number number of the most recent meals to retrieve, in desc
     * @return JSON string | "{}" if result is null
     */
    public String getRecentJson(Long userId, int number) {
        return getJsonString(getRecent(userId, number));
    }

    /**
     * Generate a JSON-like string from a given array of RecordedFood
     * @param foodArray An array of RecordedFood to generate JSON from
     * @return JSON string
     */
    public String getJsonString(List<RecordedFood> foodArray) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");

        for (RecordedFood current : foodArray) {
            stringBuilder.append(current.toString()).append(",");
        }

        if (!foodArray.isEmpty()) {
            stringBuilder.deleteCharAt(stringBuilder.toString().length() - 1);
        }

        stringBuilder.append("]");

        return stringBuilder.toString();
    }

}
