package nz.ac.canterbury.seng302.tab.entity.garminentities;

/**
 * Represents heart rate data associated with a daily summary retrieved from the Garmin
 * Wellness API.
 * Contains information about minimum, maximum, average, and resting heart rates.
 */
public class HeartRateData {
    private final int averageHeartRate;

    /**
     * Constructs a HeartRateData object with the provided heart rate values.
     *
     * @param minHeartRate     The minimum heart rate value.
     * @param maxHeartRate     The maximum heart rate value.
     * @param averageHeartRate The average heart rate value.
     * @param restingHeartRate The resting heart rate value.
     */
    public HeartRateData(int minHeartRate, int maxHeartRate,
                         int averageHeartRate, int restingHeartRate) {
        this.averageHeartRate = averageHeartRate;
    }

    public int getAverageHeartRate() {
        return averageHeartRate;
    }

}



