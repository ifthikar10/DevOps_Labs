USE CASE: 2 Produce a Report on the Salary of All Employees
CHARACTERISTIC INFORMATION
Goal in Context

As an HR advisor I want to produce a report on the salary of all employees so that I can support financial reporting of the organisation.

Scope

Company.

Level

Primary task.

Preconditions

Database contains current salary rows.

Success End Condition

A company-wide salary report (count, average, min, max, total) is produced.

Failed End Condition

Report is not produced.

Primary Actor

HR Advisor.

Trigger

Finance requests aggregate salary information.

MAIN SUCCESS SCENARIO

Finance requests company-wide salary aggregates.

HR runs report that reads current salary rows.

System computes count, average, min, max, total.

HR provides the report to finance.

EXTENSIONS

No salary data:

Inform finance that no current salary data exists.

SUB-VARIATIONS

None.

SCHEDULE

DUE DATE: Release 1.0