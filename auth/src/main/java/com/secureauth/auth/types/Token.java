package com.secureauth.auth.types;

import java.time.LocalDateTime;

public record Token(
        String accessToken,

        String refreshToken,

        LocalDateTime expiresAt
) {}
