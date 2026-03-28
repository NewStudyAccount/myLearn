## ADDED Requirements

### Requirement: Entity class generation
The system SHALL generate Java Entity classes with JPA annotations based on database table structure.

#### Scenario: Generate Entity with Table annotation
- **WHEN** user initiates entity code generation for a table
- **THEN** system generates @Entity and @Table(name="table_name") annotated class

#### Scenario: Generate field declarations
- **WHEN** code generation runs
- **THEN** system generates private field for each column with proper Java type

#### Scenario: Generate Column annotations
- **WHEN** code generation runs
- **THEN** system generates @Column annotation with name, nullable attributes

#### Scenario: Generate primary key field
- **WHEN** table has primary key column
- **THEN** system generates @Id and @GeneratedValue for the primary key

#### Scenario: Generate getters and setters
- **WHEN** code generation runs
- **THEN** system generates standard getter/setter methods for all fields

#### Scenario: Generate field comments
- **WHEN** column has comment in database
- **THEN** system generates @Column(comment="...") with the comment text