## ADDED Requirements

### Requirement: Maven 配置使用 Java 21
项目所有 Maven 模块的 pom.xml 配置文件 SHALL 使用 Java 21 作为编译和运行版本。

#### Scenario: 父 pom.xml 配置 Java 21
- **WHEN** 查看父 pom.xml 的 properties 节点
- **THEN** java.version 属性值为 21

#### Scenario: Maven 编译器插件配置 Java 21
- **WHEN** 查看父 pom.xml 的 maven-compiler-plugin 配置
- **THEN** source 和 target 值均为 21

### Requirement: 项目构建成功
使用 Java 21 编译环境执行 Maven 构建 SHALL 成功完成，无编译错误。

#### Scenario: Maven clean package 构建
- **WHEN** 执行 `mvn clean package` 命令
- **THEN** 构建成功，所有模块生成 JAR 包

### Requirement: 依赖库兼容性验证
项目所有依赖库 SHALL 在 Java 21 环境下正常工作。

#### Scenario: Spring Boot 启动验证
- **WHEN** 在 Java 21 环境下启动应用
- **THEN** Spring Boot 应用成功启动，无兼容性错误

#### Scenario: MyBatis-Plus 功能验证
- **WHEN** 在 Java 21 环境下执行数据库操作
- **THEN** MyBatis-Plus 正常工作，查询和更新操作成功

### Requirement: 开发环境文档更新
项目 README SHALL 更新说明 Java 版本要求为 21。

#### Scenario: README 环境要求说明
- **WHEN** 查看项目 README 的环境要求部分
- **THEN** 明确说明需要 Java 21 或更高版本
