## ADDED Requirements

### Requirement: Domain entity generation

The system SHALL generate Java entity classes with MyBatis Plus annotations based on table metadata.

#### Scenario: Generate entity class
- **WHEN** user triggers backend code generation for a table
- **THEN** system generates domain/<TableName>.java with @TableName, @TableId, @TableField annotations matching table structure

#### Scenario: Map column types to Java types
- **WHEN** system encounters a database column type
- **THEN** system maps it to the appropriate Java type using the type mapping configuration

### Requirement: Mapper interface generation

The system SHALL generate MyBatis Plus Mapper interfaces extending BaseMapper.

#### Scenario: Generate Mapper interface
- **WHEN** user triggers backend code generation for a table
- **THEN** system generates mapper/<TableName>Mapper.java extending BaseMapper<Entity>

### Requirement: Service layer generation

The system SHALL generate Service interfaces and their implementations with common CRUD methods.

#### Scenario: Generate Service interface
- **WHEN** user triggers backend code generation for a table
- **THEN** system generates service/<TableName>Service.java extending IService<Entity>

#### Scenario: Generate Service implementation
- **WHEN** user triggers backend code generation for a table
- **THEN** system generates service/impl/<TableName>ServiceImpl.java extending ServiceImpl with injected Mapper

### Requirement: Controller generation

The system SHALL generate RESTful Controller classes with standard CRUD endpoints.

#### Scenario: Generate Controller
- **WHEN** user triggers backend code generation for a table
- **THEN** system generates controller/<TableName>Controller.java with list, getById, create, update, delete endpoints using @RestController

#### Scenario: Use consistent response wrapper
- **WHEN** Controller endpoints return data
- **THEN** all endpoints use the project's standard response wrapper class

### Requirement: Type mapping configuration

The system SHALL maintain a configurable mapping from MySQL data types to Java types and TypeScript types.

#### Scenario: Map common MySQL types
- **WHEN** system reads a column of type VARCHAR
- **THEN** system maps it to Java String and TypeScript string

#### Scenario: Map numeric types
- **WHEN** system reads a column of type BIGINT
- **THEN** system maps it to Java Long and TypeScript number

#### Scenario: Map date types
- **WHEN** system reads a column of type DATETIME
- **THEN** system maps it to Java LocalDateTime and TypeScript string
