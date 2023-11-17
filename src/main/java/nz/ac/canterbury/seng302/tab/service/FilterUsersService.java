package nz.ac.canterbury.seng302.tab.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.utility.FilterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Functionality for everything related to filtering users
 */
@Service
public class FilterUsersService {

    /**
     * To access the location functionalities with the database
     */
    @Autowired
    LocationService locationService;
    /**
     * To access the tabuser functionalities with the database
     */
    @Autowired
    TabUserService tabUserService;
    @Autowired
    private EntityManager entityManager;

    public FilterUsersService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Creates and executes the query with all the required filter parameters
     *
     * @param cities        list of strings, containing the cities to filter by
     * @param sports        List of strings, containing the sports to filter by
     * @param startPosition int, which page to get
     * @param search        String, the inputted search request that will match first and last names
     * @param allMatches    boolean, if true will return all matching results, if false
     *                      will return a max of 10 matching users
     * @return TabUser All the registered users who match the filter/search criteria
     */
    public List<TabUser> criteriaFilter(List<String> cities, List<String> sports, int startPosition,
                                        String search, boolean allMatches) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TabUser> criteriaQuery = criteriaBuilder.createQuery(TabUser.class);

        Root<TabUser> user = criteriaQuery.from(TabUser.class);
        Join<TabUser, Location> location = user.join("location", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        List<Predicate> sportPredicates = FilterUtils.addSportPredicates(criteriaBuilder, user,
            sports, "favouriteSport");
        List<Predicate> cityPredicates = FilterUtils.addCityPredicates(criteriaBuilder, location,
            cities);

        Predicate orPredicateSport =
            criteriaBuilder.or(sportPredicates.toArray(new Predicate[sportPredicates.size()]));
        Predicate orPredicateCity =
            criteriaBuilder.or(cityPredicates.toArray(new Predicate[cityPredicates.size()]));
        List<Predicate> searchPredicates = FilterUtils.addSearchPredicatesUsers(criteriaBuilder,
            user, search);
        Predicate orPredicateSearch =
            criteriaBuilder.or(searchPredicates.toArray(new Predicate[searchPredicates.size()]));

        if (!cities.get(0).equals("none")) {
            predicates.add(orPredicateCity);
        }
        if (!sports.get(0).equals("none")) {
            predicates.add(orPredicateSport);
        }
        if (!search.equals("none")) {
            predicates.add(orPredicateSearch);
        }

        if (!cities.get(0).equals("none") || !sports.get(0).equals("none")
            || !search.equals("none")) {
            criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
        }

        List<Order> orderList = new ArrayList<>();
        orderList.add(criteriaBuilder.desc(user.get("firstName")));
        orderList.add(criteriaBuilder.desc(user.get("lastName")));
        criteriaQuery.orderBy(orderList);

        if (allMatches) {
            return entityManager.createQuery(criteriaQuery).getResultList();
        }
        return entityManager.createQuery(criteriaQuery).setFirstResult(startPosition)
            .setMaxResults(10).getResultList();
    }
}
