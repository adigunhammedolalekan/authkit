package io.github.adigunhammedolalekan.authkit.service;

import io.github.adigunhammedolalekan.authkit.integration.APIService;
import io.github.adigunhammedolalekan.authkit.integration.OauthIntegrationService;
import io.github.adigunhammedolalekan.authkit.integration.apple.AppleOauthIntegrationService;
import io.github.adigunhammedolalekan.authkit.integration.facebook.FacebookOauthIntegrationService;
import io.github.adigunhammedolalekan.authkit.integration.google.GoogleOauthIntegrationService;
import io.github.adigunhammedolalekan.authkit.integration.x.XOauthIntegrationService;
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
        var credential = config.getCredential(providerIdentity);
        return switch (providerIdentity) {
            case GOOGLE -> new GoogleOauthIntegrationService(credential, apiService);
            case TWITTER -> new XOauthIntegrationService(credential, apiService);
            case FACEBOOK -> new FacebookOauthIntegrationService(credential, apiService);
            case APPLE -> new AppleOauthIntegrationService(credential, apiService);
        };
    }
}
