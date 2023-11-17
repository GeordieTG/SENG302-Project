package nz.ac.canterbury.seng302.tab.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * LocationAPI class to call the api request when the user input the address
 */
@Service
public class LocationApi {

    private static final String ACCEPT = "Accept";
    private static final String FEATURES = "features";
    @Value("${LOCATION_API_KEY}")
    static String locationApiKey;
    private static final String APISTR = "https://api.openrouteservice.org"
        + "/geocode/autocomplete?api_key=" + locationApiKey + "&text=";
    Logger logger = LoggerFactory.getLogger(LocationApi.class);

    /**
     * Method to call autocomplete api when the user input the address
     * This method is for the input address text box
     *
     * @param inputLocation the string input of text box for address
     * @return A JSONArray consisting of the geographical information for the given address
     * @throws IOException          Thrown if any I/O error occurs
     * @throws InterruptedException Thrown if any thread is interrupted
     * @throws ParseException       Thrown if any error arises while parsing the returned JSON data
     */
    public JSONArray getAutoMappingAddress(String inputLocation)
        throws IOException, InterruptedException, ParseException {

        inputLocation = inputLocation.replace(" ", "%20");
        logger.info("get the http request for the location API");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(
                URI.create(
                    APISTR
                        + inputLocation))
            .header(ACCEPT,
                "application/json, application/geo+json, application/gpx+xml, "
                    + "img/png; charset=utf-8")
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONParser parser = new JSONParser();
        JSONObject jsonResponse = (JSONObject) parser.parse(response.body());
        return (JSONArray) jsonResponse.get(FEATURES);

    }

    /**
     * Method to call autocomplete api when the user input the country
     * This method is for the input address text box
     *
     * @param inputLocation the string input of text box for country
     * @return A JSONArray consisting of the geographical information for the given address
     * @throws IOException          Thrown if any I/O error occurs
     * @throws InterruptedException Thrown if any thread is interrupted
     * @throws ParseException       Thrown if any error arises while parsing the returned JSON data
     */
    public JSONArray getCountryAutoMappingAddress(String inputLocation)
        throws IOException, InterruptedException, ParseException {
        inputLocation = inputLocation.replace(" ", "%20");
        logger.info("get the http request for the country in location API");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(
                URI.create(APISTR
                    + inputLocation
                    + "&layers=country"))
            .header(ACCEPT, "application/json, application/geo+json, application/gpx+xml,"
                + " img/png; charset=utf-8")
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONParser parser = new JSONParser();
        JSONObject jsonResponse = (JSONObject) parser.parse(response.body());
        return (JSONArray) jsonResponse.get(FEATURES);

    }


    /**
     * Method to call autocomplete api when the user input the city
     * This method is for the input address text box
     *
     * @param inputLocation the string input of text box for city
     * @return the json object form the api to the front end
     * @throws IOException          exception error for input/output
     * @throws InterruptedException exception error for interruptions
     * @throws ParseException       exception error for parsing
     */
    public JSONArray getCityAutoMappingAddress(String inputLocation)
        throws IOException, InterruptedException, ParseException {
        inputLocation = inputLocation.replace(" ", "%20");
        HttpClient client = HttpClient.newHttpClient();
        logger.info("get the http request for the city in location API");
        HttpRequest request = HttpRequest.newBuilder(
                URI.create(APISTR
                    + inputLocation + "&layers=city"))
            .header(ACCEPT, "application/json, application/geo+json, application/gpx+xml,"
                + " img/png; charset=utf-8")
            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONParser parser = new JSONParser();
        JSONObject jsonResponse = (JSONObject) parser.parse(response.body());
        return (JSONArray) jsonResponse.get(FEATURES);

    }

}
