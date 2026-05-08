## ADDED Requirements

### Requirement: 迁移任务创建
系统 SHALL 支持创建迁移任务，指定源端配置名称和目标端配置名称。创建任务时系统 SHALL 校验源端和目标端配置均存在且已启用，且两者不能相同。

#### Scenario: 创建迁移任务成功
- **WHEN** 用户提交迁移任务，源配置为 "minio-local"，目标配置为 "aliyun-default"，两者均存在且已启用
- **THEN** 系统创建一条 `oss_migrate_task` 记录，状态为 PENDING，并自动根据源配置的 endpoint 匹配 `sys_oss_file` 表中的文件，生成对应的 `oss_migrate_task_item` 记录

#### Scenario: 源配置不存在
- **WHEN** 用户提交迁移任务，源配置名称在 `sys_oss_config` 中不存在
- **THEN** 系统拒绝创建，返回错误信息 "源OSS配置不存在: {configName}"

#### Scenario: 目标配置未启用
- **WHEN** 用户提交迁移任务，目标配置的 is_active 为 0
- **THEN** 系统拒绝创建，返回错误信息 "目标OSS配置未启用: {configName}"

#### Scenario: 源配置和目标配置相同
- **WHEN** 用户提交迁移任务，源配置和目标配置名称相同
- **THEN** 系统拒绝创建，返回错误信息 "源配置和目标配置不能相同"

### Requirement: 迁移任务异步执行
系统 SHALL 使用 Spring @Async 异步执行迁移任务。任务开始执行时状态更新为 RUNNING，每个文件迁移完成后更新对应 task_item 状态和 task 的成功/失败计数。

#### Scenario: 任务开始执行
- **WHEN** 迁移任务被调度执行
- **THEN** 任务状态更新为 RUNNING，开始逐个处理 task_item

#### Scenario: 单文件迁移成功
- **WHEN** 某个 task_item 的文件从源端下载并上传到目标端成功
- **THEN** task_item 状态更新为 SUCCESS，记录 target_url，task 的 success_count 加 1，`sys_oss_file` 中对应记录的 fileUrl 更新为目标端 URL

#### Scenario: 单文件迁移失败
- **WHEN** 某个 task_item 的文件迁移过程中发生异常
- **THEN** task_item 状态更新为 FAILED，记录 error_msg，task 的 fail_count 加 1，继续处理下一个 item

#### Scenario: 全部 item 处理完成
- **WHEN** 所有 task_item 均处理完毕（SUCCESS 或 FAILED）
- **THEN** 任务状态更新为 COMPLETED，记录 finished_at 时间

### Requirement: 迁移文件流式传输
系统 SHALL 使用流式传输进行文件迁移，从源端获取 InputStream 直接写入目标端，避免大文件内存溢出。上传前 SHALL 通过 getFileSize API 获取源文件真实大小作为 contentLength。

#### Scenario: 流式迁移文件
- **WHEN** 迁移一个文件
- **THEN** 系统调用 `factory.downloadFileStream()` 获取输入流，调用 `factory.getFileSize()` 获取文件大小，再调用 `factory.uploadFile(config, objectName, contentType, inputStream, contentLength)` 流式上传

#### Scenario: 获取文件大小失败
- **WHEN** 调用 `getFileSize` 失败（如文件不存在）
- **THEN** 该 task_item 标记为 FAILED，error_msg 记录具体原因

### Requirement: 迁移更新原记录
系统 SHALL 在文件迁移成功后更新 `sys_oss_file` 表中对应记录的 `fileUrl` 为目标端 URL，而非创建新的数据库记录。

#### Scenario: 更新文件记录
- **WHEN** 文件迁移成功，源 URL 为 "http://old-server:9000/bucket/file.md"，目标 URL 为 "https://oss.aliyuncs.com/bucket/file.md"
- **THEN** `sys_oss_file` 中该记录的 fileUrl 更新为 "https://oss.aliyuncs.com/bucket/file.md"

