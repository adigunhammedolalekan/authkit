package io.github.adigunhammedolalekan.authkitsample.errorhandler;

public record ErrorResponse(
        int statusCode,

        String message
) {}
