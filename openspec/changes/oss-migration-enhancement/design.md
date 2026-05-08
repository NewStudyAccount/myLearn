## Context

myproject-oss 模块当前已有基础的跨存储迁移功能，包括 `OssFileServiceImpl.migrateFile/migrateFiles/migrateAll` 方法和对应的 Controller 端点。但存在以下关键问题：

1. **工厂路由缺陷**：`OssFileServiceImpl` 和 `OssConfigServiceImpl` 通过 `@Autowired OssClientFactory` 注入，Spring 容器中只有 `S3OssClientFactory` 一个实现（AliyunOssClientFactory 和 MinioClientFactory 未实现），无法根据 `provider` 字段路由到不同工厂
2. **contentLength 错误**：`migrateFile` 中使用 `sysOssFile.getFileUrl().length()` 作为文件大小，这是 URL 字符串长度而非文件实际大小
3. **同步阻塞**：全量迁移在一个 HTTP 请求中同步执行，文件数量多时必然超时
4. **数据冗余**：迁移时创建新的 `sys_oss_file` 记录而非更新原记录
5. **无进度追踪**：`getMigrateProgress` 是空壳实现

当前数据模型：`sys_oss_config`（配置表）、`sys_oss_file`（文件记录表，fileUrl 存完整访问 URL）

## Goals / Non-Goals

**Goals:**
- 实现基于 provider 的工厂路由机制，支持多提供商迁移
- 将迁移逻辑重构为异步任务模式，支持进度查询和断点续传
- 修复 contentLength 计算错误，使用流式传输避免大文件内存溢出
- 迁移时更新原 `sys_oss_file` 记录的 fileUrl，而非创建新记录
- 提供迁移任务的创建、执行、进度查询、取消能力
- 支持迁移后可选删除源端文件

**Non-Goals:**
- 不实现分布式任务调度（单机 `@Async` 即可满足当前规模）
- 不实现迁移任务的持久化重试（应用重启后未完成任务标记为失败）
- 不引入消息队列（RabbitMQ/Kafka 等）
- 不实现分片上传/断点续传的上传侧优化（仅关注迁移任务层面的断点续传）
- 不改变 `OssClientFactory` 接口的核心方法签名（仅新增 `getFileSize`）

## Decisions

### 1. 工厂路由：OssClientFactoryProvider

**选择**：新增 `OssClientFactoryProvider` 类，注入所有 `OssClientFactory` 实现，根据 `SysOssConfig.provider` 字段路由

**理由**：
- 当前 `@Autowired OssClientFactory` 只能注入一个实现，无法支持多提供商
- Provider 模式是 Spring 中多实现路由的标准做法
- 不改变现有 Factory 接口，对已有代码侵入最小

**实现**：
```java
@Component
public class OssClientFactoryProvider {
    private final Map<String, OssClientFactory> factoryMap;

    public OssClientFactoryProvider(List<OssClientFactory> factories) {
        this.factoryMap = factories.stream()
            .collect(Collectors.toMap(OssClientFactory::getProvider, f -> f));
    }

    public OssClientFactory getFactory(String provider) {
        OssClientFactory factory = factoryMap.get(provider);
        if (factory == null) {
            throw new RuntimeException("不支持的OSS提供商: " + provider);
        }
        return factory;
    }

    public OssClientFactory getFactory(SysOssConfig config) {
        return getFactory(config.getProvider());
    }
}
```

**替代方案**：
- 在 `OssClientFactory` 接口中增加 `supports(provider)` 方法 + `@Qualifier` → 过于分散，每个调用点都需要判断
- 使用策略模式 + `ApplicationContext.getBean()` → 耦合 Spring 容器，不利于测试

### 2. 异步任务：数据库持久化 + @Async

**选择**：使用 `oss_migrate_task` + `oss_migrate_task_item` 两张表持久化任务状态，`@Async` 线程池异步执行

**理由**：
- 数据库持久化保证任务状态不丢失（相比内存队列）
- `@Async` 是 Spring 内置能力，无需引入额外依赖
- task_item 粒度支持断点续传：失败后只需重试未完成的 item

**表结构设计**：

