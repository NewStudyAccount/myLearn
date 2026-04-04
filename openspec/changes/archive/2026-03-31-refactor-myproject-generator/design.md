## 背景

当前 myproject-generator 使用 Java 字符串作为模板，主要问题包括：
- 模板硬编码在 Java 文件中，难以维护
- 模板与生成逻辑没有分离
- CRUD 功能不完整（缺少 VO、Req、PageRequest）
- 无法在不修改 Java 代码的情况下添加新的模板类型

生成器需要支持：
- 从数据库读取表元数据
- 生成基于 MyBatis-Plus 的后端代码（Entity、Mapper、Service、Controller）
- 生成 Vue 3 前端代码（API、Vue 页面）
- 前后端完整 CRUD 操作

## 目标 / 非目标

### 目标

1. 将模板提取为外部 .vm 文件，便于修改
2. 创建后端完整 CRUD 模板（Entity、Mapper、Service、Controller、VO、Req）
3. 创建前端完整 CRUD 模板（API、Vue 页面）
4. 重构 TemplateEngine 支持从文件系统加载模板
5. 保持 MyBatis-Plus 3.x 兼容性

### 非目标

1. 改变底层数据库读取机制
2. 添加新的数据库支持（保持 MySQL/PostgreSQL）
3. 支持其他前端框架（保持 Vue 3）
4. 添加代码编译/验证功能

## 技术决策

## 1. 模板存储位置
决策：将模板存储在 src/main/resources/templates/ 目录下
- 理由：标准 Maven 资源位置，易于打包和加载
- 备选：外部目录 - 更灵活但更难打包

## 2. 模板加载策略
决策：使用类路径资源加载，回退到文件系统
- 理由：在开发和生产环境中都能工作
- 备选：仅文件系统 - 更简单但需要手动管理路径

## 3. 后端模板结构
决策：为每一层创建独立模板
- entity.vm - 带 MyBatis-Plus 注解的 Entity 类
- mapper.vm - Mapper 接口
- mapper-xml.vm - MyBatis XML
- service.vm - Service 接口
- service-impl.vm - Service 实现
- controller.vm - REST Controller
- vo.vm - 响应 VO
- req.vm - 请求 DTO

## 4. 前端模板结构
决策：创建 API 和 Vue 页面的独立模板
- api.vm - TypeScript API 函数
- vue.vm - Vue 3 页面组件

## 5. CRUD 操作覆盖
决策：支持完整 CRUD 操作
- 后端：create、read（list/single）、update、delete
- 前端：分页列表、创建弹窗、编辑弹窗、查看弹窗、删除

## 风险与权衡

1. 模板维护 - 多个模板文件可能更难管理
   - 缓解：使用一致的命名和结构

2. Velocity 版本兼容性 - 需要确保 Velocity 2.x 兼容性
   - 缓解：使用正确的配置

3. 模板调试 - 更难调试模板错误
   - 缓解：在 TemplateEngine 中添加详细错误日志

4. 破坏性变更 - 重构可能会破坏现有生成器用户
   - 缓解：在 GeneratorController API 中保持向后兼容