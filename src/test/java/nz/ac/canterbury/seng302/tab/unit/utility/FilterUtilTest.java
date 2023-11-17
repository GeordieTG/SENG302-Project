package nz.ac.canterbury.seng302.tab.unit.utility;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.utility.FilterUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class FilterUtilTest {

    CriteriaBuilder criteriaBuilder;
    Root<?> entityRoot;
    Join<?, Location> location;
    Root<TabUser> user;

    @BeforeEach
    void setUp() {
        criteriaBuilder = mock(CriteriaBuilder.class);
        entityRoot = mock(Root.class);
        location = mock(Join.class);
        user = mock(Root.class);
    }

    @Test
    void testAddSportPredicatesNoSportsSelected() {
        List<String> sports = new ArrayList<>();
        sports.add("none");

        List<Predicate> result = FilterUtils.addSportPredicates(criteriaBuilder,
            entityRoot, sports, "sport");

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testAddSportPredicatesSingleSport() {
        List<String> sports = new ArrayList<>();
        sports.add("Soccer");

        Predicate predicateMock = mock(Predicate.class);
        when(criteriaBuilder.like(any(), anyString())).thenReturn(predicateMock);

        List<Predicate> result = FilterUtils.addSportPredicates(criteriaBuilder,
            entityRoot, sports, "sport");

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(predicateMock, result.get(0));
        verify(criteriaBuilder).like(any(), anyString());
    }

    @Test
    void testAddSportPredicatesMultipleSports() {
        List<String> sports = new ArrayList<>();
        sports.add("Soccer");
        sports.add("Basketball");

        Predicate predicateMock = mock(Predicate.class);
        when(criteriaBuilder.like(any(), anyString())).thenReturn(predicateMock);

        List<Predicate> result = FilterUtils.addSportPredicates(criteriaBuilder,
            entityRoot, sports, "sport");

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(predicateMock, result.get(0));
        Assertions.assertEquals(predicateMock, result.get(1));
        verify(criteriaBuilder, times(2)).like(any(), anyString());
    }

    @Test
    void testAddCityPredicatesNoCitiesSelected() {
        List<String> cities = new ArrayList<>();
        cities.add("none");

        List<Predicate> result = FilterUtils.addCityPredicates(criteriaBuilder, location, cities);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testAddCityPredicatesSingleCity() {
        List<String> cities = new ArrayList<>();
        cities.add("New York");

        Predicate predicateMock = mock(Predicate.class);
        when(criteriaBuilder.like(any(), anyString())).thenReturn(predicateMock);

        List<Predicate> result = FilterUtils.addCityPredicates(criteriaBuilder, location, cities);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(predicateMock, result.get(0));
        verify(criteriaBuilder).like(any(), anyString());
    }

    @Test
    void testAddCityPredicatesMultipleCities() {
        List<String> cities = new ArrayList<>();
        cities.add("Los Angeles");
        cities.add("Chicago");

        Predicate predicateMock = mock(Predicate.class);
        when(criteriaBuilder.like(any(), anyString())).thenReturn(predicateMock);

        List<Predicate> result = FilterUtils.addCityPredicates(criteriaBuilder, location, cities);

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(predicateMock, result.get(0));
        Assertions.assertEquals(predicateMock, result.get(1));
        verify(criteriaBuilder, times(2)).like(any(), anyString());
    }

    @Test
    void testAddSearchPredicatesNoSearchTerm() {
        List<Predicate> result = FilterUtils.addSearchPredicates(criteriaBuilder, entityRoot,
            location, "none", "nameField", false);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testAddSearchPredicatesWithoutLocation() {
        when(criteriaBuilder.like(any(), anyString())).thenReturn(mock(Predicate.class));

        List<Predicate> result = FilterUtils.addSearchPredicates(criteriaBuilder, entityRoot,
            location, "searchTerm", "nameField", false);

        Assertions.assertEquals(3, result.size());
        verify(criteriaBuilder, times(3)).like(any(), anyString());
    }

    @Test
    void testAddSearchPredicatesWithLocation() {
        when(criteriaBuilder.like(any(), anyString())).thenReturn(mock(Predicate.class));

        List<Predicate> result = FilterUtils.addSearchPredicates(criteriaBuilder, entityRoot,
            location, "searchTerm", "nameField", true);

        Assertions.assertEquals(2, result.size());
        verify(criteriaBuilder, times(2)).like(any(), anyString());
    }

    @Test
    void testAddSearchPredicatesUsersNoSearchTerm() {
        List<Predicate> result = FilterUtils.addSearchPredicatesUsers(criteriaBuilder,
            user, "none");

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testAddSearchPredicatesUsersWithSearchTerm() {
        when(criteriaBuilder.like(any(), anyString())).thenReturn(mock(Predicate.class));

        List<Predicate> result = FilterUtils.addSearchPredicatesUsers(criteriaBuilder,
            user, "searchTerm");

        Assertions.assertEquals(2, result.size());
        verify(criteriaBuilder, times(2)).like(any(), anyString());
    }
}
