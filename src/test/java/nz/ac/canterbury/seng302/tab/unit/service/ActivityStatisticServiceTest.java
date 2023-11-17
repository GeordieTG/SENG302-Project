package nz.ac.canterbury.seng302.tab.unit.service;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import javassist.NotFoundException;
import nz.ac.canterbury.seng302.tab.entity.GameActivity;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.formobjects.PlayerScoreForm;
import nz.ac.canterbury.seng302.tab.repository.ActivityRepository;
import nz.ac.canterbury.seng302.tab.service.ActivityService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.TeamRoleService;
import nz.ac.canterbury.seng302.tab.service.TeamService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


/**
 * Test class which tests the service functionality related to an Activity's statistics
 */
@ExtendWith(MockitoExtension.class)
class ActivityStatisticServiceTest {

    TabUser user;

    Team team;

    Location location;

    GameActivity gameActivity;

    GameActivity gameActivity2;

    PlayerScoreForm playerScoreForm;

    @Mock
    TeamRoleService teamRoleService;
    @Mock
    TeamService teamService;

    @Mock
    TabUserService tabUserService;

    @Mock
    ActivityRepository activityRepository;

    @InjectMocks
    ActivityService activityService;


    @BeforeEach
    void beforeEach() {
        location = new Location("City", "Country");
        user = new TabUser(Arrays.asList("test", "person"), location, "email@email.com",
            new Date(2002, 12, 12).toString(),
            "*****", "image.jpg", "Baseball");

        team = new Team("team", "team", location, "team");

        gameActivity = new GameActivity(user, team, "Game", Arrays.asList("", ""),
            "", location, Arrays.asList("", "1"));
        gameActivity.setId(1L);

        gameActivity2 = new GameActivity(user, team, "Game", Arrays.asList("", ""),
            "", location, Arrays.asList("", "1"));
        gameActivity2.setId(2L);


    }

    /**
     * Tests that when a Player score form object with a null activity,
     * is used to add a Player score statistic to an activity, that an error is thrown
     */
    @Test
    void givenInvalidActivityId_whenAddingPlayerScore_thenExceptionThrown() {
        try {
            activityService.createPlayerScoreStatisticList(gameActivity,
                    List.of(new PlayerScoreForm(gameActivity.getId(), user.getId(), 5, 1)));
        } catch (NullPointerException e) {
            Assertions.assertNotNull(e);
        }
    }


    /**
     * Tests that when a valid player score form is used to add a Player score
     * statistic to an activity,
     * that the score is stored
     *
     * @throws NotFoundException when an activity is ot found, scored player is not found,
     *                           if activity type is not a game
     */
    @Test
    void givenValidPlayerScoreForm_whenAddingPlayerScore_thenPlayerScoreStored()
        throws NotFoundException {
        when(tabUserService.getById(Mockito.anyLong())).thenReturn(user);
        when(activityRepository.save(any())).thenReturn(gameActivity);
        GameActivity returnedGameActivity = activityService
            .createPlayerScoreStatisticList(gameActivity,
                List.of(new PlayerScoreForm(gameActivity.getId(), 3143154L,
                    5, 1)));
        Assertions.assertEquals(user, returnedGameActivity.getUser());
    }


    //    /**
    //     * Tests that when a Substitution form object with an invalid substituted player id,
    //     * is used to add a Substitution statistic to an activity, then an error is thrown.
    //     */
    //    @Test
    //    void givenInvalidSubstitutedPlayerId_whenAddingSubstitution_thenExceptionThrown() {
    //
    //
    //        Assertions.assertThrows(NullPointerException.class,
    //            () -> activityService.createSubstitutionStatisticList(gameActivity,
    //                List.of(new SubstitutionForm(eq(1L),
    //                        eq(2L),
    //                        eq(1L),
    //                        eq(1))),
    //                    false));
    //    }

    //
    //    /**
    //     * Tests that when a Substitution form object with an invalid substitute player id,
    //     * is used to add a Substitution statistic to an activity, then an error is thrown.
    //     */
    //    @Test
    //    void givenInvalidSubstitutePlayerId_whenAddingSubstitution_thenExceptionThrown() {
    //        when(tabUserService.getById(Mockito.anyLong())).thenAnswer(
    //            invocation -> (long) invocation.getArgument(0) == 1L ? user :
    //                null);
    //        Assertions.assertThrows(NullPointerException.class,
    //            () -> activityService.createSubstitutionStatisticList(gameActivity,
    //                List.of(new SubstitutionForm(eq(1L),
    //                        eq(1L),
    //                        eq(2L),
    //                        eq(1))),
    //                    false));
    //    }

