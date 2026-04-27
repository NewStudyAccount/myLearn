<template>
  <div class="sysTag-container">
    <el-card class="search-card">
      <el-form :model="queryParams" ref="queryFormRef" label-width="100px">
        <el-row :gutter="20">
                  <el-col :span="8">
                    <el-form-item label="标签名" prop="name">
                      <el-input v-model="queryParams.name" placeholder="请输入标签名" clearable @keyup.enter="handleQuery" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="" prop="idDeleted">
                      <el-input v-model="queryParams.idDeleted" placeholder="请输入" clearable @keyup.enter="handleQuery" />
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
            <el-table-column label="" align="center" prop="id" />
            <el-table-column label="标签名" align="center" prop="name" />
            <el-table-column label="" align="center" prop="idDeleted" />
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
                <el-form-item label="标签名" prop="name">
                      <el-input v-model="form.name" placeholder="请输入标签名" />
                </el-form-item>
                <el-form-item label="" prop="idDeleted">
                      <el-input v-model="form.idDeleted" placeholder="请输入" />
                </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="formDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="viewDialogVisible" title="详情" width="800px" destroy-on-close>
      <el-descriptions :column="2" border>
            <el-descriptions-item label="">{{ currentRow?.id }}</el-descriptions-item>
            <el-descriptions-item label="标签名">{{ currentRow?.name }}</el-descriptions-item>
            <el-descriptions-item label="">{{ currentRow?.idDeleted }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
  import { ref, reactive, onMounted } from 'vue'
  import { Search, Refresh, Plus, Delete } from '@element-plus/icons-vue'
  import { ElMessage, ElMessageBox } from 'element-plus'
  import { listSysTag, getSysTagById,deleteSysTag, addSysTag, updateSysTag } from '@/api/blog/sysTagApi'
  import type { SysTag } from '@/api/blog/sysTagApi'

  const loading = ref(false)
  const dataList = ref<SysTag[]>([])
  const total = ref(0)
  const queryParams = reactive({
    pageQuery: {
      pageNum: 1,
      pageSize: 10
    },
                  name: undefined,
                  idDeleted: undefined,
  })
  const queryFormRef = ref()
  const formDialogVisible = ref(false)
  const viewDialogVisible = ref(false)
  const dialogTitle = ref('')
  const currentRow = ref<SysTag>()
  const selectedRow = ref<SysTag>()
  const single = ref(true)
  const formRef = ref()
  const form = reactive<Partial<SysTag>>({})
  const rules = reactive<Record<string, any[]>>({})

  const getList = async () => {
    loading.value = true
    try {
      const res = await listSysTag(queryParams)
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
    dialogTitle.value = '新增标签'
    currentRow.value = undefined
    resetForm()
    formDialogVisible.value = true
  }

  const handleEdit = async(row: SysTag) => {
    dialogTitle.value = '编辑标签'
    currentRow.value = row
    try {
      // 调用接口获取最新数据
      const res = await getSysTagById(row.id)
      Object.assign(form, res.data)
    } catch (error) {
      ElMessage.error('获取数据失败')
      return
    }
    formDialogVisible.value = true
  }

  const handleView = async (row: SysTag) => {
    try {
      // 调用接口获取最新数据用于查看
      const res = await getSysTagById(row.id)
      currentRow.value = res.data
      viewDialogVisible.value = true
    } catch (error) {
      ElMessage.error('获取数据失败')
    }
  }

  const handleDelete = async (row?: SysTag) => {
    if (!row) return
    try {
      await ElMessageBox.confirm('是否确认删除选中的数据?', '警告', { type: 'warning' })
      await deleteSysTag(row.id)
      ElMessage.success('删除成功')
      await getList()
    } catch {}
  }

  const handleSelectionChange = (selection: SysTag[]) => {
    single.value = selection.length !== 1
    if (selection.length === 1) {
      selectedRow.value = selection[0]
    }
  }

  const rowClick = (row: SysTag) => {
    currentRow.value = row
  }

  const handleSubmit = async () => {
    const valid = await formRef.value?.validate()
    if (!valid) return

    try {
      const data = { ...form }
      if (data.id) {
        await updateSysTag(data)
        ElMessage.success('修改成功')
      } else {
        await addSysTag(data)
        ElMessage.success('新增成功')
      }
      formDialogVisible.value = false
      await getList()
    } catch {}
  }

  // 重置表单函数
  const resetForm = () => {
    Object.keys(form).forEach(key => {
      (form as any)[key] = undefined
    })
  }
  onMounted(() => {
    getList()
  })
</script>

<style scoped lang="scss">
    .sysTag-container {
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
