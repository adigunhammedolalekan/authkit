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

## How it works
When you create an instance of AuthManager (as demonstrated below), the library constructs a dependency graph that automatically initializes all the necessary components, including:

* DatabaseManager
* DatabaseMigrator
* Repository
* TokenService

The DatabaseMigrator sets up the database tables, while the TokenService handles the generation and validation of JWT tokens.

### JWT
This library uses JSON Web Tokens (JWT) for authentication. JWTs are widely recognized, with a strong ecosystem use and for their support in microservices environments and setups. The tokens are generated and validated using asymmetric keys, and you can customize the configuration as shown in the sample section below.

```java
public record AuthManagerConfig(
        DatabaseConfig databaseConfig,

        TokenConfig tokenConfig
) {}


public record DatabaseConfig(
        String dsn,

        String username,

        String password
) {}

public record TokenConfig(
        String issuer,

        String subject,

        long tokenExpirationInMinutes,

        long refreshTokenExpirationInMinutes,

        String tokenPublicKey,

        String tokenPrivateKey
) {}
```
As you can see from the above, it's very self-explanatory. You can configure the library using the following

```java
var config = new AuthManagerConfig(
        new DatabaseConfig(
                "jdbc:postgresql://localhost:5432/server", // dsn
                "postgres", // username
                "56d0921d-bc1b-41a2-992d-2f3955445dc9" // password
        ),
        new TokenConfig(
                "IsserName", // issuer
                "Subject", // Token Subject
                5, // token expiration time(in mins)
                50, // refresh-token expiration time(in mins)
                PUBLIC_KEY, // RSA public key
                PRIVATE_KEY // RSA private key
        ));

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
```
Passwords must satisfy the following conditions
* must be at least 6 characters
* must contain a lowercase letter
* must contain an uppercase letter
* must contain a special character
* must contain a digit (0-9)


#### Contact
You can reach out to me at `adigunhammed.lekan@gmail.com` or on X `@adxgun` if you have any questions
