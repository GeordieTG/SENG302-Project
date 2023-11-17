package nz.ac.canterbury.seng302.tab.entity.datatransferobjects;

import java.util.List;

/**
 * Used when sending a recommended meal or group of recommended meals from JS.
 */
public class MealDto {

    private List<Long> mealIds;

    private List<Long> mealQuantities;

    private Long activityId;

    public MealDto() {

    }

    public List<Long> getMealIds() {
        return mealIds;
    }

    public void setMealIds(List<Long> mealIds) {
        this.mealIds = mealIds;
    }

    public List<Long> getMealQuantities() {
        return mealQuantities;
    }

    public void setMealQuantities(List<Long> mealQuantities) {
        this.mealQuantities = mealQuantities;
    }


    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    @Override
    public String toString() {
        return "MealDto{"
            + "mealIds=" + mealIds
            + "mealQuantities=" + mealQuantities
            + '}';
    }
}
