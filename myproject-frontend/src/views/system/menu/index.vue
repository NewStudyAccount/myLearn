<template>
  <div class="sysMenu-container">
    <el-card class="search-card">
      <el-form :model="queryParams" ref="queryFormRef" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="菜单名称" prop="menuName">
              <el-input v-model="queryParams.menuName" placeholder="请输入菜单名称" clearable @keyup.enter="handleQuery" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="权限code" prop="perCode">
              <el-input v-model="queryParams.perCode" placeholder="请输入权限code" clearable @keyup.enter="handleQuery" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="菜单类型" prop="menuType">
              <el-input v-model="queryParams.menuType" placeholder="请输入菜单类型" clearable @keyup.enter="handleQuery" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="路由地址" prop="path">
              <el-input v-model="queryParams.path" placeholder="请输入路由地址" clearable @keyup.enter="handleQuery" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="组件路径" prop="component">
              <el-input v-model="queryParams.component" placeholder="请输入组件路径" clearable @keyup.enter="handleQuery" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="" prop="componentName">
              <el-input v-model="queryParams.componentName" placeholder="请输入" clearable @keyup.enter="handleQuery" />
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
        <el-table-column label="菜单id" align="center" prop="menuId" />
        <el-table-column label="菜单名称" align="center" prop="menuName" />
        <el-table-column label="权限code" align="center" prop="perCode" />
        <el-table-column label="菜单类型" align="center" prop="menuType" />
        <el-table-column label="排序" align="center" prop="menuSort" />
        <el-table-column label="父级id" align="center" prop="parentId" />
        <el-table-column label="路由地址" align="center" prop="path" />
        <el-table-column label="组件路径" align="center" prop="component" />
        <el-table-column label="" align="center" prop="componentName" />
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
        <el-descriptions-item label="菜单id">{{ currentRow?.menuId }}</el-descriptions-item>
        <el-descriptions-item label="菜单名称">{{ currentRow?.menuName }}</el-descriptions-item>
        <el-descriptions-item label="权限code">{{ currentRow?.perCode }}</el-descriptions-item>
        <el-descriptions-item label="菜单类型">{{ currentRow?.menuType }}</el-descriptions-item>
        <el-descriptions-item label="排序">{{ currentRow?.menuSort }}</el-descriptions-item>
        <el-descriptions-item label="父级id">{{ currentRow?.parentId }}</el-descriptions-item>
        <el-descriptions-item label="路由地址">{{ currentRow?.path }}</el-descriptions-item>
        <el-descriptions-item label="组件路径">{{ currentRow?.component }}</el-descriptions-item>
        <el-descriptions-item label="">{{ currentRow?.componentName }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { Search, Refresh, Plus, Delete } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listSysMenu, deleteSysMenu } from '@/api/sysMenuApi'
import type { SysMenu } from '@/api/sysMenuApi'
import FormDialog from '@/components/menu/FormDialog.vue'

const loading = ref(false)
const dataList = ref<SysMenu[]>([])
const total = ref(0)
const queryParams = reactive({
  pageQuery: {
    pageNum: 1,
    pageSize: 10
  },
  menuName: undefined,
  perCode: undefined,
  menuType: undefined,
  path: undefined,
  component: undefined,
  componentName: undefined,
})
const queryFormRef = ref()
const dialogVisible = ref(false)
const viewDialogVisible = ref(false)
const dialogTitle = ref('')
const currentRow = ref<SysMenu>()
const selectedRow = ref<SysMenu>()
const single = ref(true)

const getList = async () => {
  loading.value = true
  try {
    const res = await listSysMenu(queryParams)
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
  dialogTitle.value = '新增菜单权限表'
  currentRow.value = undefined
  dialogVisible.value = true
}

const handleEdit = (row: SysMenu) => {
  dialogTitle.value = '编辑菜单权限表'
  currentRow.value = row
  dialogVisible.value = true
}

const handleView = (row: SysMenu) => {
  currentRow.value = row
  viewDialogVisible.value = true
}

const handleDelete = async (row?: SysMenu) => {
  if (!row) return
  try {
    await ElMessageBox.confirm('是否确认删除选中的数据?', '警告', { type: 'warning' })
    await deleteSysMenu(row.menuId)
    ElMessage.success('删除成功')
    await getList()
  } catch {}
}

const handleSelectionChange = (selection: SysMenu[]) => {
  single.value = selection.length !== 1
  if (selection.length === 1) {
    selectedRow.value = selection[0]
  }
}

const rowClick = (row: SysMenu) => {
  currentRow.value = row
}

onMounted(() => {
  getList()
})
</script>

<style scoped lang="scss">
.sysMenu-container {
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
