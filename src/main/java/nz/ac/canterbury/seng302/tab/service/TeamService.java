package nz.ac.canterbury.seng302.tab.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javassist.NotFoundException;
import nz.ac.canterbury.seng302.tab.entity.Club;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.entity.TeamRoles;
import nz.ac.canterbury.seng302.tab.repository.TeamRepository;
import nz.ac.canterbury.seng302.tab.repository.TeamRoleRepository;
import nz.ac.canterbury.seng302.tab.utility.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


/**
 * Service class for TeamResults, defined by the @link{Service} annotation.
 * This class links automatically with @link{TeamRepository},
 * see the @link{Autowired} annotation below
 */
@Service
public class TeamService {
    /**
     * To use the team functionalities with the database
     */
    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TeamRoleRepository teamRoleRepository;

    /**
     * To access the location functionalities with the database
     */
    @Autowired
    private LocationService locationService;
    /**
     * To access the location functionalities with the database
     */
    @Autowired
    private TeamRoleService teamRoleService;


    /**
     * To access the token dump functionalities with the database
     */
    @Autowired
    private TokenDumpService tokenDumpService;

    @Autowired
    private TabUserService tabUserService;

    /**
     * Gets all teams from persistence
     *
     * @return all teams currently saved in persistence
     */
    public List<Team> getTeamResults() {
        return teamRepository.findAll();
    }

    /**
     * Gets a team entity by a given id
     *
     * @param id the id for a specific team
     * @return A team entity which represents a team in the database
     * @throws NotFoundException if team is not found by given id
     */
    public Team getTeamById(long id) throws NotFoundException {
        Optional<Team> teamOptional = teamRepository.findById(id);
        if (teamOptional.isPresent()) {
            return teamOptional.get();
        } else {
            throw new NotFoundException("Team not found");
        }
    }

    /**
     * Saves team to database
     *
     * @param team Entity object representing a team
     * @return the saved entity of team
     */
    public Team createTeam(Team team) {
        return teamRepository.save(team);
    }

    /**
     * Return a list of teams that the user is a manager or coach of
     *
     * @param user the user requesting the information
     * @return a list of Team objects that the user is a manager or coach of
     * @throws NotFoundException throws exception if no team can be found with the gathered id
     */
    public List<Team> getTeamsThatUserManagesOrCoaches(TabUser user) throws NotFoundException {
        List<Long> teamIds = teamRoleRepository.teamsUserManagesOrCoaches(user.getId());
        ArrayList<Team> teams = new ArrayList<>();
        for (Long teamId : teamIds) {
            teams.add(getTeamById(teamId));
        }
        return teams;
    }


    /**
     * Creates a new team entity
     *
     * @param name    Team's name
     * @param sport   Name of sport team plays
     * @param city    Name of the city
     * @param country Name of the country
     * @param image   String representing the file/image
     * @return A team entity which represents a team in the database
     */
    public Team createTeamInDatabase(String name, String sport, String city, String country,
                                     String image) {

        Location location = locationService.createLocationInDatabase(city, country);

        Team team = new Team(name, sport, location, image);

        registerTabUserToTeam(team, tabUserService.getCurrentlyLoggedIn());

        team.setCreationDate(TokenGenerator.getCurrentTime());
        teamRepository.save(team);
        team.addTeamRole(teamRoleService.tabUserCreatesTeam(team,
            tabUserService.getCurrentlyLoggedIn()));
        return teamRepository.save(team);
    }

    /**
     * Adds a user to a current team
     *
     * @param team    Entity representing the team
     * @param tabUser Current User logged in
     * @return Team Entity that the user is registered to
     */
    public Team registerTabUserToTeam(Team team, TabUser tabUser) {
        team.registerTabUser(tabUser);
        Team dbTeam = teamRepository.save(team);
        dbTeam.addTeamRole(teamRoleService.addTeamRole(new TeamRoles(team, tabUser, "Member")));
        return teamRepository.save(dbTeam);
    }

