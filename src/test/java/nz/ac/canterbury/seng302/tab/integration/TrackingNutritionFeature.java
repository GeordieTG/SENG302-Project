package nz.ac.canterbury.seng302.tab.integration;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.UnsupportedEncodingException;
import javassist.NotFoundException;
import nz.ac.canterbury.seng302.tab.entity.RecordedFood;
import nz.ac.canterbury.seng302.tab.service.RecordedFoodService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

/**
 * Integration test class for Tracking Nutrition
 */
@SpringBootTest
@AutoConfigureMockMvc
public class TrackingNutritionFeature {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    RecordedFoodService recordedFoodService;

    @Autowired
    TabUserService tabUserService;
    Long selectedFoodQuantity;

    Long selectedFoodFdcId;

    Long selectedFoodPortionSizeNumber;

    JSONObject selectedFood;

    JSONObject searchFoodResponse;

    JSONArray actualResponse;


    @When("I search for the food {string}")
    public void i_search_for_the_food(String searchQuery) throws Exception {

        MvcResult mvcResult = mockMvc.perform(get("/search-foods/" + searchQuery).with(csrf())
            .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        searchFoodResponse = (JSONObject) new org.json.simple.parser.JSONParser().parse(response);
    }

    @When("I select the first result")
    public void i_select_the_first_result() {
        JSONObject firstResult = (JSONObject) ((JSONArray) searchFoodResponse.get("foods")).get(0);
        selectedFood = firstResult;
        selectedFoodFdcId = (Long) firstResult.get("fdcId");
    }

    @When("I select the first serving size")
    public void i_select_the_first_serving_size() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
            get("/get-foods/" + selectedFoodFdcId.toString()).with(csrf())
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        JSONArray getFoodResponse =
            (JSONArray) new org.json.simple.parser.JSONParser().parse(response);
        JSONObject selectedFoodDetails = (JSONObject) getFoodResponse.get(0);
        JSONObject firstPortionSize =
            (JSONObject) ((JSONArray) selectedFoodDetails.get("foodPortions")).get(0);
        selectedFoodPortionSizeNumber = (Long) firstPortionSize.get("id");
    }


    @When("I select {int} as the quantity")
    public void i_select_as_the_quantity(int quantity) {
        selectedFoodQuantity = (long) quantity;
    }

    @When("I submit the add food form")
    public void i_submit_the_add_food_form() throws Exception {

        mockMvc.perform(
                post("/addFood").with(csrf()).param("quantity", selectedFoodQuantity.toString())
                    .param("fdcId", selectedFoodFdcId.toString())
                    .param("portionSizeNumber", selectedFoodPortionSizeNumber.toString()))
            .andExpect(status().is2xxSuccessful()).andReturn();
    }


    @Then("The food is persisted in the database")
    public void the_food_is_persisted_in_the_database() throws NotFoundException {
        RecordedFood recordedFood = recordedFoodService.getFoodFromDatabase(1L);
        Assertions.assertEquals(selectedFoodFdcId, recordedFood.getFdcId());
        Assertions.assertEquals(selectedFoodQuantity, recordedFood.getQuantity());
        Assertions.assertEquals(selectedFoodPortionSizeNumber, recordedFood.getPortionSizeNumber());
        Assertions.assertEquals(IntegrationTestConfigurations.loggedInUser, recordedFood.getUser());

    }

}