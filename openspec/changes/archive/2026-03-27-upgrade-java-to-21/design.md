## Context

myproject 是一个基于 Spring Boot 3.5.7 的多模块 Maven 项目，包含 6 个子模块。当前使用 Java 17 作为编译和运行版本。项目依赖包括 MyBatis-Plus、JWT、FastJSON2、AWS S3 SDK 等第三方库。需要将 Java 版本从 17 升级到 21 以获取最新的 LTS 特性。

## Goals / Non-Goals

**Goals:**
- 将项目 Java 版本从 17 升级到 21
- 确保所有依赖库与 Java 21 兼容
- 验证构建流程在 Java 21 下正常运行

**Non-Goals:**
- 不引入 Java 21 新特性（如虚拟线程）到现有代码中
- 不升级 Spring Boot 版本（3.5.7 已支持 Java 21）
- 不修改项目业务逻辑

## Decisions

### 决策 1：使用 Java 21 LTS 版本
- **选择**: 升级到 Java 21
- **理由**: Java 21 是最新的 LTS 版本，提供长期支持，且 Spring Boot 3.5.7 已完全支持
- **备选方案**: 维持 Java 17（缺乏新特性）、升级到 Java 22+（非 LTS 版本）

### 决策 2：仅修改 Maven 配置
- **选择**: 仅更新 pom.xml 中的 java.version 和 maven-compiler-plugin 配置
- **理由**: Spring Boot 3.5.7 已兼容 Java 21，无需升级依赖版本
- **风险**: 需验证所有依赖库（特别是 JWT、FastJSON2）在 Java 21 下的兼容性

### 决策 3：不修改源代码
- **选择**: 不使用 Java 21 新特性重构现有代码
- **理由**: 升级范围最小化，避免引入不必要的风险

## Risks / Trade-offs

- **风险**: 某些依赖库可能与 Java 21 不兼容 → **缓解**: 升级后运行完整测试验证
- **风险**: 开发环境需要安装 Java 21 → **缓解**: 在 README 中说明环境要求
- **权衡**: 暂不使用虚拟线程等新特性，保持代码稳定性
