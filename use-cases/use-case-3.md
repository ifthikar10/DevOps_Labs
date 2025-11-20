USE CASE: 3 Produce a Report on the Salary of Employees in a Department
CHARACTERISTIC INFORMATION
Goal in Context

As an HR advisor I want to produce a report on the salary of employees in a department so that I can support financial reporting of the organisation.

Scope

Company / Department.

Level

Primary task.

Preconditions

We know the department (name or code). Database contains current salary and dept assignment rows.

Success End Condition

A department-level salary report is available.

Failed End Condition

No report is produced.

Primary Actor

HR Advisor.

Trigger

Finance or department manager requests salary information for a department.

MAIN SUCCESS SCENARIO

Request is received specifying the department.

HR queries current employees assigned to that department and their current salaries.

System computes count, average, min, max, total for that department.

HR provides the report to requestor.

EXTENSIONS

Department not found:

Inform requestor that the department is invalid.

Department has no current employees:

Inform requestor no current employees are assigned.

SUB-VARIATIONS

None.

SCHEDULE

DUE DATE: Release 1.0