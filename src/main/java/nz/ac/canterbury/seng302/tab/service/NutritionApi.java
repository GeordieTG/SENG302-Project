package nz.ac.canterbury.seng302.tab.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.simple.JSONAware;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service for the Nutrition API, makes calls to the API
 */
@Service
public class NutritionApi {

    String apiBaseUrl = "https://api.nal.usda.gov/fdc/v1/";
    Logger logger = LoggerFactory.getLogger(NutritionApi.class);

    /**
     * The API key for the Nutrition API
     */
    @Value("${NUTRITION_API_KEY:nokey}")
    private String apiKey;

    /**
     * Gets the food search results from the Nutrition API
     * @param query The query to search for
     * @return The food search results
     */
    public JSONAware searchFoods(String query)
        throws IOException, InterruptedException, ParseException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(
                URI.create(
                    apiBaseUrl + "foods/search?" + "query=" + query.replace(" ",
                        "%20") + "&dataType" + "=SR%20Legacy" + "&pageSize=10"))
            .header("X-Api-Key", apiKey)

            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONParser parser = new JSONParser();
        return (JSONAware) parser.parse(response.body());
    }

    /**
     * Gets the details of a list food ids from the Nutrition API
     * @param fdcIds The fdcIds of the foods
     * @return The details of the foods
     */
    public JSONAware getFoods(Long[] fdcIds)
        throws IOException, InterruptedException, ParseException {

        StringBuilder urlBuilder =
            new StringBuilder("https://api.nal.usda.gov/fdc/v1/"); // Replace with your API URL
        Long[] foodNutrientIds = {205L, 208L, 203L, 269L, 204L};

        for (int i = 0; i < fdcIds.length; i++) {
            Long fdcId = fdcIds[i];
            if (i == 0) {
                urlBuilder.append("foods?fdcIds=").append(fdcId.toString());
            } else {
                urlBuilder.append("&fdcIds=").append(fdcId.toString());
            }
        }

        for (Long foodNutrientId : foodNutrientIds) {
            urlBuilder.append("&nutrients=").append(foodNutrientId);
        }

        String finalUrl = urlBuilder.toString();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(
                URI.create(
                    finalUrl))
            .header("X-Api-Key", apiKey)

            .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONParser parser = new JSONParser();
        return (JSONAware) parser.parse(response.body());
    }
}
