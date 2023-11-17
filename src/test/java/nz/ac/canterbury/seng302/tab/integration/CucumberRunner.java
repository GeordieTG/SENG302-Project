package nz.ac.canterbury.seng302.tab.integration;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Runner for Integration tests to be included in coverage
 */
@RunWith(Cucumber.class)
@CucumberOptions(plugin = {
    "pretty", "html:build/reports/tests/integration/integration-report.html"}, features = {
        "src/test/resources/features/integration"},
    glue = {"nz/ac/canterbury/seng302/tab/integration"})
public class CucumberRunner {
}
