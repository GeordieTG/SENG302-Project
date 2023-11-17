package nz.ac.canterbury.seng302.tab.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


/**
 * Entity class reflecting an entry of name, and favourite programming language
 * Note the @link{Entity} annotation required for declaring this as a persistence entity
 */
@Entity
@Table(name = "TAB_USER")
public class TabUser implements UserDetails {
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final Set<Activity> activities = new HashSet<>();
    /**
     * Creates the UserTAB/Team one to many relationship table "tab_user_registration"
     * in the database to represent a TABUser registering for a team
     */
    @OneToMany(mappedBy = "user")
    public Set<TeamRoles> teamRoles;
    /**
     * Generated id of the TabUser entity.
     * Primary key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    /**
     * First name string of the user
     */
    @Column(nullable = false)
    private String firstName;
    /**
     * Last name string of the user
     */
    @Column(nullable = false)
    private String lastName;
    /**
     * Email string of the user
     */
    @Column(nullable = false)
    private String email;
    /**
     * DoB string of the user
     * DD/MM/YYYY format
     */
    @Column(nullable = false)
    private String dateOfBirth;
    /**
     * Password string of the user
     * Encrypted
     */
    @Column(nullable = false)
    private String password;
    /**
     * Profile picture string
     * Contains path to the image from the storage location
     */
    @Column(nullable = false)
    private String profilePicture;
    /**
     * String of the user's favourite sport
     */
    @Column(nullable = false)
    private String favouriteSport;
    @Column
    private String confirmation;
    @Column
    private String expiryTime;
    /**
     * Token used to identify which user is attempting to reset their password when
     * clicking on an email link.
     * Null if password hasn't been requested to be reset.
     */
    @Column
    private String resetPasswordToken;
    /**
     * Indicates the time that the reset password token should expire.
     * Null if password hasn't been requested to be reset.
     */
    @Column
    private String resetPasswordExpiryTime;
    @Column()
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<Authority> userRoles;
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "address_id", referencedColumnName = "location_id")
    private Location location;

    /**
     * The users calorie intake preference
     */
    @Column(columnDefinition = "varchar(255) default 'maintain'")
    private String calorieIntakePreference;

    /**
     * JPA required no-args constructor
     */
    protected TabUser() {
    }

    /**
     * Creates a new FormResult object
     *
     * @param fullName       a list containing the first and last name of the user
     * @param location       location of the user as Location
     * @param email          user's email
     * @param dateOfBirth    DoB of the user. In DD/MM/YYYY format
     * @param profilePicture path to the profile image
     * @param favouriteSport Favourite sport of the user
     * @param password       user's password
     */
    public TabUser(List<String> fullName, Location location, String email,
                   String dateOfBirth, String password, String profilePicture,
                   String favouriteSport) {
        this.firstName = fullName.get(0);
        this.lastName = fullName.get(1);
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.password = password;
        this.profilePicture = profilePicture;
        this.favouriteSport = favouriteSport;
        this.location = location;
    }

    /**
     * Basic constructor with the mandatory fields
     *
     * @param firstName first name of the user
     * @param lastName  last name of the user
     * @param location  location of the user, as Location
     */
    public TabUser(String firstName, String lastName, Location location) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
    }


    public String getCalorieIntakePreference() {
        return calorieIntakePreference;
    }

    public void setCalorieIntakePreference(String calorieIntakePreference) {
        this.calorieIntakePreference = calorieIntakePreference;
    }

    public void setTeamRoles(Set<TeamRoles> teams) {
        this.teamRoles = teams;
    }

    public void addTeamRole(TeamRoles teamRole) {
        this.teamRoles.add(teamRole);
    }

    /**
     * Returns the users ID
     *
     * @return Long the users ID
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the users first name
     *
     * @return String the users first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the users last name
     *
     * @return String the users last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Returns the users email
     *
     * @return String the users email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email address.
     *
     * @param email the email address to be set for the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the users date of birth
     *
     * @return String the users date of birth
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Returns the users password
     *
     * @return String the users password
     */
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Returns the users profile picture
     *
     * @return String the users profile picture
     */
    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    /**
     * Returns the users favourite sport
     *
     * @return String the users favourite sport
     */
    public String getFavouriteSport() {
        return favouriteSport;
    }

    public Long getLocationId() {
        return location.getLocationId();
    }

    public String getResetPasswordExpiryTime() {
        return resetPasswordExpiryTime;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    @Override
    public boolean equals(Object user) {
        if (user == this) {
            return true;
        }
        if (!(user instanceof TabUser castedUser)) {
            return false;
        }
        return Objects.equals(castedUser.getId(), this.id);
    }

    /**
     * overide hash code as you have to for sonar
     *
     * @return hash code comparison
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    /**
     * Creates the string for printing FormResult objects
     *
     * @return String the output when printing FormResult objects
     */
    @Override
    public String toString() {
        return "FormResult{"
            + "id=" + id
            + ", first name='" + firstName + '\''
            + ", last name='" + lastName + '\''
            + ", email='" + email + '\''
            + ", DOB='" + dateOfBirth + '\''
            + ", PFP='" + profilePicture + '\''
            + ", sport='" + favouriteSport + '\''
            + ", locID='" + location.getLocationId() + '\''
            + '}';
    }

    /**
     * Adds a new role/authority to the user's list of roles.
     *
     * @param authority the new role/authority to be added to the user's list
     */
    public void grantAuthority(String authority) {
        if (userRoles == null) {
            userRoles = new ArrayList<>();
        }
        userRoles.add(new Authority(authority));
    }

    /**
     * Gets the list of authorities/roles associated with the user.
     *
     * @return a List of GrantedAuthority objects representing the user's authorities/role
     */
    public List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        this.userRoles.forEach(
            authority -> authorities.add(new SimpleGrantedAuthority(authority.getRole())));
        return authorities;
    }

    public Location getLocation() {
        return location;
    }
}
