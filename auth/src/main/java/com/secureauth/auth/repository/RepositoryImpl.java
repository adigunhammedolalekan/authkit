package com.secureauth.auth.repository;

import com.secureauth.auth.database.DatabaseManager;
import com.secureauth.auth.database.Queries;
import com.secureauth.auth.types.User;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class RepositoryImpl implements Repository {

    private final DatabaseManager manager;

    public RepositoryImpl(DatabaseManager manager) {
        this.manager = manager;
    }

    @Override
    public void save(User user) {
        try(var statement = manager.prepareStatement(Queries.CREATE_USER)) {
            statement.setObject(1, user.id());
            statement.setString(2, user.email());
            statement.setString(3, user.password());
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try(var statement = manager.prepareStatement(Queries.GET_USER_BY_EMAIL)) {
            statement.setString(1, email);
            var result = statement.executeQuery();
            return result.next() ? Optional.of(User.fromResultSet(result)) : Optional.empty();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Optional<User> findById(UUID id) {
        try(var statement = manager.prepareStatement(Queries.GET_USER_BY_ID)) {
            statement.setObject(1, id);
            var result = statement.executeQuery();
            return result.next() ? Optional.of(User.fromResultSet(result)) : Optional.empty();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void updateLastLogin(UUID id, LocalDateTime lastLogin) {
        try(var statement = manager.prepareStatement(Queries.UPDATE_USER_LAST_LOGIN)) {
            statement.setTimestamp(1, Timestamp.valueOf(lastLogin));
            statement.setObject(2, id);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void updatePassword(UUID id, String password) {
        try(var statement = manager.prepareStatement(Queries.UPDATE_USER_PASSWORD)) {
            statement.setString(1, password);
            statement.setObject(2, id);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
