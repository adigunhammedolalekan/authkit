package io.github.adigunhammedolalekan.authkit.integration.google;

import io.github.adigunhammedolalekan.authkit.integration.APIService;
import io.github.adigunhammedolalekan.authkit.integration.OauthIntegrationService;
import io.github.adigunhammedolalekan.authkit.types.AccessToken;
import io.github.adigunhammedolalekan.authkit.types.OauthUserInfo;
import io.github.adigunhammedolalekan.authkit.types.ThirdPartyAuthCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class GoogleOauthIntegrationService implements OauthIntegrationService {

    private final APIService apiService;
    private final ThirdPartyAuthCredential credential;

    private final Logger LOGGER = LoggerFactory.getLogger(GoogleOauthIntegrationService.class);

    public GoogleOauthIntegrationService(
            ThirdPartyAuthCredential credential,
            APIService apiService) {
        this.credential = credential;
        this.apiService = apiService;
    }

    @Override
    public AccessToken getAccessToken(String code) {
        var body = Map.of(
                "code", code,
                "client_id", credential.clientId(),
                "client_secret", credential.clientSecret(),
                "grant_type", "authorization_code",
                "redirect_uri", credential.redirectUri());

        try {
            return apiService.post(
                    credential.identity().getRetrieveAccessTokenUrl(),
                    body,
                    AccessToken.class,
                    "Content-Type",
                    "application/x-www-form-urlencoded");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public OauthUserInfo getUser(AccessToken accessToken) {
        try {
            var params = Map.of("access_token", accessToken.accessToken());
            var info = apiService.getWithParams(
                    credential.identity().getUserInfoUrl(),
                    params, GoogleOauthUser.class);
            return OauthUserInfo.forGoogle(info);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
