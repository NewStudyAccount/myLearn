<template>
  <div class="oss-migrate-container">
    <el-card class="search-card">
      <el-form :model="queryParams" ref="queryFormRef" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="源配置" prop="sourceConfig">
              <el-select v-model="queryParams.sourceConfig" placeholder="请选择源OSS配置" clearable>
                <el-option v-for="item in configList" :key="item.configName" :label="item.configName" :value="item.configName" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="目标配置" prop="targetConfig">
              <el-select v-model="queryParams.targetConfig" placeholder="请选择目标OSS配置" clearable>
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
        <el-button type="primary" :disabled="selectedRows.length === 0" @click="handleBatchMigrate">
          <el-icon><Switch /></el-icon>批量迁移
        </el-button>
        <el-button type="warning" @click="handleMigrateAll">
          <el-icon><Switch /></el-icon>全量迁移
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
        <el-table-column label="访问URL" align="center" prop="fileUrl" show-overflow-tooltip />
        <el-table-column label="操作" width="120" align="center">
          <template #default="{ row }">
            <el-button type="text" @click.stop="handleSingleMigrate(row)">迁移</el-button>
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

    <el-card class="task-card" style="margin-top: 20px;">
      <template #header>
        <div class="card-header">
          <span>迁移任务列表</span>
          <el-button type="primary" size="small" @click="showCreateDialog">
            <el-icon><Plus /></el-icon>新建迁移任务
          </el-button>
        </div>
      </template>

      <el-table :data="taskList" v-loading="taskLoading">
        <el-table-column label="任务ID" align="center" prop="taskId" width="180" />
        <el-table-column label="源配置" align="center" prop="sourceConfig" width="140" />
        <el-table-column label="目标配置" align="center" prop="targetConfig" width="140" />
        <el-table-column label="状态" align="center" width="120">
          <template #default="{ row }">
            <el-tag :type="statusTagType(row.status)">{{ statusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="进度" align="center" width="200">
          <template #default="{ row }">
            <el-progress
              :percentage="calcPercent(row)"
              :status="row.status === 'COMPLETED' ? 'success' : row.status === 'FAILED' ? 'exception' : undefined"
              :stroke-width="16"
              :text-inside="true"
            />
          </template>
        </el-table-column>
        <el-table-column label="成功/失败/总数" align="center" width="140">
          <template #default="{ row }">
            {{ row.successCount }}/{{ row.failCount }}/{{ row.totalCount }}
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="createdAt" width="180" />
        <el-table-column label="操作" align="center" width="200">
          <template #default="{ row }">
            <el-button type="text" @click="handleViewProgress(row)">进度</el-button>
            <el-button type="text" v-if="row.status === 'RUNNING' || row.status === 'PENDING'" @click="handleCancelTask(row)" style="color: #F56C6C;">取消</el-button>
            <el-button type="text" v-if="row.status === 'FAILED' || row.status === 'CANCELLED'" @click="handleResumeTask(row)" style="color: #E6A23C;">续传</el-button>
          </template>
        </el-table-column>
      </el-table>

      <Pagination
        :current-page="taskPageNum"
        :page-size="taskPageSize"
        :total="taskTotal"
        @current-change="handleTaskPageChange"
        @size-change="handleTaskSizeChange"
      />
    </el-card>

    <el-dialog v-model="progressDialogVisible" title="迁移进度" width="600px" destroy-on-close>
      <div v-if="currentProgress">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="任务ID">{{ currentProgress.taskId }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="statusTagType(currentProgress.status)">{{ statusLabel(currentProgress.status) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="源配置">{{ currentProgress.sourceConfig }}</el-descriptions-item>
          <el-descriptions-item label="目标配置">{{ currentProgress.targetConfig }}</el-descriptions-item>
          <el-descriptions-item label="总数">{{ currentProgress.totalCount }}</el-descriptions-item>
          <el-descriptions-item label="成功">{{ currentProgress.successCount }}</el-descriptions-item>
          <el-descriptions-item label="失败">{{ currentProgress.failCount }}</el-descriptions-item>
          <el-descriptions-item label="剩余">{{ currentProgress.remaining }}</el-descriptions-item>
        </el-descriptions>
        <el-progress
          :percentage="currentProgress.percent"
          :status="currentProgress.status === 'COMPLETED' ? 'success' : currentProgress.status === 'FAILED' ? 'exception' : undefined"
          :stroke-width="20"
          :text-inside="true"
          style="margin-top: 20px;"
        />
      </div>
      <template #footer>
        <el-button @click="stopPolling(); progressDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="createDialogVisible" title="新建迁移任务" width="500px" destroy-on-close>
      <el-form :model="createForm" label-width="120px">
        <el-form-item label="源配置" required>
          <el-select v-model="createForm.sourceConfig" placeholder="请选择源OSS配置">
            <el-option v-for="item in configList" :key="item.configName" :label="item.configName" :value="item.configName" />
          </el-select>
        </el-form-item>
        <el-form-item label="目标配置" required>
          <el-select v-model="createForm.targetConfig" placeholder="请选择目标OSS配置">
            <el-option v-for="item in configList" :key="item.configName" :label="item.configName" :value="item.configName" />
          </el-select>
        </el-form-item>
        <el-form-item label="迁移范围">
          <el-radio-group v-model="createForm.migrateScope">
            <el-radio value="all">全量迁移</el-radio>
            <el-radio value="selected">指定文件</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="createForm.migrateScope === 'selected'" label="文件ID列表">
          <el-input v-model="createForm.ossIdsText" placeholder="多个文件ID用逗号分隔" />
        </el-form-item>
        <el-form-item label="删除源端文件">
          <el-switch v-model="createForm.deleteSource" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreateTask" :loading="createLoading">创建并执行</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { Search, Refresh, Switch, Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  listSysOssFile,
  createMigrateTask,
  getMigrateTaskProgress,
  cancelMigrateTask,
  resumeMigrateTask,
  listMigrateTasks
} from '@/api/oss/sysOssFileApi'
import type { SysOssFile, MigrateTaskProgress } from '@/api/oss/sysOssFileApi'
import { listSysOssConfig } from '@/api/oss/sysOssConfigApi'
import type { SysOssConfig } from '@/api/oss/sysOssConfigApi'
import Pagination from '@/components/Pagination/index.vue'

const loading = ref(false)
const dataList = ref<SysOssFile[]>([])
const total = ref(0)
const selectedRows = ref<SysOssFile[]>([])
const configList = ref<SysOssConfig[]>([])
const queryFormRef = ref()

const progressDialogVisible = ref(false)
const currentProgress = ref<MigrateTaskProgress | null>(null)
let pollingTimer: ReturnType<typeof setInterval> | null = null

const taskLoading = ref(false)
const taskList = ref<any[]>([])
const taskTotal = ref(0)
const taskPageNum = ref(1)
const taskPageSize = ref(10)

const createDialogVisible = ref(false)
const createLoading = ref(false)
const createForm = reactive({
  sourceConfig: '',
  targetConfig: '',
  migrateScope: 'all' as 'all' | 'selected',
  ossIdsText: '',
  deleteSource: false
})

const queryParams = reactive({
  pageQuery: { pageNum: 1, pageSize: 10 },
  sourceConfig: undefined as string | undefined,
  targetConfig: undefined as string | undefined
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

const getTaskList = async () => {
  taskLoading.value = true
  try {
    const res = await listMigrateTasks(undefined, taskPageNum.value, taskPageSize.value)
    taskList.value = res.data.rows || []
    taskTotal.value = res.data.total || 0
  } finally {
    taskLoading.value = false
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

const handleTaskPageChange = (page: number) => {
  taskPageNum.value = page
  getTaskList()
}

const handleTaskSizeChange = (size: number) => {
  taskPageSize.value = size
  taskPageNum.value = 1
  getTaskList()
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

const statusTagType = (status: string) => {
  const map: Record<string, string> = {
    PENDING: 'info',
    RUNNING: 'warning',
    COMPLETED: 'success',
    FAILED: 'danger',
    CANCELLED: 'info'
  }
  return map[status] || 'info'
}

const statusLabel = (status: string) => {
  const map: Record<string, string> = {
    PENDING: '等待中',
    RUNNING: '执行中',
    COMPLETED: '已完成',
    FAILED: '失败',
    CANCELLED: '已取消'
  }
  return map[status] || status
}

const calcPercent = (row: any) => {
  if (row.totalCount === 0) return 0
  return Math.round(((row.successCount + row.failCount) / row.totalCount) * 100)
}

const startPolling = (taskId: number) => {
  stopPolling()
  const poll = async () => {
    try {
      const res = await getMigrateTaskProgress(taskId)
      currentProgress.value = res.data
      if (res.data.status === 'COMPLETED' || res.data.status === 'FAILED' || res.data.status === 'CANCELLED') {
        stopPolling()
        getTaskList()
      }
    } catch (error) {
      console.error('轮询进度失败:', error)
    }
  }
  poll()
  pollingTimer = setInterval(poll, 2000)
}

const stopPolling = () => {
  if (pollingTimer) {
    clearInterval(pollingTimer)
    pollingTimer = null
  }
}

const handleSingleMigrate = async (row: SysOssFile) => {
  if (!queryParams.sourceConfig || !queryParams.targetConfig) {
    ElMessage.warning('请先选择源配置和目标配置')
    return
  }
  if (queryParams.sourceConfig === queryParams.targetConfig) {
    ElMessage.warning('源配置和目标配置不能相同')
    return
  }

  try {
    await ElMessageBox.confirm(`确认将文件 ${row.originalName} 从 ${queryParams.sourceConfig} 迁移到 ${queryParams.targetConfig}?`, '确认迁移', { type: 'warning' })
    const res = await createMigrateTask(queryParams.sourceConfig, queryParams.targetConfig, [row.ossId], false)
    const taskId = res.data
    ElMessage.success(`迁移任务已创建，任务ID: ${taskId}`)
    progressDialogVisible.value = true
    startPolling(taskId)
    getTaskList()
    await getList()
  } catch (error) {
    ElMessage.error('创建迁移任务失败')
  }
}

const handleBatchMigrate = async () => {
  if (!queryParams.sourceConfig || !queryParams.targetConfig) {
    ElMessage.warning('请先选择源配置和目标配置')
    return
  }
  if (queryParams.sourceConfig === queryParams.targetConfig) {
    ElMessage.warning('源配置和目标配置不能相同')
    return
  }
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请选择要迁移的文件')
    return
  }

  try {
    await ElMessageBox.confirm(`确认将 ${selectedRows.value.length} 个文件从 ${queryParams.sourceConfig} 迁移到 ${queryParams.targetConfig}?`, '确认批量迁移', { type: 'warning' })
    const ossIds = selectedRows.value.map(row => row.ossId)
    const res = await createMigrateTask(queryParams.sourceConfig, queryParams.targetConfig, ossIds, false)
    const taskId = res.data
    ElMessage.success(`批量迁移任务已创建，任务ID: ${taskId}`)
    progressDialogVisible.value = true
    startPolling(taskId)
    getTaskList()
    await getList()
  } catch (error) {
    ElMessage.error('创建批量迁移任务失败')
  }
}

const handleMigrateAll = async () => {
  if (!queryParams.sourceConfig || !queryParams.targetConfig) {
    ElMessage.warning('请先选择源配置和目标配置')
    return
  }
  if (queryParams.sourceConfig === queryParams.targetConfig) {
    ElMessage.warning('源配置和目标配置不能相同')
    return
  }

  try {
    await ElMessageBox.confirm(`确认将 ${queryParams.sourceConfig} 中的所有文件迁移到 ${queryParams.targetConfig}?`, '确认全量迁移', { type: 'warning' })
    const res = await createMigrateTask(queryParams.sourceConfig, queryParams.targetConfig, undefined, false)
    const taskId = res.data
    ElMessage.success(`全量迁移任务已创建，任务ID: ${taskId}`)
    progressDialogVisible.value = true
    startPolling(taskId)
    getTaskList()
    await getList()
  } catch (error) {
    ElMessage.error('创建全量迁移任务失败')
  }
}

const handleViewProgress = async (row: any) => {
  try {
    const res = await getMigrateTaskProgress(row.taskId)
    currentProgress.value = res.data
    progressDialogVisible.value = true
    if (res.data.status === 'RUNNING' || res.data.status === 'PENDING') {
      startPolling(row.taskId)
    }
  } catch (error) {
    ElMessage.error('获取进度失败')
  }
}

const handleCancelTask = async (row: any) => {
  try {
    await ElMessageBox.confirm('确认取消此迁移任务?', '确认取消', { type: 'warning' })
    await cancelMigrateTask(row.taskId)
    ElMessage.success('任务已取消')
    getTaskList()
  } catch (error) {
    ElMessage.error('取消任务失败')
  }
}

const handleResumeTask = async (row: any) => {
  try {
    await ElMessageBox.confirm('确认续传此迁移任务?', '确认续传', { type: 'warning' })
    await resumeMigrateTask(row.taskId)
    ElMessage.success('任务已续传')
    getTaskList()
  } catch (error) {
    ElMessage.error('续传任务失败')
  }
}

const showCreateDialog = () => {
  createForm.sourceConfig = queryParams.sourceConfig || ''
  createForm.targetConfig = queryParams.targetConfig || ''
  createForm.migrateScope = 'all'
  createForm.ossIdsText = ''
  createForm.deleteSource = false
  createDialogVisible.value = true
}

const handleCreateTask = async () => {
  if (!createForm.sourceConfig || !createForm.targetConfig) {
    ElMessage.warning('请选择源配置和目标配置')
    return
  }
  if (createForm.sourceConfig === createForm.targetConfig) {
    ElMessage.warning('源配置和目标配置不能相同')
    return
  }

  createLoading.value = true
  try {
    let ossIds: number[] | undefined
    if (createForm.migrateScope === 'selected' && createForm.ossIdsText) {
      ossIds = createForm.ossIdsText.split(',').map(id => Number(id.trim())).filter(id => !isNaN(id))
      if (ossIds.length === 0) {
        ElMessage.warning('请输入有效的文件ID')
        return
      }
    }
    const res = await createMigrateTask(createForm.sourceConfig, createForm.targetConfig, ossIds, createForm.deleteSource)
    const taskId = res.data
    ElMessage.success(`迁移任务已创建，任务ID: ${taskId}`)
    createDialogVisible.value = false
    progressDialogVisible.value = true
    startPolling(taskId)
    getTaskList()
  } catch (error) {
    ElMessage.error('创建迁移任务失败')
  } finally {
    createLoading.value = false
  }
}

onMounted(() => {
  getList()
  getConfigList()
  getTaskList()
})

onUnmounted(() => {
  stopPolling()
})
</script>

<style scoped lang="scss">
.oss-migrate-container {
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
.task-card {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}
</style>