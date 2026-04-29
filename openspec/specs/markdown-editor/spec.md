### Requirement: Markdown 富文本编辑器组件
系统 SHALL 在文章内容编辑表单中集成 Markdown 富文本编辑器，替代原有的 `<el-input>` 单行输入框。编辑器 SHALL 支持所见即所得（wysiwyg）和源码两种编辑模式，用户 SHALL 可在两种模式间自由切换。编辑器输出内容 SHALL 为 Markdown 格式字符串。编辑器 SHALL 支持图片上传回调（onUploadImg），用于将图片上传至 OSS 并插入 Markdown 图片语法。

#### Scenario: 编辑文章内容时显示富文本编辑器
- **WHEN** 用户打开文章内容的新增或编辑表单
- **THEN** 系统显示 Markdown 富文本编辑器，默认为所见即所得模式

#### Scenario: 切换编辑模式
- **WHEN** 用户点击编辑器模式切换按钮
- **THEN** 编辑器在所见即所得模式和源码模式之间切换，已有内容不丢失

#### Scenario: 编辑器输出 Markdown 格式
- **WHEN** 用户在编辑器中完成内容编辑并提交表单
- **THEN** 编辑器输出的 content 字段为 Markdown 格式字符串

#### Scenario: 编辑器支持图片上传
- **WHEN** 用户通过编辑器工具栏或粘贴方式插入图片
- **THEN** 编辑器调用 onUploadImg 回调，由父组件处理图片上传并返回 URL，编辑器自动插入 Markdown 图片语法

### Requirement: Markdown 内容预览
系统 SHALL 在文章内容查看详情时使用 Markdown 渲染组件展示内容，而非纯文本显示。

#### Scenario: 查看文章内容详情时渲染 Markdown
- **WHEN** 用户查看文章内容详情
- **THEN** 系统使用 Markdown 渲染组件将 content 字段渲染为格式化的 HTML 展示

### Requirement: 编辑器动态导入
Markdown 编辑器组件 SHALL 使用动态导入（dynamic import）加载，避免增加首屏加载体积。

#### Scenario: 编辑器按需加载
- **WHEN** 用户首次访问文章内容编辑页面
- **THEN** Markdown 编辑器组件通过动态导入加载，不影响其他页面的加载性能

### Requirement: 文章管理页面集成内容编辑
文章管理页面 SHALL 在操作列提供"编辑内容"按钮。点击后通过 `getSysArticleContentByArticleId` 接口（入参为文章 id）获取文章内容，并打开 Markdown 编辑器弹窗进行编辑。保存时调用 `updateSysArticleContent` 接口更新内容。

#### Scenario: 点击编辑内容按钮加载内容
- **WHEN** 用户在文章管理列表点击某篇文章的"编辑内容"按钮
- **THEN** 系统调用 `getSysArticleContentByArticleId` 接口获取该文章内容，并打开 Markdown 编辑器弹窗显示内容

#### Scenario: 文章无内容记录时打开编辑器
- **WHEN** 用户点击"编辑内容"按钮，但该文章尚无内容记录（接口返回空）
- **THEN** 系统打开空编辑器弹窗，articleId 预填，用户可新增内容

#### Scenario: 保存编辑的内容
- **WHEN** 用户在编辑器弹窗中编辑内容后点击保存
- **THEN** 系统调用 `updateSysArticleContent` 接口保存内容，保存成功后关闭弹窗并提示成功

### Requirement: 移除独立内容管理页面
系统 SHALL 移除独立的文章内容管理页面（`views/blog/content/index.vue`）及其路由配置，内容编辑统一从文章管理页面入口操作。

#### Scenario: 访问内容管理页面
- **WHEN** 用户访问原内容管理页面路由
- **THEN** 系统不再显示该页面，路由已移除
