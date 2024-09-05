package io.github.adigunhammedolalekan.authkit.integration.x;

import io.github.adigunhammedolalekan.authkit.integration.APIService;
import io.github.adigunhammedolalekan.authkit.integration.OauthIntegrationService;
import io.github.adigunhammedolalekan.authkit.integration.google.GoogleOauthIntegrationService;
import io.github.adigunhammedolalekan.authkit.types.AccessToken;
import io.github.adigunhammedolalekan.authkit.types.OauthUserInfo;
import io.github.adigunhammedolalekan.authkit.types.ThirdPartyAuthCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class XOauthIntegrationService implements OauthIntegrationService {

    private final ThirdPartyAuthCredential credential;
    private final APIService apiService;

    private final Logger LOGGER = LoggerFactory.getLogger(GoogleOauthIntegrationService.class);

    public XOauthIntegrationService(
            ThirdPartyAuthCredential credential,
            APIService apiService) {
        this.credential = credential;
        this.apiService = apiService;
    }

    @Override
    public AccessToken getAccessToken(String code) {
        try {
            var params = Map.of(
                    "code_verifier", code,
                    "client_id", credential.clientId(),
                    "grant_type", "authorization_code",
                    "redirect_uri", credential.redirectUri());
            return apiService.post(
                    credential.identity().getRetrieveAccessTokenUrl(),
                    params,
                    AccessToken.class,
                    "Authorization", String.format("Basic %s", credential.getXBasicAuthenticationHeader()),
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
            var params = Map.of(
                    "include_email", "true",
                    "include_entities", "false",
                    "skip_status", "true");
            var bearerToken = String.format("Bearer %s", accessToken.accessToken());
            var xUserInfo = apiService.getWithParams(
                    credential.identity().getUserInfoUrl(),
                    params,
                    XOauthUser.class,
                    "Authorization",
                    bearerToken);
            return OauthUserInfo.forX(xUserInfo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
