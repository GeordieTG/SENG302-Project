package nz.ac.canterbury.seng302.tab.unit.controller;


import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import javassist.NotFoundException;
import nz.ac.canterbury.seng302.tab.controller.NutritionController;
import nz.ac.canterbury.seng302.tab.controller.UserProfileController;
import nz.ac.canterbury.seng302.tab.controller.ViewClubController;
import nz.ac.canterbury.seng302.tab.entity.Club;
import nz.ac.canterbury.seng302.tab.entity.TabUser;
import nz.ac.canterbury.seng302.tab.service.RecordedFoodService;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import nz.ac.canterbury.seng302.tab.service.ValidationService;
import nz.ac.canterbury.seng302.tab.unit.UnitCommonTestSetup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

@SpringBootTest
@AutoConfigureMockMvc
class NutritionControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Mock
    private ValidationService validationService;
    @Mock
    private TabUserService tabUserService;
    @Mock
    private RecordedFoodService recordedFoodService;


    @InjectMocks
    private NutritionController nutritionController;

    @InjectMocks
    UserProfileController userProfileController;

    @Mock
    Model model;



    /**
     * Test to see whether the nutrition page page is corrected
     *
     * @throws Exception throws exception if mockMvc.perform throws an error
     */

    @Test
    void testLoadingCorrectNutritionPage() throws Exception {
        Long id = 123L;
        TabUser user = UnitCommonTestSetup.createTestUser();
        user.setId(id);
        Model model = new ConcurrentModel();
        when(tabUserService.getCurrentlyLoggedIn()).thenReturn(user);

        String response = nutritionController.nutritionGet(model);
        Assertions.assertTrue(response.contains("nutrition"));
        Assertions.assertEquals(model.getAttribute("userId"), id);
    }

    @Test
    void addFoodOverviewDetailsToModel_checkModelAttributes() throws NotFoundException {
        Map<String, Double> sample = new HashMap<>();
        sample.put(RecordedFoodService.PROTEIN, 1.0);
        sample.put(RecordedFoodService.SUGAR, 2.0);
        sample.put(RecordedFoodService.FAT, 3.0);
        sample.put(RecordedFoodService.CARBOHYDRATES, 4.0);
        sample.put(RecordedFoodService.ENERGY, 5.0);
        when(recordedFoodService.addNutritionInfoToModel(Mockito.anyLong(),
            Mockito.any(LocalDate.class)))
            .thenReturn(sample
            );
        TabUser user = UnitCommonTestSetup.createTestUser();
        user.setId(1L);
        when(tabUserService.getCurrentlyLoggedIn()).thenReturn(user);
        userProfileController.addNutritionInformation(model);
        Mockito.verify(model, Mockito.times(1)).addAttribute("totalCalories", 5);
        Mockito.verify(model, Mockito.times(1)).addAttribute("totalFat", 3);
        Mockito.verify(model, Mockito.times(1)).addAttribute("totalCarbs", 4);
        Mockito.verify(model, Mockito.times(1)).addAttribute("totalProtein", 1);
        Mockito.verify(model, Mockito.times(1)).addAttribute("totalSugar", 2);
    }
}
