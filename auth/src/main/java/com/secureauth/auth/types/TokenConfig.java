package com.secureauth.auth.types;

public record TokenConfig(
        String issuer,

        long tokenExpirationInMinutes,

        long refreshTokenExpirationInMinutes,

        String tokenPublicKey,

        String tokenPrivateKey
) {}
