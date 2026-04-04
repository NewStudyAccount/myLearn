## 1. 模板引擎重构

- [x] 1.1 重构 TemplateEngine 支持从类路径加载 .vm 文件
- [x] 1.2 添加模板加载的文件系统回退机制
- [x] 1.3 添加模板渲染的详细错误日志

## 2. 后端模板文件

- [x] 2.1 创建 entity.vm 模板（带 MyBatis-Plus 注解）
- [x] 2.2 创建 mapper.vm 模板（Mapper 接口）
- [x] 2.3 创建 mapper-xml.vm 模板（MyBatis XML）
- [x] 2.4 创建 service.vm 模板（Service 接口）
- [x] 2.5 创建 service-impl.vm 模板（Service 实现）
- [x] 2.6 创建 controller.vm 模板（REST Controller）
- [x] 2.7 创建 vo.vm 模板（响应 VO）
- [x] 2.8 创建 req.vm 模板（请求 DTO）

## 3. 前端模板文件

- [x] 3.1 创建 api.vm 模板（TypeScript API 函数）
- [x] 3.2 创建 vue.vm 模板（Vue 3 页面组件）

## 4. 生成器重构

- [x] 4.1 重构 EntityGenerator 使用外部模板
- [x] 4.2 重构 MapperGenerator 使用外部模板
- [x] 4.3 重构 ServiceGenerator 使用外部模板
- [x] 4.4 重构 ControllerGenerator 使用外部模板
- [x] 4.5 重构 FrontendApiGenerator 使用外部模板
- [x] 4.6 重构 FrontendVueGenerator 使用外部模板

## 5. 新增生成器类

- [x] 5.1 创建 VoGenerator 用于 VO 生成
- [x] 5.2 创建 ReqGenerator 用于请求 DTO 生成

## 6. 测试与集成

- [ ] 6.1 使用示例表元数据测试模板渲染
- [ ] 6.2 验证生成的代码能正确编译
- [ ] 6.3 通过 GeneratorController 测试完整 CRUD 流程
- [ ] 6.4 验证与现有 API 的向后兼容性