### Requirement: 模块与数据中心ID的常量映射

系统 SHALL 在 `SnowflakeIdUtil` 中定义模块与 datacenterId 的常量映射：默认=0、system=1、oss=2、blog=3。

#### Scenario: 常量可被外部访问
- **WHEN** 外部代码引用 `SnowflakeIdUtil.DATACENTER_ID_SYSTEM`
- **THEN** 返回值 1

#### Scenario: 各模块常量值正确
- **WHEN** 查询所有模块常量
- **THEN** DATACENTER_ID_DEFAULT=0, DATACENTER_ID_SYSTEM=1, DATACENTER_ID_OSS=2, DATACENTER_ID_BLOG=3

### Requirement: 模块级静态方法生成ID

系统 SHALL 为每个模块提供专用的静态方法，使用对应的 datacenterId 生成唯一ID。

#### Scenario: system模块生成ID
- **WHEN** 调用 `SnowflakeIdUtil.systemNextId()`
- **THEN** 返回的ID中 datacenterId 部分为 1

#### Scenario: oss模块生成ID
- **WHEN** 调用 `SnowflakeIdUtil.ossNextId()`
- **THEN** 返回的ID中 datacenterId 部分为 2

#### Scenario: blog模块生成ID
- **WHEN** 调用 `SnowflakeIdUtil.blogNextId()`
- **THEN** 返回的ID中 datacenterId 部分为 3
