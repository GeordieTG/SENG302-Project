package nz.ac.canterbury.seng302.tab.service;


import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import javassist.NotFoundException;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.repository.LocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * class that perform the location functionality and create in the table
 */
@Service
public class LocationService {
    /**
     * Allows to log
     */
    Logger logger = LoggerFactory.getLogger(LocationService.class);
    @Autowired
    private LocationRepository locationRepository;

    /**
     * Gets a location entity by a given location id
     *
     * @param id specific location id
     * @return Location entity with the id
     * @throws Exception if location not found with the specified id
     */
    public Location getLocationByLocationId(long id) throws IOException {
        try {
            logger.info("get the location by the id");
            Optional<Location> locationOptional = locationRepository.findByLocationId(id);
            if (locationOptional.isPresent()) {
                return locationOptional.get();
            } else {
                throw new NoSuchElementException("Location not found");
            }
        } catch (Exception e) {
            throw new IOException("Error retrieving location", e);
        }
    }

    /**
     * Creates a new location entity
     *
     * @param location Location entiy
     * @return A location entity which represents a location in the database
     */
    public Location createLocationInDatabase(Location location) {
        return locationRepository.save(location);
    }

    /**
     * Creates a new location entity
     *
     * @param city    String of the city
     * @param country String of the country
     * @return A location entity which represents a location in the database
     */
    public Location createLocationInDatabase(String city, String country) {
        Location location = new Location(city, country);

        return locationRepository.save(location);
    }

    /**
     * Get all location within the database
     *
     * @return list of all the locations
     */
    public List<Location> getAllLocationInDatabase() {
        logger.info("get all the location in the database");
        return locationRepository.findAll();
    }

    /**
     * Updates an existing entity by a given id for the mandatory fields
     *
     * @param id      String of the location entity id
     * @param city    String of the city
     * @param country String of the country
     * @throws Exception Throws error if location cannot be found by the id
     */
    public Location updateMandatoryInDatabase(Long id, String city, String country)
        throws IOException {
        Location location = getLocationByLocationId(id);
        logger.info("update location in the database");
        location.setCity(city);
        location.setCountry(country);
        return locationRepository.save(location);
    }

    /**
     * Updates an existing entity by a given id for the non-mandatory fields
     *
     * @param id       String of the location entity id
     * @param line1    String of the line_1
     * @param line2    String of the line_2
     * @param suburb   String of the suburb
     * @param postcode String of the postcode
     * @throws NotFoundException Throws error if location cannot be found by the id
     */
    public void updateOptionalInDatabase(Long id, String line1, String line2, String suburb,
                                         String postcode) throws NotFoundException {
        try {
            Location location = getLocationByLocationId(id);
            logger.info("update option data about location in the database");
            location.setLine1(line1);
            location.setLine2(line2);
            location.setSuburb(suburb);
            location.setPostcode(postcode);
            locationRepository.save(location);
        } catch (Exception e) {
            throw new NotFoundException("Location not found.");
        }
    }

    /**
     * delete the location from the database
     *
     * @param id the id of the location to be deleted
     */
    public void deleteLocationInDatabase(Long id) {
        logger.info("delete location in the database");
        locationRepository.deleteById(id);
    }

    /**
     * Deletes all data in the location database
     */
    public void deleteAllData() {
        logger.info("delete all locations in the database");
        locationRepository.deleteAll();
    }

    public void save(Location location) {
        locationRepository.save(location);
    }

    public List<String> findCities() {
        return locationRepository.findCities();
    }

}
