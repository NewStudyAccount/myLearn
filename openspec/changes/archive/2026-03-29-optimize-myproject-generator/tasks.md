## 1. 修复类名生成问题

- [x] 1.1 修改TableReader.toCamelCase()方法，添加capitalize参数控制首字母大写
- [x] 1.2 更新EntityGenerator中调用toCamelCase()的地方，传入capitalize=true
- [x] 1.3 更新GeneratorController中ZIP文件命名逻辑，确保文件名与类名一致
- [x] 1.4 验证生成的Entity、Service、Controller、Mapper类名均为大写开头

## 2. 增强Service层代码生成

- [x] 2.1 修改ServiceGenerator的接口模板，添加getPageList、getOne、listByCondition方法声明
- [x] 2.2 修改ServiceGenerator的实现类模板，添加getPageList、getOne、listByCondition方法实现
- [x] 2.3 确保生成的Service接口继承IService并包含saveBatch、removeByIds方法声明
- [x] 2.4 验证生成的Service实现类正确实现所有接口方法

## 3. 集成测试与验证

- [x] 3.1 为典型表（如sys_user、order_detail）生成代码，验证类名正确性
- [x] 3.2 验证生成的Service层代码可编译通过
- [x] 3.3 测试ZIP下载功能，确保文件命名正确
- [x] 3.4 验证向后兼容性，现有API接口行为不变
