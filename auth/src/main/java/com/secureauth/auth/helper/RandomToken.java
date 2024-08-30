package com.secureauth.auth.helper;

import java.util.Random;

public class RandomToken {

    public static String generate() {
        var random = new Random();
        return String.format("%06d", random.nextInt(999999));
    }
}
