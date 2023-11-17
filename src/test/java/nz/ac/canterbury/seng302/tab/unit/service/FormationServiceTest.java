package nz.ac.canterbury.seng302.tab.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javassist.NotFoundException;
import nz.ac.canterbury.seng302.tab.entity.Formation;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.formobjects.FormationForm;
import nz.ac.canterbury.seng302.tab.repository.FormationRepository;
import nz.ac.canterbury.seng302.tab.service.FormationService;
import nz.ac.canterbury.seng302.tab.service.TeamService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.opentest4j.AssertionFailedError;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Formation Service Class tests
 */
@SpringBootTest
class FormationServiceTest {
    static Team team;
    static Formation formation;
    static Formation formation2;
    static FormationForm formationForm;
    @Mock
    FormationRepository formationRepository;
    @Mock
    TeamService teamService;
    @InjectMocks
    FormationService formServ = new FormationService();

    /**
     * Before ALl set up formations for testing
     */
    @BeforeAll
    static void setUp() {
        team = new Team("team", "team", new Location("team", "team"), "team");
        formation = new Formation("4-2-3-1", "Football", team);
        formation2 = new Formation("4-6-5", "Rugby", team);
        formationForm = new FormationForm();
        formationForm.setFormation("4-3-2-1");
        formationForm.setSport("Football");
    }

    @Test
    void testFindAllFormationsByTeamId_WithFormations_ReturnsFormations() {
        Long teamId = 1L;
        List<Formation> formations = Arrays.asList(formation, formation2);
        when(formationRepository.findAllFormationsByTeamId(teamId)).thenReturn(formations);
        List<Formation> result = formServ.findAllFormationsByTeamId(teamId);
        assertNotNull(result);
        Assertions.assertEquals(2, result.size());
    }

    @Test
    void testFindAllFormationsByTeamId_WithNoFormations_ReturnsEmptyList() {
        Long teamId = 1L;
        when(formationRepository.findAllFormationsByTeamId(teamId)).thenReturn(
            Collections.emptyList());
        List<Formation> result = formServ.findAllFormationsByTeamId(teamId);
        assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testFindAllFormationsByTeamId_WithNonExistentTeam_ReturnsEmptyList() {
        Long teamId = 1L;
        when(formationRepository.findAllFormationsByTeamId(teamId)).thenReturn(null);
        List<Formation> result = formServ.findAllFormationsByTeamId(teamId);
        Assertions.assertNotNull(result);
    }

    @Test
    void testAddFormation_ValidTeamAndFormationForm_ReturnsSavedFormation()
        throws Exception {
        long teamId = 1L;
        when(teamService.getTeamById(teamId)).thenReturn(team);
        when(formationRepository.save(any(Formation.class))).thenReturn(formation);
        Formation result = formServ.addFormation(formationForm, teamId);
        assertNotNull(result);
        assertEquals(formation, result);
    }

    @Test
    void testAddFormation_InvalidTeamId_ThrowsException() throws NotFoundException {
        long teamId = 1L;
        when(teamService.getTeamById(teamId)).thenThrow(new NotFoundException("Invalid team ID"));
        Assertions.assertThrows(NotFoundException.class,
            () -> formServ.addFormation(formationForm, teamId));
    }

    @Test
    void testGetFormationById_FormationExists_ReturnsOptionalWithFormation()
        throws Exception {
        long formationId = 1L;
        when(formationRepository.findById(formationId)).thenReturn(Optional.of(formation));
        Optional<Formation> result = formServ.getFormationById(formationId);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(formation, result.get());

    }

    @Test
    void testGetFormationById_FormationDoesNotExist_ReturnsEmptyOptional() throws Exception {
        long formationId = 1L;
        when(formationRepository.findById(formationId)).thenReturn(Optional.empty());
        Optional<Formation> result = formServ.getFormationById(formationId);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    void testGetFormationById_RepositoryException_ThrowsException() {

        long formationId = 1L;
        when(formationRepository.findById(formationId)).thenThrow(
            new RuntimeException("Repository exception"));
        try {
            formServ.getFormationById(formationId);
        } catch (NotFoundException e) {
            assertEquals("Formation not found.", e.getMessage());
        }

    }

    @Test
    void testFieldImages() {
        List<String> sports = Arrays.asList(
            "Baseball",
            "Basketball",
            "Netball",
            "Cricket",
            "Football",
            "Futsal",
            "Hockey",
            "Rugby",
            "Volleyball"
        );

        List<String> expectedImages = Arrays.asList(
            "images/fields/Baseball.png",
            "images/fields/Basketball.png",
            "images/fields/Netball.jpg",
            "images/fields/Cricket.png",
            "images/fields/Football.jpg",
            "images/fields/Futsal.png",
            "images/fields/Hockey.png",
            "images/fields/Rugby.png",
            "images/fields/Volleyball.jpg"
        );

        for (int i = 0; i < sports.size(); i++) {
            String sport = sports.get(i);
            String expectedImage = expectedImages.get(i);
            String actualImage = formServ.getFormationImage(sport);
            assertEquals(expectedImage, actualImage);
        }
    }

    @Test
    void testDefaultFieldImage() {
        String sport = "UnknownSport";
        String expectedImage = "images/fields/default.jpg";
        String actualImage = formServ.getFormationImage(sport);
        assertEquals(expectedImage, actualImage);
    }

    @Test
    void testExceptionalFlow() {
        String sport = null;
        String expectedImage = "images/fields/default.jpg";
        String actualImage = formServ.getFormationImage(sport);
        assertEquals(expectedImage, actualImage);
    }

    @Test
    void testToString() {
        Assertions.assertEquals(
            "Formation{formation=4-2-3-1, field='Football',"
                + " team='Team{id=null, name='team', sport='team', "
                + "locationID='0'}'}",
            formation.toString());
    }

    /**
     * Write the test for the formation position array to string
     * with the valid test case [["1","2","3"], ["4","5"], ["6","7","8","9"]]
     * -> "1, 2, 3-4, 5-6, 7, 8, 9"
     */
    @Test
    void testPositionStringToArray() {
        FormationService formationService = new FormationService();
        ArrayList<String[]> formationPosition = new ArrayList<>();
        String[] positionString1 = new String[] {"1", "2", "3"};
        String[] positionString2 = new String[] {"4", "5"};
        String[] positionString3 = new String[] {"6", "7", "8", "9"};

        formationPosition.add(positionString1);
        formationPosition.add(positionString2);
        formationPosition.add(positionString3);
        String expected = "1, 2, 3-4, 5-6, 7, 8, 9";
        String positionArray = formationService.positionArrayToString(formationPosition);
        assertEquals(expected, positionArray);
    }
}
