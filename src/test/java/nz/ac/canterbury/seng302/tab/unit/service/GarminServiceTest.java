package nz.ac.canterbury.seng302.tab.unit.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.oauth.OAuth10aService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ExecutionException;
import nz.ac.canterbury.seng302.tab.entity.GarminAccessToken;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.entity.garminentities.GarminActivity;
import nz.ac.canterbury.seng302.tab.entity.garminentities.GarminDaily;
import nz.ac.canterbury.seng302.tab.exception.GarminPermissionException;
import nz.ac.canterbury.seng302.tab.repository.GarminAccessTokenRepository;
import nz.ac.canterbury.seng302.tab.service.GarminService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.unit.UnitCommonTestSetup;
import nz.ac.canterbury.seng302.tab.utility.Encryption;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

/**
 * Test for the Garmin Service
 */
@SpringBootTest
class GarminServiceTest {

    @Mock
    GarminAccessTokenRepository garminAccessTokenRepository;

    @Mock
    TabUserService tabUserService;

    @Mock
    OAuth10aService oauth10aService;

    @Mock
    Encryption encryption;

    @InjectMocks
    GarminService garminService;

    String jsonFilePath = "src/test/resources/jsontestfiles/JsonDailies.json";
    String emptyJsonFilePath = "src/test/resources/jsontestfiles/EmptyJson.json";
    String garminActivityPath = "src/test/resources/jsontestfiles/GarminActivity.json";
    String jsonContent = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
    String emptyJson = new String(Files.readAllBytes(Paths.get(emptyJsonFilePath)));

    String garminActivity = new String(Files.readAllBytes(Paths.get(garminActivityPath)));

    public GarminServiceTest() throws IOException {
    }

    @Test
    void checkUserNotConnectedTest() {
        TabUser currentlyLoggedIn = UnitCommonTestSetup.createTestUser();
        when(garminAccessTokenRepository.checkIfConnected(currentlyLoggedIn.getId()))
            .thenReturn(null);
        String bool = garminService.fetchDailies(currentlyLoggedIn, new ConcurrentModel());
        Assertions.assertEquals("False", bool);
    }

    @Test
    void checkUserConnectedTest() throws IOException, ExecutionException, InterruptedException {
        TabUser currentlyLoggedIn = UnitCommonTestSetup.createTestUser();
        GarminAccessToken garminAccessToken = new GarminAccessToken(currentlyLoggedIn);
        garminAccessToken.setSecretToken("123789123678912345678912345678");
        garminAccessToken.setToken("123789123678912345678912345678");
        // Mocking for the get most recent daily as mocking the actual call didn't work
        when(tabUserService.getCurrentlyLoggedIn()).thenReturn(currentlyLoggedIn);
        when(garminAccessTokenRepository.checkIfConnected(currentlyLoggedIn.getId()))
                .thenReturn(garminAccessToken);
        when(encryption.decrypt(anyString())).thenReturn(garminAccessToken.getSecretToken());
        // Mocking the response with a Response object
        ObjectMapper objectMapper = new ObjectMapper();

        // Parse the JSON array
        JsonNode jsonArray = objectMapper.readTree(jsonContent);
        String modifiedJsonString = jsonContent;
        // Check if the parsed JSON is an array and contains at least one element
        if (jsonArray.isArray() && jsonArray.size() > 0) {
            // Get the first element in the array
            JsonNode firstObject = jsonArray.get(0);
            // Modify the "startTimeInSeconds" field with the current epoch time
            long currentEpochTime = Instant.now().getEpochSecond();
            ((ObjectNode) firstObject).put("startTimeInSeconds", currentEpochTime);
            // Convert the modified JSON array back to a string
            modifiedJsonString = jsonArray.toString();
        }
        Response response = new Response(200, "", null, modifiedJsonString);
        when(oauth10aService.execute(any())).thenReturn(response);

        Model testModel = new ConcurrentModel();
        String bool = garminService.fetchDailies(currentlyLoggedIn, testModel);
        Assertions.assertEquals("True", bool);
        Assertions.assertNotNull(testModel.getAttribute("garminDaily"));
    }

