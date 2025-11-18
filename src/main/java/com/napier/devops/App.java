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

        // Get Employee
        Employee emp = a.getEmployee(255530);
        // Display Result
        a.displayEmployee(emp);

        a.salaryReportAll();

        // Disconnect from database
        a.disconnect();
    }

    /**
     * Get employee by ID from database
     */
    public Employee getEmployee(int ID)
    {
        try
        {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT emp_no, first_name, last_name "
                            + "FROM employees "
                            + "WHERE emp_no = " + ID;
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Return new employee if valid.
            // Check one is returned
            if (rset.next())
            {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("emp_no");
                emp.first_name = rset.getString("first_name");
                emp.last_name = rset.getString("last_name");
                return emp;
            }
            else
                return null;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Failed to get employee details");
            return null;
        }
    }

    public void displayEmployee(Employee emp)
    {
        if (emp != null)
        {
            System.out.println(
                    emp.emp_no + " "
                            + emp.first_name + " "
                            + emp.last_name + "\n"
                            + emp.title + "\n"
                            + "Salary:" + emp.salary + "\n"
                            + emp.dept_name + "\n"
                            + "Manager: " + emp.manager + "\n");
        }
    }

    /**
     * Salary report for all current employees
     */
    public void salaryReportAll() {
        String sql = "SELECT COUNT(*) AS cnt, " +
                "AVG(salary) AS avg_salary, " +
                "MIN(salary) AS min_salary, " +
                "MAX(salary) AS max_salary, " +
                "SUM(salary) AS total_salary " +
                "FROM salaries " +
                "WHERE to_date = '9999-01-01'";

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                System.out.println("=== All Employees Salary Report ===");
                System.out.println("Count  : " + rs.getInt("cnt"));
                System.out.printf("Average: %.2f%n", rs.getDouble("avg_salary"));
                System.out.println("Min    : " + rs.getInt("min_salary"));
                System.out.println("Max    : " + rs.getInt("max_salary"));
                System.out.println("Total  : " + rs.getLong("total_salary"));
                System.out.println("===================================");
            } else {
                System.out.println("No salary data found.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to produce salary report: " + e.getMessage());
        }
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