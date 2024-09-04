package io.github.adigunhammedolalekan.authkit.database;

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

    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }
}
