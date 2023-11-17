package nz.ac.canterbury.seng302.tab.api;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * GarminAPI
 */
@Service
public class GarminApi extends DefaultApi10a {

    /**
     * Step One - Get Unauthorized Request Token
     */
    private static final String REQUEST_TOKEN_RESOURCE =
        "https://connectapi.garmin.com/oauth-service/oauth/request_token";
    /**
     * Step Three - Get Access Token
     */
    private static final String ACCESS_TOKEN_RESOURCE =
        "https://connectapi.garmin.com/oauth-service/oauth/access_token";
    @Value("${current_url}")
    private static String url;
    /**
     * Step Two - Redirect User to Garmin and authorize request token
     * oauth_callback variable is the link the user will be redirected to after authorization.
     */
    private String authorizeLink = "https://connect.garmin"
        + ".com/oauthConfirm?oauth_callback=" + url + "/verifyAuth";

    public static GarminApi instance() {
        return InstanceHolder.INSTANCE;
    }

    public static void setUrl(String url) {
        GarminApi.url = url;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return ACCESS_TOKEN_RESOURCE;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return REQUEST_TOKEN_RESOURCE;
    }

    @Override
    public String getAuthorizationBaseUrl() {
        if (GarminApi.url != null) {
            authorizeLink = "https://connect.garmin"
                + ".com/oauthConfirm?oauth_callback=" + url + "/verifyAuth";
        }
        return authorizeLink;
    }

    private static class InstanceHolder {
        private static final GarminApi INSTANCE = new GarminApi();
    }
}
