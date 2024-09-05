package io.github.adigunhammedolalekan.authkit.types;

import static java.util.Objects.nonNull;

public record TokenConfig(
        String issuer,

        long tokenExpirationInMinutes,

        long refreshTokenExpirationInMinutes,

        String tokenPublicKey,

        String tokenPrivateKey
) {

    @Override
    public long tokenExpirationInMinutes() {
        return tokenExpirationInMinutes <= 0 ? 5 : tokenExpirationInMinutes;
    }

    @Override
    public long refreshTokenExpirationInMinutes() {
        return refreshTokenExpirationInMinutes <= 0 ? 50 : refreshTokenExpirationInMinutes;
    }

    public boolean isValid() {
        return nonNull(issuer) && !issuer.isEmpty()
                && nonNull(tokenPublicKey) && !tokenPublicKey.isEmpty()
                && nonNull(tokenPrivateKey) && !tokenPrivateKey.isEmpty();
    }
}
