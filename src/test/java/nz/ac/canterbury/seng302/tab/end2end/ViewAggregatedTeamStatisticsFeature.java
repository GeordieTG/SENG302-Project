package nz.ac.canterbury.seng302.tab.end2end;

import static com.microsoft.playwright.options.WaitForSelectorState.VISIBLE;

import com.microsoft.playwright.Page;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

/**
 * View Aggregated Team Statistics Tests
 */
public class ViewAggregatedTeamStatisticsFeature {


    @When("I click on a recent activity")
    public void click_on_a_recent_activity() {
        PlaywrightBrowser.page.waitForSelector("#recentGame",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        PlaywrightBrowser.page.locator("#recentGame").first().click();
    }

    @Then("I can see the details of the activity")
    public void can_see_the_details_of_the_activity() {
        // Check we are taken to the Activity details page
        String currentUrl = PlaywrightBrowser.page.url();
        Assertions.assertTrue(currentUrl.contains("/viewActivity"));
    }
}
