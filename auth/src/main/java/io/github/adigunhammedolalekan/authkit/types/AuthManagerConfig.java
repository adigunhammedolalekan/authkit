package io.github.adigunhammedolalekan.authkit.types;

public record AuthManagerConfig(
        DatabaseConfig databaseConfig,

        TokenConfig tokenConfig,

        ThirdPartyAuthConfig thirdPartyAuthConfig
) {}