    /**
     * Adds a user to a current team via a team invitation token
     *
     * @param inviteToken A team's invitation token
     * @param tabUser     Current User logged in
     * @return The team the user was added to
     * @throws NotFoundException Throws error if team can not be found by invite token
     */
    public Team registerTabUserToTeam(String inviteToken, TabUser tabUser)
        throws NotFoundException {
        Team team;
        try {

            team = getTeamByToken(inviteToken);

            // Ensures user isn't already a member of this team
            if (teamRoleService.tabUserInTeam(team.getId(), tabUser.getId())) {
                return null;
            } else {
                registerTabUserToTeam(team, tabUser);
                return team;
            }

        } catch (Exception e) {
            throw new NotFoundException("Team not found");
        }
    }

    /**
     * Creates a new team entity, without registering the currently logged-in user to it
     *
     * @param name    Team's name
     * @param sport   Name of sport team plays
     * @param city    Name of the city
     * @param country Name of the country
     * @param image   String representing the file/image
     * @return A team entity which represents a team in the database
     */
    public Team createTeamInDatabaseNoRegister(String name, String sport, String city,
                                               String country, String image) {
        Location location = locationService.createLocationInDatabase(city, country);
        Team team = new Team(name, sport, location, image);
        return teamRepository.save(team);
    }

    /**
     * Updates an existing entity by a given id
     *
     * @param id      String representing the id of the team
     * @param name    Team's name
     * @param sport   Name of sport team plays
     * @param city    Name of the city
     * @param country Name of the country
     * @return A team entity which represents a team in the database
     * @throws NotFoundException Throws error if team can not be found by id
     * @throws IOException       Throws error if problem getting location
     */
    public Team updateTeamInDatabase(String id, String name, String sport, String city,
                                     String country) throws IOException, NotFoundException {
        Team team = getTeamById(Long.parseLong(id));
        team.updateTeam(name, sport);
        locationService.updateMandatoryInDatabase(team.getLocationId(), city, country);
        return teamRepository.save(team);
    }

    /**
     * Updates an existing entity's image by given id
     *
     * @param id    String representing the id of the team
     * @param image String representing the file path
     * @return A team entity which represents a team in the database
     * @throws NotFoundException Throws error if team can not be found by id
     */
    public Team updateProfilePictureInDatabase(String id, String image) throws NotFoundException {
        Team team = getTeamById(Long.parseLong(id));
        team.updateProfilePicture(image);
        return teamRepository.save(team);
    }

    /**
     * Removes a user from a current team
     *
     * @param team    Entity representing the team
     * @param tabUser Current User logged in
     * @return Team Entity that the user has been removed from
     */
    public Team removeTabUserFromTeam(Team team, TabUser tabUser) {
        teamRoleService.removeUserFromTeam(team.getId(), tabUser.getId());
        return teamRepository.save(team);
    }

