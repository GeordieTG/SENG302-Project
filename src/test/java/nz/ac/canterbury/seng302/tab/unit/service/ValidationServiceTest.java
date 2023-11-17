package nz.ac.canterbury.seng302.tab.unit.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import javassist.NotFoundException;
import nz.ac.canterbury.seng302.tab.entity.Activity;
import nz.ac.canterbury.seng302.tab.entity.Club;
import nz.ac.canterbury.seng302.tab.entity.GameActivity;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.entity.datatransferobjects.ClubDto;
import nz.ac.canterbury.seng302.tab.entity.enums.ActivityResult;
import nz.ac.canterbury.seng302.tab.entity.enums.SupportedSports;
import nz.ac.canterbury.seng302.tab.entity.garminentities.GarminActivity;
import nz.ac.canterbury.seng302.tab.formobjects.ActivityForm;
import nz.ac.canterbury.seng302.tab.formobjects.ChangePasswordForm;
import nz.ac.canterbury.seng302.tab.formobjects.CreateTeamForm;
import nz.ac.canterbury.seng302.tab.formobjects.EditTeamForm;
import nz.ac.canterbury.seng302.tab.formobjects.ExerciseGoalsForm;
import nz.ac.canterbury.seng302.tab.formobjects.FactForm;
import nz.ac.canterbury.seng302.tab.formobjects.FormationForm;
import nz.ac.canterbury.seng302.tab.formobjects.PlayerScoreForm;
import nz.ac.canterbury.seng302.tab.formobjects.ScoreForm;
import nz.ac.canterbury.seng302.tab.formobjects.StatisticsForm;
import nz.ac.canterbury.seng302.tab.formobjects.SubstitutionForm;
import nz.ac.canterbury.seng302.tab.formobjects.UserEditProfilePageForm;
import nz.ac.canterbury.seng302.tab.formobjects.UserRegistrationForm;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.TeamService;
import nz.ac.canterbury.seng302.tab.service.ValidationService;
import nz.ac.canterbury.seng302.tab.unit.UnitCommonTestSetup;
import nz.ac.canterbury.seng302.tab.utility.TestHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;

/**
 * Ensures that the validation passes/fails accordingly depending on the input
 * of the createActivityForm
 */
@SpringBootTest
@AutoConfigureMockMvc
class ValidationServiceTest {

    static Team team;
    static Activity activity = new Activity();
    static GameActivity activity1 = new GameActivity();
    static UserRegistrationForm userRegistrationForm = new UserRegistrationForm();
    static UserEditProfilePageForm userEditProfilePageForm = new UserEditProfilePageForm();
    static CreateTeamForm createTeamForm = new CreateTeamForm();
    static EditTeamForm editTeamForm = new EditTeamForm();
    @Mock
    TeamService teamService;
    @Mock
    TabUserService tabUserService;
    @InjectMocks
    ValidationService validationService;
    @Mock
    Model model;
    ActivityForm form = new ActivityForm();

    public static Stream<Arguments> invalidStreetAddressSupplier() {
        return Stream.of(
            Arguments.of("String@WithSpecialChars", "String@WithSpecialChars"),
            Arguments.of("TooLongStringExceedingMaxLengthLimit12345678901"
                + "23456789012345678901234567890123456789012345678901234567890", ""),
            Arguments.of("StringWithNewline\nCharacter", "StringWithNewline\nCharacter"),
            Arguments.of("StringWith\tTabCharacter", "StringWith\tTabCharacter"),
            Arguments.of("String@WithSpecialChars", "Allowed"),
            Arguments.of("Allowed", "String@WithSpecialChars")
        );
    }

    public static Stream<Arguments> validStreetAddressSupplier() {
        return Stream.of(
            Arguments.of("", ""),
            Arguments.of("valid", "valid"),
            Arguments.of("123", "123"),
            Arguments.of("ShortString", "ShortString"),
            Arguments.of("822 Coatesville", "Riverhead")
        );
    }

    static Stream<Arguments> invalidScoreFormArguments() {
        return Stream.of(
            Arguments.of(TestHelper.createInvalidTotalPointsForm()),
            Arguments.of(TestHelper.createInvalidTimeStampForm()),
            Arguments.of(TestHelper.createValidNoHomeScoreForm())
        );
    }

    static Stream<Arguments> validExerciseGoals() {
        return Stream.of(
            Arguments.of(TestHelper.createFullValidExerciseGoalForm()),
            Arguments.of(TestHelper.createSubsetValidExerciseGoalForm()),
            Arguments.of(TestHelper.createLowerBoundValidExerciseGoalForm()),
            Arguments.of(TestHelper.createUpperBoundValidExerciseGoalForm())
        );
    }

    @BeforeEach
    void setUp() {

        team = UnitCommonTestSetup.createTestTeam();
        activity.setStartTime("2023-07-31T10:29");
        activity.setEndTime("2023-07-31T11:29");
        activity1.setStartTime("2023-07-31T10:29");
        activity1.setEndTime("2023-07-31T11:29");
        activity1.setPosition("1-2-3");

        userRegistrationForm.setFirstName("Test");
        userRegistrationForm.setLastName("User");
        userRegistrationForm.setDateOfBirth("11-09");
        userRegistrationForm.setAddress1("123 Fake Street");
        userRegistrationForm.setAddress2("Address 2");
        userRegistrationForm.setCountry("Country");
        userRegistrationForm.setCity("Christchurch");
        userRegistrationForm.setPostcode("1234");
        userRegistrationForm.setEmail("test@email.com");
        userRegistrationForm.setPassword("*****");
        userRegistrationForm.setConfirmPassword("*****");
        userRegistrationForm.setSuburb("Suburb");

        userEditProfilePageForm.setFirstName("Test");
        userEditProfilePageForm.setLastName("User");
        userEditProfilePageForm.setDateOfBirth("11-09");
        userEditProfilePageForm.setAddress1("123 Fake Street");
        userEditProfilePageForm.setAddress2("Address 2");
        userEditProfilePageForm.setCountry("Country");
        userEditProfilePageForm.setCity("Christchurch");
        userEditProfilePageForm.setPostcode("1234");
        userEditProfilePageForm.setEmail("test@email.com");
        userEditProfilePageForm.setSuburb("Suburb");

        createTeamForm.setName("Test");
        createTeamForm.setAddress1("123 Fake Street");
        createTeamForm.setAddress2("Address 2");
        createTeamForm.setCountry("Country");
        createTeamForm.setCity("Christchurch");
        createTeamForm.setPostcode("1234");
        createTeamForm.setSuburb("Suburb");
        createTeamForm.setSport(SupportedSports.Football);

        editTeamForm.setName("Test");
        editTeamForm.setAddress1("123 Fake Street");
        editTeamForm.setAddress2("Address 2");
        editTeamForm.setCountry("Country");
        editTeamForm.setCity("Christchurch");
        editTeamForm.setPostcode("1234");
        editTeamForm.setSuburb("Suburb");
        editTeamForm.setSport("Football");

        form.setTeam(-1);
        form.setType("training");
        form.setDescription("Please bring your mouth guard");
        form.setStartTime("2900-03-25T12:00");
        form.setEndTime("2900-03-26T12:00");
        form.setAddress1("Main Road 123");
        form.setPostcode("7805");
        form.setCity("Greymouth");
        form.setCountry("New Zealand");
    }

