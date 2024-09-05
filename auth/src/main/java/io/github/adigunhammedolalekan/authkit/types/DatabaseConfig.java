package io.github.adigunhammedolalekan.authkit.types;

import static java.util.Objects.nonNull;

public record DatabaseConfig(
        String dsn,

        String username,

        String password,

        int connectionPoolSize
) {

    @Override
    public int connectionPoolSize() {
        return connectionPoolSize == 0 ? 5 : connectionPoolSize;
    }

    public boolean isValid() {
        return nonNull(dsn) && !dsn.isEmpty()
                && nonNull(username) && !username.isEmpty()
                && nonNull(password) && !password.isEmpty();
    }
}
