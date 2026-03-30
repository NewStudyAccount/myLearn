# myproject-generator-ai 模块提案

## 概述
myproject-generator-ai 是一个基于 AI 的智能代码生成模块，旨在通过自然语言描述自动生成完整的前后端代码，包括前端页面、API接口、后端Controller/Service/Mapper等，并提供完善的基础CRUD功能。

## ADDED Requirements

### Requirement: AI代码生成核心引擎
系统 SHALL 提供基于 AI 的代码生成核心引擎，支持通过自然语言描述生成代码。

#### Scenario: AI引擎初始化成功
- **WHEN** 系统启动时
- **THEN** 初始化 AI 模型连接（支持 OpenAI/Claude/本地模型）
- **AND** 加载代码生成模板库
- **AND** 初始化提示词工程配置

#### Scenario: AI模型切换
- **WHEN** 管理员配置不同的AI模型
- **THEN** 系统支持动态切换 OpenAI、Claude、本地LLM等模型
- **AND** 保存模型配置到数据库

### Requirement: 自然语言需求解析
系统 SHALL 解析用户的自然语言需求描述，提取关键业务信息。

#### Scenario: 需求解析成功
- **WHEN** 用户输入自然语言需求描述（如"创建一个用户管理模块，包含用户列表、新增、编辑、删除功能"）
- **THEN** AI引擎解析需求，提取实体信息（表名、字段列表）
- **AND** 识别业务功能（CRUD操作、查询条件、排序规则等）
- **AND** 生成结构化的代码生成配置

#### Scenario: 需求描述不完整
- **WHEN** 用户输入的需求描述信息不足
- **THEN** 系统提示用户补充必要信息（如表名、关键字段）
- **AND** 提供需求描述模板供参考

### Requirement: 数据库表结构智能推断
系统 SHALL 根据业务需求自动推断数据库表结构。

#### Scenario: 从需求推断表结构
- **WHEN** AI引擎解析需求成功
- **THEN** 根据实体名称生成表名（遵循命名规范）
- **AND** 根据业务功能推断字段列表（名称、类型、约束）
- **AND** 自动添加通用字段（id、create_time、update_time等）
- **AND** 生成建表SQL语句

#### Scenario: 利用现有表结构
- **WHEN** 用户指定使用已存在的数据库表
- **THEN** 系统读取表结构信息
- **AND** 基于现有表结构生成代码

### Requirement: 后端代码生成
系统 SHALL 生成完整的后端代码，包括Entity、Mapper、Service、Controller。

#### Scenario: 生成Entity实体类
- **WHEN** 表结构确定后
- **THEN** 生成Java实体类，包含所有字段及getter/setter
- **AND** 添加JPA/MyBatis注解
- **AND** 添加字段校验注解（@NotNull、@Size等）
- **AND** 生成Swagger文档注解

#### Scenario: 生成Mapper接口和XML
- **WHEN** Entity生成完成
- **THEN** 生成Mapper接口，继承BaseMapper
- **AND** 生成MyBatis XML映射文件，包含基础CRUD SQL
- **AND** 根据业务需求生成自定义查询方法

#### Scenario: 生成Service层
- **WHEN** Mapper生成完成
- **THEN** 生成Service接口，定义业务方法
- **AND** 生成ServiceImpl实现类，实现CRUD业务逻辑
- **AND** 添加事务注解和异常处理
- **AND** 实现分页查询、条件查询等通用功能

#### Scenario: 生成Controller层
- **WHEN** Service生成完成
- **THEN** 生成RestController，提供RESTful API接口
- **AND** 实现标准CRUD接口（GET、POST、PUT、DELETE）
- **AND** 添加参数校验和统一返回格式
- **AND** 生成完整的Swagger API文档注解

### Requirement: 前端API接口代码生成
系统 SHALL 生成前端API接口调用代码。

#### Scenario: 生成TypeScript API文件
- **WHEN** 后端Controller生成完成
- **THEN** 生成TypeScript API接口文件（xxxApi.ts）
- **AND** 基于Axios封装所有API调用方法
- **AND** 定义TypeScript类型接口（Request、Response类型）
- **AND** 添加API接口注释文档

#### Scenario: API接口方法完整性
- **WHEN** 生成API接口文件
- **THEN** 包含列表查询方法（支持分页、条件查询）
- **AND** 包含详情查询方法
- **AND** 包含新增方法
- **AND** 包含更新方法
- **AND** 包含删除方法（单个删除、批量删除）

### Requirement: 前端Vue页面代码生成
系统 SHALL 生成完整的Vue 3前端页面代码。

#### Scenario: 生成Vue列表页面
- **WHEN** API接口代码生成完成
- **THEN** 生成Vue 3组件（使用Composition API）
- **AND** 集成Element Plus表格组件展示数据列表
- **AND** 实现分页功能
- **AND** 实现搜索和筛选功能
- **AND** 添加新增、编辑、删除操作按钮

