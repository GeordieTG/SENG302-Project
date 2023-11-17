package nz.ac.canterbury.seng302.tab.end2end;

import static com.microsoft.playwright.options.WaitForSelectorState.VISIBLE;

import com.microsoft.playwright.Page;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;

/**
 * Cucumber for view team activities feature
 */
public class ViewTeamActivitiesFeature {

    @Given("I am on my team’s profile")
    public void am_on_my_team_s_profile() {
        PlaywrightBrowser.page.locator("#team-dropdown-btn").click();
        PlaywrightBrowser.page.locator("#viewMyTeamsBtn").click();
        PlaywrightBrowser.page.locator("#page-2").click();
        PlaywrightBrowser.page.locator("#index-2").click();
        String currentUrl = PlaywrightBrowser.page.url();
        Assertions.assertTrue(currentUrl.contains("/viewTeam"));
    }

    @Given("I am on my team’s profile which id is {int}")
    public void am_on_my_team_s_profile_which_id_is(Integer int1) {
        PlaywrightBrowser.page.locator("#team-dropdown-btn").click();
        PlaywrightBrowser.page.locator("#viewMyTeamsBtn").click();
        PlaywrightBrowser.page.locator("#page-2").click();
        PlaywrightBrowser.page.locator("#index-" + int1).click();
        String currentUrl = PlaywrightBrowser.page.url();
        Assertions.assertTrue(currentUrl.contains("/viewTeam"));
    }

    @When("I click on a UI element to see all the team’s activities,")
    public void click_on_a_ui_element_to_see_all_the_team_s_activities() {

        PlaywrightBrowser.page.locator("#team-activity-button").click();

    }

    @Given("I enter a valid Create Activity form with a team :")
    public void enter_a_valid_create_activity_form_with_a_team(
        io.cucumber.datatable.DataTable dataTable) {

        List<Map<String, String>> activities = dataTable.asMaps(String.class, String.class);
        PlaywrightBrowser.page.locator("#activity-dropdown-btn").click();
        PlaywrightBrowser.page.locator("#createActivityButton").click();
        for (Map<String, String> activity : activities) {

            // Do something with the team data, such as creating an activity
            PlaywrightBrowser.page.navigate(PlaywrightBrowser.baseUrl + "/createActivity");
            String teamId = activity.get("teamid");
            PlaywrightBrowser.page.locator("#team").selectOption(teamId);
            String type = activity.get("type");
            PlaywrightBrowser.page.locator("#type").selectOption(type);
            String startDate = activity.get("startDate");
            PlaywrightBrowser.page.locator("#startTime").fill(startDate);
            String endDate = activity.get("endDate");
            PlaywrightBrowser.page.locator("#endTime").fill(endDate);
            String description = activity.get("description");
            PlaywrightBrowser.page.locator("#description").fill(description);
            PlaywrightBrowser.page.locator("#nextPageButton").click();

            PlaywrightBrowser.page.locator("#address1").fill("Greymouth");
            PlaywrightBrowser.page.locator("#postcode").fill("7805");
            PlaywrightBrowser.page.locator("#city").fill("Greymouth");
            PlaywrightBrowser.page.locator("#country").fill("New Zealand");

            PlaywrightBrowser.page.locator("#createActivityButton").last().click();
        }
    }

    @Then("I see Team activity page.")
    public void see_team_activity_page() {
        PlaywrightBrowser.page.waitForSelector("#today-btn",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#today-btn"));
    }

    @Then("I see a list of all activities for that team.")
    public void see_a_list_of_all_activities_for_that_team() {
        String classes = PlaywrightBrowser.page.locator("#tab-calendar").getAttribute("class");
        Assertions.assertTrue(classes.contains("active"));

    }

    @And("I click on a UI element to see all the team’s activities")
    public void click_on_a_ui_element_to_see_all_the_teams_activities() {
        PlaywrightBrowser.page.locator("#team-activity-button").click();
    }


    @When("I am the Manager of the team")
    public void am_the_manager_of_the_team() {
    }

    @Then("I can see an edit activity button.")
    public void can_see_the_edit_button() {
        PlaywrightBrowser.page.locator(".toastui-calendar-template-time").first().click();
        PlaywrightBrowser.page.locator(".toastui-calendar-template-time").first().click();
        PlaywrightBrowser.page.waitForSelector("#editActivityButton",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#editActivityButton"));
    }


    @And("It is next to the activity entry in the list.")
    public void it_is_next_to_the_activity_entry_in_the_list() {
        boolean isEditButtonPresent = PlaywrightBrowser.page
            .querySelector(".stretched-link #edit-activity-button") != null;
        Assertions.assertTrue(isEditButtonPresent);
    }

    @And("The button links to the edit activity page.")
    public void the_button_links_to_the_edit_activity_page() {
        PlaywrightBrowser.page.locator("#edit-activity-button").first().click();
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#nextPageButton"));
    }
}
