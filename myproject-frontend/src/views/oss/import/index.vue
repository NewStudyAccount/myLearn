<template>
  <div class="oss-import-container">
    <el-card class="search-card">
      <el-form :model="queryParams" ref="queryFormRef" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="目标配置" prop="configName">
              <el-select v-model="queryParams.configName" placeholder="请选择OSS配置" clearable>
                <el-option v-for="item in configList" :key="item.configName" :label="item.configName" :value="item.configName" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8" style="margin-top: 4px;">
            <el-button type="primary" @click="handleQuery">
              <el-icon><Search /></el-icon>搜索
            </el-button>
            <el-button @click="resetQuery">
              <el-icon><Refresh /></el-icon>重置
            </el-button>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <div class="toolbar">
        <el-button type="primary" @click="handleSingleImport">
          <el-icon><Upload /></el-icon>单文件导入
        </el-button>
        <el-button type="success" @click="handleBatchImport">
          <el-icon><Upload /></el-icon>批量导入
        </el-button>
      </div>

      <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="文件ID" align="center" prop="ossId" width="100" />
        <el-table-column label="预览" align="center" width="100">
          <template #default="{ row }">
            <el-image
              v-if="isImage(row.fileSuffix)"
              :src="row.fileUrl"
              :preview-src-list="[row.fileUrl]"
              fit="cover"
              style="width: 60px; height: 60px; border-radius: 4px; cursor: pointer;"
              preview-teleported
            />
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="原始文件名" align="center" prop="originalName" />
        <el-table-column label="后缀" align="center" prop="fileSuffix" width="80" />
        <el-table-column label="内容类型" align="center" prop="contentType" />
        <el-table-column label="操作" width="120" align="center">
          <template #default="{ row }">
            <el-button type="text" @click.stop="handleView(row)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>

      <Pagination
        :current-page="queryParams.pageQuery.pageNum"
        :page-size="queryParams.pageQuery.pageSize"
        :total="total"
        @current-change="handlePageChange"
        @size-change="handleSizeChange"
      />
    </el-card>

    <!-- 单文件导入对话框 -->
    <el-dialog v-model="singleImportDialogVisible" title="单文件导入" width="600px" destroy-on-close>
      <el-form :model="importForm" label-width="100px">
        <el-form-item label="OSS配置" required>
          <el-select v-model="importForm.configName" placeholder="请选择OSS配置" style="width: 100%;">
            <el-option v-for="item in configList" :key="item.configName" :label="item.configName" :value="item.configName" />
          </el-select>
        </el-form-item>
        <el-form-item label="选择文件" required>
          <el-upload
            ref="singleUploadRef"
            :auto-upload="false"
            :limit="1"
            :on-change="handleSingleFileChange"
            :on-exceed="handleExceed"
            drag
          >
            <el-icon class="el-icon--upload"><Upload /></el-icon>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <template #tip>
              <div class="el-upload__tip">支持任意格式文件上传</div>
            </template>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="singleImportDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="importLoading" @click="submitSingleImport">确定</el-button>
      </template>
    </el-dialog>

    <!-- 批量导入对话框 -->
    <el-dialog v-model="batchImportDialogVisible" title="批量导入" width="600px" destroy-on-close>
      <el-form :model="importForm" label-width="100px">
        <el-form-item label="OSS配置" required>
          <el-select v-model="importForm.configName" placeholder="请选择OSS配置" style="width: 100%;">
            <el-option v-for="item in configList" :key="item.configName" :label="item.configName" :value="item.configName" />
          </el-select>
        </el-form-item>
        <el-form-item label="选择文件" required>
          <el-upload
            ref="batchUploadRef"
            :auto-upload="false"
            :multiple="true"
            :on-change="handleBatchFileChange"
            :on-exceed="handleExceed"
            drag
          >
            <el-icon class="el-icon--upload"><Upload /></el-icon>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <template #tip>
              <div class="el-upload__tip">支持多个文件同时上传</div>
            </template>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="batchImportDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="importLoading" @click="submitBatchImport">确定</el-button>
      </template>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog v-model="viewDialogVisible" title="详情" width="800px" destroy-on-close>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="文件ID">{{ currentRow?.ossId }}</el-descriptions-item>
        <el-descriptions-item label="文件名">{{ currentRow?.fileName }}</el-descriptions-item>
        <el-descriptions-item label="原始文件名">{{ currentRow?.originalName }}</el-descriptions-item>
        <el-descriptions-item label="后缀">{{ currentRow?.fileSuffix }}</el-descriptions-item>
        <el-descriptions-item label="访问URL">{{ currentRow?.fileUrl }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { Search, Refresh, Upload } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import type { UploadFile, UploadInstance } from 'element-plus'
import { listSysOssFile, importFile, importBatch } from '@/api/oss/sysOssFileApi'
import { listSysOssConfig } from '@/api/oss/sysOssConfigApi'
import type { SysOssFile } from '@/api/oss/sysOssFileApi'
import type { SysOssConfig } from '@/api/oss/sysOssConfigApi'
import Pagination from '@/components/Pagination/index.vue'

const loading = ref(false)
const importLoading = ref(false)
const dataList = ref<SysOssFile[]>([])
const total = ref(0)
const selectedRows = ref<SysOssFile[]>([])
const configList = ref<SysOssConfig[]>([])
const queryFormRef = ref()
const singleUploadRef = ref<UploadInstance>()
const batchUploadRef = ref<UploadInstance>()

const singleImportDialogVisible = ref(false)
const batchImportDialogVisible = ref(false)
const viewDialogVisible = ref(false)
const currentRow = ref<SysOssFile>()

const singleFile = ref<File | null>(null)
const batchFiles = ref<File[]>([])

const queryParams = reactive({
  pageQuery: { pageNum: 1, pageSize: 10 },
  configName: undefined as string | undefined
})

const importForm = reactive({
  configName: ''
})

const getList = async () => {
  loading.value = true
  try {
    const res = await listSysOssFile(queryParams)
    dataList.value = res.data.rows
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const getConfigList = async () => {
  try {
    const res = await listSysOssConfig({ pageQuery: { pageNum: 1, pageSize: 100 } })
    configList.value = res.data.rows || []
  } catch (error) {
    console.error('获取配置列表失败:', error)
  }
}

const handlePageChange = (page: number) => {
  queryParams.pageQuery.pageNum = page
  getList()
}

const handleSizeChange = (size: number) => {
  queryParams.pageQuery.pageSize = size
  queryParams.pageQuery.pageNum = 1
  getList()
}

const handleQuery = () => {
  queryParams.pageQuery.pageNum = 1
  getList()
}

const resetQuery = () => {
  queryFormRef.value?.resetFields()
  handleQuery()
}

const handleSelectionChange = (selection: SysOssFile[]) => {
  selectedRows.value = selection
}

const isImage = (suffix?: string) => {
  if (!suffix) return false
  return ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp', 'svg'].includes(suffix.toLowerCase())
}

const handleView = (row: SysOssFile) => {
  currentRow.value = row
  viewDialogVisible.value = true
}

const handleSingleImport = () => {
  singleFile.value = null
  importForm.configName = ''
  singleImportDialogVisible.value = true
}

const handleBatchImport = () => {
  batchFiles.value = []
  importForm.configName = ''
  batchImportDialogVisible.value = true
}

const handleSingleFileChange = (file: UploadFile) => {
  singleFile.value = file.raw || null
}

const handleBatchFileChange = (file: UploadFile) => {
  if (file.raw) {
    batchFiles.value.push(file.raw)
  }
}

const handleExceed = () => {
  ElMessage.warning('文件数量超出限制')
}

const submitSingleImport = async () => {
  if (!singleFile.value) {
    ElMessage.warning('请选择要导入的文件')
    return
  }
  if (!importForm.configName) {
    ElMessage.warning('请选择OSS配置')
    return
  }

  importLoading.value = true
  try {
    await importFile(singleFile.value, importForm.configName)
    ElMessage.success('导入成功')
    singleImportDialogVisible.value = false
    await getList()
  } catch (error) {
    ElMessage.error('导入失败')
  } finally {
    importLoading.value = false
  }
}

const submitBatchImport = async () => {
  if (batchFiles.value.length === 0) {
    ElMessage.warning('请选择要导入的文件')
    return
  }
  if (!importForm.configName) {
    ElMessage.warning('请选择OSS配置')
    return
  }

  importLoading.value = true
  try {
    const results = await importBatch(batchFiles.value, importForm.configName)
    const successCount = results.filter((r: any) => r.success).length
    const failCount = results.filter((r: any) => !r.success).length
    ElMessage.success(`导入完成: 成功${successCount}个, 失败${failCount}个`)
    batchImportDialogVisible.value = false
    await getList()
  } catch (error) {
    ElMessage.error('批量导入失败')
  } finally {
    importLoading.value = false
  }
}

onMounted(() => {
  getList()
  getConfigList()
})
</script>

<style scoped lang="scss">
.oss-import-container {
  padding: 20px;
}
.search-card {
  margin-bottom: 20px;
}
.table-card {
  .toolbar {
    padding: 10px 0;
  }
}
</style>
