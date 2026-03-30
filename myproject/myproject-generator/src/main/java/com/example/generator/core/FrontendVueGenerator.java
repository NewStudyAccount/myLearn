package com.example.generator.core;

import com.example.generator.config.GeneratorProperties;
import com.example.generator.domain.ColumnInfo;
import com.example.generator.domain.TableInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class FrontendVueGenerator {

    private static final Logger log = LoggerFactory.getLogger(FrontendVueGenerator.class);

    private final TemplateEngine templateEngine;
    private final GeneratorProperties properties;

    public FrontendVueGenerator(TemplateEngine templateEngine, GeneratorProperties properties) {
        this.templateEngine = templateEngine;
        this.properties = properties;
    }

    public String generate(TableInfo tableInfo) {
        Map<String, Object> context = new HashMap<>();
        
        tableInfo.setLowerEntityName(tableInfo.getEntityName().substring(0, 1).toLowerCase() + tableInfo.getEntityName().substring(1));
        
        String pkName = tableInfo.getPrimaryKey() != null ? tableInfo.getPrimaryKey().getFieldName() : "id";
        String pkCapitalName = pkName.substring(0, 1).toUpperCase() + pkName.substring(1);
        
        context.put("table", tableInfo);
        context.put("entityName", tableInfo.getEntityName());
        context.put("entityLowerName", tableInfo.getLowerEntityName());
        context.put("pkName", pkName);
        context.put("pkCapitalName", pkCapitalName);
        context.put("description", tableInfo.getTableComment());

        List<ColumnInfo> formColumns = tableInfo.getColumns().stream()
            .filter(c -> !c.isPrimaryKey())
            .collect(Collectors.toList());
        context.put("formColumns", formColumns);

        List<ColumnInfo> searchColumns = tableInfo.getColumns().stream()
            .filter(c -> !c.isPrimaryKey() && ("String".equals(c.getJavaType()) || "Integer".equals(c.getJavaType())))
            .collect(Collectors.toList());
        context.put("searchColumns", searchColumns);

        context.put("apiPath", properties.getApiPath().replace("\\", "/"));
        
        String entityName = tableInfo.getEntityName();
        if (entityName.endsWith("Entity")) {
            entityName = entityName.substring(0, entityName.length() - 6);
        }
        context.put("baseName", entityName);
        
        // API function names (matching FrontendApiGenerator naming convention)
        context.put("apiGetList", "getList" + entityName);
        context.put("apiGetById", "getById" + entityName);
        context.put("apiCreate", "create" + entityName);
        context.put("apiUpdate", "update" + entityName);
        context.put("apiDelete", "delete" + entityName);
        
        // API file path (matching FrontendApiGenerator fileName)
        String apiFilePath = "@/api" + properties.getApiPath().replace("\\", "/") + "/" + tableInfo.getLowerEntityName() + "Api";
        context.put("apiFilePath", apiFilePath);

        String template = getVueTemplate();
        return templateEngine.renderString(template, context);
    }

    private String getVueTemplate() {
        return "<template>\n" +
            "  <div class=\"${entityLowerName}-container\">\n" +
            "    <el-card class=\"search-card\">\n" +
            "      <el-form :model=\"queryParams\" ref=\"queryForm\" label-width=\"120px\">\n" +
            "        <el-row :gutter=\"20\">\n" +
            "#foreach($column in $searchColumns)\n" +
            "          <el-col :span=\"8\">\n" +
            "            <el-form-item label=\"${column.columnComment}\" prop=\"${column.fieldName}\">\n" +
            "              <el-input v-model=\"queryParams.${column.fieldName}\" placeholder=\"请输入${column.columnComment}\" clearable @keyup.enter=\"handleQuery\" />\n" +
            "            </el-form-item>\n" +
            "          </el-col>\n" +
            "#end\n" +
            "          <el-col :span=\"8\" style=\"margin-top: 4px;\">\n" +
            "            <el-button type=\"primary\" @click=\"handleQuery\">\n" +
            "              <Icon icon=\"ep:search\" />搜索\n" +
            "            </el-button>\n" +
            "            <el-button @click=\"resetQuery\">\n" +
            "              <Icon icon=\"ep:refresh\" />重置\n" +
            "            </el-button>\n" +
            "          </el-col>\n" +
            "        </el-row>\n" +
            "      </el-form>\n" +
            "    </el-card>\n" +
            "\n" +
            "    <el-card class=\"table-card\">\n" +
            "      <div class=\"toolbar\">\n" +
            "        <el-button type=\"primary\" @click=\"handleAdd\">\n" +
            "          <Icon icon=\"ep:plus\" />新增\n" +
            "        </el-button>\n" +
            "        <el-button :disabled=\"single\" type=\"danger\" @click=\"handleDelete(rowData)\">\n" +
            "          <Icon icon=\"ep:delete\" />删除\n" +
            "        </el-button>\n" +
            "      </div>\n" +
            "\n" +
            "      <el-table v-loading=\"loading\" :data=\"dataList\" @row-click=\"rowClick\" @selection-change=\"handleSelectionChange\">\n" +
            "        <el-table-column type=\"selection\" width=\"55\" align=\"center\" />\n" +
            "#foreach($column in $table.columns)\n" +
            "        <el-table-column label=\"${column.columnComment}\" align=\"center\" prop=\"${column.fieldName}\" />\n" +
            "#end\n" +
            "        <el-table-column label=\"操作\" width=\"180\" align=\"center\">\n" +
            "          <template #default=\"{row}\">\n" +
            "            <el-button type=\"text\" @click.stop=\"handleView(row)\">查看</el-button>\n" +
            "            <el-button type=\"text\" @click.stop=\"handleEdit(row)\">编辑</el-button>\n" +
            "            <el-button type=\"text\" @click.stop=\"handleDelete(row)\">删除</el-button>\n" +
            "          </template>\n" +
            "        </el-table-column>\n" +
            "      </el-table>\n" +
            "\n" +
            "      <pagination v-model:page=\"queryParams.pageNo\" v-model:limit=\"queryParams.pageSize\" :total=\"total\" @pagination=\"getList\" />\n" +
            "    </el-card>\n" +
            "\n" +
            "    <el-dialog v-model=\"dialogVisible\" :title=\"dialogTitle\" width=\"800px\" destroy-on-close>\n" +
            "      <el-form ref=\"formRef\" :model=\"formParams\" :rules=\"rules\" label-width=\"120px\">\n" +
            "#foreach($column in $formColumns)\n" +
            "        <el-form-item label=\"${column.columnComment}\" prop=\"${column.fieldName}\">\n" +
            "          <el-input v-model=\"formParams.${column.fieldName}\" placeholder=\"请输入${column.columnComment}\" />\n" +
            "        </el-form-item>\n" +
            "#end\n" +
            "      </el-form>\n" +
            "      <template #footer>\n" +
            "        <el-button @click=\"dialogVisible = false\">取消</el-button>\n" +
            "        <el-button type=\"primary\" @click=\"handleSubmit\">确定</el-button>\n" +
            "      </template>\n" +
            "    </el-dialog>\n" +
            "\n" +
            "    <el-dialog v-model=\"viewDialogVisible\" title=\"${description}详情\" width=\"800px\" destroy-on-close>\n" +
            "      <el-descriptions :column=\"2\" border>\n" +
            "#foreach($column in $table.columns)\n" +
            "        <el-descriptions-item label=\"${column.columnComment}\">{{ rowData.${column.fieldName} }}</el-descriptions-item>\n" +
            "#end\n" +
            "      </el-descriptions>\n" +
            "    </el-dialog>\n" +
            "  </div>\n" +
            "</template>\n" +
            "\n" +
            "<script setup lang=\"ts\">\n" +
            "import { ref, reactive, onMounted } from 'vue'\n" +
            "import { ${apiGetList}, ${apiGetById}, ${apiCreate}, ${apiUpdate}, ${apiDelete} } from '${apiFilePath}'\n" +
            "import type { ${baseName} } from '${apiFilePath}'\n" +
            "import { ElMessage, ElMessageBox } from 'element-plus'\n" +
            "\n" +
            "const loading = ref(false)\n" +
            "const dataList = ref<${baseName}[]>([])\n" +
            "const total = ref(0)\n" +
            "const queryParams = reactive({\n" +
            "  pageNo: 1,\n" +
            "  pageSize: 10,\n" +
            "#foreach($column in $searchColumns)\n" +
            "  ${column.fieldName}: undefined,\n" +
            "#end\n" +
            "})\n" +
            "const queryFormRef = ref()\n" +
            "const dialogVisible = ref(false)\n" +
            "const viewDialogVisible = ref(false)\n" +
            "const dialogTitle = ref('')\n" +
            "const rowData = ref<${baseName}>()\n" +
            "const ids = ref<number[]>([])\n" +
            "const single = ref(true)\n" +
            "\n" +
            "const formParams = reactive({\n" +
            "#foreach($column in $formColumns)\n" +
            "  ${column.fieldName}: undefined,\n" +
            "#end\n" +
            "})\n" +
            "const formRef = ref()\n" +
            "const rules = reactive({\n" +
            "})\n" +
            "\n" +
            "const getList = async () => {\n" +
            "  loading.value = true\n" +
            "  try {\n" +
            "    const res = await ${apiGetList}(queryParams)\n" +
            "    dataList.value = res.list\n" +
            "    total.value = res.total\n" +
            "  } finally {\n" +
            "    loading.value = false\n" +
            "  }\n" +
            "}\n" +
            "\n" +
            "const handleQuery = () => {\n" +
            "  queryParams.pageNo = 1\n" +
            "  getList()\n" +
            "}\n" +
            "\n" +
            "const resetQuery = () => {\n" +
            "  queryFormRef.value?.resetFields()\n" +
            "  handleQuery()\n" +
            "}\n" +
            "\n" +
            "const handleAdd = () => {\n" +
            "  dialogVisible.value = true\n" +
            "  dialogTitle.value = '新增${description}'\n" +
            "  Object.assign(formParams, {\n" +
            "#foreach($column in $formColumns)\n" +
            "    ${column.fieldName}: undefined,\n" +
            "#end\n" +
            "  })\n" +
            "}\n" +
            "\n" +
            "const handleEdit = (row: ${baseName}) => {\n" +
            "  dialogVisible.value = true\n" +
            "  dialogTitle.value = '编辑${description}'\n" +
            "  Object.assign(formParams, row)\n" +
            "}\n" +
            "\n" +
            "const handleView = (row: ${baseName}) => {\n" +
            "  viewDialogVisible.value = true\n" +
            "  rowData.value = row\n" +
            "}\n" +
            "\n" +
            "const handleDelete = async (row: ${baseName}) => {\n" +
            "  try {\n" +
            "    await ElMessageBox.confirm('是否确认删除选中的数据?', '警告', { type: 'warning' })\n" +
            "    await ${apiDelete}(row.${pkName})\n" +
            "    ElMessage.success('删除成功')\n" +
            "    await getList()\n" +
            "  } catch {}\n" +
            "}\n" +
            "\n" +
            "const handleSubmit = async () => {\n" +
            "  const valid = await formRef.value?.validate()\n" +
            "  if (!valid) return\n" +
            "\n" +
            "  try {\n" +
            "    const data = { ...formParams }\n" +
            "    if (data.${pkName}) {\n" +
            "      await ${apiUpdate}(data)\n" +
            "      ElMessage.success('修改成功')\n" +
            "    } else {\n" +
            "      await ${apiCreate}(data)\n" +
            "      ElMessage.success('新增成功')\n" +
            "    }\n" +
            "    dialogVisible.value = false\n" +
            "    await getList()\n" +
            "  } catch {}\n" +
            "}\n" +
            "\n" +
            "const handleSelectionChange = (selection: ${baseName}[]) => {\n" +
            "  ids.value = selection.map((item) => item.${pkName})\n" +
            "  single.value = selection.length !== 1\n" +
            "}\n" +
            "\n" +
            "const rowClick = (row: ${baseName}) => {\n" +
            "  rowData.value = row\n" +
            "}\n" +
            "\n" +
            "onMounted(() => {\n" +
            "  getList()\n" +
            "})\n" +
            "</script>\n" +
            "\n" +
            "<style scoped lang=\"scss\">\n" +
            ".${entityLowerName}-container {\n" +
            "  padding: 20px;\n" +
            "}\n" +
            ".search-card {\n" +
            "  margin-bottom: 20px;\n" +
            "}\n" +
            ".table-card {\n" +
            "  .toolbar {\n" +
            "    padding: 10px 0;\n" +
            "  }\n" +
            "}\n" +
            "</style>\n";
    }
}