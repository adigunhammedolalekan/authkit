package io.github.adigunhammedolalekan.authkit.types;

public record TokenConfig(
        String issuer,

        long tokenExpirationInMinutes,

        long refreshTokenExpirationInMinutes,

        String tokenPublicKey,

        String tokenPrivateKey
) {}
