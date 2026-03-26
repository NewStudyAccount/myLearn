## Why

前端项目 myproject-frontend 当前只有基础的示例页面，缺少用户认证功能。需要添加登录/登出功能，页面风格类似 RuoYi-Vue，包括登录页面和登录后的主布局（左侧菜单 + 顶部导航 + 内容区域）。

## What Changes

- 创建登录页面（用户名、密码登录，无验证码）
- 创建主布局组件（类似 RuoYi-Vue 风格）
  - 左侧菜单栏（可折叠）
  - 顶部导航栏（显示用户信息、退出按钮）
  - 内容区域（支持标签页切换）
- 创建用户状态管理（Pinia Store）
- 实现路由守卫（未登录跳转到登录页）
- 创建 API 接口封装（登录、登出、获取用户信息）

## Capabilities

### New Capabilities
- `user-authentication`: 用户认证功能，包括登录、登出、token 管理
- `main-layout`: 主布局组件，类似 RuoYi-Vue 风格
- `route-guard`: 路由守卫，保护需要认证的页面

### Modified Capabilities
（无现有 spec 需要修改）

## Impact

- 新增文件：
  - `src/views/LoginView.vue` - 登录页面
  - `src/components/layout/MainLayout.vue` - 主布局组件
  - `src/components/layout/HeaderNav.vue` - 顶部导航组件
  - `src/components/layout/SideMenu.vue` - 左侧菜单组件
  - `src/stores/user.ts` - 用户状态管理
  - `src/api/auth.ts` - 认证 API
- 修改文件：
  - `src/router/index.ts` - 添加路由守卫和路由配置
  - `src/main.ts` - 可能需要调整
