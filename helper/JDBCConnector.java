package com.mc.helper;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Abstract class that provides methods to establish and close connections with a MySQL database.
 * @author Michael Cassidy
 */
public abstract class JDBCConnector {
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jbdcUrl = protocol + vendor + location + databaseName + "?serverTimeZone=UTC";
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String userName = "sqlUser";
    private static final String password = "Passw0rd!";
    public static Connection connection;

    /**
     * Opens a connection to the MySQL database.
     * If successful, the connection is stored in the public static variable 'connection'.
     * In case of an error, the error message is printed to the console.
     */
    public static void openConnection() {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(jbdcUrl, userName, password);
            System.out.println("Database Connection Successful");
        } catch(Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Closes the connection to the MySQL database.
     * If successful, a message is printed to the console.
     * In case of an error, the error message is printed to the console.
     */
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Database Connection Closed");
        } catch(Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
