package nz.ac.canterbury.seng302.tab.end2end;

import static com.microsoft.playwright.options.WaitForSelectorState.VISIBLE;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Assertions;

/**
 * Test the Edit Activity feature using E2E testing.
 */
public class EditActivityFeature {


    @Given("I navigate to the Edit Activity form")
    public void navigate_to_the_edit_activity_form() {

        PlaywrightBrowser.page.locator("#editActivityButton").click();

        // Check we are taken to the editActivity page
        String currentUrl = PlaywrightBrowser.page.url();
        Assertions.assertTrue(currentUrl.contains("/editActivity"));

    }

    @When("I enter a valid Edit Activity form without a team")
    public void enter_a_valid_edit_activity_form_without_a_team() {

        PlaywrightBrowser.page.locator("#team").selectOption("-1");
        PlaywrightBrowser.page.locator("#type").selectOption("Competition");
        PlaywrightBrowser.page.locator("#startTime").fill("2023-05-02T12:45");
        PlaywrightBrowser.page.locator("#endTime").fill("2023-05-05T14:00");
        PlaywrightBrowser.page.locator("#description").fill("Change of plans, it's a competition");

        PlaywrightBrowser.page.locator("#nextPageButton").click();

        PlaywrightBrowser.page.locator("#address1").fill("Place over the hill");
        PlaywrightBrowser.page.locator("#postcode").fill("7089");
        PlaywrightBrowser.page.locator("#city").fill("Kyoto");
        PlaywrightBrowser.page.locator("#country").fill("Japan");


        PlaywrightBrowser.page.locator("#saveActivityButton").click();

    }

    @Then("I see the edited activity details without the team")
    public void see_the_edited_activity_details_without_the_team() {
        // Check we are taken to the viewActivity page
        String currentUrl = PlaywrightBrowser.page.url();
        Assertions.assertTrue(currentUrl.contains("/viewActivity"));
        // Ensure details are showing correctly
        Locator type = PlaywrightBrowser.page.locator("#activityType");
        Locator startTime = PlaywrightBrowser.page.locator("#startTime");
        Locator endTime = PlaywrightBrowser.page.locator("#endTime");
        Locator description = PlaywrightBrowser.page.locator("#description");

        Assertions.assertEquals("Competition", type.textContent());
        Assertions.assertEquals("2023-05-02 - ", startTime.textContent());
        Assertions.assertEquals("2023-05-05", endTime.textContent());
        Assertions.assertEquals("Change of plans, it's a competition", description.textContent());
    }

    @When("I enter a valid Edit Activity form with a team")
    public void enter_a_valid_edit_activity_form_with_a_team() {
        PlaywrightBrowser.page.locator("#team").selectOption("2");
        PlaywrightBrowser.page.locator("#type").selectOption("Competition");
        PlaywrightBrowser.page.locator("#startTime").fill("2024-05-02T12:45");
        PlaywrightBrowser.page.locator("#endTime").fill("2024-05-05T14:00");
        PlaywrightBrowser.page.locator("#description").fill("Change of plans, it's a competition");

        PlaywrightBrowser.page.locator("#nextPageButton").click();

        PlaywrightBrowser.page.locator("#address1").fill("Place over the hill");
        PlaywrightBrowser.page.locator("#postcode").fill("7089");
        PlaywrightBrowser.page.locator("#city").fill("Kyoto");
        PlaywrightBrowser.page.locator("#country").fill("Japan");

        PlaywrightBrowser.page.locator("#saveActivityButton").click();
    }

