### Requirement: SnowflakeIdUtil SHALL generate unique 64-bit IDs

系统 SHALL 提供 `SnowflakeIdUtil` 工具类，基于雪花算法生成全局唯一的 64 位长整型ID。ID 结构为：1位符号位 + 41位时间戳（毫秒精度）+ 5位数据中心ID + 5位机器ID + 12位序列号。同时 SHALL 提供模块级静态方法（systemNextId、ossNextId、blogNextId），各方法使用对应的 datacenterId 生成ID。

#### Scenario: 生成唯一ID
- **WHEN** 调用 `SnowflakeIdUtil.nextId()` 方法
- **THEN** 返回一个正的 64 位长整型ID，且多次调用返回的ID互不相同

#### Scenario: ID趋势递增
- **WHEN** 连续调用 `nextId()` 多次
- **THEN** 后生成的ID大于先生成的ID（同一毫秒内按序列号递增，跨毫秒按时间戳递增）

#### Scenario: 模块方法生成带模块标识的ID
- **WHEN** 调用 `SnowflakeIdUtil.systemNextId()`
- **THEN** 返回的ID中 datacenterId 编码为 1，且ID全局唯一

### Requirement: SnowflakeIdUtil SHALL support custom datacenter and worker IDs

系统 SHALL 支持通过构造方法或初始化方法指定数据中心ID（0-31）和机器ID（0-31），并在ID中编码这些标识。同时 SHALL 预定义模块级 datacenterId 常量。

#### Scenario: 使用自定义datacenterId和workerId初始化
- **WHEN** 使用有效的 datacenterId（0-31）和 workerId（0-31）初始化工具类
- **THEN** 生成的ID中包含指定的 datacenterId 和 workerId 信息

#### Scenario: datacenterId或workerId超出范围
- **WHEN** 传入的 datacenterId 或 workerId 超出 0-31 范围
- **THEN** 抛出 IllegalArgumentException

### Requirement: SnowflakeIdUtil SHALL handle clock drift

系统 SHALL 检测时钟回拨情况。当检测到当前时间小于上次生成ID的时间时，SHALL 抛出异常以防止生成重复ID。

#### Scenario: 检测到时钟回拨
- **WHEN** 系统时钟回拨到上次ID生成时间之前
- **THEN** 抛出 RuntimeException，提示时钟回拨异常

#### Scenario: 正常时间推进
- **WHEN** 系统时钟正常推进（当前时间 >= 上次生成ID时间）
- **THEN** 正常生成ID，无异常抛出

### Requirement: SnowflakeIdUtil SHALL be thread-safe

系统 SHALL 保证 `SnowflakeIdUtil` 在多线程环境下安全使用，不会生成重复ID。

#### Scenario: 多线程并发调用
- **WHEN** 多个线程同时调用 `nextId()` 方法
- **THEN** 每个线程获取的ID互不相同，无重复ID生成
