package nz.ac.canterbury.seng302.tab.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javassist.NotFoundException;
import nz.ac.canterbury.seng302.tab.entity.Sport;
import nz.ac.canterbury.seng302.tab.repository.SportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for SportResults, defined by the @link{Service} annotation.
 * This class links automatically with @link{SportRepository},
 * see the @link{Autowired} annotation below
 */
@Service
public class SportService {
    @Autowired
    private SportRepository sportRepository;

    /**
     * Gets a sport by its id
     *
     * @param id id of the sport
     * @return A sport entity
     * @throws Exception if the id is not in the database
     */
    public Sport getSportById(long id) throws IOException {
        try {
            Optional<Sport> sportOptional = sportRepository.findById(id);
            if (sportOptional.isPresent()) {
                return sportOptional.get();
            } else {
                throw new NotFoundException("Sport not found");
            }
        } catch (Exception e) {
            throw new IOException("Error retrieving sport", e);
        }
    }


    public boolean sportExists(String name) {
        return sportRepository.checkExists(name).isPresent();
    }

    public List<Sport> getSportByName(String name) {
        return sportRepository.findByName(name);
    }

    /**
     * Saves a new sport in the database
     *
     * @param name the new sport to be added
     */
    public Sport createSportInDatabase(String name) {
        Sport sport = new Sport(name);
        return sportRepository.save(sport);
    }

    /**
     * Adds new sport to if it does not already exist
     *
     * @param newSports String of the new sport name
     */
    public void addNewSports(String newSports) {
        String[] sports = newSports.split(", ");
        for (String sport : sports) {
            if (!sportExists(sport)) {
                createSportInDatabase(sport);
            }
        }
    }

    public List<Sport> findAll() {
        return sportRepository.findAll();
    }

    public List<String> findAllNames() {
        return sportRepository.findAll().stream().map(Sport::getName).toList();
    }

    public void deleteAllData() {
        sportRepository.deleteAll();
    }

    public List<Sport> getSportResults() {
        return sportRepository.findAll();
    }
}

