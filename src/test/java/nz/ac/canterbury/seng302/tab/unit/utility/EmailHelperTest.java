package nz.ac.canterbury.seng302.tab.unit.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;

import nz.ac.canterbury.seng302.tab.utility.EmailHelper;
import org.junit.jupiter.api.Test;

/**
 * Test email functionality
 */
class EmailHelperTest {

    /**
     * Test correct url is returned on local environment
     */
    @Test
    void localUrl() {
        String result = EmailHelper.getUrl("http://localhost");
        assertEquals("http://localhost:8080/", result);
    }

    /**
     * Test correct url is returned on test environment
     */
    @Test
    void testUrl() {
        String result =
            EmailHelper.getUrl("https://csse-s302g4.canterbury.ac.nz/test/lostPassword");
        assertEquals("https://csse-s302g4.canterbury.ac.nz/test/", result);
    }

    /**
     * Test correct url is returned on prod environment
     */
    @Test
    void prodUrl() {
        String result =
            EmailHelper.getUrl("https://csse-s302g4.canterbury.ac.nz/prod/lostPassword");
        assertEquals("https://csse-s302g4.canterbury.ac.nz/prod/", result);
    }
}
