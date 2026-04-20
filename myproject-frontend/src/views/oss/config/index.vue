<template>
  <div class="sysOssConfig-container">
    <el-card class="search-card">
      <el-form :model="queryParams" ref="queryFormRef" label-width="100px">
        <el-row :gutter="20">
                  <el-col :span="8">
                    <el-form-item label="配置名称" prop="configName">
                      <el-input v-model="queryParams.configName" placeholder="请输入配置名称（唯一标识）" clearable @keyup.enter="handleQuery" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="提供商类型" prop="provider">
                      <el-input v-model="queryParams.provider" placeholder="请输入提供商类型（aliyun、minio等）" clearable @keyup.enter="handleQuery" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="存储桶名称" prop="bucketName">
                      <el-input v-model="queryParams.bucketName" placeholder="请输入存储桶名称" clearable @keyup.enter="handleQuery" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="是否启用" prop="isActive">
                      <el-select v-model="queryParams.isActive" placeholder="请选择是否启用" clearable @change="handleQuery">
                        <el-option label="启用" :value="1" />
                        <el-option label="禁用" :value="0" />
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
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>新增
        </el-button>
        <el-button :disabled="single" type="danger" @click="handleDelete(selectedRow)">
          <el-icon><Delete /></el-icon>删除
        </el-button>
      </div>

      <el-table v-loading="loading" :data="dataList" @row-click="rowClick" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="配置名称" align="center" prop="configName" />
            <el-table-column label="提供商类型" align="center" prop="provider" />
            <el-table-column label="服务端点" align="center" prop="endpoint" />
            <el-table-column label="存储桶名称" align="center" prop="bucketName" />
            <el-table-column label="区域（可选）" align="center" prop="region" />
            <el-table-column label="是否启用" align="center" prop="isActive">
              <template #default="{ row }">
                <el-tag v-if="row.isActive === true" type="success">启用</el-tag>
                <el-tag v-else type="danger">禁用</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="创建时间" align="center" prop="createdAt" />
            <el-table-column label="更新时间" align="center" prop="updatedAt" />
        <el-table-column label="操作" width="180" align="center">
          <template #default="{ row }">
            <el-button type="text" @click.stop="handleView(row)">查看</el-button>
            <el-button type="text" @click.stop="handleEdit(row)">编辑</el-button>
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
                <el-form-item label="配置名称（唯一标识）" prop="configName">
                      <el-input v-model="form.configName" placeholder="请输入配置名称（唯一标识）" />
                </el-form-item>
                <el-form-item label="提供商类型（aliyun、minio等）" prop="provider">
                      <el-input v-model="form.provider" placeholder="请输入提供商类型（aliyun、minio等）" />
                </el-form-item>
                <el-form-item label="服务端点" prop="endpoint">
                      <el-input v-model="form.endpoint" placeholder="请输入服务端点" />
                </el-form-item>
                <el-form-item label="访问密钥" prop="accessKey">
                      <el-input v-model="form.accessKey" placeholder="请输入访问密钥" />
                </el-form-item>
                <el-form-item label="秘密密钥" prop="secretKey">
                      <el-input v-model="form.secretKey" placeholder="请输入秘密密钥" />
                </el-form-item>
                <el-form-item label="存储桶名称" prop="bucketName">
                      <el-input v-model="form.bucketName" placeholder="请输入存储桶名称" />
                </el-form-item>
                <el-form-item label="区域（可选）" prop="region">
                      <el-input v-model="form.region" placeholder="请输入区域（可选）" />
                </el-form-item>
                <el-form-item label="JSON格式的扩展配置" prop="extraConfig">
                      <el-input v-model="form.extraConfig" type="textarea" placeholder="请输入JSON格式的扩展配置" />
                </el-form-item>
                <el-form-item label="是否启用" prop="isActive">
                  <el-switch v-model="form.isActive" :active-value="1" :inactive-value="0" active-text="启用" inactive-text="禁用" />
                </el-form-item>

      </el-form>
      <template #footer>
        <el-button @click="formDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="viewDialogVisible" title="详情" width="800px" destroy-on-close>
      <el-descriptions :column="2" border>
            <el-descriptions-item label="主键ID">{{ currentRow?.id }}</el-descriptions-item>
            <el-descriptions-item label="配置名称（唯一标识）">{{ currentRow?.configName }}</el-descriptions-item>
            <el-descriptions-item label="提供商类型（aliyun、minio等）">{{ currentRow?.provider }}</el-descriptions-item>
            <el-descriptions-item label="服务端点">{{ currentRow?.endpoint }}</el-descriptions-item>
            <el-descriptions-item label="访问密钥">{{ currentRow?.accessKey }}</el-descriptions-item>
            <el-descriptions-item label="秘密密钥">{{ currentRow?.secretKey }}</el-descriptions-item>
            <el-descriptions-item label="存储桶名称">{{ currentRow?.bucketName }}</el-descriptions-item>
            <el-descriptions-item label="区域（可选）">{{ currentRow?.region }}</el-descriptions-item>
            <el-descriptions-item label="JSON格式的扩展配置">{{ currentRow?.extraConfig }}</el-descriptions-item>
            <el-descriptions-item label="是否启用">
              <el-tag v-if="currentRow?.isActive === 1" type="success">启用</el-tag>
              <el-tag v-else type="danger">禁用</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="创建时间">{{ currentRow?.createdAt }}</el-descriptions-item>
            <el-descriptions-item label="更新时间">{{ currentRow?.updatedAt }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
  import { ref, reactive, onMounted, watch } from 'vue'
  import { Search, Refresh, Plus, Delete } from '@element-plus/icons-vue'
  import { ElMessage, ElMessageBox } from 'element-plus'
  import { listSysOssConfig, deleteSysOssConfig, createSysOssConfig, updateSysOssConfig } from '@/api/oss/sysOssConfigApi'
  import type { SysOssConfig } from '@/api/oss/sysOssConfigApi'

  const loading = ref(false)
  const dataList = ref<SysOssConfig[]>([])
  const total = ref(0)
  const queryParams = reactive({
    pageQuery: {
      pageNum: 1,
      pageSize: 10
    },
                  configName: undefined,
                  provider: undefined,
                  endpoint: undefined,
                  accessKey: undefined,
                  secretKey: undefined,
                  bucketName: undefined,
                  region: undefined,
                  extraConfig: undefined,
                  isActive: undefined,
                  createdAt: undefined,
                  updatedAt: undefined,
  })
  const queryFormRef = ref()
  const formDialogVisible = ref(false)
  const viewDialogVisible = ref(false)
  const dialogTitle = ref('')
  const currentRow = ref<SysOssConfig>()
  const selectedRow = ref<SysOssConfig>()
  const single = ref(true)
  const formRef = ref()
  const form = reactive<Partial<SysOssConfig>>({})
  const rules = reactive<Record<string, any[]>>({})

  const getList = async () => {
    loading.value = true
    try {
      const res = await listSysOssConfig(queryParams)
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

  const handleAdd = () => {
    dialogTitle.value = '新增OSS配置表'
    currentRow.value = undefined
    Object.keys(form).forEach(key => {
      (form as any)[key] = undefined
    })
    formDialogVisible.value = true
  }

  const handleEdit = (row: SysOssConfig) => {
    dialogTitle.value = '编辑OSS配置表'
    currentRow.value = row
    Object.assign(form, row)
    formDialogVisible.value = true
  }

  const handleView = (row: SysOssConfig) => {
    currentRow.value = row
    viewDialogVisible.value = true
  }

  const handleDelete = async (row?: SysOssConfig) => {
    if (!row) return
    try {
      await ElMessageBox.confirm('是否确认删除选中的数据?', '警告', { type: 'warning' })
      await deleteSysOssConfig(row.id)
      ElMessage.success('删除成功')
      await getList()
    } catch {}
  }

  const handleSelectionChange = (selection: SysOssConfig[]) => {
    single.value = selection.length !== 1
    if (selection.length === 1) {
      selectedRow.value = selection[0]
    }
  }

  const rowClick = (row: SysOssConfig) => {
    currentRow.value = row
  }

  const handleSubmit = async () => {
    const valid = await formRef.value?.validate()
    if (!valid) return

    try {
      const data = { ...form }
      if (data.id) {
        await updateSysOssConfig(data)
        ElMessage.success('修改成功')
      } else {
        await createSysOssConfig(data)
        ElMessage.success('新增成功')
      }
      formDialogVisible.value = false
      await getList()
    } catch {}
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
    .sysOssConfig-container {
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
