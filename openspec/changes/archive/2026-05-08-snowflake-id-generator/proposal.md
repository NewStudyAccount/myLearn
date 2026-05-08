## Why

项目当前缺少分布式唯一ID生成方案，在多实例部署或分库分表场景下，数据库自增ID无法保证全局唯一性。雪花算法（Snowflake）是一种轻量级、高性能的分布式ID生成方案，无需依赖外部服务即可生成趋势递增的唯一ID，适合当前项目的技术栈和部署需求。

## What Changes

- 在 `myproject-framework` 模块的 `com.example.utils` 包下新增 `SnowflakeIdUtil` 工具类
- 实现基于雪花算法的唯一ID生成，支持自定义数据中心ID和机器ID
- 提供简单的静态方法调用接口，便于业务层直接使用

## Capabilities

### New Capabilities
- `snowflake-id`: 雪花算法唯一ID生成能力，包含ID生成核心逻辑、配置支持和工具类封装

### Modified Capabilities
（无）

## Impact

- 新增代码：`myproject-framework/src/main/java/com/example/utils/SnowflakeIdUtil.java`
- 无API变更，无数据库变更，无依赖变更
- 业务模块可通过引入 framework 模块直接使用该工具类
