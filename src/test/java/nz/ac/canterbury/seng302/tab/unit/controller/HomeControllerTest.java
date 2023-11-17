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
class HomeControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    /**
     * Setup before the entire test
     */
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    /**
     * Test on if view name is present upon landing on the home page
     *
     * @throws Exception if error
     */
    @Test
    void givenWelcomePageUri_whenMockMvx_thenReturnsJspViewName() throws Exception {
        mockMvc.perform(get("/demo"))
            .andDo(print())
            .andExpect(view().name("welcome"));
    }

    /**
     * Test on if view name is present if redirected to the home page
     *
     * @throws Exception if error
     */
    @Test
    void givenNoPageUri_whenMockMvc_thenReturnsJspViewName() throws Exception {
        mockMvc.perform(get("/"))
            .andDo(print())
            .andExpect(view().name("redirect:./demo"));
    }
}
