## ADDED Requirements

### Requirement: 多提供商工厂路由
系统 SHALL 提供 `OssClientFactoryProvider` 组件，根据 `SysOssConfig.provider` 字段自动路由到对应的 `OssClientFactory` 实现。系统启动时 SHALL 自动收集所有 `OssClientFactory` 实现并建立 provider → factory 的映射。

#### Scenario: 获取 S3 工厂
- **WHEN** 调用 `OssClientFactoryProvider.getFactory("s3")`
- **THEN** 返回 `S3OssClientFactory` 实例

#### Scenario: 获取不支持的提供商
- **WHEN** 调用 `OssClientFactoryProvider.getFactory("unsupported")`
- **THEN** 抛出运行时异常，信息为 "不支持的OSS提供商: unsupported"

#### Scenario: 通过配置对象获取工厂
- **WHEN** 调用 `OssClientFactoryProvider.getFactory(sysOssConfig)`，config 的 provider 为 "minio"
- **THEN** 返回 provider 为 "minio" 的 `OssClientFactory` 实例

### Requirement: 获取文件大小
`OssClientFactory` 接口 SHALL 新增 `getFileSize(SysOssConfig, String objectName)` 方法，返回指定对象的字节大小。`S3OssClientFactory` SHALL 使用 S3 `HeadObjectRequest` 实现。

#### Scenario: 获取存在的文件大小
- **WHEN** 调用 `factory.getFileSize(config, "test.pdf")` 且对象存在，大小为 1024 字节
- **THEN** 返回 1024

#### Scenario: 获取不存在的文件大小
- **WHEN** 调用 `factory.getFileSize(config, "not-exist.pdf")` 且对象不存在
- **THEN** 抛出运行时异常，信息提示对象不存在

### Requirement: 替换直接注入 OssClientFactory
`OssFileServiceImpl` 和 `OssConfigServiceImpl` 中直接 `@Autowired OssClientFactory` 的方式 SHALL 替换为注入 `OssClientFactoryProvider`，通过 provider 获取正确的工厂实例。

#### Scenario: OssFileServiceImpl 上传文件
- **WHEN** `OssFileServiceImpl` 需要上传文件到 provider 为 "minio" 的配置
- **THEN** 通过 `OssClientFactoryProvider.getFactory(config)` 获取对应工厂，而非直接使用注入的单一工厂

#### Scenario: OssConfigServiceImpl 初始化配置
- **WHEN** `OssConfigServiceImpl.initConfig()` 初始化所有活跃配置的客户端
- **THEN** 对每个配置通过 `OssClientFactoryProvider` 获取对应工厂创建客户端