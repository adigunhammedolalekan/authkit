package io.github.adigunhammedolalekan.authkit.integration;

import io.github.adigunhammedolalekan.authkit.types.AccessToken;
import io.github.adigunhammedolalekan.authkit.types.OauthUserInfo;

public interface OauthIntegrationService {
    AccessToken getAccessToken(String code);

    OauthUserInfo getUser(AccessToken accessToken);
}
