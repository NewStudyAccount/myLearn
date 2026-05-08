## 1. 后端 VO 与 Service 层

- [x] 1.1 在 `myproject-blog` 模块 `com.example.domain.vo` 包下创建 `SysArticleContentHtmlVo` 类，包含 `id`（Long）、`articleId`（Long）、`htmlContent`（String）三个字段，使用 `@Data` 注解
- [x] 1.2 在 `SysArticleContentService` 接口中新增 `queryHtmlByArticleId(Long id)` 方法，返回类型为 `SysArticleContentHtmlVo`
- [x] 1.3 在 `SysArticleContentServiceImpl` 中实现 `queryHtmlByArticleId` 方法：复用现有逻辑从 OSS 获取 Markdown 内容，调用 `MarkDownUtil.toKramdownHtml()` 转换为 HTML，封装到 `SysArticleContentHtmlVo` 返回

## 2. 后端 Controller 层

- [x] 2.1 在 `SysArticleContentController` 中新增 `GET /queryHtmlByArticleId/{id}` 接口，调用 Service 层 `queryHtmlByArticleId` 方法，返回 `Response<SysArticleContentHtmlVo>`
- [x] 2.2 为新接口添加 `@Operation(summary = "根据文章ID获取HTML格式内容")` 注解

## 3. 前端 API 层

- [x] 3.1 在 `sysArticleContentApi.ts` 中新增 `SysArticleContentHtml` 接口定义，包含 `id`、`articleId`、`htmlContent` 字段
- [x] 3.2 在 `sysArticleContentApi.ts` 中新增 `getSysArticleContentHtmlByArticleId(id: number)` 方法，调用 `GET /sysArticleContent/queryHtmlByArticleId/{id}`

## 4. 前端文档展示页

- [x] 4.1 创建 `myproject-frontend/src/views/blog/doc/index.vue` 文档展示页组件，包含文章标题区域、HTML 内容渲染区域（使用 `v-html`）、返回按钮
- [x] 4.2 在文档展示页中实现加载状态（loading）和错误提示逻辑
- [x] 4.3 编写文档展示页的 CSS 样式，确保 HTML 内容（标题、段落、表格、代码块、列表等）排版美观

## 5. 前端路由与导航

- [x] 5.1 在前端路由配置中注册 `/blog/doc/:articleId` 路由，指向 `views/blog/doc/index.vue`，设置 `meta: { requiresAuth: true, title: '文档展示' }`
- [x] 5.2 在文章管理列表页（`views/blog/article/index.vue`）操作列新增"查看文档"按钮，点击后跳转到 `/blog/doc/{articleId}`
