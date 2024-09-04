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
}
