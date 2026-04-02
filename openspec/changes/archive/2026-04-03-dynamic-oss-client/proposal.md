## Why

当前myproject-oss模块需要硬编码或静态配置OSS连接信息，无法动态支持多个云存储提供商（如阿里云、MinIO）或在运行时更新配置。这限制了系统的灵活性和可维护性。通过从数据库表`sys_oss_config`读取配置并动态生成OSS客户端，可以实现配置的集中管理、多环境支持以及无需重启的配置更新。

## What Changes

- 修改myproject-oss模块，使其从数据库表`sys_oss_config`读取OSS配置
- 支持多种云存储提供商（阿里云OSS、MinIO等）的配置格式
- 实现OSS客户端工厂，根据配置动态创建相应的客户端实例
- 提供配置缓存和刷新机制，避免频繁数据库查询
- **BREAKING**: 可能需要调整现有OSS客户端的初始化方式，从静态配置改为动态获取

## Capabilities

### New Capabilities
- `oss-dynamic-client`: 从数据库读取配置并动态创建OSS客户端的能力，包括配置管理、客户端工厂、缓存机制等

### Modified Capabilities
- 无（当前没有相关现有能力）

## Impact

- 影响模块：myproject-oss模块需要重构
- 数据库：需要创建或确认`sys_oss_config`表的存在
- 依赖：可能需要引入数据库访问依赖（如MyBatis、JPA等）
- 系统：其他依赖OSS客户端的模块需要适配新的获取方式
- 配置：需要配置数据库连接和缓存参数