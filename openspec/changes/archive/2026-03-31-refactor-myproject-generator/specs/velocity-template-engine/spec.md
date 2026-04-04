## ADDED Requirements

### Requirement: Template Engine Loads .vm Files
The template engine SHALL load Velocity template files (.vm) from the classpath resources directory.

#### Scenario: Load template from classpath
- **WHEN** generator requests a template by name (e.g., "templates/entity.vm")
- **THEN** engine loads the template from classpath and renders it with provided context

#### Scenario: Template not found
- **WHEN** requested template does not exist in classpath
- **THEN** engine throws RuntimeException with clear error message

### Requirement: Template Engine Renders Context
The template engine SHALL render templates with a map of context variables.

#### Scenario: Render with context
- **WHEN** engine renders template with context containing table metadata
- **THEN** output contains substituted values for all context variables

#### Scenario: Nested context objects
- **WHEN** context contains nested objects (e.g., table.columns)
- **THEN** template can access nested properties using dot notation

### Requirement: Template Engine Supports Common Directives
The template engine SHALL support Velocity directives including #foreach, #if, #set.

#### Scenario: Foreach directive
- **WHEN** template uses #foreach to iterate over columns
- **THEN** engine generates correct iteration output

#### Scenario: If directive
- **WHEN** template uses #if for conditional logic
- **THEN** engine correctly evaluates conditions
