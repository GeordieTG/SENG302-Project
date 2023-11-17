package nz.ac.canterbury.seng302.tab.service;

import java.io.IOException;
import java.io.InvalidClassException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javassist.NotFoundException;
import nz.ac.canterbury.seng302.tab.entity.Activity;
import nz.ac.canterbury.seng302.tab.entity.GameActivity;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.StatisticFact;
import nz.ac.canterbury.seng302.tab.entity.StatisticPlayerScore;
import nz.ac.canterbury.seng302.tab.entity.StatisticSubstitution;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.entity.TeamRoles;
import nz.ac.canterbury.seng302.tab.entity.enums.ActivityResult;
import nz.ac.canterbury.seng302.tab.formobjects.ActivityForm;
import nz.ac.canterbury.seng302.tab.formobjects.FactForm;
import nz.ac.canterbury.seng302.tab.formobjects.PlayerScoreForm;
import nz.ac.canterbury.seng302.tab.formobjects.ScoreForm;
import nz.ac.canterbury.seng302.tab.formobjects.StatisticsForm;
import nz.ac.canterbury.seng302.tab.formobjects.SubstitutionForm;
import nz.ac.canterbury.seng302.tab.repository.ActivityRepository;
import nz.ac.canterbury.seng302.tab.repository.StatisticFactRepository;
import nz.ac.canterbury.seng302.tab.repository.StatisticScoreRepository;
import nz.ac.canterbury.seng302.tab.repository.SubstituteStatisticsRepository;
import nz.ac.canterbury.seng302.tab.utility.ConvertingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Functionality for everything related to Activities
 */
@Service
public class ActivityService {

    public static final String ACTIVITY_NOT_FOUND = "Activity not found!";
    Logger logger = LoggerFactory.getLogger(ActivityService.class);
    @Autowired
    TabUserService tabUserService;
    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private StatisticFactRepository statisticFactRepository;

    @Autowired
    private SubstituteStatisticsRepository substituteStatisticsRepository;

    @Autowired
    private StatisticScoreRepository statisticScoreRepository;
    @Autowired
    private TeamService teamService;
    @Autowired
    private TeamRoleService teamRoleService;
    @Autowired
    private LocationService locationService;

    public List<Activity> getAllActivities() {
        return activityRepository.findAll();
    }

    /**
     * Creates an activity in the database based off of the users input in the
     * "Create an Activity" Form.
     * The form must be validated before this.
     *
     * @param activityForm an object representation of the users inputted fields
     *                     while filling out the create activity form.
     * @return Return the activity that has been created in the database
     * @throws NotFoundException Throws exception if a team cant be identified by the teamId
     *                           passed in, or if currentlyLoggedIn is null
     */
    public Activity createActivityInDatabase(ActivityForm activityForm) throws NotFoundException {
        Team team = null;
        if (activityForm.getTeamId() != -1) {
            team = teamService.getTeamById(activityForm.getTeamId());
        }

        TabUser user = tabUserService.getCurrentlyLoggedIn();
        Location location = locationService.createLocationInDatabase(activityForm.getCity(),
            activityForm.getCountry());

        Activity activity = createCorrectActivity(activityForm, user, team, location);

        locationService.updateOptionalInDatabase(activity.getLocation().getLocationId(),
            activityForm.getAddress1(), activityForm.getAddress2(), activityForm.getSuburb(),
            activityForm.getPostcode());
        return activityRepository.save(activity);
    }

    /**
     * Saves activity to database
     *
     * @param activity entity object representing an activity
     * @return the saved entity
     */
    public Activity createActivity(Activity activity) {
        return activityRepository.save(activity);
    }

    /**
     * Gets the information of an activity based on their id
     *
     * @param id the target activity id
     * @return FormResult the activity information
     */
    public Activity getActivityById(long id) {
        logger.info("Getting activity by id");
        return activityRepository.findById(id);
    }

