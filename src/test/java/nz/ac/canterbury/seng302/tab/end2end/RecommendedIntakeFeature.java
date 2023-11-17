package nz.ac.canterbury.seng302.tab.end2end;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Integration Test for the calorie intake preference
 */
public class RecommendedIntakeFeature {
    @Then("I can see a dedicated UI element to set my calorie intake preference")
    public void i_can_see_a_dedicated_ui_element_to_set_my_calorie_intake_preference() {
        PlaywrightBrowser.page.locator("#calorieIntakePreferenceInput").isVisible();
    }

    @When("I change my calorie intake preference to {string}")
    public void i_change_my_calorie_intake_preference_to_preference(String preference) {
        PlaywrightBrowser.page.locator("#caloriePreference").selectOption(preference);
    }

    @Then("I can see a dedicated UI element telling me it has changed.")
    public void i_can_see_a_dedicated_ui_element_telling_me_it_has_changed() {
        PlaywrightBrowser.page
                .getByText("Calorie Intake Preference Updated Successfully").isVisible();
    }
}
