package com.secureauth.auth.repository;


import com.secureauth.auth.types.PasswordResetToken;
import com.secureauth.auth.types.User;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface Repository {
    void saveUser(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findById(UUID id);

    void updateLastLogin(UUID id, LocalDateTime lastLogin);

    void updatePassword(UUID id, String password);

    void savePasswordResetToken(PasswordResetToken token);

    Optional<PasswordResetToken> findPasswordResetToken(String token);
}
