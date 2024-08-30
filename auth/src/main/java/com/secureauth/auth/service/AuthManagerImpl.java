package com.secureauth.auth.service;

import com.secureauth.auth.exception.AuthException;
import com.secureauth.auth.helper.Dates;
import com.secureauth.auth.helper.Emails;
import com.secureauth.auth.helper.PasswordValidator;
import com.secureauth.auth.helper.RandomToken;
import com.secureauth.auth.repository.Repository;
import com.secureauth.auth.types.PasswordResetToken;
import com.secureauth.auth.types.Token;
import com.secureauth.auth.types.User;
import org.mindrot.jbcrypt.BCrypt;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public class AuthManagerImpl implements AuthManager {

    private final Repository repository;
    private final TokenService tokenService;

    private static final int PASSWORD_RESET_TOKEN_EXPIRATION_IN_MINS = 15;

    public AuthManagerImpl(Repository repository, TokenService tokenService) {
        this.repository = repository;
        this.tokenService = tokenService;
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

        if (resetToken.expiresAt().after(Date.from(Instant.now()))) {
            throw new AuthException("password reset token is expired");
        }

        var newPasswordHash = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        repository.updatePassword(user.id(), newPasswordHash);
    }
}
