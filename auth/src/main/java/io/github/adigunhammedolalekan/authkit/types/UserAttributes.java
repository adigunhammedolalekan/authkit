package io.github.adigunhammedolalekan.authkit.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

import static java.util.Objects.nonNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserAttributes(
        Map<ThirdPartyAuthProviderIdentity, OauthUserInfo> authUserInfo
) {

    public static UserAttributes empty() {
        return new UserAttributes(Map.of());
    }

    public boolean isEmpty() {
        return nonNull(authUserInfo) && authUserInfo.isEmpty();
    }

    public boolean hasInfoForProvider(ThirdPartyAuthProviderIdentity providerIdentity) {
        return nonNull(authUserInfo) && authUserInfo.containsKey(providerIdentity);
    }

    public void update(ThirdPartyAuthProviderIdentity providerIdentity, OauthUserInfo oauthUserInfo) {
        authUserInfo.put(providerIdentity, oauthUserInfo);
    }
}
