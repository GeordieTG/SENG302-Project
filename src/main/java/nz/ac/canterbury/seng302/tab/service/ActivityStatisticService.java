package nz.ac.canterbury.seng302.tab.service;

import java.util.Arrays;
import java.util.List;
import nz.ac.canterbury.seng302.tab.entity.Activity;
import nz.ac.canterbury.seng302.tab.entity.StatisticPlayerScore;
import nz.ac.canterbury.seng302.tab.entity.StatisticSubstitution;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.repository.PlayerScoreStatisticRepository;
import nz.ac.canterbury.seng302.tab.repository.SubstituteStatisticsRepository;
import nz.ac.canterbury.seng302.tab.utility.ConvertingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The class that related to get the statistics substitution
 */
@Service
public class ActivityStatisticService {

    @Autowired
    SubstituteStatisticsRepository substituteStatisticsRepository;

    @Autowired
    PlayerScoreStatisticRepository playerScoreStatisticRepository;

    @Autowired
    TeamRoleService teamRoleService;

    @Autowired
    ActivityService activityService;

    @Autowired
    FormationService formationService;

    /**
     * function to get the substitution statistics from the database
     *
     * @param activityId the substitutions activity
     * @return list of activity substitutions
     */
    public List<StatisticSubstitution> getStatisticSubstitution(long activityId) {
        return substituteStatisticsRepository.getStatisticSubstitution(activityId);
    }

    /**
     * function to get the player scores from the database
     *
     * @param activityId the score form activity
     * @return list of activity scorers
     */
    public List<StatisticPlayerScore> getStatisticPlayerScore(long activityId) {
        return playerScoreStatisticRepository.getStatisticPlayerScore(activityId);
    }

    /**
     * function to get the player scores from the database
     *
     * @param activityId the score form activity
     * @return list of activity scorers
     */
    public List<StatisticPlayerScore> getPersonalScoringEventsForActivity(long activityId,
                                                                          long userId) {
        return playerScoreStatisticRepository.getPersonalScoringEventsForActivity(activityId,
            userId);
    }

    /**
     * Method for calculating the total play time of a player for an activity
     *
     * @param activityId id of the activity
     * @param userId     id of the user
     * @return the total play time of a player for an activity
     */
    public int getPersonalTotalTimePlayedForActivity(long activityId, long userId) {

        int playersTotalTime = 0;
        boolean playerStarted = false;
        boolean playerOn = false;
        int playerOnAtTime = 0;

        // Get the activity by the Id
        Activity activity = activityService.getActivityById(activityId);

        // Get the duration of the activity
        long activityTime = activity.getDuration();
        teamRoleService.getActivityDuration(activity.getStartTime(), activity.getEndTime());

        // Get all the substitution events for the activity
        List<StatisticSubstitution> substitutionsEvents =
                substituteStatisticsRepository.getStatisticSubstitution(activityId);
        int numOfEvents = substitutionsEvents.size();
        int eventNum = 0;

        playerStarted = isPlayerStarted(userId, playerStarted, activity);
        playerOn = playerStarted;
        for (StatisticSubstitution statisticSubstitution : substitutionsEvents) {

            // Player got substituted (came off)
            if (statisticSubstitution.getSubstitutedPlayer().getId() == userId) {
                eventNum += 1;
                playerOn = false;
                // If the player started on and got subbed off, then set the players total time to
                // the time of the substitute event
                if (playerStarted) {
                    playerStarted = false;
                }

                // Set player total time to the time of the sub event
                // minus the time the player was on
                playersTotalTime += (statisticSubstitution.getSubstituteTime() - playerOnAtTime);

            }

            // Player is the substitute (came on)
            if (statisticSubstitution.getSubstitutePlayer().getId() == userId) {
                eventNum += 1;
                playerOn = true;
                playerOnAtTime = statisticSubstitution.getSubstituteTime();
            }

            // If the player is still subbed in and was not subbed off for the rest of the activity
            if (eventNum == numOfEvents && playerOn) {
                playersTotalTime += (activityTime - playerOnAtTime);
            }
        }

        if (eventNum == 0) {
            playersTotalTime = (int) activityTime;
        }

        return playersTotalTime;
    }


    /**
     * Check if a player started the match
     *
     * @param userId player to be checked
     * @param playerStarted current player started variable
     * @param activity current activity
     * @return true if the player started else false
     */
    private boolean isPlayerStarted(long userId, boolean playerStarted, Activity activity) {
        if (activity.getPosition() != null) {
            // If player started on at the beginning of the activity
            List<String[]> positionList =
                ConvertingUtil.positionStringToArray(activity.getPosition());
            for (String[] row : positionList) {
                if (Arrays.stream(row).anyMatch(Long.toString(userId)::equals)) {
                    playerStarted = true;
                }
            }

        }
        return playerStarted;
    }

    /**
     * Gets the personal substitution statistics for a particular user
     * @param activityId id of the activity
     * @param user Entity object representing a user
     * @return a list of entity objects representing substitutions
     */
    public List<StatisticSubstitution> getPersonalStatisticSubstitution(long activityId,
                                                                        TabUser user) {
        return substituteStatisticsRepository
            .getPersonalStatisticSubstitution(activityId, user.getId());
    }

}
