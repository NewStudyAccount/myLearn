## Context

myproject-generator 使用 Velocity 模板引擎生成 CRUD 代码。当前模板与实际项目实现存在以下差异：

1. **controller.vm**：使用 GET 分页查询，而实际项目使用 POST + 请求体
2. **list.vue.vm**：使用旧版分页组件，响应格式不匹配
3. **api.ts.vm**：列表查询使用 GET，缺少嵌套分页参数结构

参考实现文件：
- `myproject-system/src/main/java/com/example/controller/SysUserController.java`
- `myproject-frontend/src/views/system/user/index.vue`
- `myproject-frontend/src/api/sysUserApi.ts`

## Goals / Non-Goals

**Goals:**
- 生成的 Controller 支持 POST 分页查询，返回 `TableDataInfo` 格式
- 生成的 Vue 页面使用新的 `Pagination` 组件，参数结构为嵌套 `pageQuery`
- 生成的 API 文件使用 POST 方法查询列表，包含 `PageQuery` 接口

**Non-Goals:**
- 不修改其他未提及的模板（如 formDialog.vue.vm、router.ts.vm 等）
- 不改变生成器的核心逻辑，仅更新模板内容
- 不处理自定义查询条件的动态生成（保持现有逻辑）

## Decisions

1. **Controller 分页改为 POST**  
   实际项目使用 `@RequestBody SysUserQueryPageReq` 接收复杂查询参数，GET 方法无法支持。模板需改为 `@PostMapping("/list")` 并使用请求体。

2. **返回类型使用 TableDataInfo**  
   实际项目的分页查询返回 `TableDataInfo<${className}>`（包含 rows/total），而非 MyBatis Plus 的 `Page` 对象。

3. **Vue 页面分页参数嵌套**  
   实际项目使用 `queryParams.pageQuery.pageNum` 结构，模板需调整为嵌套形式，并使用新的 `Pagination` 组件。

4. **API 使用 POST 方法**  
   列表查询改为 POST 方法，支持复杂查询条件。添加 `PageQuery` 接口定义。

## Risks / Trade-offs

- **风险**：模板更新后，新生成的代码可能与旧项目不兼容 → 仅影响新生成的代码，旧代码不受影响
- **权衡**：需要在模板中添加 `PageQuery` 和 `TableDataInfo` 的导入语句 → 生成器需确保这些类在项目中可用
