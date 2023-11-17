package nz.ac.canterbury.seng302.tab.unit.service;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;
import nz.ac.canterbury.seng302.tab.entity.Formation;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.service.FormationService;
import nz.ac.canterbury.seng302.tab.service.PositionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Tests for the Position service
 */
@SpringBootTest
class PositionServiceTest {

    static Team team;
    static Formation formation;
    static Formation formation2;
    @Mock
    FormationService formationService = new FormationService();
    /**
     * Inject Mocks for the position service
     */
    @InjectMocks
    PositionService positionService = new PositionService();

    /**
     * Before All tests initialise a team, and 2 formations
     */
    @BeforeAll
    static void setUp() {
        team = new Team("team", "team", new Location("team", "team"), "team");
        formation = new Formation("4-2-3-1", "Football", team);
        formation2 = new Formation("4-6-5", "Rugby", team);
    }

    @Test
    void addUserToFormationTest() throws Exception {

        String[] filler = {"1", "2", "3"};
        ArrayList<String[]> array = new ArrayList<>();
        array.add(filler);
        array.add(filler);
        formation.setPosition("1,2,3-1,2,3");
        Long formationId = 2L;
        when(formationService.getFormationById(formationId))
            .thenReturn(Optional.ofNullable(formation));
        int[] position = {0, 0};

        // Call the method
        Long userId = 5L;
        positionService.addUserToFormation(userId, formationId, position);

        // Use Mockito to verify the targetArray variable
        ArgumentCaptor<ArrayList<String[]>> targetArrayCaptor =
            ArgumentCaptor.forClass(ArrayList.class);
        Mockito.verify(formationService).positionArrayToString(targetArrayCaptor.capture());

        ArrayList<String[]> targetArray = targetArrayCaptor.getValue();
        Assertions.assertEquals(userId.toString(), targetArray.get(position[0])[position[1]]);
    }

    @Test
    void removeValidUserFromFormation() throws Exception {
        Long formationId = 2L;

        formation.setPosition("5,2,3-1,2,3");
        when(formationService.getFormationById(formationId))
            .thenReturn(Optional.ofNullable(formation));

        Long userId = 5L;
        // Call the method
        positionService.removeUserFromFormation(userId, formationId);

        // Use Mockito to verify the targetArray variable
        ArgumentCaptor<ArrayList<String[]>> targetArrayCaptor =
            ArgumentCaptor.forClass(ArrayList.class);
        Mockito.verify(formationService).positionArrayToString(targetArrayCaptor.capture());

        ArrayList<String[]> targetArray = targetArrayCaptor.getValue();
        int[] position = {0, 0};
        Assertions.assertEquals("0", targetArray.get(position[0])[position[1]]);
    }

    @Test
    void removeInvalidUserFromFormation() throws Exception {
        String[] filler = {"1", "2", "3"};
        String[] start = {"7", "2", "3"};
        ArrayList<String[]> array = new ArrayList<>();
        array.add(start);
        array.add(filler);
        formation.setPosition("7,2,3-1,2,3");
        long formationId = 2L;
        when(formationService.getFormationById(formationId))
            .thenReturn(Optional.ofNullable(formation));
        Long userId = 5L;
        boolean result = positionService.removeUserFromFormation(userId, formationId);
        Assertions.assertFalse(result);
    }
}
