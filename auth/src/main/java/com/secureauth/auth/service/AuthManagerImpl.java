package com.secureauth.auth.service;

import com.secureauth.auth.exception.AuthException;
import com.secureauth.auth.helper.Emails;
import com.secureauth.auth.helper.PasswordValidator;
import com.secureauth.auth.repository.Repository;
import com.secureauth.auth.types.Token;
import com.secureauth.auth.types.User;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.UUID;

public class AuthManagerImpl implements AuthManager {

    private final Repository repository;
    private final TokenService tokenService;

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
        repository.save(user);
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
    public void forgotPassword(String email) {
    }
}
