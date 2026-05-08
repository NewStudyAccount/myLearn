## ADDED Requirements

### Requirement: 后端 SHALL 提供根据文章ID获取 HTML 格式文档内容的接口

系统 SHALL 提供 `GET /project/sysArticleContent/queryHtmlByArticleId/{id}` 接口，接收文章ID作为路径参数，返回包含 HTML 格式内容的 `SysArticleContentHtmlVo` 对象。该对象 SHALL 包含 `id`（内容ID）、`articleId`（文章ID）和 `htmlContent`（HTML 格式内容）三个字段。

#### Scenario: 成功获取文章的 HTML 内容
- **WHEN** 客户端调用 `GET /project/sysArticleContent/queryHtmlByArticleId/{id}` 接口，传入有效的文章ID
- **THEN** 系统返回 200 状态码，响应体中 `data.htmlContent` 为该文章 Markdown 内容经 Flexmark 转换后的 HTML 字符串，`data.id` 为内容记录ID，`data.articleId` 为文章ID

#### Scenario: 文章ID不存在
- **WHEN** 客户端调用接口传入不存在的文章ID
- **THEN** 系统返回错误响应，提示文章内容不存在

#### Scenario: 文章存在但无内容记录
- **WHEN** 客户端调用接口传入的文章ID对应文章存在，但该文章尚无内容记录
- **THEN** 系统返回错误响应，提示文章内容不存在

### Requirement: 后端 SHALL 使用 Flexmark Kramdown 模式将 Markdown 转换为 HTML

系统 SHALL 在 Service 层调用 `MarkDownUtil.toKramdownHtml()` 方法将 Markdown 内容转换为 HTML，支持表格、脚注、定义列表、缩写等扩展语法。

#### Scenario: 包含表格的 Markdown 内容转换
- **WHEN** 文章 Markdown 内容包含 GFM 表格语法
- **THEN** 转换后的 HTML 中包含 `<table>` 元素，表格正确渲染

#### Scenario: 空 Markdown 内容转换
- **WHEN** 文章 Markdown 内容为空或 null
- **THEN** 转换后的 HTML 内容为空字符串

### Requirement: 前端 SHALL 提供文档展示页面

系统 SHALL 在 `/blog/doc/:articleId` 路径下提供文档展示页面，页面 SHALL 从后端获取 HTML 格式内容并使用 `v-html` 指令渲染展示。页面 SHALL 显示文章标题和 HTML 文档内容。

#### Scenario: 通过路由访问文档展示页
- **WHEN** 用户访问 `/blog/doc/123` 路径
- **THEN** 页面调用后端接口获取文章ID为 123 的 HTML 内容，并渲染展示

#### Scenario: 文档内容加载中
- **WHEN** 页面正在请求后端接口获取 HTML 内容
- **THEN** 页面显示加载状态（loading）

#### Scenario: 文档内容加载失败
- **WHEN** 后端接口返回错误或网络异常
- **THEN** 页面显示错误提示信息，并提供返回按钮

#### Scenario: 文档内容成功加载
- **WHEN** 后端接口成功返回 HTML 内容
- **THEN** 页面使用 `v-html` 渲染 HTML 内容，并应用文档展示样式，确保排版美观

### Requirement: 文档展示页 SHALL 提供返回导航

文档展示页 SHALL 提供返回按钮或导航链接，允许用户返回文章列表页。

#### Scenario: 点击返回按钮
- **WHEN** 用户在文档展示页点击返回按钮
- **THEN** 系统导航回文章列表页

### Requirement: 前端 API 层 SHALL 提供获取 HTML 内容的方法

前端 `sysArticleContentApi.ts` SHALL 新增 `getSysArticleContentHtmlByArticleId` 方法，调用后端 HTML 内容接口。

#### Scenario: 调用 API 获取 HTML 内容
- **WHEN** 前端调用 `getSysArticleContentHtmlByArticleId(id)` 方法
- **THEN** 方法向 `GET /sysArticleContent/queryHtmlByArticleId/{id}` 发起请求，返回包含 `htmlContent` 字段的响应数据
