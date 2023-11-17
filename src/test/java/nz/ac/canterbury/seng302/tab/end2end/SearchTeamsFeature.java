package nz.ac.canterbury.seng302.tab.end2end;

import static com.microsoft.playwright.options.WaitForSelectorState.VISIBLE;

import com.microsoft.playwright.Page;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

/**
 * Test the search teams feature
 */
public class SearchTeamsFeature {
    @When("I hit the Search Teams button")
    public void hit_the_search_teams_button() {
        PlaywrightBrowser.page.locator("#search-dropdown-btn").click();
        PlaywrightBrowser.page.locator("#search-teams-btn").click();
    }


    @Then("I see a search form")
    public void see_a_search_form() {
        // Check we are taken to the search team page (which is the all teams page)
        String currentUrl = PlaywrightBrowser.page.url();
        Assertions.assertTrue(currentUrl.contains("/viewTeams"));
        // Check form is present
        PlaywrightBrowser.page.waitForSelector("#searchQuery",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#searchQuery"));
    }

    @Given("I am being shown a search form")
    public void am_being_shown_a_search_form() {
        PlaywrightBrowser.page.navigate(PlaywrightBrowser.baseUrl + "/viewTeams");
        // Check form is present
        PlaywrightBrowser.page.waitForSelector("#searchQuery",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#searchQuery"));
    }

    @When("I submit the search query {string}")
    public void submit_the_search_query(String query) {
        PlaywrightBrowser.page.locator("#searchQuery").fill(query);
        PlaywrightBrowser.page.locator("#submit-search-btn").click();
    }

    @Then("I see a list of teams which match the search query {string}")
    public void see_a_list_of_teams_which_match_the_search_query(String query) {
        Assertions.assertTrue(PlaywrightBrowser.page.locator("#team-name-index-0")
            .textContent().contains(query), "Team with name Canada National Team not found");
        Assertions.assertTrue(PlaywrightBrowser.page.locator("#team-name-index-1")
            .textContent().contains(query), "Team with name Brazilian National Team not found");
    }

    /**
     * Unable to check the error message you see when you input a string of less than 3 characters,
     * as it's not in the dom
     * so instead this checks that the search query is required
     */
    @Then("I see an error message telling me that my query is too short")
    public void see_an_error_message_telling_me_that_my_query_is_too_short() {
        String currentUrl = PlaywrightBrowser.page.url();
        Assertions.assertTrue(currentUrl.contains("/viewTeams"));
        Assertions.assertTrue(
            PlaywrightBrowser.page.locator("#searchQuery[required]")
                .count() > 0);
    }

    @Then("I see a message telling me there are no results")
    public void see_a_message_telling_me_there_are_no_results() {
        PlaywrightBrowser.page.waitForSelector(".text-center.mt-5",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.locator(".text-center.mt-5")
            .getByText("No Results").isVisible());
    }

    @Then("The teams are sorted correctly")
    public void the_teams_are_sorted_correctly() {

        String firstTeamName = PlaywrightBrowser.page.locator("#team-name-index-0").textContent();
        String secondTeamName = PlaywrightBrowser.page.locator("#team-name-index-1").textContent();
        Assertions.assertTrue(firstTeamName.compareToIgnoreCase(secondTeamName) < 0);
    }
}
