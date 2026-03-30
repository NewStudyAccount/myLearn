## Why

项目中存在大量类似的CRUD操作，每次新建一个业务模块都需要手动编写 controller、service、mapper 以及前端页面，耗时且容易出错。通过分析数据库表结构自动生成代码，可以大幅提升开发效率，减少重复工作。

## What Changes

- 新增 `myproject-generator` 模块，用于根据数据库表信息自动生成代码
- 支持生成后端代码：Controller、Service、Mapper、Entity
- 支持生成前端代码：Vue页面、API接口封装
- 提供配置化的模板生成机制，支持自定义模板
- 集成数据库表结构读取能力，自动解析字段类型和注释

## Capabilities

### New Capabilities

- `code-generator`: 数据库表结构读取与代码生成核心能力，包含模板引擎、字段映射、文件输出等
- `generator-controller`: 后端Controller代码自动生成能力，生成标准RESTful API接口
- `generator-service`: 后端Service层代码生成能力，包含CRUD操作方法
- `generator-mapper`: MyBatis Mapper接口及XML生成能力，自动映射SQL操作
- `generator-entity`: 后端Entity实体类生成能力，基于字段类型转换
- `generator-frontend-vue`: 前端Vue页面生成能力，生成列表、新增、编辑、查看页面
- `generator-frontend-api`: 前端API接口封装生成能力，统一请求管理

### Modified Capabilities

- (无)

## Impact

- 后端：新增 `myproject-generator` 模块，依赖现有myproject项目结构
- 前端：新增生成器相关组件和页面
- 数据库：需要读取业务库表结构（无写入权限需求）
- 配置：添加生成器相关配置项