`oss_migrate_task`：
| 字段 | 类型 | 说明 |
|------|------|------|
| task_id | BIGINT PK | 任务ID |
| source_config | VARCHAR(100) | 源配置名称 |
| target_config | VARCHAR(100) | 目标配置名称 |
| status | VARCHAR(20) | PENDING/RUNNING/COMPLETED/FAILED/CANCELLED |
| total_count | INT | 总文件数 |
| success_count | INT | 成功数 |
| fail_count | INT | 失败数 |
| delete_source | TINYINT(1) | 是否删除源端文件 |
| created_at | DATETIME | 创建时间 |
| finished_at | DATETIME | 完成时间 |

`oss_migrate_task_item`：
| 字段 | 类型 | 说明 |
|------|------|------|
| item_id | BIGINT PK | 明细ID |
| task_id | BIGINT FK | 关联任务ID |
| oss_id | BIGINT | 关联文件ID |
| status | VARCHAR(20) | PENDING/SUCCESS/FAILED |
| source_url | VARCHAR(255) | 源文件URL |
| target_url | VARCHAR(255) | 目标文件URL |
| error_msg | TEXT | 失败原因 |
| created_at | DATETIME | 创建时间 |

**替代方案**：
- Redis 存储任务状态 → 重启丢失，需要额外维护 Redis key 生命周期
- 消息队列（RabbitMQ）→ 引入新依赖，当前规模不需要
- Spring Batch → 过于重量级，配置复杂

### 3. 迁移模式：更新原记录

**选择**：迁移时更新 `sys_oss_file.fileUrl` 为目标端 URL，不创建新记录

**理由**：
- 其他模块（blog 等）通过 `ossId` 引用文件，创建新记录会导致引用断裂
- 更新原记录保持引用完整性，无需级联更新其他表

**替代方案**：
- 创建新记录 + 删除旧记录 → 需要更新所有引用该 ossId 的外键，复杂度高
- 创建新记录 + 保留旧记录 → 数据冗余，查询混乱

### 4. 流式传输：downloadFileStream + uploadFile(InputStream)

**选择**：使用 `downloadFileStream` 获取输入流，通过 `uploadFile(config, objectName, contentType, inputStream, contentLength)` 流式上传

**理由**：
- 避免大文件全部加载到内存（byte[] 方式）
- 通过 `HeadObject` API 获取真实 contentLength
- 流式传输是对象存储迁移的最佳实践

### 5. OssClientFactory 接口扩展

**选择**：在接口中新增 `getFileSize(SysOssConfig, String objectName)` 方法

**理由**：
- 迁移时需要知道源文件大小以正确设置流式上传的 contentLength
- S3 SDK 提供 `HeadObjectRequest` 可高效获取文件元数据而不下载文件体

## Risks / Trade-offs

- **[Risk]** `@Async` 线程池满时新任务被拒绝 → **Mitigation**：配置合理的线程池参数（核心线程数 2，最大线程数 4，队列容量 100），任务状态为 PENDING 时可等待
- **[Risk]** 应用重启导致运行中任务中断 → **Mitigation**：重启后将 RUNNING 状态的任务标记为 FAILED，用户可基于 task_item 重新发起续传
- **[Risk]** 迁移过程中源端文件被删除导致失败 → **Mitigation**：单个 item 失败不影响其他 item，记录错误信息，任务继续执行
- **[Risk]** 大文件流式传输网络中断 → **Mitigation**：失败 item 记录错误信息，支持重新执行该 item
- **[Trade-off]** 数据库持久化 vs 性能：每个 item 的状态更新会产生数据库写入，但保证了可靠性，当前文件规模（百~千级）下性能可接受
- **[Trade-off]** 更新原记录 vs 可追溯：迁移后原 URL 丢失，无法回溯 → 可通过 task_item 的 source_url 字段追溯

## Migration Plan

1. 执行 DDL 创建 `oss_migrate_task` 和 `oss_migrate_task_item` 表
2. 新增 `OssClientFactoryProvider` 类
3. 在 `OssClientFactory` 接口新增 `getFileSize` 方法，`S3OssClientFactory` 实现之
4. 新增 `OssMigrateTask` / `OssMigrateTaskItem` 实体和 Mapper
5. 新增 `OssMigrateService` 和 `OssMigrateServiceImpl`，实现异步迁移逻辑
6. 新增 `OssMigrateController`，提供任务管理端点
7. 修改 `OssFileServiceImpl` 中旧的迁移方法，委托给新的 `OssMigrateService`
8. 更新前端迁移页面，适配异步轮询模式
9. 回滚策略：保留旧同步迁移端点作为 fallback，新端点路径不同可并存