    /**
     * Updates an existing activity by a given id
     *
     * @return A activity entity which represents a team in the database
     * @throws NotFoundException Throws error if activity can not be found by id
     */
    public Activity updateActivityInDatabase(ActivityForm activityForm) throws NotFoundException,
        IOException {
        Activity activity = getActivityById(Long.parseLong(activityForm.getId()));
        Team team = null;
        if (activityForm.getTeamId() != -1) {
            team = teamService.getTeamById(activityForm.getTeamId());
        }
        locationService.updateOptionalInDatabase(activity.getLocation().getLocationId(),
            activityForm.getAddress1(), activityForm.getAddress2(), activityForm.getSuburb(),
            activityForm.getPostcode());
        locationService.updateMandatoryInDatabase(activity.getLocation().getLocationId(),
            activityForm.getCity(), activityForm.getCountry());
        Location location =
            locationService.getLocationByLocationId(activity.getLocation().getLocationId());
        activity.updateActivity(team, activityForm.getType(), activityForm.getStartTime(),
            activityForm.getEndTime(), activityForm.getDescription(), location);
        if (activity.getType().equals("Game") || activity.getType().equals("Friendly")) {
            GameActivity gameActivity = new GameActivity(activity);
            activityRepository.delete(activity);
            return activityRepository.save(gameActivity);
        }
        return activityRepository.save(activity);
    }

    /**
     * Gets all the teams activities, ordered by start time
     *
     * @param id id of the team
     * @return the team's activities
     */
    public List<Activity> searchTeamActivity(long id) {
        return activityRepository.getTeamActivityById(id);
    }

    /**
     * Gets all the user's activities, ordered by start time
     *
     * @param id id of the user
     * @return the user's activities
     */
    public List<Activity> searchPersonalActivity(long id) {
        return activityRepository.getPersonalActivityByUserId(id);
    }

    /**
     * Creates personal activity objects for the calendar to display
     *
     * @param personalActivities a list of entity objects representing activities
     * @return a list of a list of strings used to represent activity data for the calendar
     */
    public List<List<String>> createPersonalEvents(List<Activity> personalActivities) {

        List<List<String>> personalEvents = new ArrayList<>();

        for (Activity activity : personalActivities) {

            List<String> activityInfo = new ArrayList<>();
            activityInfo.add("true");
            activityInfo.add(activity.getType());
            activityInfo.add(activity.getStartTime());
            activityInfo.add(activity.getEndTime());
            activityInfo.add(String.valueOf(activity.getId()));
            activityInfo.add("Personal");
            activityInfo.add(activity.getLocation().toString());
            activityInfo.add(ConvertingUtil.convertDateToTime(activity.getStartTime()));
            personalEvents.add(activityInfo);
        }

        return personalEvents;
    }

    /**
     * Creates activity objects for individual teams for the calendar to display
     *
     * @param teamIds a list of longs representing team ids
     * @param user    an entity object representing the current user
     * @return a list of a list of strings used to represent activity data for the calendar
     */
    public List<List<List<String>>> createTeamEventsFromTeamIds(List<Long> teamIds, TabUser user) {

        List<List<List<String>>> teamEvents = new ArrayList<>();

        for (long teamId : teamIds) {
            List<Activity> teamActivity = searchTeamActivity(teamId);
            if (!teamActivity.isEmpty()) {
                teamEvents.add(createTeamEventsFromActivities(teamActivity, teamId, user));
            }
        }
        return teamEvents;
    }

    /**
     * Creates team activity objects for the calendar to display
     *
     * @param teamActivity a list of all the activities for a team
     * @param teamId       a long representing a teams id
     * @param user         the
     * @return a list of a list of strings used to represent activity data for the calendar
     */
    public List<List<String>> createTeamEventsFromActivities(List<Activity> teamActivity,
                                                             Long teamId, TabUser user) {

        List<List<String>> teamEvents = new ArrayList<>();

        for (Activity activity : teamActivity) {

            List<String> activityInfo = new ArrayList<>();
            activityInfo.add(teamRoleService.tabUserManagerOrCoachOfTeam(teamId, user.getId())
                ? "true" : "false");
            activityInfo.add(activity.getType());
            activityInfo.add(activity.getStartTime());
            activityInfo.add(activity.getEndTime());
            activityInfo.add(String.valueOf(activity.getId()));
            activityInfo.add(activity.getTeam().getName());
            activityInfo.add(activity.getLocation().toString());
            activityInfo.add(ConvertingUtil.convertDateToTime(activity.getStartTime()));
            try {
                activityInfo.add(teamService.getTeamById(teamId).getSport());
            } catch (NotFoundException e) {
                logger.error("Team not found");
            }
            teamEvents.add(activityInfo);
        }

        return teamEvents;
    }


