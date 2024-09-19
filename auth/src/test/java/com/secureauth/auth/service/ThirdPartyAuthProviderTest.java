package com.secureauth.auth.service;

import com.secureauth.auth.util.TestDataUtil;
import io.github.adigunhammedolalekan.authkit.integration.APIService;
import io.github.adigunhammedolalekan.authkit.integration.apple.AppleOauthIntegrationService;
import io.github.adigunhammedolalekan.authkit.integration.facebook.FacebookOauthIntegrationService;
import io.github.adigunhammedolalekan.authkit.integration.google.GoogleOauthIntegrationService;
import io.github.adigunhammedolalekan.authkit.integration.x.XOauthIntegrationService;
import io.github.adigunhammedolalekan.authkit.service.ThirdPartyAuthProvider;
import io.github.adigunhammedolalekan.authkit.types.ThirdPartyAuthConfig;
import io.github.adigunhammedolalekan.authkit.types.ThirdPartyAuthCredential;
import io.github.adigunhammedolalekan.authkit.types.ThirdPartyAuthProviderIdentity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.secureauth.auth.util.TestDataUtil.generateDummyConfig;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ThirdPartyAuthProviderTest {

    @Mock
    private APIService apiService;

    private final ThirdPartyAuthConfig config = generateDummyConfig();

    private ThirdPartyAuthProvider authProvider;

    @BeforeEach
    protected void setUp() {
        authProvider = new ThirdPartyAuthProvider(apiService, config);
    }

    @Test
    void get_integration_service_test() {
        var google = authProvider.getIntegrationService(ThirdPartyAuthProviderIdentity.GOOGLE);
        var facebook = authProvider.getIntegrationService(ThirdPartyAuthProviderIdentity.FACEBOOK);
        var x = authProvider.getIntegrationService(ThirdPartyAuthProviderIdentity.X);
        var apple = authProvider.getIntegrationService(ThirdPartyAuthProviderIdentity.APPLE);

        assertInstanceOf(GoogleOauthIntegrationService.class, google);
        assertInstanceOf(FacebookOauthIntegrationService.class, facebook);
        assertInstanceOf(AppleOauthIntegrationService.class, apple);
        assertInstanceOf(XOauthIntegrationService.class, x);
    }

    @Test
    void get_authorization_url_test() {
        var googleOauthUrl = authProvider.getAuthorizationUrl(ThirdPartyAuthProviderIdentity.GOOGLE);
        var facebookOauthUrl = authProvider.getAuthorizationUrl(ThirdPartyAuthProviderIdentity.FACEBOOK);
        var appleOauthUrl = authProvider.getAuthorizationUrl(ThirdPartyAuthProviderIdentity.APPLE);
        var xOauthUrl = authProvider.getAuthorizationUrl(ThirdPartyAuthProviderIdentity.X);

        assertEquals(googleOauthUrl, "https://accounts.google.com/o/oauth2/auth?client_id=googleClientId&redirect_uri=https://redirect.google.uri&scope=https://www.googleapis.com/auth/userinfo.email+https://www.googleapis.com/auth/userinfo.profile&response_type=code");
        assertEquals(facebookOauthUrl, "");
        assertEquals(appleOauthUrl, "");
        assertEquals(xOauthUrl, "");
    }
}
