package io.github.adigunhammedolalekan.authkit.helper;

import java.security.SecureRandom;

public class RandomToken {

    public static String generate() {
        var random = new SecureRandom();
        return String.format("%06d", random.nextInt(999999));
    }
}
