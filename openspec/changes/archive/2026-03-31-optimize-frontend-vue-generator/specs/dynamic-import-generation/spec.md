## ADDED Requirements

### Requirement: 动态生成 API 函数 import
系统 SHALL 根据实体信息动态生成具体的 API 函数 import 语句，引用 FrontendApiGenerator 生成的函数。

#### Scenario: 生成单个实体 API 函数 import
- **WHEN** 生成 SysUser 实体的 Vue 组件
- **THEN** 生成 `import { getListSysUser, getByIdSysUser, createSysUser, updateSysUser, deleteSysUser } from '@/api/sysUserApi'`

#### Scenario: 生成多个 API 函数 import
- **WHEN** 生成任意实体的 Vue 组件
- **THEN** 根据实体名生成对应的五个 CRUD API 函数 import

### Requirement: 动态生成 TypeScript 类型 import
系统 SHALL 根据实体信息动态生成 TypeScript 类型 import 语句。

#### Scenario: 生成实体类型 import
- **WHEN** 生成 SysUser 实体的 Vue 组件
- **THEN** 生成 `import type { SysUser } from '@/api/sysUserApi'`

### Requirement: 生成 Vue Composition API import
系统 SHALL 自动生成 Vue Composition API 的 import 语句。

#### Scenario: 生成 Vue 核心 API import
- **WHEN** 生成任意实体的 Vue 组件
- **THEN** 生成 `import { ref, reactive, onMounted } from 'vue'`

### Requirement: 生成 Element Plus 组件 import
系统 SHALL 自动生成 Element Plus 组件的 import 语句。

#### Scenario: 生成消息组件 import
- **WHEN** 生成任意实体的 Vue 组件
- **THEN** 生成 `import { ElMessage, ElMessageBox } from 'element-plus'`

### Requirement: import 语句顺序规范
系统 SHALL 按照特定顺序生成 import 语句。

#### Scenario: 生成正确顺序的 import
- **WHEN** 生成任意实体的 Vue 组件
- **THEN** import 语句按以下顺序生成：
  1. Vue Composition API
  2. API 函数和类型
  3. Element Plus 组件