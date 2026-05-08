## Why

当前雪花算法工具类仅提供默认实例（datacenterId=0），所有模块共用同一个数据中心ID，无法通过ID本身区分数据来源模块。不同业务模块（system、oss、blog）使用不同的 datacenterId 可以实现：通过ID即可溯源所属模块，便于排查问题和数据隔离。

## What Changes

- 为各业务模块预定义 datacenterId 常量：默认0、system模块1、oss模块2、blog模块3
- 在 `SnowflakeIdUtil` 中新增各模块专用的静态方法（如 `systemNextId()`、`ossNextId()`、`blogNextId()`）
- 为每个模块创建对应的静态实例

## Capabilities

### New Capabilities
- `module-id-mapping`: 模块与数据中心ID的映射能力，提供按模块生成带标识ID的便捷方法

### Modified Capabilities
- `snowflake-id`: 扩展 SnowflakeIdUtil 的API，新增模块级静态方法

## Impact

- 修改文件：`myproject-framework/src/main/java/com/example/utils/SnowflakeIdUtil.java`
- 新增模块级静态方法，不影响现有 `nextId()` 方法（向后兼容）
- 各业务模块可直接调用对应的模块方法生成ID
