## 1. Core Module Setup

- [x] 1.1 Create myproject-generator module structure with Maven/Gradle config
- [x] 1.2 Add Velocity template engine dependency
- [x] 1.3 Configure dataSource for table metadata reading
- [x] 1.4 Create GeneratorProperties configuration class
- [x] 1.5 Setup logging and exception handling

## 2. Database Table Reading (code-generator)

- [x] 2.1 Implement TableMeta-reader using JdbcTemplate
- [x] 2.2 Implement column type to Java type mapping service
- [x] 2.3 Create TableInfo DTO with all metadata fields
- [x] 2.4 Add support for primary key detection
- [x] 2.5 Add field comment extraction from database

## 3. Template Engine

- [x] 3.1 Initialize Velocity engine with project encoding
- [x] 3.2 Create default templates for entity, mapper, service, controller
- [x] 3.3 Create default frontend templates for Vue and API service
- [x] 3.4 Implement template rendering service
- [x] 3.5 Add custom type mapping configuration support

## 4. Entity Generation

- [x] 4.1 Create entity template with JPA annotations
- [x] 4.2 Implement EntityGenerator with JavaType mapping
- [x] 4.3 Generate @Table annotation with table name
- [x] 4.4 Generate @Column annotations and field comments
- [x] 4.5 Generate getters/setters using reflection or toString

## 5. Mapper Generation

- [x] 5.1 Create MyBatis Mapper interface template
- [x] 5.2 Create MyBatis XML mapping template
- [x] 5.3 Implement MapperGenerator for interface generation
- [x] 5.4 Implement MapperXmlGenerator for XML generation
- [x] 5.5 Generate CRUD SQL statements with proper type handling

## 6. Service Generation

- [x] 6.1 Create Service interface and implementation templates
- [x] 6.2 Implement ServiceGenerator with @Service annotation
- [x] 6.3 Generate CRUD methods (save, update, deleteById, findAll, findById)
- [ ] 6.4 Add transactional annotation support
- [x] 6.5 Integrate with generated Mapper

## 7. Controller Generation

- [x] 7.1 Create RESTful Controller template
- [x] 7.2 Implement ControllerGenerator with @RestController
- [x] 7.3 Generate standard CRUD endpoints (/, /{id})
- [x] 7.4 Add @RequestMapping with base path configuration
- [x] 7.5 Generate method-level mappings (GET, POST, PUT, DELETE)

## 8. Frontend API Generation

- [x] 8.1 Create TypeScript API service template
- [x] 8.2 Implement ApiGenerator for TypeScript service
- [x] 8.3 Generate CRUD methods with Axios calls
- [x] 8.4 Add pagination and query params support
- [ ] 8.5 Export typed interfaces for entities

## 9. Frontend Vue Page Generation

- [x] 9.1 Create Vue 3 component template with Element Plus
- [x] 9.2 Implement VueGenerator for list page generation
- [x] 9.3 Generate table with dynamic columns from table metadata
- [x] 9.4 Generate Add/Edit dialog forms with validation
- [x] 9.5 Generate detail view component
- [x] 9.6 Add search form and pagination controls

## 10. Integration & Testing

- [x] 10.1 Create GeneratorController for web UI access
- [x] 10.2 Add table selection endpoint returning available tables
- [x] 10.3 Implement code preview functionality
- [ ] 10.4 Add download/zip output for generated files
- [ ] 10.5 Create basic module test with mock database