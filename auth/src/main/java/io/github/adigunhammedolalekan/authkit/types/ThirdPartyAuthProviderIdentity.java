package io.github.adigunhammedolalekan.authkit.types;

import java.net.URLEncoder;

public enum ThirdPartyAuthProviderIdentity {
    GOOGLE,

    APPLE,

    FACEBOOK,

    X;

    private static final String GOOGLE_OAUTH_USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo?";
    private static final String GOOGLE_OAUTH_ACCESS_TOKEN_URL = "https://accounts.google.com/o/oauth2/token?";

    private static final String FACEBOOK_OAUTH_USER_INFO_URL = "https://graph.facebook.com/me?";
    private static final String FACEBOOK_OAUTH_ACCESS_TOKEN_URL = "https://graph.facebook.com/v20.0/oauth/access_token?";

    private static final String TWITTER_OAUTH_USER_INFO_URL = "https://api.x.com/1.1/account/verify_credentials.json?";
    private static final String TWITTER_OAUTH_ACCESS_TOKEN_URL = "https://api.x.com/2/oauth2/token?";

    private static final String APPLE_OAUTH_USER_INFO_URL = "";
    private static final String APPLE_OAUTH_ACCESS_TOKEN_URL = "https://appleid.apple.com/auth/token?";

    public static final String GOOGLE_AUTHORIZATION_URL_TEMPLATE = "https://accounts.google.com/o/oauth2/auth?client_id=%s&redirect_uri=%s&scope=https://www.googleapis.com/auth/userinfo.email+https://www.googleapis.com/auth/userinfo.profile&response_type=code";
    public static final String FACEBOOK_AUTHORIZATION_URL_TEMPLATE = "https://www.facebook.com/v12.0/dialog/oauth?client_id=%s&redirect_uri=%s&state=%s&response_type=code&scope=email,public_profile";
    public static final String APPLE_AUTHORIZATION_URL_TEMPLATE = "https://appleid.apple.com/auth/authorize?client_id=%s&redirect_uri=%s&state=%s&response_type=code&scope=name,email&response_mode=query";
    public static final String X_AUTHORIZATION_URL_TEMPLATE = "https://twitter.com/i/oauth2/authorize?response_type=code&client_id=%s&redirect_uri=%s&scope=users.read&state=%s&code_challenge=%s&code_challenge_method=plain";

    public String getRetrieveAccessTokenUrl() {
        return switch (this) {
            case GOOGLE -> GOOGLE_OAUTH_ACCESS_TOKEN_URL;
            case APPLE -> APPLE_OAUTH_ACCESS_TOKEN_URL;
            case FACEBOOK -> FACEBOOK_OAUTH_ACCESS_TOKEN_URL;
            case X -> TWITTER_OAUTH_ACCESS_TOKEN_URL;
        };
    }

    public String getUserInfoUrl() {
        return switch (this) {
            case GOOGLE -> GOOGLE_OAUTH_USER_INFO_URL;
            case APPLE -> APPLE_OAUTH_USER_INFO_URL;
            case FACEBOOK -> FACEBOOK_OAUTH_USER_INFO_URL;
            case X -> TWITTER_OAUTH_USER_INFO_URL;
        };
    }
}
