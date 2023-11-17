package nz.ac.canterbury.seng302.tab.end2end;

import static com.microsoft.playwright.options.WaitForSelectorState.VISIBLE;

import com.microsoft.playwright.Page;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

/**
 * Test for Update Password using E2E testing
 */
public class UpdatePasswordFeature {


    @And("I navigate to the edit user profile page")
    public void navigate_to_the_edit_user_profile_page() {
        PlaywrightBrowser.page.navigate(PlaywrightBrowser.baseUrl + "/profilePage");

        // Check we are taken to the profile page
        String currentUrl = PlaywrightBrowser.page.url();
        Assertions.assertTrue(currentUrl.contains("/profilePage"));

        PlaywrightBrowser.page.locator("#editProfileBtn").click();

        // Check we are taken to the editProfile page
        String currentUrl2 = PlaywrightBrowser.page.url();
        Assertions.assertTrue(currentUrl2.contains("/editProfile"));


        PlaywrightBrowser.page.locator("#detailsTab").click();

        // Check change password button is present
        PlaywrightBrowser.page.waitForSelector("#changePasswordBtn",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#changePasswordBtn"));

    }

    @When("I click on the change password button")
    public void click_on_the_change_password_button() {
        PlaywrightBrowser.page.locator("#changePasswordBtn").click();
    }

    @Then("I see a change password form")
    public void see_a_change_password_form() {

        // Check we are taken to the change password page
        String currentUrl = PlaywrightBrowser.page.url();
        Assertions.assertTrue(currentUrl.contains("/changePassword"));

        // Check old password field is present
        PlaywrightBrowser.page.waitForSelector("#oldPassword",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#oldPassword"));

        // Check new password field is present
        PlaywrightBrowser.page.waitForSelector("#password",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#password"));

        // Check retype password field is present
        PlaywrightBrowser.page.waitForSelector("#confirmPassword",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#confirmPassword"));


    }

    @When("I enter the old password and it is different to the password in the database")
    public void enter_the_old_password_and_it_is_different_to_the_password_in_the_database() {

        PlaywrightBrowser.page.locator("#oldPassword").fill("Qwerty10!");
        PlaywrightBrowser.page.locator("#submitBtn").click();
    }

    @Then("I see an {string} error saying {string}")
    public void see_an_error_saying(String error, String message) {

        // Check validation has failed, and we are still on the change password page
        String currentUrl = PlaywrightBrowser.page.url();
        Assertions.assertTrue(currentUrl.contains("/changePassword"));

        // Check validation error has appeared
        PlaywrightBrowser.page.waitForSelector("#" + error,
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#" + error));

        // Check the message is correct
        Assertions.assertTrue(
            PlaywrightBrowser.page.locator("#" + error).textContent().contains(message));
    }

    @When("I enter different new and retype passwords")
    public void enter_different_new_and_retype_passwords() {

        PlaywrightBrowser.page.locator("#oldPassword").fill("*****");
        PlaywrightBrowser.page.locator("#password").fill("Qwerty10`");
        PlaywrightBrowser.page.locator("#confirmPassword").fill("Qwerty10!");
        PlaywrightBrowser.page.locator("#submitBtn").click();

    }

    @When("I enter a weak password")
    public void enter_a_weak_password() {
        PlaywrightBrowser.page.locator("#oldPassword").fill("*****");
        PlaywrightBrowser.page.locator("#password").fill("password");
        PlaywrightBrowser.page.locator("#confirmPassword").fill("password");
        PlaywrightBrowser.page.locator("#submitBtn").click();
    }
}