    /**
     * Ensures that validation passes when a valid form is submitted
     */
    @Test
    void validateCreateActivityFormTestValidNoTeam() {

        Assertions.assertTrue(validationService.validateActivityForm(form, model));
    }

    /**
     * Ensures that validation passes when a valid form is submitted and a team is selected
     */
    @Test
    void validateCreateActivityFormTestValidWithTeam() throws NotFoundException {

        form.setTeam(team.getId());

        when(teamService.getTeamById(anyLong())).thenReturn(team);


        Assertions.assertTrue(validationService.validateActivityForm(form, model));
    }

    /**
     * Ensures that validation fails when no type is selected
     */
    @Test
    void validateCreateActivityFormTestEmptyDescription() {

        form.setDescription("");

        Assertions.assertFalse(validationService.validateActivityForm(form, model));
    }

    /**
     * Ensures that validation passes when the description is exactly 150 characters long
     */
    @Test
    void validateCreateActivityFormTestDescriptionPerfectLength() {

        String perfectLengthText =
            "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget "
                + "dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis p";

        form.setDescription(perfectLengthText);

        Assertions.assertTrue(validationService.validateActivityForm(form, model));
    }

    /**
     * Ensures that validation fails when the description 151 characters long
     */
    @Test
    void validateCreateActivityFormTestDescriptionTooLong() {

        String oneCharacterTooLong =
            "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget "
                + "dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis pp";

        form.setDescription(oneCharacterTooLong);

        Assertions.assertFalse(validationService.validateActivityForm(form, model));
    }

    /**
     * Ensures that validation fails when the description doesn't contain any letters
     */
    @Test
    void validateCreateActivityFormTestDescriptionContainsNoLetters() {

        String noLetters = "123/><>+=|";

        form.setDescription(noLetters);

        Assertions.assertFalse(validationService.validateActivityForm(form, model));
    }

    /**
     * Ensures that validation passes when the description contains a mix of letters and other
     * characters
     */
    @Test
    void validateCreateActivityFormTestDescriptionContainsBothLettersAndSpecialCharacters() {

        String mix = "Hey team we have a game at 12:45pm this Saturday!!";

        form.setDescription(mix);

        Assertions.assertTrue(validationService.validateActivityForm(form, model));
    }

    /**
     * Ensures that validation passes when the description contains new lines
     * / bullet points like a real description might
     */
    @Test
    void validateCreateActivityFormTestDescriptionContainsNewLines() {

        String multiLineBulletPoints = """
            Hey team we have a game at 12:45pm this Saturday!!\s
            please make sure to bring:\s
             - Mouth guard\s
             - Boots""";

        form.setDescription(multiLineBulletPoints);

        Assertions.assertTrue(validationService.validateActivityForm(form, model));
    }

    /**
     * Ensures that validation fails when no start time is entered
     */
    @Test
    void validateCreateActivityFormTestEmptyStartTime() {

        form.setStartTime("");

        Assertions.assertFalse(validationService.validateActivityForm(form, model));
    }

    /**
     * Ensures that validation fails when no end time is entered
     */
    @Test
    void validateCreateActivityFormTestEmptyEndTime() {

        form.setEndTime("");

        Assertions.assertFalse(validationService.validateActivityForm(form, model));
    }

    /**
     * Ensures that validation fails when the end time is prior to the start time
     */
    @Test
    void validateCreateActivityFormTestEndTimePriorToStartTime() {

        form.setStartTime("2900-03-25T12:00");
        form.setEndTime("2900-03-25T11:59");

        Assertions.assertFalse(validationService.validateActivityForm(form, model));
    }

    /**
     * Ensures that validation passes when the end time is the same as the start time
     */
    @Test
    void validateCreateActivityFormTestEndTimeEqualToStartTime() {

        form.setStartTime("2900-03-25T12:00");
        form.setEndTime("2900-03-25T12:00");

        Assertions.assertTrue(validationService.validateActivityForm(form, model));
    }

    /**
     * Ensures that validation fails when the start time is prior to the team creation time
     * (This will fail if you are
     * reading this in the year 2900 but that's not my problem)
     */
    @Test
    void validateCreateActivityFormTestStartTimePriorToTeamCreation() {

        form.setTeam(team.getId());
        form.setStartTime("2003-03-25T12:00");

        Assertions.assertFalse(validationService.validateActivityForm(form, model));
    }

    /**
     * Ensures that validation fails when the end time is prior to the team creation time
     * (This will fail if you are
     * reading this in the year 2900 but that's not my problem)
     */
    @Test
    void validateCreateActivityFormTestEndTimePriorToTeamCreation() {

        form.setTeam(team.getId());
        form.setEndTime("2003-03-25T12:00");

        Assertions.assertFalse(validationService.validateActivityForm(form, model));
    }


    /**
     * Ensures that validation passes if there is a team selected when the type requires a team
     */
    @Test
    void validateCreateActivityFormTestTeamWithGame() throws NotFoundException {

        form.setTeam(team.getId());
        form.setType("Game");

        when(teamService.getTeamById(anyLong())).thenReturn(team);


        Assertions.assertTrue(validationService.validateActivityForm(form, model));
    }

    /**
     * Ensures that validation passes if there is a team selected when the type requires a team
     */
    @Test
    void validateCreateActivityFormTestTeamWithFriendly() throws NotFoundException {

        form.setTeam(team.getId());
        form.setType("Friendly");

        when(teamService.getTeamById(anyLong())).thenReturn(team);

        Assertions.assertTrue(validationService.validateActivityForm(form, model));
    }

    @ParameterizedTest
    @ValueSource(strings = {"none", "Game", "Friendly"})
    void validateCreateActivityFormTest(String activityType) {
        form.setType(activityType);

        boolean validationResult = validationService.validateActivityForm(form, model);

        Assertions.assertFalse(validationResult);
    }

    /**
     * Ensure validation fails when the address isn't entered
     */
    @Test
    void validateCreateActivityFormNoTestNoAddress() {

        form.setAddress1("");

        Assertions.assertFalse(validationService.validateActivityForm(form, model));
    }

    /**
     * Ensure validation fails when the postcode isn't entered
     */
    @Test
    void validateCreateActivityFormNoTestNoPostcode() {

        form.setPostcode("");

        Assertions.assertFalse(validationService.validateActivityForm(form, model));
    }

