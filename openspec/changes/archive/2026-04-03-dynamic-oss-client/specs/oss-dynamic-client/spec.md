## ADDED Requirements

### Requirement: Configuration Database Storage
The system SHALL store OSS configuration in the `sys_oss_config` database table with the following required fields: `config_name` (unique identifier), `provider` (cloud provider type), `endpoint`, `access_key`, `secret_key`, and `bucket_name`. Optional fields SHALL include `region`, `extra_config` (JSON format), `is_active`, `created_at`, and `updated_at`.

#### Scenario: Configuration table exists
- **WHEN** system starts
- **THEN** system verifies that `sys_oss_config` table exists with required schema

#### Scenario: Configuration record creation
- **WHEN** administrator creates a new OSS configuration record
- **THEN** system validates required fields and stores configuration in database

### Requirement: Dynamic Configuration Loading
The system SHALL load OSS configuration from database on demand and cache it for performance. The system SHALL support multiple active configurations simultaneously.

#### Scenario: Load configuration by name
- **WHEN** application requests OSS client for specific configuration name
- **THEN** system retrieves configuration from database (or cache) and returns it

#### Scenario: Load all active configurations
- **WHEN** system needs to list available configurations
- **THEN** system returns all configurations where `is_active` is true

### Requirement: Multi-Provider Client Factory
The system SHALL create appropriate OSS client instances based on the `provider` field value. The system SHALL support at least `aliyun` (Alibaba Cloud OSS) and `minio` providers.

#### Scenario: Create Aliyun OSS client
- **WHEN** configuration has `provider` = "aliyun"
- **THEN** system creates and returns Aliyun OSS client instance with provided credentials

#### Scenario: Create MinIO client
- **WHEN** configuration has `provider` = "minio"
- **THEN** system creates and returns MinIO client instance with provided credentials

#### Scenario: Unsupported provider
- **WHEN** configuration has unsupported `provider` value
- **THEN** system throws descriptive error indicating unsupported provider type

### Requirement: Configuration Caching
The system SHALL cache configuration data to minimize database queries. Cache SHALL have configurable TTL (Time-To-Live) with default of 5 minutes.

#### Scenario: Cache hit
- **WHEN** configuration is requested and exists in cache with valid TTL
- **THEN** system returns cached configuration without database query

#### Scenario: Cache miss
- **WHEN** configuration is requested and not in cache or cache expired
- **THEN** system queries database, updates cache, and returns configuration

#### Scenario: Cache invalidation
- **WHEN** configuration is updated in database
- **THEN** system invalidates cache entry for that configuration

### Requirement: Configuration Refresh Mechanism
The system SHALL provide manual cache refresh capability. The system MAY provide automatic refresh at configurable intervals.

#### Scenario: Manual refresh
- **WHEN** administrator triggers cache refresh
- **THEN** system clears cache and reloads all active configurations from database

#### Scenario: Automatic refresh (if implemented)
- **WHEN** configured refresh interval elapses
- **THEN** system refreshes cache for all active configurations

### Requirement: Backward Compatibility
The system SHALL maintain existing OSS client interfaces while transitioning to dynamic configuration. Existing code using static configuration SHALL continue to work during migration period.

#### Scenario: Legacy client access
- **WHEN** existing code requests OSS client using old method
- **THEN** system returns client using dynamic configuration (with fallback to static if needed)

### Requirement: Error Handling and Validation
The system SHALL validate configuration data and provide clear error messages for invalid configurations.

#### Scenario: Invalid configuration format
- **WHEN** database contains malformed configuration (missing required fields, invalid JSON)
- **THEN** system logs error and either uses default values or throws descriptive exception

#### Scenario: Connection failure
- **WHEN** OSS client cannot connect using provided configuration
- **THEN** system logs error and throws appropriate exception with troubleshooting information