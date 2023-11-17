package nz.ac.canterbury.seng302.tab.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import nz.ac.canterbury.seng302.tab.entity.Activity;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.formobjects.SubstitutionForm;
import nz.ac.canterbury.seng302.tab.repository.TeamRoleRepository;
import nz.ac.canterbury.seng302.tab.service.TeamRoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * Unit tests for te TeamRoleService Class
 */
@SpringBootTest
class TeamRoleServiceTest {

    @MockBean
    TeamRoleRepository mockTeamRoleRepository;

    @Autowired
    TeamRoleService teamRoleService;

    Location location;

    TabUser tabUser;

    Activity activity;

    Team team;

    @BeforeEach
    void setup() {

        location = new Location("Greymouth", "New Zealand");

        tabUser = new TabUser(Arrays.asList("Bob", "Smith"), location, "bob@email.com",
            new Date(2002, 11, 31).toString(), "*****", "default.jpg",
            "basketball");

        team = new Team("Bishops", "Basketball", location, "pfp");

        team.setId(1);

        activity = new Activity(tabUser, team, "Game", "2023-07-17T14:18", "2023-07-17T15:18",
            "Description", location);

        activity.setPosition("1-2");
    }

    /**
     * Tests the calculation of the length of an activity in minutes
     * End time will always be after Start Time as it is validated on form submission (Contract)
     */
    @Test
    void getActivityDurationTest() {

        String startTime = "2023-07-17T14:18";
        String endTime = "2023-07-17T15:18";

        long result = teamRoleService.getActivityDuration(startTime, endTime);

        Assertions.assertEquals(60, result);
    }

    /**
     * Tests if the players time off the field is calculated correctly
     * when they haven't been subbed off
     */
    @Test
    void addTotalTimePlayedTest_NoSubstitutions() {

        List<SubstitutionForm> substitutionForms = new ArrayList<>();
        teamRoleService.addTotalTimePlayed(activity, substitutionForms, false, false);

        ArgumentCaptor<Long> timeSpentOnCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(mockTeamRoleRepository, Mockito.atLeastOnce())
            .updateUserTimePlayed(any(), eq("1"), timeSpentOnCaptor.capture());

        Assertions.assertEquals(60, timeSpentOnCaptor.getAllValues().get(0));

    }

    /**
     * Tests if the players time off the field is calculated correctly
     * when they start on, but get subbed off
     */
    @Test
    void addTotalTimePlayedTest_OneSubstitution_PlayerComesOff() {

        List<SubstitutionForm> substitutionForms = new ArrayList<>();
        substitutionForms.add(new SubstitutionForm(1L, 1L, 2L, 10));

        teamRoleService.addTotalTimePlayed(activity, substitutionForms, false, false);

        ArgumentCaptor<Long> timeSpentOnCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(mockTeamRoleRepository, Mockito.atLeastOnce())
            .updateUserTimePlayed(any(), eq("1"), timeSpentOnCaptor.capture());
        Assertions.assertEquals(10, timeSpentOnCaptor.getAllValues().get(0));
    }

    /**
     * Tests if the players time off the field is calculated correctly
     * when they start off, but get subbed on
     */
    @Test
    void addTotalTimePlayedTest_OneSubstitution_PlayerComesOn() {

        List<SubstitutionForm> substitutionForms = new ArrayList<>();
        substitutionForms.add(new SubstitutionForm(1L, 1L, 2L, 10));

        teamRoleService.addTotalTimePlayed(activity, substitutionForms, false, true);

        ArgumentCaptor<Long> timeSpentOnCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(mockTeamRoleRepository, Mockito.atLeastOnce())
            .updateUserTimePlayed(any(), eq("2"), timeSpentOnCaptor.capture());

        Assertions.assertEquals(50, timeSpentOnCaptor.getAllValues().get(0));
    }

    /**
     * Tests if the players time off the field is calculated correctly
     * when they start off, get subbed on, then back off
     */
    @Test
    void addTotalTimePlayedTest_MultipleSubstitutions_Off_On_Off() {
        List<SubstitutionForm> substitutionForms = new ArrayList<>();
        substitutionForms.add(new SubstitutionForm(1L, 1L, 2L, 10));
        substitutionForms.add(new SubstitutionForm(1L, 2L, 1L, 45));

        teamRoleService.addTotalTimePlayed(activity, substitutionForms, false, false);

        ArgumentCaptor<Long> timeSpentOnCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(mockTeamRoleRepository, Mockito.atLeastOnce())
            .updateUserTimePlayed(any(), eq("1"), timeSpentOnCaptor.capture());

        Long timeSpentOnFirst = timeSpentOnCaptor.getAllValues().get(0);
        Assertions.assertEquals(10, timeSpentOnFirst);

        Long timeSpentOnSecond = timeSpentOnCaptor.getAllValues().get(1);
        Assertions.assertEquals(15, timeSpentOnSecond);

        Long totalTime = timeSpentOnFirst + timeSpentOnSecond;
        Assertions.assertEquals(25, totalTime);
    }

    /**
     * Tests if the players time off the field is calculated correctly
     * when they start on, get subbed off, then back on
     */
    @Test
    void addTotalTimePlayedTest_MultipleSubstitutions_On_Off_On() {
        List<SubstitutionForm> substitutionForms = new ArrayList<>();
        substitutionForms.add(new SubstitutionForm(1L, 1L, 2L, 10));
        substitutionForms.add(new SubstitutionForm(1L, 2L, 1L, 45));

        teamRoleService.addTotalTimePlayed(activity, substitutionForms, false, false);

        ArgumentCaptor<Long> timeSpentOnCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(mockTeamRoleRepository, Mockito.atLeastOnce())
            .updateUserTimePlayed(any(), eq("2"), timeSpentOnCaptor.capture());

        Long timeSpentOnFirst = timeSpentOnCaptor.getAllValues().get(0);
        Assertions.assertEquals(50, timeSpentOnFirst);

        Long timeSpentOnSecond = timeSpentOnCaptor.getAllValues().get(1);
        Assertions.assertEquals(-15, timeSpentOnSecond);

        Long totalTime = timeSpentOnFirst + timeSpentOnSecond;
        Assertions.assertEquals(35, totalTime);
    }

    /**
     * Tests the substitution Events are sorted in the ascending order
     */
    @Test
    void testSortSubstituteEvents() {

        List<SubstitutionForm> substitutionForms = new ArrayList<>();
        substitutionForms.add(new SubstitutionForm(1L, 2L, 3L, 20));
        substitutionForms.add(new SubstitutionForm(1L, 2L, 3L, 10));
        substitutionForms.add(new SubstitutionForm(1L, 2L, 3L, 30));

        TeamRoleService teamRoleService = new TeamRoleService();
        teamRoleService.sortSubstitutionEvents(substitutionForms);

        assertEquals(3, substitutionForms.size());
        assertEquals(10, substitutionForms.get(0).getSubstituteTime());
        assertEquals(30, substitutionForms.get(substitutionForms.size() - 1).getSubstituteTime());
    }
}
