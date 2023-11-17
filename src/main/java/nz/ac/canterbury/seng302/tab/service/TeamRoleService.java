package nz.ac.canterbury.seng302.tab.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import nz.ac.canterbury.seng302.tab.entity.Activity;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.entity.TeamRoles;
import nz.ac.canterbury.seng302.tab.formobjects.PlayerScoreForm;
import nz.ac.canterbury.seng302.tab.formobjects.SubstitutionForm;
import nz.ac.canterbury.seng302.tab.repository.TeamRoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Functionality for everything related to team roles
 */
@Service
public class TeamRoleService {
    Logger logger = LoggerFactory.getLogger(TeamRoleService.class);
    @Autowired
    private TeamRoleRepository teamRoleRepository;

    /**
     * Add TeamId, userId, and Role to db
     *
     * @param teamRoles TeamId, userId, and Role
     * @return added teamRoles
     */
    public TeamRoles addTeamRole(TeamRoles teamRoles) {
        return teamRoleRepository.save(teamRoles);
    }

    /**
     * Update TeamId, userId, and Role to db
     *
     * @param teamRoles TeamId, userId, and Role
     * @return updated teamRoles
     */
    public TeamRoles updateTeamRole(TeamRoles teamRoles) {
        return teamRoleRepository.save(teamRoles);
    }

    /**
     * Find all teams and roles the user is enrolled with
     *
     * @param userId Long user's ID
     * @return List of all teams (team_id) the user is in and the role
     */
    public List<TeamRoles> getTeamsAndRoles(Long userId) {
        List<TeamRoles> results = teamRoleRepository.findAllMemberTeamsAndRoles(userId);
        if (results == null) {
            //If the query returns null
            results = new ArrayList<>();
        }
        return results;
    }

    /**
     * Find all members and their roles within a given team
     *
     * @param teamId Long team's ID
     * @return List of all members (user_id) and their role
     */
    public List<TeamRoles> getUsersAndRoles(Long teamId) {
        List<TeamRoles> results = teamRoleRepository.findAllTeamsUsersAndRoles(teamId);
        if (results == null) {
            //if query returns null
            results = new ArrayList<>();
        }
        return results;
    }

    /**
     * Find the user's role within a given team
     *
     * @param teamId Long team's Id
     * @param userId Long user's Id
     * @return String role i.e. "manager"
     */
    public String getUserRole(Long teamId, Long userId) {
        String result = teamRoleRepository.getUserRole(teamId, userId);
        if (result == null) {
            result = "";
        }
        return result;
    }

    /**
     * Remove the user's role within a given team
     *
     * @param teamId Long team's Id
     * @param userId Long user's Id
     */
    public void removeUserFromTeam(Long teamId, Long userId) {
        teamRoleRepository.deleteRoleBy(teamId, userId);
    }

    /**
     * Adds a user to a current team
     *
     * @param team    Entity representing the team
     * @param tabUser Current User logged in
     * @return Team Entity that the user is registered to
     */
    public TeamRoles tabUserCreatesTeam(Team team, TabUser tabUser) {
        TeamRoles teamRoles = new TeamRoles(team, tabUser, "Manager");
        return addTeamRole(teamRoles);
    }

    /**
     * Adds the given user to the given team
     *
     * @param team Team
     * @param user TabUser
     * @return TeamRoles: what the given user is registered as
     */
    public TeamRoles tabUserJoinsTeam(Team team, TabUser user) {
        TeamRoles teamRoles = new TeamRoles(team, user);
        return addTeamRole(teamRoles);
    }

    /**
     * Confirms if the given user is in a given team
     *
     * @param teamId Long team_id
     * @param userId Long user_id
     * @return true if in team, else false
     */
    public boolean tabUserInTeam(long teamId, long userId) {
        List<TeamRoles> teamRoles = getUsersAndRoles(teamId);
        for (TeamRoles teamRole : teamRoles) {
            if (teamRole.getUser().getId() == userId) {
                return true;
            }
        }
        return false;
    }

