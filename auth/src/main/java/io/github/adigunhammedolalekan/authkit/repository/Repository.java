package io.github.adigunhammedolalekan.authkit.repository;


import io.github.adigunhammedolalekan.authkit.types.PasswordResetToken;
import io.github.adigunhammedolalekan.authkit.types.User;

import java.time.LocalDateTime;
import java.util.List;
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

    void updateUserMetadata(UUID id, String metadata);

    void deleteUser(UUID id);

    List<User> getUsers();
}
