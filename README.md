### authkit
A compact and reusable authentication library.

## Why
While working on multiple projects, you realize the need for a robust authentication component that can handle:

* User registration(including validation of email, used emails and password validation)
* User login and token generation
* Token validation and verification
* Email verification
* Password changes
* Password resets
* Authentication via popular third-party platforms (Google, Meta, etc.)


This library is designed to meet those needs. It serves as a pluggable component for any Java project and potentially for projects in other languages as well. The goal is to keep it simple and easy to modify. It currently stores user authentication data in an SQL database, you can choose your preferred SQL database but PostgresSQL is recommended 

### Installation
```groovy
implementation 'io.github.adigunhammedolalekan:authkit:v1.0.5'
```

## How it works
When you create an instance of AuthManager (as demonstrated below), the library constructs a dependency graph that automatically initializes all the necessary components, including:

* DatabaseManager
* DatabaseMigrator
* Repository
* TokenService
* ThirdPartyAuthProvider

The DatabaseMigrator sets up the database tables, while the TokenService handles the generation and validation of JWT tokens.

### JWT
This library uses JSON Web Tokens (JWT) for authentication. JWTs are widely recognized, with a strong ecosystem use and for their support in microservices environments and setups. The tokens are generated and validated using asymmetric keys, and you can customize the configuration as shown in the sample section below.

### Third-Party authentication config
To enable third-party authentication, which currently supports Google, Facebook, Twitter, and Apple, you need to provide the OAuth configuration parameters for each provider. Once these parameters are configured, you can perform the initial step of the OAuth flow to retrieve the client's authorization code (this step occurs outside the scope of this library). After obtaining the authorization code, you can authenticate the user's account using the following process:

* **Existing Account with Different Provider or Password:** If the account's email already exists in the system but was registered using a different provider or a password, an error will be thrown to indicate this conflict.

* **New Account:** If the account does not exist in the system, the user will be automatically signed up, and an authentication Token will be returned.

* **Existing Account with Same Provider:** If the account has already been signed up using the same provider, the account information will be updated, and a Token will be returned to the caller.

```java
public record AuthManagerConfig(
        DatabaseConfig databaseConfig,

        TokenConfig tokenConfig,

        ThirdPartyAuthConfig thirdPartyAuthConfig
) {}


public record DatabaseConfig(
        String dsn,

        String username,

        String password,

        int connectionPoolSize
) {}

public record TokenConfig(
        String issuer,

        long tokenExpirationInMinutes,

        long refreshTokenExpirationInMinutes,

        String tokenPublicKey,

        String tokenPrivateKey
) {}

public record ThirdPartyAuthConfig(
        List<ThirdPartyAuthCredential> credentials
) {}

public record ThirdPartyAuthCredential(
        ThirdPartyAuthProviderIdentity identity,

        String clientId,

        String clientSecret,

        String redirectUri
) {}
```
As you can see from the above, it's very self-explanatory. You can configure the library using the following

```java
// You can safely set any of the above value to `null` if you don't want to enable any third-party auth
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
                "jdbc:postgresql://localhost:5432/server", // dsn
                "postgres", // username
                "56d0921d-bc1b-41a2-992d-2f3955445dc9", // password,
                5
        ),
        new TokenConfig(
                "IsserName", // issuer
                5, // token expiration time(in mins)
                50, // refresh-token expiration time(in mins)
                PUBLIC_KEY, // RSA public key
                PRIVATE_KEY // RSA private key
        ),
        thirdPartyAuthConfig);

// init the lib
var authManager = 
        AuthBootstrap.config(config)
        .create();

var email = "adigunhammed.lekan" + UUID.randomUUID().toString().substring(0, 5) + "@gmail.com";
var password = "$strongPASSWORD001"; 

// sign up a new user
var newUser = authManager.signUp(email, password);
System.out.println(newUser);

// authenticate/login a user
var authToken = authManager.login(email, password);
System.out.println(authToken);

// find out the identity of a user using a token
var authenticatedUser = authManager.me(authToken.accessToken());
System.out.println(authenticatedUser);

var newPassword = "$newVERYstrongPASSWORD002";
authManager.changePassword(newUser.id(), newUser.password(), newPassword);

var newAuthToken = authManager.login(email, newPassword);
System.out.println(newAuthToken);

var passwordResetToken = authManager.resetPassword(email);
System.out.println(passwordResetToken);

var newPasswordAfterReset = "$$$resetPASSWORD123";
authManager.confirmPasswordReset(email, passwordResetToken.token(), newPasswordAfterReset);

var authTokenAfterReset = authManager.login(email, newPasswordAfterReset);
System.out.println(authTokenAfterReset);

// ThirdParty auth: Apple, Facebook, Google and X
// Perform the first step in the oauth flow and retrieve the client's authorization_code
// You can then use this code to authenticate the user
// This will automatically sign up the user if it does not exists or update the user if it does exists
// and a valid `AccessToken` will be returned
var token = authManager.thirdPartyAuthentication(ThirdPartyAuthProviderIdentity.GOOGLE, authorizationCode);

// retrieve the user and their info(from the third party provider)
var user = authManager.me(token.accessToken());
var oauthUserInfo = user.getInfo(ThirdPartyAuthProviderIdentity.GOOGLE);
```

#### **Passwords must satisfy the following conditions**
* must be at least 6 characters
* must contain a lowercase letter
* must contain an uppercase letter
* must contain a special character
* must contain a digit (0-9)


#### Note on password reset steps
To reset a user's password, you can utilize the library to generate and verify a password reset token. After generating the token, it should be sent to the user via email or text message for verification. Once the token is confirmed, you can proceed to update the user's password. Sample below
```java
var passwordResetToken = authManager.resetPassword(email);
System.out.println(passwordResetToken);

var newPasswordAfterReset = "$$$resetPASSWORD123";
authManager.confirmPasswordReset(email, passwordResetToken.token(), newPasswordAfterReset);

var authTokenAfterReset = authManager.login(email, newPasswordAfterReset);
System.out.println(authTokenAfterReset);
```

#### Contact
You can reach out to me at `adigunhammed.lekan@gmail.com` or on X `@adxgun` if you have any questions
