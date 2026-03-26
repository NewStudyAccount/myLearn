## ADDED Requirements

### Requirement: 用户登录功能
系统 SHALL 提供用户登录功能，支持用户名和密码登录。

#### Scenario: 登录成功
- **WHEN** 用户输入正确的用户名和密码并点击登录按钮
- **THEN** 系统验证通过，保存 token 到 localStorage
- **AND** 跳转到首页

#### Scenario: 登录失败
- **WHEN** 用户输入错误的用户名或密码
- **THEN** 系统显示错误提示信息
- **AND** 保持在登录页面

### Requirement: 用户登出功能
系统 SHALL 提供用户登出功能。

#### Scenario: 登出成功
- **WHEN** 用户点击退出登录按钮
- **THEN** 系统清除 localStorage 中的 token
- **AND** 跳转到登录页面

### Requirement: 用户信息管理
系统 SHALL 管理用户登录状态和用户信息。

#### Scenario: 获取用户信息
- **WHEN** 用户登录成功后
- **THEN** 系统获取用户信息（用户名、头像等）
- **AND** 保存到 Pinia Store

#### Scenario: 检查登录状态
- **WHEN** 系统需要判断用户是否已登录
- **THEN** 检查 localStorage 中是否存在有效 token
