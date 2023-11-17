package nz.ac.canterbury.seng302.tab.repository;

import java.util.List;
import nz.ac.canterbury.seng302.tab.entity.Food;
import org.springframework.data.repository.CrudRepository;

/**
 * Access the Meal database
 */
public interface FoodRepository extends CrudRepository<Food, Long> {

    Food findById(long id);

    List<Food> findAll();


}

