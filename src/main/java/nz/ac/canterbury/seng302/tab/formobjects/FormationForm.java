package nz.ac.canterbury.seng302.tab.formobjects;

/**
 * Creates a form object to handle passing formation information around
 */
public class FormationForm {

    private String formation;
    private String formationId;
    private String formationDisplay;
    private String sport;

    public String getFormation() {
        return formation;
    }

    public void setFormation(String formation) {
        setFormationDisplay(formation);
        this.formation = formation;
    }

    public String getFormationDisplay() {
        return formationDisplay;
    }

    private void setFormationDisplay(String formationDisplay) {
        if (formationDisplay != null) {
            StringBuilder reversedString = new StringBuilder(formationDisplay);
            reversedString.reverse();
            this.formationDisplay = reversedString.toString();
        }
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getFormationId() {
        return formationId;
    }

    public void setFormationId(String formationId) {
        this.formationId = formationId;
    }
}
