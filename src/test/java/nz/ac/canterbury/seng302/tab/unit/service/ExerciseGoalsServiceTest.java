package nz.ac.canterbury.seng302.tab.unit.service;


import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import nz.ac.canterbury.seng302.tab.entity.ExerciseGoals;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.entity.garminentities.GarminDaily;
import nz.ac.canterbury.seng302.tab.formobjects.ExerciseGoalsForm;
import nz.ac.canterbury.seng302.tab.repository.ExerciseGoalsRepository;
import nz.ac.canterbury.seng302.tab.service.ExerciseGoalsService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.unit.UnitCommonTestSetup;
import nz.ac.canterbury.seng302.tab.utility.TestHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
class ExerciseGoalsServiceTest {

    @Mock
    TabUserService tabUserService;

    @InjectMocks
    private ExerciseGoalsService exerciseGoalsService;

    @Mock
    private ExerciseGoalsRepository exerciseGoalsRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    public static Stream<Arguments> validExerciseGoalFormsAndGarminDaily() {
        return Stream.of(
            Arguments.of(TestHelper.createNormalValidExerciseGoalForm(280f, 18f, 2442, 3.844f),
                TestHelper.createNormalValidGarminDaily(210, 0.3, 1221, 961),
                62),
            Arguments.of(TestHelper.createNormalValidExerciseGoalForm(null, null, null, 5f),
                TestHelper.createNormalValidGarminDaily(300, 1, 2500, 500),
                10),
            Arguments.of(TestHelper.createNormalValidExerciseGoalForm(400f, null, 5000, null),
                TestHelper.createNormalValidGarminDaily(300, 1, 2500, 500),
                62),
            Arguments.of(TestHelper.createNormalValidExerciseGoalForm(400f, null, 5000, 0.1f),
                TestHelper.createNormalValidGarminDaily(300, 1, 2500, 500),
                75)
        );
    }

    public static Stream<Arguments> validExerciseGoalForms() {
        return Stream.of(
                Arguments.of(TestHelper.createNormalValidExerciseGoalForm(1500f, null, null, null),
                        TestHelper.createNormalValidGarminDaily(210, 0, 0, 0),
                        14),
                Arguments.of(TestHelper.createNormalValidExerciseGoalForm(null, 60f, null, null),
                        TestHelper.createNormalValidGarminDaily(0, 0.3, 0, 0),
                        30),
                Arguments.of(TestHelper.createNormalValidExerciseGoalForm(null, null, 3000, null),
                        TestHelper.createNormalValidGarminDaily(0, 0, 300, 0),
                        10),
                Arguments.of(TestHelper.createNormalValidExerciseGoalForm(null, null, null, 5f),
                        TestHelper.createNormalValidGarminDaily(0, 0, 0, 2000),
                        40)
        );
    }

    private static Stream<ExerciseGoalsForm> createValidExerciseGoalForms() {
        return Stream.of(
                TestHelper.createNormalValidExerciseGoalForm(null, 1f, null, null),
                TestHelper.createNormalValidExerciseGoalForm(1f, null, null, null),
                TestHelper.createNormalValidExerciseGoalForm(null, null, 1, null),
                TestHelper.createNormalValidExerciseGoalForm(null, null, null, 1f)
        );
    }

