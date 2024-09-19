package io.github.adigunhammedolalekan.authkitsample.service;

import io.github.adigunhammedolalekan.authkit.helper.RandomToken;
import io.github.adigunhammedolalekan.authkit.service.AuthManager;
import io.github.adigunhammedolalekan.authkit.types.ThirdPartyAuthConfig;
import io.github.adigunhammedolalekan.authkit.types.ThirdPartyAuthProviderIdentity;
import io.github.adigunhammedolalekan.authkit.types.Token;
import io.github.adigunhammedolalekan.authkit.types.User;
import io.github.adigunhammedolalekan.authkitsample.types.AuthenticateAccountRequest;
import io.github.adigunhammedolalekan.authkitsample.types.CreateAccountRequest;

public class AuthService {

    private final AuthManager authManager;
    private final ThirdPartyAuthConfig thirdPartyAuthConfig;

    public AuthService(AuthManager authManager, ThirdPartyAuthConfig thirdPartyAuthConfig) {
        this.authManager = authManager;
        this.thirdPartyAuthConfig = thirdPartyAuthConfig;
    }

    public User createAccount(CreateAccountRequest request) {
        return authManager.signUp(request.email(), request.password());
    }

    public Token login(AuthenticateAccountRequest request) {
        return authManager.login(request.email(), request.password());
    }

    public User getUser(String token) {
        return authManager.me(token);
    }

    public String thirdPartyLogin(String provider) {
        var providerIdentity = ThirdPartyAuthProviderIdentity.valueOf(provider.toUpperCase());
        return authManager.getThirdPartyAuthorizationUrl(providerIdentity);
    }

    public Token completeThirdPartyLogin(String provider, String code) {
        return authManager.thirdPartyAuthentication(ThirdPartyAuthProviderIdentity.valueOf(provider.toUpperCase()), code);
    }

    public void deleteAccount(String token) {
        var account = authManager.me(token);
        authManager.deleteUser(account.id());
    }
}
