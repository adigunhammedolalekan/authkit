package io.github.adigunhammedolalekan.authkit.integration.facebook;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FacebookOauthUser(
        String id,

        String email,

        @JsonProperty("first_name")
        String firstName,

        @JsonProperty("last_name")
        String lastName,

        String gender
) {}