    /**
     * Search teams within the team database by the given search string
     *
     * @param searchString String to search with
     * @param pageNumber   Page number of the generated results to retrieve from
     * @return results
     */
    public Page<Team> searchTeams(String searchString, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 10, (Sort.by("name")));
        return teamRepository.searchTeams(pageable, searchString);
    }

    /**
     * Search teams within the team database by the given search string and id
     *
     * @param searchString String to search with
     * @param id           id of the target
     * @param pageNumber   Page number of the generated results to retrieve from
     * @return results
     */
    public Page<Team> searchUserTeams(String searchString, long id, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 10);
        return teamRepository.searchTeamsId(pageable, searchString, id);
    }

    /**
     * Deletes all data in team database
     */
    public void deleteAllData() {
        teamRepository.deleteAll();
    }

    /**
     * Changes token
     *
     * @param teamId long representing team id
     * @return string of the newly added token
     */
    public String changeToken(long teamId) {
        Timestamp time = getRegenTime(teamId);
        if (time == null || TokenGenerator.isTokenExpired(time)) {
            String token = tokenDumpService.createToken();
            tokenDumpService.addToken(token);
            teamRepository.setCurrentToken(teamId, token);
            teamRepository.setRegenTime(teamId, TokenGenerator.getExpiryTime10Mins());
            return teamRepository.getCurrentToken(teamId);
        }
        return "Error";
    }

    /**
     * Gets the regeneration time for the selected team
     *
     * @param teamId Long representing the team
     * @return a timestamp representing the time the token was creates plus 10 minutes
     */
    public Timestamp getRegenTime(long teamId) {
        return teamRepository.getRegenTime(teamId);
    }

    /**
     * Resets the regeneration time token to current time for tests
     *
     * @param teamId long representing team to reset the regeneration
     */
    public void resetRegenTime(long teamId) {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        Timestamp currentTimestamp = new Timestamp(currentDate.getTime());
        currentTimestamp.setTime(currentTimestamp.getTime() - 1);
        teamRepository.setRegenTime(teamId, currentTimestamp);
    }

    /**
     * Gets current token of a team
     *
     * @param teamId long representing team
     * @return string representation of token
     */
    public String getCurrentToken(long teamId) {
        return teamRepository.getCurrentToken(teamId);
    }

    /**
     * Retrieves a Team from DB based on the given token string
     * Checks if there are more than one team with the same token
     * Throws exception on detection
     *
     * @param token String token
     * @return Team found team
     * @throws NotFoundException if more than one team or no team found
     */
    public Team getTeamByToken(String token) throws NotFoundException {
        ArrayList<Team> teams = teamRepository.getTeamByToken(token);
        if (teams.size() > 1) {
            //There are teams with the same token. ERROR
            throw new IllegalStateException("More than one team with the same token");
        } else if (teams.isEmpty()) {
            throw new NotFoundException("No team found matching the token");
        } else {
            return teams.get(0);
        }
    }

    /**
     * Adds one to either the win, loss, or draw column depending on the integer given
     *
     * @param teamId  long representing id of the team
     * @param outcome integer representing the outcome of the activity
     * @throws NotFoundException if team does not exist in the database
     */
    public String addToWinLossDrawColumn(long teamId, int outcome) throws NotFoundException {
        Team team = getTeamById(teamId);
        String returnVal = " is updated";
        switch (outcome) {
            // Activity is a win
            case 0 -> {
                team.setWins(team.getWins() + 1);
                returnVal = "Win column" + returnVal;
            }
            // Activity is a draw
            case 1 -> {
                team.setDraws(team.getDraws() + 1);
                returnVal = "Draw column" + returnVal;
            }
            // Activity is a loss
            case 2 -> {
                team.setLosses(team.getLosses() + 1);
                returnVal = "Loss column" + returnVal;
            }
            default -> returnVal = "Not a valid integer";
        }
        save(team);
        return returnVal;
    }

    public Team save(Team team) {
        return teamRepository.save(team);
    }

    /**
     * Get teams that have no clubs by a certain user
     *
     * @param currentlyLoggedIn currently logged-in user
     * @return List of teams with no clubs
     * @throws NotFoundException No clubs found
     */
    public List<Team> getTeamsForClubs(TabUser currentlyLoggedIn)
        throws NotFoundException {
        return getTeamsThatUserManagesOrCoaches(currentlyLoggedIn).stream()
            .filter(team -> team.getClub() == null)
            .toList();
    }

    /**
     * Get teams that have no clubs by a certain user
     * and get the teams that are part of the selected club
     *
     * @param currentlyLoggedIn currently logged-in user
     * @param club              current club
     * @return List of teams with no clubs and teams that are part of the club
     * @throws NotFoundException No clubs found
     */
    public List<Team> getTeamsForEditClub(TabUser currentlyLoggedIn, Club club)
        throws NotFoundException {
        return getTeamsThatUserManagesOrCoaches(currentlyLoggedIn).stream()
            .filter(team -> team.getClub() == club || team.getClub() == null)
            .toList();
    }
}