## Context

当前 myproject-generator 模块已经实现了基于数据库表结构的代码生成功能，包括后端的 Entity、Mapper、Service、Controller 和前端的 API、Vue 组件。但这种方式要求开发者先创建数据库表，然后才能生成代码，且无法理解业务需求的自然语言描述。

随着 AI 技术的成熟，特别是大语言模型（LLM）在代码生成领域的突破，我们有机会创建一个更智能的代码生成器。通过引入 AI 能力，开发者只需描述业务需求，系统就能自动推断表结构、生成完整的前后端代码，真正实现"需求驱动开发"。

## Goals / Non-Goals

**Goals:**
1. 实现 AI 需求解析能力，将自然语言转换为结构化配置
2. 实现表结构智能推断，自动生成数据库设计
3. 复用现有代码生成器，生成高质量的前后端代码
4. 提供配置管理功能，支持保存和重用配置
5. 提供模板管理功能，支持自定义代码模板
6. 构建完整的 Web 管理界面
7. 支持多种 AI 模型（OpenAI、Claude、本地 LLM）
8. 支持多项目、多技术栈配置

**Non-Goals:**
1. 不替换现有的 myproject-generator 模块
2. 不改变现有代码生成器的核心逻辑
3. 不处理极其复杂的业务逻辑（如工作流、权限系统）
4. 不提供在线代码编辑器（仅支持预览和下载）

## Decisions

### 1. 技术选型

**AI 框架选择：LangChain4j**
- **理由**：
  - 专为 Java 生态设计，与 Spring Boot 集成良好
  - 支持多种 LLM 提供商（OpenAI、Azure OpenAI、本地模型等）
  - 提供提示词模板、对话管理、工具调用等高级功能
  - 社区活跃，文档完善
- **备选方案**：Spring AI（较新，功能尚不完善）

**提示词工程策略：结构化提示 + Few-Shot Learning**
- 使用结构化的提示词模板，明确定义输入输出格式
- 提供典型示例（Few-Shot），提高 AI 理解准确度
- 采用链式思考（Chain-of-Thought）提示，引导 AI 逐步推理

**代码生成策略：AI + 模板混合模式**
- AI 负责理解需求、推断结构、生成配置
- 传统模板引擎负责生成最终代码（复用现有生成器）
- 结合两者优势：AI 的智能性 + 模板的稳定性

### 2. 架构设计

**分层架构：**
```
Controller 层（REST API）
    ↓
Service 层（业务逻辑）
    ↓
Core 层（核心引擎）
├── AiEngine（AI 能力封装）
├── RequirementParser（需求解析）
├── TableStructureInferrer（表结构推断）
├── CodeGeneratorOrchestrator（代码生成编排）
└── TemplateManager（模板管理）
    ↓
Integration 层（外部集成）
├── 现有 Generator（Entity、Service、Controller 等）
└── AI Provider（OpenAI、Claude、Local LLM）
```

**核心流程设计：**
```
1. 接收自然语言需求
   ↓
2. AI 解析需求 → 提取实体、字段、业务规则
   ↓
3. AI 推断表结构 → 生成字段定义、约束、索引
   ↓
4. 生成 TableInfo 对象（与现有 Generator 兼容）
   ↓
5. 调用现有 Generator → 生成各层代码
   ↓
6. （可选）AI 优化代码 → 改进命名、添加注释
   ↓
7. 返回生成结果 → 预览/下载
```

### 3. 数据模型设计

**ai_generator_config（配置表）**
```sql
CREATE TABLE ai_generator_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    config_name VARCHAR(100) NOT NULL COMMENT '配置名称',
    project_name VARCHAR(100) NOT NULL COMMENT '项目名称',
    requirement_text TEXT COMMENT '需求描述',
    table_structure JSON COMMENT '表结构配置',
    generation_options JSON COMMENT '生成选项',
    created_by VARCHAR(50),
    created_time DATETIME,
    updated_time DATETIME,
    version INT DEFAULT 1 COMMENT '版本号'
);
```

