package nz.ac.canterbury.seng302.tab.formobjects;

import java.util.Arrays;
import java.util.List;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.entity.TeamRoles;

/**
 * Creates a form object to be used on the editeTeam page
 */
public class EditTeamForm {

    private final List<String> roleList = Arrays.asList("Manager", "Coach", "Member");
    private List<TeamRoles> teamRoles;
    private String id;
    private String name;
    private String sport;
    private String address1;
    private String address2;
    private String suburb;
    private String city;
    private String country;
    private String postcode;

    /**
     * Required for Spring Boot to autofill
     */
    public EditTeamForm() {
    }

    /**
     * Constructor for the edit team form
     *
     * @param team          team being edited
     * @param location      location of team
     * @param teamRolesList list of team roles
     */
    public EditTeamForm(Team team, Location location, List<TeamRoles> teamRolesList) {
        this.id = String.valueOf(team.getId());
        this.name = team.getName();
        this.sport = team.getSport();
        this.address1 = location.getLine1();
        this.address2 = location.getLine2();
        this.suburb = location.getSuburb();
        this.city = location.getCity();
        this.postcode = location.getPostcode();
        this.country = location.getCountry();
        this.teamRoles = teamRolesList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public List<TeamRoles> getTeamRoles() {
        return teamRoles;
    }

    public void setTeamRoles(List<TeamRoles> teamRoles) {
        this.teamRoles = teamRoles;
    }

    public List<String> getRoleList() {
        return roleList;
    }
}
