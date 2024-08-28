package com.secureauth.auth.database;

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
    Connection connection;

    @Mock
    Statement statement;

    @Mock
    PreparedStatement preparedStatement;

    private DatabaseManager databaseManager;

    @BeforeEach
    void setUp() {
        databaseManager = new DatabaseManager(connection);
    }

    @Test
    void execute_update_test() throws SQLException {
        when(connection.createStatement()).thenReturn(statement);

        databaseManager.executeUpdate(Queries.CREATE_USER);

        verify(statement, times(1))
                .executeUpdate(Queries.CREATE_USER);
    }

    @Test
    void get_prepared_statement_test() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        var result = databaseManager.prepareStatement(Queries.CREATE_USER);

        assertEquals(result, preparedStatement);
        verify(connection, times(1)).prepareStatement(Queries.CREATE_USER);
    }
}
