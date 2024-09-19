package io.github.adigunhammedolalekan.authkitsample.types;

public record AuthenticateAccountRequest(
        String email,

        String password
) {}
