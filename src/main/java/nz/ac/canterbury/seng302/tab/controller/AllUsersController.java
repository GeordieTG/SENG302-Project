package nz.ac.canterbury.seng302.tab.controller;

import static nz.ac.canterbury.seng302.tab.utility.HeaderHelper.addLoggedInAttr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import nz.ac.canterbury.seng302.tab.entity.Location;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.entity.enums.SupportedSports;
import nz.ac.canterbury.seng302.tab.service.FilterUsersService;
import nz.ac.canterbury.seng302.tab.service.LocationService;
import nz.ac.canterbury.seng302.tab.service.SportService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller class for View And Search All Users page
 */
@Controller
public class AllUsersController {
    private static final String SPORT = "Sport";
    private static final String CITY = "city";
    /**
     * Allows us to log
     */
    Logger logger = LoggerFactory.getLogger(AllUsersController.class);
    /**
     * Allows us to access user functionality with the database
     */
    @Autowired
    private TabUserService tabUserService;

    /**
     * Allows us to access filter user functionality with the database
     */
    @Autowired
    private FilterUsersService filterUsersService;
    /**
     * Allows us to access sport functionality with the database
     */
    @Autowired
    private SportService sportService;
    /**
     * Allows us to access location functionality with the database
     */
    @Autowired
    private LocationService locationService;

    /**
     * Gets all registered users who match the filter and search criteria
     *
     * @param sportQuery  String of sports to filter by (comma seperated values)
     * @param cityQuery   String of cities to filter by (comma seperated values)
     * @param searchQuery A string that is used to match first and last names
     * @param model       (map-like) representation of results to be used by thymeleaf
     * @return thymeleaf allUsers, the next page of users to be displayed
     */

    @PostMapping("/filter")
    public String getAllPagesFilter(
        @RequestParam(name = "hiddenFilterQuerySport", required = false, defaultValue = "none")
        String sportQuery,
        @RequestParam(name = "hiddenFilterQueryCity", required = false, defaultValue = "none")
        String cityQuery,
        @RequestParam(name = "searchName", required = false, defaultValue = "none")
        String searchQuery,
        @RequestParam(name = "pageNumber", required = false, defaultValue = "1") int currentPage,
        Model model) {
        logger.info("POST /filter");
        model.addAttribute(SPORT, true);
        model.addAttribute(CITY, true);
        if (sportQuery.equals("")) {
            sportQuery = "none";
            model.addAttribute(SPORT, false);
        }
        if (cityQuery.equals("")) {
            cityQuery = "none";
            model.addAttribute(CITY, false);
        }
        if (searchQuery.equals("")) {
            searchQuery = "none";
        }
        if (cityQuery.equals("none") && sportQuery.equals("none") && searchQuery.equals("none")) {
            return "redirect:/allUsers";
        }
        addLoggedInAttr(model);
        return "redirect:/allUsers?searchQuery=" + searchQuery + "&sportQuery=" + sportQuery
            + "&cityQuery=" + cityQuery + "&pageNumber=" + currentPage;
    }

