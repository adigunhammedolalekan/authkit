package io.github.adigunhammedolalekan.authkit.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AccessToken(

        @JsonProperty("access_token")
        String accessToken,

        @JsonProperty("id_token")
        String idToken
) {}
