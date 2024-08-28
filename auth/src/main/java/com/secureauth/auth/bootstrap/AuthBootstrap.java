package com.secureauth.auth.bootstrap;

import com.secureauth.auth.database.DatabaseManager;
import com.secureauth.auth.database.DatabaseMigrator;
import com.secureauth.auth.exception.AuthException;
import com.secureauth.auth.repository.RepositoryImpl;
import com.secureauth.auth.service.AuthManager;
import com.secureauth.auth.service.AuthManagerImpl;
import com.secureauth.auth.service.JwtTokenService;
import com.secureauth.auth.types.AuthManagerConfig;
import com.secureauth.auth.types.DatabaseConfig;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AuthBootstrap {

    private final AuthManagerConfig config;

    private AuthBootstrap(AuthManagerConfig config) {
        if (config == null) {
            throw new NullPointerException("config is null");
        }
        this.config = config;
    }

    public static AuthBootstrap config(AuthManagerConfig config) {
        return new AuthBootstrap(config);
    }

    public AuthManager create() {
        try {
            var connection = getConnection(config.databaseConfig());
            var databaseManager = new DatabaseManager(connection);
            new DatabaseMigrator(databaseManager)
                    .migrate();
            var repository = new RepositoryImpl(databaseManager);
            var tokenService = new JwtTokenService(config.tokenConfig());
            return new AuthManagerImpl(repository, tokenService);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | SQLException e) {
            throw new AuthException("Error creating AuthManager " + e.getMessage());
        }
    }

    private Connection getConnection(DatabaseConfig config) throws SQLException {
        return DriverManager.getConnection(
                config.dsn(),
                config.username(),
                config.password());
    }
}
