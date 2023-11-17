package nz.ac.canterbury.seng302.tab.unit.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import nz.ac.canterbury.seng302.tab.unit.UnitCommonTestSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for view teams controller
 */
@SpringBootTest
@AutoConfigureMockMvc
class ViewTeamsControllerTest {

    private final UnitCommonTestSetup unitCommonTestSetup = new UnitCommonTestSetup();
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    }


    /**
     * Test to see whether the view teams page is corrected
     *
     * @throws Exception throws exception if mockMvc.perform throws an error
     */
    @Test
    @WithMockUser(username = "test@gmail.com", password = "Qwerty10!", authorities = "USER")
    void contextLoads() throws Exception {

        mockMvc.perform(get("/viewTeams").with(csrf())
                .contentType("text/html; charset=UTF-8"))
            .andExpect(status().isOk());
    }

}
