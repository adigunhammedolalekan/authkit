
import io.github.adigunhammedolalekan.authkit.bootstrap.AuthBootstrap;
import io.github.adigunhammedolalekan.authkit.types.*;

import java.util.List;
import java.util.UUID;

public class Main {


    private static String PRIVATE_KEY = """
            -----BEGIN PRIVATE KEY-----
            MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDc09Qt2uxMLQOs
            rVTFCW50y34pP15vZ/76XF8JUA5Gpds82mXqR+r/wleS3q/WmxhtB7jY2ZmYx0op
            vd/0exZRw0m5RvCUWVba3JZ1I2nbUD5dNoe32nyc3y7bguYRhuW/dXG+2rfMs3T8
            31r8LGZ3GfoFrtemPX0H6OEGibHeZX5+ayCJgRO9K/smhWgirdjPeaWY9DjOncPg
            iAbRARXZui0+Tp9zpFArptIyfDYayTdW1Z4UfBI0OSD7QXY3Zff+CrQbKlS5wdV1
            nQNuw4EGd5RQ1WOKmSRT8nlEz0K5M+3i1HLUxryezBTwMV9BBNhtMbxCKvGnXytM
            Me3sdIrFAgMBAAECggEAHAYx7iPTSxOHXNmeYWcCAoZ0m010cZT0Ede5I9hqB2Mz
            AHUC7Al2dEC4lmX2ULjcD7IkLjjnOjFIYyW6jy8ztOqgp4euLlVsJVhLK9BztaZo
            U7IA5T8wA0R7kNX61oyVCYghCER/gNXhadn6lPgZhNHiHeiDa5BwFpniPM8qqHti
            d9yirLteB76THMmVVuJKa5Yk7u95hrV7lebWFby6samPYwvDh6wm2nRvNDLHnVM7
            ZiJ36fKfvJy+O2Sm5nH1vqZD0tf4bH6ZZiGJqSAdmq6eUyN8cdCc2TnPDc41J1fp
            ENOWKerzWeRGrt0vZjRxiLmpBqjUMHnte7pWJMSPPQKBgQD5mERBf94jcQWI9+Kx
            1ShQjCB6ITn0tqbGjQh7AIJ91h1sZbKIa0gSIaKR68ASF9T7CaOqW/txb2IBedAQ
            khVWg251dSwPLsYJSMgcBrgO/0ZYFUg/Tp5F9kdTq6K5ZI/MNziJPha3WbVfxjtu
            nE1pc0VLRQfYEahxviGIQlwfhwKBgQDifpKlvoAaR0StPQzWuxxoeIT0Rq6WBnNV
            QPSaqebVYMbMN1jRNZ4A88jLQsPHqtWt3qLiMaMkeH2GvRlLct7MnTznSl3WGpK3
            f8F86rbOzmVQvWN6UU8YDZ6Ucz6bEuxUX6dk0Uow4/Ubz97ogWK0CskFVxTqNFob
            qZZTiBeeUwKBgQDpkZ1mMhOd1PlaCKcCxbeZUNhfkdJbd8WwDnC+Mmbq/UDBnJWq
            5cIm/YTVyukNxXGQjp+zH6q4p4PuX6DesOX49LucfU8C0kGOlKZ2267IkgixCxF1
            9MtWKzbqTLbViQ6sC40Dma/GWtPJUYdyZiBnKvDQlEPX5gZckpFsorT05wKBgE6J
            f0ZTwU3bapf7tGj9JsOfDtM5cy54fCOSy36Z7X4N+cE0a+rXgbtozTaWJxckSMXq
            jmaKubxav4QqLAw6f8FJwKWe5tzoB/nzJd+v78XUUdNmaIkwipGXn2Zwc8QIEsTL
            CltuKyC62vJS6UOMIfK2TrA4Wvs/PgObkZHTfgUbAoGAOD2hXQI+HLhwBl7ojykn
            84oMUb9AdAuV9iuDoQ6kyH2Rv0Ket5rVf74zrDDZHI13TCYl8EIxEHkh4Uep3G8p
            rttkwpl/A+lYWMAYE5apYDApUNfbgOmN/foHyZOuMyerb+2QMCuzIV2jeGpytUVR
            Af4zM3NZMTRwlcN8RAmJ9ig=
            -----END PRIVATE KEY-----
            """;


