package io.github.adigunhammedolalekan.authkit.bootstrap;

import io.github.adigunhammedolalekan.authkit.database.DatabaseManager;
import io.github.adigunhammedolalekan.authkit.database.DatabaseMigrator;
import io.github.adigunhammedolalekan.authkit.exception.AuthException;
import io.github.adigunhammedolalekan.authkit.integration.APIServiceImpl;
import io.github.adigunhammedolalekan.authkit.repository.RepositoryImpl;
import io.github.adigunhammedolalekan.authkit.service.AuthManager;
import io.github.adigunhammedolalekan.authkit.service.AuthManagerImpl;
import io.github.adigunhammedolalekan.authkit.service.JwtTokenService;
import io.github.adigunhammedolalekan.authkit.service.ThirdPartyAuthProvider;
import io.github.adigunhammedolalekan.authkit.types.AuthManagerConfig;
import io.github.adigunhammedolalekan.authkit.types.DatabaseConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.adigunhammedolalekan.authkit.types.ThirdPartyAuthConfig;

import java.net.http.HttpClient;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

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
            var authProvider = getAuthProvider(config.thirdPartyAuthConfig());
            var databaseManager = new DatabaseManager(dataSource);
            new DatabaseMigrator(databaseManager)
                    .migrate();
            var repository = new RepositoryImpl(databaseManager);
            var tokenService = new JwtTokenService(config.tokenConfig());
            return new AuthManagerImpl(repository, tokenService, authProvider);
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

    private ThirdPartyAuthProvider getAuthProvider(ThirdPartyAuthConfig thirdPartyAuthConfig) {
        var apiService = new APIServiceImpl(HttpClient.newHttpClient());
        return new ThirdPartyAuthProvider(apiService, thirdPartyAuthConfig);
    }
}
