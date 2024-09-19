package com.secureauth.auth.util;

import io.github.adigunhammedolalekan.authkit.types.ThirdPartyAuthConfig;
import io.github.adigunhammedolalekan.authkit.types.ThirdPartyAuthCredential;
import io.github.adigunhammedolalekan.authkit.types.ThirdPartyAuthProviderIdentity;

public class TestDataUtil {

    public static ThirdPartyAuthConfig generateDummyConfig() {
        return ThirdPartyAuthConfig.create(
                ThirdPartyAuthCredential.of(
                        ThirdPartyAuthProviderIdentity.GOOGLE,
                        "googleClientId",
                        "googleClientSecret",
                        "https://redirect.google.uri"),
                ThirdPartyAuthCredential.of(
                        ThirdPartyAuthProviderIdentity.FACEBOOK,
                        "facebookClientId",
                        "facebookClientSecret",
                        "https://redirect.facebook.uri"),
                ThirdPartyAuthCredential.of(
                        ThirdPartyAuthProviderIdentity.APPLE,
                        "appleClientId",
                        "appleClientSecret",
                        "https://redirect.apple.uri"),
                ThirdPartyAuthCredential.of(
                        ThirdPartyAuthProviderIdentity.X,
                        "xClientId",
                        "xClientSecret",
                        "https://redirect.x.uri"));

    }
}
