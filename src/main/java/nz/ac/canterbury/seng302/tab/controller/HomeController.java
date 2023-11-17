package nz.ac.canterbury.seng302.tab.controller;

import static nz.ac.canterbury.seng302.tab.utility.HeaderHelper.addLoggedInAttr;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * This is a basic spring boot controller, note the @link{Controller} annotation which defines this.
 * This controller defines endpoints as functions with specific HTTP mappings
 */
@Controller
public class HomeController {
    Logger logger = LoggerFactory.getLogger(HomeController.class);
    List<String> quotes = new ArrayList<>(List.of(
        "Overpower. Overtake. Overcome.",
        "The past doesn’t matter. Take today.",
        "Get up, Grow Up, Get Greatness.",
        "War does not determine who is right - only who is left",
        "Start somewhere. Start here.",
        "Everything you do, go hard",
        "Your sports, in your hands"
    ));

    /**
     * Redirects GET default url '/' to '/demo'
     *
     * @return redirect to /demo
     */
    @GetMapping("/")
    public String home() {
        logger.info("GET /");
        return "redirect:./demo";
    }

    /**
     * Gets the thymeleaf page representing the /demo page (a basic welcome screen with some links)
     *
     * @param model (map-like) representation of data to be used in thymeleaf display
     * @return thymeleaf welcome
     */
    @GetMapping("/demo")
    public String getTemplate(Model model) {
        logger.info("GET /demo");
        addLoggedInAttr(model);
        int quoteIndex = new Random().nextInt(quotes.size());
        String quote = quotes.get(quoteIndex);
        model.addAttribute("quote", quote);
        return "welcome";
    }
}
