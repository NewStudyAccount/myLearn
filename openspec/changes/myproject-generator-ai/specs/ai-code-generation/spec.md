## ADDED Requirements

### Requirement: 代码生成编排
系统 SHALL 根据解析后的需求信息，编排并调用各个代码生成器。

#### Scenario: 生成后端代码
- **WHEN** 用户确认需求解析结果并选择生成后端代码
- **THEN** 系统将解析结果转换为 TableInfo 对象
- **AND** 按顺序调用 EntityGenerator、MapperGenerator、ServiceGenerator、ControllerGenerator
- **AND** 收集所有生成的代码
- **AND** 返回生成结果

#### Scenario: 生成前端代码
- **WHEN** 用户确认需求解析结果并选择生成前端代码
- **THEN** 调用 FrontendApiGenerator 生成 API 接口文件
- **AND** 调用 FrontendVueGenerator 生成 Vue 组件
- **AND** 返回生成结果

#### Scenario: 选择性生成
- **WHEN** 用户仅勾选部分代码类型（如只生成 Entity 和 Service）
- **THEN** 系统仅调用选中的生成器
- **AND** 跳过未选中的代码生成
- **AND** 返回选中部分的代码

### Requirement: TableInfo 对象转换
系统 SHALL 将 AI 解析结果转换为现有生成器兼容的 TableInfo 对象。

#### Scenario: 字段类型映射
- **WHEN** 转换解析结果到 TableInfo
- **THEN** 将 Java 类型映射到数据库类型（String→VARCHAR、Integer→INT、Date→DATETIME）
- **AND** 设置字段长度和精度
- **AND** 设置字段约束（NOT NULL、DEFAULT）
- **AND** 设置字段注释

#### Scenario: 主键和索引
- **WHEN** 转换解析结果到 TableInfo
- **THEN** 自动设置 id 字段为主键（PRIMARY KEY AUTO_INCREMENT）
- **AND** 根据查询需求添加索引（如用户名、邮箱字段）
- **AND** 为常用查询字段创建普通索引
- **AND** 为唯一约束字段创建唯一索引

### Requirement: 完整 CRUD 代码生成
系统 SHALL 生成包含完整 CRUD 功能的代码。

#### Scenario: 生成 Entity 实体类
- **WHEN** 调用 EntityGenerator
- **THEN** 生成 Java 实体类，包含所有字段
- **AND** 添加 Lombok 注解（@Data、@Builder 等）
- **AND** 添加 MyBatis-Plus 注解（@TableName、@TableId、@TableField）
- **AND** 添加字段校验注解（@NotNull、@Size、@Email 等）
- **AND** 添加 Swagger 文档注解（@Schema）

#### Scenario: 生成 Mapper 接口和 XML
- **WHEN** 调用 MapperGenerator
- **THEN** 生成 Mapper 接口，继承 BaseMapper<Entity>
- **AND** 添加自定义查询方法声明（如 selectByUsername）
- **AND** 生成 MyBatis XML 映射文件
- **AND** 在 XML 中定义 resultMap
- **AND** 在 XML 中实现自定义 SQL（列表查询、条件查询等）

#### Scenario: 生成 Service 层
- **WHEN** 调用 ServiceGenerator
- **THEN** 生成 Service 接口，定义业务方法
- **AND** 生成 ServiceImpl 实现类，继承 ServiceImpl<Mapper, Entity>
- **AND** 实现分页列表查询（PageHelper 或 MyBatis-Plus 分页）
- **AND** 实现条件查询（支持多字段组合查询）
- **AND** 实现新增、更新、删除方法
- **AND** 添加事务注解（@Transactional）
- **AND** 添加业务校验逻辑（如唯一性检查）

#### Scenario: 生成 Controller 层
- **WHEN** 调用 ControllerGenerator
- **THEN** 生成 RestController，提供 RESTful API
- **AND** 实现分页列表接口（GET /api/{entity}/list）
- **AND** 实现详情查询接口（GET /api/{entity}/{id}）
- **AND** 实现新增接口（POST /api/{entity}）
- **AND** 实现更新接口（PUT /api/{entity}/{id}）
- **AND** 实现删除接口（DELETE /api/{entity}/{id}）
- **AND** 实现批量删除接口（DELETE /api/{entity}/batch）
- **AND** 添加参数校验（@Valid、@Validated）
- **AND** 使用统一返回格式（Result<T>）
- **AND** 添加完整的 Swagger API 文档注解

