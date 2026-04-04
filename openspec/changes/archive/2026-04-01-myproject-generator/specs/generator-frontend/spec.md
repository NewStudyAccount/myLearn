## ADDED Requirements

### Requirement: Vue3 list page generation

The system SHALL generate Vue3 + TypeScript list page components for each selected table, including data table, search form, pagination, and CRUD action buttons.

#### Scenario: Generate list component
- **WHEN** user triggers frontend code generation for a table
- **THEN** system generates a list.vue file containing Element Plus el-table with columns matching table fields, search form with el-form, and pagination component

#### Scenario: Generate with TypeScript types
- **WHEN** system generates the list component
- **THEN** the component uses TypeScript to define table row interface matching the database columns

### Requirement: Vue3 form dialog generation

The system SHALL generate Vue3 form dialog components supporting both create and edit modes with form validation.

#### Scenario: Generate form dialog component
- **WHEN** user triggers frontend code generation for a table
- **THEN** system generates a FormDialog.vue with el-form fields matching table columns, validation rules, and submit logic

#### Scenario: Support edit mode
- **WHEN** FormDialog is opened for editing
- **THEN** the form is pre-populated with existing data passed via props

### Requirement: API client generation

The system SHALL generate TypeScript API client modules with Axios request functions for CRUD operations.

#### Scenario: Generate CRUD API functions
- **WHEN** user triggers frontend code generation for a table
- **THEN** system generates api/<tableName>.ts with list, getById, create, update, delete functions using Axios

### Requirement: Router configuration generation

The system SHALL generate Vue Router configuration entries for each generated page.

#### Scenario: Generate router module
- **WHEN** user triggers frontend code generation for a table
- **THEN** system generates router/modules/<tableName>.ts with route definitions pointing to the generated components