    @Test
    void mostRecentDailyTest()
        throws IOException, ExecutionException, InterruptedException, GarminPermissionException {
        TabUser currentlyLoggedIn = UnitCommonTestSetup.createTestUser();
        GarminAccessToken garminAccessToken = new GarminAccessToken(currentlyLoggedIn);
        garminAccessToken.setSecretToken("123789123678912345678912345678");
        garminAccessToken.setToken("123789123678912345678912345678");
        when(tabUserService.getCurrentlyLoggedIn()).thenReturn(currentlyLoggedIn);
        when(garminAccessTokenRepository.checkIfConnected(currentlyLoggedIn.getId()))
            .thenReturn(garminAccessToken);
        when(encryption.decrypt(anyString())).thenReturn(garminAccessToken.getSecretToken());
        // Mocking the response with a Response object
        ObjectMapper objectMapper = new ObjectMapper();

        // Parse the JSON array
        JsonNode jsonArray = objectMapper.readTree(jsonContent);
        String modifiedJsonString = jsonContent;
        // Check if the parsed JSON is an array and contains at least one element
        if (jsonArray.isArray() && jsonArray.size() > 0) {
            // Get the first element in the array
            JsonNode firstObject = jsonArray.get(0);
            // Modify the "startTimeInSeconds" field with the current epoch time
            long currentEpochTime = Instant.now().getEpochSecond();
            ((ObjectNode) firstObject).put("startTimeInSeconds", currentEpochTime);
            // Convert the modified JSON array back to a string
            modifiedJsonString = jsonArray.toString();
        }
        Response response = new Response(200, "", null, modifiedJsonString);
        when(oauth10aService.execute(any())).thenReturn(response);

        GarminDaily garminDaily = garminService.getMostRecentDailies();
        Assertions.assertNotNull(garminDaily);
    }

    @Test
    void testGetGarminActivities()
        throws GarminPermissionException, IOException, ExecutionException, InterruptedException {
        TabUser currentlyLoggedIn = UnitCommonTestSetup.createTestUser();
        GarminAccessToken garminAccessToken = new GarminAccessToken(currentlyLoggedIn);
        garminAccessToken.setSecretToken("123789123678912345678912345678");
        garminAccessToken.setToken("123789123678912345678912345678");
        when(tabUserService.getCurrentlyLoggedIn()).thenReturn(currentlyLoggedIn);
        when(garminAccessTokenRepository.checkIfConnected(currentlyLoggedIn.getId()))
            .thenReturn(garminAccessToken);
        when(encryption.decrypt(anyString())).thenReturn(garminAccessToken.getSecretToken());
        Response response = new Response(200, "", null, garminActivity);
        when(oauth10aService.execute(any())).thenReturn(response);
        List<GarminActivity> garminActivitiesList = garminService.getGarminActivities();
        Assertions.assertNotNull(garminActivitiesList);
        Assertions.assertEquals(2, garminActivitiesList.size());
    }

