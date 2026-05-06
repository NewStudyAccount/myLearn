## Context

项目基于 Spring Boot 3.5 + Vue 3 的前后端分离架构。后端 myproject-blog 模块已集成 Flexmark 库，并提供了 `MarkDownUtil` 工具类，支持将 Markdown 文本转换为 HTML（支持 CommonMark、Kramdown、MultiMarkdown、Markdown 等多种解析模式）。当前文章内容以 Markdown 格式存储在 OSS 中，前端通过 `queryByArticleId` 接口获取原始 Markdown 文本，再使用 `md-editor-v3` 的 `MdPreview` 组件在前端渲染。

现有数据流：前端请求 → 后端从 OSS 下载 .md 文件 → 返回 Markdown 原文 → 前端 MdPreview 组件渲染。

本次变更的目标是在后端完成 Markdown → HTML 的转换，前端直接展示 HTML 内容，减少前端渲染负担，同时提供独立的文档展示页面。

## Goals / Non-Goals

**Goals:**
- 后端新增接口，根据文章ID返回已转换为 HTML 的文档内容
- 前端新增独立的文档展示页面，使用 `v-html` 渲染后端返回的 HTML
- 文档展示页支持从文章列表页跳转进入
- 复用现有的 `MarkDownUtil` 工具类进行服务端转换

**Non-Goals:**
- 不修改现有的 `queryByArticleId` 接口（保持向后兼容）
- 不替换前端 MarkdownEditor 编辑功能
- 不实现 HTML 内容的 XSS 过滤（当前为内部管理系统，内容来源可信）
- 不实现文档展示页的 SEO 优化（非公开站点）

## Decisions

### 1. 后端新增独立接口返回 HTML 内容

**选择**：新增 `GET /project/sysArticleContent/queryHtmlByArticleId/{id}` 接口，返回包含 HTML 内容的 `SysArticleContentHtmlVo`

**理由**：与现有 `queryByArticleId` 接口分离，保持向后兼容。现有接口返回 Markdown 原文用于编辑场景，新接口返回 HTML 用于展示场景，职责清晰。

**备选方案**：
- 在现有接口增加 `format=html` 参数：侵入性强，需修改现有接口逻辑
- 前端自行转换：增加前端负担，且 Flexmark 已在后端可用，服务端转换更高效

### 2. 使用 Kramdown 解析模式作为默认转换模式

**选择**：使用 `MarkDownUtil.toKramdownHtml()` 进行转换

**理由**：Kramdown 模式支持最多的扩展语法（缩写、定义列表、脚注、表格、排版扩展），能提供最丰富的渲染效果，适合文档展示场景。

**备选方案**：
- CommonMark：标准规范但扩展语法支持少，表格等无法渲染
- MultiMarkdown：介于两者之间，但扩展性不如 Kramdown

### 3. 前端使用 `v-html` 直接渲染 HTML

**选择**：使用 Vue 的 `v-html` 指令渲染后端返回的 HTML 字符串

**理由**：后端已通过 Flexmark 将 Markdown 转为标准 HTML，前端无需再次解析。`v-html` 是 Vue 内置指令，无需引入额外依赖。内容来源为系统内部用户编辑，安全性可控。

**备选方案**：
- 使用 iframe + srcdoc：隔离性好但样式控制复杂，交互受限
- 使用第三方 HTML 渲染组件：引入不必要的依赖

### 4. 文档展示页使用独立路由

**选择**：在 `/blog/doc/:articleId` 路径下新增文档展示页

**理由**：与文章管理页面（`/blog/article`）分离，职责清晰。通过路由参数传递文章ID，支持直接通过 URL 访问特定文档。

## Risks / Trade-offs

- **[XSS 风险]** → 使用 `v-html` 渲染后端返回的 HTML 存在 XSS 风险。当前为内部管理系统，内容由可信用户编辑，风险可控。若后续开放给外部用户，需引入 HTML 消毒库（如 OWASP Java HTML Sanitizer）
- **[样式一致性]** → 后端 Flexmark 生成的 HTML 不包含样式，需前端提供 CSS 样式表确保渲染效果美观。需编写文档展示专用的 CSS 样式
- **[转换性能]** → 每次请求都需从 OSS 下载 Markdown 文件并执行 Flexmark 转换。可后续引入 Caffeine 缓存优化，当前文章量较小，暂不实施
