package io.github.adigunhammedolalekan.authkit.types;

import java.time.LocalDateTime;

public record Token(
        String accessToken,

        String refreshToken,

        LocalDateTime expiresAt
) {}