    private static String PUBLIC_KEY = """
            -----BEGIN PUBLIC KEY-----
            MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3NPULdrsTC0DrK1UxQlu
            dMt+KT9eb2f++lxfCVAORqXbPNpl6kfq/8JXkt6v1psYbQe42NmZmMdKKb3f9HsW
            UcNJuUbwlFlW2tyWdSNp21A+XTaHt9p8nN8u24LmEYblv3Vxvtq3zLN0/N9a/Cxm
            dxn6Ba7Xpj19B+jhBomx3mV+fmsgiYETvSv7JoVoIq3Yz3mlmPQ4zp3D4IgG0QEV
            2botPk6fc6RQK6bSMnw2Gsk3VtWeFHwSNDkg+0F2N2X3/gq0GypUucHVdZ0DbsOB
            BneUUNVjipkkU/J5RM9CuTPt4tRy1Ma8nswU8DFfQQTYbTG8Qirxp18rTDHt7HSK
            xQIDAQAB
            -----END PUBLIC KEY-----
            """;

    public static void main(String[] args) {

        var thirdPartyAuthConfig = ThirdPartyAuthConfig.of(
                    ThirdPartyAuthCredential.of(
                            ThirdPartyAuthProviderIdentity.GOOGLE,
                            "clientId",
                            "clientSecret",
                            "https://redirect.uri"
                    ),
                    ThirdPartyAuthCredential.of(
                            ThirdPartyAuthProviderIdentity.FACEBOOK,
                            "clientId",
                            "clientSecret",
                            "https://facebook.uri"
                    ),
                    ThirdPartyAuthCredential.of(
                            ThirdPartyAuthProviderIdentity.X,
                            "clientId",
                            "clientSecret",
                            "https://twitter.uri"
                    ),
                    ThirdPartyAuthCredential.of(
                            ThirdPartyAuthProviderIdentity.APPLE,
                            "clientId",
                            "clientSecret",
                            "https://apple.uri"
                    ));
        var config = new AuthManagerConfig(
                new DatabaseConfig(
                        "jdbc:postgresql://localhost:5432/server",
                        "postgres",
                        "56d0921d-bc1b-41a2-992d-2f3955445dc9",
                        5),
                new TokenConfig(
                        "Zave",
                        5,
                        50,
                        PUBLIC_KEY,
                        PRIVATE_KEY),
                thirdPartyAuthConfig);
        var authManager = AuthBootstrap.config(config)
                .create();

        var email = "adigunhammed.lekan" + UUID.randomUUID().toString().substring(0, 5) + "@gmail.com";
        var password = "$adigunHAMMED001";

        var newUser = authManager.signUp(email, password);
        System.out.println(newUser);

        var authToken = authManager.login(email, password);
        System.out.println(authToken);

        var authenticatedUser = authManager.me(authToken.accessToken());
        System.out.println(authenticatedUser);

        var newPassword = "$newVERYstrongPASSWORD002";
        authManager.changePassword(newUser.id(), password, newPassword);

        var newAuthToken = authManager.login(email, newPassword);
        System.out.println(newAuthToken);

        var passwordResetToken = authManager.resetPassword(email);
        System.out.println(passwordResetToken);

        var newPasswordAfterReset = "$$$resetPASSWORD123";
        authManager.confirmPasswordReset(email, passwordResetToken.token(), newPasswordAfterReset);

        var authTokenAfterReset = authManager.login(email, newPasswordAfterReset);
        System.out.println(authTokenAfterReset);
    }
}
