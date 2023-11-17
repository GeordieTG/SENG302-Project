package nz.ac.canterbury.seng302.tab.service;

import java.util.List;
import nz.ac.canterbury.seng302.tab.entity.Activity;
import nz.ac.canterbury.seng302.tab.entity.GameActivity;
import nz.ac.canterbury.seng302.tab.repository.TeamStatsRepository;
import nz.ac.canterbury.seng302.tab.utility.TokenGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Functionality for everything related to team statistics
 */
@Service
public class TeamStatsService {

    Logger logger = LoggerFactory.getLogger(TeamStatsService.class);

    @Autowired
    private TabUserService tabUserService;

    @Autowired
    private TeamStatsRepository teamStatsRepository;

    public int getTotalGamesPlayed(long id) {
        return teamStatsRepository.getTotalGamesPlayed(id);
    }

    public int getTeamWins(long id) {
        return teamStatsRepository.getTeamWins(id);
    }

    public int getTeamLosses(long id) {
        return teamStatsRepository.getTeamLosses(id);
    }

    public int getTeamDraws(long id) {
        return teamStatsRepository.getTeamDraws(id);
    }

    public int getTeamGameWins(long id) {
        return teamStatsRepository.getTeamGameWins(id);
    }

    public int getTeamGameLosses(long id) {
        return teamStatsRepository.getTeamGameLosses(id);
    }

    public int getTeamGameDraws(long id) {
        return teamStatsRepository.getTeamGameDraws(id);
    }

    public int getTeamFriendlyWins(long id) {
        return teamStatsRepository.getTeamFriendlyWins(id);
    }

    public int getTeamFriendlyLosses(long id) {
        return teamStatsRepository.getTeamFriendlyLosses(id);
    }

    public List<GameActivity> getTeamRecentGames(long id) {
        return teamStatsRepository.getTeamRecentGames(id,
            TokenGenerator.getCurrentTime().toString().replace(" ", "T"));
    }

    public List<GameActivity> getTeamActivities(long id) {
        return teamStatsRepository.getTeamActivities(id);
    }

    public List<Activity> getTeamOtherActivities(long id) {
        return teamStatsRepository.getTeamOtherActivities(id);
    }

    public List<GameActivity> getGamePlayed(long id) {
        return teamStatsRepository.getGamePlayed(id,
            TokenGenerator.getCurrentTime().toString().replace(" ", "T"));
    }
}