    /**
     * Method for adding/updating formation positions to an activity
     *
     * @param id       activity Id
     * @param position activity position
     */
    public Activity updateActivityPositionById(long id, String position, Long formationId) {
        Activity activity = getActivityById(id);
        activity.setPosition(position);
        activity.setFormation(formationId);
        return activityRepository.save(activity);
    }

    /**
     * Adds a list of Fact statistics to an activity and saves it to the database,
     * based off the information given in the Fact Forms
     *
     * @param factForms a list of forms representing facts of an activity
     */
    public void updateFactStatistics(List<FactForm> factForms) {

        Activity activity = getActivityById(factForms.get(0).getActivityId());
        List<StatisticFact> oldStatisticFacts = activity.getFactStatistics();
        List<StatisticFact> newStatisticFacts = new ArrayList<>();
        List<Long> goodIds = new ArrayList<>();

        int count = 0;
        for (FactForm factForm : factForms) {
            if (count < oldStatisticFacts.size()) {
                long id = oldStatisticFacts.get(count).getId();
                StatisticFact existingStatisticFact = statisticFactRepository.findById(id);
                existingStatisticFact.setDescription(factForm.getDescription());
                existingStatisticFact.setTimeOccurred(factForm.getTimeOccurred());
                goodIds.add(id);
                newStatisticFacts.add(existingStatisticFact);
            } else {
                newStatisticFacts.add(new StatisticFact(activity, factForm.getDescription(),
                    factForm.getTimeOccurred()));
            }
            count += 1;
        }
        activityRepository.deleteStatFactsByIdNotIn(activity.getId(), goodIds);
        activity.setFactStatistics(newStatisticFacts);
        activityRepository.save(activity);
    }

    /**
     * Adds a fact with no time occurred to an activity, then saves it in the repository
     *
     * @param activity      the activity the fact will be added to
     * @param statisticFact the fact statistic that will be added
     * @return the activity with the fact added to it
     */
    public Activity addFactStatistic(Activity activity, StatisticFact statisticFact) {
        activity.addFactStatistic(statisticFact);
        return activityRepository.save(activity);
    }

    /**
     * Creates the corresponding activity entity based on the users input
     *
     * @param activityForm an object representation of the users inputted fields
     *                     while filling out the create activity form.
     * @param user         entity object of the current user
     * @param team         entity object of the team corresponding to the activity
     * @param location     the location of the activity as an entity object
     * @return an entity object representing the activity
     */
    public Activity createCorrectActivity(ActivityForm activityForm, TabUser user, Team team,
                                          Location location) {
        Activity activity;
        if (!Objects.equals(activityForm.getType(), "Game")
            && !Objects.equals(activityForm.getType(), "Friendly")) {
            activity =
                new Activity(user, team, activityForm.getType(), activityForm.getStartTime(),
                    activityForm.getEndTime(), activityForm.getDescription(), location);
        } else {
            activity = new GameActivity(user, team, activityForm.getType(),
                activityForm.getStartTime(), activityForm.getEndTime(),
                activityForm.getDescription(), location);
        }
        return activity;
    }

