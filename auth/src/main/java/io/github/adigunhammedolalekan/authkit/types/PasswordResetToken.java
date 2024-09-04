package io.github.adigunhammedolalekan.authkit.types;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

public record PasswordResetToken(
        UUID id,

        UUID userId,

        String token,

        Date expiresAt,

        Date createdAt,

        Date updatedAt,

        Date deletedAt
) {

    public static PasswordResetToken of(UUID userId, String token, Date expiresAt) {
        return new PasswordResetToken(
                UUID.randomUUID(),
                userId,
                token,
                expiresAt,
                null,
                null,
                null);
    }

    public static PasswordResetToken fromResultSet(ResultSet result) throws SQLException {
        return new PasswordResetToken(
                (UUID) result.getObject("id"),
                (UUID) result.getObject("user_id"),
                result.getString("token"),
                result.getDate("expires_at"),
                result.getDate("created_at"),
                result.getDate("updated_at"),
                result.getDate("deleted_at"));
    }
}
