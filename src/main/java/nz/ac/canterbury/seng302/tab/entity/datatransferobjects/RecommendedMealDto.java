package nz.ac.canterbury.seng302.tab.entity.datatransferobjects;

import java.util.List;
import nz.ac.canterbury.seng302.tab.entity.Food;

/**
 * Data Transfer Object to pass Java Objects through to JavaScript.
 * Represents a recommended meal with its nutritional information, quantity and the list
 * of foods that make up the meal
 */
public class RecommendedMealDto {

    /**
     * The name of the meal
     */
    private String name;

    /**
     * The total calories of the meal
     */
    private Double calories;

    /**
     * The total protein of the meal
     */
    private Double protein;

    /**
     * The total fat of the meal
     */
    private Double fat;

    /**
     * The total sugar of the meal
     */
    private Double sugar;

    /**
     * The quantity of the meal which is recommended to be eaten
     */
    private double quantity;

    private Long mealId;

    /**
     * The list of foods that make up the meal
     */
    private List<Food> foods;

    private String icon;

    /**
     * Constructor for RecommendedMealDto
     *
     * @param name the name of the meal
     * @param calories the total calories of the meal
     * @param protein the total protein of the meal
     * @param fat the total fat of the meal
     * @param sugar the total sugar of the meal
     * @param foods the list of foods that make up the meal
     * @param quantity the quantity of the meal which is recommended to be eaten
     * @param icon the icon of the meal
     */
    public RecommendedMealDto(String name, Double calories, Double protein, Double fat,
                              Double sugar, List<Food> foods, Long mealId, double quantity,
                              String icon) {
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.sugar = sugar;
        this.quantity = quantity;
        this.foods = foods;
        this.mealId = mealId;
        this.icon = icon;
    }


    public String getName() {
        return name;
    }



    public Double getCalories() {
        return calories;
    }


    public Double getProtein() {
        return protein;
    }


    public Double getFat() {
        return fat;
    }

    public Double getSugar() {
        return sugar;
    }


    public double getQuantity() {
        return quantity;
    }

    public List<Food> getFoods() {
        return foods;
    }

    public Long getMealId() {
        return mealId;
    }

    public void setMealId(Long mealId) {
        this.mealId = mealId;
    }

    public String getIcon() {
        return icon;
    }
}