    @Test
    void testGetGarminActivitiesWithEmptyJson()
        throws GarminPermissionException, IOException, ExecutionException, InterruptedException {
        TabUser currentlyLoggedIn = UnitCommonTestSetup.createTestUser();
        GarminAccessToken garminAccessToken = new GarminAccessToken(currentlyLoggedIn);
        garminAccessToken.setSecretToken("123789123678912345678912345678");
        garminAccessToken.setToken("123789123678912345678912345678");
        when(tabUserService.getCurrentlyLoggedIn()).thenReturn(currentlyLoggedIn);
        when(garminAccessTokenRepository.checkIfConnected(currentlyLoggedIn.getId()))
            .thenReturn(garminAccessToken);
        when(encryption.decrypt(anyString())).thenReturn(garminAccessToken.getSecretToken());
        Response response = new Response(200, "", null, garminActivity);
        when(oauth10aService.execute(any())).thenReturn(response);
        List<GarminActivity> garminActivitiesList = garminService.getGarminActivities();
        GarminActivity individualGarminActivity = garminActivitiesList.get(0);
        Assertions.assertEquals("WHEELCHAIR PUSH WALK", individualGarminActivity.getActivityType());
        Assertions.assertEquals(195, individualGarminActivity.getActiveKilocalories());
        Assertions.assertEquals(4096.0, individualGarminActivity.getDistanceInMeters());
        Assertions.assertEquals(4463, individualGarminActivity.getDurationInSeconds());
        Assertions.assertEquals(14336689, individualGarminActivity.getActivityId());

    }






    @Test
    void mostRecentDailyNullAccessTest() throws GarminPermissionException {
        TabUser currentlyLoggedIn = UnitCommonTestSetup.createTestUser();
        GarminAccessToken garminAccessToken = new GarminAccessToken(currentlyLoggedIn);
        garminAccessToken.setSecretToken("123789123678912345678912345678");
        garminAccessToken.setToken("123789123678912345678912345678");
        when(tabUserService.getCurrentlyLoggedIn()).thenReturn(currentlyLoggedIn);
        when(garminAccessTokenRepository.checkIfConnected(currentlyLoggedIn.getId()))
            .thenReturn(null);
        GarminDaily garminDaily = garminService.getMostRecentDailies();
        Assertions.assertNull(garminDaily);
    }

    @Test
    void mostRecentDailyNullResponseTest() throws IOException, ExecutionException,
        InterruptedException, GarminPermissionException {
        TabUser currentlyLoggedIn = UnitCommonTestSetup.createTestUser();
        GarminAccessToken garminAccessToken = new GarminAccessToken(currentlyLoggedIn);
        garminAccessToken.setSecretToken("123789123678912345678912345678");
        garminAccessToken.setToken("123789123678912345678912345678");
        when(tabUserService.getCurrentlyLoggedIn()).thenReturn(currentlyLoggedIn);
        when(garminAccessTokenRepository.checkIfConnected(currentlyLoggedIn.getId()))
                .thenReturn(garminAccessToken);
        when(encryption.decrypt(anyString())).thenReturn(garminAccessToken.getSecretToken());
        Response response = new Response(200, "", null, emptyJson);
        when(oauth10aService.execute(any())).thenReturn(response);

    }

    @Test
    void testSaveAccessToken() throws IOException, ExecutionException, InterruptedException {
        when(encryption.encrypt("accessTokenSecret"))
            .thenReturn("encodedSecretAccessToken");
        TabUser currentlyLoggedIn = UnitCommonTestSetup.createTestUser();
        GarminAccessToken garminAccessToken = new GarminAccessToken(currentlyLoggedIn);
        garminAccessToken.setSecretToken("123789123678912345678912345678");
        garminAccessToken.setToken("123789123678912345678912345678");
        when(tabUserService.getCurrentlyLoggedIn()).thenReturn(currentlyLoggedIn);
        when(garminAccessTokenRepository.checkIfConnected(currentlyLoggedIn.getId()))
            .thenReturn(null);
        when(oauth10aService.getAccessToken(any(), any())).thenReturn(
            new OAuth1AccessToken("accessToken", "accessTokenSecret"));
        OAuth1RequestToken requestToken =
            new OAuth1RequestToken("requestTokenValue", "requestTokenSecret");
        String oauthVerifier = "verifier";
        garminService.saveAccessToken(requestToken, oauthVerifier);
        verify(encryption, times(1)).encrypt("accessTokenSecret");
        verify(garminAccessTokenRepository, times(1)).checkIfConnected(anyLong());
        verify(garminAccessTokenRepository, times(1)).save(any(GarminAccessToken.class));
    }
}