    @Then("I see the edited activity details with the team")
    public void see_the_edited_activity_details_with_the_team() {
        // Check we are taken to the viewActivity page
        String currentUrl = PlaywrightBrowser.page.url();

        Assertions.assertTrue(currentUrl.contains("/viewActivity"));


        // Ensure details are showing correctly
        Locator team = PlaywrightBrowser.page.locator("#team");
        Locator type = PlaywrightBrowser.page.locator("#activityType");
        Locator startTime = PlaywrightBrowser.page.locator("#startTime");
        Locator endTime = PlaywrightBrowser.page.locator("#endTime");
        Locator description = PlaywrightBrowser.page.locator("#description");

        Assertions.assertEquals("Scotland National Team", team.textContent());
        Assertions.assertEquals("Competition", type.textContent());
        Assertions.assertEquals("2024-05-02 - ", startTime.textContent());
        Assertions.assertEquals("2024-05-05", endTime.textContent());
        Assertions.assertEquals("Change of plans, it's a competition", description.textContent());
    }

    @When("I enter a Edit Activity form with activity type that requires a team with no team")
    public void enter_a_edit_activity_form_with_activity_type_that_requires_a_team_with_no_team() {

        PlaywrightBrowser.page.locator("#team").selectOption("-1");
        PlaywrightBrowser.page.locator("#type").selectOption("Game");
        PlaywrightBrowser.page.locator("#startTime").fill("2003-03-25T12:45");
        PlaywrightBrowser.page.locator("#endTime").fill("2003-03-25T14:00");
        PlaywrightBrowser.page.locator("#description")
            .fill("Hey team at 12:45pm this Saturday");

        PlaywrightBrowser.page.locator("#nextPageButton").click();

        PlaywrightBrowser.page.locator("#address1").fill("123 Main Road");
        PlaywrightBrowser.page.locator("#postcode").fill("7805");
        PlaywrightBrowser.page.locator("#city").fill("Greymouth");
        PlaywrightBrowser.page.locator("#country").fill("New Zealand");

        PlaywrightBrowser.page.locator("#saveActivityButton").click();
    }

