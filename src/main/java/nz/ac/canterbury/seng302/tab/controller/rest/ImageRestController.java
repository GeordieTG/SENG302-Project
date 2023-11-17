package nz.ac.canterbury.seng302.tab.controller.rest;

import java.nio.file.FileSystemException;
import javassist.NotFoundException;
import nz.ac.canterbury.seng302.tab.entity.Club;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.service.ClubService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.TeamService;
import nz.ac.canterbury.seng302.tab.utility.ImageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * A rest controller.
 * Used to add images to the database.
 */
@RestController
public class ImageRestController {

    /**
     * To generate logs
     */
    Logger logger = LoggerFactory.getLogger(ImageRestController.class);

    @Autowired
    TeamService teamService;
    @Autowired
    TabUserService tabUserService;
    @Autowired
    ClubService clubService;

    /**
     * To use the image saving
     */
    ImageHelper imageHelper = new ImageHelper();

    /**
     * post mapping for uploading an image
     *
     * @param image multipart file representing an image a user wants to upload
     * @param id    of either a user, club, or team to submit too
     * @param type  string representing which file directory to be saved too.
     */
    @PostMapping("/uploadImage")
    @ResponseBody
    public String uploadImage(@RequestParam("image") MultipartFile image,
                              @RequestParam("id") Long id,
                              @RequestParam("type") String type) {
        logger.info("POST /uploadImage");
        try {
            switch (type) {
                case "club" -> {
                    Club club = clubService.getById(id);
                    String file = imageHelper.save(image, type);
                    club.setImage(file);
                    clubService.saveClub(club);

                }
                case "team" -> {
                    Team team = teamService.getTeamById(id);
                    String file = imageHelper.save(image, type);
                    team.setImage(file);
                    teamService.save(team);
                }
                case "user" -> {
                    TabUser user = tabUserService.getById(id);
                    String file = imageHelper.save(image, type);
                    user.setProfilePicture(file);
                    tabUserService.addTabUser(user);
                }
                default -> {
                    return "You are a failure";
                }
            }
        } catch (FileSystemException | NotFoundException e) {
            logger.error(String.valueOf(e));
        }
        return "Success";
    }
}
