package com.secureauth.auth.service;


import com.secureauth.auth.types.Token;
import com.secureauth.auth.types.User;

import java.util.UUID;

public interface AuthManager {
    User signUp(String email, String password);

    Token login(String email, String password);

    User me(String token);

    void changePassword(UUID userId, String oldPassword, String newPassword);

    void forgotPassword(String email);
}
