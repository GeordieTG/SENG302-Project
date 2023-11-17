package nz.ac.canterbury.seng302.tab.controller;

import nz.ac.canterbury.seng302.tab.controller.rest.NutritionRestController;
import nz.ac.canterbury.seng302.tab.service.RecordedFoodService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.ValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller class for the Tracking Nutrition Functionality
 */
@Controller
public class NutritionController {

    private static final Integer RECENT_FOOD_COUNT = 10;

    @Autowired
    TabUserService tabUserService;
    @Autowired
    RecordedFoodService recordedFoodService;
    @Autowired
    ValidationService validationService;

    @Autowired
    NutritionRestController nutritionRestController;

    /**
     * Allows us to log
     */
    Logger logger = LoggerFactory.getLogger(NutritionController.class);


    /**
     * Get request endpoint for display the nutrition trend
     * @param model the theme leaf model
     * @return the nutrition page
     */
    @GetMapping("/nutrition")
    public String nutritionGet(Model model) {
        logger.info("GET /nutrition");
        model.addAttribute("userId", tabUserService.getCurrentlyLoggedIn().getId());
        model.addAttribute("recentFoods",
                recordedFoodService.getRecentJson(tabUserService.getCurrentlyLoggedIn().getId(),
                RECENT_FOOD_COUNT));
        return "nutrition";
    }
}
