## ADDED Requirements

### Requirement: RESTful Controller generation
The system SHALL generate Spring Boot Controller code with standard RESTful API endpoints.

#### Scenario: Generate CRUD endpoints
- **WHEN** user initiates controller code generation for a table
- **THEN** system generates create, update, delete, list, getById endpoints

#### Scenario: Generate GET endpoints
- **WHEN** code generation runs
- **THEN** system generates GET /{id} and GET / (list) endpoints

#### Scenario: Generate POST endpoint
- **WHEN** code generation runs
- **THEN** system generates POST / endpoint for entity creation

#### Scenario: Generate PUT endpoint
- **WHEN** code generation runs
- **THEN** system generates PUT /{id} endpoint for entity update

#### Scenario: Generate DELETE endpoint
- **WHEN** code generation runs
- **THEN** system generates DELETE /{id} endpoint for entity deletion