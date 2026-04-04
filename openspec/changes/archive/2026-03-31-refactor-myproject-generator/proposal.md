## 现状与问题

当前 myproject-generator 代码生成器使用 Java 字符串作为模板，存在以下问题：
- 模板硬编码在 Java 文件中，难以维护和扩展
- 模板与生成逻辑耦合，无法独立修改模板内容
- CRUD 功能不完整（缺少 VO、Req、PageRequest）
- 无法在不修改 Java 代码的情况下添加新的模板类型

现在需要重构为基于 Velocity 模板文件的代码生成器，支持前后端完整 CRUD 代码生成。

## 变更内容

1. 模板外部化：将所有代码生成模板从 Java 代码提取为独立的 .vm 模板文件
2. 后端模板：创建基于 MyBatis-Plus 的完整 CRUD 模板（Entity、Mapper、Service、Controller）
3. 前端模板：创建 Vue 3 + TypeScript 完整 CRUD 模板（API、Vue 页面）
4. 模板引擎优化：重构 TemplateEngine 支持从文件系统加载模板
5. 生成能力扩展：支持生成 VO、Req、PageRequest 等完整分层架构代码

## 能力

### 新增能力

- velocity-template-engine：基于 Velocity 模板文件的代码生成引擎
- mybatis-plus-backend-crud：MyBatis-Plus 后端完整 CRUD 代码生成
- vue3-frontend-crud：Vue 3 前端完整 CRUD 代码生成

### 修改能力

- myproject-generator：现有代码生成器重构为模板文件形式

## 影响

- 影响模块：myproject-generator
- 依赖变化：需要添加 velocity-tools 或保持现有 velocity 依赖
- 前端依赖：保持现有 Vue 3 + Element Plus 技术栈
- 后端依赖：保持 MyBatis-Plus 3.x