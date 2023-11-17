package nz.ac.canterbury.seng302.tab.end2end;

import static com.microsoft.playwright.options.WaitForSelectorState.VISIBLE;

import com.microsoft.playwright.Page;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

/**
 * Create Formation Controller
 */
public class CreateFormationFeature {

    @When("I hit the see all formations button")
    public void i_hit_the_see_all_formations_button() {
        PlaywrightBrowser.page.locator("#nav-formations").click();
    }

    @Then("I can see the teams formations")
    public void i_can_see_the_teams_formations() {
        Assertions.assertTrue(PlaywrightBrowser.page.locator("#formation-list-group").textContent()
            .contains("1-3-4-2"));
    }

    @When("I click the create formation button")
    public void i_click_the_create_formation_button() {
        PlaywrightBrowser.page.locator("#createFormationButton").click();
    }

    @Then("I am given an option to select a formation")
    public void i_am_given_an_option_to_select_a_formation() {
        PlaywrightBrowser.page.waitForSelector("#formation",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#formation"));
    }

    @Given("I am on my first team’s profile")
    public void i_am_on_my_first_teams_profile() {
        PlaywrightBrowser.page.navigate(PlaywrightBrowser.baseUrl
            + "/viewTeam?id=2&page=&showAllTeams=0");
        String currentUrl = PlaywrightBrowser.page.url();
        Assertions.assertTrue(currentUrl.contains("/viewTeam"));
    }

    @When("I enter a formation form with an invalid formation field")
    public void i_enter_a_formation_form_with_an_invalid_formation_field() {
        PlaywrightBrowser.page.locator("#formation").fill("1-3-4-");
        PlaywrightBrowser.page.locator("#createFormationButton").click();
    }

    @Then("I can see an error message saying that the formation is invalid")
    public void i_can_see_an_error_message_saying_that_the_formation_is_invalid() {
        PlaywrightBrowser.page.waitForSelector("#formationError",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#formationError"));

        Assertions.assertTrue(PlaywrightBrowser.page.locator("#formationError").textContent()
            .contains("Please follow the pattern e.g 4-3-3"));

    }

    @And("I submit a valid formation form")
    public void i_submit_a_valid_formation_form() {
        PlaywrightBrowser.page.locator("#formation").fill("1-3-4-2");
        PlaywrightBrowser.page.locator("#sport").selectOption("Rugby");

        PlaywrightBrowser.page.locator("#createFormationButton").click();
    }

    @Then("I am given an option to select a sports field")
    public void i_am_given_an_option_to_select_a_sports_field() {
        PlaywrightBrowser.page.waitForSelector("#sport",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#sport"));
    }

    @When("I set up a formation")
    public void i_set_up_a_formation() {
        PlaywrightBrowser.page.locator("#formation").fill("1-3-4-2");
        PlaywrightBrowser.page.locator("#sport").selectOption("Rugby");
        PlaywrightBrowser.page.locator("#preview").click();
    }

    @Then("I see icons of players organised as described by the pattern on the graphical pitch")
    public void i_see_icons_of_players_organised_by_the_pattern_on_the_graphical_pitch() {
        PlaywrightBrowser.page.waitForSelector("#formation",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#formation"));
    }

    @When("I enter a valid formation form")
    public void i_enter_a_valid_formation_form() {
        PlaywrightBrowser.page.locator("#formation").fill("1-3-4-2");
        PlaywrightBrowser.page.locator("#sport").selectOption("Rugby");
        PlaywrightBrowser.page.locator("#createFormationButton").click();
    }

    @Then("the formation is persisted in the system")
    public void the_formation_is_persisted_in_the_system() {
        PlaywrightBrowser.page.waitForSelector("#displayedFormation",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#displayedFormation"));
    }
}
