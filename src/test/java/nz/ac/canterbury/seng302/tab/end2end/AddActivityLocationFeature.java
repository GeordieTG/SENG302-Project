package nz.ac.canterbury.seng302.tab.end2end;

import static com.microsoft.playwright.options.WaitForSelectorState.VISIBLE;

import com.microsoft.playwright.Page;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

/**
 * Test class for the add activity feature
 */
public class AddActivityLocationFeature {

    @When("I hit the next button")
    public void hit_the_next_button() {
        PlaywrightBrowser.page.locator("#nextPageButton").click();
    }

    @Then("I can see fields to enter the location details")
    public void can_see_fields_to_enter_the_location_details() {

        PlaywrightBrowser.page.waitForSelector("#address1",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#address1"));

        PlaywrightBrowser.page.waitForSelector("#address2",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#address2"));

        PlaywrightBrowser.page.waitForSelector("#suburb",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#suburb"));

        PlaywrightBrowser.page.waitForSelector("#city",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#city"));

        PlaywrightBrowser.page.waitForSelector("#postcode",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#postcode"));

        PlaywrightBrowser.page.waitForSelector("#country",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#country"));
    }

    @And("I enter a {string} Activity form with no first address line, "
        + "no city, no postcode, and no country")
    public void enter_a_activity_form_with_no_first_address_line_no_city_no_postcode_and_no_country(
        String type) {
        PlaywrightBrowser.page.locator("#team").selectOption("-1");
        PlaywrightBrowser.page.locator("#type").selectOption("Game");
        PlaywrightBrowser.page.locator("#startTime").fill("2003-03-25T12:45");
        PlaywrightBrowser.page.locator("#endTime").fill("2003-03-25T14:00");
        PlaywrightBrowser.page.locator("#description")
            .fill("Hey team at 12:45pm this Saturday");

        PlaywrightBrowser.page.locator("#nextPageButton").click();


        PlaywrightBrowser.page.locator("#address1").fill("");
        PlaywrightBrowser.page.locator("#postcode").fill("");
        PlaywrightBrowser.page.locator("#city").fill("");
        PlaywrightBrowser.page.locator("#country").fill("");

        switch (type) {
            case "create" -> PlaywrightBrowser.page
                .locator("#createActivityButton").last().click();
            case "edit" -> PlaywrightBrowser.page
                .locator("#saveActivityButton").click();
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    @Then("I can see an error message saying that I must enter each of these fields")
    public void can_see_an_error_message_saying_that_must_enter_each_of_these_fields() {

        Assertions.assertEquals("Please enter an address",
            PlaywrightBrowser.page.locator("#validAddressError").textContent());
        Assertions.assertEquals("Please enter a country",
            PlaywrightBrowser.page.locator("#validCountryError").textContent());
        Assertions.assertEquals("Please enter a postcode",
            PlaywrightBrowser.page.locator("#validPostcodeError").textContent());
        Assertions.assertEquals("Please enter a city",
            PlaywrightBrowser.page.locator("#validCityError").textContent());
    }

    @When("I enter an Activity form with no {string}")
    public void enter_an_activity_form_with_no(String field) {

        PlaywrightBrowser.page.locator("#team").selectOption("-1");
        PlaywrightBrowser.page.locator("#type").selectOption("Competition");
        PlaywrightBrowser.page.locator("#startTime").fill("2024-05-02T12:45");
        PlaywrightBrowser.page.locator("#endTime").fill("2024-05-05T14:00");
        PlaywrightBrowser.page.locator("#description")
            .fill("Change of plans, it's a competition");

        PlaywrightBrowser.page.locator("#nextPageButton").click();

        switch (field) {
            case "postcode" -> {
                PlaywrightBrowser.page.locator("#address1")
                    .fill("Place over the hill");
                PlaywrightBrowser.page.locator("#postcode").fill("");
                PlaywrightBrowser.page.locator("#city").fill("Kyoto");
                PlaywrightBrowser.page.locator("#country").fill("Japan");
                PlaywrightBrowser.page.locator("#saveActivityButton").click();
                PlaywrightBrowser.page.locator("#nextPageButton").click();
            }
            case "country" -> {
                PlaywrightBrowser.page.locator("#address1")
                    .fill("Place over the hill");
                PlaywrightBrowser.page.locator("#postcode").fill("8019");
                PlaywrightBrowser.page.locator("#city").fill("Kyoto");
                PlaywrightBrowser.page.locator("#country").fill("");
                PlaywrightBrowser.page.locator("#saveActivityButton").click();
                PlaywrightBrowser.page.locator("#nextPageButton").click();
            }
            case "city" -> {
                PlaywrightBrowser.page.locator("#address1")
                    .fill("Place over the hill");
                PlaywrightBrowser.page.locator("#postcode").fill("8019");
                PlaywrightBrowser.page.locator("#city").fill("");
                PlaywrightBrowser.page.locator("#country").fill("Japan");
                PlaywrightBrowser.page.locator("#saveActivityButton").click();
                PlaywrightBrowser.page.locator("#nextPageButton").click();
            }
            case "address" -> {
                PlaywrightBrowser.page.locator("#address1").fill("");
                PlaywrightBrowser.page.locator("#postcode").fill("8019");
                PlaywrightBrowser.page.locator("#city").fill("Kyoto");
                PlaywrightBrowser.page.locator("#country").fill("Japan");
                PlaywrightBrowser.page.locator("#saveActivityButton").click();
                PlaywrightBrowser.page.locator("#nextPageButton").click();
            }
            default -> throw new IllegalStateException("Unexpected value: " + field);
        }
    }

    @Then("I start to enter an address {string}")
    public void start_to_enter_an_address(String address) {

        PlaywrightBrowser.page.locator("#address1").fill(address);
        int addressListLength = (int) PlaywrightBrowser.page
            .evaluate("document.querySelector('#address_list').options.length");

        Assertions.assertTrue(addressListLength > 0);
    }
}
