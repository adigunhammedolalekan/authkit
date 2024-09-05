package io.github.adigunhammedolalekan.authkit.types;

import com.auth0.jwt.interfaces.DecodedJWT;
import io.github.adigunhammedolalekan.authkit.integration.facebook.FacebookOauthUser;
import io.github.adigunhammedolalekan.authkit.integration.google.GoogleOauthUser;
import io.github.adigunhammedolalekan.authkit.integration.x.XOauthUser;

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

    public static OauthUserInfo forFacebook(FacebookOauthUser facebookOauthUser) {
        return new OauthUserInfo(
                facebookOauthUser.id(),
                facebookOauthUser.email(),
                facebookOauthUser.firstName(),
                facebookOauthUser.lastName(),
                null,
                facebookOauthUser.gender());
    }

    public static OauthUserInfo forX(XOauthUser xOauthUser) {
        return new OauthUserInfo(
                xOauthUser.id(),
                xOauthUser.email(),
                xOauthUser.firstName(),
                xOauthUser.lastName(),
                null,
                null);
    }

    public static OauthUserInfo forApple(DecodedJWT jwt) {
        return new OauthUserInfo(
                jwt.getSubject(),
                jwt.getClaim("email").asString(),
                null,
                null,
                null,
                null);
    }
}
