package nz.ac.canterbury.seng302.tab.end2end;

import static com.microsoft.playwright.options.WaitForSelectorState.VISIBLE;

import com.microsoft.playwright.Page;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

/**
 * Test for Create a Team Token using E2E testing
 */
public class CreateTeamTokenFeature {
    /**
     * Team page URl
     */
    private String teamUrl;

    /**
     * Empty string to ensure a token is generated
     */
    private String tokenStorage;

    /**
     * Retrieve the token from the page
     *
     * @return a string containing the teams token
     */
    public String retrieve_token() {
        PlaywrightBrowser.page.locator("#ShowToken").click();
        String token = PlaywrightBrowser.page.locator("#invitation-token-section").inputValue();
        return token;
    }

    @Given("I have created a team {string}")
    public void have_created_team(String name) {
        PlaywrightBrowser.page.locator("#team-dropdown-btn").click();
        PlaywrightBrowser.page.getByText("Create Team").click();
        Assertions.assertTrue(PlaywrightBrowser.page.url().contains("/createTeam"));

        PlaywrightBrowser.page.locator("#team_name").fill(name);
        PlaywrightBrowser.page.locator("#sport").selectOption("Football");
        PlaywrightBrowser.page.locator("#next_button").click();

        PlaywrightBrowser.page.getByPlaceholder("City").fill("Auckland");
        PlaywrightBrowser.page.getByPlaceholder("Country").fill("New Zealand");
        PlaywrightBrowser.page.locator("#submit_button").click();

        teamUrl = PlaywrightBrowser.page.url();
    }

    @When("A unique token was created for the team")
    public void a_unique_token_was_created_for_the_team() {
        PlaywrightBrowser.page.locator("#teamMembersBtn").click();
        PlaywrightBrowser.page.getByText("Generate").click();
        PlaywrightBrowser.page.locator("#teamMembersBtn").click();
        PlaywrightBrowser.page.waitForSelector("#ShowToken",
            new Page.WaitForSelectorOptions().setState(VISIBLE)).click();
        String token = PlaywrightBrowser.page.locator("#invitation-token-section").innerText();
        Assertions.assertNotNull(token);
    }

    @When("I am on the team profile page of the created team {string}")
    public void am_on_the_team_profile_page_of_the_created_team(String name) {
        PlaywrightBrowser.page.navigate(teamUrl);
        PlaywrightBrowser.page.getByText(name);

    }

    @Then("I can see a unique secret token")
    public void can_see_a_unique_secret_token() {
        String token = retrieve_token();
        Assertions.assertNotNull(token);
    }

    @Then("Token is exactly 12 characters long")
    public void token_is_exactly_12_characters_long() {
        String token = retrieve_token();
        Assertions.assertEquals(12, token.length());
    }

    /**
     * Includes 'check if the token does not contain special characters' segment of AC
     */
    @Then("Token is a combination of only letters and numbers")
    public void token_is_a_combination_of_only_letters_and_numbers() {
        String token = retrieve_token();
        Assertions.assertTrue(token.matches("^[a-zA-Z0-9]*$"));
    }

    /**
     * Includes the AC segment, 'The token is not a repeat of the previous token'
     */
    @Then("A new token is generated")
    public void a_new_token_is_generated() {
        String newToken = retrieve_token();

        Assertions.assertNotEquals(tokenStorage, newToken);
    }
}
