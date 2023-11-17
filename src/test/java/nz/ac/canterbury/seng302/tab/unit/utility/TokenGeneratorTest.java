package nz.ac.canterbury.seng302.tab.unit.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import nz.ac.canterbury.seng302.tab.utility.TokenGenerator;
import org.junit.jupiter.api.Test;

/**
 * Tests the token generator function
 */
class TokenGeneratorTest {

    /**
     * Ensure that a token can be generated in the form of a 34 character string
     */
    @Test
    void generateTokenTest() {
        String token = TokenGenerator.genToken();
        assertEquals(36, token.length());
    }

    @Test
    void testGenAlphaToken() {
        String token = TokenGenerator.genAlphaToken();
        assertTrue(token.matches("[a-zA-Z0-9]+"));
    }

    @Test
    void testGenAlphaToken_Is12Char() {
        String token = TokenGenerator.genAlphaToken();
        assertEquals(12, token.length());
    }

    @Test
    void testGenAlphaToken_IsUnique() {
        // Test that two consecutive tokens are not the same
        String token = TokenGenerator.genAlphaToken();
        String token2 = TokenGenerator.genAlphaToken();
        assertNotEquals(token, token2);
    }

    @Test
    void testGenAlphaTokenPerformance() {
        // Test that the method generates tokens quickly enough
        long startTime = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            TokenGenerator.genAlphaToken();
        }
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        assertTrue(elapsedTime < 1000000000); // 1 second in nanoseconds
    }


}
