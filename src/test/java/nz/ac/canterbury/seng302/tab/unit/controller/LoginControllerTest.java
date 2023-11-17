package nz.ac.canterbury.seng302.tab.unit.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoginControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    /**
     * Set up before the entire test
     */
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    /**
     * View name displayed upon entering the login page from the full URL
     *
     * @throws Exception if error
     */
    @Test
    void givenLoginPageUri_whenMockMvc_thenReturnsJspViewName() throws Exception {
        mockMvc.perform(get("/login"))
            .andDo(print())
            .andExpect(view().name("login"));
    }
}
