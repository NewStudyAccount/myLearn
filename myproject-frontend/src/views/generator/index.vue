<template>
  <div class="generator-container">
    <el-card class="table-card">
      <template #header>
        <span>数据库表列表</span>
      </template>

      <el-table v-loading="loading" :data="tableList" @selection-change="handleSelectionChange" border>
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="表名" prop="tableName" />
        <el-table-column label="表注释" prop="tableComment" />
        <el-table-column label="操作" width="180" align="center">
          <template #default="{ row }">
            <el-button type="primary" link @click="handlePreview(row)">预览</el-button>
            <el-button type="success" link @click="handleGenerate(row)">生成</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="batch-actions" v-if="selectedTables.length > 0">
        <el-button type="primary" @click="handleBatchGenerate">
          <el-icon><Download /></el-icon>
          批量生成 ({{ selectedTables.length }}个表)
        </el-button>
      </div>
    </el-card>

    <el-drawer v-model="configVisible" title="生成配置" size="500px" destroy-on-close>
      <GeneratorConfig
        :table-name="currentTable"
        @generate="handleConfigGenerate"
        @download="handleConfigDownload"
      />
    </el-drawer>

    <el-drawer v-model="previewVisible" title="代码预览" size="80%" destroy-on-close>
      <CodePreview :code-map="previewCode" />
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getTables, generateCode, downloadCode } from '@/api/generator'
import { ElMessage } from 'element-plus'
import { Download } from '@element-plus/icons-vue'
import GeneratorConfig from './components/GeneratorConfig.vue'
import CodePreview from './components/CodePreview.vue'

interface TableInfo {
  tableName: string
  tableComment: string
}

const loading = ref(false)
const tableList = ref<TableInfo[]>([])
const selectedTables = ref<TableInfo[]>([])
const configVisible = ref(false)
const previewVisible = ref(false)
const currentTable = ref('')
const previewCode = ref<Record<string, string>>({})

const loadTables = async () => {
  loading.value = true
  try {
    const res = await getTables()
    tableList.value = res
  } finally {
    loading.value = false
  }
}

const handleSelectionChange = (selection: TableInfo[]) => {
  selectedTables.value = selection
}

const handlePreview = (row: TableInfo) => {
  currentTable.value = row.tableName
  configVisible.value = true
}

const handleGenerate = (row: TableInfo) => {
  currentTable.value = row.tableName
  configVisible.value = true
}

const handleConfigGenerate = async (params: any) => {
  try {
    const res = await generateCode(params)
    previewCode.value = res
    configVisible.value = false
    previewVisible.value = true
  } catch {
    ElMessage.error('生成失败')
  }
}

const handleConfigDownload = async (params: any) => {
  try {
    await downloadCode(params)
    ElMessage.success('下载成功')
    configVisible.value = false
  } catch {
    ElMessage.error('下载失败')
  }
}

const handleBatchGenerate = async () => {
  ElMessage.info('批量生成功能开发中...')
}

onMounted(() => {
  loadTables()
})
</script>

<style scoped lang="scss">
.generator-container {
  padding: 20px;
}
.table-card {
  margin-bottom: 20px;
}
.batch-actions {
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid #ebeef5;
}
</style>
