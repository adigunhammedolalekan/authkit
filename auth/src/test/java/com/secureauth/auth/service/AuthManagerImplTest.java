package com.secureauth.auth.service;

import com.secureauth.auth.exception.AuthException;
import com.secureauth.auth.helper.Emails;
import com.secureauth.auth.helper.PasswordValidator;
import com.secureauth.auth.repository.Repository;
import com.secureauth.auth.types.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthManagerImplTest {

    @Mock
    private Repository authRepository;

    @Mock
    private TokenService tokenService;

    private AuthManager authManager;

    @BeforeEach
    protected void setUp() {
        authManager = new AuthManagerImpl(authRepository, tokenService);
    }

    @Test
    void sign_up_test() {
        var TESTEMAIL = "test@secureauth.com";
        var TESTPASSWORD = "$TESTpassword001";

        when(authRepository.findByEmail(TESTEMAIL)).thenReturn(Optional.empty());

        var user = authManager.signUp(TESTEMAIL, TESTPASSWORD);

        var argumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(authRepository).saveUser(argumentCaptor.capture());

        var savedUser = argumentCaptor.getValue();
        assertThat(savedUser)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(user);
    }

    @Test
    void sign_up_invalid_email_test() {
        var TESTEMAIL = "test@secureauth";
        var TESTPASSWORD = "$TESTpassword001";

        var exception = assertThrows(AuthException.class,
                () -> authManager.signUp(TESTEMAIL, TESTPASSWORD));

        assertEquals(exception.getMessage(), Emails.INVALID_EMAIL_ERROR);
    }

    @Test
    void sign_up_invalid_password_test() {
        var TESTEMAIL = "test@secureauth.com";
        var PASSWORDS = List.of("$password001", "$TESTpassword", "$TEST001", "TEST");

        when(authRepository.findByEmail(TESTEMAIL)).thenReturn(Optional.empty());

        for (var invalidPassword : PASSWORDS) {
            var exception = assertThrows(AuthException.class,
                    () -> authManager.signUp(TESTEMAIL, invalidPassword));

            assertEquals(exception.getMessage(), PasswordValidator.PASSWORD_VALIDATION_ERROR_MESSAGE);
        }
    }

    @Test
    void sign_up_existing_email_test() {
        var TESTEMAIL = "test@secureauth.com";
        var TESTPASSWORD = "$TESTpassword001";

        when(authRepository.findByEmail(TESTEMAIL)).thenReturn(Optional.of(User.of(UUID.randomUUID())));

        var exception = assertThrows(AuthException.class,
                () -> authManager.signUp(TESTEMAIL, TESTPASSWORD));

        assertEquals(exception.getMessage(), "User with email test@secureauth.com already exists");
    }
}