    @Test
    void testUpdateExerciseGoalSuccess() throws IOException {

        ExerciseGoalsForm exerciseGoalsForm = new ExerciseGoalsForm();
        exerciseGoalsForm.setId(1L);
        exerciseGoalsForm.setSteps(1000);
        exerciseGoalsForm.setCaloriesBurnt(500f);
        exerciseGoalsForm.setTotalActivityTime(60f);
        exerciseGoalsForm.setDistanceTravelled(5.0f);
        TabUser user = UnitCommonTestSetup.createTestUser();
        when(tabUserService.getCurrentlyLoggedIn()).thenReturn(user);

        ExerciseGoals mockExerciseGoals = new ExerciseGoals();
        mockExerciseGoals.setId(1L);

        when(exerciseGoalsRepository.findExerciseGoalsById(1L)).thenReturn(mockExerciseGoals);

        when(exerciseGoalsRepository.save(mockExerciseGoals)).thenReturn(mockExerciseGoals);

        ExerciseGoalsForm newExerciseGoalForm = new ExerciseGoalsForm(mockExerciseGoals);
        newExerciseGoalForm.setCaloriesBurnt(350f);
        newExerciseGoalForm.setTotalActivityTime(120f);
        newExerciseGoalForm.setSteps(9000);
        exerciseGoalsForm.setDistanceTravelled(1.0f);

        // Call the method under test
        ExerciseGoals updatedExerciseGoals =
                exerciseGoalsService.updateExerciseGoal(newExerciseGoalForm);

        // Assert that the exerciseGoalsRepository
        // save method was called with the correct ExerciseGoals
        verify(exerciseGoalsRepository).save(updatedExerciseGoals);

        // Assert that the returned ExerciseGoals matches the mockExerciseGoals
        assertEquals(mockExerciseGoals, updatedExerciseGoals);
    }

    @Test
    void testUpdateExerciseGoalNotFound() {
        // Create a mock ExerciseGoalsForm with an ID that won't be found
        ExerciseGoalsForm exerciseGoalsForm = new ExerciseGoalsForm();
        exerciseGoalsForm.setId(2L);

        // Mock repository behavior to return an empty Optional
        when(exerciseGoalsRepository.findById(2L)).thenReturn(Optional.empty());

        // Call the method under test and expect an IOException
        assertThrows(IOException.class, () ->
                exerciseGoalsService.updateExerciseGoal(exerciseGoalsForm));

        // You can also assert the exception message if needed
        try {
            exerciseGoalsService.updateExerciseGoal(exerciseGoalsForm);
            fail("Expected IOException was not thrown.");
        } catch (IOException e) {
            assertEquals("Can not found the exercise goal with the id 2", e.getMessage());
        }
    }

    @ParameterizedTest
    @MethodSource("validExerciseGoalFormsAndGarminDaily")
    void testExerciseGoalProgressCalculation(ExerciseGoalsForm exerciseGoalsForm,
                                             GarminDaily garminDaily, int totalProgress) {
        exerciseGoalsService.calculateGoalProgress(exerciseGoalsForm, garminDaily);
        Assertions.assertEquals(totalProgress, exerciseGoalsForm.getTotalProgress());

    }

    @Test
    void testExerciseGoalFormIsEmpty() {
        ExerciseGoalsForm exerciseGoalsForm = TestHelper.createNormalValidExerciseGoalForm(
                null, null, null, null);
        Assertions.assertTrue(exerciseGoalsForm.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("createValidExerciseGoalForms")
    void testExerciseGoalFormIsNotEmpty(ExerciseGoalsForm exerciseGoalsForm) {
        Assertions.assertFalse(exerciseGoalsForm.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("validExerciseGoalForms")
    void testIndividualExerciseGoalProgress(ExerciseGoalsForm exerciseGoalsForm,
                                             GarminDaily garminDaily, int totalProgress) {
        exerciseGoalsService.calculateGoalProgress(exerciseGoalsForm, garminDaily);
        List<Integer> progressList = new ArrayList<>();
        progressList.add(exerciseGoalsForm.getStepsProgress());
        progressList.add(exerciseGoalsForm.getCaloriesProgress());
        progressList.add(exerciseGoalsForm.getDistanceProgress());
        progressList.add(exerciseGoalsForm.getTimeProgress());
        Assertions.assertTrue(progressList.contains(totalProgress));
        Assertions.assertEquals(1, exerciseGoalsForm.getGoalCount());
        Assertions.assertEquals(3, Collections.frequency(progressList, 0));
    }

}
