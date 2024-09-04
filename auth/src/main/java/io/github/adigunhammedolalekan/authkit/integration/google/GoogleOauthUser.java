package io.github.adigunhammedolalekan.authkit.integration.google;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleOauthUser(
        String id,

        String email,

        @JsonProperty("verified_email")
        String verifiedEmail,

        String name,

        String givenName,

        String familyName,

        String gender
) {}
