package io.github.adigunhammedolalekan.authkit.integration.google;

import io.github.adigunhammedolalekan.authkit.integration.APIService;
import io.github.adigunhammedolalekan.authkit.integration.OauthIntegrationService;
import io.github.adigunhammedolalekan.authkit.types.AccessToken;
import io.github.adigunhammedolalekan.authkit.types.OauthUserInfo;
import io.github.adigunhammedolalekan.authkit.types.ThirdPartyAuthConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class GoogleOauthIntegrationService implements OauthIntegrationService {

    private static final String GOOGLE_OAUTH_USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo";
    private static final String GOOGLE_OAUTH_ACCESS_TOKEN_URL = "https://accounts.google.com/o/oauth2/token";

    private final APIService apiService;
    private final ThirdPartyAuthConfig.Google config;

    private final Logger LOGGER = LoggerFactory.getLogger(GoogleOauthIntegrationService.class);

    public GoogleOauthIntegrationService(
            ThirdPartyAuthConfig.Google config,
            APIService apiService) {
        this.config = config;
        this.apiService = apiService;
    }

    @Override
    public AccessToken getAccessToken(String code) {
        var body = Map.of(
                "code", code,
                "client_id", config.clientId(),
                "client_secret", config.clientSecret(),
                "grant_type", "authorization_code",
                "redirect_uri", config.redirectURI());

        try {
            return apiService.post(GOOGLE_OAUTH_ACCESS_TOKEN_URL, body, AccessToken.class, "Content-Type", "application/x-www-form-urlencoded");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public OauthUserInfo getUser(String accessToken) {
        try {
            var params = Map.of("access_token", accessToken);
            var info = apiService.getWithParams(
                    GOOGLE_OAUTH_USER_INFO_URL,
                    params, GoogleOauthUser.class);
            return OauthUserInfo.forGoogle(info);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
