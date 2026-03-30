## ADDED Requirements

### Requirement: MyBatis Mapper generation
The system SHALL generate MyBatis Mapper interface and XML mapping files.

#### Scenario: Generate Mapper interface
- **WHEN** user initiates mapper code generation for a table
- **THEN** system generates Mapper interface with @Mapper annotation

#### Scenario: Generate Mapper XML
- **WHEN** code generation runs
- **THEN** system generates MyBatis XML with CRUD statements

#### Scenario: Generate SELECT statements
- **WHEN** mapper XML generation runs
- **THEN** system generates findById and findAll SELECT statements

#### Scenario: Generate INSERT statement
- **WHEN** mapper XML generation runs
- **THEN** system generates insert statement with useGeneratedKeys

#### Scenario: Generate UPDATE statement
- **WHEN** mapper XML generation runs
- **THEN** system generates update statement for all non-primary fields

#### Scenario: Generate DELETE statement
- **WHEN** mapper XML generation runs
- **THEN** system generates deleteById statement