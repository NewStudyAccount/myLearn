## ADDED Requirements

### Requirement: 单文件导入
系统 SHALL 支持将单个本地文件导入到 OSS。用户通过 multipart 上传文件并指定目标 OSS 配置名，系统 SHALL 将文件上传到对应的存储后端并创建 sys_oss_file 记录。

#### Scenario: 指定配置名上传
- **WHEN** 用户上传文件并指定有效的 OSS 配置名
- **THEN** 系统将文件上传到该配置对应的存储后端，创建数据库记录并返回文件信息

#### Scenario: 配置名不存在
- **WHEN** 用户指定的 OSS 配置名不存在
- **THEN** 系统返回配置不存在的错误信息

### Requirement: 批量文件导入
系统 SHALL 支持批量导入本地文件。用户通过上传多个文件，系统 SHALL 逐个上传到指定的 OSS 后端并创建数据库记录。

#### Scenario: 批量上传多个文件
- **WHEN** 用户上传多个文件并指定目标 OSS 配置名
- **THEN** 系统逐个上传所有文件，返回每个文件的上传结果（成功/失败及原因）

#### Scenario: 部分文件上传失败
- **WHEN** 批量上传中部分文件因大小超限或类型不支持而失败
- **THEN** 系统继续处理剩余文件，最终返回成功和失败的统计信息

### Requirement: 通过 Excel 清单导入
系统 SHALL 支持通过上传 Excel 文件指定要导入的文件列表。Excel 文件包含文件路径和目标 OSS 配置名。

#### Scenario: 通过 Excel 批量导入
- **WHEN** 用户上传包含文件路径清单的 Excel 文件并指定默认 OSS 配置名
- **THEN** 系统读取 Excel 中的文件路径，逐个上传到指定的 OSS 后端

#### Scenario: Excel 格式错误
- **WHEN** 上传的 Excel 文件格式不正确或缺少必要字段
- **THEN** 系统返回格式错误的提示信息
