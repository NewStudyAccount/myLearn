## ADDED Requirements

### Requirement: S3 协议兼容的客户端工厂
系统 SHALL 提供基于 AWS S3 SDK 的 `S3OssClientFactory` 实现，支持通过 S3 协议创建 `S3Client`。

#### Scenario: 创建 S3 客户端
- **WHEN** provider 为 `s3` 的 OssConfig 被传入 `S3OssClientFactory.createClient()`
- **THEN** 返回一个配置好 endpoint、credentials、region 的 `S3Client` 实例

#### Scenario: 提供商标识
- **WHEN** 调用 `S3OssClientFactory.getProvider()`
- **THEN** 返回字符串 `"s3"`

### Requirement: 文件上传能力
系统 SHALL 支持通过任意已注册的 Factory 上传文件到对象存储。

#### Scenario: 上传文件成功
- **WHEN** 调用 `factory.uploadFile("test.txt", fileBytes)` 且客户端连接正常
- **THEN** 文件被上传到指定 bucket，返回对象的访问路径或 key

#### Scenario: 上传文件失败
- **WHEN** 调用 `factory.uploadFile("test.txt", fileBytes)` 但客户端连接失败
- **THEN** 抛出运行时异常，包含错误信息

### Requirement: 文件下载能力
系统 SHALL 支持通过任意已注册的 Factory 从对象存储下载文件。

#### Scenario: 下载文件成功
- **WHEN** 调用 `factory.downloadFile("test.txt")` 且对象存在
- **THEN** 返回文件的字节数组

#### Scenario: 下载文件不存在
- **WHEN** 调用 `factory.downloadFile("not-exist.txt")` 但对象不存在
- **THEN** 抛出运行时异常，提示对象不存在

### Requirement: 文件上传 REST 端点
系统 SHALL 提供 HTTP 端点用于上传文件。

#### Scenario: 通过接口上传文件
- **WHEN** 发送 `POST /project/oss/upload` 携带 MultipartFile 和 configName 参数
- **THEN** 使用指定配置的 Factory 上传文件，返回上传结果

### Requirement: 文件下载 REST 端点
系统 SHALL 提供 HTTP 端点用于下载文件。

#### Scenario: 通过接口下载文件
- **WHEN** 发送 `GET /project/oss/download?configName=xxx&objectName=yyy`
- **THEN** 使用指定配置的 Factory 下载文件，以文件流形式返回

### Requirement: S3 客户端连接测试
系统 SHALL 支持对 S3 客户端进行连接测试。

#### Scenario: S3 连接测试成功
- **WHEN** 调用 `testConnection(configName)` 且 S3 服务可达
- **THEN** 执行 `listBuckets()` 成功，返回 true

#### Scenario: S3 连接测试失败
- **WHEN** 调用 `testConnection(configName)` 但 S3 服务不可达
- **THEN** 捕获异常，返回 false
