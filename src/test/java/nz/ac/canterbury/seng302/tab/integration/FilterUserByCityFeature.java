package nz.ac.canterbury.seng302.tab.integration;


import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.ArrayList;
import java.util.List;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.service.FilterUsersService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Integration test for Seraching user by City
 */
@SpringBootTest
@AutoConfigureMockMvc
public class FilterUserByCityFeature {

    @Autowired
    TabUserService tabUserService;
    @Autowired
    private FilterUsersService filterUsersService;
    private List<List<String>> filterArrays;

    @When("I select a city to filter by")
    public void i_select_a_city_to_filter_by() {
        filterArrays = new ArrayList<>();
        filterArrays.add(List.of("Exotic"));
        filterArrays.add(List.of("none"));
    }

    @When("I select more than one city to filter by")
    public void i_select_more_than_one_sport_to_filter_by() {
        filterArrays = new ArrayList<>();
        filterArrays.add(List.of("Xadia", "Gondor"));
        filterArrays.add(List.of("none"));
    }


    @Then("only the users with the selected city are displayed.")
    public void only_the_users_with_the_selected_city_are_displayed() {
        List<TabUser> pages =
            filterUsersService.criteriaFilter(filterArrays.get(0), filterArrays.get(1),
                0, "none", true);
        Assertions.assertEquals(3, pages.size());
    }

    @Then("all the users with the selected city are displayed.")
    public void all_the_users_with_the_selected_sport_are_displayed() {
        List<TabUser> pages =
            filterUsersService.criteriaFilter(filterArrays.get(0), filterArrays.get(1),
                0, "none", true);
        Assertions.assertEquals(6, pages.size());
    }

    @When("I deselect a city to filter by")
    public void i_deselect_a_sport_to_filter_by() {
        filterArrays = new ArrayList<>();
        filterArrays.add(List.of("Unique"));
        filterArrays.add(List.of("none"));
    }

    @When("I click reset")
    public void i_click_reset() {
        filterArrays = new ArrayList<>();
        filterArrays.add(List.of("none"));
        filterArrays.add(List.of("none"));
    }

    @Then("all the users are displayed.")
    public void all_the_users_are_displayed() {
        List<TabUser> pages =
            filterUsersService.criteriaFilter(filterArrays.get(0), filterArrays.get(1),
                0, "none", true);
        Assertions.assertEquals(tabUserService.getFormResults().size(), pages.size());
    }
}
