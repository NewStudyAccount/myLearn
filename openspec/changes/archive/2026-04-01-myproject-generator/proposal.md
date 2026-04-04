## Why

myproject项目需要一个代码生成器，能够根据数据库表结构自动生成前后端代码。目前开发新功能时，需要手动编写前端Vue3组件和后端Java实体类、Controller、Service、Mapper等代码，重复性工作多、效率低且容易出错。通过引入代码生成器，可以大幅提升开发效率，保证代码规范一致性。

## What Changes

- 新增代码生成功能模块，支持从数据库表生成完整代码
- 前端生成：Vue3 + TypeScript组件（列表页、表单页、详情页）、API接口调用代码、路由配置
- 后端生成：MyBatis Plus实体类(Domain)、Mapper接口、Service层、Controller层
- 支持自定义生成配置（表前缀过滤、字段映射、包名配置等）
- 提供在线预览和下载生成代码的能力

## Capabilities

### New Capabilities

- `code-generator`: 代码生成功能核心模块，包含表结构读取、模板引擎、代码生成与下载
- `generator-frontend`: 前端代码生成模块，生成Vue3+TypeScript组件及相关代码
- `generator-backend`: 后端代码生成模块，生成Java实体类、Mapper、Service、Controller代码

### Modified Capabilities

- 无

## Impact

- **新增依赖**：模板引擎（Velocity/FreeMarker）、数据库元数据读取模块
- **前端影响**：新增代码生成器页面、生成配置表单、代码预览组件
- **后端影响**：新增代码生成相关Controller、Service及模板文件
- **数据库影响**：无新增表，读取现有数据库元数据（information_schema）
