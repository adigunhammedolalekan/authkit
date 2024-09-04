package io.github.adigunhammedolalekan.authkit.types;

public record ThirdPartyAuthConfig(
        Google google,

        Facebook facebook,

        Twitter twitter,

        Apple apple
) {

    public record Google(
            String clientId,

            String clientSecret,

            String redirectURI
    ) {}

    public record Facebook(
            String clientId,

            String clientSecret,

            String redirectURI
    ) {}

    public record Twitter(
            String clientId,

            String clientSecret,

            String redirectURI
    ) {}

    public record Apple(

    ) {}

    public static Google google(String clientId, String clientSecret, String redirectURI) {
        return new Google(clientId, clientSecret, redirectURI);
    }

    public static Facebook facebook(String clientId, String clientSecret, String redirectURI) {
        return new Facebook(clientId, clientSecret, redirectURI);
    }

    public static Twitter twitter(String clientId, String clientSecret, String redirectURI) {
        return new Twitter(clientId, clientSecret, redirectURI);
    }
}