    /**
     * Ensure validation fails when the city isn't entered
     */
    @Test
    void validateCreateActivityFormNoTestNoCity() {

        form.setCity("");

        Assertions.assertFalse(validationService.validateActivityForm(form, model));
    }

    /**
     * Ensure validation fails when the country isn't entered
     */
    @Test
    void validateCreateActivityFormNoTestNoCountry() {

        form.setCountry("");

        Assertions.assertFalse(validationService.validateActivityForm(form, model));
    }

    /**
     * Ensure validation passes when the form is valid
     */
    @Test
    void validateValidFormationForm() {

        FormationForm formationForm = new FormationForm();
        formationForm.setSport("Football");
        formationForm.setFormation("4-3-3");

        Assertions.assertTrue(validationService.validateFormationForm(formationForm, model));
    }

    /**
     * Ensure validation passes when the formation has a single digit
     */
    @Test
    void validateFormationFormValidFormationSingleDigit() {

        FormationForm formationForm = new FormationForm();
        formationForm.setSport("Football");
        formationForm.setFormation("1");

        Assertions.assertTrue(validationService.validateFormationForm(formationForm, model));
    }

    @ParameterizedTest
    @CsvSource({
        "4-3-3-, Football",
        "-4-3-3, Football",
        "4-3--3, Football",
        "4-3-3-3-3-3-3, Football",
        "4-a-3, Football",
        "411, Football",
        "4-9-0, Football",
        "4-3-3, none",
        "a, Football"
    })
    void validateFormationFormInvalidFormations(String formation, String sport) {
        FormationForm formationForm = new FormationForm();
        formationForm.setSport(sport);
        formationForm.setFormation(formation);
        Assertions.assertFalse(validationService.validateFormationForm(formationForm, model));
    }

    /**
     * Tests a valid user form registration (Mainly for if the regex changes)
     */
    @Test
    void validateRegistrationFormValidValues() {
        UserRegistrationForm userRegForm = new UserRegistrationForm();
        userRegForm.setAddress1("addr1");
        userRegForm.setAddress2("addr2");
        userRegForm.setCity("city");
        userRegForm.setCountry("country");
        userRegForm.setPostcode("8000");
        userRegForm.setFirstName("Test");
        userRegForm.setLastName("Person");
        userRegForm.setEmail("email123456789@email.com");
        userRegForm.setSuburb("suburb");
        userRegForm.setPassword("Password1!");
        userRegForm.setConfirmPassword("Password1!");
        userRegForm.setDateOfBirth(new Date(2000 - 1900, 1, 1).toString());
        Assertions.assertTrue(validationService.validateRegistrationForm(userRegForm, model));

    }

    @Test
    void validatePlayerScoreFormInvalidScore() {
        PlayerScoreForm playerScoreForm = new PlayerScoreForm();
        playerScoreForm.setScore(-6);
        Assertions.assertFalse(validationService.validatePlayerScoreForm(playerScoreForm));
    }


    @Test
    void validatePlayerScoreFormInvalidPlayerId() {
        PlayerScoreForm playerScoreForm = new PlayerScoreForm();
        playerScoreForm.setScoredPlayerId(null);
        Assertions.assertFalse(validationService.validatePlayerScoreForm(playerScoreForm));
    }

    @Test
    void validatePlayerScoreFormInvalidNegativePlayerId() {
        PlayerScoreForm playerScoreForm = new PlayerScoreForm();
        playerScoreForm.setScoredPlayerId(-1L);
        Assertions.assertFalse(validationService.validatePlayerScoreForm(playerScoreForm));
    }

    @Test
    void validatePlayerScoreFormInvalidActivityId() {
        PlayerScoreForm playerScoreForm = new PlayerScoreForm();
        playerScoreForm.setActivityId(-1L);
        Assertions.assertFalse(validationService.validatePlayerScoreForm(playerScoreForm));
    }

    @Test
    void validatePlayerScoreFormInvalidScoreTime() {
        PlayerScoreForm playerScoreForm = new PlayerScoreForm();
        playerScoreForm.setScoreTime(-50);
        Assertions.assertFalse(validationService.validatePlayerScoreForm(playerScoreForm));
    }

    @Test
    void validatePlayerScoreFormValidForm() {
        PlayerScoreForm playerScoreForm = new PlayerScoreForm();
        playerScoreForm.setScoredPlayerId(1L);
        playerScoreForm.setScoreTime(20);
        playerScoreForm.setActivityId(2L);
        playerScoreForm.setScore(3);
        Assertions.assertTrue(validationService.validatePlayerScoreForm(playerScoreForm));
    }

    @Test
    void validateSubstitutionFormInvalidSubstitutedPlayerId() {
        SubstitutionForm substitutionForm = new SubstitutionForm();
        substitutionForm.setSubstitutePlayerId(1L);
        substitutionForm.setSubstitutedPlayerId(-5L);
        Assertions.assertFalse(validationService.validateSubstitutionForm(substitutionForm, model));
    }

    @Test
    void validateSubstitutionFormInvalidSubstitutePlayerId() {
        SubstitutionForm substitutionForm = new SubstitutionForm();
        substitutionForm.setSubstitutePlayerId(-1L);
        substitutionForm.setSubstitutedPlayerId(5L);
        Assertions.assertFalse(validationService.validateSubstitutionForm(substitutionForm, model));
    }

    @Test
    void validateSubstitutionFormInvalidPlayerIds() {
        SubstitutionForm substitutionForm = new SubstitutionForm();
        substitutionForm.setSubstitutePlayerId(-1L);
        substitutionForm.setSubstitutedPlayerId(-5L);
        Assertions.assertFalse(validationService.validateSubstitutionForm(substitutionForm, model));
    }

    @Test
    void validateSubstitutionFormInvalidSubstituteTimeNegative() {
        SubstitutionForm substitutionForm = new SubstitutionForm();
        substitutionForm.setSubstitutePlayerId(1L);
        substitutionForm.setSubstitutedPlayerId(5L);
        substitutionForm.setSubstituteTime(-60);
        Assertions.assertFalse(validationService.validateSubstitutionForm(substitutionForm, model));
    }


    @Test
    void validateSubstitutionFormInvalidSubs() {
        SubstitutionForm substitutionForm = new SubstitutionForm();
        substitutionForm.setSubstitutePlayerId(2L);
        substitutionForm.setSubstitutedPlayerId(2L);
        Assertions.assertFalse(validationService.validateSubstitutionForm(substitutionForm, model));
    }

    @Test
    void validateSubstitutionFormValidForm() {
        SubstitutionForm substitutionForm = new SubstitutionForm();
        substitutionForm.setSubstitutePlayerId(2L);
        substitutionForm.setSubstitutedPlayerId(7L);
        substitutionForm.setSubstituteTime(30);
        Assertions.assertTrue(validationService.validateSubstitutionForm(substitutionForm, model));
    }

