package nz.ac.canterbury.seng302.tab.unit.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import nz.ac.canterbury.seng302.tab.controller.AddActivityStatisticsController;
import nz.ac.canterbury.seng302.tab.entity.Activity;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.formobjects.StatisticsForm;
import nz.ac.canterbury.seng302.tab.service.ActivityService;
import nz.ac.canterbury.seng302.tab.unit.UnitCommonTestSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

@ExtendWith(MockitoExtension.class)
class AddActivityStatisticsControllerTest {
    @Mock
    ActivityService activityService;
    @InjectMocks
    AddActivityStatisticsController addActStatsController;
    Long activityId;
    Model model = new ConcurrentModel();

    @BeforeEach
    void setUp() {
        activityId = 999L;
    }

    @Test
    void getCreateStatisticsForm() {
        StatisticsForm statisticsForm = new StatisticsForm();
        Activity activity = new Activity();
        Team testTeam = UnitCommonTestSetup.createTestTeam();
        activity.setTeam(testTeam);
        when(activityService.getStatistics(activityId))
            .thenReturn(statisticsForm);
        when(activityService.getActivityById(activityId))
            .thenReturn(activity);
        Assertions.assertEquals("addActivityStatistics",
            addActStatsController.getCreateStatisticsForm(activityId, model));
        Assertions.assertEquals(testTeam, model.getAttribute("myTeam"));
        Assertions.assertEquals(statisticsForm, model.getAttribute("wrapper"));
    }


    @Test
    void getFactForm() {
        Activity activity = new Activity();
        Team testTeam = UnitCommonTestSetup.createTestTeam();
        activity.setTeam(testTeam);
        when(activityService.getActivityById(anyLong())).thenReturn(activity);
        Assertions.assertEquals("addActivityFacts",
            addActStatsController.getFactForm(activityId, model));
        Assertions.assertEquals(StatisticsForm.class, model.getAttribute("wrapper").getClass());
    }

}
