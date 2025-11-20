USE CASE: 8 Update an Employee’s Details
CHARACTERISTIC INFORMATION
Goal in Context

As an HR advisor I want to update an employee’s details so that employee records are kept up-to-date.

Scope

Company.

Level

Primary task.

Preconditions

HR has the new data and permission to update employee records.

Success End Condition

Employee records are updated and historical records (title/salary) are maintained properly.

Failed End Condition

Update fails or partial updates leave inconsistent data.

Primary Actor

HR Advisor.

Trigger

A change request (e.g., name change, promotion, salary change) is submitted.

MAIN SUCCESS SCENARIO

HR receives updated data for an employee.

HR updates the employees table for personal fields as needed.

For title/salary changes, HR closes the existing current rows and inserts new current rows.

System confirms successful update and maintains historical records.

EXTENSIONS

Employee not found:

HR is informed and asked to verify the identifier.

Constraint violation (e.g., salary out of bounds):

System rejects change and provides validation feedback.

SUB-VARIATIONS

Minor personal detail updates.

Title or salary change requiring historical closure and insertion.

SCHEDULE

DUE DATE: Release 1.0