package io.github.adigunhammedolalekan.authkitsample.types;

public record CreateAccountRequest(
        String email,

        String password,

        String firstName,

        String lastName,

        String country
) {}
