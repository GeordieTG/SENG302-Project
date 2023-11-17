package nz.ac.canterbury.seng302.tab.end2end;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;


/**
 * Test for Edit Team Role Feature using E2E tests
 */
public class EditTeamRoleFeature {

    @Given("I am in the team profile page of a team I manage")
    public void am_in_the_team_profile_page_of_a_team_i_manage() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @When("when I click on a UI element to edit the members role,")
    public void when_i_click_on_a_ui_element_to_edit_the_members_role() {
        PlaywrightBrowser.page.locator("#nav-member-tab").click();
    }

    @Then("I see the list of members and their roles in the team.")
    public void see_the_list_of_members_and_their_roles_in_the_team() {
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#rolePicture"));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#memberName"));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#Role"));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#team-member_0"));
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#member_role_0"));
    }


    @Then("their roles can be one of “manager”, “coach”, or “member”.")
    public void their_roles_can_be_one_of_manager_coach_or_member() {

        Locator teamMemberAndRole = PlaywrightBrowser.page.locator("#teamMemberAndRoleList");
        int teamMemberSize = teamMemberAndRole.count();
        List<String> roleList = new ArrayList<>(Arrays.asList("Member", "Coach", "Manager"));

        for (int i = 0; i < teamMemberSize; i++) {
            Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#team-member_" + i));
            Locator roleDetail = PlaywrightBrowser.page.locator("#member_role_" + i);
            String role = roleDetail.textContent();

            Assertions.assertTrue(roleList.contains(role));
        }
    }

    @Given("I click on a UI element to edit the members role")
    public void click_on_a_ui_element_to_edit_the_members_role() {
        PlaywrightBrowser.page.click("a:has-text('Edit')");
        PlaywrightBrowser.page.click("a[href='#nav-members']");
    }

    @Given("I change my role from manager to coach")
    public void change_my_role_from_manager_to_coach() {
        PlaywrightBrowser.page.selectOption(".team_roles_dropdown", "Member");
    }

    @When("I hit the save changes button")
    public void hit_the_save_changes_button() {
        PlaywrightBrowser.page.click("button:has-text('Save Changes')");
    }

    @Then("I see an error message about {string}")
    public void see_an_error_message(String messageTag) {
        PlaywrightBrowser.page.waitForSelector("#" + messageTag);
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#" + messageTag));
    }

    @And("My changes are not saved")
    public void my_changes_are_not_saved() {
        String currentUrl = PlaywrightBrowser.page.url();
        Assertions.assertTrue(currentUrl.contains("/editTeamProfile"));
    }

    @Given("I am on my team’s profile that I manage")
    public void am_on_my_teams_profile_that_i_manage() {
        PlaywrightBrowser.page.locator("#team-dropdown-btn").click();
        PlaywrightBrowser.page.locator("#viewMyTeamsBtn").click();
        PlaywrightBrowser.page.locator("#index-5").click();
        String currentUrl = PlaywrightBrowser.page.url();
        Assertions.assertTrue(currentUrl.contains("/viewTeam"));
    }

    @Given("I change {int} people to manager")
    public void change_people_to_manager(int peopleToManager) {
        List<ElementHandle> dropdowns =
            PlaywrightBrowser.page.querySelectorAll("select.team_roles_dropdown");
        for (int i = 0; i < peopleToManager; i++) {
            dropdowns.get(i).selectOption("Manager");
        }
    }

    @And("I change {int} people to manager and I change {int} people to coach")
    public void change_people_to_manager_and_i_change_people_to_coach(int numManagers,
                                                                      int numCoaches) {
        List<ElementHandle> dropdowns =
            PlaywrightBrowser.page.querySelectorAll("select.team_roles_dropdown");
        List<ElementHandle> managers = dropdowns.subList(0, numManagers);
        List<ElementHandle> coaches = dropdowns.subList(numManagers, numCoaches + numManagers);
        for (ElementHandle manager : managers) {
            manager.selectOption("Manager");
        }
        for (ElementHandle coach : coaches) {
            coach.selectOption("Coach");
        }
    }

    @Then("I am on my teams profile")
    public void am_on_my_teams_profile() {
        String currentUrl = PlaywrightBrowser.page.url();
        Assertions.assertTrue(currentUrl.contains("/viewTeam"));
    }

    @And("I see a success message about {string}")
    public void see_a_success_message_about(String messageTag) {
        PlaywrightBrowser.page.waitForSelector("#" + messageTag);
        Assertions.assertTrue(PlaywrightBrowser.page.isVisible("#" + messageTag));
    }


}
