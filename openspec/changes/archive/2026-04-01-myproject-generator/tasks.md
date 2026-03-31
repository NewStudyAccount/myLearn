## 1. 后端 - 基础架构搭建

- [x] 1.1 创建代码生成器模块包结构（generator包）
- [x] 1.2 添加Velocity依赖到pom.xml
- [x] 1.3 创建数据库元数据读取服务（DatabaseMetaService）
- [x] 1.4 创建类型映射配置类（TypeMappingConfig），配置MySQL到Java/TypeScript的类型映射

## 2. 后端 - 模板引擎与生成核心

- [x] 2.1 创建Velocity模板引擎工具类（TemplateEngine）
- [x] 2.2 编写后端实体类模板（domain.vm）
- [x] 2.3 编写后端Mapper模板（mapper.vm）
- [x] 2.4 编写后端Service模板（service.vm）
- [x] 2.5 编写后端ServiceImpl模板（serviceImpl.vm）
- [x] 2.6 编写后端Controller模板（controller.vm）

## 3. 后端 - 代码生成服务与API

- [x] 3.1 创建代码生成服务接口（GeneratorService）
- [x] 3.2 实现代码生成服务（GeneratorServiceImpl），集成元数据读取与模板渲染
- [x] 3.3 创建代码生成控制器（GeneratorController），提供preview/download接口
- [x] 3.4 实现ZIP打包下载功能
- [x] 3.5 创建数据库表列表查询接口（读取information_schema.tables）

## 4. 前端 - 页面与组件

- [x] 4.1 创建代码生成器页面目录结构（views/generator/）
- [x] 4.2 创建表选择页面（TableSelect.vue），展示数据库表列表供选择
- [x] 4.3 创建生成配置页面（GeneratorConfig.vue），配置包名、表前缀等选项
- [x] 4.4 创建代码预览组件（CodePreview.vue），支持多文件预览和高亮显示
- [x] 4.5 创建API调用模块（api/generator.ts）

## 5. 前端 - 代码生成模板

- [x] 5.1 编写前端列表页模板（list.vue.vm）
- [x] 5.2 编写前端表单弹窗模板（formDialog.vue.vm）
- [x] 5.3 编写前端API客户端模板（api.ts.vm）
- [x] 5.4 编写前端路由配置模板（router.ts.vm）

## 6. 集成测试与优化

- [x] 6.1 使用3-5个典型表测试后端代码生成
- [x] 6.2 使用3-5个典型表测试前端代码生成
- [x] 6.3 验证生成代码可正常编译运行
- [x] 6.4 优化模板输出格式和代码风格
