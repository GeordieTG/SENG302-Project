package nz.ac.canterbury.seng302.tab.end2end;

import static com.microsoft.playwright.options.WaitForSelectorState.VISIBLE;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import org.junit.jupiter.api.Assertions;

/**
 * End-to-end tests for adding activity statistics
 */
public class AddActivityStatisticsFeature {
    @And("I click on a UI element to see all activities")
    public void i_click_on_a_ui_element_to_see_all_activities() {
        PlaywrightBrowser.page.locator("#activity-dropdown-btn").click();
        PlaywrightBrowser.page.locator("#viewActivityButton").click();
    }

    @And("I click on a UI element to see my first teams activities")
    public void i_click_on_a_ui_element_to_see_my_first_teams_activities() {
        PlaywrightBrowser.page.navigate(PlaywrightBrowser.baseUrl + "/viewActivity?id=1");
    }

    @When("I click on a UI element to add statistics")
    public void i_click_on_a_ui_element_to_add_statistics() {
        PlaywrightBrowser.page.locator("#addStatisticsButton").click();
    }

    @Then("I see a page to input statistics")
    public void i_see_a_page_to_input_statistics() {
        String url = PlaywrightBrowser.page.url();
        Assertions.assertTrue(url.contains("addActivityStatistics"));

        PlaywrightBrowser.page.waitForSelector("#scoringTableContainer",
            new Page.WaitForSelectorOptions().setState(VISIBLE));

        Assertions.assertTrue(PlaywrightBrowser.page.locator("#scoringTableContainer")
            .isVisible());
        Assertions.assertTrue(PlaywrightBrowser.page.locator("#substitutionTableContainer")
            .isVisible());
        Assertions.assertTrue(PlaywrightBrowser.page.locator("#addScore").isVisible());
    }

    @Then("I see inputs to record a scoring event")
    public void i_see_inputs_to_record_a_scoring_event() {
        String url = PlaywrightBrowser.page.url();
        Assertions.assertTrue(url.contains("addActivityStatistics"));

        PlaywrightBrowser.page.waitForSelector("#scoringTableContainer",
            new Page.WaitForSelectorOptions().setState(VISIBLE));

        Assertions.assertTrue(
            PlaywrightBrowser.page.locator("#scoringTableContainer").isVisible());
    }

    @When("I click the '+' button on the scoring table")
    public void i_click_the_plus_button_on_the_scoring_table() {
        Assertions.assertTrue(PlaywrightBrowser.page.locator("#addScoringEventButton")
            .isVisible());
        PlaywrightBrowser.page.locator("#addScoringEventButton").click();
    }

    @When("I click the '+' button on the substitution table")
    public void i_click_the_plus_button_on_the_substitution_table() {
        Assertions.assertTrue(PlaywrightBrowser.page.locator("#addSubstitutionEventButton")
            .isVisible());
        PlaywrightBrowser.page.locator("#addSubstitutionEventButton").click();
    }

    @Then("I am given another row to add an additional scoring event")
    public void i_am_given_another_row_to_add_an_additional_scoring_event() {
        String url = PlaywrightBrowser.page.url();
        Assertions.assertTrue(url.contains("addActivityStatistics"));

        PlaywrightBrowser.page.waitForSelector("#playerScoreForms1-scoredPlayerId",
            new Page.WaitForSelectorOptions().setState(VISIBLE));

        List<ElementHandle> childElements =
            PlaywrightBrowser.page.querySelector("#tableBody").querySelectorAll("tr");

        Assertions.assertTrue(childElements.size() > 1);
    }

    @Then("I am given another row to add an additional substitution event")
    public void i_am_given_another_row_to_add_an_additional_substitution_event() {
        String url = PlaywrightBrowser.page.url();
        Assertions.assertTrue(url.contains("addActivityStatistics"));

        List<ElementHandle> childElements =
            PlaywrightBrowser.page.querySelector("#substitutionTableBody").querySelectorAll("tr");

        Assertions.assertTrue(childElements.size() > 1);
    }

