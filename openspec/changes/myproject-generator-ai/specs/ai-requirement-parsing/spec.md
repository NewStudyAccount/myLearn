## ADDED Requirements

### Requirement: 自然语言需求输入
系统 SHALL 提供自然语言需求输入接口，接收用户的业务需求描述。

#### Scenario: 接收需求描述
- **WHEN** 用户在前端输入业务需求文本
- **THEN** 系统接收并验证输入内容非空
- **AND** 将需求文本传递给 AI 解析引擎

### Requirement: AI 需求解析
系统 SHALL 使用 AI 模型解析自然语言需求，提取结构化信息。

#### Scenario: 解析成功
- **WHEN** AI 引擎接收需求描述（如"创建用户管理模块，包含用户名、邮箱、手机号字段，支持列表查询、新增、编辑、删除功能"）
- **THEN** AI 模型分析需求文本
- **AND** 提取实体名称（User/用户）
- **AND** 提取字段列表（username、email、phone）
- **AND** 提取业务操作（列表查询、新增、编辑、删除）
- **AND** 返回结构化的 JSON 结果

#### Scenario: 解析失败 - 信息不足
- **WHEN** 用户输入的需求描述过于简单或模糊
- **THEN** AI 引擎返回错误提示
- **AND** 系统提示用户补充必要信息（如实体名称、关键字段）
- **AND** 提供需求描述模板供参考

#### Scenario: 解析失败 - AI 服务异常
- **WHEN** AI API 调用失败（网络错误、超时、限流等）
- **THEN** 系统捕获异常并记录日志
- **AND** 返回友好的错误提示给用户
- **AND** 提供重试选项

### Requirement: 实体信息提取
系统 SHALL 从需求描述中提取实体相关信息。

#### Scenario: 提取实体名称
- **WHEN** AI 解析需求成功
- **THEN** 提取实体的中文名称（如"用户"）
- **AND** 生成英文实体名（如"User"）
- **AND** 生成数据库表名（如"sys_user"，遵循命名规范）

#### Scenario: 提取字段列表
- **WHEN** AI 解析需求成功
- **THEN** 提取所有业务字段（名称、类型、描述）
- **AND** 自动添加通用字段（id、create_time、update_time、create_by、update_by）
- **AND** 推断字段的 Java 类型（String、Integer、Date 等）
- **AND** 推断字段的数据库类型（VARCHAR、INT、DATETIME 等）
- **AND** 推断字段约束（是否必填、最大长度等）

### Requirement: 业务操作识别
系统 SHALL 识别需求描述中的业务操作类型。

#### Scenario: 识别 CRUD 操作
- **WHEN** AI 解析需求成功
- **THEN** 识别是否需要列表查询功能
- **AND** 识别是否需要详情查询功能
- **AND** 识别是否需要新增功能
- **AND** 识别是否需要编辑功能
- **AND** 识别是否需要删除功能
- **AND** 识别是否需要批量删除功能

#### Scenario: 识别查询条件
- **WHEN** 需求描述中提到查询条件（如"按用户名搜索"）
- **THEN** 提取查询字段列表
- **AND** 识别查询类型（精确匹配、模糊查询、范围查询等）

#### Scenario: 识别排序规则
- **WHEN** 需求描述中提到排序（如"按创建时间倒序"）
- **THEN** 提取排序字段
- **AND** 识别排序方向（升序、降序）

### Requirement: 结果验证
系统 SHALL 验证 AI 解析结果的合理性。

#### Scenario: 验证实体信息
- **WHEN** AI 返回解析结果
- **THEN** 验证实体名称非空
- **AND** 验证至少包含一个业务字段
- **AND** 验证字段类型合法

#### Scenario: 验证失败
- **WHEN** 解析结果验证不通过
- **THEN** 返回具体的验证错误信息
- **AND** 允许用户手动编辑修正

### Requirement: 结果预览和编辑
系统 SHALL 允许用户预览和编辑 AI 解析结果。

#### Scenario: 展示解析结果
- **WHEN** AI 解析成功
- **THEN** 在前端展示解析出的实体信息
- **AND** 以可编辑表格展示字段列表
- **AND** 展示识别出的业务操作

#### Scenario: 用户手动调整
- **WHEN** 用户对解析结果不满意
- **THEN** 允许修改实体名称和表名
- **AND** 允许添加、删除、编辑字段
- **AND** 允许修改字段类型、长度、约束等属性
- **AND** 允许勾选/取消业务操作

### Requirement: 提示词优化
系统 SHALL 持续优化 AI 提示词以提高解析准确率。

#### Scenario: 使用结构化提示词
- **WHEN** 构建 AI 请求
- **THEN** 使用预定义的提示词模板
- **AND** 明确指定输出格式（JSON Schema）
- **AND** 提供技术栈上下文信息

#### Scenario: 使用 Few-Shot 示例
- **WHEN** 构建 AI 请求
- **THEN** 在提示词中包含典型示例
- **AND** 示例覆盖常见业务场景
- **AND** 引导 AI 理解期望的输出格式

### Requirement: 性能优化
系统 SHALL 优化 AI 请求性能，降低响应时间。

#### Scenario: 请求缓存
- **WHEN** 收到相同的需求描述
- **THEN** 检查缓存中是否存在解析结果
- **AND** 如果存在，直接返回缓存结果
- **AND** 如果不存在，调用 AI 并缓存结果

#### Scenario: 异步处理
- **WHEN** AI 解析耗时较长
- **THEN** 前端显示加载动画
- **AND** 后端异步调用 AI API
- **AND** 实时更新解析进度（可选）