    //    /**
    //     * Tests that when a Substitution form object with a null activity,
    //     * is used to add a Substitution statistic to an activity, that an error is thrown
    //     */
    //    @Test
    //    void givenInvalidActivityId_whenAddingSubstitution_thenExceptionThrown() {
    //        Assertions.assertThrows(NullPointerException.class,
    //            () -> activityService.createSubstitutionStatisticList(null,
    //                List.of(new SubstitutionForm(1L,
    //                        1L,
    //                        2L,
    //                        1)),
    //                    false));
    //    }

    //    /**
    //     * Tests that a valid player score form can be submitted
    //     */
    //    @Test
    //    void validPlayerScoreForm() throws NotFoundException {
    //        when(activityRepository.save(any())).thenAnswer(
    //                invocation -> invocation.getArgument(0));
    //        when(tabUserService.getById(anyLong())).thenReturn(user);
    //        playerScoreForm = new PlayerScoreForm(gameActivity.getId(), 3143154L, 5, 1);
    //        GameActivity returned = activityService.createPlayerScoreStatisticList(
    //                gameActivity, List.of(playerScoreForm));
    //
    //        StatisticPlayerScore stats = returned.getPlayerScoreStatistics().get(0);
    //        Assertions.assertEquals(playerScoreForm.getScore(), stats.getScore());
    //    }
    //
    //
    //    /**
    //     * Tests that when a valid score form is used to set the score of an activity,
    //     * that the activity returned has the correct score
    //     *
    //     * @throws NotFoundException when activity is not found in database
    //     */
    //    @Test
    //    void givenValidScoreForm_whenAddingScore_thenScoreStored() throws NotFoundException {
    //        when(activityService.getActivityById(Mockito.anyLong())).thenAnswer(
    //            invocation ->
    //            (long) invocation.getArgument(0) == gameActivity.getId() ? gameActivity :
    //                null);
    //        String homeScore = "1-1";
    //        ScoreForm scoreForm = new ScoreForm(homeScore, 1L);
    //        String returnedScore =
    //            ((GameActivity) activityService.setActivityScore(scoreForm)).getHomeScore();
    //        Assertions.assertEquals(homeScore, returnedScore);
    //    }
    //
    //    /**
    //     * Tests that when a valid score form with an opposition team's score is
    //     * used to set the score of an activity,
    //     * that the activity returned has the correct score
    //     *
    //     * @throws NotFoundException when activity is not found in database
    //     */
    //    @Test
    //    void givenValidScoreFormWithOppositionScore_whenAddingScore_thenScoreStored()
    //        throws NotFoundException {
    //        when(activityService.getActivityById(Mockito.anyLong())).thenAnswer(
    //            invocation ->
    //            (long) invocation.getArgument(0) == gameActivity.getId() ? gameActivity :
    //                null);
    //        String homeScore = "1-1";
    //        String oppositionScore = "2-2";
    //        ScoreForm scoreForm = new ScoreForm(homeScore, 1L, oppositionScore);
    //        GameActivity returnedActivity =
    //            ((GameActivity) activityService.setActivityScore(scoreForm));
    //        Assertions.assertEquals(homeScore, returnedActivity.getHomeScore());
    //        Assertions.assertEquals(oppositionScore, returnedActivity.getOppositionScore());
    //    }
    //
    //    /**
    //     * Tests that when a Score form object with a null activity,
    //     * is used to add a Score statistic to an activity, that an error is thrown
    //     */
    //    @Test
    //    void givenInvalidActivityId_whenSettingScore_thenExceptionThrown() {
    //        when(activityService.getActivityById(Mockito.anyLong())).thenReturn(null);
    //        Assertions.assertThrows(NotFoundException.class,
    //            () -> activityService.setActivityScore(new ScoreForm("", 1L)));
    //    }
    //
    //    /**
    //     * Tests that when a fact form object with a null activity,
    //     * is used to add a fact statistic to an activity, that an error is thrown
    //     */
    //    @Test
    //    void givenInvalidActivityId_whenAddingFact_thenExceptionThrown() {
    //        when(activityService.getActivityById(Mockito.anyLong())).thenReturn(null);
    //        Assertions.assertThrows(NotFoundException.class,
    //            () -> activityService.addFactStatistic(new FactForm(1L, "", 1)));
    //    }
}
