package com.pt.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Singleton Factory class responsible for managing database connection lifecycles.
 * It centralizes connection parameters, handles active driver lookups, and encapsulates clean resource disposal workflows.
 */

public class ConnectionFactory {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=OrdersManagement;encrypt=true;trustServerCertificate=true;";
    private static final String USER = "orders_user";
    private static final String PASSWORD = "Orders123!";

    /**
     * Private constructor to prevent direct external instantiation of this utility factory class.
     */
    private ConnectionFactory() {
    }

    /**
     * Attempts to establish a new, active state transaction connection channel pointing to the configured relational Microsoft SQL Server database.
     *
     * @return an active operational {@link Connection} database object instance, or {@code null} if an unhandled connection SQL failure occurs
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Safely disposes and safely closes an active open JDBC database Connection resource.
     * Suppresses prospective target internal resource failures silently and handles defensive pre-null structural checks.
     *
     * @param connection the target relational JDBC {@link Connection} instance layer targeted for closure action
     */
    public static void close(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Safely closes an active operational compilation JDBC Statement execution resource container.
     *
     * @param statement the transactional compiled execution {@link Statement} resource targeted for closure action
     */
    public static void close(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Safely closes an active active JDBC relational query evaluation ResultSet container resource.
     *
     * @param resultSet the structural relational data query collection matrix {@link ResultSet} resource targeted for closure action
     */
    public static void close(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}