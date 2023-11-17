package nz.ac.canterbury.seng302.tab.end2end;


import static com.microsoft.playwright.options.WaitForSelectorState.VISIBLE;

import com.microsoft.playwright.Page;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;


/**
 * Cucumber for login feature
 */

public class JoinTeamFeature {

    @Given("I am anywhere on the system")
    public void am_anywhere_on_the_system() {
        PlaywrightBrowser.page.navigate(PlaywrightBrowser.baseUrl + "/viewTeams");
    }

    @When("I hit the view teams button")
    public void hit_the_view_teams_button() {
        PlaywrightBrowser.page.locator("#team-dropdown").click();
        PlaywrightBrowser.page.locator("#viewMyTeamsBtn").click();

    }

    @Then("I can choose to join a new team")
    public void can_choose_to_join_a_new_team() {

        // Check we are taken to the login page
        String currentUrl = PlaywrightBrowser.page.url();
        Assertions.assertTrue(currentUrl.contains("/viewTeams?showAllTeams=0"));
        PlaywrightBrowser.page.locator("#team-dropdown-btn").click();
        PlaywrightBrowser.page.locator("#joinTeamBtn").click();
        // Check form is present
        PlaywrightBrowser.page.waitForSelector("#userForm",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#userForm"));
    }

    @Given("I am being shown an input to join a team")
    public void am_being_shown_an_input_to_join_a_team() {
        PlaywrightBrowser.page.navigate(PlaywrightBrowser.baseUrl + "/viewTeams?showAllTeams=0");
        PlaywrightBrowser.page.locator("#joinTeamBtn").click();
    }

    @When("I input an invitation token that is associated to a team in the system")
    public void input_an_invitation_token_that_is_associated_to_a_team_in_the_system() {
        PlaywrightBrowser.page.locator("#invitation-token").fill("AAAAAAAAAAAA");
        PlaywrightBrowser.page.locator("#joinTeamSubmitBtnModal").click();
    }

    @When("I input an a valid invitation token that is not associated to a team in the system")
    public void input_a_valid_invitation_token_that_is_not_associated_to_a_team_in_the_system() {
        PlaywrightBrowser.page.locator("#invitation-token").fill("AAAAAAZAAAAA");
        PlaywrightBrowser.page.locator("#joinTeamSubmitBtnModal").click();
    }

    @Then("I am added as a member to this team")
    public void am_added_as_a_member_to_this_team() {
        String currentUrl = PlaywrightBrowser.page.url();
        Assertions.assertTrue(currentUrl.contains("viewTeam"));
        PlaywrightBrowser.page.waitForSelector("#joinTeamBtn",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#joinTeamBtn"));
    }

    @Then("An error message tells me the token is invalid")
    public void an_error_message_tells_me_the_token_is_invalid() {
        PlaywrightBrowser.page.waitForSelector("#userForm",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        PlaywrightBrowser.page.locator("#inviteError").textContent();
        Assertions.assertTrue(PlaywrightBrowser.page
            .locator("#inviteError").isVisible());
        Assertions.assertEquals("The Invite is invalid",
            PlaywrightBrowser.page.locator("#inviteError").textContent().trim());
    }

    @Given("I have joined a new team")
    public void have_joined_a_new_team() {
        PlaywrightBrowser.page.locator("#team-dropdown-btn").click();
        PlaywrightBrowser.page.locator("#viewMyTeamsBtn").click();
        PlaywrightBrowser.page.locator("#joinTeamBtn").click();
        PlaywrightBrowser.page.locator("#invitation-token").fill("AAAAAAAAAAAC");
        PlaywrightBrowser.page.locator("#joinTeamSubmitBtnModal").click();
        Assertions.assertFalse(PlaywrightBrowser.page.url().contains("tokenError"),
            "User is already a member of this team!");

    }

    @Then("I see the new team I just joined")
    public void see_the_new_team_i_just_joined() {
        PlaywrightBrowser.page.waitForSelector(".fw-bold",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        PlaywrightBrowser.page.locator("#page-2").click();
        Assertions.assertTrue(PlaywrightBrowser.page
            .getByText("Scotland National Team").isVisible());
    }

    @Given("I am logged in as team {int}")
    public void am_logged_in_as_team(int teamId) {
        PlaywrightBrowser.page.navigate(PlaywrightBrowser.baseUrl + "/");
        PlaywrightBrowser.page.locator("#loginButton").click();

        // Login
        PlaywrightBrowser.page.locator("#username")
            .fill("test" + teamId + "@email.com");
        PlaywrightBrowser.page.locator("#password").fill("*****");
        PlaywrightBrowser.page.locator("#loginSubmitButton").click();

        String currentUrl = PlaywrightBrowser.page.url();
        Assertions.assertTrue(currentUrl.contains("/profilePage"));
    }
}
