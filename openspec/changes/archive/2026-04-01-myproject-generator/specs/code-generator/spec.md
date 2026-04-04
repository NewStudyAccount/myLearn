## ADDED Requirements

### Requirement: Table metadata reading

The system SHALL read database table metadata through JDBC connection to information_schema, including table name, column name, column type, column comment, primary key, and index information.

#### Scenario: Read single table metadata
- **WHEN** user selects a table for code generation
- **THEN** system queries information_schema and returns complete column definitions including name, type, nullable, default value, and comments

#### Scenario: Read multiple tables metadata
- **WHEN** user selects multiple tables for batch generation
- **THEN** system queries and returns metadata for all selected tables

### Requirement: Code template engine

The system SHALL use Apache Velocity as the template engine to generate code files from metadata and templates.

#### Scenario: Render template with metadata
- **WHEN** system receives table metadata and a template
- **THEN** system renders the template and produces output code text

#### Scenario: Support variable substitution
- **WHEN** template contains variables like ${className}, ${tableName}, ${columns}
- **THEN** system substitutes variables with actual values from metadata

### Requirement: Generation configuration

The system SHALL support configurable generation settings including package name, table prefix filter, and output path.

#### Scenario: Configure package name
- **WHEN** user specifies a target package name
- **THEN** all generated Java files use the specified package

#### Scenario: Filter by table prefix
- **WHEN** user specifies a table prefix to ignore
- **THEN** system removes the prefix from generated class names

### Requirement: Code preview

The system SHALL provide a preview API that returns generated code content without writing files.

#### Scenario: Preview single table code
- **WHEN** user requests preview for a table
- **THEN** system returns generated code content for frontend and backend

### Requirement: Code download

The system SHALL package generated code files and provide a downloadable ZIP archive.

#### Scenario: Download generated code
- **WHEN** user clicks download after generation
- **THEN** system creates a ZIP file containing all generated code and sends it to the client