    /**
     * Gets all registered users in page format
     *
     * @param model       (map-like) representation of results to be used by thymeleaf
     * @param currentPage the current page of users that is being displayed
     * @return thymeleaf allUsers, the next page of users to be displayed
     */
    @GetMapping("/allUsers")
    public String getOnePage(Model model,
                             @RequestParam(name = "pageNumber", required = false,
                                 defaultValue = "1")
                             int currentPage,
                             @RequestParam(name = "sportQuery", required = false,
                                 defaultValue = "none")
                             String sportQuery,
                             @RequestParam(name = "cityQuery", required = false,
                                 defaultValue = "none")
                             String cityQuery,
                             @RequestParam(name = "searchQuery", required = false,
                                 defaultValue = "none")
                             String searchQuery) throws IOException {
        logger.info("GET /allUsers");
        model.addAttribute(SPORT, true);
        model.addAttribute(CITY, true);
        if (sportQuery.equals("none")) {
            model.addAttribute(SPORT, false);
        }
        if (cityQuery.equals("none")) {
            model.addAttribute(CITY, false);
        }
        model.addAttribute("filter", true);
        addLoggedInAttr(model);
        List<List<String>> filterArrays = generateFilterArrays(cityQuery, sportQuery);
        List<TabUser> pages =
            filterUsersService.criteriaFilter(filterArrays.get(0), filterArrays.get(1),
                (currentPage - 1) * 10, searchQuery, false);
        List<TabUser> resultsList =
            filterUsersService.criteriaFilter(List.of("none"), List.of("none"),
                (currentPage - 1) * 10, searchQuery, true);

        List<Location> locations = new ArrayList<>();
        Set<String> cities = new HashSet<>();
        Set<String> sports = new HashSet<>();
        for (TabUser page : pages) {
            locations.add(locationService.getLocationByLocationId(page.getLocationId()));
        }
        for (TabUser city : resultsList) {
            cities.add(locationService.getLocationByLocationId(city.getLocationId()).getCity());
        }
        for (TabUser sport : resultsList) {
            sports.add(sport.getFavouriteSport());
        }

        if (searchQuery.equals("none")) {
            model.addAttribute("sports", EnumSet.range(SupportedSports.Baseball,
                SupportedSports.Volleyball));
            model.addAttribute("cities", locationService.findCities());

        } else {
            model.addAttribute("sports", sports);
            model.addAttribute("cities", cities);
        }

        if (tabUserService.getCurrentlyLoggedIn() != null) {
            model.addAttribute("userID", tabUserService.getCurrentlyLoggedIn().getId());
        }
        long totalItems = Long.parseLong(String.valueOf(
            filterUsersService.criteriaFilter(filterArrays.get(0), filterArrays.get(1),
                (currentPage - 1) * 10, searchQuery, true).size()));
        double totalPages = Math.ceil((double) totalItems / 10);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("responses", pages);
        model.addAttribute("locations", locations);
        model.addAttribute("searchQueryCity", cityQuery);
        model.addAttribute("searchQuerySport", sportQuery);
        model.addAttribute("searchName", searchQuery);
        return "allUsers";
    }

    /**
     * Retrieves the profile information for a single user identified by their ID and populates
     * a view with the information.
     *
     * @param model the Model object used to add attributes to the view
     * @param id    the ID of the user whose profile information is being requested
     * @return the name of the view to display the selected user's profile information
     */
    @GetMapping("/oneUser")
    public String selectedProfile(Model model, @RequestParam("id") int id) {
        logger.info("GET /oneUser");
        addLoggedInAttr(model);
        TabUser user = tabUserService.getById(id);
        addAttributes(user.getFirstName(), user.getLastName(), user.getEmail(),
            user.getDateOfBirth(), model);

        model.addAttribute("profilePicture", user.getProfilePicture());
        model.addAttribute("favouriteSport", user.getFavouriteSport());
        return "selectedUser";
    }


    /**
     * Adds attributes to the Model object for use in a view,
     * including the first name, last name, email,
     * date of birth, and emailExists status.
     *
     * @param firstName   the first name of the user being displayed
     * @param lastName    the last name of the user being displayed
     * @param email       the email of the user being displayed
     * @param dateOfBirth the date of birth of the user being displayed
     * @param model       the Model object used to add attributes to the view
     */
    public void addAttributes(String firstName, String lastName, String email, String dateOfBirth,
                              Model model) {
        model.addAttribute("firstName", firstName);
        model.addAttribute("lastName", lastName);
        model.addAttribute("email", email);
        model.addAttribute("DOB", dateOfBirth);
    }

    /**
     * Generates two arrays, consisting of sports and cities to filter by
     * date of birth, and emailExists status.
     *
     * @param sportQuery A string (CSV) containing the sports to filter by
     * @param cityQuery  A string (CSV) containing the cities to filter by
     * @return A list containing two lists, one for the sports to filter by and
     *         one for the cities to filter by.
     */
    public List<List<String>> generateFilterArrays(String cityQuery, String sportQuery) {
        List<String> sportFilters = new ArrayList<>();
        List<String> cityFilters = new ArrayList<>();
        String[] sportList = sportQuery.split(", ");
        String[] cityList = cityQuery.split(", ");
        Collections.addAll(sportFilters, sportList);
        Collections.addAll(cityFilters, cityList);
        List<List<String>> result = new ArrayList<>();
        result.add(cityFilters);
        result.add(sportFilters);
        return result;
    }
}
