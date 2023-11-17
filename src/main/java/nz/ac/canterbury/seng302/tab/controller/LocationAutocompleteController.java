package nz.ac.canterbury.seng302.tab.controller;


import java.io.IOException;
import nz.ac.canterbury.seng302.tab.service.LocationApi;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * a class to pass the input variable from the front end to the java backend
 */
@Controller
public class LocationAutocompleteController {

    /**
     * To use the location API
     */
    private final LocationApi locationApi = new LocationApi();

    /**
     * catch the location input from the url link for the full address
     *
     * @param locations input location string
     * @return return the json object from the api included all the data for the address
     * @throws IOException          exception error for input/output
     * @throws ParseException       exception error for parsing
     * @throws InterruptedException exception error for interruption
     */
    @GetMapping("/address")
    @ResponseBody
    public JSONArray getAutomappingAddress(
        @RequestParam(value = "locations", required = true) String locations)
        throws IOException, ParseException, InterruptedException {
        return locationApi.getAutoMappingAddress(locations);
    }

    /**
     * catch the locaiton input from the url link for the country
     *
     * @param locations input country location string
     * @return return the json object from the api included all the data for the address
     * @throws IOException          exception error for input/output
     * @throws ParseException       exception error for parsing
     * @throws InterruptedException exception error for interruption
     */
    @GetMapping("/country")
    @ResponseBody
    public JSONArray getCountryAutomapping(
        @RequestParam(value = "locations", required = true) String locations)
        throws IOException, ParseException, InterruptedException {
        return locationApi.getCountryAutoMappingAddress(locations);
    }

    /**
     * catch the locaiton input from the url link for the city
     *
     * @param locations input city location string
     * @return return the json object from the api included all the data for the address
     * @throws IOException          exception error for input/output
     * @throws ParseException       exception error for parsing
     * @throws InterruptedException exception error for interruption
     */
    @GetMapping("/city")
    @ResponseBody
    public JSONArray getCityAutomapping(
        @RequestParam(value = "locations", required = true) String locations)
        throws IOException, ParseException, InterruptedException {
        return locationApi.getCityAutoMappingAddress(locations);
    }
}
