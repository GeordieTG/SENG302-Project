package nz.ac.canterbury.seng302.tab.unit.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nz.ac.canterbury.seng302.tab.controller.rest.NutritionRestController;
import nz.ac.canterbury.seng302.tab.entity.Food;
import nz.ac.canterbury.seng302.tab.entity.RecommendedMeal;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.entity.datatransferobjects.RecommendedMealDto;
import nz.ac.canterbury.seng302.tab.service.MealService;
import nz.ac.canterbury.seng302.tab.service.NutritionApi;
import nz.ac.canterbury.seng302.tab.service.RecordedFoodService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.ValidationService;
import nz.ac.canterbury.seng302.tab.unit.UnitCommonTestSetup;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@AutoConfigureMockMvc
@SpringBootTest
class NutritionRestControllerTest {

    @MockBean
    private NutritionApi nutritionApi;

    @MockBean
    private RecordedFoodService recordedFoodService;

    @MockBean
    private TabUserService tabUserService;
    @Mock
    private ValidationService validationService;

    @InjectMocks
    private NutritionRestController nutritionRestController;
    @MockBean
    private MealService mealService;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    Logger logger = LoggerFactory.getLogger(NutritionRestController.class);

    private static String readFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line);
        }
        reader.close();
        return content.toString();
    }

    @Test
    @WithMockUser(username = "test@gmail.com", password = "Qwerty10!", authorities = "USER")
    void searchFoods_foodsReturned() throws Exception {
        String expectedResponseString =
            readFile("api_example_responses/searchFoods_foodsReturned_ExampleResponse.txt");
        JSONParser parser = new JSONParser();
        JSONObject expectedResponse = (JSONObject) parser.parse(expectedResponseString);
        when(nutritionApi.searchFoods(any())).thenReturn(expectedResponse);

        MvcResult mvcResult = mockMvc.perform(
                get("/search-foods/apple").with(csrf()).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andReturn();
        String actualResponseString = mvcResult.getResponse().getContentAsString();
        JSONObject actualResponse = (JSONObject) parser.parse(actualResponseString);
        assertEquals(expectedResponse, actualResponse);

    }

    @Test
    @WithMockUser(username = "test@gmail.com", password = "Qwerty10!", authorities = "USER")
    void getFoods_foodsReturned() throws Exception {
        String expectedResponseString =
            readFile("api_example_responses/getFoods_foodsReturned_ExampleResponse.txt");
        JSONParser parser = new JSONParser();
        JSONArray expectedResponse = (JSONArray) parser.parse(expectedResponseString);
        when(nutritionApi.getFoods(any())).thenReturn(expectedResponse);

        MvcResult mvcResult = mockMvc.perform(
                get("/get-foods/1,2").with(csrf()).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andReturn();
        String actualResponseString = mvcResult.getResponse().getContentAsString();
        JSONArray actualResponse = (JSONArray) parser.parse(actualResponseString);
        assertEquals(expectedResponse, actualResponse);

    }


    @Test
    @WithMockUser(username = "test@gmail.com", password = "Qwerty10!", authorities = "USER")
    void searchFoods_noFoodsReturned() throws Exception {
        String expectedResponseString =
            readFile("api_example_responses/searchFoods_noFoodsReturned_ExampleResponse.txt");
        JSONParser parser = new JSONParser();
        JSONObject expectedResponse = (JSONObject) parser.parse(expectedResponseString);
        when(nutritionApi.searchFoods(any())).thenReturn(expectedResponse);

        MvcResult mvcResult = mockMvc.perform(
                get("/search-foods/apple").with(csrf()).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andReturn();
        String actualResponseString = mvcResult.getResponse().getContentAsString();
        JSONObject actualResponse = (JSONObject) parser.parse(actualResponseString);
        assertEquals(expectedResponse, actualResponse);

    }


    @Test
    @WithMockUser(username = "test@gmail.com", password = "Qwerty10!", authorities = "USER")
    void getFoods_noFoodsReturned() throws Exception {
        String expectedResponseString =
            readFile("api_example_responses/getFoods_foodsReturned_ExampleResponse.txt");
        JSONParser parser = new JSONParser();
        JSONArray expectedResponse = (JSONArray) parser.parse(expectedResponseString);
        when(nutritionApi.getFoods(any())).thenReturn(expectedResponse);

        MvcResult mvcResult = mockMvc.perform(
                get("/get-foods/1,2").with(csrf()).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andReturn();
        String actualResponseString = mvcResult.getResponse().getContentAsString();
        JSONArray actualResponse = (JSONArray) parser.parse(actualResponseString);
        assertEquals(expectedResponse, actualResponse);

    }

    @Test
    @WithMockUser(username = "test@gmail.com", password = "Qwerty10!", authorities = "USER")
    void getFoods_internalServerError() throws Exception {
        when(nutritionApi.getFoods(any())).thenThrow(
            new InterruptedException("Test Exception"));

        mockMvc.perform(
                get("/get-foods/1,2").with(csrf()).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is5xxServerError());
    }

    @Test
    @WithMockUser(username = "test@gmail.com", password = "Qwerty10!", authorities = "USER")
    void searchFoods_internalServerError() throws Exception {
        when(nutritionApi.searchFoods(any())).thenThrow(
            new InterruptedException("Test Exception"));

        mockMvc.perform(
                get("/search-foods/apple").with(csrf()).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is5xxServerError());
    }





    @Test
    @WithMockUser(username = "test@gmail.com", password = "Qwerty10!", authorities = "USER")
    void getDetailsOfFoodListTest() throws Exception {

        Map<String, Double> sample = new HashMap<>();
        sample.put(RecordedFoodService.PROTEIN, 1.0);
        sample.put(RecordedFoodService.SUGAR, 2.0);
        sample.put(RecordedFoodService.FAT, 3.0);
        sample.put(RecordedFoodService.CARBOHYDRATES, 4.0);
        sample.put(RecordedFoodService.ENERGY, 5.0);


        JSONObject jsonObject = new JSONObject();
        jsonObject.put(RecordedFoodService.PROTEIN, 1.0);
        jsonObject.put(RecordedFoodService.SUGAR, 2.0);
        jsonObject.put(RecordedFoodService.FAT, 3.0);
        jsonObject.put(RecordedFoodService.CARBOHYDRATES, 4.0);
        jsonObject.put(RecordedFoodService.ENERGY, 5.0);



        when(recordedFoodService.addNutritionInfoToModel(any(),
            any(LocalDate.class))).thenReturn(
            sample);

        MvcResult mvcResult = mockMvc.perform(
                get("/get-foods/last-7-days/1")
                    .with(csrf()).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andReturn();
        String actualResponseString = mvcResult.getResponse()
            .getContentAsString();
        JSONArray actualResponse = (JSONArray) new JSONParser()
            .parse(actualResponseString);
        assertEquals(7, actualResponse.size());
        for (Object obj : actualResponse) {
            assertEquals(jsonObject, obj);
        }
    }

    @Test
    void whenValidInput_thenRedirect() throws Exception {
        TabUser user = UnitCommonTestSetup.createTestUser();
        when(tabUserService.getCurrentlyLoggedIn()).thenReturn(user);
        when(validationService.validateAddFoodForm(any(), any(),
                any())).thenReturn(true);
        when(recordedFoodService.saveFoodToDatabase(any())).thenReturn(null);
        mockMvc.perform(post("/addFood").param("quantity", "1")
                .param("fdcId", "1")
                .param("portionSizeNumber", "1").with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void whenInvalidInput_thenRedirect() throws Exception {
        TabUser user = UnitCommonTestSetup.createTestUser();
        when(validationService.validateAddFoodForm(any(), any(),
                any())).thenReturn(false);
        when(tabUserService.getCurrentlyLoggedIn()).thenReturn(user);
        when(recordedFoodService.saveFoodToDatabase(any())).thenReturn(null);
        mockMvc.perform(post("/addFood").param("quantity", "1")
                .param("fdcId", "1")
                .param("portionSizeNumber", "1").with(csrf()))
                .andExpect(status().is3xxRedirection());
    }


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @ValueSource(strings = {"bulk", "cut", "maintain"})
    void testUpdateCaloriePreferenceBulk(String requestBody) {
        TabUser user = UnitCommonTestSetup.createTestUser();
        when(tabUserService.getCurrentlyLoggedIn()).thenReturn(user);

        ResponseEntity<Map<String, String>> response =
                nutritionRestController.updateCaloriePreference(requestBody);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(tabUserService).updateUser(user);
    }

    @Test
    void testUpdateCaloriePreferenceError() {
        String requestBody = "invalidPreference";
        TabUser user = UnitCommonTestSetup.createTestUser();

        when(tabUserService.getCurrentlyLoggedIn()).thenReturn(user);

        ResponseEntity<Map<String, String>> response =
                nutritionRestController.updateCaloriePreference(requestBody);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        // Verify that updateUser was not called in this case
        verify(tabUserService, never()).updateUser(any());
    }

    @Test
    void testUpdateCaloriePreferenceUserError() {
        Exception testException = new RuntimeException("Test exception message");
        String requestBody = "cut";

        when(tabUserService.getCurrentlyLoggedIn()).thenThrow(testException);

        ResponseEntity<Map<String, String>> responseEntity =
                nutritionRestController.updateCaloriePreference(requestBody);


        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        verify(logger).error("Could not update calorie preference: "
            + testException.getMessage(), testException);
    }

    @Test
    @WithMockUser(username = "test@gmail.com", password = "Qwerty10!", authorities = "USER")
    void testGetRandomMeals() throws Exception {
        TabUser testUser = UnitCommonTestSetup.createTestUser();
        testUser.setCalorieIntakePreference("Maintain");
        when(tabUserService.getCurrentlyLoggedIn()).thenReturn(testUser);
        Food food = UnitCommonTestSetup.createTestFood();
        Food food2 = UnitCommonTestSetup.createTestFood();
        food2.setId(2L);
        RecommendedMeal recommendedMeal = UnitCommonTestSetup.createTestMeal();
        food.setMeal(recommendedMeal);
        food2.setMeal(recommendedMeal);
        List<Food> foods = new ArrayList<>();
        foods.add(food);
        foods.add(food2);
        recommendedMeal.setFoods(foods);
        List<RecommendedMeal> recommendedMealList = new ArrayList<>();
        recommendedMealList.add(recommendedMeal);
        when(mealService.getAllMealsByPreference(testUser.getCalorieIntakePreference()))
                .thenReturn(recommendedMealList);

        RecommendedMealDto recommendedMealDto = new RecommendedMealDto(recommendedMeal.getName(),
            1.0, 1.0, 1.0, 1.0, foods, recommendedMeal.getId(), 1, "default");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(List.of(recommendedMealDto));


        when(mealService.calculateRequiredMealQuantity(Mockito.any(), Mockito.any(), Mockito.any()))
            .thenReturn(1.0);
        when(mealService.calculateCalorieTotal(Mockito.any())).thenReturn(1.0);
        when(mealService.calculateProteinTotal(Mockito.any())).thenReturn(1.0);
        when(mealService.calculateFatTotal(Mockito.any())).thenReturn(1.0);
        when(mealService.calculateSugarTotal(Mockito.any())).thenReturn(1.0);

        MvcResult mvcResult = mockMvc.perform(
            get("/get-recommended-meals/500").with(csrf()))
            .andExpect(status().is2xxSuccessful()).andReturn();
        String actualResponseString = mvcResult.getResponse().getContentAsString();
        JSONArray actualResponse = (JSONArray) new JSONParser()
            .parse(actualResponseString);
        JSONArray expectedResponse = (JSONArray) new JSONParser()
            .parse(jsonString);
        assertEquals(expectedResponse, actualResponse);

    }

}
