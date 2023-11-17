package nz.ac.canterbury.seng302.tab.entity.datatransferobjects;

import java.io.Serializable;

/**
 * Data Transfer Object to pass Java Objects through to JavaScript.
 * Created to pass TabUser Objects through rest requests.
 */

public class TabUserDto implements Serializable {
    private final long id;
    private final String firstName;
    private final String lastName;

    /**
     * Constructor for TabUserDto
     *
     * @param id        id of the user
     * @param firstName first name of the user
     * @param lastName  last name of the user
     */
    public TabUserDto(long id, String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}

