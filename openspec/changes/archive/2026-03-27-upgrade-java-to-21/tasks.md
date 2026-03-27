## 1. 更新 Maven 配置

- [x] 1.1 修改父 pom.xml 的 java.version 属性从 17 改为 21
- [x] 1.2 修改父 pom.xml 的 maven-compiler-plugin source 和 target 为 21

## 2. 验证构建

- [ ] 2.1 执行 mvn clean package 验证所有模块编译成功
- [ ] 2.2 检查构建输出，确认无编译警告或错误

## 3. 验证依赖兼容性

- [ ] 3.1 启动应用验证 Spring Boot 正常工作
- [ ] 3.2 执行数据库操作验证 MyBatis-Plus 兼容性
- [ ] 3.3 验证 JWT 认证功能正常
- [ ] 3.4 验证 FastJSON2 序列化/反序列化正常

## 4. 更新文档

- [ ] 4.1 更新 README 中的 Java 版本要求说明
