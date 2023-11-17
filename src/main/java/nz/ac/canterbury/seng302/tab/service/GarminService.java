package nz.ac.canterbury.seng302.tab.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import nz.ac.canterbury.seng302.tab.api.GarminApi;
import nz.ac.canterbury.seng302.tab.entity.Activity;
import nz.ac.canterbury.seng302.tab.entity.GarminAccessToken;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.entity.enums.GarminState;
import nz.ac.canterbury.seng302.tab.entity.garminentities.GarminActivity;
import nz.ac.canterbury.seng302.tab.entity.garminentities.GarminDaily;
import nz.ac.canterbury.seng302.tab.entity.garminentities.HeartRateData;
import nz.ac.canterbury.seng302.tab.exception.GarminPermissionException;
import nz.ac.canterbury.seng302.tab.repository.GarminAccessTokenRepository;
import nz.ac.canterbury.seng302.tab.repository.GarminActivityRepository;
import nz.ac.canterbury.seng302.tab.utility.Encryption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/**
 * Garmin Service Class
 */
@Service
public class GarminService {

    public static final String ACTIVE_KILOCALORIES = "activeKilocalories";
    public static final String ACTIVE_TIME_IN_SECONDS = "activeTimeInSeconds";
    public static final String STEPS = "steps";
    public static final String STEPS_GOAL = "stepsGoal";
    public static final String DISTANCE_IN_METERS = "distanceInMeters";
    public static final String MIN_HEART_RATE_IN_BEATS_PER_MINUTE
        = "minHeartRateInBeatsPerMinute";
    public static final String MAX_HEART_RATE_IN_BEATS_PER_MINUTE
        = "maxHeartRateInBeatsPerMinute";
    public static final String AVERAGE_HEART_RATE_IN_BEATS_PER_MINUTE
        = "averageHeartRateInBeatsPerMinute";
    public static final String RESTING_HEART_RATE_IN_BEATS_PER_MINUTE
        = "restingHeartRateInBeatsPerMinute";
    private ObjectMapper objMapper = new ObjectMapper();
    @Autowired
    GarminAccessTokenRepository garminAccessTokenRepository;

    @Autowired
    TabUserService tabUserService;

    @Autowired
    Encryption encryption;

    @Autowired
    GarminActivityRepository garminActivityRepository;

    Logger logger = LoggerFactory.getLogger(GarminService.class);

    /**
     * Provided by Fabian
     */
    @Value("${garmin.consumer.secret}")
    String secretKey;

    /**
     * Provided by Fabian
     */
    @Value("${garmin.consumer.key}")
    String consumerKey;
    GarminState garminState = GarminState.NOT_CONNECTED;
    @Value("${current_url}")
    private String url;
    /**
     * ScribeJava service used to communicate with the Garmin API
     */
    private OAuth10aService oauth10aService;

