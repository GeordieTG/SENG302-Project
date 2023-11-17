package nz.ac.canterbury.seng302.tab.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Entity Object representing facts of an activity
 */
@Entity
public class StatisticFact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * The Activity the fact is associated with
     */
    @ManyToOne
    @JoinColumn(name = "activity_id")
    private Activity activity;

    /**
     * The description of the fact
     */
    @Column(nullable = false)
    private String description;
    /**
     * The time the fact occurred in minutes (Optional)
     */
    @Column
    private Integer timeOccurred;

    /**
     * Empty Constructor for JPA
     */
    public StatisticFact() {
        // ...
    }

    /**
     * Constructor for fact with time occurred
     *
     * @param activity The Activity the fact is from
     * @param description  The description of the fact
     * @param timeOccurred The time the fact occurred
     */
    public StatisticFact(Activity activity, String description,
                         Integer timeOccurred) {
        this.activity = activity;
        this.description = description;
        this.timeOccurred = timeOccurred;
    }


    /**
     * Constructor for fact with time occurred
     *
     * @param gameActivity The Activity the fact is from
     * @param description  The description of the fact
     */
    public StatisticFact(GameActivity gameActivity, String description) {
        this.activity = gameActivity;
        this.description = description;
    }

    public Activity getActivity() {
        return activity;
    }


    public Integer getTimeOccurred() {
        return timeOccurred;
    }

    public void setTimeOccurred(Integer scoredTime) {
        this.timeOccurred = scoredTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "StatisticFact{" + "ActivityId: " + activity.getId() + ", Description: "
            + description + ", Time: " + timeOccurred + "}";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

