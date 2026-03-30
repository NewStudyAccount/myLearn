## ADDED Requirements

### Requirement: 登录页面展示
系统 SHALL 提供登录页面，包含用户名和密码输入框及登录按钮。

#### Scenario: 访问登录页
- **WHEN** 用户访问 `/login` 路径
- **THEN** 系统展示登录页面，包含用户名输入框、密码输入框和登录按钮

### Requirement: 用户登录认证
系统 SHALL 调用后端接口进行用户认证。

#### Scenario: 登录成功
- **WHEN** 用户输入正确的用户名和密码并点击登录按钮
- **THEN** 系统调用 `POST /project/auth/login` 接口
- **AND** 登录成功后保存 token 到 localStorage
- **AND** 跳转到首页

#### Scenario: 登录失败
- **WHEN** 用户输入错误的用户名或密码
- **THEN** 系统显示错误提示信息
- **AND** 保持在登录页面

### Requirement: 登录后获取用户信息
用户登录成功后，系统 SHALL 调用接口获取当前登录用户信息。

#### Scenario: 获取用户信息成功
- **WHEN** 用户登录成功后
- **THEN** 系统调用 `POST /project/auth/me` 接口
- **AND** 将用户信息（用户名、头像、角色等）保存到 Pinia Store

#### Scenario: 获取用户信息失败
- **WHEN** 调用获取用户信息接口失败
- **THEN** 系统清除 token 并跳转到登录页

### Requirement: 登录后获取动态路由菜单
用户登录成功后，系统 SHALL 调用接口获取动态路由菜单数据。

#### Scenario: 获取动态路由成功
- **WHEN** 用户登录成功后
- **THEN** 系统调用 `POST /project/menu/tree` 接口
- **AND** 将菜单数据保存到 Pinia Store
- **AND** 根据菜单数据动态添加路由到 Vue-Router

#### Scenario: 获取动态路由失败
- **WHEN** 调用获取动态路由接口失败
- **THEN** 系统显示错误提示

### Requirement: 路由守卫
系统 SHALL 实现路由守卫，保护需要认证的页面。

#### Scenario: 未登录访问受保护页面
- **WHEN** 用户未登录时访问受保护页面
- **THEN** 系统重定向到登录页

#### Scenario: 已登录访问登录页
- **WHEN** 用户已登录时访问登录页
- **THEN** 系统重定向到首页

#### Scenario: 页面刷新恢复状态
- **WHEN** 用户刷新页面且 localStorage 中有 token
- **THEN** 系统重新获取用户信息和动态路由
- **AND** 正常展示页面
