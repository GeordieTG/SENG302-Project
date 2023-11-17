package nz.ac.canterbury.seng302.tab.formobjects;

import nz.ac.canterbury.seng302.tab.entity.enums.SupportedSports;

/**
 * Creates a form object to be used on the createTeam page
 */
public class CreateTeamForm {

    private String name;
    private SupportedSports sport;
    private String address1;
    private String address2;
    private String suburb;
    private String city;
    private String country;
    private String postcode;

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public SupportedSports getSport() {
        return sport;
    }

    public void setSport(SupportedSports sport) {
        this.sport = sport;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }
}
