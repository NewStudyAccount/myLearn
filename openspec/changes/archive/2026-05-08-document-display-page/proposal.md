## Why

当前文章内容仅在后端以 Markdown 格式存储，前端查看文章时需要先获取原始 Markdown 文本再由前端渲染，增加了前端负担且渲染效果依赖客户端性能。项目后端已集成 Flexmark 库（MarkDownUtil），具备服务端 Markdown 转 HTML 的能力，但尚未对外暴露转换接口。需要新增一个文档展示页，后端将 Markdown 文档转换为 HTML 后返回给前端，前端直接渲染 HTML 内容，提供更流畅的文档阅读体验。

## What Changes

- 后端新增接口：根据文章ID获取已转换为 HTML 格式的文档内容（`/sysArticleContent/queryHtmlByArticleId/{id}`）
- 后端 Service 层新增方法：调用 `MarkDownUtil` 将 Markdown 内容转换为 HTML，封装到新的 VO（`SysArticleContentHtmlVo`）中返回
- 前端新增文档展示页（`/blog/doc/:articleId`），用于展示后端返回的 HTML 文档内容
- 前端新增 API 方法调用后端 HTML 内容接口
- 前端路由注册文档展示页

## Capabilities

### New Capabilities
- `doc-html-render`: 后端 Markdown 转 HTML 接口与前端文档展示页能力，包含后端转换接口、前端展示页面和路由配置

### Modified Capabilities

（无）

## Impact

- **后端代码**：`SysArticleContentController` 新增接口，`SysArticleContentService` 新增方法，新增 `SysArticleContentHtmlVo` 类
- **前端代码**：新增 `views/blog/doc/index.vue` 展示页，新增 API 方法，路由配置新增文档展示页
- **API 变更**：新增 GET 接口 `/project/sysArticleContent/queryHtmlByArticleId/{id}`
- **依赖变更**：无，后端已依赖 Flexmark，前端使用 `v-html` 指令渲染 HTML
