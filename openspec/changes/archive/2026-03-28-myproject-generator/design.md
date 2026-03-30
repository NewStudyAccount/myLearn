## Context

当前项目myproject采用Vue 3 + Spring Boot架构，业务模块开发时需要手动编写大量重复性代码（Controller、Service、Mapper、Entity及前端Vue页面）。随着项目规模扩大，代码维护成本高，一致性难以保证。

## Goals / Non-Goals

**Goals:**
- 实现数据库表结构自动解析，读取表名、字段名、类型、注释等信息
- 自动生成后端RESTful Controller代码
- 自动生成Service层CRUD业务代码
- 自动生成MyBatis Mapper接口及XML映射文件
- 自动生成Entity实体类
- 自动生成前端Vue页面（列表、新增、编辑、详情）
- 自动生成前端API封装代码
- 支持Velocity/Freemarker模板引擎扩展

**Non-Goals:**
- 不支持代码部署上线（仅生成代码）
- 不支持数据库DDL脚本生成
- 不生成复杂业务逻辑（仅基础CRUD）
- 不支持分布式事务场景

## Decisions

1. **模板引擎选择Velocity** — Velocity语法简洁，学习成本低，社区成熟。相比Freemarker更轻量，适合代码生成场景。

2. **数据库连接方式** — 复用项目现有数据源配置，通过 JdbcTemplate 读取表结构信息，无需额外数据库权限。

3. **前端生成策略** — 生成单文件Vue组件（列表+新增+编辑+详情四合一），使用Element Plus组件栈，与现有前端技术栈一致。

4. **代码输出方式** — 生成到指定目录而非直接写入源码目录，支持预览后手动复制，适合团队协作流程。

5. **字段类型映射** — 数据库类型到Java类型建立标准映射表（VARCHAR→String, INT→Integer, DATETIME→LocalDateTime等），支持自定义类型映射。

## Risks / Trade-offs

- [风险] 数据库表注释缺失导致生成的代码缺少字段说明 → 缓解：允许手动补充注释或使用字段名作为备选
- [风险] 复杂关联表（一对多、多对多）无法自动处理 → 缓解：仅支持单表CRUD，关联逻辑需手动扩展
- [风险] 生成的Vue页面不符合项目现有规范 → 缓解：提供可配置的模板，调整生成样式和行为
- [风险] 数据库类型与Java类型映射不完整 → 缓解：提供自定义类型映射配置文件