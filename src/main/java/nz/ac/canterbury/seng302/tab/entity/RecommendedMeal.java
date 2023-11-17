package nz.ac.canterbury.seng302.tab.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;

/**
 * Entity to represent a recommended meal
 */
@Entity
public class RecommendedMeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meal_id")
    private Long id;

    @Column
    private String caloriePreference;

    @Column
    private String name;


    @JsonIgnore
    @OneToMany(mappedBy = "recommendedMeal", fetch = FetchType.EAGER)
    private List<Food> foods;

    @Column(columnDefinition = "varchar(20) default 'default'")
    private String icon;

    /**
     * JPA required no-args constructor
     *
     */
    public RecommendedMeal() {
        // empty constructor
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaloriePreference() {
        return caloriePreference;
    }

    public void setCaloriePreference(String caloriePreference) {
        this.caloriePreference = caloriePreference;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public List<Food> getFoods() {
        return foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String iconKey) {
        this.icon = iconKey;
    }
}
