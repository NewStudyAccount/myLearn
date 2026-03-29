## Context

myproject-generator是一个基于MyBatis-Plus的代码生成器，从数据库表元数据生成Entity、Mapper、Service、Controller等代码。当前实现存在两个核心问题：

1. **Service层空壳问题**: ServiceGenerator生成的接口仅继承IService，实现类仅继承ServiceImpl，不包含任何业务方法。开发者每次生成后仍需手动添加分页查询、条件查询等常用方法。

2. **类名命名问题**: TableReader.toCamelCase()方法将首字母转为小写（lowerCamelCase），但Java类名应为UpperCamelCase。这导致生成的类名如"user"而非"User"，违反Java规范。

### 技术约束
- 使用Apache Velocity模板引擎
- 基于MyBatis-Plus的IService/ServiceImpl体系
- 通过REST API暴露生成能力
- 生成代码打包为ZIP下载

## Goals / Non-Goals

**Goals:**
- Service层生成包含常用业务方法的完整代码
- 所有生成的类名遵循Java UpperCamelCase命名规范
- 保持向后兼容，不影响现有API接口

**Non-Goals:**
- 不改变生成器的整体架构
- 不添加新的模板引擎
- 不支持自定义模板（后续迭代考虑）

## Decisions

### Decision 1: Service层方法集选择
**选择**: 生成以下方法：
- `getPageList(Page<T> page, Wrapper<T> queryWrapper)`: 分页查询
- `getOne(Wrapper<T> queryWrapper)`: 条件查询单条
- `listByCondition(Wrapper<T> queryWrapper)`: 条件查询列表
- `saveBatch(Collection<T> entityList)`: 批量保存（继承自IService）
- `removeByIds(Collection<? extends Serializable> idList)`: 批量删除（继承自IService）

**理由**: 这些是最常用的业务方法，覆盖80%的使用场景。批量操作已由IService提供，只需在接口中声明。

**替代方案**: 考虑过生成更多方法（如导入导出、复杂统计），但会增加复杂度，初期保持简单。

### Decision 2: 类名生成修复策略
**选择**: 在TableReader.toCamelCase()中添加capitalize参数，默认转为UpperCamelCase

**理由**: 最小改动范围，只修改一个方法。通过参数控制保持灵活性。

**替代方案**: 考虑在EntityGenerator中单独处理，但会导致多处修改且不一致。

### Decision 3: 模板组织方式
**选择**: 保持现有的Java文本块模板方式，新增Service方法模板

**理由**: 与现有代码风格一致，无需引入额外依赖。

## Risks / Trade-offs

- **[风险]** 修改toCamelCase可能影响现有字段名生成 → **缓解**: 添加参数控制，默认行为不变
- **[风险]** 新增的Service方法可能不适用于所有业务场景 → **缓解**: 提供最通用的方法集，后续可扩展
- **[权衡]** 生成更多代码会增加文件大小 → **可接受**: 增量很小，且能减少手动编码工作
