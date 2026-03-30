## Context

myproject 前端项目使用 Vue 3 + TypeScript + Pinia + Vue-Router + Axios 技术栈。后端已提供认证和菜单 API：
- `POST /project/auth/login` - 登录接口
出参格式：
```json
{
  "code": "200",
  "msg": "登录成功",
  "data": {
    "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpbl91c2VyX2lkIjoxfQ.ono_-z8bI28kHxByyCtZ5584MVbVMvpvEdfbV-jqeuc"
  }
}
```
- `POST /project/auth/me` - 获取用户信息
出参格式：
```json
{
  "code": "200",
  "msg": "success",
  "data": {
    "permissions": [
      "*:*:*",
      "s"
    ],
    "roles": [
      "admin"
    ],
    "user": {
      "sysUserDto": {
        "userId": "1",
        "userName": "admin",
        "userPwd": "$2a$10$xB8E7korwpq54qUErSS1Re.yhFDjHDxc67P2e6nradgGBnLnC3zvG",
        "userAvatorUrl": null,
        "userSex": "1",
        "userPhone": null
      },
      "permissionList": [
        "s",
        "s",
        "*:*:*"
      ],
      "enabled": true,
      "password": "$2a$10$xB8E7korwpq54qUErSS1Re.yhFDjHDxc67P2e6nradgGBnLnC3zvG",
      "username": "admin",
      "credentialsNonExpired": true,
      "accountNonExpired": true,
      "accountNonLocked": true
    }
  }
}
```
- `POST /project/menu/tree` - 获取动态路由菜单
出参格式：
```json
{
  "code": "200",
  "msg": "success",
  "data": [
    {
      "menuId": 1,
      "menuName": "系统管理",
      "perCode": "s",
      "menuType": "M",
      "menuSort": 1,
      "parentId": 0,
      "path": "/system",
      "component": null,
      "componentName": null
    },
    {
      "menuId": 2,
      "menuName": "文章管理",
      "perCode": "s",
      "menuType": "M",
      "menuSort": 2,
      "parentId": 0,
      "path": "/blog",
      "component": null,
      "componentName": null
    }
  ]
}
```

需要在前端实现完整的登录流程，包括页面、状态管理、路由守卫和动态路由生成。

## Goals / Non-Goals

**Goals:**
- 创建登录页面，实现用户名密码登录
- 登录后自动获取用户信息和动态路由菜单
- 使用 Pinia 管理用户状态和路由状态
- 实现路由守卫保护需要认证的页面
- 根据后端返回的菜单数据动态生成前端路由

**Non-Goals:**
- 不修改后端 API 接口
- 不实现第三方登录（如 OAuth）
- 不实现密码重置功能

## Decisions

### 决策 1：登录后顺序调用接口
- **选择**: 登录成功后先获取用户信息，再获取动态路由菜单
- **理由**: 用户信息和路由菜单是独立的接口，顺序调用更清晰
- **备选方案**: 并行调用两个接口，但需要处理部分失败的情况

### 决策 2：使用 Pinia 管理状态
- **选择**: 创建 auth store 管理用户信息，使用现有状态管理规范
- **理由**: 项目已使用 Pinia，保持一致性
- **风险**: 页面刷新后状态丢失，需要从 localStorage 恢复

### 决策 3：动态路由使用 addRoute
- **选择**: 使用 Vue-Router 的 router.addRoute() 动态添加路由
- **理由**: 标准做法，支持按需加载路由
- **风险**: 路由重复添加问题，需要在添加前检查

## Risks / Trade-offs

- **风险**: Token 过期后接口返回 401 → **缓解**: 使用 Axios 拦截器统一处理
- **风险**: 动态路由添加后刷新页面丢失 → **缓解**: 在路由守卫中重新获取
- **权衡**: 登录后加载时间可能较长（两个接口串行调用）
