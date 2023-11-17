package nz.ac.canterbury.seng302.tab.unit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.github.scribejava.core.model.OAuth1RequestToken;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import nz.ac.canterbury.seng302.tab.controller.GarminController;
import nz.ac.canterbury.seng302.tab.service.GarminService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@SpringBootTest
class GarminControllerTest {
    @Mock
    GarminService garminService;
    @InjectMocks
    private GarminController garminController;

    @Test
    void connectGarminTest() throws IOException, ExecutionException, InterruptedException {
        OAuth1RequestToken requestToken = new OAuth1RequestToken(
            "123467891234789123478912345678", "123467891234789123478912345678");
        when(garminService.getRequestToken()).thenReturn(requestToken);
        MockHttpServletResponse response = new MockHttpServletResponse();
        String result = garminController.connectGarmin(response);
        Assertions.assertNotNull(response.getCookie("privateRequestToken"));
        Assertions.assertNotNull(result);
    }

    @Test
    void verifyAuthTest() throws IOException, ExecutionException, InterruptedException {
        OAuth1RequestToken requestToken = new OAuth1RequestToken(
            "123467891234789123478912345678",
            "123467891234789123478912345678");
        when(garminService.getRequestToken()).thenReturn(requestToken);
        MockHttpServletResponse response = new MockHttpServletResponse();
        garminController.connectGarmin(response);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies());
        String result = garminController.verifyAuth("",
            requestToken.getToken(), request);
        Assertions.assertEquals("redirect:/profilePage", result);
    }

    @Test
    void verifyAuthTestOauthNull() throws IOException, ExecutionException, InterruptedException {
        OAuth1RequestToken requestToken = new OAuth1RequestToken(
            "123467891234789123478912345678", "123467891234789123478912345678");
        when(garminService.getRequestToken()).thenReturn(requestToken);
        MockHttpServletResponse response = new MockHttpServletResponse();
        garminController.connectGarmin(response);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies());
        String result = garminController.verifyAuth("null",
            requestToken.getToken(), request);
        Assertions.assertEquals("redirect:/profilePage", result);
    }

    @Test
    void verifyAuthTestCookiesNull() throws IOException, ExecutionException, InterruptedException {
        OAuth1RequestToken requestToken = new OAuth1RequestToken(
            "123467891234789123478912345678", "123467891234789123478912345678");
        when(garminService.getRequestToken()).thenReturn(requestToken);
        MockHttpServletResponse response = new MockHttpServletResponse();
        garminController.connectGarmin(response);
        MockHttpServletRequest request = new MockHttpServletRequest();
        String result = garminController.verifyAuth("notnull",
            requestToken.getToken(), request);
        Assertions.assertEquals("redirect:/profilePage", result);
    }


}
