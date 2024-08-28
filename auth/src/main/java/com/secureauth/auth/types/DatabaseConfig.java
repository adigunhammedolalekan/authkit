package com.secureauth.auth.types;

public record DatabaseConfig(
        String dsn,

        String username,

        String password
) {}
