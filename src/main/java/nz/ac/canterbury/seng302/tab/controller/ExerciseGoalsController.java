package nz.ac.canterbury.seng302.tab.controller;

import java.io.IOException;
import nz.ac.canterbury.seng302.tab.entity.ExerciseGoals;
import nz.ac.canterbury.seng302.tab.formobjects.ExerciseGoalsForm;
import nz.ac.canterbury.seng302.tab.service.ExerciseGoalsService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Controller class for handling exercise goals
 */
@Controller
public class ExerciseGoalsController {

    /**
     * Allows to log
     */
    Logger logger = LoggerFactory.getLogger(ExerciseGoalsController.class);

    @Autowired
    ExerciseGoalsService exerciseGoalsService;

    @Autowired
    TabUserService tabUserService;

    @Autowired
    ValidationService validationService;

    /**
     * Saves the exercise goals to the database
     * @param exerciseGoalsForm the form containing the exercise goals
     * @return redirect to the profile page
     */
    @PostMapping("/setGoals")
    public String saveGoals(@ModelAttribute ExerciseGoalsForm exerciseGoalsForm,
                            BindingResult bindingResult)
        throws IOException {
        logger.info("POST /setGoals");

        if (validationService.validateExerciseGoalForm(exerciseGoalsForm)) {

            if (exerciseGoalsForm.getId() != null) {
                exerciseGoalsService.updateExerciseGoal(exerciseGoalsForm);
            } else {
                ExerciseGoals exerciseGoals = new ExerciseGoals(exerciseGoalsForm,
                    tabUserService.getCurrentlyLoggedIn());
                exerciseGoalsService.save(exerciseGoals);
            }
        }

        return "redirect:./profilePage";
    }
}