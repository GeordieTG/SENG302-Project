package nz.ac.canterbury.seng302.tab.end2end;

import static com.microsoft.playwright.options.WaitForSelectorState.VISIBLE;

import com.microsoft.playwright.Page;
import io.cucumber.java.en.Given;
import org.junit.jupiter.api.Assertions;

/**
 * Collection of common and recurring steps for the E2E tests
 */
public class CommonStepDefs {

    @Given("I have logged in")
    public void have_logged_in() {
        //Navigate to the login screen
        PlaywrightBrowser.page.navigate(PlaywrightBrowser.baseUrl + "/");
        PlaywrightBrowser.page.locator("#loginButton").click();

        // Login
        PlaywrightBrowser.page.locator("#username").fill("test1@email.com");
        PlaywrightBrowser.page.locator("#password").fill("*****");
        PlaywrightBrowser.page.locator("#loginSubmitButton").click();

        String currentUrl = PlaywrightBrowser.page.url();
        Assertions.assertTrue(currentUrl.contains("/profilePage"));
    }

    @Given("I am being shown a team search form")
    public void i_am_being_shown_a_team_search_form() {
        PlaywrightBrowser.page.navigate(PlaywrightBrowser.baseUrl + "/viewTeams");
        // Check form is present
        PlaywrightBrowser.page.waitForSelector("#searchQuery",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#searchQuery"));
    }

    @Given("I am on the {string} page")
    public void am_on_the_page(String page) {
        PlaywrightBrowser.page.navigate(PlaywrightBrowser.baseUrl + "/" + page);
    }

}