    @Then("I see an {string} error message saying {string}")
    public void see_an_error_message_saying(String error, String message) {

        // Check validation has failed, and we are still on the create activity page
        String currentUrl = PlaywrightBrowser.page.url();
        Assertions.assertTrue(currentUrl.contains("/editActivity"));

        // Check validation error has appeared
        PlaywrightBrowser.page.waitForSelector("#" + error,
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#" + error));

        // Check the message is correct
        Assertions.assertTrue(PlaywrightBrowser.page.locator("#" + error)
            .textContent().trim().replaceAll("\\s+",
                " ").contains(message));
    }

    @When("I enter a Edit Activity form with a missing activity type")
    public void enter_a_edit_activity_form_with_a_missing_activity_type() {
        PlaywrightBrowser.page.locator("#team").selectOption("-1");
        PlaywrightBrowser.page.locator("#type").selectOption("none");
        PlaywrightBrowser.page.locator("#startTime").fill("2003-03-25T12:45");
        PlaywrightBrowser.page.locator("#endTime").fill("2003-03-25T14:00");
        PlaywrightBrowser.page.locator("#description")
            .fill("Hey team at 12:45pm this Saturday");

        PlaywrightBrowser.page.locator("#nextPageButton").click();

        PlaywrightBrowser.page.locator("#address1").fill("123 Main Road");
        PlaywrightBrowser.page.locator("#postcode").fill("7805");
        PlaywrightBrowser.page.locator("#city").fill("Greymouth");
        PlaywrightBrowser.page.locator("#country").fill("New Zealand");

        PlaywrightBrowser.page.locator("#saveActivityButton").click();

    }

    @When("I enter a Edit Activity form with an invalid description")
    public void enter_a_edit_activity_form_with_an_invalid_description() {
        PlaywrightBrowser.page.locator("#team").selectOption("-1");
        PlaywrightBrowser.page.locator("#type").selectOption("none");
        PlaywrightBrowser.page.locator("#startTime").fill("2003-03-25T12:45");
        PlaywrightBrowser.page.locator("#endTime").fill("2003-03-25T14:00");
        PlaywrightBrowser.page.locator("#description").fill("");

        PlaywrightBrowser.page.locator("#nextPageButton").click();

        PlaywrightBrowser.page.locator("#address1").fill("123 Main Road");
        PlaywrightBrowser.page.locator("#postcode").fill("7805");
        PlaywrightBrowser.page.locator("#city").fill("Greymouth");
        PlaywrightBrowser.page.locator("#country").fill("New Zealand");

        PlaywrightBrowser.page.locator("#saveActivityButton").click();
    }

    @When("I enter a Edit Activity form with missing Start Time and End Times")
    public void enter_a_edit_activity_form_with_missing_start_time_and_end_time() {

        PlaywrightBrowser.page.locator("#team").selectOption("-1");
        PlaywrightBrowser.page.locator("#type").selectOption("none");
        PlaywrightBrowser.page.locator("#startTime").fill("");
        PlaywrightBrowser.page.locator("#endTime").fill("");
        PlaywrightBrowser.page.locator("#description").fill("");

        PlaywrightBrowser.page.locator("#nextPageButton").click();

        PlaywrightBrowser.page.locator("#address1").fill("123 Main Road");
        PlaywrightBrowser.page.locator("#postcode").fill("7805");
        PlaywrightBrowser.page.locator("#city").fill("Greymouth");
        PlaywrightBrowser.page.locator("#country").fill("New Zealand");

        PlaywrightBrowser.page.locator("#saveActivityButton").click();
    }

    @When("I enter a Edit Activity form with a End Time before the Start Time")
    public void enter_a_edit_activity_form_with_a_end_time_before_the_start_time() {
        PlaywrightBrowser.page.locator("#team").selectOption("-1");
        PlaywrightBrowser.page.locator("#type").selectOption("none");
        PlaywrightBrowser.page.locator("#startTime").fill("2003-03-25T12:45");
        PlaywrightBrowser.page.locator("#endTime").fill("2002-03-25T14:00");
        PlaywrightBrowser.page.locator("#description").fill("");

        PlaywrightBrowser.page.locator("#nextPageButton").click();

        PlaywrightBrowser.page.locator("#address1").fill("123 Main Road");
        PlaywrightBrowser.page.locator("#postcode").fill("7805");
        PlaywrightBrowser.page.locator("#city").fill("Greymouth");
        PlaywrightBrowser.page.locator("#country").fill("New Zealand");

        PlaywrightBrowser.page.locator("#saveActivityButton").click();
    }

    @When("I enter a Edit Activity form with a Start Time and "
        + "End Time before the Team Creation Time")
    public void enter_a_edit_activity_form_with_a_start_end_time_before_the_creation_time() {
        PlaywrightBrowser.page.locator("#team").selectOption("2");
        PlaywrightBrowser.page.locator("#type").selectOption("none");
        PlaywrightBrowser.page.locator("#startTime").fill("1900-03-25T12:45");
        PlaywrightBrowser.page.locator("#endTime").fill("1900-03-25T14:00");
        PlaywrightBrowser.page.locator("#description")
            .fill("Hey team at 12:45pm this Saturday");

        PlaywrightBrowser.page.locator("#nextPageButton").click();

        PlaywrightBrowser.page.locator("#address1").fill("123 Main Road");
        PlaywrightBrowser.page.locator("#postcode").fill("7805");
        PlaywrightBrowser.page.locator("#city").fill("Greymouth");
        PlaywrightBrowser.page.locator("#country").fill("New Zealand");

        PlaywrightBrowser.page.locator("#saveActivityButton").click();
    }

    @And("I navigate to the view team activities page")
    public void navigate_to_the_view_team_activities_page() {
        PlaywrightBrowser.page.locator("#activity-dropdown-btn").click();
        PlaywrightBrowser.page.locator("#viewActivityButton").click();

        // Check we are taken to the view my activities page
        String currentUrl = PlaywrightBrowser.page.url();
        Assertions.assertTrue(currentUrl.contains("/viewMyActivities"));
    }

    @And("I see the list of activities")
    public void see_the_list_of_activities() {

        PlaywrightBrowser.page.locator(".toastui-calendar-template-time").first();

        // Check there is an activity
        PlaywrightBrowser.page.waitForSelector(".toastui-calendar-template-time",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        PlaywrightBrowser.page.locator(".toastui-calendar-template-time").first().isVisible();
    }

    @When("I hit the edit activity button next to the activity entry in the list")
    public void hit_the_edit_activity_button_next_to_the_activity_entry_in_the_list() {
        PlaywrightBrowser.page.locator(".toastui-calendar-template-time").first().click();
        PlaywrightBrowser.page.locator(".toastui-calendar-template-time").first().click();
        PlaywrightBrowser.page.locator("#editActivityButton").click();
    }

    @Then("I see the edited activity form")
    public void see_the_edited_activity_form() {
        // Check we are taken to the editActivity page
        String currentUrl = PlaywrightBrowser.page.url();
        Assertions.assertTrue(currentUrl.contains("/editActivity"));

    }

    @And("I navigate to the view my teams activities page")
    public void navigate_to_the_view_my_teams_activities_page() {
        PlaywrightBrowser.page.locator("#team-dropdown-btn").click();
        PlaywrightBrowser.page.locator("#viewMyTeamsBtn").click();
        PlaywrightBrowser.page.locator("#index-5").click();

        // Check I am taken to the team activities page
        PlaywrightBrowser.page.waitForSelector("#today-btn",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#today-btn"));


    }


    @And("I am the manager or coach of the team")
    public void am_the_manager_or_coach_of_the_team() {

        // Check I am the manager or coach by checking if there is an edit button
        PlaywrightBrowser.page.waitForSelector("#edit-activity-button",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#edit-activity-button"));

    }

    @When("I hit the edit activity button next to the activity entry in the team activity list")
    public void hit_the_edit_activity_button_next_to_activity_entry_in_the_team_activity_list() {

        PlaywrightBrowser.page.locator(".toastui-calendar-template-time").first().click();
        PlaywrightBrowser.page.locator(".toastui-calendar-template-time").first().click();
        PlaywrightBrowser.page.locator("#editActivityButton").click();
    }


    @Given("I am logged in with a new user")
    public void am_logged_in_with_a_new_user() {
        // Navigate to login screen
        PlaywrightBrowser.page.navigate(PlaywrightBrowser.baseUrl + "/");
        PlaywrightBrowser.page.locator("#loginButton").click();

        // Login
        PlaywrightBrowser.page.locator("#username").fill("test4@email.com");
        PlaywrightBrowser.page.locator("#password").fill("*****");
        PlaywrightBrowser.page.locator("#loginSubmitButton").click();

        String currentUrl = PlaywrightBrowser.page.url();
        Assertions.assertTrue(currentUrl.contains("/profilePage"));
    }

    @And("I enter a valid Create Activity form with team New Zealand National Team")
    public void enter_a_valid_create_activity_form_with_team_new_zealand_national_team() {

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        String formattedNowDate = now.format(formatter);

        PlaywrightBrowser.page.selectOption("#team", "New Zealand National Team");
        PlaywrightBrowser.page.locator("#type").selectOption("Training");
        PlaywrightBrowser.page.locator("#startTime").fill(formattedNowDate);
        LocalDateTime futureDate = now.plusHours(2);
        String formattedFutureDate = futureDate.format(formatter);
        PlaywrightBrowser.page.locator("#endTime").fill(formattedFutureDate);
        PlaywrightBrowser.page.locator("#description")
            .fill("Hey guys big game this weekend");

        PlaywrightBrowser.page.locator("#nextPageButton").click();

        PlaywrightBrowser.page.locator("#address1").fill("123 Main Road");
        PlaywrightBrowser.page.locator("#postcode").fill("7805");
        PlaywrightBrowser.page.locator("#city").fill("Greymouth");
        PlaywrightBrowser.page.locator("#country").fill("New Zealand");

        PlaywrightBrowser.page.locator("#createActivityButton").last().click();
    }
}
