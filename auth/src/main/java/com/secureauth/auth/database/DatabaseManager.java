package com.secureauth.auth.database;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;

public class DatabaseManager {

    private final HikariDataSource dataSource;

    public DatabaseManager(
            HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void executeUpdate(String query) throws SQLException {
        var connection = this.dataSource.getConnection();
        connection.createStatement().executeUpdate(query);
        connection.close();
    }

    public PreparedStatement prepareStatement(String query) throws SQLException {
        return this.dataSource.getConnection().prepareStatement(query);
    }

    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }
}
