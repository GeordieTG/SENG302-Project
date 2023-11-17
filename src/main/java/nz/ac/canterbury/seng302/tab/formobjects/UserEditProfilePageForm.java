package nz.ac.canterbury.seng302.tab.formobjects;

import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.TabUser;

/**
 * Creates a form object to be used on the createTeam page
 */
public class UserEditProfilePageForm {

    private String firstName;
    private String lastName;
    private String email;
    private String dateOfBirth;
    private String address1;
    private String address2;
    private String suburb;
    private String city;
    private String postcode;
    private String country;
    private String favouriteSport;

    /**
     * Used in POST mappings when user and location don't exist
     */
    public UserEditProfilePageForm() {
    }

    /**
     * Constructor for editing a user profile
     *
     * @param user     user object
     * @param location location of user
     */
    public UserEditProfilePageForm(TabUser user, Location location) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.dateOfBirth = user.getDateOfBirth();
        this.address1 = location.getLine1();
        this.address2 = location.getLine2();
        this.suburb = location.getSuburb();
        this.city = location.getCity();
        this.country = location.getCountry();
        this.favouriteSport = user.getFavouriteSport();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFavouriteSport() {
        return favouriteSport;
    }

    public void setFavouriteSport(String favouriteSport) {
        this.favouriteSport = favouriteSport;
    }

}
