package io.github.adigunhammedolalekan.authkit.types;

import io.github.adigunhammedolalekan.authkit.helper.Json;

import java.util.Map;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

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

    @Override
    public String toString() {
        if (isNull(authUserInfo)) {
            return "{}";
        }
        try {
            return Json.create(authUserInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
