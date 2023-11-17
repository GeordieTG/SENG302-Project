package nz.ac.canterbury.seng302.tab.utility;

import java.util.Comparator;
import java.util.List;
import nz.ac.canterbury.seng302.tab.entity.Club;
import nz.ac.canterbury.seng302.tab.entity.Searchable;
import nz.ac.canterbury.seng302.tab.entity.Team;

/**
 * sort the club entity
 */
public class SortingUtil {
    /**
     * Empty Constructor to hide implicit one
     */
    private SortingUtil() {
    }

    /**
     * function to sort the team and the club name with a list of team and club
     *
     * @param teamAndClubs team and club list
     * @return a list with team and club
     */
    public static <T extends Searchable> List<T> sortTeamAndClubNameFirst(List<T> teamAndClubs) {
        return teamAndClubs.stream().sorted(Comparator.comparing(SortingUtil::getNameFromObject)
            .thenComparing(SortingUtil::getCountryFromObject)
            .thenComparing(SortingUtil::getCityFromObject)).toList();
    }

    /**
     * function to sort the team and the club locations with a list of team and club
     *
     * @param teamAndClubs team and club list
     * @return a list with team and club
     */
    public static <T extends Searchable> List<T> sortTeamAndClubLocation(List<T> teamAndClubs) {
        return teamAndClubs.stream().sorted(Comparator.comparing(SortingUtil::getCountryFromObject)
            .thenComparing(Comparator.comparing(SortingUtil::getCityFromObject).reversed())
            .thenComparing(SortingUtil::getNameFromObject)).toList();
    }

    /**
     * get the name for either team or the club
     *
     * @param obj the object to get the name
     * @return the name of the object
     */
    private static String getNameFromObject(Object obj) {
        if (obj instanceof Team team) {
            return team.getName();
        } else if (obj instanceof Club club) {
            return club.getName();
        }
        return "";
    }

    /**
     * get the country for either team or the club
     *
     * @param obj the object to get the country
     * @return the country of the object
     */
    private static String getCountryFromObject(Object obj) {
        if (obj instanceof Team team) {
            return team.getLocation().getCountry();
        } else if (obj instanceof Club club) {
            return club.getLocation().getCountry();
        }
        return "";
    }

    /**
     * get the city for either team or the club
     *
     * @param obj the object to get the city
     * @return the city of the object
     */
    private static String getCityFromObject(Object obj) {
        if (obj instanceof Team team) {
            return team.getLocation().getCity();
        } else if (obj instanceof Club club) {
            return club.getLocation().getCity();
        }
        return "";
    }
}
