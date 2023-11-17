package nz.ac.canterbury.seng302.tab.controller.rest;

import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import javassist.NotFoundException;
import nz.ac.canterbury.seng302.tab.entity.Club;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.entity.datatransferobjects.ClubDto;
import nz.ac.canterbury.seng302.tab.service.ClubService;
import nz.ac.canterbury.seng302.tab.service.LocationService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.TeamService;
import nz.ac.canterbury.seng302.tab.service.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * A rest controller.
 * Used to create a club in the DB without having to refresh the page.
 */
@RestController
public class ClubRestController {

    /**
     * Allows us to log
     */
    Logger logger = LoggerFactory.getLogger(ClubRestController.class);

    @Autowired
    LocationService locationService;
    @Autowired
    ClubService clubService;

    @Autowired
    ValidationService validationService;

    @Autowired
    TeamService teamService;

    @Autowired
    TabUserService tabUserService;

    /**
     * Post request called from JS to save a club in the DB
     *
     * @param clubDto       Sent in the HTTP request as a JSON file,
     *                      gets mapped to a ClubDto automatically
     * @param bindingResult Used to ensure empty fields are bound as null
     * @return A ResponseEntity containing the HTTP status and any error messages to display
     */
    @PostMapping("/create-club")
    @ResponseBody
    public ResponseEntity<Map<String, String>> createClub(@RequestBody ClubDto clubDto,
                                                          BindingResult bindingResult) {
        logger.info("POST /create-club");
        Map<String, String> body = new HashMap<>();
        try {
            if (validationService.validateClub(clubDto, body)) {
                locationService.createLocationInDatabase(clubDto.getClub().getLocation());
                //Set default image (will be overriden in case of new image)
                Long clubId = clubService.saveClubDto(clubDto,
                    tabUserService.getCurrentlyLoggedIn()).getId();
                body.put("id", String.valueOf(clubId));
            } else {
                logger.info("Invalid club detail.");
                return ResponseEntity.status(400).body(body);
            }
        } catch (Exception e) {
            logger.info(e.toString());
            return ResponseEntity.status(400).body(body);
        }
        return ResponseEntity.status(201).body(body);
    }

    /**
     * PUT request called from JS to edit an existing club within the DB
     *
     * @param clubId        URL parameter
     * @param clubDto       Sent in the HTTP request as a JSON file,
     *                      gets mapped to a ClubDto automatically
     * @param bindingResult Used to ensure empty fields are bound as null
     * @return A ResponseEntity containing the HTTP status and any error messages to display
     */
    @PostMapping("/edit-club")
    @ResponseBody
    @Transactional
    public ResponseEntity<Map<String, String>> editClub(@RequestParam("id") Long clubId,
                                                        @RequestBody ClubDto clubDto,
                                                        BindingResult bindingResult) {

        logger.info("PUT /edit-club");
        Map<String, String> body = new HashMap<>();
        try {
            //get club
            Club club = clubService.getById(clubId);
            if (validationService.validateClub(clubDto, body)) {

                //if all entries okay, update location
                Location current = locationService.getLocationByLocationId(
                    club.getLocation().getLocationId());
                Location updated = clubDto.getClub().getLocation();
                current.setLine1(updated.getLine1());
                current.setLine2(updated.getLine2());
                current.setSuburb(updated.getSuburb());
                current.setPostcode(updated.getPostcode());
                current.setMandatory(updated.getCity(), updated.getCountry());
                locationService.save(current);

                //blanket update rest
                Club updatedClub = clubDto.getClub();
                club.setImage(updatedClub.getImage());
                club.setName(updatedClub.getName());
                club.setSport(updatedClub.getSport());

                clubService.updateClubTeams(club, clubDto.getTeamIds());

            } else {
                logger.info("Invalid club detail.");
                return ResponseEntity.status(400).body(body);
            }
        } catch (Exception e) {
            logger.info(e.toString());
            return ResponseEntity.status(400).body(body);
        }
        return ResponseEntity.status(200).body(body);
    }

    /**
     * Get request called from JS to get a teams information
     *
     * @param id The id of the team
     * @return The team entity
     * @throws NotFoundException Thrown if the team doesn't exist
     */
    @GetMapping("/get-team/{id}")
    public ResponseEntity<Team> getTeam(@PathVariable Long id) throws NotFoundException {
        logger.info("GET /get-team/{teams}");
        Team team = teamService.getTeamById(id);
        return ResponseEntity.ok(team);
    }
}
