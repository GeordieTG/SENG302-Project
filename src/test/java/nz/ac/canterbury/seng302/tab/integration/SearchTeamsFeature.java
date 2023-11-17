package nz.ac.canterbury.seng302.tab.integration;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.ArrayList;
import java.util.List;
import nz.ac.canterbury.seng302.tab.entity.Team;
import nz.ac.canterbury.seng302.tab.service.FilterTeamsService;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Integration test for Search teams
 */
@SpringBootTest
@AutoConfigureMockMvc
public class SearchTeamsFeature {

    @Autowired
    private FilterTeamsService filterTeamsService;

    private List<List<String>> filterArrays;

    @When("I enter a search string of at least 3 characters")
    public void i_enter_a_search_string_of_at_least_3_characters() {
        filterArrays = new ArrayList<>();
        filterArrays.add(List.of("none"));
        filterArrays.add(List.of("none"));
    }

    @Then("I see a list of all the teams matching the search query")
    public void i_see_a_list_of_all_the_teams_matching_the_search_query() {

        List<?> pages =
            filterTeamsService.criteriaFilter(Team.class, filterArrays.get(0), filterArrays.get(1),
                "Raith", 1, false);
        Assertions.assertEquals(1, pages.size());

    }
}
