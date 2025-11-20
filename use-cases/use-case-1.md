USE CASE: 1 View an employee’s profile details
CHARACTERISTIC INFORMATION
Goal in Context

As an HR advisor I want to view an employee’s profile details (personal and employment info) so that I can check identity, contact info and current employment status.

Scope

Company.

Level

Primary task.

Preconditions

Employee record exists in the database.

Success End Condition

HR can see the employee’s profile (name, contact, hire date, current job title, current department).

Failed End Condition

Profile is not displayed.

Primary Actor

HR Advisor.

Trigger

HR requests to view an employee’s profile.

MAIN SUCCESS SCENARIO

HR requests an employee profile (by employee number or search).

System retrieves employee row from employees.

System aggregates current title, department and basic employment fields.

System displays the profile to HR.

EXTENSIONS

Employee does not exist:

Inform HR the employee does not exist.

SUB-VARIATIONS

None.

SCHEDULE

DUE DATE: Release 1.0