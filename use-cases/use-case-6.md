USE CASE: 6 Add a New Employee
CHARACTERISTIC INFORMATION
Goal in Context

As an HR advisor I want to add a new employee’s details so that I can ensure the new employee is paid and tracked by the organisation.

Scope

Company.

Level

Primary task.

Preconditions

HR has the required employee information (emp_no, name, birth_date, gender, hire_date) and appropriate permissions.

Success End Condition

A new employee row is created (and optionally title/salary/dept rows).

Failed End Condition

Employee is not created.

Primary Actor

HR Advisor.

Trigger

A new hire is onboarded.

MAIN SUCCESS SCENARIO

HR collects required personal and employment data for the new hire.

HR invokes the add-employee process.

System inserts a new employees row (and optionally current title, salary, department).

System confirms insertion and HR verifies data.

EXTENSIONS

Emp_no conflict (duplicate):

HR selects or is assigned a different emp_no.

Retry insert.

Missing required data:

System rejects insertion and requests missing fields.

SUB-VARIATIONS

Insert only employees row (minimal).

Insert employees + titles + salaries + dept_emp (complete onboarding).

SCHEDULE

DUE DATE: Release 1.0