## Why

升级 myproject 的 Java 版本从 17 到 21，以获得最新的语言特性、性能提升和安全更新。Java 21 是 LTS（长期支持）版本，提供虚拟线程（Virtual Threads）、模式匹配、记录类（Records）等新特性，有助于提升开发效率和应用性能。

## What Changes

- 将 Maven 项目中 `java.version` 属性从 17 升级到 21
- 更新 `maven-compiler-plugin` 的 `source` 和 `target` 配置为 21
- 验证所有依赖库（Spring Boot 3.5.7、MyBatis-Plus、Lombok 等）与 Java 21 的兼容性
- 更新 CI/CD 构建环境（如有）以使用 Java 21

## Capabilities

### New Capabilities

- `java-21-upgrade`: 全面升级项目 Java 版本至 21，包括配置更新和兼容性验证

### Modified Capabilities

无

## Impact

- **影响范围**: 所有 Maven 子模块（myproject-common、myproject-server、myproject-system、myproject-blog、myproject-framework、myproject-oss）
- **依赖兼容性**: Spring Boot 3.5.7 已完全支持 Java 21，无需额外调整
- **构建工具**: Maven 编译器插件需更新配置
- **风险**: 需验证所有依赖库（特别是 JWT、FastJSON2 等）在 Java 21 下的兼容性
