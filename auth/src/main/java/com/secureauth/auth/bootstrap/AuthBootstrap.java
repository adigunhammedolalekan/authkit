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
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

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
            var dataSource = getDataSource(config.databaseConfig());
            var databaseManager = new DatabaseManager(dataSource);
            new DatabaseMigrator(databaseManager)
                    .migrate();
            var repository = new RepositoryImpl(databaseManager);
            var tokenService = new JwtTokenService(config.tokenConfig());
            return new AuthManagerImpl(repository, tokenService);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AuthException("Error creating AuthManager " + e.getMessage());
        }
    }

    private HikariDataSource getDataSource(DatabaseConfig databaseConfig) {
        var config = new HikariConfig();
        config.setJdbcUrl(databaseConfig.dsn());
        config.setUsername(databaseConfig.username());
        config.setPassword(databaseConfig.password());
        config.setMaximumPoolSize(databaseConfig.connectionPoolSize());

        return new HikariDataSource(config);
    }
}