    /**
     * Parses a JSON node representing a Garmin daily summary and creates a GarminDaily object.
     *
     * @param node The JSON node representing the Garmin daily summary.
     * @return A GarminDaily object containing the parsed data.
     */
    private static GarminDaily parseGarminDaily(JsonNode node) {
        double activeKilocalories = node.get(ACTIVE_KILOCALORIES) != null
            ? node.get(ACTIVE_KILOCALORIES).asDouble() : 0.0;
        double activeTimeHrs = node.get(ACTIVE_TIME_IN_SECONDS) != null
            ? node.get(ACTIVE_TIME_IN_SECONDS).asDouble() / 60 / 60 : 0.0;
        int steps = node.get(STEPS) != null ? node.get(STEPS).asInt() : 0;
        int stepsGoal = node.get(STEPS_GOAL) != null ? node.get(STEPS_GOAL).asInt() : 0;
        double distanceInMeters = node.get(DISTANCE_IN_METERS) != null
            ? node.get(DISTANCE_IN_METERS).asDouble() : 0;
        int minHeartRate = node.get(MIN_HEART_RATE_IN_BEATS_PER_MINUTE) != null
            ? node.get(MIN_HEART_RATE_IN_BEATS_PER_MINUTE).asInt() : 0;
        int maxHeartRate = node.get(MAX_HEART_RATE_IN_BEATS_PER_MINUTE) != null
            ? node.get(MAX_HEART_RATE_IN_BEATS_PER_MINUTE).asInt() : 0;
        int averageHeartRate = node.get(AVERAGE_HEART_RATE_IN_BEATS_PER_MINUTE) != null
            ? node.get(AVERAGE_HEART_RATE_IN_BEATS_PER_MINUTE).asInt() : 0;
        int restingHeartRate = node.get(RESTING_HEART_RATE_IN_BEATS_PER_MINUTE) != null
            ? node.get(RESTING_HEART_RATE_IN_BEATS_PER_MINUTE).asInt() : 0;
        HeartRateData heartRateData = new HeartRateData(minHeartRate,
            maxHeartRate, averageHeartRate, restingHeartRate);
        return new GarminDaily(activeKilocalories, activeTimeHrs,
            steps, stepsGoal, distanceInMeters, heartRateData, true);
    }

    public GarminState getGarminState() {
        return garminState;
    }

    public void setGarminState(GarminState garminState) {
        this.garminState = garminState;
    }

    /**
     * Gets the service for the garmin service lazily and ensuring it is only created once to avoid
     * memory leaks and excessive resource usage
     *
     * @return The service
     */

    public OAuth10aService lazyService() {
        if (this.oauth10aService == null) {
            this.oauth10aService =
                new ServiceBuilder(consumerKey)
                    .apiSecret(secretKey)
                    .build(GarminApi.instance());
            GarminApi.setUrl(url);
        }
        return this.oauth10aService;
    }

    /**
     * Step One of the OAuth process.
     * Calls the Garmin API service to generate an unauthorized request token for a given user.
     *
     * @return a OAuth1RequestToken object containing the public token and secret token.
     * @throws IOException          thrown within ScribeJava
     * @throws ExecutionException   thrown within ScribeJava
     * @throws InterruptedException thrown within ScribeJava
     */
    public OAuth1RequestToken getRequestToken()
        throws IOException, ExecutionException, InterruptedException {
        return lazyService().getRequestToken();
    }

