package nz.ac.canterbury.seng302.tab.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Entity to represent the Location
 * Used for both team and user
 */
@Entity

public class Location {
    /**
     * Primary key of the locaiton table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private long locationId;
    /**
     * the street address
     */
    @Column(name = "line_1")
    private String line1 = "";
    /**
     * extra street address
     */
    @Column(name = "line_2")
    private String line2 = "";
    /**
     * the suburb
     */
    @Column(name = "suburb")
    private String suburb = "";
    /**
     * hold the city name
     */
    @Column(name = "city", nullable = false)
    private String city;
    /**
     * hold the country name
     */
    @Column(name = "country", nullable = false)
    private String country;
    /**
     * hold the postcode of the address
     */
    @Column(name = "postcode")
    private String postcode;

    @OneToOne(mappedBy = "location")
    private Team team;

    @OneToOne(mappedBy = "location")
    private TabUser userLocId;

    protected Location() {
    }

    /**
     * Location entity constructor for Team
     *
     * @param city    String of the city
     * @param country String of the country
     */
    public Location(String city, String country) {
        this.city = city;
        this.country = country;
    }

    /**
     * Sets mandatory address fields
     *
     * @param city    string
     * @param country string
     */
    public void setMandatory(String city, String country) {
        this.city = city;
        this.country = country;
    }

    /**
     * Used when an ArrayList string form of the location is required.
     *
     * @return ArrayList [line1, line2, suburb, city, country, postcode]
     */
    public List<String> getFullLocationArray() {
        List<String> fullLocation = new ArrayList<>();
        fullLocation.add(line1);
        fullLocation.add(line2);
        fullLocation.add(suburb);
        fullLocation.add(city);
        fullLocation.add(country);
        fullLocation.add(postcode);
        return fullLocation;
    }

    /**
     * returns JSON format of the full location
     *
     * @return String
     */
    public String getFullLocationString() {
        return "location{ line_1=" + line1 + '\'' + ", line_2=" + line2 + '\''
            + ", suburb=" + suburb + '\''
            + ", city=" + city + '\'' + ", country=" + country + '\''
            + ", postcode=" + postcode + "}";
    }

    public String getCity() {
        return city;
    }

    /**
     * Sets city if the arg String is at least 1 length, and is not a whitespace
     *
     * @param city String
     */
    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    /**
     * sets Country if the arg string is greater than 3 in length
     * All leading and trailing whitespaces are stripped in advance
     *
     * @param country string
     */
    public void setCountry(String country) {
        this.country = country;
    }

    public Long getLocationId() {
        return locationId;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    /**
     * Override .toString(). Gives a string summary of the recorded location variables.
     *
     * @return string
     */
    @Override
    public String toString() {
        return String.format("%s, %s", city, country);
    }

    /**
     * A method to generate street address string.
     * Returns null if street address fields are all empty
     *
     * @return String
     */
    public String getStreetAddressString() {
        StringBuilder builder = new StringBuilder();
        List<String> addresses = Arrays.asList(line1, line2, suburb, postcode);
        for (String address : addresses) {
            if (builder.isEmpty()) {
                builder.append(address);
            } else if (!address.equals("")) {
                builder.append(", ");
                builder.append(address);
            }
        }
        return builder.toString();
    }
}