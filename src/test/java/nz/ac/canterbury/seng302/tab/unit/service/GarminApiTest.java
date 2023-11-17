package nz.ac.canterbury.seng302.tab.unit.service;

import nz.ac.canterbury.seng302.tab.api.GarminApi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Test for ht garmian api
 */
@SpringBootTest
class GarminApiTest {
    @Test
    void getAuthorizationBaseUrlWithLoadingNull() {
        String authUrl = "https://connect.garmin"

            + ".com/oauthConfirm?oauth_callback=" + "null" + "/verifyAuth";
        GarminApi garminApi = GarminApi.instance();
        String url = garminApi.getAuthorizationBaseUrl();
        Assertions.assertEquals(url, authUrl);
    }

    @Test
    void getAuthorizationBaseUrlWithLocalHostoading() {
        String authUrl = "https://connect.garmin"

            + ".com/oauthConfirm?oauth_callback=" + "http://localhost:8080" + "/verifyAuth";
        GarminApi garminApi = GarminApi.instance();
        GarminApi.setUrl("http://localhost:8080");
        String url = garminApi.getAuthorizationBaseUrl();
        Assertions.assertEquals(url, authUrl);
    }
}