## Why

myproject-generator 的代码生成模板与当前项目实际实现存在差异。手写的 Controller、Vue 页面和 API 文件已演进为更规范的模式（如 POST 分页查询、嵌套分页参数、统一响应格式），但生成器模板未同步更新，导致生成的代码需要大量手动修改。

## What Changes

- 更新 `controller.vm`：分页查询改为 POST 方法，使用 `TableDataInfo` 返回类型，支持请求体参数
- 更新 `list.vue.vm`：使用新的 `Pagination` 组件，调整分页参数结构为嵌套 `pageQuery`，适配 `res.data.rows`/`res.data.total` 响应格式
- 更新 `api.ts.vm`：列表查询改为 POST 方法，添加 `PageQuery` 接口，参数结构调整为嵌套分页

## Capabilities

### New Capabilities

（无）

### Modified Capabilities

- `code-generation`: 更新生成器模板以匹配当前项目实现规范

## Impact

- 修改文件：
  - `myproject-generator/src/main/resources/templates/generator/controller.vm`
  - `myproject-generator/src/main/resources/templates/generator/list.vue.vm`
  - `myproject-generator/src/main/resources/templates/generator/api.ts.vm`
- 影响所有后续通过生成器创建的代码
- 生成的代码将与 SysUserController、SysUser 页面、sysUserApi 保持一致
