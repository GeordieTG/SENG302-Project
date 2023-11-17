package nz.ac.canterbury.seng302.tab.formobjects;

import nz.ac.canterbury.seng302.tab.entity.Activity;


/**
 * Creates a form object to be used on the editActivity page
 */
public class ActivityForm {

    private String id;
    private long teamId;
    private String type;
    private String startTime;
    private String endTime;
    private String description;

    private String address1;
    private String address2;
    private String suburb;
    private String city;
    private String postcode;
    private String country;

    /**
     * Required for Spring Boot to autofill
     */
    public ActivityForm() {
    }

    /**
     * Constructor for activity
     *
     * @param activity activity object
     */
    public ActivityForm(Activity activity) {
        this.id = String.valueOf(activity.getId());
        if (activity.getTeam() == null) {
            this.teamId = -1;
        } else {
            this.teamId = activity.getTeam().getId();
        }
        this.type = activity.getType();
        this.startTime = activity.getStartTime();
        this.endTime = activity.getEndTime();
        this.description = activity.getDescription();
        this.address1 = activity.getLocation().getLine1();
        this.address2 = activity.getLocation().getLine2();
        this.suburb = activity.getLocation().getSuburb();
        this.city = activity.getLocation().getCity();
        this.postcode = activity.getLocation().getPostcode();
        this.country = activity.getLocation().getCountry();

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTeamId() {
        return teamId;
    }

    public void setTeam(long id) {
        this.teamId = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

}
