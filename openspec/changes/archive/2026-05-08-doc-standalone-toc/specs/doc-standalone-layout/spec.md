## MODIFIED Requirements

### Requirement: 文档展示页 SHALL 使用独立全屏布局

系统 SHALL 将文档展示页路由从 MainLayout 子路由移出，作为顶层路由独立注册。文档展示页 SHALL 不包含管理系统侧边栏和顶栏，提供全屏沉浸式阅读体验。

#### Scenario: 访问文档展示页不显示管理系统布局
- **WHEN** 用户访问 `/blog/doc/123` 路径
- **THEN** 页面以全屏模式渲染，不显示 SideMenu 侧边栏和 HeaderNav 顶栏
- **AND** 文档内容占据完整视口宽度

#### Scenario: 文档展示页仍受路由守卫保护
- **WHEN** 未登录用户访问 `/blog/doc/123` 路径
- **THEN** 系统重定向到登录页面

#### Scenario: 从文章列表跳转到文档展示页
- **WHEN** 用户在文章列表页点击"查看文档"按钮
- **THEN** 系统导航到 `/blog/doc/{articleId}`，页面以全屏模式展示

### Requirement: 文档展示页 SHALL 提供返回导航

文档展示页 SHALL 在页面顶部提供返回按钮，允许用户返回文章列表页。

#### Scenario: 点击返回按钮
- **WHEN** 用户在文档展示页点击返回按钮
- **THEN** 系统导航回文章列表页
