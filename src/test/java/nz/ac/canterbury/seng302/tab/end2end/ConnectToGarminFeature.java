package nz.ac.canterbury.seng302.tab.end2end;

import static com.microsoft.playwright.options.WaitForSelectorState.VISIBLE;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

/**
 * End to End steps for the connect to garmin feature
 */

public class ConnectToGarminFeature {

    Page newPage;

    @Given("I can see a button for connecting to the Garmin watch")
    public void i_can_see_a_button_for_connecting_to_the_garmin_watch() {
        PlaywrightBrowser.page.waitForSelector("#garminBtn",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#garminBtn"));
    }

    @When("I click the button for connecting to the Garmin watch")
    public void i_click_on_the_button_for_connecting_to_the_garmin_watch() {
        newPage = PlaywrightBrowser.browserContext.waitForPage(() -> {
            PlaywrightBrowser.page.waitForSelector("#garminBtn",
                new Page.WaitForSelectorOptions().setState(VISIBLE)).click();
        });
    }

    @Then("I am redirected to the Garmin Portal to log in")
    public void i_am_redirected_to_the_garmin_portal_to_log_in() {
        newPage.waitForLoadState(LoadState.LOAD);
        Assertions.assertTrue(newPage.url().contains("connect.garmin.com"));
    }

}
