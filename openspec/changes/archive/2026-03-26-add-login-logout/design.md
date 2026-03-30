## Context

myproject-frontend 项目需要添加登录/登出功能和类似 RuoYi-Vue 的主布局。当前项目只有基础的示例页面，需要完整的用户认证系统。

## Goals / Non-Goals

**Goals:**
- 实现用户名密码登录功能
- 创建类似 RuoYi-Vue 风格的主布局（左侧菜单 + 顶部导航）
- 实现路由守卫保护需要认证的页面
- 支持退出登录功能
- Token 持久化存储

**Non-Goals:**
- 不实现注册功能
- 不实现验证码功能
- 不实现第三方登录
- 不实现权限控制（后续迭代）

## Decisions

### Decision 1: 登录页面设计
- **选择**: 参考 RuoYi-Vue 的登录页面设计，简洁美观
- **理由**: 用户熟悉 RuoYi-Vue 风格，降低学习成本
- **替代方案**: 自定义设计 - 增加设计成本

### Decision 2: Token 存储
- **选择**: 使用 localStorage 存储 token
- **理由**: 简单直接，页面刷新后保持登录状态
- **替代方案**: 使用 sessionStorage - 关闭浏览器后丢失登录状态

### Decision 3: 主布局结构
- **选择**: 使用 Element Plus 的 Container 布局组件
- **理由**: 与 Element Plus 风格统一，组件成熟
- **替代方案**: 自定义布局组件 - 增加开发成本

### Decision 4: 路由守卫
- **选择**: 在 router.beforeEach 中检查 token
- **理由**: Vue Router 官方推荐方式
- **替代方案**: 使用组件级守卫 - 分散逻辑，不易维护

## Risks / Trade-offs

- **风险**: Token 易被 XSS 攻击窃取 → **缓解**: 后续可升级为 httpOnly cookie
- **风险**: 登录接口未实现，使用模拟数据 → **缓解**: 预留接口对接位置
- **权衡**: 选择了简单实现，暂不支持权限控制

## Migration Plan

1. 创建登录页面组件
2. 创建用户状态管理 Store
3. 创建 API 接口封装
4. 修改路由配置，添加路由守卫
5. 创建主布局组件
6. 测试登录/登出流程
