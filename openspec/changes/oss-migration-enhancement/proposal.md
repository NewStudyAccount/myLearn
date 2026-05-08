## Why

myproject-oss 模块已有基础的跨存储迁移功能（单文件/批量/全量），但存在多个严重缺陷：`OssClientFactory` 直接注入单一实现无法按 provider 路由、迁移时 contentLength 使用 URL 字符串长度而非实际文件大小、全量迁移同步阻塞导致 HTTP 超时、迁移创建新记录而非更新原记录造成数据冗余、缺少异步任务追踪和断点续传能力。这些问题导致迁移功能在实际生产场景中不可用，需要重构为可靠的异步迁移方案。

## What Changes

- 新增 `OssClientFactoryProvider`，根据 `SysOssConfig.provider` 字段路由到正确的 `OssClientFactory` 实现，替代当前直接 `@Autowired OssClientFactory` 只注入单一实现的方式
- 新增 `OssMigrateTask` 数据库实体和 `oss_migrate_task` 表，持久化记录迁移任务状态（待执行/执行中/已完成/失败/已取消）
- 新增 `OssMigrateTaskItem` 实体和 `oss_migrate_task_item` 表，记录每个文件的迁移明细（支持断点续传）
- 重构迁移逻辑为异步执行：使用 Spring `@Async` + 线程池，迁移接口立即返回 taskId，前端轮询进度
- 修复 `migrateFile` 中 contentLength 计算错误：通过 `HeadObject` API 获取真实文件大小
- 迁移模式改为"更新原记录"：更新 `sys_oss_file.fileUrl` 而非创建新记录，可选是否删除源端文件
- 在 `OssClientFactory` 接口新增 `getFileSize` 方法，供迁移时获取源文件实际大小
- **BREAKING**: `OssFileService` 迁移方法签名变更，返回值从直接返回结果改为返回 taskId
- **BREAKING**: `OssController` 迁移端点响应格式变更，从同步返回结果改为返回任务 ID

## Capabilities

### New Capabilities
- `oss-migration-task`: 异步迁移任务管理能力，包括任务创建、异步执行、进度查询、断点续传、任务取消
- `oss-factory-provider`: 多提供商工厂路由能力，根据 provider 字段自动选择正确的 OssClientFactory 实现

### Modified Capabilities

## Impact

- 影响模块：myproject-oss（核心改动）
- 数据库：新增 `oss_migrate_task` 和 `oss_migrate_task_item` 两张表
- API 变更：`/project/sysOssFile/migrate/*` 端点响应格式变更（同步 → 异步任务 ID）
- 依赖：无新增外部依赖（`@Async` 为 Spring 内置）
- 前端：迁移页面需适配异步轮询模式