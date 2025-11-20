USE CASE: 5 Produce a Report on the Salary of Employees of a Given Role
CHARACTERISTIC INFORMATION
Goal in Context

As an HR advisor I want to produce a report on the salary of employees of a given role so that I can support financial reporting of the organisation.

Scope

Company.

Level

Primary task.

Preconditions

We know the role. Database contains current employee salary data.

Success End Condition

A report is available for HR to provide to finance.

Failed End Condition

No report is produced.

Primary Actor

HR Advisor.

Trigger

A request for finance information is sent to HR.

MAIN SUCCESS SCENARIO

Finance request salary information for a given role.

HR advisor captures name of the role to get salary information for.

HR advisor extracts current salary information of all employees of the given role.

HR advisor provides report to finance.

EXTENSIONS

Role does not exist:

HR advisor informs finance no role exists.

SUB-VARIATIONS

None.

SCHEDULE

DUE DATE: Release 1.0