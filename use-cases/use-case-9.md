USE CASE: 9 Delete an Employee’s Details
CHARACTERISTIC INFORMATION
Goal in Context

As an HR advisor I want to delete an employee’s details so that the company is compliant with data retention legislation or policy.

Scope

Company.

Level

Primary task.

Preconditions

HR has authority and legal basis to delete the record.

Success End Condition

Employee data is removed or anonymized per policy.

Failed End Condition

Deletion is not performed.

Primary Actor

HR Advisor.

Trigger

A retention policy expiry or deletion request is received.

MAIN SUCCESS SCENARIO

HR confirms deletion request and legal basis.

HR chooses deletion mode (hard delete or soft-delete/anonymize).

System performs deletion or anonymization across related tables (titles, salaries, dept_emp, dept_manager) or anonymizes the employees row and closes current related rows.

System logs the action for audit (if required).

EXTENSIONS

Legal hold exists:

System prevents deletion and notifies HR.

Related foreign-key constraints:

System performs transactional deletes in the correct order or fails and notifies HR.

SUB-VARIATIONS

Hard delete (irreversible removal).

Soft delete (anonymization + mark deleted_at).

SCHEDULE

DUE DATE: Release 1.0