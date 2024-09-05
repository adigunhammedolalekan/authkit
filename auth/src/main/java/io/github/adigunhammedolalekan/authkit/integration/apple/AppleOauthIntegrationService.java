package io.github.adigunhammedolalekan.authkit.integration.apple;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import io.github.adigunhammedolalekan.authkit.exception.AuthException;
import io.github.adigunhammedolalekan.authkit.integration.APIService;
import io.github.adigunhammedolalekan.authkit.integration.OauthIntegrationService;
import io.github.adigunhammedolalekan.authkit.integration.google.GoogleOauthIntegrationService;
import io.github.adigunhammedolalekan.authkit.types.AccessToken;
import io.github.adigunhammedolalekan.authkit.types.OauthUserInfo;
import io.github.adigunhammedolalekan.authkit.types.ThirdPartyAuthCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.Map;

public class AppleOauthIntegrationService implements OauthIntegrationService {

    private final ThirdPartyAuthCredential credential;
    private final APIService apiService;

    private final Logger LOGGER = LoggerFactory.getLogger(GoogleOauthIntegrationService.class);

    public AppleOauthIntegrationService(
            ThirdPartyAuthCredential credential,
            APIService apiService) {
        this.credential = credential;
        this.apiService = apiService;
    }

    @Override
    public AccessToken getAccessToken(String code) {
        try {
            var params = Map.of(
                    "code", code,
                    "grant_type", "authorization_code",
                    "redirect_uri", credential.redirectUri(),
                    "client_id", credential.clientId(),
                    "client_secret", credential.clientSecret());
            return apiService.post(
                    credential.identity().getRetrieveAccessTokenUrl(),
                    params,
                    AccessToken.class,
                    "Content-Type",
                    "application/x-www-form-urlencoded");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public OauthUserInfo getUser(AccessToken accessToken) {
        try {
            var decoded = verifyJWT(accessToken.idToken());
            return OauthUserInfo.forApple(decoded);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private DecodedJWT verifyJWT(String token) throws Exception {
        var jwt = JWT.decode(token);
        var publicKey = getApplePublicKey(jwt.getKeyId());
        var decoded = JWT.require(Algorithm.RSA256(publicKey))
                .withIssuer(ThirdPartyAuthCredential.APPLE_ISS)
                .build()
                .verify(token);

        if (decoded.getExpiresAt().before(new Date())) {
            throw new AuthException("Expired Apple JWT token");
        }

        if (!decoded.getAudience().contains(credential.clientId())) {
            throw new AuthException("Invalid Apple JWT token, audience mismatch");
        }

        return decoded;
    }

    private RSAPublicKey getApplePublicKey(String kid) throws Exception {
        var jwkSet = JWKSet.load(new URI(ThirdPartyAuthCredential.APPLE_AUTH_KEYS_URL).toURL());
        var jwk = jwkSet.getKeyByKeyId(kid);
        if (!(jwk instanceof RSAKey)) {
            throw new AuthException("failed to retrieve apple public key");
        }

        return ((RSAKey) jwk).toRSAPublicKey();
    }
}
