package nz.ac.canterbury.seng302.tab.end2end;

import static com.microsoft.playwright.options.WaitForSelectorState.VISIBLE;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.Assertions;

/**
 * Test for Create Activity using E2E testing
 */
public class CreateActivityFeature {

    private String formattedNowDateInput;

    private String formattedFutureDateInput;

    private String formattedNowDateExpected;

    private String formattedFutureDateexpected;

    @Given("I am logged in")
    public void am_logged_in() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime futureDate = now.plusHours(2);
        DateTimeFormatter formatterInput = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        DateTimeFormatter formatterOutput = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        formattedNowDateInput = now.format(formatterInput);
        formattedFutureDateInput = futureDate.format(formatterInput);
        formattedNowDateExpected = now.format(formatterOutput) + " - ";
        formattedFutureDateexpected = futureDate.format(formatterOutput);

        // Navigate to login screen
        PlaywrightBrowser.page.navigate(PlaywrightBrowser.baseUrl + "/");
        PlaywrightBrowser.page.locator("#loginButton").click();

        // Login
        PlaywrightBrowser.page.locator("#username").fill("test1@email.com");
        PlaywrightBrowser.page.locator("#password").fill("*****");
        PlaywrightBrowser.page.locator("#loginSubmitButton").click();

        String currentUrl = PlaywrightBrowser.page.url();
        Assertions.assertTrue(currentUrl.contains("/profilePage"));
    }

    @When("I hit the Activity button")
    public void hit_the_activity_button() {
        PlaywrightBrowser.page.locator("#activity-dropdown-btn").click();
        PlaywrightBrowser.page.locator("#createActivityButton").click();
    }


    @Then("I am on the Create Activity form")
    public void am_on_the_create_activity_form() {

        // Check we are taken to the createActivity page
        String currentUrl = PlaywrightBrowser.page.url();
        Assertions.assertTrue(currentUrl.contains("/createActivity"));

        // Check from is present
        PlaywrightBrowser.page.waitForSelector("#activityForm",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#activityForm"));
    }

    @When("I enter a Create Activity form with activity type that requires a team with no team")
    public void enter_a_create_activity_form_with_activity_type_that_requires_team_with_no_team() {

        PlaywrightBrowser.page.locator("#team").selectOption("-1");
        PlaywrightBrowser.page.locator("#type").selectOption("Game");
        PlaywrightBrowser.page.locator("#startTime").fill("2003-03-25T12:45");
        PlaywrightBrowser.page.locator("#endTime").fill("2003-03-25T14:00");
        PlaywrightBrowser.page.locator("#description").fill("Hey team at 12:45pm this Saturday");

        PlaywrightBrowser.page.locator("#nextPageButton").click();


        PlaywrightBrowser.page.locator("#address1").fill("Greymouth");
        PlaywrightBrowser.page.locator("#postcode").fill("7805");
        PlaywrightBrowser.page.locator("#city").fill("Greymouth");
        PlaywrightBrowser.page.locator("#country").fill("New Zealand");

        PlaywrightBrowser.page.locator("#createActivityButton").last().click();
    }

    @When("I enter a Create Activity form with a missing activity type")
    public void enter_a_create_activity_form_with_a_missing_activity_type() {

        PlaywrightBrowser.page.locator("#team").selectOption("-1");
        PlaywrightBrowser.page.locator("#type").selectOption("none");
        PlaywrightBrowser.page.locator("#startTime").fill("2003-03-25T12:45");
        PlaywrightBrowser.page.locator("#endTime").fill("2003-03-25T14:00");
        PlaywrightBrowser.page.locator("#description").fill("Hey team at 12:45pm this Saturday");

        PlaywrightBrowser.page.locator("#nextPageButton").click();


        PlaywrightBrowser.page.locator("#address1").fill("Greymouth");
        PlaywrightBrowser.page.locator("#postcode").fill("7805");
        PlaywrightBrowser.page.locator("#city").fill("Greymouth");
        PlaywrightBrowser.page.locator("#country").fill("New Zealand");

        PlaywrightBrowser.page.locator("#createActivityButton").last().click();
    }


    @When("I enter a Create Activity form with a Start Time and "
        + "End Time before the Team Creation Time")
    public void enter_a_create_activity_form_with_a_start_end_time_before_the_team_creation_time() {

        PlaywrightBrowser.page.locator("#team").selectOption("2");
        PlaywrightBrowser.page.locator("#type").selectOption("none");
        PlaywrightBrowser.page.locator("#startTime").fill("1900-03-25T12:45");
        PlaywrightBrowser.page.locator("#endTime").fill("1900-03-25T14:00");
        PlaywrightBrowser.page.locator("#description").fill("Hey team at 12:45pm this Saturday");

        PlaywrightBrowser.page.locator("#nextPageButton").click();

        PlaywrightBrowser.page.locator("#address1").fill("Greymouth");
        PlaywrightBrowser.page.locator("#postcode").fill("7805");
        PlaywrightBrowser.page.locator("#city").fill("Greymouth");
        PlaywrightBrowser.page.locator("#country").fill("New Zealand");

        PlaywrightBrowser.page.locator("#createActivityButton").last().click();
    }

    @When("I enter a Create Activity form with missing Start Time and End Times")
    public void enter_a_create_activity_form_with_missing_start_time_and_end_times() {

        PlaywrightBrowser.page.locator("#team").selectOption("-1");
        PlaywrightBrowser.page.locator("#type").selectOption("none");
        PlaywrightBrowser.page.locator("#description").fill("");

        PlaywrightBrowser.page.locator("#nextPageButton").click();


        PlaywrightBrowser.page.locator("#address1").fill("Greymouth");
        PlaywrightBrowser.page.locator("#postcode").fill("7805");
        PlaywrightBrowser.page.locator("#city").fill("Greymouth");
        PlaywrightBrowser.page.locator("#country").fill("New Zealand");

        PlaywrightBrowser.page.locator("#createActivityButton").last().click();
    }

    @When("I enter a Create Activity form with a End Time before the Start Time")
    public void enter_a_create_activity_form_with_a_end_time_before_the_start_time() {

        PlaywrightBrowser.page.locator("#team").selectOption("-1");
        PlaywrightBrowser.page.locator("#type").selectOption("none");
        PlaywrightBrowser.page.locator("#startTime").fill("2003-03-25T12:45");
        PlaywrightBrowser.page.locator("#endTime").fill("2002-03-25T14:00");
        PlaywrightBrowser.page.locator("#description").fill("");

        PlaywrightBrowser.page.locator("#nextPageButton").click();

        PlaywrightBrowser.page.locator("#address1").fill("Greymouth");
        PlaywrightBrowser.page.locator("#postcode").fill("7805");
        PlaywrightBrowser.page.locator("#city").fill("Greymouth");
        PlaywrightBrowser.page.locator("#country").fill("New Zealand");

        PlaywrightBrowser.page.locator("#createActivityButton").last().click();
    }

    @When("I enter a valid Create Activity form without a team")
    public void enter_a_valid_create_activity_form_without_a_team() {

        PlaywrightBrowser.page.locator("#team").selectOption("-1");
        PlaywrightBrowser.page.locator("#type").selectOption("Training");
        PlaywrightBrowser.page.locator("#startTime").fill(formattedNowDateInput);
        PlaywrightBrowser.page.locator("#endTime").fill(formattedFutureDateInput);
        PlaywrightBrowser.page.locator("#description").fill("Hey guys big game this weekend");

        PlaywrightBrowser.page.locator("#nextPageButton").click();

        PlaywrightBrowser.page.locator("#address1").fill("123 Main Road");
        PlaywrightBrowser.page.locator("#postcode").fill("7805");
        PlaywrightBrowser.page.locator("#city").fill("Greymouth");
        PlaywrightBrowser.page.locator("#country").fill("New Zealand");

        PlaywrightBrowser.page.locator("#createActivityButton").last().click();
    }

    @When("I enter a valid Create Activity form with a team")
    public void enter_a_valid_create_activity_form_with_a_team() {

        PlaywrightBrowser.page.locator("#team").selectOption("2");
        PlaywrightBrowser.page.locator("#type").selectOption("Game");
        PlaywrightBrowser.page.locator("#startTime").fill(formattedNowDateInput);
        PlaywrightBrowser.page.locator("#endTime").fill(formattedFutureDateInput);
        PlaywrightBrowser.page.locator("#description").fill("Hey guys big game this weekend");

        PlaywrightBrowser.page.locator("#nextPageButton").click();

        PlaywrightBrowser.page.locator("#address1").fill("123 Main Road");
        PlaywrightBrowser.page.locator("#postcode").fill("7805");
        PlaywrightBrowser.page.locator("#city").fill("Greymouth");
        PlaywrightBrowser.page.locator("#country").fill("New Zealand");

        PlaywrightBrowser.page.locator("#createActivityButton").last().click();
    }

    @When("I enter a Create Activity form with an invalid description")
    public void enter_a_create_activity_form_with_an_invalid_description() {

        PlaywrightBrowser.page.locator("#team").selectOption("-1");
        PlaywrightBrowser.page.locator("#type").selectOption("none");
        PlaywrightBrowser.page.locator("#startTime").fill("2003-03-25T12:45");
        PlaywrightBrowser.page.locator("#endTime").fill("2003-03-25T14:00");
        PlaywrightBrowser.page.locator("#description").fill("");

        PlaywrightBrowser.page.locator("#nextPageButton").click();

        PlaywrightBrowser.page.locator("#address1").fill("Greymouth");
        PlaywrightBrowser.page.locator("#postcode").fill("7805");
        PlaywrightBrowser.page.locator("#city").fill("Greymouth");
        PlaywrightBrowser.page.locator("#country").fill("New Zealand");

        PlaywrightBrowser.page.locator("#createActivityButton").last().click();
    }

    @When("I navigate to the Create Activity form")
    public void navigate_to_the_create_activity_form() {

        PlaywrightBrowser.page.navigate(PlaywrightBrowser.baseUrl + "/createActivity");

        // Check we are taken to the createActivity page
        String currentUrl = PlaywrightBrowser.page.url();
        Assertions.assertTrue(currentUrl.contains("/createActivity"));

        // Check from is present
        PlaywrightBrowser.page.waitForSelector("#activityForm",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#activityForm"));
    }

    @Then("I see a {string} error message saying {string}")
    public void see_a_error_message_saying(String error, String message) {

        // Check validation has failed and we are still on the create activity page
        String currentUrl = PlaywrightBrowser.page.url();
        Assertions.assertTrue(currentUrl.contains("/createActivity"));

        // Check validation error has appeared
        PlaywrightBrowser.page.waitForSelector("#" + error,
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#" + error));

        // Check the message is correct
        Assertions.assertTrue(
            PlaywrightBrowser.page.locator("#" + error).textContent().trim().replaceAll("\\s+",
                " ").contains(message));
    }

    @Then("I am able to select from a list of my teams that I coach or manage")
    public void am_able_to_select_from_a_list_of_my_teams_that_coach_or_manage() {

        List<String> content = PlaywrightBrowser.page.locator("#team").allTextContents();
        Assertions.assertFalse(content.get(0).contains("Jack"));
        Assertions.assertTrue(content.get(0).contains("Bishops"));
        Assertions.assertTrue(content.get(0).contains("Bulldogs"));
    }

    @Then("I am able to select from a list of activity types")
    public void am_able_to_select_from_a_list_of_activity_types() {

        List<String> game = PlaywrightBrowser.page.locator("#type").selectOption("Game");
        List<String> friendly = PlaywrightBrowser.page.locator("#type").selectOption("Friendly");
        List<String> competition =
            PlaywrightBrowser.page.locator("#type").selectOption("Competition");
        List<String> training = PlaywrightBrowser.page.locator("#type").selectOption("Training");
        List<String> other = PlaywrightBrowser.page.locator("#type").selectOption("Other");

        Assertions.assertNotNull(game);
        Assertions.assertNotNull(friendly);
        Assertions.assertNotNull(competition);
        Assertions.assertNotNull(training);
        Assertions.assertNotNull(other);
    }

    @Then("I see the activity details without the team")
    public void see_the_activity_details_without_the_team() {

        // Ensure we are now on the activity details page
        Locator details = PlaywrightBrowser.page.locator("#activityDetailsCard");
        Assertions.assertNotNull(details);

        // Ensure details are showing correctly
        Locator type = PlaywrightBrowser.page.locator("#activityType");
        Locator startTime = PlaywrightBrowser.page.locator("#startTime");
        Locator endTime = PlaywrightBrowser.page.locator("#endTime");
        Locator description = PlaywrightBrowser.page.locator("#description");

        Assertions.assertEquals("Training", type.textContent());
        Assertions.assertEquals(startTime.textContent(), formattedNowDateExpected);
        Assertions.assertEquals(endTime.textContent(), formattedFutureDateexpected);
        Assertions.assertEquals("Hey guys big game this weekend", description.textContent());
    }


    @Then("I see the activity details with the team")
    public void see_the_activity_details_with_the_team() {

        // Ensure we are now on the activity details page
        Locator details = PlaywrightBrowser.page.locator("#activityDetailsCard");
        Assertions.assertNotNull(details);

        // Ensure details are showing correctly
        Locator team = PlaywrightBrowser.page.locator("#team");
        Locator type = PlaywrightBrowser.page.locator("#activityType");
        Locator startTime = PlaywrightBrowser.page.locator("#startTime");
        Locator endTime = PlaywrightBrowser.page.locator("#endTime");
        Locator description = PlaywrightBrowser.page.locator("#description");

        Assertions.assertEquals("Scotland National Team", team.textContent());
        Assertions.assertEquals("Game", type.textContent());
        Assertions.assertEquals(formattedNowDateExpected, startTime.textContent());
        Assertions.assertEquals(formattedFutureDateexpected, endTime.textContent());
        Assertions.assertEquals("Hey guys big game this weekend", description.textContent());
    }
}
