package io.github.adigunhammedolalekan.authkit.repository;

import io.github.adigunhammedolalekan.authkit.database.DatabaseManager;
import io.github.adigunhammedolalekan.authkit.database.Queries;
import io.github.adigunhammedolalekan.authkit.types.PasswordResetToken;
import io.github.adigunhammedolalekan.authkit.types.User;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RepositoryImpl implements Repository {

    private final DatabaseManager manager;

    public RepositoryImpl(DatabaseManager manager) {
        this.manager = manager;
    }

    @Override
    public void saveUser(User user) {
        try(var connection = manager.getConnection()) {
            try(var statement = connection.prepareStatement(Queries.CREATE_USER)) {
                statement.setObject(1, user.id());
                statement.setString(2, user.email());
                statement.setString(3, user.password());
                statement.setString(4, user.authProvider());
                statement.setString(5, user.getAttributesAsString());
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try(var connection = manager.getConnection()) {
            try(var statement = connection.prepareStatement(Queries.GET_USER_BY_EMAIL)) {
                statement.setString(1, email);
                var result = statement.executeQuery();
                return result.next() ? Optional.of(User.fromResultSet(result)) : Optional.empty();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Optional<User> findById(UUID id) {
        try(var connection = manager.getConnection()) {
            try(var statement = connection.prepareStatement(Queries.GET_USER_BY_ID)) {
                statement.setObject(1, id);
                var result = statement.executeQuery();
                return result.next() ? Optional.of(User.fromResultSet(result)) : Optional.empty();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void updateLastLogin(UUID id, LocalDateTime lastLogin) {
        try(var connection = manager.getConnection()) {
            try(var statement = connection.prepareStatement(Queries.UPDATE_USER_LAST_LOGIN)) {
                statement.setTimestamp(1, Timestamp.valueOf(lastLogin));
                statement.setObject(2, id);
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void updatePassword(UUID id, String password) {
        try(var connection = manager.getConnection()) {
            try(var statement = connection.prepareStatement(Queries.UPDATE_USER_PASSWORD)) {
                statement.setString(1, password);
                statement.setObject(2, id);
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void savePasswordResetToken(PasswordResetToken token) {
        try(var connection = manager.getConnection()) {
            try(var statement = connection.prepareStatement(Queries.INSERT_PASSWORD_RESET_TOKEN)) {
                statement.setObject(1, token.id());
                statement.setObject(2, token.userId());
                statement.setString(3, token.token());
                statement.setTimestamp(4, new Timestamp(token.expiresAt().getTime()));
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Optional<PasswordResetToken> findPasswordResetToken(String token) {
        try(var connection = manager.getConnection()) {
            try (var statement = connection.prepareStatement(Queries.FIND_PASSWORD_RESET_TOKEN)) {
                statement.setString(1, token);
                var result = statement.executeQuery();
                return result.next() ? Optional.of(PasswordResetToken.fromResultSet(result)) : Optional.empty();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void updateUserMetadata(UUID id, String metadata) {
        try(var connection = manager.getConnection()) {
            try(var statement = connection.prepareStatement(Queries.UPDATE_USER_META)) {
                statement.setString(1, metadata);
                statement.setObject(2, id);
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void deleteUser(UUID id) {
        try(var connection = manager.getConnection()) {
            try(var statement = connection.prepareStatement(Queries.DELETE_USER)) {
                statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                statement.setObject(2, id);
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<User> getUsers() {
        var users = new ArrayList<User>();
        try(var connection = manager.getConnection()) {
            try (var statement = connection.prepareStatement(Queries.FIND_ALL_USERS)) {
                var result = statement.executeQuery();
                while (result.next()) {
                    users.add(User.fromResultSet(result));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return users;
    }
}
