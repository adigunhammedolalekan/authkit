package io.github.adigunhammedolalekan.authkit.types;

import io.github.adigunhammedolalekan.authkit.helper.Json;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public record User(
        UUID id,

        String email,

        String password,

        Date createdAt,

        Date updatedAt,

        Date deletedAt,

        Date lastLoginAt,

        String authProvider,

        String attributes
) {

    public static User of(String email, String password) {
        return new User(UUID.randomUUID(), email, BCrypt.hashpw(password, BCrypt.gensalt()), null, null, null, null, null, null);
    }

    public static User of(ThirdPartyAuthProviderIdentity thirdPartyAuthProviderIdentity, OauthUserInfo userInfo) {
        var attrs = new UserAttributes(Map.of(thirdPartyAuthProviderIdentity, userInfo));
        return new User(
                UUID.randomUUID(),
                userInfo.email(),
                null,
                null,
                null,
                null,
                null,
                thirdPartyAuthProviderIdentity.name(),
                attrs.toString());
    }

    public static User fromResultSet(ResultSet result) throws SQLException {
        return new User(
                UUID.fromString(result.getString("id")),
                result.getString("email"),
                result.getString("password"),
                result.getDate("created_at"),
                result.getDate("updated_at"),
                result.getDate("deleted_at"),
                result.getDate("last_login_at"),
                result.getString("auth_provider"),
                result.getString("attributes"));
    }

    public static User of(UUID id) {
        return new User(id, null, null, null, null, null, null, null, null);
    }

    public UserAttributes getAttributes() {
        return Optional.ofNullable(attributes)
                .map(value -> Json.parse(value, UserAttributes.class))
                .orElse(UserAttributes.empty());
    }

    public String getAttributesAsString() {
        return Json.create(getAttributes());
    }

    public Map<ThirdPartyAuthProviderIdentity, OauthUserInfo> getUserInfos() {
        return getAttributes().authUserInfo();
    }

    public Optional<OauthUserInfo> getUserInfo(ThirdPartyAuthProviderIdentity authProvider) {
        return Optional.ofNullable(getUserInfos().get(authProvider));
    }
}
