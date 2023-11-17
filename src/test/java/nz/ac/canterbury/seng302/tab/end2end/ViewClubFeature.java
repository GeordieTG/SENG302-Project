package nz.ac.canterbury.seng302.tab.end2end;

import static com.microsoft.playwright.options.WaitForSelectorState.VISIBLE;

import com.microsoft.playwright.Page;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

/**
 * End to end  java feature for  view club
 */
public class ViewClubFeature {

    @When("I am on the club's profile page looking at the list of teams within the club,")
    public void i_am_on_the_club_s_profile_page_looking_at_the_list_of_teams_within_the_club() {
        PlaywrightBrowser.page.locator("#submit_button").click();
    }

    @Then("I can click on any of the teams on the list")
    public void i_can_click_on_any_of_the_teams_on_the_list() {
        PlaywrightBrowser.page.locator("#team-myTeam").click();
    }

    @Then("be redirected to the team's profile page.")
    public void be_redirected_to_the_team_s_profile_page() {
        String currentUrl = PlaywrightBrowser.page.url();
        Assertions.assertTrue(currentUrl.contains("/viewTeam"));
    }

    @Given("I created a new club or viewing an existing club,")
    public void i_have_created_a_new_club_or_viewing_an_existing_club() {
        PlaywrightBrowser.page.locator("#club-dropdown-btn").click();
        PlaywrightBrowser.page.locator("#createClubBtn").click();
        PlaywrightBrowser.page.locator("#name").fill("Chelsea");

        PlaywrightBrowser.page.locator("#city").fill("Christchurch");
        PlaywrightBrowser.page.locator("#country").fill("New Zealand");
        PlaywrightBrowser.page.locator("#sport").selectOption("Football");
        PlaywrightBrowser.page.locator("#sport").click();
        PlaywrightBrowser.page.locator("#sport").click();


        PlaywrightBrowser.page.locator("#dropdownController").click();
        PlaywrightBrowser.page.locator("#team-1-checkbox-label").click();


    }

    @Then("I can see {string} name and default image")
    public void i_can_see_name_and_default_image(String string) {
        PlaywrightBrowser.page.waitForSelector("#" + "clubSectionForNoClub",
            new Page.WaitForSelectorOptions().setState(VISIBLE));

        Assertions.assertTrue(PlaywrightBrowser.page.locator(
            "#" + "clubSectionForNoClub").isVisible());
    }

}
