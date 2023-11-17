package nz.ac.canterbury.seng302.tab.end2end;

import static com.microsoft.playwright.options.WaitForSelectorState.VISIBLE;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import org.junit.jupiter.api.Assertions;

/**
 * End-to-End Tests for the Edit Lineup Functionality on the Activity Details Page
 */
public class EditLineupForGame {

    @Then("There should be a section for the user to add a lineup for the game")
    public void there_should_be_a_section_for_the_user_to_add_a_lineup_for_the_game() {
        PlaywrightBrowser.page.locator("#switchToEditLineUp").click();
        PlaywrightBrowser.page.waitForSelector("#formation-list-group",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#formation-list-group"));
    }

    @When("I select a formation from the formation dropdown and set up")
    public void select_a_formation_from_the_formation_dropdown_and_set_up() {
        PlaywrightBrowser.page.locator("#switchToEditLineUp").click();
        Locator selectLocator = PlaywrightBrowser.page.locator("#formation-list-group");
        ElementHandle selectElement = selectLocator.elementHandle();
        List<ElementHandle> options = selectElement.querySelectorAll("option");
        ElementHandle lastOption = options.get(options.size() - 1);
        String lastOptionValue = lastOption.getAttribute("value");
        selectElement.selectOption(lastOptionValue);

        PlaywrightBrowser.page.locator("#formation-list-group").selectOption(lastOptionValue);
        PlaywrightBrowser.page.waitForSelector("#changeFormationBtn").click();
    }

    @When("I select a simple formation from the formation dropdown and set up")
    public void select_a_simple_formation_from_the_formation_dropdown_and_set_up() {
        PlaywrightBrowser.page.locator("#switchToEditLineUp").click();
        Locator selectLocator = PlaywrightBrowser.page.locator("#formation-list-group");
        ElementHandle selectElement = selectLocator.elementHandle();
        List<ElementHandle> options = selectElement.querySelectorAll("option");
        ElementHandle lastOption = options.get(options.size() - 1);
        String lastOptionValue = lastOption.getAttribute("value");
        selectElement.selectOption(lastOptionValue);

        PlaywrightBrowser.page.locator("#formation-list-group").selectOption(lastOptionValue);
        PlaywrightBrowser.page.waitForSelector("#changeFormationBtn").click();
    }

    @Then("Then the formation is displayed on the screen")
    public void then_the_formation_is_displayed_on_the_screen() {
        PlaywrightBrowser.page.waitForSelector("#formation",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#formation"));

        String imageSrc = PlaywrightBrowser.page.locator("#field").getAttribute("src");
        Assertions.assertEquals("images/fields/Rugby.png", imageSrc);
    }

    @When("I click on a players icon")
    public void click_on_a_players_icon() {
        PlaywrightBrowser.page.waitForSelector("#p2-0",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#p2-0"));
    }

    @When("I can select a player from my team to fill in that position")
    public void can_select_a_player_from_my_team_to_fill_in_that_position() {
        PlaywrightBrowser.page.selectOption("#p1-0", "2");
    }

    @And("I submit a valid formation form with a simple formation")
    public void submit_a_valid_formation_form() {
        PlaywrightBrowser.page.locator("#formation").fill("1");
        PlaywrightBrowser.page.locator("#sport").selectOption("Rugby");
        PlaywrightBrowser.page.locator("#createFormationButton").click();
    }

    @When("I can see a section for substitutes")
    public void can_see_a_section_for_substitutes() {
        PlaywrightBrowser.page.waitForSelector("#substitutions",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#substitutions"));
    }

    @When("I select a player as a substitute")
    public void select_a_player_as_a_substitute() {
        PlaywrightBrowser.page.selectOption("#p1-0", "");
        PlaywrightBrowser.page.selectOption("#ps-0", "2");
    }

    @When("I select a player as a starter")
    public void select_a_player_as_a_starter() {
        PlaywrightBrowser.page.selectOption("#p1-0", "2");
    }

    @When("The sub is displayed on the screen")
    public void the_sub_is_displayed_on_the_screen() {
        String imageSrc = PlaywrightBrowser.page.locator("#psImg0").getAttribute("src");
        Assertions.assertNotSame("/images/default.jpg", imageSrc);
    }

    @When("The starter is displayed on the screen")
    public void the_starter_is_displayed_on_the_screen() {
        String imageSrc = PlaywrightBrowser.page.locator("#p1Img0").getAttribute("src");
        Assertions.assertNotSame("/images/default.jpg", imageSrc);
    }

    @When("I don't fill in all the positions with players")
    public void dont_fill_in_all_the_positions_with_players() {
        String imageSrc = PlaywrightBrowser.page.locator("#p1Img0").getAttribute("src");
        Assertions.assertEquals("images/default.jpg", imageSrc);
    }

    @Then("An error message tells me that the formation is not complete")
    public void an_error_message_tells_me_that_the_formation_is_not_complete() {
        PlaywrightBrowser.page.locator("#saveLineUpBtn").click();

        PlaywrightBrowser.page.waitForSelector("#lineUpErrorToast",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#lineUpErrorToast"));

    }

    @When("I cancel")
    public void cancel() {

        PlaywrightBrowser.page.locator("#showCancelbtn").click();

        PlaywrightBrowser.page.waitForSelector("#cancelLineUp",
            new Page.WaitForSelectorOptions().setState(VISIBLE));

        PlaywrightBrowser.page.locator("#cancelLineUp").click();

    }

    @Then("The current changes to the formation are ignored")
    public void the_current_changes_to_the_formation_are_ignored() {

        Assertions.assertTrue(PlaywrightBrowser.page.isHidden("#formation"));

    }

    @And("I save the line-up")
    public void save_the_line_up() {
        PlaywrightBrowser.page.locator("#saveLineUpBtn").click();
    }
}
