package io.github.adigunhammedolalekan.authkit.types;

import java.util.Base64;

public record ThirdPartyAuthCredential(
        ThirdPartyAuthProviderIdentity identity,

        String clientId,

        String clientSecret,

        String redirectUri
) {

    public static final String APPLE_AUTH_KEYS_URL = "https://appleid.apple.com/auth/oauth2/v2/keys";
    public static final String APPLE_ISS = "https://appleid.apple.com";

    public static ThirdPartyAuthCredential of(
            ThirdPartyAuthProviderIdentity identity,
            String clientId,
            String clientSecret,
            String redirectUri) {
        return new ThirdPartyAuthCredential(identity, clientId, clientSecret, redirectUri);
    }

    public String getXBasicAuthenticationHeader() {
        return Base64.getEncoder().encodeToString(String.format("%s:%s", this.clientId(), this.clientSecret()).getBytes());
    }
}
