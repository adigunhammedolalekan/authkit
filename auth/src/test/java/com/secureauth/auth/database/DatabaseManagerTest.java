package com.secureauth.auth.database;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DatabaseManagerTest {

    @Mock
    HikariDataSource dataSource;

    @Mock
    Connection connection;

    @Mock
    Statement statement;

    @Mock
    PreparedStatement preparedStatement;

    private DatabaseManager databaseManager;

    @BeforeEach
    void setUp() {
        databaseManager = new DatabaseManager(dataSource);
    }

    @Test
    void execute_update_test() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);

        databaseManager.executeUpdate(Queries.CREATE_USER);

        verify(statement, times(1))
                .executeUpdate(Queries.CREATE_USER);
    }
}
