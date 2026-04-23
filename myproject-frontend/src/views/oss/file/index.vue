<template>
  <div class="sysOssFile-container">
    <el-card class="search-card">
      <el-form :model="queryParams" ref="queryFormRef" label-width="100px">
        <el-row :gutter="20">
                  <el-col :span="8">
                    <el-form-item label="" prop="fileName">
                      <el-input v-model="queryParams.fileName" placeholder="请输入" clearable @keyup.enter="handleQuery" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="" prop="originalName">
                      <el-input v-model="queryParams.originalName" placeholder="请输入" clearable @keyup.enter="handleQuery" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="" prop="fileSuffix">
                      <el-input v-model="queryParams.fileSuffix" placeholder="请输入" clearable @keyup.enter="handleQuery" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="" prop="fileUrl">
                      <el-input v-model="queryParams.fileUrl" placeholder="请输入" clearable @keyup.enter="handleQuery" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="" prop="contentType">
                      <el-input v-model="queryParams.contentType" placeholder="请输入" clearable @keyup.enter="handleQuery" />
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
        <el-button type="primary" @click="handleUpload">
          <el-icon><Upload /></el-icon>上传文件
        </el-button>
        <el-button type="success" @click="handleImageUpload">
          <el-icon><Picture /></el-icon>上传图片
        </el-button>
        <el-button :disabled="single" type="danger" @click="handleDelete(selectedRow)">
          <el-icon><Delete /></el-icon>删除
        </el-button>
      </div>

      <el-table v-loading="loading" :data="dataList" @row-click="rowClick" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="文件Id" align="center" prop="ossId" />
            <el-table-column label="预览" align="center" width="100">
              <template #default="{ row }">
                <el-image
                    v-if="isImage(row.fileSuffix)"
                    :src="row.fileUrl"
                    :preview-src-list="[row.fileUrl]"
                    fit="cover"                  style="width: 60px; height: 60px; border-radius: 4px; cursor: pointer;"
                    preview-teleported
                />
                <span v-else>-</span>
              </template>
            </el-table-column>
            <el-table-column label="文件名" align="center" prop="fileName" />
            <el-table-column label="原始文件名" align="center" prop="originalName" />
            <el-table-column label="后缀" align="center" prop="fileSuffix" />
            <el-table-column label="文件访问Url" align="center" prop="fileUrl" />
            <el-table-column label="" align="center" prop="contentType" />
        <el-table-column label="操作" width="180" align="center">
          <template #default="{ row }">
            <el-button type="text" @click.stop="handleDownload(row)">下载</el-button>
            <el-button type="text" @click.stop="handlePreview(row)">预览</el-button>
            <el-button type="text" @click.stop="handleView(row)">查看</el-button>
            <el-button type="text" @click.stop="handleDelete(row)">删除</el-button>
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

    <el-dialog v-model="formDialogVisible" :title="dialogTitle" width="800px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
                <el-form-item label="" prop="fileName">
                      <el-input v-model="form.fileName" placeholder="请输入" />
                </el-form-item>
                <el-form-item label="" prop="originalName">
                      <el-input v-model="form.originalName" placeholder="请输入" />
                </el-form-item>
                <el-form-item label="" prop="fileSuffix">
                      <el-input v-model="form.fileSuffix" placeholder="请输入" />
                </el-form-item>
                <el-form-item label="" prop="fileUrl">
                      <el-input v-model="form.fileUrl" placeholder="请输入" />
                </el-form-item>
                <el-form-item label="" prop="contentType">
                      <el-input v-model="form.contentType" placeholder="请输入" />
                </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="formDialogVisible = false">取消</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="viewDialogVisible" title="详情" width="800px" destroy-on-close>
      <el-descriptions :column="2" border>
            <el-descriptions-item label="文件Id">{{ currentRow?.ossId }}</el-descriptions-item>
            <el-descriptions-item label="文件名">{{ currentRow?.fileName }}</el-descriptions-item>
            <el-descriptions-item label="原始文件名">{{ currentRow?.originalName }}</el-descriptions-item>
            <el-descriptions-item label="后缀">{{ currentRow?.fileSuffix }}</el-descriptions-item>
            <el-descriptions-item label="访问URL">{{ currentRow?.fileUrl }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <FileUpload
        v-model="uploadDialogVisible"
        title="上传文件"
        :limit="1"
        tip="支持任意格式文件上传"
        @success="handleUploadSuccess"
    />

    <el-dialog v-model="imageUploadDialogVisible" title="上传图片" width="600px" destroy-on-close>
      <ImageUpload
          v-model="uploadedImageUrl"
          :limit="9"
          :multiple="true"
          tip="支持 jpg、png、gif 格式，单个文件不超过 5MB"
      />
      <template #footer>
        <el-button @click="imageUploadDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleImageUploadConfirm">确定</el-button>
      </template>
    </el-dialog>

  </div>
</template>

<script setup lang="ts">
  import { ref, reactive, onMounted, watch } from 'vue'
  import { Search, Refresh, Delete } from '@element-plus/icons-vue'
  import { ElMessage, ElMessageBox } from 'element-plus'
  import {listSysOssFile, deleteSysOssFile, uploadSysOssFile, downloadSysOssFile} from '@/api/oss/sysOssFileApi'
  import type { SysOssFile } from '@/api/oss/sysOssFileApi'
  import { downloadFile } from '@/utils/fileDownload'
  import FileUpload from '@/components/FileUpload/index.vue'
  import ImageUpload from '@/components/ImageUpload/index.vue'

  const loading = ref(false)
  const dataList = ref<SysOssFile[]>([])
  const total = ref(0)
  const queryParams = reactive({
    pageQuery: {
      pageNum: 1,
      pageSize: 10
    },
                  fileName: undefined,
                  originalName: undefined,
                  fileSuffix: undefined,
                  fileUrl: undefined,
                  contentType: undefined,
  })
  const queryFormRef = ref()
  const formDialogVisible = ref(false)
  const viewDialogVisible = ref(false)
  const uploadDialogVisible = ref(false)
  const imageUploadDialogVisible = ref(false)
  const uploadedImageUrl = ref<string | string[]>('')

  const dialogTitle = ref('')
  const currentRow = ref<SysOssFile>()
  const selectedRow = ref<SysOssFile>()
  const single = ref(true)
  const formRef = ref()
  const form = reactive<Partial<SysOssFile>>({})
  const rules = reactive<Record<string, any[]>>({})

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





  const handleView = (row: SysOssFile) => {
    currentRow.value = row
    viewDialogVisible.value = true
  }




  const handleImageUpload = () => {
    uploadedImageUrl.value = ''
    imageUploadDialogVisible.value = true
  }

  const handleImageUploadConfirm = async () => {
    if (!uploadedImageUrl.value) {
      ElMessage.warning('请至少上传一张图片')
      return
    }

    try {
      const urls = Array.isArray(uploadedImageUrl.value)
          ? uploadedImageUrl.value
          : [uploadedImageUrl.value]

      for (const url of urls) {
        ElMessage.success('图片上传成功')
      }

      imageUploadDialogVisible.value = false
      await getList()
    } catch (error) {
      ElMessage.error('操作失败')
    }
  }

  // 文件上传
  const handleUpload = () => {
    uploadDialogVisible.value = true
  }


  const handleUploadSuccess = async (files: File[]) => {
    if (!files || files.length === 0) return

    try {
      await uploadSysOssFile(files[0])
      ElMessage.success('上传成功')
      uploadDialogVisible.value = false
      await getList()
    } catch (error) {
      ElMessage.error('上传失败')
    }
  }

  const handleDownload = async (row: SysOssFile) => {
    if (!row.fileName) {
      ElMessage.warning('文件不存在')
      return
    }
    try {
      const blob = await downloadSysOssFile(row.fileName)
      downloadFile(blob, row.originalName || row.fileName)
    } catch (error) {
      ElMessage.error('下载失败')
    }
  }

  const isImage = (suffix?: string) => {
    if (!suffix) return false
    const imageSuffixes = ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp', 'svg']
    return imageSuffixes.includes(suffix.toLowerCase())
  }

  const handlePreview = (row: SysOssFile) => {
    if (row.fileUrl) {
      window.open(row.fileUrl, '_blank')
    } else {
      ElMessage.warning('文件链接不存在')
    }
  }



  const handleDelete = async (row?: SysOssFile) => {
    if (!row) return
    try {
      await ElMessageBox.confirm('是否确认删除选中的数据?', '警告', { type: 'warning' })
      console.log('是否确认删除选中的数据'+JSON.stringify(row))
      console.log('是否确认删除选中的数据'+row.ossId)
      await deleteSysOssFile(row.ossId)
      ElMessage.success('删除成功')
      await getList()
    } catch {}
  }

  const handleSelectionChange = (selection: SysOssFile[]) => {
    single.value = selection.length !== 1
    if (selection.length === 1) {
      selectedRow.value = selection[0]
    }
  }

  const rowClick = (row: SysOssFile) => {
    currentRow.value = row
  }


  watch(() => currentRow.value, (val) => {
    if (val) {
      Object.assign(form, val)
    } else {
      Object.keys(form).forEach(key => {
        (form as any)[key] = undefined
      })
    }
  }, { immediate: true })

  onMounted(() => {
    getList()
  })
</script>

<style scoped lang="scss">
    .sysOssFile-container {
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
