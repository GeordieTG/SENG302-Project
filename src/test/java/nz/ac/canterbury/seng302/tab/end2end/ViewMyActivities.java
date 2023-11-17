package nz.ac.canterbury.seng302.tab.end2end;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

/**
 * Test for the view my activities feature using E2E testing.
 */
public class ViewMyActivities {

    @When("I click View Activities UI element")
    public void click_view_activities_ui_element() {

        PlaywrightBrowser.page.locator("#activity-dropdown-btn").click();
        PlaywrightBrowser.page.locator("#viewActivityButton").click();

    }

    @Then("I see all my activities")
    public void see_all_m_activities() {
        PlaywrightBrowser.page.locator("#today-btn").click();
        Assertions.assertTrue(PlaywrightBrowser.page.locator("#today-btn").isVisible());
    }

    @When("I have personal activities")
    public void have_personal_activities() {

        PlaywrightBrowser.page.navigate(PlaywrightBrowser.baseUrl + "/createActivity");

        PlaywrightBrowser.page.locator("#team").selectOption("-1");
        PlaywrightBrowser.page.locator("#type").selectOption("Training");
        PlaywrightBrowser.page.locator("#startTime").fill("2099-03-25T12:45");
        PlaywrightBrowser.page.locator("#endTime").fill("2099-03-25T14:00");
        PlaywrightBrowser.page.locator("#description").fill("Hey guys big game this weekend");

        PlaywrightBrowser.page.locator("#nextPageButton").click();

        PlaywrightBrowser.page.locator("#address1").fill("123 Main Road");
        PlaywrightBrowser.page.locator("#postcode").fill("7805");
        PlaywrightBrowser.page.locator("#city").fill("Greymouth");
        PlaywrightBrowser.page.locator("#country").fill("New Zealand");

        PlaywrightBrowser.page.locator("#createActivityButton").last().click();

    }
}
