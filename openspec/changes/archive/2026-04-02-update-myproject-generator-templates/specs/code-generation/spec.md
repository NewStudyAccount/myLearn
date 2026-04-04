## ADDED Requirements

### Requirement: Generator produces POST-based Controller
The system SHALL generate Controller classes that use POST method for paginated list queries with request body parameters.

#### Scenario: Generated list endpoint
- **WHEN** generator creates a Controller
- **THEN** the list endpoint uses `@PostMapping("/list")`
- **AND** accepts a request body parameter for query conditions
- **AND** returns `TableDataInfo<${className}>` type

### Requirement: Generator produces Vue page with new Pagination component
The system SHALL generate Vue list pages that use the new Pagination component with nested pageQuery structure.

#### Scenario: Pagination component usage
- **WHEN** generator creates a Vue list page
- **THEN** the page uses `<Pagination>` component (capitalized)
- **AND** passes `:current-page="queryParams.pageQuery.pageNum"`
- **AND** passes `:page-size="queryParams.pageQuery.pageSize"`
- **AND** handles `@current-change` and `@size-change` events

#### Scenario: Response data handling
- **WHEN** list API returns paginated data
- **THEN** page assigns `res.data.rows` to `dataList`
- **AND** assigns `res.data.total` to `total`

### Requirement: Generator produces API with POST list method
The system SHALL generate TypeScript API files that use POST method for list queries with PageQuery interface.

#### Scenario: List API function
- **WHEN** generator creates an API file
- **THEN** the list function uses `method: 'post'`
- **AND** passes `data: params` (not `params`)

#### Scenario: PageQuery interface
- **WHEN** generator creates an API file
- **THEN** the file includes a `PageQuery` interface with `pageNum` and `pageSize`
- **AND** the list params interface includes `pageQuery: PageQuery`
