package io.github.adigunhammedolalekan.authkit.helper;

import java.util.regex.Pattern;

import static java.util.Objects.isNull;

public class PasswordValidator {

    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_\\-+=\\[\\]{}|\\\\:;\"'<>,.?/~`]).{1,}$";
    public static final String PASSWORD_VALIDATION_ERROR_MESSAGE = """
            Password length must be at least 6 and must contain a special character, a lowercase letter, an uppercase letter and a digit.
            """;


    public static boolean isValidPassword(String password) {
        if (isNull(password) || password.length() < 8) {
            return false;
        }
        return Pattern.compile(PASSWORD_REGEX)
                .matcher(password)
                .matches();
    }
}
