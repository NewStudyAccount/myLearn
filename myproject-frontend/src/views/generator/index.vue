<template>
  <div class="generator-container">
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="表名">
          <el-input
              v-model="searchForm.tableName"
              placeholder="请输入表名"
              clearable
              @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item label="表注释">
          <el-input
              v-model="searchForm.tableComment"
              placeholder="请输入表注释"
              clearable
              @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span>数据库表列表</span>
          <el-tag type="info">共 {{ filteredTableList.length }} 个表</el-tag>
        </div>
      </template>

      <el-table
          v-loading="loading"
          :data="filteredTableList"
          @selection-change="handleSelectionChange"
          border
          stripe
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="序号" type="index" width="60" align="center" />
        <el-table-column label="表名" prop="tableName" min-width="200" show-overflow-tooltip />
        <el-table-column label="表注释" prop="tableComment" min-width="200" show-overflow-tooltip />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handlePreview(row)">
              <el-icon><View /></el-icon>
              预览
            </el-button>
            <el-button type="success" link size="small" @click="handleGenerate(row)">
              <el-icon><Download /></el-icon>
              生成
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="batch-actions" v-if="selectedTables.length > 0">
        <el-checkbox
            v-model="selectAll"
            @change="handleSelectAll"
        >
          全选
        </el-checkbox>
        <el-text type="info" class="ml-2">已选择 {{ selectedTables.length }} 个表</el-text>
        <el-button type="primary" @click="handleBatchGenerate">
          <el-icon><Download /></el-icon>
          批量下载
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

    <el-dialog
        v-model="previewVisible"
        title="代码预览"
        width="80%"
        top="5vh"
        destroy-on-close
        fullscreen
    >
      <CodePreview :code-map="previewCode" />
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { getTables, generateCode, downloadCode } from '@/api/generator'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, View, Download } from '@element-plus/icons-vue'
import GeneratorConfig from './components/GeneratorConfig.vue'
import CodePreview from './components/CodePreview.vue'

interface TableInfo {
  tableName: string
  tableComment: string
}

const loading = ref(false)
const tableList = ref<TableInfo[]>([])
const selectedTables = ref<TableInfo[]>([])
const selectAll = ref(false)
const configVisible = ref(false)
const previewVisible = ref(false)
const currentTable = ref('')
const previewCode = ref<Record<string, string>>({})

const searchForm = ref({
  tableName: '',
  tableComment: '',
})

const filteredTableList = computed(() => {
  return tableList.value.filter((table) => {
    const matchName = !searchForm.value.tableName ||
        table.tableName.toLowerCase().includes(searchForm.value.tableName.toLowerCase())
    const matchComment = !searchForm.value.tableComment ||
        table.tableComment?.toLowerCase().includes(searchForm.value.tableComment.toLowerCase())
    return matchName && matchComment
  })
})

const loadTables = async () => {
  loading.value = true
  try {
    const res = await getTables()
    tableList.value = res.data || []
  } catch (error) {
    ElMessage.error('加载表列表失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
}

const handleReset = () => {
  searchForm.value = {
    tableName: '',
    tableComment: '',
  }
}

const handleSelectionChange = (selection: TableInfo[]) => {
  selectedTables.value = selection
  selectAll.value = selection.length === filteredTableList.value.length && filteredTableList.value.length > 0
}

const handleSelectAll = (val: boolean) => {
  if (val) {
    selectedTables.value = [...filteredTableList.value]
  } else {
    selectedTables.value = []
  }
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
  if (selectedTables.value.length === 0) {
    ElMessage.warning('请先选择要生成的表')
    return
  }

  try {
    await ElMessageBox.confirm(
        `确定要批量生成 ${selectedTables.value.length} 个表的代码吗？`,
        '批量生成',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }
    )

    const params = {
      packageName: 'com.example',
      tablePrefix: '',
    }

    for (const table of selectedTables.value) {
      await downloadCode({
        tableName: table.tableName,
        ...params,
      })
    }

    ElMessage.success('批量生成成功')
    selectedTables.value = []
    selectAll.value = false
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('批量生成失败')
    }
  }
}

onMounted(() => {
  loadTables()
})
</script>

<style scoped lang="scss">
.generator-container {
  padding: 20px;
}

.search-card {
  margin-bottom: 20px;
}

.table-card {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}

.batch-actions {
  margin-top: 16px;
  padding: 16px;
  border-top: 1px solid #ebeef5;
  display: flex;
  align-items: center;
  gap: 12px;
}

.ml-2 {
  margin-left: 8px;
}
</style>
