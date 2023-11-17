package nz.ac.canterbury.seng302.tab.controller;

import static nz.ac.canterbury.seng302.tab.utility.HeaderHelper.addLoggedInAttr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import nz.ac.canterbury.seng302.tab.entity.Club;
import nz.ac.canterbury.seng302.tab.entity.Searchable;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.entity.enums.SupportedSports;
import nz.ac.canterbury.seng302.tab.service.FilterTeamsService;
import nz.ac.canterbury.seng302.tab.service.LocationService;
import nz.ac.canterbury.seng302.tab.utility.SortingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for view teams
 */
@Controller
public class ViewTeamsController {

    /**
     * Allows us to log
     */
    Logger logger = LoggerFactory.getLogger(ViewTeamsController.class);


    /**
     * Allows us to access location functionality with the database
     */
    @Autowired
    private LocationService locationService;

    /**
     * Allows us to access filter team functionality with the databse
     */
    @Autowired
    private FilterTeamsService filterTeamsService;


    /**
     * Get one page for viewing teams
     *
     * @param sportQuery  String of sports to filter by (comma seperated values)
     * @param cityQuery   String of cities to filter by (comma seperated values)
     * @param searchQuery A string that is used to match first and last names
     * @return thymeleaf viewTeams
     */
    @GetMapping("/viewTeams")
    public String getOnePageFilter(Model model,
                                   @RequestParam(name = "currentPage",
                                       required = false, defaultValue = "1")
                                   int currentPage,
                                   @RequestParam(name = "sportQuery",
                                       required = false, defaultValue = "none")
                                   String sportQuery,
                                   @RequestParam(name = "cityQuery",
                                       required = false, defaultValue = "none")
                                   String cityQuery,
                                   @RequestParam(name = "showAllTeams",
                                       required = false, defaultValue = "1")
                                   int showAllTeams,
                                   @RequestParam(name = "searchQuery",
                                       required = false, defaultValue = "none")
                                   String searchQuery) throws IOException {
        logger.info("GET /viewTeams");
        List<List<String>> filterArrays = generateFilterArrays(cityQuery,
            sportQuery);
        List<? extends Searchable> pages;
        List<? extends Searchable> clubs;
        List<? extends Searchable> checkLocationTeam = executeQuery(Team.class, filterArrays,
            searchQuery, showAllTeams, true);
        List<? extends Searchable> checkLocationClub = executeQuery(Club.class, filterArrays,
            searchQuery, showAllTeams, true);

        boolean checkLocation = checkLocationClub.isEmpty()
            && checkLocationTeam.isEmpty();

        pages = executeQuery(Team.class, filterArrays, searchQuery,
            showAllTeams, false);
        clubs = executeQuery(Club.class, filterArrays, searchQuery,
            showAllTeams, false);

        List<? extends Searchable> teamAndClubList = Stream.concat(pages.stream(),
            clubs.stream()).toList();
        if (!searchQuery.equals("none") && !checkLocation) {
            teamAndClubList = SortingUtil.sortTeamAndClubLocation(teamAndClubList);
        } else {
            teamAndClubList = SortingUtil.sortTeamAndClubNameFirst(teamAndClubList);
        }
        Set<String> cities = new HashSet<>();
        Set<String> sports = new HashSet<>();


        if (searchQuery.equals("none")) {
            cities.addAll(locationService.findCities());
            EnumSet<SupportedSports> sportsRange =
                EnumSet.range(SupportedSports.Baseball, SupportedSports.Volleyball);
            List<String> list = new ArrayList<>();
            for (SupportedSports supportedSports : sportsRange) {
                String toString = supportedSports.toString();
                list.add(toString);
            }
            sports.addAll(list);
        } else {
            filterTeamsService.addCityAndSportFilters(teamAndClubList, sports, cities);
        }
        addLoggedInAttr(model);
        model.addAttribute("cities", cities);
        model.addAttribute("sports", sports);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("showAllTeams", showAllTeams);
        double totalPages = Math.ceil((double) teamAndClubList.size() / 10);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("filter", true);
        model.addAttribute("totalItems", teamAndClubList.size());
        int startPosition = (currentPage - 1) * 10;
        int next = Math.min((teamAndClubList.size() - startPosition), 10);
        model.addAttribute("teams", teamAndClubList.subList(
            startPosition, startPosition + next));
        model.addAttribute("searchQueryCity", cityQuery);
        model.addAttribute("searchQuerySport", sportQuery);
        model.addAttribute("searchQuery", searchQuery);
        return "viewTeams";
    }

