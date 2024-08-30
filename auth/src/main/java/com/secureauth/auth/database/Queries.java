package com.secureauth.auth.database;

import java.util.List;

public class Queries {
    private static final String CREATE_AUTH_TABLE = """
            CREATE TABLE IF NOT EXISTS user_auth (
                id UUID PRIMARY KEY,
                email VARCHAR(255) UNIQUE NOT NULL,
                password VARCHAR(255) NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                deleted_at TIMESTAMP,
                last_login_at TIMESTAMP,
                email_confirmed BOOLEAN NOT NULL DEFAULT false,
                email_confirmed_at TIMESTAMP);
            """;

    private static final String CREATE_PASSWORD_RESET_TOKENS = """
            CREATE TABLE IF NOT EXISTS password_reset_tokens (
                id UUID PRIMARY KEY,
                user_id UUID NOT NULL REFERENCES user_auth("id"),
                token VARCHAR(255) NOT NULL,
                expires_at TIMESTAMP,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                deleted_at TIMESTAMP);
            """;

    public static final String CREATE_USER = """
            INSERT INTO user_auth (id, email, password)
            VALUES (?, ?, ?);
            """;

    public static final String GET_USER_BY_ID = """
            SELECT * FROM user_auth WHERE id = ?;
            """;

    public static final String GET_USER_BY_EMAIL = """
            SELECT * FROM user_auth WHERE email = ?
            """;

    public static final String UPDATE_USER_LAST_LOGIN = """
            UPDATE user_auth SET last_login_at = ? WHERE id = ?;
            """;

    public static final String UPDATE_USER_PASSWORD = """
            UPDATE user_auth SET password = ? WHERE id = ?;
            """;

    public static final String INSERT_PASSWORD_RESET_TOKEN = """
            INSERT INTO password_reset_tokens (id, user_id, token, expires_at)
            VALUES (?, ?, ?);
            """;

    public static final String FIND_PASSWORD_RESET_TOKEN = """
            SELECT * FROM password_reset_tokens WHERE token = ?;
            """;

    public static final List<String> MIGRATIONS = List.of(
            CREATE_AUTH_TABLE,
            CREATE_PASSWORD_RESET_TOKENS);
}
