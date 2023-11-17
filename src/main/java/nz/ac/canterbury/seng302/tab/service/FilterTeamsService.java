package nz.ac.canterbury.seng302.tab.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import nz.ac.canterbury.seng302.tab.entity.Club;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.Searchable;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.entity.TeamRoles;
import nz.ac.canterbury.seng302.tab.utility.FilterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Functionality for everything related to filtering teams
 */
@Service
public class FilterTeamsService {

    @Autowired
    TabUserService tabUserService;
    @Autowired
    LocationService locationService;
    @Autowired
    private EntityManager entityManager;

    public FilterTeamsService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Creates and executes the query with all the required filter parameters
     * for teams or clubs
     *
     * @param entityClass Class of the entity (Team.class or Club.class)
     * @param cities      List of strings, containing the cities to filter by
     * @param sports      List of strings, containing the sports to filter by
     * @param search      String, the inputted search request that will match
     *                    first and last
     *                    names
     * @param oneUser     int, flag indicating if filtering for a single user
     * @return List of teams or clubs, All the registered users who match the
     *         filter/search criteria
     */
    @Transactional
    public <T extends Searchable> List<T> criteriaFilter(Class<T> entityClass, List<String> cities,
                                                         List<String> sports, String search,
                                                         int oneUser, boolean checkLocation) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<?> criteriaQuery = criteriaBuilder.createQuery(entityClass);

        Root<T> entityRoot;
        Join<T, Location> location;
        Join<T, TabUser> user;

        if (entityClass.equals(Team.class)) {
            entityRoot = (Root<T>) criteriaQuery.from(Team.class);
            location = entityRoot.join("location", JoinType.LEFT);
            Join<Team, TeamRoles> teamRoles = entityRoot.join(
                "teamRoles", JoinType.LEFT);
            user = teamRoles.join("user", JoinType.LEFT);
        } else if (entityClass.equals(Club.class)) {
            entityRoot = (Root<T>) criteriaQuery.from(Club.class);
            location = entityRoot.join("location", JoinType.LEFT);
            user = entityRoot.join("owner", JoinType.LEFT);
        } else {
            throw new IllegalArgumentException("Unsupported entity class: "
                + entityClass);
        }

        List<Predicate> predicates = new ArrayList<>();
        List<Predicate> searchPredicates =
            FilterUtils.addSearchPredicates(criteriaBuilder, entityRoot,
                location, search, "name", checkLocation);
        List<Predicate> sportPredicates =
            FilterUtils.addSportPredicates(criteriaBuilder, entityRoot, sports,
                "sport");
        List<Predicate> cityPredicates =
            FilterUtils.addCityPredicates(criteriaBuilder, location, cities);

        Predicate orPredicateSport =
            criteriaBuilder.or(sportPredicates.toArray(new Predicate[sportPredicates.size()]));
        Predicate orPredicateCity =
            criteriaBuilder.or(cityPredicates.toArray(new Predicate[cityPredicates.size()]));
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
            if (oneUser == 0) {
                Predicate finalPredicate = criteriaBuilder.and(
                    criteriaBuilder.equal(user.get("id"),
                        tabUserService.getCurrentlyLoggedIn().getId()),
                    criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]))
                );
                criteriaQuery.where(finalPredicate);
            } else {
                criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
            }
        } else {
            if (oneUser == 0) {
                criteriaQuery.where(criteriaBuilder.equal(user.get("id"),
                    tabUserService.getCurrentlyLoggedIn().getId()));
            }
        }


        return (List<T>) entityManager.createQuery(criteriaQuery).getResultList();
    }

    /**
     * Adds different cities and sports depending on a list
     *
     * @param list   list of Team and Clubs
     * @param sports list of strings representing sports
     * @param cities list of strings representing cites
     * @throws IOException if Location does not exist
     */
    public void addCityAndSportFilters(List<? extends Searchable> list,
                                       Set<String> sports, Set<String> cities)
        throws IOException {
        for (Object obj : list) {
            if (obj instanceof Team team) {
                cities.add(locationService.getLocationByLocationId(
                    team.getLocationId()).getCity());
                sports.add(team.getSport());
            } else if (obj instanceof Club club) {
                cities.add(locationService.getLocationByLocationId(
                    club.getLocation().getLocationId()).getCity());
                sports.add(club.getSport().toString());
            }
        }
    }
}
