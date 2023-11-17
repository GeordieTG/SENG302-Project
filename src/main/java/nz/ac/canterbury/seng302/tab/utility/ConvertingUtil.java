package nz.ac.canterbury.seng302.tab.utility;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Used to convert from a full date string to a string of the time
 */
public class ConvertingUtil {
    /**
     * Hidden Constructor for SonarQube
     */
    private ConvertingUtil() {}

    /**
     * Converts a date to a typical hour:minute time (10:30) used by the calendar
     *
     * @param date The start time of the activity
     * @return a string representing the start time of an activity
     */
    public static String convertDateToTime(String date) {
        LocalDateTime dateTime = LocalDateTime.parse(date, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    /**
     * Converts a given formation position string into a ArrayList String[] for processing:
     * "1,2,3,4-5,6-7,8,9"  -> [["1","2","3","4"], ["4","5"], ["6","7","8","9"]]
     *
     * @param positionString formation string ["1","2","9","4"]
     * @return ArrayList String[]
     */
    public static List<String[]> positionStringToArray(String positionString) {
        String[] firstSplit = positionString.split("-"); //["1,2,3,4", "5,6", "7,8,9"]
        ArrayList<String[]> convertedArray = new ArrayList<>();
        for (String layer : firstSplit) {
            convertedArray.add(layer.split(",")); //["1", "2", "3", "4"]
        }
        return convertedArray;
    }

    /**
     * get activity duration from formatted start and end time
     *
     * @param start start time
     * @param end   end time
     * @return total time in minutes
     */
    public static long getActivityDuration(String start, String end) {
        // Create DateTimeFormatter for the specified format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        // Parse the strings to LocalDateTime objects
        LocalDateTime startTime = LocalDateTime.parse(start, formatter);
        LocalDateTime endTime = LocalDateTime.parse(end, formatter);

        // Calculate the duration between the two LocalDateTime objects
        Duration duration = Duration.between(startTime, endTime);

        // Get the time difference in minutes
        return duration.toMinutes();
    }

    /**
     * Converts a list of good IDs to a comma-separated values (CSV) string.
     *
     * @param goodIds The list of good IDs to be converted to CSV.
     * @return A CSV string representation of the provided list of good IDs.
     */
    public static String convertToCsv(List<Long> goodIds) {
        StringJoiner goodIdsCsv = new StringJoiner(",");
        for (Long id : goodIds) {
            goodIdsCsv.add(id.toString());
        }
        return goodIdsCsv.toString();
    }
}
