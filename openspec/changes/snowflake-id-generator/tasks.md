## 1. 核心实现

- [x] 1.1 创建 `SnowflakeIdUtil` 类，定义雪花算法常量（起始时间戳、各部分位数和掩码）
- [x] 1.2 实现构造方法，支持传入 datacenterId 和 workerId，并校验范围（0-31）
- [x] 1.3 实现 `nextId()` 方法，包含时间戳获取、序列号递增、时钟回拨检测逻辑
- [x] 1.4 使用 synchronized 保证线程安全，确保多线程下不生成重复ID

## 2. 易用性封装

- [x] 2.1 提供默认实例（datacenterId=0, workerId=0）和静态方法 `nextId()`，简化调用
- [x] 2.2 添加 Javadoc 注释，说明使用方式和参数约束

## 3. 验证

- [x] 3.1 编写单元测试，验证单线程下ID唯一性和趋势递增
- [x] 3.2 编写单元测试，验证多线程并发下ID唯一性
- [x] 3.3 编写单元测试，验证 datacenterId/workerId 越界时抛出 IllegalArgumentException
- [x] 3.4 编写单元测试，验证时钟回拨时抛出异常
