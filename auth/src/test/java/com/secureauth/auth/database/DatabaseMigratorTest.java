package com.secureauth.auth.database;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DatabaseMigratorTest {

    @Mock
    private DatabaseManager databaseManager;

    private DatabaseMigrator migrator;

    @BeforeEach
    void setUp() {
        migrator = new DatabaseMigrator(databaseManager);
    }

    @Test
    void migrate_test() {
        migrator.migrate();

        Queries.MIGRATIONS.forEach(this::verifyMigrationExecution);
    }

    private void verifyMigrationExecution(String query) {
        try {
            verify(databaseManager, times(1)).executeUpdate(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
