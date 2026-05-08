## ADDED Requirements

### Requirement: 单文件导出
系统 SHALL 支持将单个 OSS 文件导出到用户本地。用户通过指定文件 ID 或文件 URL，系统 SHALL 返回文件内容供下载。

#### Scenario: 通过文件 ID 导出
- **WHEN** 用户提供有效的文件 ID 并请求导出
- **THEN** 系统根据 file_url 从 OSS 获取文件内容，以附件形式返回给用户

#### Scenario: 文件不存在
- **WHEN** 用户提供的文件 ID 在 sys_oss_file 表中不存在
- **THEN** 系统返回文件不存在的错误信息

### Requirement: 批量文件导出
系统 SHALL 支持批量导出多个 OSS 文件。用户通过提供文件 ID 列表，系统 SHALL 将所有文件打包为 ZIP 压缩包供下载。

#### Scenario: 批量导出多个文件
- **WHEN** 用户提供多个有效的文件 ID 并请求批量导出
- **THEN** 系统将所有文件打包为 ZIP 文件，以附件形式返回

#### Scenario: 部分文件不存在
- **WHEN** 用户提供的文件 ID 列表中部分文件不存在
- **THEN** 系统导出存在的文件，在 ZIP 中包含一个说明文件记录失败的文件 ID

### Requirement: 按条件筛选导出
系统 SHALL 支持按文件后缀名、上传时间范围等条件筛选并导出文件。

#### Scenario: 按文件后缀筛选
- **WHEN** 用户指定文件后缀名（如 .png、.jpg）并请求导出
- **THEN** 系统从 sys_oss_file 表查询匹配的文件，打包为 ZIP 返回

#### Scenario: 按时间范围筛选
- **WHEN** 用户指定开始时间和结束时间并请求导出
- **THEN** 系统导出该时间范围内上传的所有文件
