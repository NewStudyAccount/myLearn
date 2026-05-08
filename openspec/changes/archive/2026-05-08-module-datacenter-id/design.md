## Context

当前 `SnowflakeIdUtil` 仅提供一个默认实例（datacenterId=0, workerId=0），所有模块共用。用户已在注释中标注了模块与 datacenterId 的映射关系（默认0、system=1、oss=2、blog=3），但尚未在代码中实现。需要将这些映射固化为代码，并提供便捷的模块级调用方法。

## Goals / Non-Goals

**Goals:**
- 定义模块与 datacenterId 的常量映射
- 为每个模块提供专用的静态实例和静态方法
- 保持现有 `nextId()` 方法向后兼容

**Non-Goals:**
- 不实现动态模块注册机制（当前模块数量固定，无需扩展性）
- 不修改雪花算法的位结构
- 不实现模块ID的自动发现

## Decisions

### 1. 在 SnowflakeIdUtil 内部定义常量和模块实例

**选择**：在工具类内部定义 `DATACENTER_ID_*` 常量和对应的静态实例

**理由**：模块数量少且固定，无需引入额外的枚举类或配置文件。保持单文件即可完成所有功能，降低复杂度。

**备选方案**：
- 枚举类定义模块映射：增加一个类文件，过度设计
- 配置文件定义：运行时读取，增加复杂度且无法在编译期校验

### 2. 模块方法命名采用 `模块名NextId()` 格式

**选择**：`systemNextId()`、`ossNextId()`、`blogNextId()`

**理由**：方法名直观，调用方一看就知道是哪个模块的ID生成器。比传参方式更清晰，IDE 自动补全友好。

**备选方案**：
- `nextId(ModuleType type)` 枚举参数方式：需要额外定义枚举，调用略繁琐
- `nextIdForSystem()` 命名：与 `nextId()` 风格不一致

## Risks / Trade-offs

- **[模块扩展]** → 新增模块时需修改 SnowflakeIdUtil 类。当前模块数量稳定，可接受
- **[datacenterId 耗尽]** → 5位仅支持32个模块，远超当前需求
