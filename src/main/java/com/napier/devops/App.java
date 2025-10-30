package com.napier.devops;
import java.sql.*;
public class App
{
    /**
     * Connection to MySQL database.
     */
    private Connection con = null;

    /**
     * Main method - entry point of the application.
     */
    public static void main(String[] args)
    {
        // Create new Application
        App a = new App();

        // Connect to database
        a.connect();

        // Disconnect from database
        a.disconnect();
    }

    /**
     * Connect to the MySQL database.
     */
    public void connect()
    {
        try
        {
            // Load MySQL Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            // Driver class not found
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        // Number of connection attempts
        int retries = 10;
        for (int i = 0; i < retries; ++i)
        {
            System.out.println("Connecting to database...");
            try
            {
                // Wait for the database to be ready
                Thread.sleep(30000);

                // Attempt to connect to the database
                con = DriverManager.getConnection(
                        "jdbc:mysql://db:3306/employees?allowPublicKeyRetrieval=true&useSSL=false",
                        "root",
                        "example"
                );

                // Connection successful
                System.out.println("Successfully connected");
                break;
            }
            catch (SQLException sqle)
            {
                // SQL error during connection
                System.out.println("Failed to connect to database attempt " + Integer.toString(i));
                System.out.println(sqle.getMessage());
            }
            catch (InterruptedException ie)
            {
                // Thread interruption error
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
    public void disconnect()
    {
        if (con != null)
        {
            try
            {
                // Close the database connection
                con.close();
            }
            catch (Exception e)
            {
                // Error while closing connection
                System.out.println("Error closing connection to database");
            }
        }
    }
}