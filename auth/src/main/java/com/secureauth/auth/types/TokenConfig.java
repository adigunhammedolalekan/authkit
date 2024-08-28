package com.secureauth.auth.types;

public record TokenConfig(
        String issuer,

        String subject,

        long tokenExpirationInMinutes,

        long refreshTokenExpirationInMinutes,

        String tokenPublicKey,

        String tokenPrivateKey
) {}