**ai_generator_template（模板表）**
```sql
CREATE TABLE ai_generator_template (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    template_name VARCHAR(100) NOT NULL COMMENT '模板名称',
    template_type VARCHAR(50) NOT NULL COMMENT '模板类型（entity/service/controller等）',
    template_content TEXT NOT NULL COMMENT '模板内容',
    template_language VARCHAR(20) COMMENT '模板语言（Java/TypeScript等）',
    is_default BOOLEAN DEFAULT FALSE COMMENT '是否默认模板',
    created_time DATETIME,
    updated_time DATETIME,
    version INT DEFAULT 1
);
```

**ai_generator_feedback（反馈表）**
```sql
CREATE TABLE ai_generator_feedback (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    config_id BIGINT COMMENT '关联配置ID',
    rating INT COMMENT '评分（1-5星）',
    feedback_text TEXT COMMENT '反馈内容',
    improvement_suggestions TEXT COMMENT '改进建议',
    created_time DATETIME
);
```

### 4. AI 提示词设计

**需求解析提示词模板：**
```
你是一个数据库设计专家和业务分析师。请分析以下业务需求，提取关键信息：

需求描述：
{requirement}

项目技术栈：
- 后端：Spring Boot + MyBatis
- 前端：Vue 3 + TypeScript + Element Plus
- 数据库：MySQL

请按以下 JSON 格式返回分析结果：
{
  "entities": [
    {
      "name": "实体名称",
      "tableName": "数据库表名",
      "description": "实体描述",
      "fields": [
        {
          "name": "字段名",
          "type": "字段类型（Java类型）",
          "dbType": "数据库类型",
          "length": "长度",
          "required": true/false,
          "description": "字段描述"
        }
      ]
    }
  ],
  "operations": ["列表查询", "新增", "编辑", "删除"],
  "businessRules": ["业务规则描述"]
}

注意事项：
1. 自动添加通用字段：id, create_time, update_time
2. 遵循命名规范：表名用下划线，Java类名用驼峰
3. 合理选择字段类型和长度
```

**表结构推断提示词模板：**
```
基于以下实体信息，生成完整的数据库表结构设计：

实体信息：
{entityInfo}

请生成 CREATE TABLE 语句，包括：
1. 完整的字段定义（类型、长度、默认值、注释）
2. 主键约束
3. 必要的索引（根据查询需求）
4. 外键约束（如果有关联关系）

返回格式：
```sql
CREATE TABLE xxx (...);
CREATE INDEX idx_xxx ON xxx(...);
```
```

### 5. 实现细节

**AI 能力封装（AiEngine）：**
```java
@Service
public class AiEngine {
    private final ChatLanguageModel chatModel;
    
    public <T> T parse(String prompt, Class<T> responseType) {
        // 使用 LangChain4j 的结构化输出
        return chatModel.chat(prompt).content().as(responseType);
    }
    
    public String generate(String template, Map<String, Object> variables) {
        String prompt = PromptTemplate.from(template).apply(variables);
        return chatModel.generate(prompt);
    }
}
```

**需求解析器（RequirementParser）：**
```java
@Service
public class RequirementParser {
    @Autowired
    private AiEngine aiEngine;
    
    public RequirementAnalysis parse(String requirement) {
        String prompt = loadPromptTemplate("requirement-parsing.txt");
        prompt = prompt.replace("{requirement}", requirement);
        return aiEngine.parse(prompt, RequirementAnalysis.class);
    }
}
```

**代码生成编排器（CodeGeneratorOrchestrator）：**
```java
@Service
public class CodeGeneratorOrchestrator {
    @Autowired
    private EntityGenerator entityGenerator;
    @Autowired
    private ServiceGenerator serviceGenerator;
    // ... 其他生成器
    
    public GeneratedCode generate(TableInfo tableInfo, GenerationOptions options) {
        GeneratedCode result = new GeneratedCode();
        
        if (options.isGenerateEntity()) {
            result.setEntity(entityGenerator.generate(tableInfo));
        }
        // ... 调用其他生成器
        
        return result;
    }
}
```

