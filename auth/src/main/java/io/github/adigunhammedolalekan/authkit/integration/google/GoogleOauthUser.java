package io.github.adigunhammedolalekan.authkit.integration.google;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GoogleOauthUser(
        String id,

        String email,

        @JsonProperty("verified_email")
        String verifiedEmail,

        String name,

        @JsonProperty("given_name")
        String givenName,

        @JsonProperty("family_name")
        String familyName,

        String gender
) {}
