package io.github.adigunhammedolalekan.authkit.types;

import io.github.adigunhammedolalekan.authkit.integration.google.GoogleOauthUser;

public record OauthUserInfo(
        String id,

        String email,

        String firstName,

        String lastName,

        String middleName,

        String gender
) {

    public static OauthUserInfo forGoogle(GoogleOauthUser googleOauthUser) {
        return new OauthUserInfo(
                googleOauthUser.id(),
                googleOauthUser.email(),
                googleOauthUser.givenName(),
                googleOauthUser.familyName(),
                null,
                googleOauthUser.gender());
    }
}
