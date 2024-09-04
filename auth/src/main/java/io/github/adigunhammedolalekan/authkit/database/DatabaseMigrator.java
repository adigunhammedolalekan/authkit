package io.github.adigunhammedolalekan.authkit.database;

import java.sql.SQLException;

public class DatabaseMigrator {

    private final DatabaseManager manager;

    public DatabaseMigrator(DatabaseManager manager) {
        this.manager = manager;
    }

    public void migrate() {
        Queries.MIGRATIONS
                .forEach(this::run);
    }

    private void run(String query) {
        try {
            manager.executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
