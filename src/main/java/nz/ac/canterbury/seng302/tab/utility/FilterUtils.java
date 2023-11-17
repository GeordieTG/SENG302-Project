package nz.ac.canterbury.seng302.tab.utility;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.TabUser;

/**
 * use to search the sport, city and country
 */
public class FilterUtils {
    /**
     * Empty Constructor to hide implicit one
     */
    private FilterUtils() {
    }

    /**
     * add the sport predicate  to the search string
     *
     * @param criteriaBuilder Used to construct criteria queries
     * @param entityRoot      The root entity
     * @param sports          the sport type that are selected
     * @param sportFieldName  the sport field name
     * @return a list of the predicate that has been created
     */
    public static List<Predicate> addSportPredicates(CriteriaBuilder criteriaBuilder,
                                                     Root<?> entityRoot,
                                                     List<String> sports,
                                                     String sportFieldName) {
        List<Predicate> sportPredicates = new ArrayList<>();
        if (!sports.get(0).equals("none")) {
            for (String sport : sports) {
                Predicate favouriteSport =
                    criteriaBuilder.like(criteriaBuilder.lower(entityRoot.get(sportFieldName)),
                        "%" + sport.toLowerCase() + "%");
                sportPredicates.add(favouriteSport);
            }
        }
        return sportPredicates;
    }

    /**
     * add the locaiton and city predicates to the search string
     *
     * @param criteriaBuilder Used to construct criteria queries
     * @param location        the locaiton that are selected
     * @param cities          the list of the city name
     * @return a list of the predicate that has been created
     */
    public static List<Predicate> addCityPredicates(CriteriaBuilder criteriaBuilder,
                                                    Join<?, Location> location,
                                                    List<String> cities) {
        List<Predicate> cityPredicates = new ArrayList<>();
        if (!cities.get(0).equals("none")) {
            for (String city : cities) {
                Predicate currentCity =
                    criteriaBuilder.like(criteriaBuilder.lower(location.get("city")),
                        "%" + city.toLowerCase() + "%");
                cityPredicates.add(currentCity);
            }
        }
        return cityPredicates;
    }

    /**
     * Adds the specific search to the database query
     *
     * @param criteriaBuilder Builder to build the query
     * @param entityRoot      Class root acting as the entity we want to search in
     * @param location        Join of the location entity to root
     * @param search          Specific search string
     * @param nameFieldName   String representing the field we want to search
     * @param checkLocation   Boolean representing whether we want to check the location
     * @return A list of predicates that the builder will deal with
     */
    public static List<Predicate> addSearchPredicates(CriteriaBuilder criteriaBuilder,
                                                      Root<?> entityRoot,
                                                      Join<?, Location> location,
                                                      String search,
                                                      String nameFieldName,
                                                      boolean checkLocation) {
        List<Predicate> searchPredicates = new ArrayList<>();
        if (!search.equals("none")) {
            if (!checkLocation) {
                Predicate namePredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(entityRoot.get(nameFieldName)),
                    "%" + search.toLowerCase() + "%");
                searchPredicates.add(namePredicate);
            }


            Predicate cityPredicate = criteriaBuilder.like(
                criteriaBuilder.lower(location.get("city")),
                "%" + search.toLowerCase() + "%");
            Predicate countryPredicate =
                criteriaBuilder.like(criteriaBuilder.lower(
                        location.get("country")),
                    "%" + search.toLowerCase() + "%");

            searchPredicates.add(cityPredicate);
            searchPredicates.add(countryPredicate);
        }
        return searchPredicates;
    }

    /**
     * Adds the required "like" statements for searching by name
     *
     * @param criteriaBuilder CriteriaBuilder, used to build the custom query
     * @param user            the root object for the query
     *                        (acts like a table would in SQL)
     * @param search          String, the inputted search request that
     *                        will match first and last names
     * @return A list of "like" statements for the query
     */
    public static List<Predicate> addSearchPredicatesUsers(CriteriaBuilder criteriaBuilder,
                                                           Root<TabUser> user,
                                                           String search) {

        List<Predicate> searchPredicates = new ArrayList<>();
        if (!search.equals("none")) {
            Predicate firstName = criteriaBuilder.like(criteriaBuilder.lower(user.get("firstName")),
                "%" + search.toLowerCase() + "%");
            Predicate lastName = criteriaBuilder.like(criteriaBuilder.lower(user.get("lastName")),
                "%" + search.toLowerCase() + "%");
            searchPredicates.add(firstName);
            searchPredicates.add(lastName);
        }
        return searchPredicates;
    }
}
