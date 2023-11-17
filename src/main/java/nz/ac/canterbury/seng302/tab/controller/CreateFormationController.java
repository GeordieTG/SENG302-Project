package nz.ac.canterbury.seng302.tab.controller;


import javassist.NotFoundException;
import nz.ac.canterbury.seng302.tab.formobjects.FormationForm;
import nz.ac.canterbury.seng302.tab.service.FormationService;
import nz.ac.canterbury.seng302.tab.service.TeamService;
import nz.ac.canterbury.seng302.tab.service.ValidationService;
import nz.ac.canterbury.seng302.tab.utility.ImageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller class for create formation page
 */
@Controller
public class CreateFormationController {

    public static final String FORMATION_FORM = "formationForm";
    /**
     * Allows us to log
     */
    Logger logger = LoggerFactory.getLogger(CreateFormationController.class);

    @Autowired

    FormationService formationService;
    @Autowired

    TeamService teamService;
    @Autowired
    ValidationService validationService;

    /**
     * End Point for create formation page
     *
     * @param model thymeleaf model
     * @return create formation html page
     */
    @GetMapping("/createFormation")
    public String getTemplate(@RequestParam(name = "id") String id,
                              Model model) {
        logger.info("GET /createFormation");

        if (model.getAttribute(FORMATION_FORM) == null) {
            model.addAttribute(FORMATION_FORM, new FormationForm());
        }
        model.addAttribute("id", id);

        return "createFormation";
    }

    /**
     * End Point for create formation page
     *
     * @return create formation html page
     */
    @PostMapping("/createFormation")
    public String createFormation(@ModelAttribute FormationForm formationForm,
                                  @RequestParam(name = "id") String id, Model model)
        throws NotFoundException {
        logger.info("POST /createFormation");

        if (validationService.validateFormationForm(formationForm, model)) {
            formationService.addFormation(formationForm, Long.parseLong(id));

            return displayFormation(id, formationForm.getFormation(), formationForm.getSport(),
                model);
        }
        model.addAttribute(FORMATION_FORM, formationForm);
        return getTemplate(id, model);
    }

    /**
     * Post end point for the setUp button on the create formation form,
     * displays a preview of what the set-up
     * formation would look like
     *
     * @param formationForm user entered details (formation field, sport field)
     * @param id            id of the team you are making the formation for
     * @param model         thymeleaf model
     * @return the form with the details needed for the preview,
     *         or the error messages if validation fails
     */
    @PostMapping("/previewFormation")
    public String previewFormation(@ModelAttribute FormationForm formationForm,
                                   @RequestParam(name = "id") String id, Model model) {
        logger.info("POST /previewFormation");
        formationForm.setFormation(formationForm.getFormation());
        if (validationService.validateFormationForm(formationForm, model)) {
            String bgImage = ImageHelper.getFieldImageLink(formationForm.getSport());
            model.addAttribute("bgImage", bgImage);
            model.addAttribute("id", id);
            model.addAttribute(FORMATION_FORM, formationForm);
            return getTemplate(id, model);
        }
        return getTemplate(id, model);
    }

    /**
     * Get mapping for displaying teams formation
     *
     * @param formation team formation
     * @param field     team field background
     * @param model     the model
     * @return display formation
     */
    @GetMapping("/displayFormation")
    public String displayFormation(@RequestParam(name = "id") String teamId,
                                   @RequestParam(name = "formation") String formation,
                                   @RequestParam(name = "field") String field, Model model) {
        StringBuilder reversedString = new StringBuilder(formation);
        reversedString.reverse();
        String reverseFormation = reversedString.toString();
        model.addAttribute("formation", reverseFormation);
        model.addAttribute("formationForward", formation);
        model.addAttribute("teamId", teamId);
        model.addAttribute("pfp", "images/default.jpg");
        String bgImage = ImageHelper.getFieldImageLink(field);
        if (field.equals("RugbyLeague")) {
            field = "Rugby League";
        }
        model.addAttribute("bgImage", bgImage);
        model.addAttribute("field", field);

        return "displayFormation";
    }
}
