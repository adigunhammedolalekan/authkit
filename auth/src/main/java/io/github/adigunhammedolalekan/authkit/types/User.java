package io.github.adigunhammedolalekan.authkit.types;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.adigunhammedolalekan.authkit.helper.Json;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public record User(
        UUID id,

        String email,

        @JsonIgnore
        String password,

        @JsonIgnore
        Date createdAt,

        @JsonIgnore
        Date updatedAt,

        @JsonIgnore
        Date deletedAt,

        Date lastLoginAt,

        String authProvider,

        UserAttributes attributes
) {

    private static final Logger LOGGER = LoggerFactory.getLogger(User.class);
    private static final String AUTH_PROVIDER_EMAIL_PASSWORD = "email_password_auth";

    public static User of(String email, String password) {
        return new User(UUID.randomUUID(), email, BCrypt.hashpw(password, BCrypt.gensalt()), null, null, null, null, AUTH_PROVIDER_EMAIL_PASSWORD, null);
    }

    public static User of(ThirdPartyAuthProviderIdentity thirdPartyAuthProviderIdentity, OauthUserInfo userInfo) {
        var attrs = new UserAttributes(Map.of(thirdPartyAuthProviderIdentity, userInfo));
        return new User(
                UUID.randomUUID(),
                userInfo.email(),
                Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes()),
                null,
                null,
                null,
                null,
                thirdPartyAuthProviderIdentity.name(),
                attrs);
    }

    public static User fromResultSet(ResultSet result) throws SQLException {
        var attrsJson = result.getString("attributes");
        var attrs = isNull(attrsJson) || attrsJson.isEmpty() ?
                UserAttributes.empty() : Json.parse(attrsJson, UserAttributes.class);

        return new User(
                UUID.fromString(result.getString("id")),
                result.getString("email"),
                result.getString("password"),
                result.getDate("created_at"),
                result.getDate("updated_at"),
                result.getDate("deleted_at"),
                result.getDate("last_login_at"),
                result.getString("auth_provider"),
                attrs);
    }

    public static User of(UUID id) {
        return new User(id, null, null, null, null, null, null, null, null);
    }

    @Override
    public UserAttributes attributes() {
        return isNull(attributes) ? UserAttributes.empty() : attributes;
    }

    @JsonIgnore
    public String getAttributesAsString() {
        return Json.create(attributes());
    }

    @JsonIgnore
    public Map<ThirdPartyAuthProviderIdentity, OauthUserInfo> getUserInfos() {
        return attributes().authUserInfo();
    }

    @JsonIgnore
    public Optional<OauthUserInfo> getInfo(ThirdPartyAuthProviderIdentity authProvider) {
        return Optional.ofNullable(getUserInfos().get(authProvider));
    }

    @JsonIgnore
    public void updateAttributes(ThirdPartyAuthProviderIdentity authProvider, OauthUserInfo userInfo) {
        attributes().update(authProvider, userInfo);
    }

    @JsonIgnore
    public boolean isSignedUpWithPassword() {
        return nonNull(authProvider()) &&
                AUTH_PROVIDER_EMAIL_PASSWORD.equals(authProvider());
    }
}
