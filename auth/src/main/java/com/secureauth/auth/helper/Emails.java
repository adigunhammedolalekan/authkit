package com.secureauth.auth.helper;

import java.util.regex.Pattern;

import static java.util.Objects.nonNull;

public class Emails {

    public static final String INVALID_EMAIL_ERROR = "Invalid email address";

    private static final String EMAIL_REGEX = """
                    ^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$""";

    public static boolean isValid(String email) {
        return nonNull(email) && Pattern.compile(EMAIL_REGEX)
                .matcher(email)
                .matches();
    }
}