#### Scenario: 生成表单对话框
- **WHEN** 生成列表页面时
- **THEN** 在同一文件中生成新增/编辑表单对话框
- **AND** 使用Element Plus Form组件
- **AND** 实现表单校验规则
- **AND** 支持表单提交和重置

#### Scenario: 生成Pinia状态管理
- **WHEN** 需要跨组件状态管理时
- **THEN** 生成Pinia Store文件
- **AND** 定义状态、Actions和Getters
- **AND** 集成API接口调用

### Requirement: 完善的CRUD功能
系统 SHALL 生成的代码包含完善的基础CRUD操作。

#### Scenario: 列表查询功能
- **WHEN** 访问列表页面
- **THEN** 展示数据列表（支持分页）
- **AND** 支持多条件组合查询
- **AND** 支持排序（升序、降序）
- **AND** 显示总记录数

#### Scenario: 新增功能
- **WHEN** 点击新增按钮
- **THEN** 弹出新增表单对话框
- **AND** 输入数据并校验
- **AND** 提交后调用新增API
- **AND** 成功后刷新列表并提示

#### Scenario: 编辑功能
- **WHEN** 点击编辑按钮
- **THEN** 弹出编辑表单对话框，回显数据
- **AND** 修改数据并校验
- **AND** 提交后调用更新API
- **AND** 成功后刷新列表并提示

#### Scenario: 删除功能
- **WHEN** 点击删除按钮
- **THEN** 弹出确认对话框
- **AND** 确认后调用删除API
- **AND** 成功后刷新列表并提示

#### Scenario: 批量删除功能
- **WHEN** 勾选多条记录并点击批量删除
- **THEN** 弹出确认对话框
- **AND** 确认后调用批量删除API
- **AND** 成功后刷新列表并提示

### Requirement: 代码质量和规范
系统 SHALL 生成符合规范和最佳实践的代码。

#### Scenario: 代码符合项目规范
- **WHEN** 生成任何代码
- **THEN** 遵循项目命名规范（驼峰命名、下划线命名等）
- **AND** 遵循代码格式规范（缩进、换行等）
- **AND** 添加完整的注释文档
- **AND** 符合ESLint/CheckStyle规则

#### Scenario: 代码可读性和可维护性
- **WHEN** 生成代码
- **THEN** 结构清晰，职责单一
- **AND** 变量和方法命名语义化
- **AND** 适当拆分复杂逻辑
- **AND** 避免硬编码，使用配置

### Requirement: 代码生成配置管理
系统 SHALL 提供灵活的代码生成配置管理。

#### Scenario: 保存生成配置
- **WHEN** 用户完成代码生成
- **THEN** 支持保存生成配置到数据库
- **AND** 包含表结构、业务规则、模板选择等配置
- **AND** 支持配置命名和版本管理

#### Scenario: 重用历史配置
- **WHEN** 用户需要重新生成代码
- **THEN** 可以加载历史保存的配置
- **AND** 支持修改配置后重新生成
- **AND** 支持配置导入导出（JSON格式）

### Requirement: 代码预览和下载
系统 SHALL 提供代码预览和批量下载功能。

#### Scenario: 在线预览生成代码
- **WHEN** 代码生成完成
- **THEN** 在Web界面展示所有生成的代码文件
- **AND** 支持语法高亮显示
- **AND** 支持代码复制

#### Scenario: 打包下载代码
- **WHEN** 用户确认生成的代码
- **THEN** 将所有文件打包为ZIP文件
- **AND** 按照项目目录结构组织文件
- **AND** 提供下载链接

### Requirement: 代码模板管理
系统 SHALL 提供代码模板管理功能。

#### Scenario: 管理代码模板
- **WHEN** 管理员访问模板管理页面
- **THEN** 可以查看所有代码模板（Entity、Service、Controller等）
- **AND** 支持编辑和自定义模板
- **AND** 支持添加新模板
- **AND** 支持模板版本管理

#### Scenario: 选择代码模板
- **WHEN** 用户生成代码时
- **THEN** 可以选择使用不同的模板风格
- **AND** 支持模板预览
- **AND** 不同模板生成不同风格的代码

### Requirement: AI训练和优化
系统 SHALL 支持AI模型的持续训练和优化。

#### Scenario: 收集用户反馈
- **WHEN** 用户使用生成的代码
- **THEN** 可以对代码质量进行评价（1-5星）
- **AND** 可以提交改进建议
- **AND** 系统记录反馈数据

#### Scenario: 模型优化迭代
- **WHEN** 收集足够的反馈数据
- **THEN** 分析反馈数据，识别常见问题
- **AND** 调整提示词工程策略
- **AND** 优化代码生成模板
- **AND** 提升代码生成质量

### Requirement: 多项目支持
系统 SHALL 支持管理和生成多个项目的代码。

