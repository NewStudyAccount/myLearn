## ADDED Requirements

### Requirement: 编辑器图片上传
Markdown 编辑器 SHALL 支持图片上传功能。用户通过编辑器工具栏图片按钮或粘贴图片时，系统 SHALL 将图片上传至 OSS 并自动将图片 URL 以 Markdown 语法插入编辑器内容中。

#### Scenario: 通过工具栏上传图片
- **WHEN** 用户点击编辑器工具栏的图片按钮并选择图片文件
- **THEN** 系统将图片上传至 OSS，上传成功后在编辑器光标位置插入 `![图片描述](oss文件URL)` 格式的 Markdown 图片语法

#### Scenario: 粘贴图片上传
- **WHEN** 用户在编辑器中粘贴图片
- **THEN** 系统将粘贴的图片上传至 OSS，上传成功后在编辑器光标位置插入 Markdown 图片语法

#### Scenario: 上传失败提示
- **WHEN** 图片上传失败
- **THEN** 系统显示上传失败的错误提示，编辑器内容不变
