package io.github.adigunhammedolalekan.authkit.types;

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
}
