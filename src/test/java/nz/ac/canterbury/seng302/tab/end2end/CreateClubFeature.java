package nz.ac.canterbury.seng302.tab.end2end;

import static com.microsoft.playwright.options.WaitForSelectorState.VISIBLE;

import com.microsoft.playwright.Page;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

/**
 * E2E tests for U38.1 create club feature
 */
public class CreateClubFeature {

    @When("I click a UI element to create a club")
    public void i_click_a_ui_element_to_create_a_club() {
        PlaywrightBrowser.page.locator("#club-dropdown-btn").click();
        PlaywrightBrowser.page.locator("#createClubBtn").click();
    }

    @Then("I am presented with a club creation form")
    public void i_am_presented_with_a_club_creation_form() {

        PlaywrightBrowser.page.waitForSelector("#submit_button",
            new Page.WaitForSelectorOptions().setState(VISIBLE));

        Assertions.assertTrue(
            PlaywrightBrowser.page.locator("#submit_button").isVisible());
    }

    @Then("I am prompted to fill the required fields")
    public void i_am_prompted_to_fill_the_required_fields() {

        PlaywrightBrowser.page.waitForSelector("#name",
            new Page.WaitForSelectorOptions().setState(VISIBLE));

        PlaywrightBrowser.page.waitForSelector("#sport",
            new Page.WaitForSelectorOptions().setState(VISIBLE));

        PlaywrightBrowser.page.waitForSelector("#address1",
            new Page.WaitForSelectorOptions().setState(VISIBLE));

        PlaywrightBrowser.page.waitForSelector("#address2",
            new Page.WaitForSelectorOptions().setState(VISIBLE));

        PlaywrightBrowser.page.waitForSelector("#suburb",
            new Page.WaitForSelectorOptions().setState(VISIBLE));

        PlaywrightBrowser.page.waitForSelector("#city",
            new Page.WaitForSelectorOptions().setState(VISIBLE));

        PlaywrightBrowser.page.waitForSelector("#postcode",
            new Page.WaitForSelectorOptions().setState(VISIBLE));

        PlaywrightBrowser.page.waitForSelector("#country",
            new Page.WaitForSelectorOptions().setState(VISIBLE));

        Assertions.assertTrue(PlaywrightBrowser.page.locator("#name").isVisible());
        Assertions.assertTrue(PlaywrightBrowser.page.locator("#sport").isVisible());
        Assertions.assertTrue(PlaywrightBrowser.page.locator("#address1").isVisible());
        Assertions.assertTrue(PlaywrightBrowser.page.locator("#address2").isVisible());
        Assertions.assertTrue(PlaywrightBrowser.page.locator("#suburb").isVisible());
        Assertions.assertTrue(PlaywrightBrowser.page.locator("#city").isVisible());
        Assertions.assertTrue(PlaywrightBrowser.page.locator("#country").isVisible());
        Assertions.assertTrue(PlaywrightBrowser.page.locator("#postcode").isVisible());
    }

    @When("I fill the form with an empty {string}")
    public void i_fill_the_form_with_an_empty_name(String field) {
        PlaywrightBrowser.page.locator("#submit_button").click();
    }

    @Then("I see an error message for the {string} input")
    public void then_i_see_an_error_message_for_the_input(String field) {
        PlaywrightBrowser.page.waitForSelector("#" + field + "_error",
            new Page.WaitForSelectorOptions().setState(VISIBLE));

        Assertions.assertTrue(PlaywrightBrowser.page.locator(
            "#" + field + "_error").isVisible());
    }

    @And("I am creating a club")
    public void i_am_creating() {
        PlaywrightBrowser.page.locator("#club-dropdown-btn").click();
        PlaywrightBrowser.page.locator("#createClubBtn").click();
    }

    @When("I don't enter a city")
    public void i_dont_enter_a_city() {
        PlaywrightBrowser.page.locator("#name").fill("Chelsea");
        PlaywrightBrowser.page.locator("#sport").selectOption("Football");
        PlaywrightBrowser.page.locator("#city").fill("");
        PlaywrightBrowser.page.locator("#country").fill("New Zealand");

    }

    @Then("I see an error message")
    public void i_see_an_error_message() {
    }

    @When("I am on the edit team page of a team, that has a club")
    public void i_am_on_the_edit_team_page_of_a_team_that_has_a_club() {
        PlaywrightBrowser.page.navigate(PlaywrightBrowser.baseUrl
            + "/editTeamProfile?id=13&tab=Profile");
    }

    @Then("I cannot edit the sport")
    public void i_can_not_edit_the_sport() {
        Assertions.assertTrue(PlaywrightBrowser.page.waitForSelector("#sport").isDisabled());
    }


}
