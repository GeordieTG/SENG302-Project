package nz.ac.canterbury.seng302.tab.end2end;

import static com.microsoft.playwright.options.WaitForSelectorState.VISIBLE;

import com.microsoft.playwright.Page;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

/**
 * Test for Edit User Profile Feature using E2E tests
 */
public class EditUserProfileFeature {


    @When("I hit the edit button")
    public void hit_the_edit_button() {

        PlaywrightBrowser.page.locator("#editProfileBtn").click();


    }

    @Then("I see the edit profile form with all my details prepopulated")
    public void see_the_edit_profile_form_with_all_my_details_prepopulated() {

        Assertions.assertEquals("League", PlaywrightBrowser.page.locator("#favouriteSport")
            .inputValue());

        PlaywrightBrowser.page.locator("#detailsTab").click();

        Assertions.assertEquals("Natalie", PlaywrightBrowser.page.locator("#firstName")
            .inputValue());
        Assertions.assertEquals("Kim", PlaywrightBrowser.page.locator("#lastName")
            .inputValue());
        Assertions.assertEquals("1999-12-26", PlaywrightBrowser.page.locator("#DOB")
            .inputValue());
        Assertions.assertEquals("test1@email.com", PlaywrightBrowser.page.locator("#email")
            .inputValue());

        PlaywrightBrowser.page.locator("#locationTab").click();

        Assertions.assertEquals("Marsellie", PlaywrightBrowser.page.locator("#city")
            .inputValue());
        Assertions.assertEquals("England", PlaywrightBrowser.page.locator("#country")
            .inputValue());

    }

    @And("I enter a valid edit profile form")
    public void enter_a_valid_edit_profile_form() {

        PlaywrightBrowser.page.locator("#detailsTab").click();

        PlaywrightBrowser.page.locator("#firstName").fill("Changed");
        PlaywrightBrowser.page.locator("#lastName").fill("Name");
        PlaywrightBrowser.page.locator("#DOB").fill("2003-03-25");
        PlaywrightBrowser.page.locator("#email").fill("and@email.com");

    }

    @When("I hit the save button")
    public void hit_the_save_button() {

        PlaywrightBrowser.page.locator("#submitDetailsBtn").click();

    }

    @Then("My new details are saved")
    public void my_new_details_are_saved() {


        Assertions.assertEquals("Changed Name",
            PlaywrightBrowser.page.locator("#userName").textContent());
        Assertions.assertEquals("2003-03-25",
            PlaywrightBrowser.page.locator("#userDOB").textContent());
        Assertions.assertEquals("and@email.com",
            PlaywrightBrowser.page.locator("#userEmail").textContent());

    }

    @Given("I have logged in as user {int}")
    public void have_logged_in_as_user(int user) {

        PlaywrightBrowser.page.navigate(PlaywrightBrowser.baseUrl + "/");
        PlaywrightBrowser.page.locator("#loginButton").click();

        // Login
        PlaywrightBrowser.page.locator("#username")
            .fill("test" + user + "@email.com");
        PlaywrightBrowser.page.locator("#password").fill("*****");
        PlaywrightBrowser.page.locator("#loginSubmitButton").click();

        String currentUrl = PlaywrightBrowser.page.url();
        Assertions.assertTrue(currentUrl.contains("/profilePage"));

    }

    @And("I enter all invalid values")
    public void enter_all_invalid_values() {

        PlaywrightBrowser.page.locator("#detailsTab").click();

        PlaywrightBrowser.page.locator("#firstName").fill("Changed");
        PlaywrightBrowser.page.locator("#lastName").fill("Name");
        PlaywrightBrowser.page.locator("#DOB").fill("2003-03-25");
        PlaywrightBrowser.page.locator("#email").fill("and@email.com");

    }

    @And("I enter invalid values for my names")
    public void enter_invalid_values_for_my_names() {

        PlaywrightBrowser.page.locator("#detailsTab").click();

        PlaywrightBrowser.page.locator("#firstName").fill("");
        PlaywrightBrowser.page.locator("#lastName").fill("!@");

    }

    @Then("An error message tells me there are invalid values")
    public void an_error_message_tells_me_there_are_invalid_values() {

        PlaywrightBrowser.page.waitForSelector("#detailsTab").click();
        PlaywrightBrowser.page.waitForSelector("#firstNameError",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.locator("#firstNameError")
            .isVisible());
        Assertions.assertTrue(PlaywrightBrowser.page.locator("#lastNameError")
            .isVisible());

    }

    @And("I enter a malformed or empty email address")
    public void enter_a_malformed_or_empty_email_address() {

        PlaywrightBrowser.page.locator("#detailsTab").click();


        PlaywrightBrowser.page.locator("#firstName").fill("Changed");
        PlaywrightBrowser.page.locator("#lastName").fill("Name");
        PlaywrightBrowser.page.locator("#DOB").fill("2003-03-25");
        PlaywrightBrowser.page.locator("#email").fill("invalidMail@test");

    }

    @Then("An error message tells me the email address is invalid")
    public void an_error_message_tells_me_the_email_address_is_invalid() {

        Assertions.assertTrue(PlaywrightBrowser.page.locator("#emailError").isVisible());

    }

    @And("I enter a date of birth for someone younger than 13 years old")
    public void enter_a_date_of_birth_for_someone_younger_than_years_old() {

        PlaywrightBrowser.page.locator("#detailsTab").click();

        PlaywrightBrowser.page.locator("#firstName").fill("Changed");
        PlaywrightBrowser.page.locator("#lastName").fill("Name");
        PlaywrightBrowser.page.locator("#DOB").fill("2020-03-25");
        PlaywrightBrowser.page.locator("#email").fill("test1@email.com");
    }

    @Then("An error message tells me the date of birth is invalid")
    public void an_error_message_tells_me_the_date_of_birth_is_invalid() {

        PlaywrightBrowser.page.locator("#detailsTab").click();
        PlaywrightBrowser.page.waitForSelector("#dobError",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.locator("#dobError").isVisible());

    }

    @And("I enter a new email address that already exists in the system")
    public void enter_a_new_email_address_that_already_exists_in_the_system() {

        PlaywrightBrowser.page.locator("#detailsTab").click();

        PlaywrightBrowser.page.locator("#firstName").fill("Changed");
        PlaywrightBrowser.page.locator("#lastName").fill("Name");
        PlaywrightBrowser.page.locator("#DOB").fill("2010-03-25");
        PlaywrightBrowser.page.locator("#email").fill("test0@email.com");

    }

    @Then("An error message tells me the email is already registered")
    public void an_error_message_tells_me_the_email_is_already_registered() {

        PlaywrightBrowser.page.locator("#detailsTab").click();
        PlaywrightBrowser.page.waitForSelector("#emailError",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.locator("#emailError").isVisible());
        Assertions.assertEquals("This email is already registered to an account.",
            PlaywrightBrowser.page.locator("#emailError").textContent().strip());

    }

    @When("I hit the cancel button")
    public void hit_the_cancel_button() {

        PlaywrightBrowser.page.locator("#cancelEditBtn").click();

    }

    @Then("I come back to my profile page with no details changed")
    public void come_back_to_my_profile_page_with_no_details_changed() {


        Assertions.assertEquals("Natalie Kim",
            PlaywrightBrowser.page.locator("#userName").textContent());
        Assertions.assertEquals("1999-12-26",
            PlaywrightBrowser.page.locator("#userDOB").textContent());
        Assertions.assertEquals("test1@email.com",
            PlaywrightBrowser.page.locator("#userEmail").textContent());
    }
}

