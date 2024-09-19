package io.github.adigunhammedolalekan.authkit.database;

import java.util.List;

public class Queries {
    private static final String CREATE_AUTH_TABLE = """
            CREATE TABLE IF NOT EXISTS user_auth (
                id UUID PRIMARY KEY,
                email VARCHAR(255) NOT NULL,
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

    private static final String ADD_PROVIDER_COLUMN_USER_TABLE = """
            ALTER TABLE user_auth ADD COLUMN IF NOT EXISTS auth_provider VARCHAR(255);
            ALTER TABLE user_auth ADD COLUMN IF NOT EXISTS attributes jsonb; 
            """;

    public static final String CREATE_USER = """
            INSERT INTO user_auth (id, email, password, auth_provider, attributes)
            VALUES (?, ?, ?, ?, ?::jsonb);
            """;

    public static final String GET_USER_BY_ID = """
            SELECT * FROM user_auth WHERE id = ? AND deleted_at IS NULL;
            """;

    public static final String GET_USER_BY_EMAIL = """
            SELECT * FROM user_auth WHERE email = ? AND deleted_at IS NULL;
            """;

    public static final String UPDATE_USER_LAST_LOGIN = """
            UPDATE user_auth SET last_login_at = ? WHERE id = ?;
            """;

    public static final String UPDATE_USER_PASSWORD = """
            UPDATE user_auth SET password = ? WHERE id = ?;
            """;

    public static final String INSERT_PASSWORD_RESET_TOKEN = """
            INSERT INTO password_reset_tokens (id, user_id, token, expires_at)
            VALUES (?, ?, ?, ?);
            """;

    public static final String FIND_PASSWORD_RESET_TOKEN = """
            SELECT * FROM password_reset_tokens WHERE token = ? AND deleted_at IS NULL;
            """;

    public static final String UPDATE_USER_META = """
            UPDATE user_auth SET attributes = ?::jsonb WHERE id = ?;
            """;

    public static final String UPDATE_USER_AUTH_PROVIDER = """
            UPDATE user_auth SET auth_provider = ? WHERE id = ?;
            """;

    public static final String DELETE_USER = """
            UPDATE user_auth SET deleted_at = ? WHERE id = ?;
            """;

    public static final String FIND_ALL_USERS = """
            SELECT * FROM user_auth WHERE deleted_at IS NULL;
            """;

    public static final List<String> MIGRATIONS = List.of(
            CREATE_AUTH_TABLE,
            CREATE_PASSWORD_RESET_TOKENS,
            ADD_PROVIDER_COLUMN_USER_TABLE);
}
