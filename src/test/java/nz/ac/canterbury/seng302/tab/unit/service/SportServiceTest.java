package nz.ac.canterbury.seng302.tab.unit.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import nz.ac.canterbury.seng302.tab.entity.Sport;
import nz.ac.canterbury.seng302.tab.service.SportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class SportServiceTest {

    @Autowired
    SportService sportService;

    /**
     * Test for .createSportInDatabase()
     */
    @Test
    void createSportInDatabaseTest() {
        int initialSize = sportService.getSportResults().size();
        Sport sport = sportService.createSportInDatabase("BloKart");

        assertEquals(initialSize + 1, sportService.getSportResults().size());
        assertEquals("BloKart", sport.getName());
    }

    /**
     * Test for .getSportById()
     */
    @Test
    void getSportByIdTest() throws Exception {
        Sport sport1 = sportService.createSportInDatabase("BloKart");
        Sport sport2 = sportService.createSportInDatabase("BasketBall");

        Sport sportResult1 = sportService.getSportById(sport1.getId());
        Sport sportResult2 = sportService.getSportById(sport2.getId());
        assertEquals(sportResult1.getId(), sport1.getId());
        assertEquals("BloKart", sport1.getName());
        assertEquals(sportResult2.getId(), sport2.getId());
        assertEquals("BasketBall", sport2.getName());
    }

    /**
     * Test for .addNewSports() with a single sport
     */
    @Test
    void addOneNewSport() {
        sportService.addNewSports("Rally");
        List<Sport> sport = sportService.getSportByName("Rally");

        assertNotNull(sport);
        assertEquals("Rally", sport.get(0).getName());
    }

    /**
     * Test for .addNewSports() with multiple sports
     */
    @Test
    void addMultipleNewSports() {
        sportService.addNewSports("Rally, Fencing, Water-polo");

        assertTrue(sportService.sportExists("Rally"));
        assertTrue(sportService.sportExists("Fencing"));
        assertTrue(sportService.sportExists("Water-polo"));
    }

    /**
     * Test for .sportExists()
     */
    @Test
    void getBySportsName() {
        sportService.addNewSports("Rally, Fencing, Water-polo");
        assertTrue(sportService.sportExists("Rally"));
        assertFalse(sportService.sportExists("sportDoesntExist"));
    }
}