    @Test
    void validateStatisticFormInvalidPlayerScoreForm() {
        StatisticsForm statisticsForm = new StatisticsForm();
        ScoreForm scoreForm = new ScoreForm("20",
            1L,
            "10",
            ActivityResult.WON);

        statisticsForm.setScoreForm(scoreForm);

        PlayerScoreForm playerScoreForm = new PlayerScoreForm(1L,
            -2L,
            3,
            20);

        List<PlayerScoreForm> playerScoreForms = new ArrayList<>();
        playerScoreForms.add(playerScoreForm);
        statisticsForm.setPlayerScoreForms(playerScoreForms);

        SubstitutionForm substitutionForm = new SubstitutionForm(1L,
            3L,
            8L,
            45);

        List<SubstitutionForm> substitutionForms = new ArrayList<>();
        substitutionForms.add(substitutionForm);
        statisticsForm.setSubstitutionForms(substitutionForms);
        Assertions.assertFalse(validationService.validateStatisticsForm(statisticsForm, activity1,
            model));
    }

    @Test
    void validateStatisticFormInvalidSubstitutionForm() {
        StatisticsForm statisticsForm = new StatisticsForm();
        ScoreForm scoreForm = new ScoreForm("20",
            1L,
            "10",
            ActivityResult.WON);

        statisticsForm.setScoreForm(scoreForm);

        PlayerScoreForm playerScoreForm = new PlayerScoreForm(1L,
            -2L,
            3,
            20);

        List<PlayerScoreForm> playerScoreForms = new ArrayList<>();
        playerScoreForms.add(playerScoreForm);
        statisticsForm.setPlayerScoreForms(playerScoreForms);

        SubstitutionForm substitutionForm = new SubstitutionForm(1L,
            3L,
            8L,
            -45);
        List<SubstitutionForm> substitutionForms = new ArrayList<>();
        substitutionForms.add(substitutionForm);
        statisticsForm.setSubstitutionForms(substitutionForms);


        Assertions.assertFalse(validationService.validateStatisticsForm(statisticsForm, activity1,
            model));
    }

    @Test
    void validateStatisticFormInvalidScoreForm() {
        StatisticsForm statisticsForm = new StatisticsForm();
        ScoreForm scoreForm = new ScoreForm("-20",
            1L,
            "10",
            null);

        // Mock the validationService
        ValidationService validationService1 = Mockito.mock(ValidationService.class);

        when(validationService1.validatePlayerScoreForm(any(PlayerScoreForm.class)
        )).thenReturn(Boolean.TRUE);
        when(validationService1.validateSubstitutionForm(any(SubstitutionForm.class),
            any(Model.class))).thenReturn(Boolean.TRUE);

        statisticsForm.setScoreForm(scoreForm);
        Assertions.assertFalse(validationService.validateStatisticsForm(statisticsForm, activity1,
            model));
    }

    @Test
    void validateStatisticFormValidForm() {
        StatisticsForm statisticsForm = new StatisticsForm();

        ScoreForm scoreForm = new ScoreForm("25",
            1L,
            "100",
            ActivityResult.LOSS);

        // Mock the validationService
        ValidationService validationService1 = Mockito.mock(ValidationService.class);

        when(validationService1.validatePlayerScoreForm(any(PlayerScoreForm.class)))
            .thenReturn(Boolean.TRUE);
        when(validationService1.validateSubstitutionForm(any(SubstitutionForm.class),
            any(Model.class))).thenReturn(Boolean.TRUE);

        statisticsForm.setScoreForm(scoreForm);
        Assertions.assertTrue(validationService.validateStatisticsForm(statisticsForm, activity1,
            model));
    }

    @Test
    void validateEditTeamForm_invalidSport() {

        EditTeamForm editTeamForm = UnitCommonTestSetup.createTestValidEditTeamForm();
        editTeamForm.setSport("NONE");
        Assertions.assertFalse(validationService.validateEditTeamForm(editTeamForm, model));
    }

    @Test
    void validateClubTeamsIsValid() {
        Team team = UnitCommonTestSetup.createTestTeam();
        team.setSport("Football");
        Club testClub = UnitCommonTestSetup.createTestClub();
        Set<Team> teamList = new HashSet<>();
        teamList.add(team);
        testClub.setTeams(teamList);
        List<Long> teamIds = new ArrayList<>();
        teamIds.add(team.getId());
        ClubDto clubDto = new ClubDto(testClub, teamIds);
        Map<String, String> body = new HashMap<>();
        Assertions.assertTrue(validationService.validateClub(clubDto, body));
    }

    @Test
    void validateClubNullTeams() {
        Club testClub = UnitCommonTestSetup.createTestClub();
        List<Long> teamIds = new ArrayList<>();
        ClubDto clubDto = new ClubDto(testClub, teamIds);
        Map<String, String> body = new HashMap<>();
        Assertions.assertTrue(validationService.validateClub(clubDto, body));
    }

    @Test
    void validateClubNoTeams() {
        List<Long> teamIds = new ArrayList<>();
        Club testClub = UnitCommonTestSetup.createTestClub();
        Set<Team> teamList = new HashSet<>();
        testClub.setTeams(teamList);
        ClubDto clubDto = new ClubDto(testClub, teamIds);
        Map<String, String> body = new HashMap<>();
        Assertions.assertTrue(validationService.validateClub(clubDto, body));
    }

