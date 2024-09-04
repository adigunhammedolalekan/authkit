package io.github.adigunhammedolalekan.authkit.service;

import io.github.adigunhammedolalekan.authkit.types.Token;
import io.github.adigunhammedolalekan.authkit.types.User;

public interface TokenService {

    Token generateToken(User user);

    User validateToken(String token);
}