    /**
     * Adds the different types of activity statistics from a given StatisticsForm,
     * given that each statistic is not empty
     *
     * @param statisticsForm The form which contains the forms of the statistics that will be added
     */
    public void addStatisticsForm(StatisticsForm statisticsForm) {
        Activity activity = getActivityById(statisticsForm.getActivityId());
        GameActivity gameActivity = (GameActivity) activity;
        createPlayerScoreStatisticList(gameActivity, statisticsForm.getPlayerScoreForms());
        createSubstitutionStatisticList(gameActivity, statisticsForm.getSubstitutionForms(),
            statisticsForm.isFirstTime());

        ScoreForm activityScoreForm = statisticsForm.getScoreForm();
        if (activityScoreForm != null) {
            try {
                activity = setActivityScore(activityScoreForm);
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
        }
        statisticsForm.setFirstTime(false);
        activityRepository.save(activity);
    }

    /**
     * Creates a player score statistic list, and adds it to an activity
     *
     * @param gameActivity     The activity the score statistic list will be added
     *                         to.
     * @param playerScoreForms the list of player score forms to be added
     * @return The gameActivity with the player score statistics added
     */
    public GameActivity createPlayerScoreStatisticList(GameActivity gameActivity,
                                                       List<PlayerScoreForm> playerScoreForms) {

        List<StatisticPlayerScore> newScoreStatistics = new ArrayList<>();
        List<StatisticPlayerScore> oldScoreStatistics =
            gameActivity.getPlayerScoreStatistics();
        List<PlayerScoreForm> oldForms = getPlayerScoreForms(oldScoreStatistics);
        List<Long> goodIds = new ArrayList<>();

        for (PlayerScoreForm oldForm : oldForms) {
            teamRoleService.minusTotalPoints(gameActivity, oldForm);
        }

        int count = 0;
        for (PlayerScoreForm playerScoreForm : playerScoreForms) {
            teamRoleService.addTotalPoints(gameActivity, playerScoreForm);
            TabUser scoredUser = tabUserService.getById(playerScoreForm.getScoredPlayerId());
            if (count < oldScoreStatistics.size()) {
                long id = oldScoreStatistics.get(count).getId();
                StatisticPlayerScore existingStatisticFact = statisticScoreRepository.findById(id);
                existingStatisticFact.setScoredTime(playerScoreForm.getScoreTime());
                existingStatisticFact.setScore(playerScoreForm.getScore());
                goodIds.add(existingStatisticFact.getId());
                newScoreStatistics.add(existingStatisticFact);
            } else {
                newScoreStatistics.add(new StatisticPlayerScore(gameActivity,
                    scoredUser,
                    playerScoreForm.getScore(),
                    playerScoreForm.getScoreTime()));
            }
            count += 1;
        }
        activityRepository.deleteStatScoreByIdNotIn(gameActivity.getId(), goodIds);
        gameActivity.setPlayerScoreStatistics(newScoreStatistics);
        return activityRepository.save(gameActivity);
    }

    /**
     * Creates a substitution Statistic list as one and adds to database
     *
     * @param gameActivity      Entity Object representing a game activity
     * @param substitutionForms List of substitution form that represent data on substitutions
     * @return the updated entity
     */
    public GameActivity createSubstitutionStatisticList(GameActivity gameActivity,
                                                        List<SubstitutionForm> substitutionForms,
                                                        boolean firstTime) {
        List<StatisticSubstitution> statisticSubstitutions = new ArrayList<>();
        List<StatisticSubstitution> oldStatsSubs = gameActivity.getSubstitutionStatistics();
        List<SubstitutionForm> oldStats = getSubstitutionForms(oldStatsSubs);
        List<Long> goodIds = new ArrayList<>();
        teamRoleService.addTotalTimePlayed(gameActivity, oldStats, true, firstTime);
        int count = 0;
        for (SubstitutionForm substitutionForm : substitutionForms) {
            TabUser substitutedPlayer = tabUserService.getById(
                    substitutionForm.getSubstitutedPlayerId());
            TabUser substitutePlayer = tabUserService.getById(
                    substitutionForm.getSubstitutePlayerId());
            if (count < oldStatsSubs.size()) {
                long id = oldStatsSubs.get(count).getId();
                StatisticSubstitution existing = substituteStatisticsRepository.findById(id);
                existing.setSubstitutedPlayer(substitutedPlayer);
                existing.setSubstitutePlayer(substitutePlayer);
                goodIds.add(existing.getId());
                existing.setSubstituteTime(substitutionForm.getSubstituteTime());
                statisticSubstitutions.add(existing);
            } else {
                statisticSubstitutions.add(new StatisticSubstitution(gameActivity,
                        substitutedPlayer,
                        substitutePlayer,
                        substitutionForm.getSubstituteTime()));
            }
            count++;
        }
        teamRoleService.addTotalTimePlayed(gameActivity,
            getSubstitutionForms(statisticSubstitutions), false, false);

        activityRepository.deleteStatSubByIdNoIn(gameActivity.getId(), goodIds);
        gameActivity.addSubstitutionStatistic(statisticSubstitutions);
        return activityRepository.save(gameActivity);
    }

    /**
     * Checks if an activity start time is before the current time
     *
     * @param activity activity that needs to be checked
     * @return Boolean, if the current time is after the activity start time return true else false
     */
    public Boolean checkActivityStartTime(Activity activity) {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        Timestamp currentTimestamp = new Timestamp(currentDate.getTime());
        String[] activityDateTime = activity.getStartTime().split("T");
        int isCurrentDateAfterActivity = currentTimestamp.toString()
            .compareTo(activityDateTime[0] + " " + activityDateTime[1] + ":00.000");
        return isCurrentDateAfterActivity > 0;
    }

    /**
     * Update the home score
     *
     * @param gameActivityId id of the game activity
     * @param homeScore      score that wants to be changed
     * @return updated game activity
     */
    public GameActivity updateHomeScore(long gameActivityId, String homeScore)
        throws NotFoundException, InvalidClassException {
        GameActivity gameActivity = (GameActivity) activityRepository.findById(gameActivityId);
        if (gameActivity == null) {
            throw new NotFoundException("Game Activity not found!");
        }
        if (homeScore == null) {
            throw new InvalidClassException("Score is invalid");
        }
        gameActivity.setHomeScore(homeScore);
        return activityRepository.save(gameActivity);
    }

    /**
     * Update the opposition score
     *
     * @param gameActivityId  id of the game activity
     * @param oppositionScore score that wants to be changed
     * @return updated game activity
     */
    public GameActivity updateOppositionScore(long gameActivityId, String oppositionScore)
        throws NotFoundException, InvalidClassException {
        GameActivity gameActivity = (GameActivity) activityRepository.findById(gameActivityId);
        if (gameActivity == null) {
            throw new NotFoundException("Game Activity not found!");
        }
        if (oppositionScore == null) {
            throw new InvalidClassException("Score is invalid");
        }
        gameActivity.setOppositionScore(oppositionScore);
        return activityRepository.save(gameActivity);
    }

    /**
     * Sets the score of an activity via a score form object
     *
     * @param scoreForm score form
     * @throws NotFoundException if score cannot be set
     */
    public Activity setActivityScore(ScoreForm scoreForm) throws NotFoundException {
        Activity activity = getActivityById(scoreForm.getActivityId());
        if (activity == null) {
            throw new NotFoundException(ACTIVITY_NOT_FOUND);
        }
        if (scoreForm.getOppositionScore() == null) {
            ((GameActivity) activity).setHomeScore(scoreForm.getHomeScore());
        } else {
            ((GameActivity) activity).setHomeScore(scoreForm.getHomeScore());
            ((GameActivity) activity).setOppositionScore(scoreForm.getOppositionScore());
            ((GameActivity) activity).setOutcome(scoreForm.getActivityResult());
        }
        return activity;
    }

    /**
     * Gets the statistics associated with the activity associated with the id
     *
     * @param id The id of the activity we want to get the statistics from
     * @return the statisticsForm which contains the statistics of the activity
     */
    public StatisticsForm getStatistics(long id) {
        StatisticsForm statisticsForm = new StatisticsForm(id);
        GameActivity activity = (GameActivity) getActivityById(id);
        List<StatisticPlayerScore> statisticPlayerScores = activity.getPlayerScoreStatistics();
        statisticPlayerScores.sort(Comparator.comparingLong(StatisticPlayerScore::getScoredTime));
        List<PlayerScoreForm> playerScoreForms = getPlayerScoreForms(statisticPlayerScores);
        statisticsForm.setPlayerScoreForms(playerScoreForms);
        List<StatisticSubstitution> statisticSubstitutions = activity.getSubstitutionStatistics();
        statisticSubstitutions.sort(Comparator.comparing(StatisticSubstitution::getSubstituteTime));
        List<SubstitutionForm> substitutionForms = getSubstitutionForms(statisticSubstitutions);
        statisticsForm.setSubstitutionForms(substitutionForms);
        ScoreForm scoreForm = new ScoreForm(activity.getHomeScore(),
            activity.getId(),
            activity.getOppositionScore(),
            activity.getOutcome());
        statisticsForm.setScoreForm(scoreForm);
        if (activity.getOutcome() != ActivityResult.UNDECIDED) {
            statisticsForm.setFirstTime(false);
        }
        return statisticsForm;
    }

    /**
     * Extracts activity facts from Generic Java Objects and converts them to strings for display
     *
     * @param activityId the id of the activity
     * @return a list of strings representing an activities fact
     */
    public List<String> getActivityFacts(long activityId) {
        List<Object> allFacts = activityRepository.getActivityFacts(activityId);
        List<String> facts = new ArrayList<>();
        for (Object obj : allFacts) {
            Object[] hold = (Object[]) obj;
            if (Objects.isNull(hold[2])) {
                facts.add(hold[1] + "");
            } else {
                facts.add(hold[1] + " at " + hold[2] + "'");
            }
        }
        return facts;
    }

    private List<PlayerScoreForm> getPlayerScoreForms(
        List<StatisticPlayerScore> statisticPlayerScores) {
        List<PlayerScoreForm> playerScoreForms = new ArrayList<>();
        for (StatisticPlayerScore statisticPlayerScore : statisticPlayerScores) {
            PlayerScoreForm playerScoreForm = new PlayerScoreForm(
                statisticPlayerScore.getGameActivity().getId(),
                statisticPlayerScore.getUser().getId(),
                statisticPlayerScore.getScore(),
                statisticPlayerScore.getScoredTime());
            playerScoreForms.add(playerScoreForm);
        }
        return playerScoreForms;
    }

    public GameActivity getGameActivity(long activityId) {
        return activityRepository.getGameActivityById(activityId);
    }

    public Activity save(Activity activity) {
        return activityRepository.save(activity);
    }

    private List<SubstitutionForm> getSubstitutionForms(
        List<StatisticSubstitution> statisticSubstitutions) {
        List<SubstitutionForm> substitutionForms = new ArrayList<>();
        for (StatisticSubstitution statisticSubstitution : statisticSubstitutions) {
            SubstitutionForm substitutionForm = new SubstitutionForm(
                statisticSubstitution.getGameActivity().getId(),
                statisticSubstitution.getSubstitutedPlayer().getId(),
                statisticSubstitution.getSubstitutePlayer().getId(),
                statisticSubstitution.getSubstituteTime());
            substitutionForms.add(substitutionForm);
        }
        return substitutionForms;
    }

    public int getTotalGamesTeamPlayed(long teamId) {
        return activityRepository.getTotalGamesTeamPlayed(teamId);
    }

    /**
     * Gets a list of users that are only in the lineup of the game
     * @param team an entity representation of a team
     * @param activity an enetity representation of an activity
     * @return a list of users that are in the lineup sorted by first name
     */
    public List<TabUser> getPlayers(Team team, Activity activity) {
        String position = activity.getPosition();

        Set<TabUser> players = team.getTeamRoles().stream()
                .map(TeamRoles::getUser).collect(Collectors.toSet());

        return Arrays.stream(position.split("-"))
                .flatMap(group -> Arrays.stream(group.split(",")))
                .map(Long::parseLong)
                .flatMap(userId -> players.stream()
                        .filter(player -> player.getId().equals(userId)))
                .sorted(Comparator.comparing(TabUser::getFirstName)).toList();
    }

    public int getTotalGamesTeamPlayedWithStatistics(long activityId) {
        return activityRepository.getTotalGamesTeamPlayedWithStatistics(activityId);
    }
}