    /**
     * Confirms whether the given user is either a manager or a coach for the given team
     *
     * @param teamId Long team_id
     * @param userId Long user_id
     * @return true if manager or coach, else false
     */
    public boolean tabUserManagerOrCoachOfTeam(long teamId, long userId) {
        List<TeamRoles> teamRoles = getUsersAndRoles(teamId);
        for (TeamRoles teamRole : teamRoles) {
            if (teamRole.getUser().getId() == userId
                && !Objects.equals(teamRole.getRole(), "Member")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Confirms whether a given user is a manager of a given team
     *
     * @param teamId Long team_id
     * @param userId Long user_id
     * @return true if manager, else false
     */
    public boolean tabUserManagerOfTeam(long teamId, long userId) {
        List<TeamRoles> teamRoles = getUsersAndRoles(teamId);
        for (TeamRoles teamRole : teamRoles) {
            if (teamRole.getUser().getId() == userId
                && Objects.equals(teamRole.getRole(), "Manager")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds points to the team roles entity
     *
     * @param teamId id of the team that a user belongs too
     * @param userId id of the user that a team has
     * @param points integer representing the number of points to add to the
     *               users total points at a team
     * @return the new total points of the user's total points at a team
     */
    public int updateTotalPoints(long teamId, long userId, int points) {
        TeamRoles teamRoles = teamRoleRepository.getTeamRoles(teamId, userId);
        teamRoles.setTotalPoints(teamRoles.getTotalPoints() + points);
        return teamRoleRepository.save(teamRoles).getTotalPoints();
    }

    /**
     * Gets the team role of a user within a team
     * @param teamId the id of the team the user is a part of
     * @param userId the given user in question
     * @return the team roles
     */
    public TeamRoles getTeamRoles(Long teamId, Long userId) {
        return teamRoleRepository.getTeamRoles(teamId, userId);
    }

    /**
     * Adds time to the team roles entityh
     *
     * @param teamId id of the team that a user belongs too
     * @param userId id of the user that a team has
     * @param time   integer representing the number of minutes a user has played at this team
     * @return the new total time of the user's total time played at a team
     */
    public int updateTotalTime(long teamId, long userId, int time) {
        TeamRoles teamRoles = teamRoleRepository.getTeamRoles(teamId, userId);
        teamRoles.setTotalTime(teamRoles.getTotalTime() + time);
        return teamRoles.getTotalTime();
    }

    /**
     * Used to delete all data within the team role table
     */
    public void deleteAllData() {
        teamRoleRepository.deleteAll();
    }

    /**
     * Add total points to a team roles
     *
     * @param activity        activity that's having its points added
     * @param playerScoreForm player score form containing all scorers
     */
    public void addTotalPoints(Activity activity, PlayerScoreForm playerScoreForm) {

        teamRoleRepository.updateUserPoints(activity.getTeam().getId(),
            playerScoreForm.getScoredPlayerId(),
            playerScoreForm.getScore());
    }

    /**
     * subtract total points to a team roles
     *
     * @param activity        activity that's having its points added
     * @param playerScoreForm player score form containing all scorers
     */
    public void minusTotalPoints(Activity activity, PlayerScoreForm playerScoreForm) {
        teamRoleRepository.updateUserPoints(activity.getTeam().getId(),
            playerScoreForm.getScoredPlayerId(),
            -playerScoreForm.getScore());
    }

    /**
     * Adds a total time played to a user
     *
     * @param activity          Entity Object relating to the current activity
     * @param substitutionForms List of substitution forms that represent substitution events
     * @param minus             boolean representing if the substitutions need to be
     *                          subtracted to reset database
     */
    public void addTotalTimePlayed(Activity activity, List<SubstitutionForm> substitutionForms,
                                   boolean minus,
                                   boolean firstTime) {

        long activityTime = getActivityDuration(activity.getStartTime(), activity.getEndTime());

        // Extract list of playerIds in the starting line up
        String lineup = activity.getPosition();
        String[] rows = lineup.split("-");
        String substitutions = rows[rows.length - 1];
        String[] substitution = substitutions.split(",");

        HashMap<String, Integer> startingLineup = new HashMap<>();

        String[] starters = Arrays.copyOfRange(rows, 0, rows.length - 1);
        updateGamesPlayed(activity, firstTime, substitution, startingLineup, starters);
        // Subtract time for when a player wasn't on the field
        sortSubstitutionEvents(substitutionForms);

        for (SubstitutionForm substitutionForm : substitutionForms) {
            updatePlayerTime(activity, substitutionForms, minus,
                activityTime, startingLineup, substitutionForm);
        }
        if (!firstTime) {
            for (Map.Entry<String, Integer> entry : startingLineup.entrySet()) {
                String key = entry.getKey();
                Integer value = entry.getValue();
                if (minus) {
                    activityTime *= -1;
                }
                if (value == 0) {
                    teamRoleRepository.updateUserTimePlayed(activity.getTeam().getId(), key,
                        activityTime);
                }
            }
        }
    }

    /**
     * Updates the games played for players based on the given activity and related information.
     *
     * @param activity       The activity for which games played are being updated.
     * @param firstTime      Indicates whether this is the first time updating for the
     *                       given activity.
     * @param substitution   An array of substituted player IDs.
     * @param startingLineup A map containing player IDs and their games played count.
     * @param starters       An array of player IDs representing the starting lineup.
     */
    private void updateGamesPlayed(Activity activity, boolean firstTime, String[] substitution,
                                   HashMap<String, Integer> startingLineup, String[] starters) {
        for (String row : starters) {
            String[] columns = row.split(",");
            for (String starter : columns) {
                startingLineup.put(starter, 0);
                if (firstTime) {
                    teamRoleRepository.updateUserGamesPlayed(activity.getTeam().getId(), starter);
                }
            }
        }

        if (firstTime) {
            for (String sub : substitution) {
                if (!Objects.equals(sub, " ")) {
                    teamRoleRepository.updateUserGamesPlayed(activity.getTeam().getId(), sub);
                }
            }
        }
    }

    public void updateUsersGamesPlayed(Long teamId, Long userId) {
        teamRoleRepository.updateUserGamesPlayed(teamId, userId.toString());
    }

    /**
     * Updates player time based on the given activity, substitution forms, and related information.
     *
     * @param activity          The activity for which player time is being updated.
     * @param substitutionForms A list of substitution forms containing player substitution data.
     * @param minus             Indicates whether time calculations should be subtracted.
     * @param activityTime      The total time of the activity.
     * @param startingLineup    A map containing player IDs and their time status.
     * @param substitutionForm  The substitution form being processed.
     */
    private void updatePlayerTime(Activity activity, List<SubstitutionForm> substitutionForms,
                                  boolean minus, long activityTime,
                                  HashMap<String, Integer> startingLineup,
                                  SubstitutionForm substitutionForm) {
        if (startingLineup.containsKey(substitutionForm.getSubstitutedPlayerId().toString())) {
            startingLineup.put(substitutionForm.getSubstitutedPlayerId().toString(), 1);
        }
        long timeLeft = activityTime - substitutionForm.getSubstituteTime();
        long previousTime = getPreviousEvent(substitutionForms, substitutionForm);
        long timeSpentOn = substitutionForm.getSubstituteTime() - previousTime;
        if (minus) {
            timeLeft *= -1;
            timeSpentOn *= -1;
        }
        teamRoleRepository.updateUserTimePlayed(activity.getTeam().getId(),
            substitutionForm.getSubstitutePlayerId().toString(),
            timeLeft);
        if (previousTime == 0) {
            teamRoleRepository.updateUserTimePlayed(activity.getTeam().getId(),
                substitutionForm.getSubstitutedPlayerId().toString(),
                timeSpentOn);
        } else {
            teamRoleRepository.updateUserTimePlayed(activity.getTeam().getId(),
                substitutionForm.getSubstitutedPlayerId().toString(),
                -timeLeft);
        }
    }

    /**
     * Given a list of forms it finds the previous sub event given the previous substitute id
     * is the same as the current substitutedId
     *
     * @param substitutionForms List of substitution forms to loop through
     * @param substitutionForm  The current substitution form we are comparing
     * @return a long representing the time of the previous event
     */
    private long getPreviousEvent(List<SubstitutionForm> substitutionForms,
                                  SubstitutionForm substitutionForm) {
        long previous = 0;
        for (SubstitutionForm substitutionForm1 : substitutionForms) {
            if (substitutionForm1.getSubstituteTime() < substitutionForm.getSubstituteTime()
                && Objects.equals(substitutionForm1.getSubstitutePlayerId(),
                substitutionForm.getSubstitutedPlayerId())) {
                previous = substitutionForm1.getSubstituteTime();
            }
        }
        return previous;
    }

    public List<TeamRoles> getTopFivePlaytimeForTeam(Long teamId) {
        return teamRoleRepository.getTopFivePlayTimeInTeam(teamId);
    }

    /**
     * Used to retrieve the top five scorers of a team along with how many goals they have scored
     *
     * @param id team id
     * @return A list of object such as {[userObject, goalsScored], [userObject, goalsScored]...}
     */
    public List<TeamRoles> getTeamTop5Scorers(long id) {
        return teamRoleRepository.getTeamTop5Scorers(id);
    }

    public void sortSubstitutionEvents(List<SubstitutionForm> substitutionForms) {
        substitutionForms.sort(Comparator.comparingInt(SubstitutionForm::getSubstituteTime));
    }

    /**
     * get activity duration from formatted start and end time
     *
     * @param start start time
     * @param end   end time
     * @return total time in minutes
     */
    public long getActivityDuration(String start, String end) {
        // Create DateTimeFormatter for the specified format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        // Parse the strings to LocalDateTime objects
        LocalDateTime startTime = LocalDateTime.parse(start, formatter);
        LocalDateTime endTime = LocalDateTime.parse(end, formatter);

        // Calculate the duration between the two LocalDateTime objects
        Duration duration = Duration.between(startTime, endTime);

        // Get the time difference in minutes
        return duration.toMinutes();
    }

}
