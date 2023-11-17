package nz.ac.canterbury.seng302.tab.utility;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * Used to generate various tokens and handle their functionality
 */
@Component
public class TokenGenerator {
    private TokenGenerator() {
    }

    /**
     * Generates a random token that  is strictly 12 characters long and is alphanumeric only.
     *
     * @return token as a string
     */
    public static String genAlphaToken() {
        UUID token = UUID.randomUUID();
        String tokenString = token.toString().replace("-", "");
        StringBuilder builder = new StringBuilder();
        Random random = new SecureRandom();
        for (int i = 0; i < 12; i++) {
            char c = tokenString.charAt(i);
            if (Character.isLetter(c)) {
                if (random.nextBoolean()) { // randomly change letter to uppercase
                    builder.append(Character.toUpperCase(c));
                } else {
                    builder.append(c);
                }
            } else { // keep digits and other characters as they are
                builder.append(c);
            }
        }
        return builder.toString();
    }

    /**
     * Generates a random token that can be used to confirm a users registration.
     *
     * @return token as a string
     */
    public static String genToken() {
        UUID token = UUID.randomUUID();
        return token.toString();
    }

    /**
     * Calculates the expiration time for the token by obtaining
     * the current time and adding two hours
     *
     * @return the expiration time as a time stamp. Can be inserted into sql directly.
     */
    public static Timestamp getExpiryTimeRegistration() {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        Timestamp currentTimestamp = new Timestamp(currentDate.getTime());
        calendar.setTime(currentTimestamp);
        calendar.add(Calendar.HOUR_OF_DAY, 2);
        return new Timestamp(calendar.getTime().getTime());
    }

    /**
     * Calculates the expiry time of a reset password token
     *
     * @return Expiry time of the reset password token
     */
    public static Timestamp getExpiryTimeOneHour() {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        Timestamp currentTimestamp = new Timestamp(currentDate.getTime());
        calendar.setTime(currentTimestamp);
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        return new Timestamp(calendar.getTime().getTime());
    }

    /**
     * Checks if the token is expired (was created more than two hours ago)
     *
     * @param expiryTime the expiration time of the token
     * @return True if expired, false if not
     */
    public static boolean isTokenExpired(Timestamp expiryTime) {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        Timestamp currentTimestamp = new Timestamp(currentDate.getTime());
        return expiryTime.before(currentTimestamp);
    }

    /**
     * Constructor for getting the expiry time for 10 minutes ahead
     *
     * @return Timestamp of 10 minutes ahead of current time
     */
    public static Timestamp getExpiryTime10Mins() {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        Timestamp currentTimestamp = new Timestamp(currentDate.getTime());
        calendar.setTime(currentTimestamp);
        calendar.add(Calendar.MINUTE, 10);
        return new Timestamp(calendar.getTime().getTime());
    }

    /**
     * Gets the current time, used to check against expiration times of tokens
     *
     * @return the current time as a time stamp
     */
    public static Timestamp getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        return new Timestamp(currentDate.getTime());
    }
}
