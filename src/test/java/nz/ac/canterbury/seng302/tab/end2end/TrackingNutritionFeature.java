package nz.ac.canterbury.seng302.tab.end2end;

import static com.microsoft.playwright.options.WaitForSelectorState.VISIBLE;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

/**
 * Step definitions for the tracking nutrition feature
 */
public class TrackingNutritionFeature {
    @Then("I see the add food button")
    public void i_see_the_add_food_button() {

        PlaywrightBrowser.page.waitForSelector("#showAddFoodModalBtn",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#showAddFoodModalBtn"));
    }

    @Given("I am on my nutrition page")
    public void i_am_on_my_nutrition_page() {

        PlaywrightBrowser.page.navigate(PlaywrightBrowser.baseUrl + "/nutrition?userId=2");
    }

    @When("I click the add food button")
    public void i_click_the_add_food_button() {
        PlaywrightBrowser.page.click("#showAddFoodModalBtn");
    }

    @Then("I see the add food modal")
    public void i_see_the_add_food_modal() {
        PlaywrightBrowser.page.waitForSelector("#addFoodModal",
            new Page.WaitForSelectorOptions().setState(VISIBLE));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#addFoodModal"));
    }

    @Then("I see my daily nutritional overview")
    public void i_see_my_daily_nutritional_overview() {
        Assertions.assertTrue(PlaywrightBrowser.page.getByRole(AriaRole.HEADING,
            new Page.GetByRoleOptions().setName("32 g")).isVisible());
        Assertions.assertTrue(PlaywrightBrowser.page.getByRole(AriaRole.HEADING,
            new Page.GetByRoleOptions().setName("43 g")).isVisible());
        Assertions.assertTrue(PlaywrightBrowser.page.getByRole(AriaRole.HEADING,
            new Page.GetByRoleOptions().setName("25 g")).isVisible());

    }

    @And("I enter a search query")
    public void i_enter_a_search_query() {
        PlaywrightBrowser.page.getByPlaceholder("Search Food").fill("big mac");
    }

    @And("I select a food from the search results")
    public void i_select_a_food_from_the_search_results() {
        PlaywrightBrowser.page.waitForSelector(
            "datalist#food_list option[value=\"McDONALD'S, BIG MAC\"]",
            new Page.WaitForSelectorOptions().setState(
                WaitForSelectorState.ATTACHED));
        PlaywrightBrowser.page.getByPlaceholder("Search Food").fill("McDONALD'S, BIG MAC");
        PlaywrightBrowser.page.locator("#nutritionInfo").click();
    }

    @And("I click the button for adding the food")
    public void i_click_the_button_for_adding_the_food() {
        PlaywrightBrowser.page.getByRole(AriaRole.BUTTON,
            new Page.GetByRoleOptions().setName("Add Meal")).click();
    }

    @Then("I see the food reflected in my daily nutritional overview")
    public void i_see_the_food_reflected_in_my_daily_nutritional_overview() {
        PlaywrightBrowser.page.navigate(PlaywrightBrowser.baseUrl + "/profilePage");
        Assertions.assertEquals("14 g", PlaywrightBrowser.page.waitForSelector("#totalFat",
            new Page.WaitForSelectorOptions().setState(VISIBLE)).innerText());
        Assertions.assertEquals("20 g", PlaywrightBrowser.page.waitForSelector("#totalCarbs",
            new Page.WaitForSelectorOptions().setState(VISIBLE)).innerText());
        Assertions.assertEquals("11 g", PlaywrightBrowser.page.waitForSelector("#totalProtein",
            new Page.WaitForSelectorOptions().setState(VISIBLE)).innerText());

    }
}