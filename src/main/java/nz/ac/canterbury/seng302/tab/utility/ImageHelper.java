package nz.ac.canterbury.seng302.tab.utility;

import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

/**
 * Class for ImageResults, defined by the @link{service} annotation.
 */
public class ImageHelper {

    public static final String USER_DIR = System.getProperty("user.dir");
    Logger logger = LoggerFactory.getLogger(ImageHelper.class);
    private String errorMessage = "";

    /**
     * initialize the image helper
     */
    public ImageHelper() {
        Path teamPictures = Path.of(USER_DIR + "/public/teamPictures/");
        Path userPictures = Path.of(USER_DIR + "/public/userPictures/");
        Path clubPictures = Path.of(USER_DIR + "/public/clubPictures/");
        try {
            Files.createDirectories(teamPictures);
            Files.createDirectories(userPictures);
            Files.createDirectories(clubPictures);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Gets field Image Link from a field string
     *
     * @param field type of field
     * @return image link to that field
     */
    public static String getFieldImageLink(String field) {
        String bgImage;
        switch (field) {
            case "Baseball" -> bgImage = "images/fields/Baseball.png";
            case "Basketball" -> bgImage = "images/fields/Basketball.png";
            case "Netball" -> bgImage = "images/fields/Netball.jpg";
            case "Cricket" -> bgImage = "images/fields/Cricket.png";
            case "Football" -> bgImage = "images/fields/Football.jpg";
            case "Futsal" -> bgImage = "images/fields/Futsal.png";
            case "Hockey" -> bgImage = "images/fields/Hockey.png";
            case "Rugby", "RugbyLeague" -> bgImage = "images/fields/Rugby.png";
            case "Volleyball" -> bgImage = "images/fields/Volleyball.jpg";
            default -> bgImage = "images/fields/default.jpg";
        }
        return bgImage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String message) {
        errorMessage = message;
    }

    /**
     * Saves file/image to a file directory with /public/images prefix
     *
     * @param file MultipartFile that represents the uploaded image from the users directory
     * @return String filename or Internal Error
     */
    public String save(MultipartFile file, String type) throws
        FileSystemException {
        Path root;
        String filePath = "public/" + type + "Pictures/" + file.getOriginalFilename();
        root = Path.of(USER_DIR + "/" + filePath);

        delete(file.getOriginalFilename());
        try {
            file.transferTo(root);
            return filePath;
        } catch (Exception e) {
            throw new FileSystemException("Internal Error try again");
        }
    }

    /**
     * Trys to delete a file/image if it exists in the file directory
     *
     * @param filename A string representing the desired file/image to be deleted
     */
    public void delete(String filename) throws FileSystemException {
        try {
            Path root = Path.of(USER_DIR + "/public/images/");
            Path file = root.resolve(filename);
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new FileSystemException("Could not delete!: " + e.getMessage());
        }
    }
}
