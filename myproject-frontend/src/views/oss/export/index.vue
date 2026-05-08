<template>
  <div class="oss-export-container">
    <el-card class="search-card">
      <el-form :model="queryParams" ref="queryFormRef" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="文件后缀" prop="suffix">
              <el-input v-model="queryParams.suffix" placeholder="如: png, jpg" clearable />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="开始时间" prop="startTime">
              <el-date-picker v-model="queryParams.startTime" type="datetime" placeholder="选择开始时间" value-format="YYYY-MM-DD HH:mm:ss" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="结束时间" prop="endTime">
              <el-date-picker v-model="queryParams.endTime" type="datetime" placeholder="选择结束时间" value-format="YYYY-MM-DD HH:mm:ss" />
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
        <el-button type="primary" :disabled="selectedRows.length === 0" @click="handleBatchExport">
          <el-icon><Download /></el-icon>批量导出ZIP
        </el-button>
        <el-button type="success" @click="handleExportCondition">
          <el-icon><Download /></el-icon>按条件导出
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
            <el-button type="text" @click.stop="handleSingleExport(row)">导出</el-button>
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
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { Search, Refresh, Download } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { listSysOssFile, exportFile, exportBatch, exportByCondition } from '@/api/oss/sysOssFileApi'
import type { SysOssFile } from '@/api/oss/sysOssFileApi'
import { downloadFile } from '@/utils/fileDownload'
import Pagination from '@/components/Pagination/index.vue'

const loading = ref(false)
const dataList = ref<SysOssFile[]>([])
const total = ref(0)
const selectedRows = ref<SysOssFile[]>([])
const queryFormRef = ref()

const queryParams = reactive({
  pageQuery: { pageNum: 1, pageSize: 10 },
  suffix: undefined as string | undefined,
  startTime: undefined as string | undefined,
  endTime: undefined as string | undefined
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

const handleSingleExport = async (row: SysOssFile) => {
  try {
    const blob = await exportFile(row.ossId)
    downloadFile(blob, row.originalName || row.fileName)
  } catch (error) {
    ElMessage.error('导出失败')
  }
}

const handleBatchExport = async () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请选择要导出的文件')
    return
  }
  try {
    const ossIds = selectedRows.value.map(row => row.ossId)
    const blob = await exportBatch(ossIds)
    downloadFile(blob, 'export.zip')
  } catch (error) {
    ElMessage.error('批量导出失败')
  }
}

const handleExportCondition = async () => {
  try {
    const files = await exportByCondition({
      suffix: queryParams.suffix,
      startTime: queryParams.startTime,
      endTime: queryParams.endTime
    })
    if (files && files.length > 0) {
      const ossIds = files.map((f: any) => f.ossId)
      const blob = await exportBatch(ossIds)
      downloadFile(blob, 'export.zip')
    } else {
      ElMessage.warning('没有符合条件的文件')
    }
  } catch (error) {
    ElMessage.error('条件导出失败')
  }
}

onMounted(() => {
  getList()
})
</script>

<style scoped lang="scss">
.oss-export-container {
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
