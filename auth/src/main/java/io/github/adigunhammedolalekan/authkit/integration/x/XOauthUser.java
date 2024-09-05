package io.github.adigunhammedolalekan.authkit.integration.x;

import com.fasterxml.jackson.annotation.JsonProperty;

import static java.util.Objects.isNull;

public record XOauthUser(
        @JsonProperty("id_str")
        String id,

        String name,

        String email
) {

    public String firstName() {
        if (isNull(name) || name.isEmpty()) {
            return null;
        }
        var names = name.split(" ");
        if (names.length > 1) {
            return names[0];
        }
        return name;
    }

    public String lastName() {
        if (isNull(name) || name.isEmpty()) {
            return null;
        }
        var names = name.split(" ");
        if (names.length > 1) {
            return names[1];
        }
        return name;
    }
}
