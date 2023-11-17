package nz.ac.canterbury.seng302.tab.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Entity to represent formations
 */
@Entity
public class Formation {

    /**
     * Primary key of the formation table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "formation_id")
    private Long id;

    /**
     * the formation string: "1-3-4-5" format
     */
    @Column(name = "formation")
    private String formationString;
    /**
     * the field
     */
    @Column(name = "field")
    private String field;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
    /**
     * Position string: i.e. "1,56,34,8-33,2,4-85,90,32,31"
     */
    @Column(name = "position")
    private String position;


    /**
     * JPA required no-args constructor
     */
    protected Formation() {
    }


    /**
     * JPA required no-args constructor
     */
    public Formation(String formation, String sport, Team team) {
        this.formationString = formation;
        this.field = sport;
        this.team = team;

    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFormation() {
        return formationString;
    }

    public void setFormation(String formation) {
        this.formationString = formation;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    /**
     * Setter returns String for mockito testing purposes
     *
     * @param position String position
     * @return position
     */
    public String setPosition(String position) {
        this.position = position;
        return position;
    }

    public String getPosition() {
        return position;
    }

    public Long getFormationId() {
        return id;
    }

    public void setFormationId(Long formationId) {
        this.id = formationId;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    /**
     * Creates the string for printing FormResult objects
     *
     * @return String the output when printing FormResult objects
     */
    @Override
    public String toString() {
        return "Formation{"
            + "formation=" + formationString
            + ", field='" + field + '\''
            + ", team='" + team + '\''
            + '}';
    }
}