### Requirement: 可选删除源端文件
系统 SHALL 支持在迁移任务中指定是否删除源端文件。当 delete_source 为 true 时，文件迁移成功后删除源端对象。

#### Scenario: 删除源端文件
- **WHEN** 迁移任务指定 delete_source = true，且文件迁移成功
- **THEN** 系统调用 `factory.deleteFile()` 删除源端对象

#### Scenario: 不删除源端文件
- **WHEN** 迁移任务指定 delete_source = false
- **THEN** 源端文件保留不动

#### Scenario: 删除源端文件失败不影响迁移结果
- **WHEN** 文件迁移成功但删除源端文件失败
- **THEN** task_item 状态仍为 SUCCESS，在 error_msg 中记录删除失败的警告信息

### Requirement: 迁移进度查询
系统 SHALL 支持通过 taskId 查询迁移任务的进度，返回任务状态、总数、成功数、失败数及百分比。

#### Scenario: 查询进行中的任务进度
- **WHEN** 查询 taskId 为 1 的任务，状态为 RUNNING，total_count=100，success_count=60，fail_count=2
- **THEN** 返回任务状态 RUNNING，进度百分比 62%，成功 60，失败 2，剩余 38

#### Scenario: 查询已完成的任务
- **WHEN** 查询 taskId 为 2 的任务，状态为 COMPLETED
- **THEN** 返回任务状态 COMPLETED，进度百分比 100%，及最终的成功/失败计数

#### Scenario: 任务不存在
- **WHEN** 查询不存在的 taskId
- **THEN** 返回错误信息 "迁移任务不存在: {taskId}"

### Requirement: 迁移任务取消
系统 SHALL 支持取消处于 PENDING 或 RUNNING 状态的迁移任务。取消后未处理的 task_item 不再执行。

#### Scenario: 取消运行中的任务
- **WHEN** 用户取消状态为 RUNNING 的任务
- **THEN** 任务状态更新为 CANCELLED，未处理的 PENDING 状态 item 保持 PENDING 不再执行

#### Scenario: 取消已完成的任务
- **WHEN** 用户尝试取消状态为 COMPLETED 的任务
- **THEN** 系统拒绝操作，返回错误信息 "任务已完成，无法取消"

### Requirement: 断点续传
系统 SHALL 支持对 FAILED 或 CANCELLED 状态的任务进行续传，仅重新执行状态为 PENDING 或 FAILED 的 task_item。

#### Scenario: 续传失败的任务
- **WHEN** 用户对状态为 FAILED 的任务发起续传，该任务有 10 个 FAILED item 和 5 个 PENDING item
- **THEN** 系统将任务状态重置为 PENDING，仅重新执行这 15 个 item，已 SUCCESS 的 item 不再重复执行

#### Scenario: 续传已完成的任务
- **WHEN** 用户尝试续传状态为 COMPLETED 的任务
- **THEN** 系统拒绝操作，返回错误信息 "任务已完成，无需续传"

### Requirement: 迁移任务列表查询
系统 SHALL 支持分页查询迁移任务列表，支持按状态筛选。

#### Scenario: 查询所有任务
- **WHEN** 用户请求迁移任务列表，不指定筛选条件
- **THEN** 返回所有迁移任务的分页列表，按创建时间倒序

#### Scenario: 按状态筛选
- **WHEN** 用户请求状态为 RUNNING 的迁移任务列表
- **THEN** 仅返回状态为 RUNNING 的任务

### Requirement: 指定文件迁移
系统 SHALL 支持创建仅包含指定 ossId 列表的迁移任务，而非全量迁移源配置下的所有文件。

#### Scenario: 指定文件列表迁移
- **WHEN** 用户提交迁移任务，指定 ossIds 为 [1, 2, 3]
- **THEN** 系统仅为这 3 个文件创建 task_item，total_count 为 3

#### Scenario: 指定的文件不存在
- **WHEN** 用户提交迁移任务，ossIds 中包含不存在的 ossId
- **THEN** 系统忽略不存在的 ossId，仅为存在的文件创建 task_item