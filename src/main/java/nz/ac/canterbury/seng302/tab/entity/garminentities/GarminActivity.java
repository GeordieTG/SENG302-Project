package nz.ac.canterbury.seng302.tab.entity.garminentities;

import static java.time.ZoneId.systemDefault;
import static nz.ac.canterbury.seng302.tab.service.GarminService.ACTIVE_KILOCALORIES;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import nz.ac.canterbury.seng302.tab.entity.Activity;
import nz.ac.canterbury.seng302.tab.entity.TabUser;

/**
 * Garmin Activity Entity
 */

@Entity
public class GarminActivity {
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");
    @Id
    @Column(name = "garmin_activity_id")
    private Long activityId;
    @Column
    private int durationInSeconds;
    @Column
    private String activityType;
    @Column
    private int activeKilocalories;
    @Column
    private LocalDateTime startTime;
    @Column
    private double distanceInMeters;
    @Column
    private int averageHeartRate;
    @Column
    private int maxHeartRate;
    @JoinColumn(name = "user_id")
    @ManyToOne
    @JsonIgnore
    private TabUser tabUser;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "activity_id")
    private Activity activity;


    /**
     * Default constructor for a GarminActivity.
     */
    public GarminActivity() {
    }

    /**
     * Represents a Garmin activity with various attributes.
     * @param garminActivity JsonNode representing data from the api
     * @param tabUser entity representing a user
     */
    public GarminActivity(JsonNode garminActivity, TabUser tabUser) {

        if (garminActivity.has("activityId")) {
            this.activityId = garminActivity.get("activityId").asLong();
        }

        if (garminActivity.has("durationInSeconds")) {
            this.durationInSeconds = garminActivity.get("durationInSeconds").asInt();
        }

        if (garminActivity.has("activityType")) {
            this.activityType = garminActivity.get("activityType").asText()
                .replace("_", " ");
        }

        if (garminActivity.has(ACTIVE_KILOCALORIES)) {
            this.activeKilocalories = garminActivity.get(ACTIVE_KILOCALORIES).asInt();
        }

        if (garminActivity.has("startTimeInSeconds")) {
            this.startTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(garminActivity
                .get("startTimeInSeconds").asInt()), systemDefault());
        }

        if (garminActivity.has("distanceInMeters")) {
            this.distanceInMeters = garminActivity.get("distanceInMeters").asInt();
        }
        if (garminActivity.has("averageHeartRateInBeatsPerMinute")) {
            this.averageHeartRate = garminActivity.get("averageHeartRateInBeatsPerMinute").asInt();
        }

        if (garminActivity.has("maxHeartRateInBeatsPerMinute")) {
            this.maxHeartRate = garminActivity.get("maxHeartRateInBeatsPerMinute").asInt();
        }
        this.tabUser = tabUser;
    }


    public static DateTimeFormatter getFormatter() {
        return formatter;
    }

    public static void setFormatter(DateTimeFormatter formatter) {
        GarminActivity.formatter = formatter;
    }

    public int getAverageHeartRate() {
        return averageHeartRate;
    }

    public void setAverageHeartRate(int averageHeartRate) {
        this.averageHeartRate = averageHeartRate;
    }

    public int getMaxHeartRate() {
        return maxHeartRate;
    }

    public void setMaxHeartRate(int maxHeartRate) {
        this.maxHeartRate = maxHeartRate;
    }


    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    public void setDurationInSeconds(int durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }

    public String getActivityType() {
        return activityType.substring(0, 1).toUpperCase() + activityType.substring(1);
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public int getActiveKilocalories() {
        return activeKilocalories;
    }

    public void setActiveKilocalories(int activeKilocalories) {
        this.activeKilocalories = activeKilocalories;
    }

    public double getDistanceInMeters() {
        return distanceInMeters;
    }

    public void setDistanceInMeters(double distanceInMeters) {
        this.distanceInMeters = distanceInMeters;
    }

    public String getStartTimeFormatted() {
        return formatter.format(startTime.atZone(systemDefault()));
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public TabUser getTabUser() {
        return tabUser;
    }

    public void setTabUser(TabUser tabUser) {
        this.tabUser = tabUser;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