    @Then("I see inputs to record a substitution event")
    public void i_see_inputs_to_record_a_substitution_event() {
        String url = PlaywrightBrowser.page.url();
        Assertions.assertTrue(url.contains("addActivityStatistics"));

        PlaywrightBrowser.page.waitForSelector("#substitutionTableContainer",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
    }

    @Then("I can see inputs to record the outcome of the activity")
    public void i_can_see_inputs_to_record_the_outcome_of_the_activity() {
        String url = PlaywrightBrowser.page.url();
        Assertions.assertTrue(url.contains("addActivityStatistics"));
        Assertions.assertTrue(PlaywrightBrowser.page.locator("#btnWin").isVisible());
        Assertions.assertTrue(PlaywrightBrowser.page.locator("#btnLoss").isVisible());
        Assertions.assertTrue(PlaywrightBrowser.page.locator("#btnDraw").isVisible());
    }

    @When("I click on a UI element to add facts")
    public void i_click_on_a_ui_element_to_add_facts() {
        PlaywrightBrowser.page.locator("#addActivityFactsButton").click();
    }

    @Then("I see a page to input facts with appropriate fields")
    public void i_see_a_page_to_input_facts_with_appropriate_fields() {
        String url = PlaywrightBrowser.page.url();
        Assertions.assertTrue(url.contains("addActivityFacts"));

        PlaywrightBrowser.page.waitForSelector("#fact-description-0",
            new Page.WaitForSelectorOptions().setState(VISIBLE));

        Assertions.assertTrue(PlaywrightBrowser.page.locator("#fact-description-0").isVisible());
        Assertions.assertTrue(PlaywrightBrowser.page.locator("#fact-time-0").isVisible());
    }

    @Then("I can see a dedicated UI element to add activity facts")
    public void i_can_see_a_dedicated_ui_element_to_add_activity_facts() {
        PlaywrightBrowser.page.locator("#addFactsButton").isVisible();
    }

    @When("I click the '+' button")
    public void i_click_the_add_button() {
        PlaywrightBrowser.page.locator("#addFactEventButton").click();
    }

    @Then("I am given another row to add an additional fact")
    public void i_am_given_another_row_to_add_an_additional_fact() {
        Assertions.assertTrue(PlaywrightBrowser.page.locator("#fact-description-1").isVisible());
        Assertions.assertTrue(PlaywrightBrowser.page.locator("#fact-time-1").isVisible());
    }

    @When("I enter scores in the format {string} for a team, "
        + "with the other team must be in the same format {string}.")
    public void i_enter_scores_with_matching_format(
        String string, String string2) {
        PlaywrightBrowser.page.locator("#teamScore").fill(string);
        PlaywrightBrowser.page.locator("#oppoScore").fill(string2);
    }

    @Then("I can see the input are valid")
    public void i_can_see_the_inputs_are_valid() {
        String teamScoreClass = PlaywrightBrowser.page.locator("#teamScore")
            .getAttribute("class");
        String oppoScoreClass = PlaywrightBrowser.page.locator("#oppoScore")
            .getAttribute("class");
        Assertions.assertTrue(teamScoreClass.contains("is-valid"));
        Assertions.assertTrue(oppoScoreClass.contains("is-valid"));
    }

    @Then("I can see the input are invalid")
    public void i_can_see_the_inputs_are_invalid() {
        String teamScoreClass = PlaywrightBrowser.page.locator("#teamScore")
            .getAttribute("class");
        String oppoScoreClass = PlaywrightBrowser.page.locator("#oppoScore")
            .getAttribute("class");
        Assertions.assertTrue(teamScoreClass.contains("is-invalid"));
        Assertions.assertTrue(oppoScoreClass.contains("is-invalid"));
    }

    @Then("I can see a dedicated UI element to add Activity statistics.")
    public void i_can_see_a_dedicated_ui_element_to_add_activity_statistics() {

        PlaywrightBrowser.page.waitForSelector("#addStatisticsBtn",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#addStatisticsBtn"));

    }
}