package nz.ac.canterbury.seng302.tab.entity.garminentities;

/**
 * Represents a daily summary retrieved from the Garmin Wellness API.
 * Contains information about the user's daily activities and heart rate data.
 */
public class GarminDaily {
    private final double activeKilocalories;
    private final double activeTimeHrs;
    private final int steps;

    private final int stepsGoal;
    private final double distanceInMeters;

    private final HeartRateData heartRateData;
    private final boolean validData;

    /**
     * Constructs a GarminDaily object with the provided data.
     *
     * @param activeKilocalories The amount of active kilocalories burned.
     * @param activeTimeHrs      The amount of Active Time Spent.
     * @param steps              The number of steps taken.
     * @param distanceInMeters   The distance covered in meters.
     * @param heartRateData      Heart rate data associated with the daily summary.
     */
    public GarminDaily(double activeKilocalories,
                       double activeTimeHrs, int steps, int stepsGoal, double distanceInMeters,
                       HeartRateData heartRateData, boolean validData) {
        this.activeKilocalories = activeKilocalories;
        this.activeTimeHrs = activeTimeHrs;
        this.steps = steps;
        this.stepsGoal = stepsGoal;
        this.distanceInMeters = distanceInMeters;
        this.heartRateData = heartRateData;
        this.validData = validData;
    }

    public double getActiveKilocalories() {
        return activeKilocalories;
    }

    public String getActiveTimeHrs() {
        return String.format("%.2f", activeTimeHrs);
    }

    public int getSteps() {
        return steps;
    }

    public int getStepsGoal() {
        return stepsGoal;
    }

    public double getDistanceInMeters() {
        return distanceInMeters;
    }

    public HeartRateData getHeartRateData() {
        return heartRateData;
    }

    public boolean getValidData() {
        return validData;
    }
}
