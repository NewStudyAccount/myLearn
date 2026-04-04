## ADDED Requirements

### Requirement: TypeScript API Generation
The system SHALL generate TypeScript API functions for CRUD operations.

#### Scenario: Generate API with all functions
- **WHEN** generating API for a table
- **THEN** API contains getList, getById, create, update, delete functions

#### Scenario: Generate API with proper types
- **WHEN** generating API
- **THEN** each function has proper TypeScript types for parameters and return values

#### Scenario: Long type handling
- **WHEN** entity has Long primary key
- **THEN** API uses string type to avoid precision loss

### Requirement: Vue Page Generation
The system SHALL generate Vue 3 page components with complete CRUD UI.

#### Scenario: Generate list with search
- **WHEN** generating Vue page
- **THEN** page includes search form with filterable fields

#### Scenario: Generate create dialog
- **WHEN** generating Vue page
- **THEN** page includes create dialog with form fields

#### Scenario: Generate edit dialog
- **WHEN** generating Vue page
- **THEN** page includes edit dialog pre-populated with row data

#### Scenario: Generate view dialog
- **WHEN** generating Vue page
- **THEN** page includes view dialog showing all field values

#### Scenario: Generate delete functionality
- **WHEN** generating Vue page
- **THEN** page includes delete with confirmation dialog

#### Scenario: Generate pagination
- **WHEN** generating Vue page
- **THEN** page includes pagination component linked to list query

### Requirement: Element Plus Integration
The system SHALL generate Vue pages using Element Plus components.

#### Scenario: Use Element Plus components
- **WHEN** generating Vue page
- **THEN** page uses el-card, el-table, el-form, el-dialog, el-button components

#### Scenario: Use Icons
- **WHEN** generating Vue page
- **THEN** page uses @iconify/vue for icons
