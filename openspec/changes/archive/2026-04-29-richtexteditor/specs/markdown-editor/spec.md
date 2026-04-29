## ADDED Requirements

### Requirement: Markdown 富文本编辑器组件
系统 SHALL 在文章内容编辑表单中集成 Markdown 富文本编辑器，替代原有的 `<el-input>` 单行输入框。编辑器 SHALL 支持所见即所得（wysiwyg）和源码两种编辑模式，用户 SHALL 可在两种模式间自由切换。编辑器输出内容 SHALL 为 Markdown 格式字符串。

#### Scenario: 编辑文章内容时显示富文本编辑器
- **WHEN** 用户打开文章内容的新增或编辑表单
- **THEN** 系统显示 Markdown 富文本编辑器，默认为所见即所得模式

#### Scenario: 切换编辑模式
- **WHEN** 用户点击编辑器模式切换按钮
- **THEN** 编辑器在所见即所得模式和源码模式之间切换，已有内容不丢失

#### Scenario: 编辑器输出 Markdown 格式
- **WHEN** 用户在编辑器中完成内容编辑并提交表单
- **THEN** 编辑器输出的 content 字段为 Markdown 格式字符串

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
