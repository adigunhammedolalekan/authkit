package io.github.adigunhammedolalekan.authkit.types;

import java.util.Base64;

public enum ThirdPartyAuthProviderIdentity {
    GOOGLE,

    APPLE,

    FACEBOOK,

    TWITTER;

    private static final String GOOGLE_OAUTH_USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo";
    private static final String GOOGLE_OAUTH_ACCESS_TOKEN_URL = "https://accounts.google.com/o/oauth2/token";

    private static final String FACEBOOK_OAUTH_USER_INFO_URL = "https://graph.facebook.com/me";
    private static final String FACEBOOK_OAUTH_ACCESS_TOKEN_URL = "https://graph.facebook.com/v20.0/oauth/access_token";

    private static final String TWITTER_OAUTH_USER_INFO_URL = "https://api.x.com/1.1/account/verify_credentials.json";
    private static final String TWITTER_OAUTH_ACCESS_TOKEN_URL = "https://api.x.com/2/oauth2/token";

    private static final String APPLE_OAUTH_USER_INFO_URL = "";
    private static final String APPLE_OAUTH_ACCESS_TOKEN_URL = "https://appleid.apple.com/auth/token";

    public String getRetrieveAccessTokenUrl() {
        return switch (this) {
            case GOOGLE -> GOOGLE_OAUTH_ACCESS_TOKEN_URL;
            case APPLE -> APPLE_OAUTH_ACCESS_TOKEN_URL;
            case FACEBOOK -> FACEBOOK_OAUTH_ACCESS_TOKEN_URL;
            case TWITTER -> TWITTER_OAUTH_ACCESS_TOKEN_URL;
        };
    }

    public String getUserInfoUrl() {
        return switch (this) {
            case GOOGLE -> GOOGLE_OAUTH_USER_INFO_URL;
            case APPLE -> "";
            case FACEBOOK -> FACEBOOK_OAUTH_USER_INFO_URL;
            case TWITTER -> TWITTER_OAUTH_USER_INFO_URL;
        };
    }
}
