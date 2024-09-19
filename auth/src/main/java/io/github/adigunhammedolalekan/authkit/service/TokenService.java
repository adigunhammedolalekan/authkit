package io.github.adigunhammedolalekan.authkit.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import io.github.adigunhammedolalekan.authkit.types.Token;
import io.github.adigunhammedolalekan.authkit.types.User;

public interface TokenService {

    Token generateToken(User user);

    User validateToken(String token);

    boolean verify(DecodedJWT token);
}