    /**
     * Gets all registered users who match the filter criteria
     *
     * @param sportQuery  String of sports to filter by (comma seperated values)
     * @param cityQuery   String of cities to filter by (comma seperated values)
     * @param searchQuery A string that is used to match first and last names
     * @param model       (map-like) representation of results to be used by thymeleaf
     * @return thymeleaf allUsers, the next page of users to be displayed
     */
    @PostMapping("/viewTeams")
    public String getAllPagesFilterTeam(@RequestParam(name = "searchQuery") String searchQuery,
                                        @RequestParam(name = "hiddenFilterQuerySport")
                                        String sportQuery,
                                        @RequestParam(name = "hiddenFilterQueryCity")
                                        String cityQuery,
                                        @RequestParam(name = "showAllTeams", required = false,
                                            defaultValue = "1")
                                        int showAllTeams,
                                        Model model) {
        logger.info("POST /viewTeams");

        if (sportQuery.equals("")) {
            sportQuery = "none";
        }
        if (cityQuery.equals("")) {
            cityQuery = "none";
        }
        if (searchQuery.equals("")) {
            searchQuery = "none";
        }
        if (cityQuery.equals("none") && sportQuery.equals("none") && searchQuery.equals("none")) {
            return "redirect:/viewTeams?showAllTeams=" + showAllTeams;
        }
        addLoggedInAttr(model);
        return "redirect:/viewTeams?currentPage=1&searchQuery=" + searchQuery + "&sportQuery="
            + sportQuery + "&cityQuery=" + cityQuery + "&showAllTeams=" + showAllTeams;
    }

    /**
     * Generates two arrays, consisting of sports and cities to filter by
     * date of birth, and emailExists status.
     *
     * @param sportQuery A string (CSV) containing the sports to filter by
     * @param cityQuery  A string (CSV) containing the cities to filter by
     * @return A list containing two lists, one for the sports to filter by
     *         and one for the cities to filter by.
     */
    public List<List<String>> generateFilterArrays(String cityQuery, String sportQuery) {
        List<String> sportFilters = new ArrayList<>();
        List<String> cityFilters = new ArrayList<>();
        String[] sport = sportQuery.split(", ");
        String[] city = cityQuery.split(", ");
        Collections.addAll(sportFilters, sport);
        Collections.addAll(cityFilters, city);
        List<List<String>> result = new ArrayList<>();
        result.add(cityFilters);
        result.add(sportFilters);
        return result;
    }

    /**
     * executes a particular query
     *
     * @param enityClass    Class representing the entity we want to search for
     * @param filterArrays  List of list of strings representing the filters we want
     * @param searchQuery   Specific search string
     * @param showAllTeams  integer representing whether or not to get all team or not
     * @param checkLocation boolean representing if we want to search for locations
     * @return a list of unknown objects representing found results from the database
     */
    public <T extends Searchable> List<T> executeQuery(Class<T> enityClass,
                                                       List<List<String>> filterArrays,
                                                       String searchQuery,
                                                       int showAllTeams,
                                                       boolean checkLocation) {

        return filterTeamsService.criteriaFilter(enityClass, filterArrays.get(0),
            filterArrays.get(1), searchQuery, showAllTeams, checkLocation);
    }
}

