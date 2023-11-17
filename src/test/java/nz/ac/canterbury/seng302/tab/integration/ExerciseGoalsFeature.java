package nz.ac.canterbury.seng302.tab.integration;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import nz.ac.canterbury.seng302.tab.entity.ExerciseGoals;
import nz.ac.canterbury.seng302.tab.formobjects.ExerciseGoalsForm;
import nz.ac.canterbury.seng302.tab.service.ExerciseGoalsService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration Test for setting exercise goals.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ExerciseGoalsFeature {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    TabUserService tabUserService;

    @Autowired
    ExerciseGoalsService exerciseGoalsService;

    @When("I submit a form with my daily activity goals")
    public void i_submit_a_form_with_my_daily_activity_goals() throws Exception {

        ExerciseGoalsForm exerciseGoalsForm = new ExerciseGoalsForm();
        exerciseGoalsForm.setSteps(100);
        exerciseGoalsForm.setCaloriesBurnt(123f);
        exerciseGoalsForm.setDistanceTravelled(125f);
        exerciseGoalsForm.setTotalActivityTime(16f);

        mockMvc.perform(post("/setGoals").with(
                    user(tabUserService.getCurrentlyLoggedIn().getEmail()).password(
                        tabUserService.getCurrentlyLoggedIn().getPassword()).roles("USER"))
                .with(csrf())
                .contentType("text/html; charset=UTF-8")
                .flashAttr("exerciseGoalsForm", exerciseGoalsForm))
            .andExpect(status().is3xxRedirection());
    }

    @Then("my goals are saved in the database")
    public void my_goals_are_saved_in_the_database() {
        ExerciseGoals exerciseGoals = exerciseGoalsService.getByUser(
            IntegrationTestConfigurations.loggedInUser);
        Assertions.assertEquals(100, exerciseGoals.getSteps());
        Assertions.assertEquals(123, exerciseGoals.getCaloriesBurnt());
        Assertions.assertEquals(125, exerciseGoals.getDistanceTravelled());
        Assertions.assertEquals(16, exerciseGoals.getTotalActivityTime());
    }

    @When("I submit a form with my daily activity goals containing floats")
    public void i_submit_a_form_with_my_daily_activity_goals_containing_floats() throws Exception {
        ExerciseGoalsForm exerciseGoalsForm = new ExerciseGoalsForm();
        exerciseGoalsForm.setSteps(100);
        exerciseGoalsForm.setCaloriesBurnt(123.3f);
        exerciseGoalsForm.setDistanceTravelled(125.3f);
        exerciseGoalsForm.setTotalActivityTime(16.4f);

        mockMvc.perform(post("/setGoals").with(
                    user(tabUserService.getCurrentlyLoggedIn().getEmail()).password(
                        tabUserService.getCurrentlyLoggedIn().getPassword()).roles("USER"))
                .with(csrf())
                .contentType("text/html; charset=UTF-8")
                .flashAttr("exerciseGoalsForm", exerciseGoalsForm))
            .andExpect(status().is3xxRedirection());
    }

    @Then("my goals are saved in the database as floats")
    public void my_goals_are_saved_in_the_database_as_floats() {
        ExerciseGoals exerciseGoals = exerciseGoalsService.getByUser(
            IntegrationTestConfigurations.loggedInUser);
        Assertions.assertEquals(100, exerciseGoals.getSteps());
        Assertions.assertEquals(123.3f, exerciseGoals.getCaloriesBurnt());
        Assertions.assertEquals(125.3f, exerciseGoals.getDistanceTravelled());
        Assertions.assertEquals(16.4f, exerciseGoals.getTotalActivityTime());
    }

    @When("I submit a form with my daily activity goals containing a subset of possible goals")
    public void i_submit_a_form_with_my_daily_activity_goals_containing_a_subset_of_possible_goals()
        throws Exception {
        ExerciseGoalsForm exerciseGoalsForm = new ExerciseGoalsForm();
        exerciseGoalsForm.setCaloriesBurnt(12.2f);
        exerciseGoalsForm.setDistanceTravelled(12.3f);

        mockMvc.perform(post("/setGoals").with(
                    user(tabUserService.getCurrentlyLoggedIn().getEmail()).password(
                        tabUserService.getCurrentlyLoggedIn().getPassword()).roles("USER"))
                .with(csrf())
                .contentType("text/html; charset=UTF-8")
                .flashAttr("exerciseGoalsForm", exerciseGoalsForm))
            .andExpect(status().is3xxRedirection());
    }

    @Then("my goals are saved in the database as a subset of goals")
    public void my_goals_are_saved_in_the_database_as_a_subset_of_goals() {
        ExerciseGoals exerciseGoals = exerciseGoalsService.getByUser(
            IntegrationTestConfigurations.loggedInUser);
        Assertions.assertEquals(12.2f, exerciseGoals.getCaloriesBurnt());
        Assertions.assertEquals(12.3f, exerciseGoals.getDistanceTravelled());
    }
}
