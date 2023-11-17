package nz.ac.canterbury.seng302.tab.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javassist.NotFoundException;
import nz.ac.canterbury.seng302.tab.entity.Club;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.entity.datatransferobjects.ClubDto;
import nz.ac.canterbury.seng302.tab.repository.ClubRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * Club service class to deal with the club data
 */
@Service
public class ClubService {

    Logger logger = LoggerFactory.getLogger(ClubService.class);

    @Autowired
    TabUserService tabUserService;

    @Autowired
    ClubRepository clubRepository;

    @Autowired
    TeamService teamService;


    /**
     * Get all the club from the database
     *
     * @return list of all the clubs
     */
    public List<Club> getAllClub() {
        return clubRepository.findAll();
    }

    /**
     * Save the club entity to the database
     *
     * @param club the club that save to the database
     * @return the club entity which includes the club id
     */
    public Club saveClub(Club club) {
        return clubRepository.save(club);
    }

    /**
     * Get the club by its id
     *
     * @param id the club id
     * @return return the club which id is matched
     */
    public Club getById(long id) {
        return clubRepository.findById(id);
    }

    /**
     * Search the club with a specific string which could match the name of the club or the locaiton
     *
     * @param searchString a string to match the club name and location
     * @param pageNumber   the size of the pagination
     * @return paged club which match the search string
     */
    public Page<Club> searchClubs(String searchString, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 10, (Sort.by("name")));
        return clubRepository.searchClubs(pageable, searchString);
    }

    /**
     * takes a ClubDto and turns it into a Club entity to save into the DB
     *
     * @param clubDto The club represented as a Dto
     * @param user    The owner of the club
     * @return The newly created club entity
     * @throws Exception Thrown if a team doesn't exist
     */
    public Club saveClubDto(ClubDto clubDto, TabUser user) throws NotFoundException {
        Set<Team> teamList = new HashSet<>();
        for (long id : clubDto.getTeamIds()) {
            Team team = teamService.getTeamById(id);
            teamList.add(team);
        }
        Club club = clubDto.getClub();

        club.setOwner(user);
        club.setImage(clubDto.getClub().getImage());
        Club savedClub = saveClub(club);
        savedClub.setTeams(teamList);

        return saveClub(savedClub);
    }

    /**
     * Update the teams that a part of the club
     *
     * @param club    Club to be updated
     * @param teamIds ID of the teams to be a part of the club
     */
    public void updateClubTeams(Club club, List<Long> teamIds) {
        Set<Team> teams = new HashSet<>();
        (teamIds).forEach(teamId -> {
            try {
                Team team = teamService.getTeamById(teamId);
                teams.add(team);
            } catch (NotFoundException e) {
                logger.error(e.getMessage());
            }
        });

        club.setTeams(teams);
        saveClub(club);
    }
}