### 6. 前端界面设计

**主要页面：**
1. **AI 代码生成页面**
   - 需求输入区（Textarea）
   - 表结构预览区（可编辑的表格）
   - 生成选项配置区（Checkbox）
   - 生成按钮、代码预览、下载按钮

2. **配置管理页面**
   - 配置列表（表格）
   - 搜索和筛选
   - 编辑、删除、导出配置

3. **模板管理页面**
   - 模板列表（分类展示）
   - 在线编辑器（CodeMirror）
   - 预览效果

**交互流程：**
```
用户输入需求 
→ 点击"解析需求" 
→ AI解析（显示加载动画） 
→ 展示表结构预览（可编辑） 
→ 用户确认/调整 
→ 点击"生成代码" 
→ 生成进度（实时更新） 
→ 展示代码预览（语法高亮） 
→ 提供下载按钮
```

## Risks / Trade-offs

### 风险

1. **AI 解析准确性不足**
   - **风险等级**：高
   - **缓解措施**：
     - 提供清晰的需求描述模板和示例
     - 允许用户手动编辑 AI 解析结果
     - 实施反馈机制，持续优化提示词

2. **AI API 调用成本**
   - **风险等级**：中
   - **缓解措施**：
     - 支持本地 LLM（Ollama + Qwen/Llama）
     - 实现请求缓存机制
     - 提供成本估算和限额控制

3. **生成代码质量不稳定**
   - **风险等级**：中
   - **缓解措施**：
     - 复用经过验证的现有生成器
     - 实施代码质量检查（语法、规范）
     - 提供代码审查和手动调整接口

4. **复杂业务场景支持不足**
   - **风险等级**：中
   - **缓解措施**：
     - 明确功能边界，聚焦基础 CRUD
     - 提供代码扩展指导
     - 支持渐进式增强

### 权衡

1. **智能化 vs 稳定性**
   - **选择**：优先稳定性，AI 作为辅助
   - **理由**：生产环境代码质量至关重要

2. **功能完整性 vs 开发周期**
   - **选择**：MVP 优先，迭代增强
   - **理由**：快速验证价值，根据反馈优化

3. **成本 vs 效果**
   - **选择**：支持本地模型，降低使用成本
   - **理由**：让更多用户能够使用

## Implementation Plan

### Phase 1: 基础架构（2周）
- 搭建 myproject-generator-ai 模块结构
- 集成 LangChain4j 框架
- 实现 AI 引擎封装（支持 OpenAI）
- 实现基础的提示词管理

### Phase 2: 核心功能（3周）
- 实现需求解析功能
- 实现表结构推断功能
- 实现代码生成编排（集成现有生成器）
- 数据库设计和表创建

### Phase 3: 管理功能（2周）
- 实现配置管理（CRUD、导入导出）
- 实现模板管理（CRUD、版本控制）
- 实现反馈收集功能

### Phase 4: 前端开发（3周）
- 开发 AI 代码生成页面
- 开发配置管理页面
- 开发模板管理页面
- 前后端联调

### Phase 5: 优化和测试（2周）
- 代码质量优化
- 提示词优化（基于测试反馈）
- 性能优化（缓存、异步处理）
- 全面测试（单元测试、集成测试）
- 文档编写

### 总计：12周

## Success Metrics

1. **功能指标**
   - 需求解析准确率 > 80%
   - 表结构推断合理性 > 85%
   - 生成代码可用性 > 90%

2. **性能指标**
   - 需求解析耗时 < 5秒
   - 代码生成耗时 < 10秒
   - API 响应时间 < 3秒

3. **用户指标**
   - 用户满意度 > 4.0/5.0
   - 配置重用率 > 60%
   - 反馈提交率 > 30%

4. **成本指标**
   - 单次生成成本 < 0.1元（使用云端 AI）
   - 本地模型可用性 > 90%
