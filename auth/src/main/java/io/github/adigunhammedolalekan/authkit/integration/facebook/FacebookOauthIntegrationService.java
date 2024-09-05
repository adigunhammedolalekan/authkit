package io.github.adigunhammedolalekan.authkit.integration.facebook;

import io.github.adigunhammedolalekan.authkit.integration.APIService;
import io.github.adigunhammedolalekan.authkit.integration.OauthIntegrationService;
import io.github.adigunhammedolalekan.authkit.integration.google.GoogleOauthIntegrationService;
import io.github.adigunhammedolalekan.authkit.types.AccessToken;
import io.github.adigunhammedolalekan.authkit.types.OauthUserInfo;
import io.github.adigunhammedolalekan.authkit.types.ThirdPartyAuthCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class FacebookOauthIntegrationService implements OauthIntegrationService {

    private final ThirdPartyAuthCredential credential;
    private final APIService apiService;

    private final Logger LOGGER = LoggerFactory.getLogger(GoogleOauthIntegrationService.class);

    public FacebookOauthIntegrationService(ThirdPartyAuthCredential credential,
                                           APIService apiService) {
        this.credential = credential;
        this.apiService = apiService;
    }

    @Override
    public AccessToken getAccessToken(String code) {
        try {
            var queryParams = Map.of(
                    "code", code,
                    "client_id", credential.clientId(),
                    "client_secret", credential.clientSecret(),
                    "redirect_uri", credential.redirectUri());
            return apiService.getWithParams(
                    credential.identity().getRetrieveAccessTokenUrl(),
                    queryParams,
                    AccessToken.class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public OauthUserInfo getUser(AccessToken accessToken) {
        try {
            var fields = "id,first_name,last_name,email";
            var queryParams = Map.of(
                    "access_token", accessToken.accessToken(),
                    "fields", fields);
            var response = apiService.getWithParams(
                    credential.identity().getUserInfoUrl(),
                    queryParams,
                    FacebookOauthUser.class);
            return OauthUserInfo.forFacebook(response);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
