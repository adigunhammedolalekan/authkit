package io.github.adigunhammedolalekan.authkit.service;

import io.github.adigunhammedolalekan.authkit.integration.APIService;
import io.github.adigunhammedolalekan.authkit.integration.OauthIntegrationService;
import io.github.adigunhammedolalekan.authkit.integration.google.GoogleOauthIntegrationService;
import io.github.adigunhammedolalekan.authkit.types.ThirdPartyAuthConfig;
import io.github.adigunhammedolalekan.authkit.types.ThirdPartyAuthProviderIdentity;

public class ThirdPartyAuthProvider {

    private final APIService apiService;
    private final ThirdPartyAuthConfig config;

    public ThirdPartyAuthProvider(
            APIService apiService,
            ThirdPartyAuthConfig config) {
        this.config = config;
        this.apiService = apiService;
    }

    public OauthIntegrationService getIntegrationService(
            ThirdPartyAuthProviderIdentity providerIdentity) {
        return switch (providerIdentity) {
            case GOOGLE -> new GoogleOauthIntegrationService(config.google(), apiService);
            case APPLE, FACEBOOK, TWITTER -> throw new UnsupportedOperationException();
        };
    }
}