### Requirement: 前端代码生成
系统 SHALL 生成完整的前端代码，包括 API 接口和 Vue 组件。

#### Scenario: 生成 TypeScript API 文件
- **WHEN** 调用 FrontendApiGenerator
- **THEN** 生成 TypeScript API 文件（{entity}Api.ts）
- **AND** 定义 TypeScript 类型接口（{Entity}Type、{Entity}Query、PageResult 等）
- **AND** 封装所有 API 调用方法（基于 Axios）
- **AND** 包含 getList（分页列表）、getDetail（详情）、create（新增）、update（更新）、delete（删除）、batchDelete（批量删除）方法
- **AND** 添加 JSDoc 注释

#### Scenario: 生成 Vue 3 组件
- **WHEN** 调用 FrontendVueGenerator
- **THEN** 生成 Vue 3 SFC 组件（{entity}/index.vue）
- **AND** 使用 Composition API（setup 语法糖）
- **AND** 导入必要的 Vue API（ref、reactive、onMounted 等）
- **AND** 导入 Element Plus 组件（ElTable、ElForm、ElDialog 等）
- **AND** 导入生成的 API 方法
- **AND** 实现数据列表展示（表格）
- **AND** 实现分页功能
- **AND** 实现搜索表单
- **AND** 实现新增/编辑对话框
- **AND** 实现表单校验规则
- **AND** 实现所有 CRUD 操作的事件处理

### Requirement: 代码质量保证
系统 SHALL 确保生成的代码符合规范和最佳实践。

#### Scenario: 命名规范
- **WHEN** 生成任何代码
- **THEN** 遵循 Java 命名规范（类名用大驼峰、方法名用小驼峰）
- **AND** 遵循数据库命名规范（表名和字段名用下划线）
- **AND** 遵循 TypeScript 命名规范
- **AND** 变量和方法名语义化

#### Scenario: 代码格式
- **WHEN** 生成任何代码
- **THEN** 使用标准的代码缩进（4个空格或2个空格，根据项目配置）
- **AND** 合理的空行和换行
- **AND** 符合 CheckStyle/ESLint 规则

#### Scenario: 注释文档
- **WHEN** 生成任何代码
- **THEN** 类和方法添加 JavaDoc/JSDoc 注释
- **AND** 复杂逻辑添加行内注释
- **AND** 注释内容清晰、准确

### Requirement: 代码预览
系统 SHALL 提供生成代码的在线预览功能。

#### Scenario: 预览生成代码
- **WHEN** 代码生成完成
- **THEN** 在前端界面展示所有生成的文件
- **AND** 使用标签页分组展示（后端代码、前端代码）
- **AND** 每个文件显示文件路径和代码内容
- **AND** 代码使用语法高亮显示
- **AND** 提供代码复制按钮

### Requirement: 代码下载
系统 SHALL 提供生成代码的批量下载功能。

#### Scenario: 打包下载
- **WHEN** 用户点击下载按钮
- **THEN** 系统将所有生成的文件打包为 ZIP
- **AND** 按照项目目录结构组织文件
- **AND** 后端代码放入 src/main/java/{package} 目录
- **AND** Mapper XML 放入 src/main/resources/mapper 目录
- **AND** 前端代码放入 src/api 和 src/views 目录
- **AND** 提供 ZIP 文件下载

### Requirement: AI 代码优化（可选）
系统 MAY 使用 AI 对生成的代码进行优化。

#### Scenario: 优化变量命名
- **WHEN** 生成的代码中存在不够语义化的变量名
- **THEN** AI 分析变量用途
- **AND** 建议更好的变量名
- **AND** 应用优化（可选）

#### Scenario: 优化代码结构
- **WHEN** 生成的代码逻辑较复杂
- **THEN** AI 分析代码
- **AND** 建议拆分方法或提取公共逻辑
- **AND** 应用优化（可选）

### Requirement: 错误处理
系统 SHALL 妥善处理代码生成过程中的各种错误。

#### Scenario: 生成器调用失败
- **WHEN** 调用某个代码生成器失败
- **THEN** 记录详细的错误日志
- **AND** 返回错误信息给用户
- **AND** 其他生成器继续执行（部分失败不影响整体）

#### Scenario: 模板解析失败
- **WHEN** 代码模板解析失败
- **THEN** 捕获异常并记录
- **AND** 返回模板错误信息
- **AND** 提供模板修复建议
