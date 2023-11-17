package nz.ac.canterbury.seng302.tab.entity.datatransferobjects;

import java.util.ArrayList;
import java.util.List;
import nz.ac.canterbury.seng302.tab.entity.Club;


/**
 * Data Transfer Object to pass Java Objects through to JavaScript.
 * Created to pass Club Objects through rest requests.
 */
public class ClubDto {
    /**
     * A mapping containing as much of the club information as possible
     * name, Sport, Location
     */
    Club club;

    /**
     * A list of teamIds each representing a team belonging to the club
     */
    List<Long> teamIds = new ArrayList<>();

    public ClubDto(Club club) {
        this.club = club;
    }

    public ClubDto(Club club, List<Long> teamIds) {
        this.club = club;
        this.teamIds = teamIds;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public List<Long> getTeamIds() {
        return teamIds;
    }

    public void setTeamIds(List<Long> teamIds) {
        this.teamIds = teamIds;
    }


    @Override
    public String toString() {
        return club.toString() + " " + teamIds.toString();
    }

}
