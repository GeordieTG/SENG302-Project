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

public class LoginFeature {

    @Given("I connect to the systems main URL")
    public void connect_to_the_systems_main_url() {
        PlaywrightBrowser.page.navigate(PlaywrightBrowser.baseUrl + "/");
    }


    @When("I hit the login button")
    public void hit_the_login_button() {
        PlaywrightBrowser.page.locator("#loginButton").click();
    }


    @Then("I see a login form")
    public void see_a_login_form() {

        // Check we are taken to the login page
        String currentUrl = PlaywrightBrowser.page.url();
        Assertions.assertTrue(currentUrl.contains("/login"));

        // Check from is present
        PlaywrightBrowser.page.waitForSelector("#loginForm",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#loginForm"));
    }
}
