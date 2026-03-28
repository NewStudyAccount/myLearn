## ADDED Requirements

### Requirement: Service layer CRUD generation
The system SHALL generate Spring Service layer code with standard CRUD operations.

#### Scenario: Generate service class
- **WHEN** user initiates service code generation for a table
- **THEN** system generates Service class with @Service annotation

#### Scenario: Generate save method
- **WHEN** code generation runs
- **THEN** system generates save(Entity) method for entity persistence

#### Scenario: Generate update method
- **WHEN** code generation runs
- **THEN** system generates update(Entity) method with ID check

#### Scenario: Generate delete method
- **WHEN** code generation runs
- **THEN** system generates deleteById(Long id) method

#### Scenario: Generate list method
- **WHEN** code generation runs
- **THEN** system generates findAll() method returning List<Entity>

#### Scenario: Generate getById method
- **WHEN** code generation runs
- **THEN** system generates findById(Long id) method with Optional return