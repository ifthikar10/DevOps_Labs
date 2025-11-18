package com.napier.devops;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

        a.salaryReportByDepartment("Sales");

        // Test salary report for manager
        a.salaryReportForManager(110039);

        a.salaryReportByRole("Engineer");  // or any real role in your DB

        // pick a new emp_no automatically
        int newEmpNo = a.getNextEmpNo();
        Employee newEmp = new Employee();
        newEmp.emp_no = newEmpNo;
        newEmp.first_name = "Test";
        newEmp.last_name = "Employee";
        newEmp.birth_date = "1995-05-05";   // optional - method supplies default if null
        newEmp.gender = "F";                // optional
        newEmp.hire_date = java.time.LocalDate.now().toString();

        boolean added = a.addEmployee(newEmp, "d000", "Junior Engineer", 42000);
        System.out.println("Add employee result: " + added);
        if (added) {
            // verify by reading back
            Employee got = a.getEmployee(newEmpNo);
            a.displayEmployee(got);
        }

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
     * Salary report for a named department (deptName)
     * Example: "Sales"
     */
    public void salaryReportByDepartment(String deptName) {
        String sql =
                "SELECT COUNT(*) AS cnt, " +
                        "AVG(s.salary) AS avg_salary, " +
                        "MIN(s.salary) AS min_salary, " +
                        "MAX(s.salary) AS max_salary, " +
                        "SUM(s.salary) AS total_salary " +
                        "FROM employees e " +
                        "JOIN dept_emp de ON e.emp_no = de.emp_no AND de.to_date = '9999-01-01' " +
                        "JOIN departments d ON de.dept_no = d.dept_no " +
                        "JOIN salaries s ON e.emp_no = s.emp_no AND s.to_date = '9999-01-01' " +
                        "WHERE d.dept_name = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, deptName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt("cnt") > 0) {
                    System.out.println("=== Salary Report for Department: " + deptName + " ===");
                    System.out.println("Count  : " + rs.getInt("cnt"));
                    System.out.printf("Average: %.2f%n", rs.getDouble("avg_salary"));
                    System.out.println("Min    : " + rs.getInt("min_salary"));
                    System.out.println("Max    : " + rs.getInt("max_salary"));
                    System.out.println("Total  : " + rs.getLong("total_salary"));
                    System.out.println("===============================================");
                } else {
                    System.out.println("No current employees found in department: " + deptName);
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to produce department salary report: " + e.getMessage());
        }
    }

    /**
     * Return list of dept_no that the given emp_no currently manages.
     */
    public List<String> getDepartmentsForManager(int managerEmpNo) {
        List<String> depts = new ArrayList<>();
        String sql = "SELECT dept_no FROM dept_manager WHERE emp_no = ? AND to_date = '9999-01-01'";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, managerEmpNo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    depts.add(rs.getString("dept_no"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to get departments for manager " + managerEmpNo + ": " + e.getMessage());
        }
        return depts;
    }

    /**
     * Salary report for the department(s) managed by the given manager (emp_no).
     */
    public void salaryReportForManager(int managerEmpNo) {
        List<String> depts = getDepartmentsForManager(managerEmpNo);
        if (depts.isEmpty()) {
            System.out.println("No current department manager assignments found for emp_no: " + managerEmpNo);
            return;
        }

        // Build parameterized IN clause
        StringBuilder inClause = new StringBuilder();
        for (int i = 0; i < depts.size(); ++i) {
            inClause.append("?");
            if (i < depts.size() - 1) inClause.append(",");
        }

        String sql = "SELECT COUNT(*) AS cnt, " +
                "AVG(s.salary) AS avg_salary, MIN(s.salary) AS min_salary, " +
                "MAX(s.salary) AS max_salary, SUM(s.salary) AS total_salary " +
                "FROM employees e " +
                "JOIN dept_emp de ON e.emp_no = de.emp_no AND de.to_date = '9999-01-01' " +
                "JOIN salaries s ON e.emp_no = s.emp_no AND s.to_date = '9999-01-01' " +
                "WHERE de.dept_no IN (" + inClause.toString() + ")";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < depts.size(); ++i) {
                ps.setString(i + 1, depts.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt("cnt") > 0) {
                    System.out.println("=== Salary Report for Manager (emp_no=" + managerEmpNo + ") Departments " + depts + " ===");
                    System.out.println("Count  : " + rs.getInt("cnt"));
                    System.out.printf("Average: %.2f%n", rs.getDouble("avg_salary"));
                    System.out.println("Min    : " + rs.getInt("min_salary"));
                    System.out.println("Max    : " + rs.getInt("max_salary"));
                    System.out.println("Total  : " + rs.getLong("total_salary"));
                    System.out.println("================================================================");
                } else {
                    System.out.println("No current employees found in manager's department(s): " + depts);
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to produce manager's department salary report: " + e.getMessage());
        }
    }
    /**
     * Salary report for employees of a given role (title)
     */
    public void salaryReportByRole(String role) {
        String sql =
                "SELECT COUNT(*) AS cnt, " +
                        "AVG(s.salary) AS avg_salary, " +
                        "MIN(s.salary) AS min_salary, " +
                        "MAX(s.salary) AS max_salary, " +
                        "SUM(s.salary) AS total_salary " +
                        "FROM employees e " +
                        "JOIN titles t ON e.emp_no = t.emp_no AND t.to_date = '9999-01-01' " +
                        "JOIN salaries s ON e.emp_no = s.emp_no AND s.to_date = '9999-01-01' " +
                        "WHERE t.title = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, role);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt("cnt") > 0) {
                    System.out.println("=== Salary Report for Role: " + role + " ===");
                    System.out.println("Count  : " + rs.getInt("cnt"));
                    System.out.printf("Average: %.2f%n", rs.getDouble("avg_salary"));
                    System.out.println("Min    : " + rs.getInt("min_salary"));
                    System.out.println("Max    : " + rs.getInt("max_salary"));
                    System.out.println("Total  : " + rs.getLong("total_salary"));
                    System.out.println("==============================================");
                } else {
                    System.out.println("No current employees found for role: " + role);
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to produce role salary report: " + e.getMessage());
        }
    }
    /**
     * Get a numeric emp_no candidate (max(emp_no) + 1).
     * Simple helper to choose a non-colliding emp_no for tests.
     */
    public int getNextEmpNo() {
        String sql = "SELECT MAX(emp_no) AS max_emp FROM employees";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int max = rs.getInt("max_emp");
                return max + 1;
            } else {
                // fallback if table empty
                return 100000;
            }
        } catch (SQLException e) {
            System.out.println("Failed to get next emp_no: " + e.getMessage());
            // pick a safe default
            return 100000;
        }
    }

    /**
     * Add a new employee and optional title, salary, dept assignment.
     * Returns true on success, false on failure.
     *
     * NOTE: employees table in the classic dataset requires birth_date, gender, hire_date.
     * This method uses defaults when those fields are null on the Employee object.
     */
    public boolean addEmployee(Employee emp, String deptNo, String title, int salary) {
        String insertEmp = "INSERT INTO employees (emp_no, birth_date, first_name, last_name, gender, hire_date) VALUES (?, ?, ?, ?, ?, ?)";
        String insertTitle = "INSERT INTO titles (emp_no, title, from_date, to_date) VALUES (?, ?, CURDATE(), '9999-01-01')";
        String insertSalary = "INSERT INTO salaries (emp_no, salary, from_date, to_date) VALUES (?, ?, CURDATE(), '9999-01-01')";
        String insertDeptEmp = "INSERT INTO dept_emp (emp_no, dept_no, from_date, to_date) VALUES (?, ?, CURDATE(), '9999-01-01')";

        try {
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(insertEmp)) {
                ps.setInt(1, emp.emp_no);
                ps.setString(2, emp.birth_date != null ? emp.birth_date : "1990-01-01");
                ps.setString(3, emp.first_name);
                ps.setString(4, emp.last_name);
                ps.setString(5, emp.gender != null ? emp.gender : "M");
                ps.setString(6, emp.hire_date != null ? emp.hire_date : java.time.LocalDate.now().toString());
                ps.executeUpdate();
            }

            if (title != null && !title.isEmpty()) {
                try (PreparedStatement ps = con.prepareStatement(insertTitle)) {
                    ps.setInt(1, emp.emp_no);
                    ps.setString(2, title);
                    ps.executeUpdate();
                }
            }

            if (salary > 0) {
                try (PreparedStatement ps = con.prepareStatement(insertSalary)) {
                    ps.setInt(1, emp.emp_no);
                    ps.setInt(2, salary);
                    ps.executeUpdate();
                }
            }

            if (deptNo != null && !deptNo.isEmpty()) {
                try (PreparedStatement ps = con.prepareStatement(insertDeptEmp)) {
                    ps.setInt(1, emp.emp_no);
                    ps.setString(2, deptNo);
                    ps.executeUpdate();
                }
            }

            con.commit();
            con.setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ex) { /* ignore */ }
            System.out.println("Failed to add employee: " + e.getMessage());
            try { con.setAutoCommit(true); } catch (SQLException ex) {}
            return false;
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