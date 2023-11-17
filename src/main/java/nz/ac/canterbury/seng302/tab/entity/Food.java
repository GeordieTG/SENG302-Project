package nz.ac.canterbury.seng302.tab.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Entity to represent a single food
 */
@Entity
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_id")
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id") // Use the chosen logical column name
    private RecommendedMeal recommendedMeal;

    @Column
    private Long fdcId;

    @Column
    private Double calories;

    @Column
    private Double protein;

    @Column
    private Double fat;

    @Column
    private Double carbs;

    @Column
    private Double sugar;

    @Column
    private Double portion;

    @Column
    private String name;


    /**
     * JPA required no-args constructor
     *
     */
    public Food() {
        // empty constructor
    }

    public Double getProtein() {
        return protein;
    }

    public void setProtein(Double protein) {
        this.protein = protein;
    }

    public Double getPortion() {
        return portion;
    }

    public void setPortion(Double portion) {
        this.portion = portion;
    }

    public Double getSugar() {
        return sugar;
    }

    public void setSugar(Double sugar) {
        this.sugar = sugar;
    }

    public Double getCarbs() {
        return carbs;
    }

    public void setCarbs(Double carbs) {
        this.carbs = carbs;
    }

    public Double getFat() {
        return fat;
    }

    public void setFat(Double fat) {
        this.fat = fat;
    }

    public Double getCalories() {
        return calories;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public Long getfdcId() {
        return fdcId;
    }

    public void setfdcId(Long fdcId) {
        this.fdcId = fdcId;
    }

    public RecommendedMeal getMeal() {
        return recommendedMeal;
    }

    public void setMeal(RecommendedMeal recommendedMeal) {
        this.recommendedMeal = recommendedMeal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
