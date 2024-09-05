package io.github.adigunhammedolalekan.authkit.types;

import static java.util.Objects.nonNull;

public record AuthManagerConfig(
        DatabaseConfig databaseConfig,

        TokenConfig tokenConfig,

        ThirdPartyAuthConfig thirdPartyAuthConfig
) {

    public boolean isValid() {
        if (thirdPartyAuthConfig != null && thirdPartyAuthConfig.credentials().isEmpty()) {
            return false;
        }

        return nonNull(databaseConfig) && databaseConfig.isValid()
                && nonNull(tokenConfig) && tokenConfig.isValid();
    }
}
