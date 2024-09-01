package com.secureauth.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.secureauth.auth.helper.Keys;
import com.secureauth.auth.types.Token;
import com.secureauth.auth.types.TokenConfig;
import com.secureauth.auth.types.User;


import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import static com.secureauth.auth.helper.Dates.toDate;


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
                generateToken(user.id().toString(), toDate(expiresAt)),
                generateToken(user.id().toString(), toDate(refreshTokenExpiresAt)),
                expiresAt);
    }

    @Override
    public User validateToken(String token) {
        var algorithm = Algorithm.RSA256(publicKey, null);  // Use RSA256 algorithm
        var verifier = JWT.require(algorithm)
                .withIssuer(config.issuer())
                .build();
        var jwt = verifier.verify(token);

        var userId = UUID.fromString(jwt.getClaim(CLAIM_KEY).asString());
        return User.of(userId);
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
