## Context

myproject是一个使用Vue3 + Element Plus前端和Java后端的全栈项目。当前开发新业务功能时，需要手动创建大量的重复代码：前端需要创建列表页、表单页、详情页组件、API调用代码、路由配置；后端需要创建实体类、Mapper接口、Service层、Controller层。这些重复性工作严重降低了开发效率，且容易因人为疏忽导致代码不一致。

引入代码生成器模块，通过读取数据库表结构元数据，结合模板引擎自动生成标准化的前后端代码，可以显著提升开发效率和代码质量。

## Goals / Non-Goals

**Goals:**
- 基于数据库表结构自动生成前后端代码
- 前端生成：Vue3 + TypeScript组件、API调用代码、路由配置、Pinia状态管理
- 后端生成：MyBatis Plus实体类(Domain)、Mapper接口、Service层、Controller层
- 支持灵活的生成配置（表前缀、包名、字段映射等）
- 提供代码预览和一键下载功能
- 生成代码遵循项目现有编码规范和目录结构

**Non-Goals:**
- 不支持反向同步（数据库变更自动更新代码）
- 不支持自定义模板的在线编辑器（第一版仅提供内置模板）
- 不支持跨数据库类型迁移（如MySQL转PostgreSQL）
- 不处理业务逻辑代码（仅生成CRUD骨架）

## Decisions

### 1. 模板引擎选择：Velocity

**选择**: Apache Velocity

**备选方案**:
- FreeMarker：功能更强大，但语法相对复杂
- Thymeleaf：主要面向HTML渲染，不适合生成各类代码文件
- 自研模板引擎：开发成本高

**理由**: Velocity语法简洁、学习成本低、Java生态成熟，适合代码生成场景。且不需要额外的复杂特性。

### 2. 元数据读取方式：JDBC + information_schema

**选择**: 直接通过JDBC查询information_schema获取表结构

**理由**: 不依赖特定ORM框架，通用性强，能获取完整的字段类型、注释、主键、索引等信息。

### 3. 前端生成架构

**选择**: 生成独立的目录结构，每个表对应一个功能模块

**结构**:
```
views/<tableName>/
├── index.vue          # 列表页
├── components/
│   ├── FormDialog.vue # 新增/编辑表单
│   └── Detail.vue     # 详情页
api/<tableName>.ts     # API接口
router/modules/<tableName>.ts  # 路由配置
```

### 4. 后端生成架构

**选择**: 标准分层架构，每个表对应一套完整代码

**结构**:
```
domain/<TableName>.java       # 实体类（含MyBatis Plus注解）
mapper/<TableName>Mapper.java # Mapper接口
service/<TableName>Service.java
service/impl/<TableName>ServiceImpl.java
controller/<TableName>Controller.java
```

### 5. 字段类型映射策略

**选择**: 维护MySQL到Java类型和MySQL到TypeScript类型的映射表

**理由**: 确保生成代码的类型安全，支持常见类型的自动映射，同时允许用户自定义扩展。

## Risks / Trade-offs

- **模板维护成本** → 版本化管理模板文件，提供模板更新机制
- **特殊字段处理** → 支持字段级别的自定义配置覆盖自动生成结果
- **大数据表性能** → 分批读取元数据，避免单次查询过多表
- **生成代码质量** → 生成后需开发者review，不适合直接部署到生产环境
- **数据库兼容性** → 第一版仅支持MySQL，后续可扩展其他数据库

## Migration Plan

1. 开发阶段：在本地环境集成代码生成器
2. 测试阶段：选择3-5个典型表进行生成验证
3. 部署阶段：作为工具模块集成到项目中，不影响现有功能
4. 回滚方案：生成器为独立模块，可随时禁用或移除
