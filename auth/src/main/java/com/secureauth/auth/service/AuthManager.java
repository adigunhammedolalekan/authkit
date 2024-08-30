package com.secureauth.auth.service;


import com.secureauth.auth.types.PasswordResetToken;
import com.secureauth.auth.types.Token;
import com.secureauth.auth.types.User;

import java.util.UUID;

public interface AuthManager {
    User signUp(String email, String password);

    Token login(String email, String password);

    User me(String token);

    void changePassword(UUID userId, String oldPassword, String newPassword);

    PasswordResetToken resetPassword(String email);

    void confirmPasswordReset(String email, String token, String newPassword);

    Token refreshToken(String token);
}