    /**
     * Step Two of the OAuth process.
     * Fetch the url to the authorization page (aka the Garmin login page).
     *
     * @param requestToken users request token generated during step one of the OAuth process
     * @return the url for the user to be redirected to in order for them to log in to Garmin
     */
    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
        final OAuth10aService service = lazyService();
        return service.getAuthorizationUrl(requestToken);
    }

    /**
     * Saves the access token obtained from the OAuth1 authentication flow.
     *
     * @param requestToken  The OAuth1 request token.
     * @param oauthVerifier The OAuth1 verifier.
     */
    public void saveAccessToken(OAuth1RequestToken requestToken, String oauthVerifier) {
        // Trade the Request Token and Verifier for the Access Token
        try {
            final OAuth1AccessToken accessToken =
                lazyService().getAccessToken(requestToken, oauthVerifier);
            // Encrypt the secret token
            String encodedSecretAccessToken = encryption.encrypt(accessToken.getTokenSecret());
            // Save the access token to the database, only if they don't already have one
            GarminAccessToken garminAccessToken = garminAccessTokenRepository
                .checkIfConnected(tabUserService.getCurrentlyLoggedIn().getId());
            if (garminAccessToken == null) {
                garminAccessToken = new GarminAccessToken(tabUserService.getCurrentlyLoggedIn());
            }
            garminAccessToken.setSecretToken(encodedSecretAccessToken);
            garminAccessToken.setToken(accessToken.getToken());
            garminAccessTokenRepository.save(garminAccessToken);
        } catch (IOException | ExecutionException | InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Saves a GarminAccessToken object to the database.
     *
     * @param garminAccessToken The GarminAccessToken object to be saved.
     */
    public void save(GarminAccessToken garminAccessToken) {
        garminAccessTokenRepository.save(garminAccessToken);
    }

    /**
     * Checks if the user is connected to garmin. Needed to change the connect button on the profile
     * page.
     *
     * @param currentlyLoggedIn The currently logged-in user
     * @param model             model for the garmin daily to be added to
     * @return "True" if they are already connected, "False" if not. Returns a string as it needs
     *          to be passed to Thymeleaf
     */
    public String fetchDailies(TabUser currentlyLoggedIn, Model model) {
        GarminAccessToken garminAccessToken = garminAccessTokenRepository
            .checkIfConnected(currentlyLoggedIn.getId());

        if (garminAccessToken != null) {
            fetchDaily(model);
            return "True";
        }
        return "False";
    }

    /**
     * Fetches current daily and adds it to the model
     *
     * @param model user profile model
     */
    private void fetchDaily(Model model) {
        GarminDaily garminDaily = new GarminDaily(0, 0, 0,
            0, 0, new HeartRateData(0, 0,
            0, 0), false);
        try {
            GarminDaily fetchedDaily = getMostRecentDailies();
            if (fetchedDaily != null) {
                garminDaily = fetchedDaily;
            }
        } catch (GarminPermissionException e) {
            logger.error(e.getMessage());
        }

        model.addAttribute("garminDaily", garminDaily);
    }

    /**
     * Retrieves the most recent daily summaries from the Garmin Wellness API using OAuth1
     * authentication.
     * Decrypts and uses the user's Garmin access token to make the API request.
     *
     * @return garminDaily a garmin daily entity or null if unsuccessful
     */
    public GarminDaily getMostRecentDailies() throws GarminPermissionException {

        final OAuthRequest request = createRequest("dailies");
        GarminAccessToken garminAccessToken =
            garminAccessTokenRepository.checkIfConnected(tabUserService
                .getCurrentlyLoggedIn().getId());
        if (garminAccessToken != null) {
            lazyService();
            signRequest(request, garminAccessToken);

            String response = executeRequest(request);
            JsonNode node = mostRecentDaily(response);
            if (node != null) {
                return parseGarminDaily(node);
            }
        }
        return null;
    }

    /**
     * Executes the request
     * @param request OAuthRequest to send to API
     * @return A string representing the response
     * @throws GarminPermissionException if user does not have permission
     */
    private String executeRequest(OAuthRequest request) throws GarminPermissionException {
        try (Response response = oauth10aService.execute(request)) {

            String jsonResponse = response.getBody();
            if (!jsonResponse.contains("errorMessage")) {
                return jsonResponse;

            } else {
                throw new GarminPermissionException();
            }
        } catch (IOException | ExecutionException | InterruptedException
            e) {
            logger.error(e.toString());
            Thread.currentThread().interrupt();
        }
        return null;
    }

    /**
     * Signs an OAuthRequest for a given access token
     * @param request OAuthRequest to sign
     * @param garminAccessToken GarminAccessToken to use as signature
     */
    private void signRequest(OAuthRequest request, GarminAccessToken garminAccessToken) {
        final OAuth1AccessToken accessToken = new OAuth1AccessToken(
            garminAccessToken.getToken(),
            encryption.decrypt(garminAccessToken.getSecretToken()));
        oauth10aService.signRequest(accessToken, request);
    }

    /**
     * Creates a request based on the string given
     * @param api String representing the end point for the api
     * @return an OAuthRequest
     */
    private static OAuthRequest createRequest(String api) {
        final OAuthRequest request = new OAuthRequest(Verb.GET, "https://apis.garmin"
            + ".com/wellness-api/rest/" + api);
        long currentTimestamp = Instant.now().getEpochSecond();
        long yesterday = Instant.now().getEpochSecond() - 86400;
        request.addQuerystringParameter("uploadStartTimeInSeconds",
            String.valueOf(yesterday));
        request.addQuerystringParameter("uploadEndTimeInSeconds",
            String.valueOf(currentTimestamp));
        return request;
    }

    /**
     * Finds and returns the most recent daily summary from the given JSON data of dailies.
     *
     * @param jsonDailies A JSON string containing daily summaries.
     * @return The JSON node representing the most recent daily summary.
     */
    private JsonNode mostRecentDaily(String jsonDailies) {
        JsonNode mostRecentSummary = null;
        List<JsonNode> todaysSummaries = new ArrayList<>();
        long maxStartTimeInSeconds = Instant.now().getEpochSecond() - 86400;
        try {
            JsonNode jsonNode = objMapper.readTree(jsonDailies);
            for (JsonNode summary : jsonNode) {
                long startTimeInSeconds = summary.get("startTimeInSeconds").asLong();
                if (startTimeInSeconds > maxStartTimeInSeconds) {
                    todaysSummaries.add(summary);
                }
            }
            if (!todaysSummaries.isEmpty()) {
                mostRecentSummary = todaysSummaries.get(todaysSummaries.size() - 1);
            }
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }
        return mostRecentSummary;
    }

    /**
     * Gets the users garmin activities
     * @return the users garmin activities
     * @throws GarminPermissionException if user does not have permission
     */
    public List<GarminActivity> getGarminActivities() throws GarminPermissionException {
        final OAuthRequest request = createRequest("activities");
        GarminAccessToken garminAccessToken =
            garminAccessTokenRepository.checkIfConnected(tabUserService
                .getCurrentlyLoggedIn().getId());
        if (garminAccessToken != null) {
            lazyService();
            signRequest(request, garminAccessToken);
            String response = executeRequest(request);
            if (response != null) {
                return parseGarminActivities(response);
            }
        }
        return Collections.emptyList();
    }

    /**
     * Parse the garmin activities into a list of GarminActivity Objects
     * @param jsonResponse response from the garmin API
     * @return a list of Garmin Activities
     */
    private List<GarminActivity> parseGarminActivities(String jsonResponse) {
        JsonNode jsonNode;

        List<GarminActivity> garminActivities = new ArrayList<>();
        try {
            jsonNode = objMapper.readTree(jsonResponse);
            for (JsonNode activity : jsonNode) {
                GarminActivity garminActivity =
                        new GarminActivity(activity,
                            tabUserService.getCurrentlyLoggedIn());
                garminActivities.add(garminActivity);
            }
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }

        return garminActivities;
    }

    /**
     * Saves Garmin Activity to database
     * @param garminActivity the entity representing a Garmin Activity
     * @return returns the saved garmin activity
     */
    public GarminActivity saveGarminActivity(GarminActivity garminActivity) {
        return garminActivityRepository.save(garminActivity);
    }

    /**
     * Gets a garmin activity by its id
     * @param activityId long representing the id of a garmin actitivity
     * @return a garmin activity if found
     */
    public GarminActivity getGarminActivityById(long activityId) {
        return garminActivityRepository.findById(activityId);
    }

    public GarminActivity getGarminActivityByActivity(Activity activity) {
        return garminActivityRepository.findByActivity(activity);
    }

    public GarminActivity getByActivityAndUser(Long activityId, Long userId) {
        return garminActivityRepository.findByActivityAndUser(activityId, userId);
    }

    /**
     * Checks If a user is linked to a garmin watch
     * @param userId user's id
     * @return bool if the user is connected
     */
    public boolean checkGarminConnected(Long userId) {
        return garminAccessTokenRepository
                .checkIfConnected(userId) != null;
    }

    /**
     * Deletes a garmin activity if one already exists
     * @param garminActivity the garmin activity to delete
     */
    public void delete(GarminActivity garminActivity) {
        garminActivityRepository.delete(garminActivity);
    }
}