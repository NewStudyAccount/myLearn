## 1. 数据库表与实体

- [x] 1.1 创建 `oss_migrate_task` 表 DDL（task_id, source_config, target_config, status, total_count, success_count, fail_count, delete_source, created_at, finished_at）并添加到 SQL 脚本
- [x] 1.2 创建 `oss_migrate_task_item` 表 DDL（item_id, task_id, oss_id, status, source_url, target_url, error_msg, created_at）并添加到 SQL 脚本
- [x] 1.3 创建 `OssMigrateTask` 实体类，使用 MyBatis-Plus 注解映射到 `oss_migrate_task` 表
- [x] 1.4 创建 `OssMigrateTaskItem` 实体类，使用 MyBatis-Plus 注解映射到 `oss_migrate_task_item` 表
- [x] 1.5 创建 `OssMigrateTaskMapper` 接口继承 `BaseMapper<OssMigrateTask>`
- [x] 1.6 创建 `OssMigrateTaskItemMapper` 接口继承 `BaseMapper<OssMigrateTaskItem>`

## 2. OssClientFactoryProvider 工厂路由

- [x] 2.1 创建 `OssClientFactoryProvider` 类，注入 `List<OssClientFactory>`，在构造函数中建立 provider → factory 的 Map 映射
- [x] 2.2 实现 `getFactory(String provider)` 方法，根据 provider 返回对应工厂，不存在时抛出异常
- [x] 2.3 实现 `getFactory(SysOssConfig config)` 便捷方法，从配置的 provider 字段获取工厂

## 3. OssClientFactory 接口扩展

- [x] 3.1 在 `OssClientFactory` 接口新增 `long getFileSize(SysOssConfig sysOssConfig, String objectName)` 方法
- [x] 3.2 在 `S3OssClientFactory` 中实现 `getFileSize`：使用 `S3Client.headObject(HeadObjectRequest)` 获取 ContentLength

## 4. 替换直接注入 OssClientFactory

- [x] 4.1 修改 `OssFileServiceImpl`：将 `@Autowired OssClientFactory` 替换为 `@Autowired OssClientFactoryProvider`，所有调用处通过 provider 获取工厂
- [x] 4.2 修改 `OssConfigServiceImpl`：将 `@Autowired OssClientFactory` 替换为 `@Autowired OssClientFactoryProvider`，initConfig 方法通过 provider 获取工厂
- [x] 4.3 修改 `OssController`：将 `OssClientFactory` 注入替换为 `OssClientFactoryProvider`

## 5. 异步迁移服务

- [x] 5.1 创建 `OssMigrateService` 接口，定义 createTask、executeAsync、getProgress、cancelTask、resumeTask、listTasks 方法
- [x] 5.2 创建 `OssMigrateServiceImpl`，注入 OssClientFactoryProvider、OssMigrateTaskMapper、OssMigrateTaskItemMapper、OssFileMapper、OssConfigService
- [x] 5.3 实现 `createTask(sourceConfig, targetConfig, ossIds, deleteSource)`：校验配置、创建 task 记录、根据 ossIds 或源配置匹配生成 task_item 记录
- [x] 5.4 实现 `executeAsync(taskId)`：使用 @Async 异步执行，更新 task 状态为 RUNNING，逐个处理 PENDING 的 item
- [x] 5.5 实现单文件迁移核心逻辑：通过 OssClientFactoryProvider 获取源/目标工厂，流式下载→获取 fileSize→流式上传→更新 sys_oss_file.fileUrl→可选删除源端文件
- [x] 5.6 实现 `getProgress(taskId)`：查询 task 及其 item 统计，返回进度信息
- [x] 5.7 实现 `cancelTask(taskId)`：校验状态后更新为 CANCELLED
- [x] 5.8 实现 `resumeTask(taskId)`：将 FAILED/CANCELLED 任务重置为 PENDING，将 PENDING/FAILED 的 item 重置为 PENDING，重新调用 executeAsync
- [x] 5.9 实现 `listTasks(status, pageNum, pageSize)`：分页查询任务列表
- [x] 5.10 配置 Spring @Async 线程池 Bean（核心线程 2、最大线程 4、队列容量 100、线程名前缀 oss-migrate-）

## 6. 迁移 Controller

- [x] 6.1 创建 `OssMigrateController`，路径 `/project/oss/migrate`
- [x] 6.2 实现 `POST /task` 端点：创建迁移任务（参数：sourceConfig, targetConfig, ossIds可选, deleteSource默认false），返回 taskId
- [x] 6.3 实现 `GET /task/{taskId}/progress` 端点：查询迁移进度
- [x] 6.4 实现 `POST /task/{taskId}/cancel` 端点：取消任务
- [x] 6.5 实现 `POST /task/{taskId}/resume` 端点：续传任务
- [x] 6.6 实现 `GET /task/list` 端点：分页查询任务列表，支持 status 筛选

## 7. 旧迁移接口兼容

- [x] 7.1 修改 `OssFileServiceImpl.migrateFile/migrateFiles/migrateAll`：委托给 `OssMigrateService.createTask` + `executeAsync`，同步等待完成后返回结果（保持旧接口兼容）
- [x] 7.2 修改 `OssController` 旧迁移端点 `/migrate/file`、`/migrate/batch`、`/migrate/all`：调用新的 OssMigrateService，返回格式增加 taskId 字段

## 8. 前端适配

- [x] 8.1 在 `sysOssFileApi.ts` 中新增迁移任务相关 API 方法：createMigrateTask、getMigrateProgress、cancelMigrateTask、resumeMigrateTask、listMigrateTasks
- [x] 8.2 重构 `migrate/index.vue`：迁移操作改为调用 createMigrateTask 获取 taskId，使用定时器轮询 getMigrateProgress 展示进度条
- [x] 8.3 新增迁移任务列表区域：展示历史迁移任务及状态，支持取消和续传操作
- [x] 8.4 新增迁移创建对话框：支持选择源/目标配置、指定文件或全量、是否删除源端文件