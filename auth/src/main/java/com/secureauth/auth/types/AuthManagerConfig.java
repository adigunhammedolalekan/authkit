package com.secureauth.auth.types;

public record AuthManagerConfig(
        DatabaseConfig databaseConfig,

        TokenConfig tokenConfig
) {
}
