package io.github.adigunhammedolalekan.authkitsample.config;

import io.github.adigunhammedolalekan.authkit.bootstrap.AuthBootstrap;
import io.github.adigunhammedolalekan.authkit.service.AuthManager;
import io.github.adigunhammedolalekan.authkit.types.*;
import io.github.adigunhammedolalekan.authkitsample.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {

    @Bean
    public TokenConfig tokenConfig(
            @Value("${authkit.token.privateKey}") String tokenPrivateKey,
            @Value("${authkit.token.publicKey}") String tokenPublicKey,
            @Value("${authkit.token.issuer}") String issuer,
            @Value("${authkit.token.expiration}") long expiration,
            @Value("${authkit.token.refreshTokenExpiration}") long refreshTokenExpiration) {
        return new TokenConfig(issuer, expiration, refreshTokenExpiration, tokenPublicKey, tokenPrivateKey);
    }

    @Bean
    public DatabaseConfig databaseConfig(
            @Value("${server.datasource.url}") String dsn,
            @Value("${server.datasource.username}") String username,
            @Value("${server.datasource.password}") String password) {
        return new DatabaseConfig(dsn, username, password, 10);
    }

    // Optional: Only needed if you want to enable ThirdPartyAuth
    @Bean
    public ThirdPartyAuthConfig thirdPartyAuthConfig(
            @Value("${authkit.thirdPartyAuth.facebook.clientId}") String facebookClientId,
            @Value("${authkit.thirdPartyAuth.facebook.clientSecret}") String facebookClientSecret,
            @Value("${authkit.thirdPartyAuth.facebook.redirectUri}") String facebookRedirectUri,
            @Value("${authkit.thirdPartyAuth.google.clientId}") String googleClientId,
            @Value("${authkit.thirdPartyAuth.google.clientSecret}") String googleClientSecret,
            @Value("${authkit.thirdPartyAuth.google.redirectUri}") String googleRedirectUri,
            @Value("${authkit.thirdPartyAuth.x.clientId}") String xClientId,
            @Value("${authkit.thirdPartyAuth.x.clientSecret}") String xClientSecret,
            @Value("${authkit.thirdPartyAuth.x.redirectUri}") String xRedirectUri) {
        return ThirdPartyAuthConfig.create(
                ThirdPartyAuthCredential.of(
                        ThirdPartyAuthProviderIdentity.FACEBOOK,
                        facebookClientId,
                        facebookClientSecret,
                        facebookRedirectUri),
                ThirdPartyAuthCredential.of(
                        ThirdPartyAuthProviderIdentity.GOOGLE,
                        googleClientId,
                        googleClientSecret,
                        googleRedirectUri),
                ThirdPartyAuthCredential.of(
                        ThirdPartyAuthProviderIdentity.X,
                        xClientId,
                        xClientSecret,
                        xRedirectUri
                ));
    }

    @Bean
    public AuthManagerConfig authManagerConfig(
            DatabaseConfig databaseConfig,
            TokenConfig tokenConfig,
            ThirdPartyAuthConfig thirdPartyAuthConfig
    ) {
        return new AuthManagerConfig(databaseConfig, tokenConfig, thirdPartyAuthConfig);
    }

    @Bean
    public AuthManager authBootstrap(AuthManagerConfig authManagerConfig) {
        return AuthBootstrap.config(authManagerConfig).create();
    }

    @Bean
    public AuthService authService(AuthManager authManager, ThirdPartyAuthConfig thirdPartyAuthConfig) {
        return new AuthService(authManager, thirdPartyAuthConfig);
    }
}