#### Scenario: 项目配置管理
- **WHEN** 管理员创建新项目配置
- **THEN** 配置项目名称、技术栈、数据库连接等信息
- **AND** 配置代码生成路径和包名规范
- **AND** 保存项目配置

#### Scenario: 切换项目生成代码
- **WHEN** 用户选择不同的项目
- **THEN** 加载对应项目的配置
- **AND** 基于项目配置生成代码
- **AND** 代码符合该项目的规范

### Requirement: API接口和前端界面
系统 SHALL 提供RESTful API和Web管理界面。

#### Scenario: RESTful API
- **WHEN** 外部系统调用生成接口
- **THEN** 提供POST `/api/ai-generator/generate`接口
- **AND** 接受自然语言需求描述
- **AND** 返回生成的代码（JSON格式）
- **AND** 提供代码下载接口

#### Scenario: Web管理界面
- **WHEN** 用户访问代码生成页面
- **THEN** 提供需求输入表单
- **AND** 提供表结构预览和编辑功能
- **AND** 提供代码生成选项配置（勾选生成哪些代码）
- **AND** 实时展示代码生成进度
- **AND** 提供代码预览和下载功能

## 技术方案

### 技术栈
- **后端框架**: Spring Boot 3.x
- **AI集成**: LangChain4j / Spring AI
- **模板引擎**: FreeMarker / Velocity
- **数据库**: MySQL（存储配置和模板）
- **前端框架**: Vue 3 + TypeScript + Element Plus

### 模块结构
```
myproject-generator-ai/
├── src/main/java/com/example/generator/ai/
│   ├── controller/        # REST控制器
│   ├── service/           # 业务服务层
│   ├── core/              # 核心生成引擎
│   │   ├── AiEngine.java              # AI引擎封装
│   │   ├── PromptBuilder.java         # 提示词构建器
│   │   ├── CodeTemplateLoader.java    # 模板加载器
│   │   ├── EntityAiGenerator.java     # Entity生成器
│   │   ├── ServiceAiGenerator.java    # Service生成器
│   │   ├── ControllerAiGenerator.java # Controller生成器
│   │   ├── FrontendAiGenerator.java   # 前端代码生成器
│   │   └── CrudGenerator.java         # CRUD功能生成器
│   ├── domain/            # 领域对象
│   ├── config/            # 配置类
│   └── utils/             # 工具类
├── src/main/resources/
│   ├── templates/         # 代码模板
│   │   ├── entity.ftl
│   │   ├── service.ftl
│   │   ├── controller.ftl
│   │   ├── frontend-api.ftl
│   │   └── frontend-vue.ftl
│   └── prompts/           # AI提示词模板
│       ├── entity-prompt.txt
│       ├── service-prompt.txt
│       └── frontend-prompt.txt
```

### 核心流程
1. **需求解析**: 接收自然语言描述 → AI解析 → 提取结构化信息
2. **表结构生成**: 推断/读取表结构 → 生成字段映射
3. **代码生成**: 加载模板 → AI填充内容 → 生成代码文件
4. **质量检查**: 语法检查 → 规范检查 → 返回结果
5. **打包下载**: 组织文件结构 → ZIP打包 → 提供下载

## 实施计划

### Phase 1: 基础架构（2周）
- 搭建项目结构
- 集成AI模型（OpenAI/本地LLM）
- 实现基础的提示词工程
- 完成需求解析功能

### Phase 2: 后端代码生成（3周）
- 实现Entity生成器
- 实现Mapper生成器
- 实现Service生成器
- 实现Controller生成器
- 完善CRUD功能

### Phase 3: 前端代码生成（3周）
- 实现前端API接口生成器
- 实现Vue页面生成器
- 实现表单对话框生成
- 集成Pinia状态管理

### Phase 4: 管理功能（2周）
- 实现配置管理
- 实现模板管理
- 实现代码预览和下载
- 完善Web管理界面

### Phase 5: 优化和测试（2周）
- 代码质量优化
- 性能优化
- 全面测试
- 文档完善

## 预期收益

1. **开发效率提升**: 从需求到代码，大幅减少重复劳动
2. **代码质量保证**: AI生成的代码符合规范，减少人为错误
3. **降低门槛**: 非专业开发者也能快速生成可用代码
4. **持续优化**: 通过反馈不断改进生成质量
5. **标准化**: 统一代码风格和项目结构

## 风险和挑战

1. **AI生成准确性**: 可能需要多次迭代优化提示词
2. **复杂业务场景**: 简单CRUD之外的复杂逻辑可能需要人工调整
3. **成本控制**: API调用费用（可通过本地模型降低）
4. **学习曲线**: 用户需要学习如何准确描述需求

## 总结

myproject-generator-ai 模块通过引入AI技术，将代码生成提升到新的高度，不仅能生成基础的CRUD代码，还能根据自然语言需求智能推断业务逻辑，真正实现"描述即实现"的开发体验。
