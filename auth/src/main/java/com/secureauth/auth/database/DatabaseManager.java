package com.secureauth.auth.database;

import java.sql.*;

public class DatabaseManager {

    private final Connection connection;

    public DatabaseManager(
            Connection connection) {
        this.connection = connection;
    }

    public void executeUpdate(String query) throws SQLException {
        connection.createStatement().executeUpdate(query);
    }

    public PreparedStatement prepareStatement(String query) throws SQLException {
        return connection.prepareStatement(query);
    }
}
