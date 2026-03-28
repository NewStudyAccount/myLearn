## ADDED Requirements

### Requirement: Frontend API encapsulation generation
The system SHALL generate TypeScript API service files with Axel thed Axios calls.

#### Scenario: Generate API service class
- **WHEN** user initiates API code generation for a table
- **THEN** system generates TypeScript service class with CRUD methods

#### Scenario: Generate list API method
- **WHEN** code generation runs
- **THEN** system generates getList(params) method with pagination support

#### Scenario: Generate getById API method
- **WHEN** code generation runs
- **THEN** system generates getById(id) method returning single entity

#### Scenario: Generate create API method
- **WHEN** code generation runs
- **THEN** system generates create(data) method with POST request

#### Scenario: Generate update API method
- **WHEN** code generation runs
- **THEN** system generates update(id, data) method with PUT request

#### Scenario: Generate delete API method
- **WHEN** code generation runs
- **THEN** system generates delete(id) method with DELETE request

#### Scenario: Generate export API method
- **WHEN** code generation runs
- **THEN** system generates export method for data export functionality