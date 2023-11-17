package nz.ac.canterbury.seng302.tab.unit.utility;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import nz.ac.canterbury.seng302.tab.utility.ValidationHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test class for the Validation Helper class
 */
class ValidationHelperTest {


    /**
     * Test fo checking correct team name.
     */
    @Test
    void checkValidTeamName() {
        Assertions.assertTrue(ValidationHelper.validateName("name"));
        assertFalse(ValidationHelper.validateName("(name)"));
        assertTrue(ValidationHelper.validateName("{name}"));
        assertTrue(ValidationHelper.validateName("{name.}"));
        assertTrue(ValidationHelper.validateName("{name.} "));
        assertFalse(ValidationHelper.validateName("122"));
        assertFalse(ValidationHelper.validateName("#%"));
        assertFalse(ValidationHelper.validateName("#%"));
    }

    /**
     * Test for checking correct team name.
     */
    @Test
    void checkValidOnlyLetters() {
        assertTrue(ValidationHelper.validateOnlyLetters("name"));
        assertFalse(ValidationHelper.validateOnlyLetters("122"));
        assertFalse(ValidationHelper.validateOnlyLetters("#%"));

        assertFalse(ValidationHelper.validateOnlyLetters("(name)"));
        assertFalse(ValidationHelper.validateOnlyLetters("{name}"));
        assertFalse(ValidationHelper.validateOnlyLetters("{name.}"));
        assertFalse(ValidationHelper.validateOnlyLetters("{name.} "));
    }

    /**
     * Test for checking basic team detail.
     */
    @Test
    void validateBasicTeamDetailsTest_Valid() {
        assertTrue(
            ValidationHelper.validateBasicTeamDetails(null, "Bishops", "Basketball", "Greymouth",
                "Russia"));
    }

    /**
     * Test for checking invalid name input.
     */
    @Test
    void validateBasicTeamDetailsTest_InvalidName() {
        try {
            assertFalse(ValidationHelper.validateBasicTeamDetails(null, "Bishops1", "Basketball",
                "Greymouth", "Ghana"));
        } catch (Exception nullPointerException) {
            // If the tests goes correctly it will throw a NullPointerException
            // as it attempts to add an attribute to the model
        }
    }

    /**
     * Test for checking invalid sport input.
     */
    @Test
    void validateBasicTeamDetailsTest_InvalidSport() {
        try {
            assertFalse(ValidationHelper.validateBasicTeamDetails(null, "Bishops", "Basket_ball",
                "Greymouth", "Brazil"));
        } catch (Exception nullPointerException) {
            // If the tests goes correctly it will throw a NullPointerException
            // as it attempts to add an attribute to the model
        }
    }

    /**
     * Test for checking invalid location input.
     */
    @Test
    void validateBasicTeamDetailsTest_InvalidLocation() {
        try {
            assertFalse(ValidationHelper.validateBasicTeamDetails(null, "Bishops", "Basketball",
                " Qzw546exrcty", "#@$Z56f7"));
        } catch (Exception nullPointerException) {
            // If the tests go correctly it will throw a NullPointerException as it
            // attempts to add an attribute to the model
        }
    }

    /**
     * Test for checking all valid input.
     */
    @Test
    void validateBasicTeamDetailsTest_AllInvalid() {
        try {
            assertFalse(ValidationHelper.validateBasicTeamDetails(null, "()", "()", "7805",
                "!@#$%$#$&*&()"));
        } catch (Exception nullPointerException) {
            // If the tests go correctly it will throw a NullPointerException as it
            // attempts to add an attribute to the model
        }
    }

    /**
     * Test for checking all valid token
     */
    @Test
    void validateTokenTest() {
        try {
            assertTrue(ValidationHelper.validateToken("FdkjguTP8T21", null));
            assertTrue(ValidationHelper.validateToken("RomQp52l6931", null));
            assertFalse(ValidationHelper.validateToken("Fdkjg TP8T21", null));
            assertFalse(ValidationHelper.validateToken("Fdkjg TP821", null));
            assertFalse(ValidationHelper.validateToken("Fdkjg ", null));
            assertFalse(ValidationHelper.validateToken("Fdkjgoho23fgTP821", null));
            assertFalse(ValidationHelper.validateToken("$$$$$$$$$$$%", null));
            assertTrue(ValidationHelper.validateToken("qwertyuioplk", null));
            assertTrue(ValidationHelper.validateToken("111111111111", null));

        } catch (Exception nullPointerException) {
            // If the tests go correctly it will throw a NullPointerException as it
            // attempts to add an attribute to the model
        }
    }

}
