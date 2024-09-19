package io.github.adigunhammedolalekan.authkit.types;

import java.util.List;

public record ThirdPartyAuthConfig(
        List<ThirdPartyAuthCredential> credentials
) {

    public ThirdPartyAuthCredential getCredential(ThirdPartyAuthProviderIdentity providerIdentity) {
        return credentials.stream()
                .filter(next -> next.identity() == providerIdentity)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("auth credential not found for " + providerIdentity));
    }

    public static ThirdPartyAuthConfig create(ThirdPartyAuthCredential...credentials) {
        return new ThirdPartyAuthConfig(List.of(credentials));
    }
}
