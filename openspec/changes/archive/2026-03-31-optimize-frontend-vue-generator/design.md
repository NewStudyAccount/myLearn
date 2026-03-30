## Context

当前 FrontendVueGenerator 使用静态模板生成 Vue 组件，其 import 语句是硬编码的，无法动态引用 FrontendApiGenerator 生成的具体 API 函数。这导致生成的代码需要手动调整，且缺乏完整的 Vue Composition API 和 Element Plus 组件导入。

## Goals / Non-Goals

**Goals:**
- 实现动态 import 生成，根据实体信息准确引用 API 函数
- 生成完整的 Vue Composition API 导入（ref、reactive、onMounted）
- 生成 Element Plus 组件导入（ElMessage、ElMessageBox）
- 生成准确的 TypeScript 类型导入

**Non-Goals:**
- 改变 FrontendApiGenerator 的模板结构
- 修改生成的 API 函数命名规则
- 影响现有的代码生成功能

## Decisions

1. **动态 import 生成策略**
   - 在 FrontendVueGenerator 的上下文构建中添加 API 函数名列表
   - 根据实体名动态生成具体的 API 函数引用
   - 使用 Velocity 模板的循环和条件语句生成 import 语句

2. **import 语句结构**
   - 将 import 分为三个部分：Vue Composition API、API 函数和类型、Element Plus 组件
   - 确保 import 语句的顺序符合项目编码规范

3. **实现方式**
   - 修改 `getVueTemplate()` 方法中的模板字符串
   - 在 `generate()` 方法中添加所需的上下文变量
   - 保持向后兼容，不影响现有生成逻辑

## Risks / Trade-offs

- **风险**: 动态生成的 import 路径可能不准确
  - **缓解**: 复用 FrontendApiGenerator 的路径生成逻辑
- **风险**: 模板复杂度增加可能影响可维护性
  - **缓解**: 保持模板结构清晰，添加必要的注释
- **权衡**: 增加了生成器的复杂度，但提高了生成代码的质量