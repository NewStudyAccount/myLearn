## ADDED Requirements

### Requirement: Entity Generation
The system SHALL generate Entity classes with MyBatis-Plus annotations based on table metadata.

#### Scenario: Generate entity with all columns
- **WHEN** table has multiple columns including primary key
- **THEN** generated entity contains all fields with correct @TableField annotations

#### Scenario: Generate entity with auto-increment primary key
- **WHEN** table has auto-increment primary key
- **THEN** generated entity uses @TableId(type = IdType.AUTO)

### Requirement: Mapper Generation
The system SHALL generate Mapper interfaces extending BaseMapper.

#### Scenario: Generate mapper interface
- **WHEN** generating mapper for a table
- **THEN** mapper extends BaseMapper<Entity> with @Mapper annotation

### Requirement: Mapper XML Generation
The system SHALL generate MyBatis XML with CRUD operations.

#### Scenario: Generate XML with CRUD
- **WHEN** generating mapper XML
- **THEN** XML contains selectById, insert, updateById, deleteById operations

### Requirement: Service Generation
The system SHALL generate Service interface and implementation.

#### Scenario: Generate service interface
- **WHEN** generating service for a table
- **THEN** service interface extends IService<Entity>

#### Scenario: Generate service implementation
- **WHEN** generating service implementation
- **THEN** implementation extends ServiceImpl<Mapper, Entity> with @Service

### Requirement: Controller Generation
The system SHALL generate REST Controller with CRUD endpoints.

#### Scenario: Generate controller with all endpoints
- **WHEN** generating controller
- **THEN** controller provides GET /list, GET /{id}, POST, PUT, DELETE /{id} endpoints

### Requirement: VO Generation
The system SHALL generate Response VO classes for API responses.

#### Scenario: Generate VO
- **WHEN** generating VO for a table
- **THEN** VO contains all fields with appropriate TypeScript type mappings

### Requirement: Req Generation
The system SHALL generate Request DTO classes for API requests.

#### Scenario: Generate request DTO
- **WHEN** generating request DTO
- **THEN** DTO contains fields matching table columns except auto-managed fields
