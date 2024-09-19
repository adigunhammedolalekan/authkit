package io.github.adigunhammedolalekan.authkit.service;


import io.github.adigunhammedolalekan.authkit.types.PasswordResetToken;
import io.github.adigunhammedolalekan.authkit.types.ThirdPartyAuthProviderIdentity;
import io.github.adigunhammedolalekan.authkit.types.Token;
import io.github.adigunhammedolalekan.authkit.types.User;

import java.util.List;
import java.util.UUID;

public interface AuthManager {
    User signUp(String email, String password);

    Token login(String email, String password);

    User me(String token);

    void changePassword(UUID userId, String oldPassword, String newPassword);

    PasswordResetToken resetPassword(String email);

    void confirmPasswordReset(String email, String token, String newPassword);

    Token refreshToken(String token);

    String getThirdPartyAuthorizationUrl(ThirdPartyAuthProviderIdentity providerIdentity);

    Token thirdPartyAuthentication(ThirdPartyAuthProviderIdentity provider, String authorizationCode);

    void deleteUser(UUID userId);

    List<User> getUsers();
}
