## ADDED Requirements

### Requirement: ZIP file download
The system SHALL generate and download code as a ZIP file containing all source files.

#### Scenario: Download all generated code
- **WHEN** user requests download of generated code
- **THEN** system creates a ZIP file containing all generated source files with proper package structure

### Requirement: Selective download
The system SHALL allow users to select which parts to download (backend only, frontend only, or all).

#### Scenario: Download backend code only
- **WHEN** user selects backend only option
- **THEN** ZIP contains Entity, Mapper, Service, Controller files

#### Scenario: Download frontend code only
- **WHEN** user selects frontend only option
- **THEN** ZIP contains Vue component and API service files

### Requirement: Correct directory structure
The system SHALL organize downloaded files in proper package structure.

#### Scenario: Backend file structure
- **WHEN** backend files are packaged
- **THEN** files are organized under com/example/{domain,mapper,service,controller}/

### Requirement: Single file preview
The system SHALL allow user to preview and download individual generated files.

#### Scenario: Preview entity file
- **WHEN** user clicks preview on entity file
- **THEN** system displays file content in modal dialog