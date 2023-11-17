package nz.ac.canterbury.seng302.tab.unit.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import nz.ac.canterbury.seng302.tab.entity.Activity;
import nz.ac.canterbury.seng302.tab.entity.GameActivity;
import nz.ac.canterbury.seng302.tab.entity.StatisticFact;
import nz.ac.canterbury.seng302.tab.entity.StatisticSubstitution;
import nz.ac.canterbury.seng302.tab.formobjects.FactForm;
import nz.ac.canterbury.seng302.tab.formobjects.SubstitutionForm;
import nz.ac.canterbury.seng302.tab.repository.ActivityRepository;
import nz.ac.canterbury.seng302.tab.repository.SubstituteStatisticsRepository;
import nz.ac.canterbury.seng302.tab.service.ActivityService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.TeamRoleService;
import nz.ac.canterbury.seng302.tab.unit.UnitCommonTestSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

/**
 * Tests for the Activity Service
 */
@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {

    @InjectMocks
    ActivityService activityService;
    @Mock
    ActivityRepository activityRepository;
    @Mock
    SubstituteStatisticsRepository substituteStatisticsRepository;
    @Mock
    TeamRoleService teamRoleService;
    @Mock
    TabUserService tabUserService;
    @Mock
    Model model;
    @Mock
    GameActivity activityMocked;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void getActivityFactsTest() {
        activityService.getActivityFacts(1);
        Object[] obj = new Object[] {1, "Facts", 89, 2};
        List<Object> objectList = new ArrayList<>();
        objectList.add(obj);
        objectList.add(obj);
        when(activityRepository.getActivityFacts(1)).thenReturn(objectList);
        List<String> factsList = activityService.getActivityFacts(1);
        Assertions.assertEquals("Facts at 89'", factsList.get(0));
        Assertions.assertEquals("Facts at 89'", factsList.get(1));
    }

    @Test
    void testCreateSubstitutionStatisticList_FirstTime() {
        GameActivity gameActivity = new GameActivity();
        gameActivity.setId(1L);
        List<SubstitutionForm> substitutionForms = new ArrayList<>();
        boolean firstTime = true;

        when(activityRepository.save(any())).thenReturn(gameActivity);

        // Call the method
        GameActivity result = activityService.createSubstitutionStatisticList(gameActivity,
                substitutionForms, firstTime);

        Assertions.assertEquals(gameActivity, result);
        verify(tabUserService, times(substitutionForms.size() * 2)).getById(anyLong());
        verify(substituteStatisticsRepository, times(substitutionForms.size())).findById(anyLong());
    }

    @Test
    void testCreateSubstitutionStatisticList_NotFirstTime() {
        GameActivity gameActivity = new GameActivity();
        gameActivity.setId(1L);
        List<SubstitutionForm> substitutionForms = new ArrayList<>();
        boolean firstTime = false;

        when(activityRepository.save(any())).thenReturn(gameActivity);

        GameActivity result = activityService.createSubstitutionStatisticList(gameActivity,
                substitutionForms, firstTime);


        Assertions.assertEquals(gameActivity, result);
        verify(tabUserService, times(substitutionForms.size() * 2)).getById(anyLong());
        verify(substituteStatisticsRepository,
                times(substitutionForms.size())).findById(anyLong());
    }

    @Test
    void testCreateSubstitutionStatisticList_ExistingStats() {
        GameActivity gameActivity = new GameActivity();
        gameActivity.setId(1L);

        when(tabUserService.getById(anyLong())).thenReturn(UnitCommonTestSetup.createTestUser());
        StatisticSubstitution existingStat = new StatisticSubstitution();
        existingStat.setId(1L);
        existingStat.setGameActivity(gameActivity);
        existingStat.setSubstitutedPlayer(UnitCommonTestSetup.createTestUser("John"));
        existingStat.setSubstitutePlayer(UnitCommonTestSetup.createTestUser("Jane"));
        gameActivity.addSubstitutionStatistic(List.of(existingStat));
        when(substituteStatisticsRepository.findById(1L)).thenReturn(existingStat);
        when(activityRepository.save(any())).thenReturn(gameActivity);

        boolean firstTime = true;
        SubstitutionForm substitutionForm = new SubstitutionForm(gameActivity.getId(),
                1L, 2L, 30);
        List<SubstitutionForm> substitutionForms = new ArrayList<>();
        substitutionForms.add(substitutionForm);
        GameActivity result = activityService.createSubstitutionStatisticList(gameActivity,
                substitutionForms, firstTime);

        Assertions.assertEquals(gameActivity, result);
        verify(tabUserService, times(substitutionForms.size() * 2)).getById(anyLong());
        verify(substituteStatisticsRepository, times(substitutionForms.size())).findById(anyLong());
        Assertions.assertEquals(existingStat.getSubstitutedPlayer(),
                result.getSubstitutionStatistics().get(0).getSubstitutedPlayer());
        Assertions.assertEquals(existingStat.getSubstitutePlayer(),
                result.getSubstitutionStatistics().get(0).getSubstitutePlayer());
    }

    @Test
    void testCreatePersonalEvents() {
        Activity activity = UnitCommonTestSetup.createTestActivity();
        List<List<String>> results = activityService.createPersonalEvents(List.of(activity));
        List<List<String>> expected = List.of(List.of("true", "Game", "2023-07-17T14:18", "2023"
                + "-07-17T15:18", "1", "Personal", "City", "Country", "14:18"));
        for (int index = 0; index < results.size(); index++) {
            Assertions.assertEquals(results.get(0).get(index), expected.get(0).get(index));
        }

    }
}
