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

    public static final List<String> MIGRATIONS = List.of(
            CREATE_AUTH_TABLE);
}
