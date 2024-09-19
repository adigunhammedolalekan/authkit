package io.github.adigunhammedolalekan.authkit.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.github.adigunhammedolalekan.authkit.exception.AuthException;
import io.github.adigunhammedolalekan.authkit.helper.Keys;
import io.github.adigunhammedolalekan.authkit.types.Token;
import io.github.adigunhammedolalekan.authkit.types.TokenConfig;
import io.github.adigunhammedolalekan.authkit.types.User;
import io.github.adigunhammedolalekan.authkit.helper.Dates;


import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;


public class JwtTokenService implements TokenService {

    private final RSAPrivateKey privateKey;
    private final RSAPublicKey publicKey;
    private final TokenConfig config;

    private static final String CLAIM_KEY = "user";

    public JwtTokenService(
            TokenConfig config) throws NoSuchAlgorithmException, InvalidKeySpecException {
        this.privateKey = Keys.getPrivateKey(config.tokenPrivateKey());
        this.publicKey = Keys.getPublicKey(config.tokenPublicKey());
        this.config = config;
    }

    @Override
    public Token generateToken(User user) {
        var expiresAt = LocalDateTime.now()
                .plusMinutes(config.tokenExpirationInMinutes());
        var refreshTokenExpiresAt = LocalDateTime.now()
                .plusMinutes(config.refreshTokenExpirationInMinutes());
        return new Token(
                generateToken(user.id().toString(), Dates.toDate(expiresAt)),
                generateToken(user.id().toString(), Dates.toDate(refreshTokenExpiresAt)),
                expiresAt);
    }

    @Override
    public User validateToken(String token) {
        var algorithm = Algorithm.RSA256(publicKey, null);
        var verifier = JWT.require(algorithm)
                .withIssuer(config.issuer())
                .build();
        var jwt = verifier.verify(token);

        if (!verify(jwt)) {
            throw new AuthException("Invalid JWT token");
        }

        var userId = UUID.fromString(jwt.getClaim(CLAIM_KEY).asString());
        return User.of(userId);
    }

    @Override
    public boolean verify(DecodedJWT token) {
        return token.getIssuer().equals(config.issuer())
                && token.getExpiresAt().after(new Date());
    }

    private String generateToken(String userId, Date expiresAt) {
        return JWT.create()
                .withIssuer(config.issuer())
                .withSubject(userId)
                .withClaim(CLAIM_KEY, userId)
                .withExpiresAt(expiresAt)
                .sign(Algorithm.RSA256(null, privateKey));
    }
}
