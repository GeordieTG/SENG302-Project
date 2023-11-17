package nz.ac.canterbury.seng302.tab.unit.service;


import static org.junit.jupiter.api.Assertions.assertEquals;

import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.service.LocationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Test class for LocationService
 */
@SpringBootTest
class LocationServiceTest {

    /**
     * To access the Location database functionality
     */
    @Autowired
    LocationService locationService;

    /**
     * Test if location has been successfully created within the database
     */
    @Test
    void createLocationInDatabaseTest() {
        int initialSize = locationService.getAllLocationInDatabase().size();
        locationService.createLocationInDatabase("Invercargill", "New Zealand");
        assertEquals(locationService.getAllLocationInDatabase().size(), initialSize + 1);
    }

    /**
     * Test if multiple locations has been successfully created within the database
     */
    @Test
    void createMultipleLocationsInDatabaseTest() {
        int initialSize = locationService.getAllLocationInDatabase().size();
        for (int i = 0; i < 5; i++) {
            locationService.createLocationInDatabase("Invercargill", "New Zealand");
        }
        assertEquals(locationService.getAllLocationInDatabase().size(), initialSize + 5);
    }

    /**
     * Test if the mandatory location fields successfully updates
     *
     * @throws Exception if location_id entered does not exist within the Location table
     */
    @Test
    void updateMandatoryInDatabaseTest() throws Exception {
        Location location = locationService.createLocationInDatabase("Invercargill", "New Zealand");
        locationService.updateMandatoryInDatabase(location.getLocationId(), "Seoul", "Korea");
        assertEquals("Seoul",
            locationService.getLocationByLocationId(location.getLocationId()).getCity());
        assertEquals("Korea",
            locationService.getLocationByLocationId(location.getLocationId()).getCountry());
    }

    /**
     * Tests if the optional location fields update successfully
     *
     * @throws Exception if location_id entered does not exist within the Location table
     */
    @Test
    void updateOptionalInDatabaseTest() throws Exception {
        Location location = locationService.createLocationInDatabase("Christchurch", "New Zealand");
        locationService.updateOptionalInDatabase(location.getLocationId(), "James Hight Building",
            "University of Canterbury",
            "Ilam", "8041");
        assertEquals("James Hight Building",
            locationService.getLocationByLocationId(location.getLocationId()).getLine1());
        assertEquals("University of Canterbury",
            locationService.getLocationByLocationId(location.getLocationId()).getLine2());
        assertEquals("Ilam",
            locationService.getLocationByLocationId(location.getLocationId()).getSuburb());
        assertEquals("8041",
            locationService.getLocationByLocationId(location.getLocationId()).getPostcode());
    }

    @Test
    void toStringTest() {
        Location location = locationService.createLocationInDatabase("Delhi", "India");
        String locationString = String.format(location.getCity() + ", " + location.getCountry());
        assertEquals(locationString, location.toString());
    }

    @Test
    void getFullLocationStringTest() {
        Location location = locationService
            .createLocationInDatabase("Taipei", "Taiwan");
        String locationString =
            "location{ line_1=" + location.getLine1() + '\'' + ", line_2=" + location.getLine2()
                + '\'' + ", suburb=" + location.getSuburb() + '\''
                + ", city=" + location.getCity() + '\'' + ", country="
                + location.getCountry()
                + '\'' + ", postcode=" + location.getPostcode() + "}";
        assertEquals(locationString, location.getFullLocationString());
    }
}
