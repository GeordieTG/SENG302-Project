package nz.ac.canterbury.seng302.tab.end2end;

import com.microsoft.playwright.FileChooser;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.nio.file.Paths;
import org.junit.jupiter.api.Assertions;


/**
 * Cucumber for add profile picture to user feature
 */

public class AddProfilePictureToUserFeature {

    boolean fileChooserEventFired = false;
    FileChooser fileChooser;

    String currentProfilePicture;

    @When("I hit the edit profile picture button")
    public void i_hit_the_edit_profile_picture_button() {

        fileChooser = PlaywrightBrowser.page.waitForFileChooser(
            () -> PlaywrightBrowser.page.click("label[for='profilePicture']"));
    }

    @Then("I see a file picker")
    public void i_see_a_file_picker() {
        Assertions.assertNotNull(fileChooser);
    }

    @Given("I choose a new profile picture")
    public void i_choose_a_new_profile_picture() {
        PlaywrightBrowser.page.navigate(PlaywrightBrowser.baseUrl + "/profilePage");
        currentProfilePicture =
            PlaywrightBrowser.page.locator("#picture-id").getAttribute("src");
        fileChooser = PlaywrightBrowser.page.waitForFileChooser(
            () -> PlaywrightBrowser.page.click("label[for='profilePicture']"));
    }

    @Given("I am on my profile page")
    public void i_am_on_my_profile_page() {

        PlaywrightBrowser.page.navigate(PlaywrightBrowser.baseUrl + "/profilePage");
        currentProfilePicture =
            PlaywrightBrowser.page.locator("#picture-id").getAttribute("src");
    }

    @When("I submit a file of the right type and size")
    public void i_submit_a_file_of_the_right_type_and_size() {
        fileChooser.setFiles(Paths.get("e2e_test_files/valid.jpeg"));
        PlaywrightBrowser.page.locator("#profileImageButton").click();
    }

    @Then("my profile picture is updated.")
    public void my_profile_picture_is_updated() {
        Assertions.assertNotEquals(currentProfilePicture,
            PlaywrightBrowser.page.locator("#picture-id").getAttribute("src"));
    }

    @When("I submit a file that is not either a png, jpg or svg")
    public void i_submit_a_file_that_is_not_either_a_png_jpg_or_svg() {
        fileChooser.setFiles(Paths.get("e2e_test_files/invalid_type.gif"));
        PlaywrightBrowser.page.locator("#profileImageButton").click();
    }


    @When("I submit a valid file with a size of more than 10MB")
    public void i_submit_a_valid_file_with_a_size_of_more_than_mb() {
        fileChooser.setFiles(Paths.get("e2e_test_files/12MB.jpg"));
        PlaywrightBrowser.page.locator("#profileImageButton").click();
    }

    @Then("My profile picture is not updated")
    public void my_profile_picture_is_not_updated() {
        PlaywrightBrowser.page.reload();
        Assertions.assertEquals(currentProfilePicture,
            PlaywrightBrowser.page.locator("#picture-id").getAttribute("src"));
    }


    @Then("I see my profile picture")
    public void i_see_my_profile_picture() {
        Assertions.assertTrue(PlaywrightBrowser.page.locator("#picture-id").isVisible());
        Assertions.assertFalse(
            PlaywrightBrowser.page.locator("#picture-id").getAttribute("src").isEmpty());
    }
}
