package nz.ac.canterbury.seng302.tab.integration;


import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.ArrayList;
import java.util.List;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.service.FilterUsersService;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Integration Test for searching users by Sport
 */
@SpringBootTest
@AutoConfigureMockMvc
public class FilterUserBySportFeature {

    @Autowired
    private FilterUsersService filterUsersService;

    private List<List<String>> filterArrays;


    @When("I select a sport to filter by")
    public void i_select_a_sport_to_filter_by() {
        filterArrays = new ArrayList<>();
        filterArrays.add(List.of("none"));
        filterArrays.add(List.of("Golf"));
    }

    @Then("only the users with the selected sport are displayed.")
    public void only_the_users_with_the_selected_sport_are_displayed() {

        List<TabUser> pages =
            filterUsersService.criteriaFilter(filterArrays.get(0), filterArrays.get(1),
                0, "none", true);
        Assertions.assertEquals(3, pages.size());
    }

    @When("I select more than one sport to filter by")
    public void i_select_more_than_one_sport_to_filter_by() {
        filterArrays = new ArrayList<>();
        filterArrays.add(List.of("none"));
        filterArrays.add(List.of("Golf", "Futsal"));
    }

    @Then("all the users with the selected sport are displayed.")
    public void all_the_users_with_the_selected_sport_are_displayed() {
        List<TabUser> pages =
            filterUsersService.criteriaFilter(filterArrays.get(0), filterArrays.get(1),
                0, "none", true);
        Assertions.assertEquals(6, pages.size());
    }

    @When("I deselect a sport to filter by")
    public void i_deselect_a_sport_to_filter_by() {
        filterArrays = new ArrayList<>();
        filterArrays.add(List.of("none"));
        filterArrays.add(List.of("Hockey"));
    }

}