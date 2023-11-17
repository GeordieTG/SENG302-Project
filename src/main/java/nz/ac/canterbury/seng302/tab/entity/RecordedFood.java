package nz.ac.canterbury.seng302.tab.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;

/**
 * Entity to represent an activity
 */
@Entity
public class RecordedFood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_id")
    private Long id;

    /**
     * The recorded users for this meal
     */
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private TabUser user;

    /**
     * id of the food in the FoodData Central database
     */
    @Column(name = "fdc_id")
    private Long fdcId;

    /**
     * The quantity of the food in grams
     */
    @Column(name = "quantity")
    private Long quantity;

    /**
     * The date the meal was recorded
     */
    @Column(name = "date_recorded")
    private LocalDateTime dateRecorded;

    /**
     * The portion size number of the food
     */
    @Column(name = "portion_size_number")
    private Long portionSizeNumber;

    /**
     * The id of the meal
     */
    @Column(name = "meal_id")
    private Long mealId;


    /**
     * The id of the relevant
     */
    @Column(name = "activity_id")
    private Long activityId;

    /**
     * Constructor for RecodedMeal
     *
     * @param user         The user who recorded the meal
     * @param fdcId        id of the food in the FoodData Central database
     * @param dateRecorded The date the meal was recorded
     * @param mealId       Id of the meal
     */
    public RecordedFood(TabUser user, Long fdcId, LocalDateTime dateRecorded, Long quantity,
                        Long portionSizeNumber, Long mealId, Long activityId) {
        this.user = user;
        this.fdcId = fdcId;
        this.dateRecorded = dateRecorded;
        this.quantity = quantity;
        this.portionSizeNumber = portionSizeNumber;
        this.mealId = mealId;
        this.activityId = activityId;
    }

    /**
     * JPA required no-args constructor
     */
    public RecordedFood() {

    }

    public Long getFdcId() {
        return fdcId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TabUser getUser() {
        return user;
    }

    public void setUser(TabUser user) {
        this.user = user;
    }

    public Long getPortionSizeNumber() {
        return portionSizeNumber;
    }

    public Long getMealId() {
        return mealId;
    }

    public void setMealId(Long mealId) {
        this.mealId = mealId;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    /**
     * Json-like string generator. Deliberately does not include user id
     * @return String
     */
    public String toString() {

        return '{'
                + "\"mealId\": " + id + ","
                + "\"fdcId\": " + fdcId + ","
                + "\"time\": \"" + dateRecorded.toString() + "\","
                + "\"quantity\": " + quantity + ","
                + "\"portionSizeNumber\": " + portionSizeNumber
                + "}";
    }
}

