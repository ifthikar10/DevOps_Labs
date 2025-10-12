package com.napier.devops;

import java.sql.*;

public class App {
    private Connection con = null;

    /**
     * Connects to the database.
     */
    public void connect() {
        try {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 100;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                // Wait a bit for db to start
                Thread.sleep(30000);
                // Connect to database
                con = DriverManager.getConnection(
                        "jdbc:mysql://db:3306/employees?allowPublicKeyRetrieval=true&useSSL=false",
                        "root",
                        "example"
                );
                System.out.println("Successfully connected");
                // Wait a bit
                Thread.sleep(10000);
                break;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " + i);
                System.out.println(sqle.getMessage());
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /**
     * Disconnects from the database.
     */
    public void disconnect() {
        if (con != null) {
            try {
                con.close();
                System.out.println("Disconnected from database.");
            } catch (Exception e) {
                System.out.println("Error closing connection to database");
            }
        }
    }

    public static void main(String[] args) {
        // Create new Application
        App a = new App();

        // Connect to database
        a.connect();

        // Disconnect from database
        a.disconnect();
    }
}