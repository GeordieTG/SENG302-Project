package nz.ac.canterbury.seng302.tab.integration;

import io.cucumber.java.Before;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import nz.ac.canterbury.seng302.tab.TabApplication;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

/**
 * Integration tests as the Voting Feature example shows, tests the applications function,
 * including the
 * application context.
 * The annotations on this file tell cucumber how the context of the integration tests
 * are to be setup. You do not need
 * to know how they work, just that they set up the application to run in the tests.
 * The @CucumberContextConfiguration annotation is required to provide the context of the tests,
 * in this case the spring
 * context. This is so that the glue can argument can find these files,
 * and understand how the tests are to be run.
 */
@CucumberContextConfiguration
@ActiveProfiles("test")
@ContextConfiguration(classes = TabApplication.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@CucumberOptions(tags = "@isolate")
public class IntegrationTestConfigurations {
    public static TabUser loggedInUser;

    @Before
    public void setUp() {
        IntegrationTestConfigurations.loggedInUser = null;
    }
}
