## ADDED Requirements

### Requirement: Database table structure reading
The system SHALL read database table schema information including table name, column name, data type, is nullable, column comment, and primary key.

#### Scenario: Read table structure
- **WHEN** user selects a database table for code generation
- **THEN** system fetches table metadata via JDBC and parses column information

### Requirement: Template engine integration
The system SHALL use Velocity template engine to render code from configured templates.

#### Scenario: Render template with data
- **WHEN** template engine receives table metadata and template file
- **THEN** system generates code content by merging data with template

### Requirement: Field type mapping
The system SHALL convert database column types to corresponding Java types automatically.

#### Scenario: Map VARCHAR to String
- **WHEN** system processes a VARCHAR column
- **THEN** system maps it to Java String type in generated entity

#### Scenario: Map INT to Integer
- **WHEN** system processes an INT column
- **THEN** system maps it to Java Integer type in generated entity

#### Scenario: Map DATETIME to LocalDateTime
- **WHEN** system processes a DATETIME column
- **THEN** system maps it to Java LocalDateTime type in generated entity

### Requirement: Code output to file
The system SHALL write generated code to specified output directory.

#### Scenario: Write generated code
- **WHEN** code generation completes
- **THEN** system saves files to configured output path with proper package structure