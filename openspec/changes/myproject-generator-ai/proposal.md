## Why

当前的代码生成器（myproject-generator）虽然能够基于数据库表结构生成完整的前后端代码，但存在以下局限性：

1. **需要预先定义表结构**：必须先在数据库中创建表，然后才能生成代码
2. **缺乏业务理解能力**：无法根据自然语言的业务需求自动推断表结构和业务逻辑
3. **定制化程度低**：生成的代码风格固定，难以适应不同项目的特殊需求
4. **需要手工配置**：需要手动配置表前缀、实体前缀等参数
5. **学习成本高**：开发者需要了解数据库设计、代码结构等技术细节

引入 AI 技术可以让代码生成器更加智能化，真正实现"描述即实现"的开发体验，大幅提升开发效率。

## What Changes

创建全新的 myproject-generator-ai 模块，在现有 myproject-generator 基础上增加以下核心能力：

### 核心变更
1. **AI 需求解析引擎**
   - 接收自然语言业务需求描述
   - 使用 AI 模型提取实体、字段、业务规则等结构化信息
   - 自动推断数据库表结构和字段类型

2. **智能代码生成引擎**
   - 基于 AI 理解的业务需求生成代码
   - 复用现有的代码生成器（Entity、Service、Controller、Frontend等）
   - 使用 AI 优化代码质量和可读性

3. **配置管理系统**
   - 保存和重用代码生成配置
   - 支持多项目、多技术栈配置
   - 配置版本管理和导入导出

4. **代码模板管理**
   - 可视化管理代码模板
   - 支持自定义模板
   - 模板版本控制

5. **Web 管理界面**
   - 需求输入界面
   - 代码预览和在线编辑
   - 配置管理界面
   - 模板管理界面

## Capabilities

### New Capabilities
- `ai-requirement-parsing`: AI 需求解析能力，将自然语言转换为结构化代码配置
- `table-structure-inference`: 表结构智能推断，根据业务需求自动生成表结构
- `ai-code-generation`: AI 代码生成能力，生成更智能、更规范的代码
- `configuration-management`: 配置管理能力，保存和重用生成配置
- `template-management`: 模板管理能力，自定义代码模板
- `code-preview-download`: 代码预览和下载能力
- `multi-project-support`: 多项目支持能力
- `ai-model-integration`: AI 模型集成能力（OpenAI/Claude/本地LLM）
- `feedback-optimization`: 反馈和优化能力，持续改进代码质量

### Modified Capabilities
- `backend-code-generation`: 增强后端代码生成，支持 AI 优化
- `frontend-code-generation`: 增强前端代码生成，支持 AI 优化
- `crud-generation`: 增强 CRUD 生成，支持更复杂的业务场景

## Impact

### 新增模块
- 创建 `myproject-generator-ai` 模块
- 不影响现有 `myproject-generator` 模块的功能

### 依赖变更
- 新增 AI 框架依赖（LangChain4j 或 Spring AI）
- 新增模板引擎依赖（可能升级 FreeMarker/Velocity）

### 数据库变更
- 新增配置管理表（ai_generator_config）
- 新增模板管理表（ai_generator_template）
- 新增反馈记录表（ai_generator_feedback）

### API 变更
- 新增 AI 代码生成接口：`POST /api/ai-generator/generate`
- 新增配置管理接口：`/api/ai-generator/config/*`
- 新增模板管理接口：`/api/ai-generator/template/*`

### 前端变更
- 新增 AI 代码生成页面
- 新增配置管理页面
- 新增模板管理页面

### 风险评估
- **低风险**：独立模块，不影响现有功能
- **中等成本**：需要 AI API 调用费用（可通过本地模型降低）
- **学习曲线**：用户需要学习如何准确描述需求
