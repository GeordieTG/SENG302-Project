package nz.ac.canterbury.seng302.tab.end2end;

import static com.microsoft.playwright.options.WaitForSelectorState.VISIBLE;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;


/**
 * Cucumber for login feature
 */

public class FilterItemBySportFeature {

    String selectedSport;

    String firstSelectedSport;
    String secondSelectedSport;

    int numberOfItemsReturned;

    @When("I select a sport from a list of sports known by the system")
    public void select_a_sport_from_a_list_of_sports_known_by_the_system() {
        PlaywrightBrowser.page.locator("#sport-filter-dropdown-btn").click();
        selectedSport = PlaywrightBrowser.page.locator(".sport-index-3").inputValue();
        PlaywrightBrowser.page.locator(".sport-index-3").click();
    }

    @Then("only the {string}s with the selected sport are displayed.")
    public void only_the_items_with_the_selected_sport_are_displayed(String itemType) {
        Assertions.assertTrue(PlaywrightBrowser.page.locator(
                "#" + itemType + "-sport-index-0")
            .textContent().equalsIgnoreCase(selectedSport), "Team sport is not: "
            + selectedSport);
        numberOfItemsReturned = PlaywrightBrowser.page.locator("." + itemType).count();
    }

    @When("I select more than one sport from a list of sports known by the system, "
        + "when filtering {string}s")
    public void select_more_than_one_sport_from_a_list_of_sports_known_by_the_system(
        String itemType) {
        PlaywrightBrowser.page.locator("#sport-filter-dropdown-btn").click();
        firstSelectedSport = PlaywrightBrowser.page.locator(".sport-index-0").inputValue();
        PlaywrightBrowser.page.locator(".sport-index-0").click();
        PlaywrightBrowser.page.locator("#sport-filter-dropdown-btn").click();
        secondSelectedSport = PlaywrightBrowser.page.locator(".sport-index-1").inputValue();
        PlaywrightBrowser.page.locator(".sport-index-1").click();
        numberOfItemsReturned = PlaywrightBrowser.page.locator("." + itemType).count();
    }

    @Then("All {string}s with any of the selected sports are displayed")
    public void all_teams_with_any_of_the_selected_sports_are_displayed(String itemType) {

        int itemCount = PlaywrightBrowser.page.locator("." + itemType).count();
        for (int i = 0; i < itemCount; i++) {
            String sport =
                PlaywrightBrowser.page.locator("#" + itemType + "-sport-index-" + i).textContent();
            Assertions.assertTrue(sport.equalsIgnoreCase(firstSelectedSport)
                    || sport.equalsIgnoreCase(secondSelectedSport),
                "Expected Team sport: " + firstSelectedSport + " or " + secondSelectedSport
                    + " Actual Sport: " + sport);
        }

    }

    @When("I deselect one or more sports from a list of sports known by the system, "
        + "when filtering {string}s")
    public void deselect_one_or_more_sports_from_a_list_of_sports_known_by_the_system(
        String itemType) {
        PlaywrightBrowser.page.locator("#sport-filter-dropdown-btn").click();
        secondSelectedSport = PlaywrightBrowser.page.locator(".sport-index-1").inputValue();
        PlaywrightBrowser.page.locator(".sport-index-1").click();
        numberOfItemsReturned = PlaywrightBrowser.page.locator("." + itemType).count();
    }

    @Then("The list of {string}s updates according to the selected sports.")
    public void the_list_of_items_updates_according_to_the_selected_sports(String itemType) {
        int teamCount = PlaywrightBrowser.page.locator("." + itemType).count();
        for (int i = 0; i < teamCount; i++) {
            String sport = PlaywrightBrowser.page.locator(
                "#" + itemType + "-sport-index-" + i).textContent();
            Assertions.assertTrue(sport.equalsIgnoreCase(firstSelectedSport),
                "Expected " + itemType + " sport: " + firstSelectedSport + " or "
                    + secondSelectedSport + " Actual Sport: " + sport);
        }
    }

    @When("I click the Reset button")
    public void click_the_button() {
        PlaywrightBrowser.page.locator("#reset-filter-btn").click();
    }

    @Given("I am being shown a user search form")
    public void am_being_shown_a_user_search_form() {
        PlaywrightBrowser.page.navigate(PlaywrightBrowser.baseUrl + "/allUsers");
        // Check form is present
        PlaywrightBrowser.page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        PlaywrightBrowser.page.waitForSelector(
            "#searchName", new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#searchName"));
    }

    @Then("The list of teams refreshes without any filters or search applied")
    public void the_list_of_teams_refreshes_without_any_filters_or_search_applied() {
        int numberOfSports = PlaywrightBrowser.page.locator(".sport").count();
        for (int i = 0; i < numberOfSports; i++) {
            String sport = PlaywrightBrowser.page.locator(".sport-index-" + i).inputValue();
            Assertions.assertFalse(
                PlaywrightBrowser.page.locator(".sport-index-" + i).isChecked(),
                "The Sport: " + sport + " is checked!");
        }
        PlaywrightBrowser.page.waitForSelector("#searchQuery",
            new Page.WaitForSelectorOptions().setState(VISIBLE).setTimeout(30000));
        Assertions.assertTrue(
            PlaywrightBrowser.page.locator("#searchQuery").inputValue().isEmpty()
                || PlaywrightBrowser.page.locator("#searchQuery")
                .inputValue().equals("none"));

    }
}