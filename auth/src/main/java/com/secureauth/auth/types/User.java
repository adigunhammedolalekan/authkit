package com.secureauth.auth.types;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

public record User(
        UUID id,

        String email,

        String password,

        Date createdAt,

        Date updatedAt,

        Date deletedAt,

        Date lastLoginAt
) {

    public static User of(String email, String password) {
        return new User(UUID.randomUUID(), email, BCrypt.hashpw(password, BCrypt.gensalt()), null, null, null, null);
    }

    public static User fromResultSet(ResultSet result) throws SQLException {
        return new User(
                UUID.fromString(result.getString("id")),
                result.getString("email"),
                result.getString("password"),
                result.getDate("created_at"),
                result.getDate("updated_at"),
                result.getDate("deleted_at"),
                result.getDate("last_login_at"));
    }

    public static User of(UUID id) {
        return new User(id, null, null, null, null, null, null);
    }
}
