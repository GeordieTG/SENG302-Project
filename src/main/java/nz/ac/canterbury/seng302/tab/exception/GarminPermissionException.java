package nz.ac.canterbury.seng302.tab.exception;

/**
 * Handle data not share exception by the Garmin
 */
public class GarminPermissionException extends Exception {
    /**
     * Controller class for garmin permission exception
     */
    public GarminPermissionException() {
        super("You have not given the correct permission");
    }


}
