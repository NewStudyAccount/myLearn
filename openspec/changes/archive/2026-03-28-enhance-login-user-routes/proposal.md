## Why

前端项目需要完成登录流程的完整集成，包括：用户登录后调用后端接口获取用户信息、获取动态路由菜单，并将这些数据用于前端路由和导航。当前后端 API 已存在（AuthController、SysMenuController），但前端尚未实现完整的登录流程。

## What Changes

- 创建前端登录页面，调用 `/project/auth/login` 接口进行登录
- 登录成功后调用 `/project/auth/me` 接口获取当前用户信息
- 登录成功后调用 `/project/menu/tree` 接口获取动态路由菜单
- 使用 Pinia 存储用户信息和路由数据
- 实现路由守卫，未登录时跳转到登录页
- 根据获取的菜单数据动态生成前端路由

## Capabilities

### New Capabilities

- `login-flow`: 完整的前端登录流程，包含登录页面、用户信息获取、动态路由加载

### Modified Capabilities

- `user-authentication`: 扩展用户认证流程，增加用户信息和动态路由获取

## Impact

- **前端模块**: myproject-frontend
- **API 调用**: `/project/auth/login`、`/project/auth/me`、`/project/menu/tree`
- **状态管理**: Pinia store 需要管理用户信息和路由状态
- **路由管理**: Vue-Router 需要支持动态路由添加