    @Test
    void validateClubTeamsIsNotValid() {
        Team team = UnitCommonTestSetup.createTestTeam();
        Club testClub = UnitCommonTestSetup.createTestClub();
        Set<Team> teamList = new HashSet<>();
        teamList.add(team);
        testClub.setTeams(teamList);
        List<Long> teamIds = new ArrayList<>();
        teamIds.add(team.getId());
        ClubDto clubDto = new ClubDto(testClub, teamIds);

        Map<String, String> body = new HashMap<>();
        Assertions.assertFalse(validationService.validateClub(clubDto, body));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
        "",
        "  ",
        "City@Name",
        "@#$%^&*()",
        "%",
        "1234567890",
        "Invalid Name 123",
        "City\nName",
        "City    Name",
        "City\rName",
        "City\\vName",
        "City\\x1EName",
        "City\\x7FName",
        "-"})
    void validateClubCityIsNotValid(String city) {
        Club testClub = UnitCommonTestSetup.createTestClub();
        Location location = new Location(city, testClub.getLocation().getCountry());
        testClub.setLocation(location);
        ClubDto clubDto = new ClubDto(testClub);
        Map<String, String> body = new HashMap<>();
        Assertions.assertFalse(validationService.validateClub(clubDto, body));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "",
        "  ",
        "City@Name",
        "@#$%^&*()",
        "%",
        "1234567890",
        "Invalid Name 123",
        "City    Name",
        "City\\vName",
        "City\\x1EName",
        "City\\x7FName",
        "-"})
    void validateClubNameIsNotValid(String name) {
        Club testClub = UnitCommonTestSetup.createTestClub();
        testClub.setName(name);
        ClubDto clubDto = new ClubDto(testClub);
        Map<String, String> body = new HashMap<>();
        Assertions.assertFalse(validationService.validateClub(clubDto, body));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "Aalborg",
        "Älmhult",
        "Örebro",
        "Zurich",
        "Łódź",
        "Malmö",
        "Thị Xã Hội An",
        "東京都",
        "北京",
        "القاهرة",
        "TaumatawhakatangihangakoauauoTamateaturipukakapikimaungahoronukupokaiwhenuakitanatahu",
        "Y", //City in alaska
        "San-Fran"})
    void validateClubCityIsValid(String city) {
        Club testClub = UnitCommonTestSetup.createTestClub();
        Location location = new Location(city, testClub.getLocation().getCountry());
        testClub.setLocation(location);
        ClubDto clubDto = new ClubDto(testClub);
        Map<String, String> body = new HashMap<>();
        Assertions.assertTrue(validationService.validateClub(clubDto, body));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
        "",
        "  ",
        "Country@Name",
        "@#$%^&*()",
        "%",
        "1234567890",
        "Invalid Name 123",
        "Country\nName",
        "Country    Name",
        "Country\rName",
        "Country\\vName",
        "Country\\x1EName",
        "Country\\x7FName",
        "''",
        "-"})
    void validateClubCountryIsNotValid(String country) {
        Club testClub = UnitCommonTestSetup.createTestClub();
        Location location = new Location(testClub.getLocation().getCity(), country);
        testClub.setLocation(location);
        ClubDto clubDto = new ClubDto(testClub);
        Map<String, String> body = new HashMap<>();
        Assertions.assertFalse(validationService.validateClub(clubDto, body));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Switzerland",
        "New Zealand",
        "日本",
        "The United Kingdom of Great Britain and Northern Ireland",
        "Timor-Leste",
        "São Tomé and Príncipe",
        "Democratic People's Republic of Korea",
        "Côte d'Ivoire"})
    void validateClubCountryIsValid(String country) {
        Club testClub = UnitCommonTestSetup.createTestClub();
        Location location = new Location(testClub.getLocation().getCity(), country);
        testClub.setLocation(location);
        ClubDto clubDto = new ClubDto(testClub);
        Map<String, String> body = new HashMap<>();
        Assertions.assertTrue(validationService.validateClub(clubDto, body));
    }

    @Test
    void validateFactsFormIsValid() {
        List<String> descriptions = List.of("D Neal Injured", "L J Cuthbert goes over the bar");
        List<FactForm> factForms = new ArrayList<>();
        StatisticsForm statisticsForm = new StatisticsForm();
        for (String description : descriptions) {
            FactForm factForm = new FactForm(1L, description,
                1 + descriptions.indexOf(description));
            factForms.add(factForm);
        }
        statisticsForm.setFactForms(factForms);
        Assertions.assertTrue(validationService.validateFactForms(statisticsForm, model));
    }

    @Test
    void validateFactsFormInvalidTime() {
        StatisticsForm statisticsForm = new StatisticsForm();
        statisticsForm.setFactForms(List.of(new FactForm(1L, "L J Cuthbert goes"
            + " over the bar", -1)));
        Assertions.assertFalse(validationService.validateFactForms(statisticsForm, model));
    }

    @Test
    void validateFactsFormNoTime() {
        StatisticsForm statisticsForm = new StatisticsForm();
        statisticsForm.setFactForms(List.of(new FactForm(1L, "L J Cuthbert goes"
            + " over the bar", null)));
        Assertions.assertFalse(validationService.validateFactForms(statisticsForm, model));
    }

    @Test
    void validateFactsFormNoDescription() {
        StatisticsForm statisticsForm = new StatisticsForm();
        statisticsForm.setFactForms(List.of(new FactForm(1L, "", 1)));
        Assertions.assertFalse(validationService.validateFactForms(statisticsForm, model));
    }

    @Test
    void validateFactsFormRemovesBlankForms() {
        List<String> descriptions = List.of("D Neal Injured", "L J Cuthbert goes over the bar");
        List<FactForm> factForms = new ArrayList<>();
        StatisticsForm statisticsForm = new StatisticsForm();
        for (String description : descriptions) {
            FactForm factForm = new FactForm(1L, description,
                1 + descriptions.indexOf(description));
            factForms.add(factForm);
            FactForm blankForm = new FactForm(1L, "", null);
            factForms.add(blankForm);
        }
        statisticsForm.setFactForms(factForms);
        int original = statisticsForm.getFactForms().size();
        validationService.validateFactForms(statisticsForm, model);
        Assertions.assertTrue(original > statisticsForm.getFactForms().size());
    }

    @Test
    void validateInvalidTeamName() {
        Assertions.assertFalse(ValidationService.validateName("Test-Team", model));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
        "",
        "  ",
        "City@Name",
        "@#$%^&*()",
        "%",
        "1234567890",
        "Invalid Name 123",
        "City\nName",
        "City    Name",
        "City\rName",
        "City\\vName",
        "City\\x1EName",
        "City\\x7FName",
        "-"})
    void validateInvalidCity(String city) {
        Assertions.assertFalse(validationService.checkCity(city, model));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
        "",
        "  ",
        "Country@Name",
        "@#$%^&*()",
        "%",
        "1234567890",
        "Invalid Name 123",
        "Country\nName",
        "Country    Name",
        "Country\rName",
        "Country\\vName",
        "Country\\x1EName",
        "Country\\x7FName",
        "''",
        "-"})
    void validateInvalidCountry(String country) {
        Assertions.assertFalse(validationService.checkCountry(country, model));
    }

    @ParameterizedTest
    @MethodSource("invalidStreetAddressSupplier")
    void validateInvalidStreetAddress(String line1, String line2) {
        Assertions.assertFalse(validationService.checkStreetAddress(line1, line2, model));
    }

    @ParameterizedTest
    @MethodSource("validStreetAddressSupplier")
    void validateValidStreetAddress(String line1, String line2) {
        Assertions.assertTrue(validationService.checkStreetAddress(line1, line2, model));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
        "Valid_String-1234",
        "Special!Chars",
        "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
        "long_string_with_no_hyphen",
        "Testing\\nNewline",
        "test\\ttab",
    })
    void validateInvalidPostcode(String postcode) {
        Assertions.assertFalse(validationService.checkPostcode(postcode, model));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
        "Valid_String-1234",
        "Special!Chars",
        "long_string_with_no_hyphen",
        "Testing\\nNewline",
        "test\\ttab",
    })
    void validateInvalidSuburb(String postcode) {
        Assertions.assertFalse(validationService.checkSuburb(postcode, model));
    }

    @Test
    void validateValidScoreForm() {
        StatisticsForm statisticsForm = new StatisticsForm();
        statisticsForm.setPlayerScoreForms(List.of(new PlayerScoreForm(1L, 1L,
            1, 1)));
        statisticsForm.setScoreForm(new ScoreForm("1", 1L, "1",
            ActivityResult.WON));
        Assertions.assertTrue(validationService.validateScoreForms(statisticsForm, activity,
            model));
    }

    @Test
    void validateInvalidScoreFormNoPlayer() {
        StatisticsForm statisticsForm = new StatisticsForm();
        statisticsForm.setPlayerScoreForms(List.of(new PlayerScoreForm(1L, -1L,
            1, 1)));
        statisticsForm.setScoreForm(new ScoreForm("1", 1L, "1",
            ActivityResult.WON));
        Assertions.assertFalse(validationService.validateScoreForms(statisticsForm, activity,
            model));
    }



    @Test
    void validateInvalidScoreFormTooManyPoint() {
        StatisticsForm statisticsForm = new StatisticsForm();
        statisticsForm.setPlayerScoreForms(List.of(new PlayerScoreForm(1L, 1L,
            1, 1)));

        statisticsForm.setScoreForm(new ScoreForm(
            "10000000000000000000000000000000000000000000000000000000000000000000000000000000000",
            1L, "1",
            ActivityResult.WON));
        Assertions.assertFalse(validationService.validateScoreForms(statisticsForm, activity,
            model));
    }

    @Test
    void validateInvalidScoreFormNegativeTime() {
        StatisticsForm statisticsForm = new StatisticsForm();
        statisticsForm.setPlayerScoreForms(List.of(new PlayerScoreForm(1L, 1L,
            1, -1)));
        statisticsForm.setScoreForm(new ScoreForm("1", 1L, "1",
            ActivityResult.WON));
        Assertions.assertFalse(validationService.validateScoreForms(statisticsForm, activity,
            model));
    }

    @Test
    void validateInvalidScoreFormNegativeAmount() {
        StatisticsForm statisticsForm = new StatisticsForm();
        statisticsForm.setPlayerScoreForms(List.of(new PlayerScoreForm(
            1L, 1L, -1, 1)));
        statisticsForm.setScoreForm(new ScoreForm("1", 1L, "1",
            ActivityResult.WON));
        Assertions.assertFalse(validationService.validateScoreForms(statisticsForm, activity,
            model));
    }

    @ParameterizedTest
    @MethodSource("invalidScoreFormArguments")
    void validateInvalidScoreForms(StatisticsForm statisticsForm) {
        Assertions.assertFalse(validationService.validateScoreForms(
            statisticsForm, activity, model));
    }

    @Test
    void validateInvalidAddFoodForm() {
        Assertions.assertFalse(validationService.validateAddFoodForm(
            1L, -1L, -1L));
    }

    @Test
    void validateValidAddFoodForm() {
        Assertions.assertTrue(validationService.validateAddFoodForm(
            1L, 1L, 1L));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "1",
        "a12",
        "!Chars",
        "long_string_with_no_hyphen",
        "Testing\\nNewline",
        "test\\ttab",
        "hello there"
    })
    void validateOnlyLettersTest_Invalid(String text) {
        Assertions.assertFalse(ValidationService.validateOnlyLetters(text, model));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "11-09",
        "11",
        "-",
        "abc"
    })
    void validateAge_InvalidByLength(String text) {
        Assertions.assertFalse(validationService.checkAge(text, model));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "abc-def-fgh",
        "a-c-b",
        "a-1-r"
    })
    void validateAge_InvalidByNumber(String text) {
        Assertions.assertFalse(validationService.checkAge(text, model));
    }

    @Test
    void validateInvalidRegistrationForm_FirstName() {
        userRegistrationForm.setFirstName("@@");
        Assertions.assertFalse(validationService.validateRegistrationForm(
            userRegistrationForm, model));
    }

    @Test
    void validateInvalidRegistrationForm_LastName() {
        userRegistrationForm.setLastName("@@");
        Assertions.assertFalse(validationService.validateRegistrationForm(
            userRegistrationForm, model));
    }

    @Test
    void validateInvalidRegistrationForm_Email() {
        userRegistrationForm.setEmail("@.com");
        Assertions.assertFalse(validationService.validateRegistrationForm(
            userRegistrationForm, model));
    }

    @Test
    void validateInvalidRegistrationForm_DateOfBirth() {
        userRegistrationForm.setDateOfBirth("10-10-2020");
        Assertions.assertFalse(validationService.validateRegistrationForm(
            userRegistrationForm, model));
    }

    @Test
    void validateInvalidRegistrationForm_Address() {
        userRegistrationForm.setAddress1("!?-_-!?");
        userRegistrationForm.setAddress1(">:(");
        Assertions.assertFalse(validationService.validateRegistrationForm(
            userRegistrationForm, model));
    }

    @Test
    void validateInvalidRegistrationForm_Suburb() {
        userRegistrationForm.setSuburb("!?-_-!?");
        Assertions.assertFalse(validationService.validateRegistrationForm(
            userRegistrationForm, model));
    }

    @Test
    void validateInvalidRegistrationForm_Postcode() {
        userRegistrationForm.setPostcode("!?-_-!?");
        Assertions.assertFalse(validationService.validateRegistrationForm(
            userRegistrationForm, model));
    }

    @Test
    void validateInvalidRegistrationForm_Country() {
        userRegistrationForm.setCountry("!?-_-!?");
        Assertions.assertFalse(validationService.validateRegistrationForm(
            userRegistrationForm, model));
    }

    @Test
    void validateInvalidRegistrationForm_City() {
        userRegistrationForm.setCity("!?-_-!?");
        Assertions.assertFalse(validationService.validateRegistrationForm(
            userRegistrationForm, model));
    }

    @Test
    void validateInvalidRegistrationForm_MatchingPassword() {
        userRegistrationForm.setPassword("!?-_-!?");
        userRegistrationForm.setConfirmPassword("!?!?");
        Assertions.assertFalse(validationService.validateRegistrationForm(
            userRegistrationForm, model));
    }

    @Test
    void validateInvalidRegistrationForm_PasswordStrength() {
        userRegistrationForm.setPassword("a");
        Assertions.assertFalse(validationService.validateRegistrationForm(
            userRegistrationForm, model));
    }

    @Test
    void validateInvalidUserEditProfilePageForm_FirstName() {
        userEditProfilePageForm.setFirstName("@@");
        Assertions.assertFalse(validationService.validateEditUserProfileForm(
            userEditProfilePageForm, "email@email.com", model));
    }

    @Test
    void validateInvalidUserEditProfilePageForm_LastName() {
        userEditProfilePageForm.setLastName("@@");
        Assertions.assertFalse(validationService.validateEditUserProfileForm(
            userEditProfilePageForm, "email@email.com", model));
    }

    @Test
    void validateInvalidUserEditProfilePageForm_Email() {
        userEditProfilePageForm.setEmail("@.com");
        Assertions.assertFalse(validationService.validateEditUserProfileForm(
            userEditProfilePageForm, "email@email.com", model));
    }

    @Test
    void validateInvalidUserEditProfilePageForm_DateOfBirth() {
        userEditProfilePageForm.setDateOfBirth("10-10-2020");
        Assertions.assertFalse(validationService.validateEditUserProfileForm(
            userEditProfilePageForm, "email@email.com", model));
    }

    @Test
    void validateInvalidUserEditProfilePageForm_Address() {
        userEditProfilePageForm.setAddress1("!?-_-!?");
        userEditProfilePageForm.setAddress1(">:(");
        Assertions.assertFalse(validationService.validateEditUserProfileForm(
            userEditProfilePageForm, "email@email.com", model));
    }

    @Test
    void validateInvalidUserEditProfilePageForm_Suburb() {
        userEditProfilePageForm.setSuburb("!?-_-!?");
        Assertions.assertFalse(validationService.validateEditUserProfileForm(
            userEditProfilePageForm, "email@email.com", model));
    }

    @Test
    void validateInvalidUserEditProfilePageForm_Postcode() {
        userEditProfilePageForm.setPostcode("!?-_-!?");
        Assertions.assertFalse(validationService.validateEditUserProfileForm(
            userEditProfilePageForm, "email@email.com", model));
    }

    @Test
    void validateInvalidUserEditProfilePageForm_Country() {
        userEditProfilePageForm.setCountry("!?-_-!?");
        Assertions.assertFalse(validationService.validateEditUserProfileForm(
            userEditProfilePageForm, "email@email.com", model));
    }

    @Test
    void validateInvalidUserEditProfilePageForm_City() {
        userEditProfilePageForm.setCity("!?-_-!?");
        Assertions.assertFalse(validationService.validateEditUserProfileForm(
            userEditProfilePageForm, "email@email.com", model));
    }

    @Test
    void validateInvalidCreateTeamForm_FirstName() {
        createTeamForm.setName("@@");
        Assertions.assertFalse(validationService.validateCreateTeamForm(
            createTeamForm, model));
    }

    @Test
    void validateInvalidCreateTeamForm_Address() {
        createTeamForm.setAddress1("!?-_-!?");
        createTeamForm.setAddress1(">:(");
        Assertions.assertFalse(validationService.validateCreateTeamForm(
            createTeamForm, model));
    }

    @Test
    void validateInvalidCreateTeamForm_Suburb() {
        createTeamForm.setSuburb("!?-_-!?");
        Assertions.assertFalse(validationService.validateCreateTeamForm(
            createTeamForm, model));
    }

    @Test
    void validateInvalidCreateTeamForm_Postcode() {
        createTeamForm.setPostcode("!?-_-!?");
        Assertions.assertFalse(validationService.validateCreateTeamForm(
            createTeamForm, model));
    }

    @Test
    void validateInvalidCreateTeamForm_Country() {
        createTeamForm.setCountry("!?-_-!?");
        Assertions.assertFalse(validationService.validateCreateTeamForm(
            createTeamForm, model));
    }

    @Test
    void validateInvalidCreateTeamForm_City() {
        createTeamForm.setCity("!?-_-!?");
        Assertions.assertFalse(validationService.validateCreateTeamForm(
            createTeamForm, model));
    }

    @Test
    void validateInvalidCreateTeamForm_NoSport() {
        createTeamForm.setSport(SupportedSports.NONE);
        Assertions.assertFalse(validationService.validateCreateTeamForm(
            createTeamForm, model));
    }

    @Test
    void validateInvalidEditTeamForm_Name() {
        editTeamForm.setName("@@");
        Assertions.assertFalse(validationService.validateEditTeamForm(
            editTeamForm, model));
    }

    @Test
    void validateInvalidEditTeamForm_Address() {
        editTeamForm.setAddress1("!?-_-!?");
        editTeamForm.setAddress1(">:(");
        Assertions.assertFalse(validationService.validateEditTeamForm(
            editTeamForm, model));
    }

    @Test
    void validateInvalidEditTeamForm_Suburb() {
        editTeamForm.setSuburb("!?-_-!?");
        Assertions.assertFalse(validationService.validateEditTeamForm(
            editTeamForm, model));
    }

    @Test
    void validateInvalidEditTeamForm_Postcode() {
        editTeamForm.setPostcode("!?-_-!?");
        Assertions.assertFalse(validationService.validateEditTeamForm(
            editTeamForm, model));
    }

    @Test
    void validateInvalidEditTeamForm_Country() {
        editTeamForm.setCountry("!?-_-!?");
        Assertions.assertFalse(validationService.validateEditTeamForm(
            editTeamForm, model));
    }

    @Test
    void validateInvalidEditTeamForm_City() {
        editTeamForm.setCity("!?-_-!?");
        Assertions.assertFalse(validationService.validateEditTeamForm(
            editTeamForm, model));
    }

    @Test
    void validateChangePasswordForm() {
        ChangePasswordForm changePasswordForm = new ChangePasswordForm();
        changePasswordForm.setOldPassword("*****");
        changePasswordForm.setPassword("Qwerty10!");
        changePasswordForm.setConfirmPassword("Qwerty10!");
        when(tabUserService.getCurrentlyLoggedIn()).thenReturn(
            UnitCommonTestSetup.createTestUser());
        Assertions.assertTrue(validationService.validateChangePasswordForm(changePasswordForm,
            model));
    }

    @Test
    void validateScoreForms_Valid() {
        StatisticsForm statisticsForm = new StatisticsForm();
        statisticsForm.setScoreForm(new ScoreForm("2", 1L, "1",
            ActivityResult.WON));
        Assertions.assertTrue(validationService.validateScoreForm(statisticsForm, model));
    }

    @Test
    void validateScoreForms_ValidDashScores() {
        StatisticsForm statisticsForm = new StatisticsForm();
        statisticsForm.setScoreForm(new ScoreForm("2-1", 1L, "1-1",
            ActivityResult.WON));
        Assertions.assertTrue(validationService.validateScoreForm(statisticsForm, model));
    }

    @Test
    void validateScoreForms_InvalidScore() {
        StatisticsForm statisticsForm = new StatisticsForm();
        statisticsForm.setScoreForm(new ScoreForm("2-", 1L, "1",
            ActivityResult.WON));
        Assertions.assertFalse((validationService.validateScoreForm(statisticsForm, model)));
    }

    @Test
    void validateScoreForms_EmptyHomeScore() {
        StatisticsForm statisticsForm = new StatisticsForm();
        statisticsForm.setScoreForm(new ScoreForm("", 1L, "2",
            ActivityResult.WON));
        Assertions.assertFalse((validationService.validateScoreForm(statisticsForm, model)));
    }

    @Test
    void validateScoreForms_EmptyOppScore() {
        StatisticsForm statisticsForm = new StatisticsForm();
        statisticsForm.setScoreForm(new ScoreForm("1", 1L, "",
            ActivityResult.WON));
        Assertions.assertFalse((validationService.validateScoreForm(statisticsForm, model)));
    }

    @Test
    void validateScoreForms_NullResult() {
        StatisticsForm statisticsForm = new StatisticsForm();
        statisticsForm.setScoreForm(new ScoreForm("1", 1L, "",
            null));
        Assertions.assertFalse((validationService.validateScoreForm(statisticsForm, model)));
    }

    @Test
    void validateScoreForms_Undecided() {
        StatisticsForm statisticsForm = new StatisticsForm();
        statisticsForm.setScoreForm(new ScoreForm("1", 1L, "",
            ActivityResult.UNDECIDED));
        Assertions.assertFalse((validationService.validateScoreForm(statisticsForm, model)));
    }

    @ParameterizedTest
    @MethodSource("validExerciseGoals")
    void validateExerciseGoalForm_Valid(ExerciseGoalsForm exerciseGoalsForm) {
        Assertions.assertTrue(validationService.validateExerciseGoalForm(exerciseGoalsForm));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "-17",
        "0",
        "150001"
    })
    void validateExerciseGoalForm_InvalidSteps(String text) {
        ExerciseGoalsForm exerciseGoalsForm = new ExerciseGoalsForm();
        exerciseGoalsForm.setSteps(Integer.valueOf(text));
        exerciseGoalsForm.setCaloriesBurnt(798f);
        exerciseGoalsForm.setDistanceTravelled(1.6f);
        exerciseGoalsForm.setTotalActivityTime(45f);
        Assertions.assertFalse(validationService.validateExerciseGoalForm(exerciseGoalsForm));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "-17",
        "0",
        "50001"
    })
    void validateExerciseGoalForm_InvalidCalories(String text) {
        ExerciseGoalsForm exerciseGoalsForm = new ExerciseGoalsForm();
        exerciseGoalsForm.setSteps(3645);
        exerciseGoalsForm.setCaloriesBurnt(Float.valueOf(text));
        exerciseGoalsForm.setDistanceTravelled(1.6f);
        exerciseGoalsForm.setTotalActivityTime(45f);
        Assertions.assertFalse(validationService.validateExerciseGoalForm(exerciseGoalsForm));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "-17",
        "0",
        "501"
    })
    void validateExerciseGoalForm_InvalidDistance(String text) {
        ExerciseGoalsForm exerciseGoalsForm = new ExerciseGoalsForm();
        exerciseGoalsForm.setSteps(3645);
        exerciseGoalsForm.setCaloriesBurnt(798f);
        exerciseGoalsForm.setDistanceTravelled(Float.valueOf(text));
        exerciseGoalsForm.setTotalActivityTime(45f);
        Assertions.assertFalse(validationService.validateExerciseGoalForm(exerciseGoalsForm));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "-17",
        "0.9",
        "0",
        "1441"
    })
    void validateExerciseGoalForm_InvalidTime(String text) {
        ExerciseGoalsForm exerciseGoalsForm = new ExerciseGoalsForm();
        exerciseGoalsForm.setSteps(3645);
        exerciseGoalsForm.setCaloriesBurnt(798f);
        exerciseGoalsForm.setDistanceTravelled(1.6f);
        exerciseGoalsForm.setTotalActivityTime(Float.valueOf(text));
        Assertions.assertFalse(validationService.validateExerciseGoalForm(exerciseGoalsForm));
    }

    @Test
    void validateGarminActivity_InvalidActivity() {
        GarminActivity garminActivity = UnitCommonTestSetup.createGarminActivity();
        garminActivity.setActivity(null);
        List<String> result = validationService.validateGarminActivity(garminActivity);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1,
            result.size());
        Assertions.assertEquals("Activity is null",
            result.get(0));
    }

    @ParameterizedTest
    @ValueSource(ints =
        {15,
            20,
            1, }
    )
    void validateGarminActivity_validDurationInSeconds(int durationInSeconds) {
        GarminActivity garminActivity = UnitCommonTestSetup.createGarminActivity();
        garminActivity.setDurationInSeconds(durationInSeconds);
        List<String> result = validationService.validateGarminActivity(garminActivity);
        Assertions.assertTrue(result.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(ints =
        {-15,
            -20,
            -1, }
    )
    void validateGarminActivity_invalidDurationInSeconds(int durationInSeconds) {
        GarminActivity garminActivity = UnitCommonTestSetup.createGarminActivity();
        garminActivity.setDurationInSeconds(durationInSeconds);
        List<String> result = validationService.validateGarminActivity(garminActivity);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1,
            result.size());
        Assertions.assertEquals("Duration must be greater than or equal to 0",
            result.get(0));
    }

    @ParameterizedTest
    @ValueSource(ints =
        {15,
            20,
            1, }
    )
    void validateGarminActivity_validDistanceInMetres(int distanceInMetres) {
        GarminActivity garminActivity = UnitCommonTestSetup.createGarminActivity();
        garminActivity.setDistanceInMeters(distanceInMetres);
        List<String> result = validationService.validateGarminActivity(garminActivity);
        Assertions.assertTrue(result.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(ints =
        {-15,
            -20,
            -1, }
    )
    void validateGarminActivity_invalidDistanceInMetres(int distanceInMetres) {
        GarminActivity garminActivity = UnitCommonTestSetup.createGarminActivity();
        garminActivity.setDistanceInMeters(distanceInMetres);
        List<String> result = validationService.validateGarminActivity(garminActivity);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1,
            result.size());
        Assertions.assertEquals("Distance must be greater than or equal to 0",
            result.get(0));
    }

    @ParameterizedTest
    @ValueSource(ints =
        {15,
            20,
            1, }
    )
    void validateGarminActivity_validActiveCalories(int activeCalories) {
        GarminActivity garminActivity = UnitCommonTestSetup.createGarminActivity();
        garminActivity.setActiveKilocalories(activeCalories);
        List<String> result = validationService.validateGarminActivity(garminActivity);
        Assertions.assertTrue(result.isEmpty());

    }

    @ParameterizedTest
    @ValueSource(ints =
        {-15,
            -20,
            -1, }
    )
    void validateGarminActivity_invalidActiveCalories(int activeCalories) {
        GarminActivity garminActivity = UnitCommonTestSetup.createGarminActivity();
        garminActivity.setActiveKilocalories(activeCalories);
        List<String> result = validationService.validateGarminActivity(garminActivity);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1,
            result.size());
        Assertions.assertEquals("Calories burnt must be greater than or equal to 0",
            result.get(0));
    }
}
