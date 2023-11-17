package nz.ac.canterbury.seng302.tab.unit.service;

import static org.mockito.Mockito.when;

import nz.ac.canterbury.seng302.tab.repository.TabUserRepository;
import nz.ac.canterbury.seng302.tab.service.TabUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Unit tests with Mockito for TabUserService.
 * Contents not same as the non-mocked test with the same name.
 */
@SpringBootTest
class TabUserServiceTest {

    @Mock
    TabUserRepository mockTabUserRepository;

    @Autowired
    TabUserService tabUserService;

    /**
     * A simple unit test to check if the method getCalorieIntakePreference
     * returns string value as expected.
     */
    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3})
    void getCalorieIntakePreferenceTest(Long arg) {
        when(mockTabUserRepository.getCalorieIntakePreference(1L))
            .thenReturn("Maintain");
        when(mockTabUserRepository.getCalorieIntakePreference(2L))
            .thenReturn(null);
        when(mockTabUserRepository.getCalorieIntakePreference(3L))
            .thenReturn("Kangaroo");
        Assertions.assertEquals("Maintain",
            tabUserService.getCalorieIntakePreference(1L), "ERROR: method not operating as "
                + "expected");
        Assertions.assertEquals("Maintain",
            tabUserService.getCalorieIntakePreference(2L), "ERROR: null case failure");
        Assertions.assertEquals("Maintain",
            tabUserService.getCalorieIntakePreference(3L), "ERROR: return value not within the "
                + "expected list of values");
    }
}
