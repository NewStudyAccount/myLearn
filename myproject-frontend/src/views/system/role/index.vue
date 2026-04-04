<template>
  <div class="sysRole-container">
    <el-card class="search-card">
      <el-form :model="queryParams" ref="queryFormRef" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="角色名" prop="roleName">
              <el-input v-model="queryParams.roleName" placeholder="请输入角色名" clearable @keyup.enter="handleQuery" />
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
        <el-table-column label="角色id" align="center" prop="roleId" />
        <el-table-column label="角色名" align="center" prop="roleName" />
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

    <FormDialog
      v-model:visible="dialogVisible"
      :title="dialogTitle"
      :data="currentRow"
      @success="getList"
    />

    <el-dialog v-model="viewDialogVisible" title="详情" width="800px" destroy-on-close>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="角色id">{{ currentRow?.roleId }}</el-descriptions-item>
        <el-descriptions-item label="角色名">{{ currentRow?.roleName }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { Search, Refresh, Plus, Delete } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listSysRole, deleteSysRole } from '@/api/sysRoleApi'
import type { SysRole } from '@/api/sysRoleApi'
import FormDialog from '@/components/role/FormDialog.vue'

const loading = ref(false)
const dataList = ref<SysRole[]>([])
const total = ref(0)
const queryParams = reactive({
  pageQuery: {
    pageNum: 1,
    pageSize: 10
  },
  roleName: undefined,
})
const queryFormRef = ref()
const dialogVisible = ref(false)
const viewDialogVisible = ref(false)
const dialogTitle = ref('')
const currentRow = ref<SysRole>()
const selectedRow = ref<SysRole>()
const single = ref(true)

const getList = async () => {
  loading.value = true
  try {
    const res = await listSysRole(queryParams)
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
  dialogTitle.value = '新增角色表'
  currentRow.value = undefined
  dialogVisible.value = true
}

const handleEdit = (row: SysRole) => {
  dialogTitle.value = '编辑角色表'
  currentRow.value = row
  dialogVisible.value = true
}

const handleView = (row: SysRole) => {
  currentRow.value = row
  viewDialogVisible.value = true
}

const handleDelete = async (row?: SysRole) => {
  if (!row) return
  try {
    await ElMessageBox.confirm('是否确认删除选中的数据?', '警告', { type: 'warning' })
    await deleteSysRole(row.roleId)
    ElMessage.success('删除成功')
    await getList()
  } catch {}
}

const handleSelectionChange = (selection: SysRole[]) => {
  single.value = selection.length !== 1
  if (selection.length === 1) {
    selectedRow.value = selection[0]
  }
}

const rowClick = (row: SysRole) => {
  currentRow.value = row
}

onMounted(() => {
  getList()
})
</script>

<style scoped lang="scss">
.sysRole-container {
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
