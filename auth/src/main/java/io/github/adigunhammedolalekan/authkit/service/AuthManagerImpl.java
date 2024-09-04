package io.github.adigunhammedolalekan.authkit.service;

import io.github.adigunhammedolalekan.authkit.exception.AuthException;
import io.github.adigunhammedolalekan.authkit.helper.Dates;
import io.github.adigunhammedolalekan.authkit.helper.Emails;
import io.github.adigunhammedolalekan.authkit.helper.PasswordValidator;
import io.github.adigunhammedolalekan.authkit.helper.RandomToken;
import io.github.adigunhammedolalekan.authkit.repository.Repository;
import io.github.adigunhammedolalekan.authkit.types.PasswordResetToken;
import io.github.adigunhammedolalekan.authkit.types.ThirdPartyAuthProviderIdentity;
import io.github.adigunhammedolalekan.authkit.types.Token;
import io.github.adigunhammedolalekan.authkit.types.User;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import static java.util.Objects.nonNull;

public class AuthManagerImpl implements AuthManager {

    private final Repository repository;
    private final TokenService tokenService;
    private final ThirdPartyAuthProvider authProvider;

    private static final int PASSWORD_RESET_TOKEN_EXPIRATION_IN_MINS = 15;

    public AuthManagerImpl(
            Repository repository,
            TokenService tokenService,
            ThirdPartyAuthProvider authProvider) {
        this.repository = repository;
        this.tokenService = tokenService;
        this.authProvider = authProvider;
    }

    @Override
    public User signUp(String email, String password) {
        if (!Emails.isValid(email)) {
            throw new AuthException(Emails.INVALID_EMAIL_ERROR);
        }

        var existingUser = repository.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new AuthException(String.format("User with email %s already exists", email));
        }

        if (!PasswordValidator.isValidPassword(password)) {
            throw new AuthException(PasswordValidator.PASSWORD_VALIDATION_ERROR_MESSAGE);
        }

        var user = User.of(email, password);
        repository.saveUser(user);
        return user;
    }

    @Override
    public Token login(String email, String password) {
        var user = repository.findByEmail(email)
                .orElseThrow(() -> new AuthException("Invalid email and password combination"));

        if (!BCrypt.checkpw(password, user.password())) {
            throw new AuthException("Invalid email and password combination");
        }

        repository.updateLastLogin(user.id(), LocalDateTime.now());

        return tokenService.generateToken(user);
    }

    @Override
    public User me(String token) {
        var user = tokenService.validateToken(token);
        return repository.findById(user.id())
                .orElseThrow(() -> new AuthException("Invalid token"));
    }

    @Override
    public void changePassword(UUID userId, String oldPassword, String newPassword) {
        var user = repository.findById(userId)
                .orElseThrow(() -> new AuthException(String.format("User with id %s not found", userId)));

        if (!BCrypt.checkpw(oldPassword, user.password())) {
            throw new AuthException("old password is invalid or incorrect");
        }

        var hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        repository.updatePassword(user.id(), hashedPassword);
    }

    @Override
    public Token refreshToken(String token) {
        var validated = tokenService.validateToken(token);
        var user = repository.findById(validated.id())
                .orElseThrow(() -> new AuthException("Invalid token"));
        return tokenService.generateToken(user);
    }

    @Override
    public PasswordResetToken resetPassword(String email) {
        var user = repository.findByEmail(email)
                .orElseThrow(() -> new AuthException(String.format("User with email %s not found", email)));

        var resetToken = PasswordResetToken.of(user.id(), RandomToken.generate(), Dates.getDateForMinutes(PASSWORD_RESET_TOKEN_EXPIRATION_IN_MINS));
        repository.savePasswordResetToken(resetToken);

        // TODO: send email
        return resetToken;
    }

    @Override
    public void confirmPasswordReset(String email, String token, String newPassword) {
        var user = repository.findByEmail(email)
                .orElseThrow(() -> new AuthException(String.format("User with email %s not found", email)));

        var resetToken = repository.findPasswordResetToken(token)
                .orElseThrow(() -> new AuthException("password reset token is invalid"));

        if (resetToken.expiresAt().after(new Date())) {
            throw new AuthException("password reset token is expired");
        }

        var newPasswordHash = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        repository.updatePassword(user.id(), newPasswordHash);
    }

    @Override
    public Token thirdPartyAuthentication(ThirdPartyAuthProviderIdentity provider, String authorizationCode) {
        var integrationService = this.authProvider.getIntegrationService(provider);
        var accessToken = integrationService.getAccessToken(authorizationCode);
        var userInfo = integrationService.getUser(accessToken.accessToken());

        var user = repository.findByEmail(userInfo.email());
        if (user.isPresent() &&
                nonNull(user.get().password()) &&
                !user.get().password().isEmpty()) {
            throw new AuthException(String.format("User with email %s already sign up with password", userInfo.email()));
        }

        if (user.isEmpty()) {
            var newUser = User.of(provider, userInfo);
            repository.saveUser(newUser);
            return tokenService.generateToken(newUser);
        }

        var attributes = user.get().getAttributes();
        attributes.update(provider, userInfo);

        repository.updateUserMetadata(user.get().id(), attributes.toString());
        return tokenService.generateToken(user.get());
    }
}
