package com.secureauth.auth.service;

import com.secureauth.auth.types.Token;
import com.secureauth.auth.types.User;

public interface TokenService {

    Token generateToken(User user);

    User validateToken(String token);
}
