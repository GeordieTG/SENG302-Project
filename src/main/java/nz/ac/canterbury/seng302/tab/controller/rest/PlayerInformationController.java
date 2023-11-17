package nz.ac.canterbury.seng302.tab.controller.rest;

import java.util.ArrayList;
import java.util.List;
import javassist.NotFoundException;
import nz.ac.canterbury.seng302.tab.entity.Activity;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.entity.datatransferobjects.TabUserDto;
import nz.ac.canterbury.seng302.tab.service.ActivityService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.TeamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


/**
 * A rest controller, different from our usual controllers.
 * Used to dynamically get the players profile picture when they are selected for the lineup.
 */
@RestController
public class PlayerInformationController {
    Logger logger = LoggerFactory.getLogger(PlayerInformationController.class);
    @Autowired
    TabUserService tabUserService;

    @Autowired
    TeamService teamService;

    @Autowired
    ActivityService activityService;

    /**
     * Rest controller end point to get the profile picture of the user given its ID.
     *
     * @param userId id of the player selected
     * @return the image src of the players image, or an error response if not found
     */
    @GetMapping("/profile-picture/{userId}")
    public ResponseEntity<String> getProfilePicture(@PathVariable("userId") Long userId) {

        logger.info("GET /profile-picture/{userId}");
        // Retrieve user information and profile picture source based on the user ID
        String profilePictureSource = tabUserService.getById(userId).getProfilePicture();

        // Check if the profile picture source exists
        if (profilePictureSource != null) {
            // Return the profile picture source as the response
            return ResponseEntity.ok(profilePictureSource);
        } else {
            // Return a 404 Not Found response if the profile picture source does not exist
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Rest controller end point to get all the members of a team given the teamID
     *
     * @param teamId the id of the team
     * @param actId the id of the activity
     * @return a list of the team members as TabUserDTO's
     */
    @GetMapping("/team-members/{teamId}/{activityId}")
    public ResponseEntity<List<TabUserDto>> getTeamMembers(@PathVariable("teamId") Long teamId,
                                                           @PathVariable("activityId") Long actId) {
        logger.info("GET /team-members/{teamId}/{activityId}");

        Activity activity = activityService.getActivityById(actId);
        List<TabUserDto> memberDto = new ArrayList<>();
        try {
            Team team = teamService.getTeamById(teamId);
            List<TabUser> members = activityService.getPlayers(team, activity);
            for (TabUser member : members) {
                memberDto.add(new TabUserDto(member.getId(), member.getFirstName(),
                        member.getLastName()));
            }
        } catch (NotFoundException e) {
            logger.error(e.getMessage());
        }

        return new ResponseEntity<>(memberDto, HttpStatus.OK);
    }

    /**
     * Rest controller for a users name
     *
     * @param userId user ID
     * @return users name in a response entity
     */
    @GetMapping("/player-name/{userId}")
    public ResponseEntity<String> getPlayerName(@PathVariable("userId") Long userId) {
        logger.info("GET /player-name/{userId}");

        String playerName = tabUserService.getById(userId).getFirstName() + ' '
            + tabUserService.getById(userId).getLastName();

        if (!playerName.equals(" ")) {
            return ResponseEntity.ok(playerName);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Rest controller for an activity's current lineup
     *
     * @param activityId activity id
     * @return Lineup as a response entity
     */
    @GetMapping("/activity-lineup/{activityId}")
    public ResponseEntity<String> getActivityLineUp(@PathVariable("activityId") Long activityId) {
        logger.info("GET /activity-lineup/{activityId}");

        String lineup = activityService.getActivityById(activityId).getPosition();
        try {

            List<String> lineUpList = List.of(lineup.split(" "));
            return ResponseEntity.ok(lineUpList.get(0));
        } catch (NullPointerException e) {
            return ResponseEntity.ok(null);
        }


    }

}
