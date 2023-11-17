package nz.ac.canterbury.seng302.tab.unit.utility;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.FileSystemException;
import nz.ac.canterbury.seng302.tab.utility.ImageHelper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

/**
 * Test class for image upload functionality
 */
@SpringBootTest
class ImageHelperTest {

    /**
     * Allows access to the image helper
     */
    private final ImageHelper imageHelper = new ImageHelper();
    /**
     * Creates a mock of the model
     */
    Model model = new ExtendedModelMap();

    ImageHelperTest() throws IOException {
    }

    /**
     * Tests that the image can be saved
     */
    @Test
    void saveTest_Valid() throws FileSystemException {
        MockMultipartFile testFile =
            new MockMultipartFile("file", "file.jpg", "image/jpg", "test data".getBytes());
        String result = imageHelper.save(testFile, "team");
        assertTrue(result.contains("public/teamPictures/file.jpg"));
    }

    /**
     * Tests that the correct error is thrown when the image type is not valid
     */
    @Test
    void saveTest_InvalidType() throws FileSystemException {
        MockMultipartFile testFile =
            new MockMultipartFile("file", "file.pdf", "application/pdf", "test data".getBytes());
        String result = imageHelper.save(testFile, "team");
        assertTrue(result.contains("public/teamPictures/file.pdf"));

    }

    /**
     * Tests that the correct error is thrown when the image is too large
     */
    @Test
    void validateTest_TooBig() throws FileSystemException {
        MockMultipartFile testFile =
            new MockMultipartFile("file", "file.jpg", "image/jpg", new byte[21474836]);
        String result = imageHelper.save(testFile, "team");
        assertTrue(result.contains("file.jpg"));
    }

}
