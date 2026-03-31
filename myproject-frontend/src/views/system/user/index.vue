<template>
  <div class="sysUser-container">
    <el-card class="search-card">
      <el-form :model="queryParams" ref="queryFormRef" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="用户名" prop="userName">
              <el-input v-model="queryParams.userName" placeholder="请输入用户名" clearable @keyup.enter="handleQuery" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="密码" prop="userPwd">
              <el-input v-model="queryParams.userPwd" placeholder="请输入密码" clearable @keyup.enter="handleQuery" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="头像" prop="userAvatorUrl">
              <el-input v-model="queryParams.userAvatorUrl" placeholder="请输入头像" clearable @keyup.enter="handleQuery" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="性别" prop="userSex">
              <el-input v-model="queryParams.userSex" placeholder="请输入性别" clearable @keyup.enter="handleQuery" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="手机" prop="userPhone">
              <el-input v-model="queryParams.userPhone" placeholder="请输入手机" clearable @keyup.enter="handleQuery" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="创建时间" prop="createDate">
              <el-input v-model="queryParams.createDate" placeholder="请输入创建时间" clearable @keyup.enter="handleQuery" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="修改时间" prop="updateDate">
              <el-input v-model="queryParams.updateDate" placeholder="请输入修改时间" clearable @keyup.enter="handleQuery" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="逻辑删除0：有效，1删除" prop="isDeleted">
              <el-input v-model="queryParams.isDeleted" placeholder="请输入逻辑删除0：有效，1删除" clearable @keyup.enter="handleQuery" />
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
        <el-table-column label="用户id" align="center" prop="userId" />
        <el-table-column label="用户名" align="center" prop="userName" />
        <el-table-column label="密码" align="center" prop="userPwd" />
        <el-table-column label="头像" align="center" prop="userAvatorUrl" />
        <el-table-column label="性别" align="center" prop="userSex" />
        <el-table-column label="手机" align="center" prop="userPhone" />
        <el-table-column label="创建人id" align="center" prop="createId" />
        <el-table-column label="创建时间" align="center" prop="createDate" />
        <el-table-column label="修改人id" align="center" prop="updateId" />
        <el-table-column label="修改时间" align="center" prop="updateDate" />
        <el-table-column label="逻辑删除0：有效，1删除" align="center" prop="isDeleted" />
        <el-table-column label="操作" width="180" align="center">
          <template #default="{ row }">
            <el-button type="text" @click.stop="handleView(row)">查看</el-button>
            <el-button type="text" @click.stop="handleEdit(row)">编辑</el-button>
            <el-button type="text" @click.stop="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-model:page="queryParams.pageNo" v-model:limit="queryParams.pageSize" :total="total" @pagination="getList" />
    </el-card>

<!--    <FormDialog-->
<!--      v-model:visible="dialogVisible"-->
<!--      :title="dialogTitle"-->
<!--      :data="currentRow"-->
<!--      @success="getList"-->
<!--    />-->

    <el-dialog v-model="viewDialogVisible" title="详情" width="800px" destroy-on-close>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="用户id">{{ currentRow?.userId }}</el-descriptions-item>
        <el-descriptions-item label="用户名">{{ currentRow?.userName }}</el-descriptions-item>
        <el-descriptions-item label="密码">{{ currentRow?.userPwd }}</el-descriptions-item>
        <el-descriptions-item label="头像">{{ currentRow?.userAvatorUrl }}</el-descriptions-item>
        <el-descriptions-item label="性别">{{ currentRow?.userSex }}</el-descriptions-item>
        <el-descriptions-item label="手机">{{ currentRow?.userPhone }}</el-descriptions-item>
        <el-descriptions-item label="创建人id">{{ currentRow?.createId }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ currentRow?.createDate }}</el-descriptions-item>
        <el-descriptions-item label="修改人id">{{ currentRow?.updateId }}</el-descriptions-item>
        <el-descriptions-item label="修改时间">{{ currentRow?.updateDate }}</el-descriptions-item>
        <el-descriptions-item label="逻辑删除0：有效，1删除">{{ currentRow?.isDeleted }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { Search, Refresh, Plus, Delete } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listSysUser, deleteSysUser } from '@/api/sysUserApi'
import type { SysUser } from '@/api/sysUserApi'
// import FormDialog from './components/FormDialog.vue'

const loading = ref(false)
const dataList = ref<SysUser[]>([])
const total = ref(0)
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  userName: undefined,
  userPwd: undefined,
  userAvatorUrl: undefined,
  userSex: undefined,
  userPhone: undefined,
  createDate: undefined,
  updateDate: undefined,
  isDeleted: undefined,
})
const queryFormRef = ref()
const dialogVisible = ref(false)
const viewDialogVisible = ref(false)
const dialogTitle = ref('')
const currentRow = ref<SysUser>()
const selectedRow = ref<SysUser>()
const single = ref(true)

const getList = async () => {
  loading.value = true
  try {
    const res = await listSysUser(queryParams)
    dataList.value = res.list
    total.value = res.total
  } finally {
    loading.value = false
  }
}

const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

const resetQuery = () => {
  queryFormRef.value?.resetFields()
  handleQuery()
}

const handleAdd = () => {
  dialogTitle.value = '新增用户表'
  currentRow.value = undefined
  dialogVisible.value = true
}

const handleEdit = (row: SysUser) => {
  dialogTitle.value = '编辑用户表'
  currentRow.value = row
  dialogVisible.value = true
}

const handleView = (row: SysUser) => {
  currentRow.value = row
  viewDialogVisible.value = true
}

const handleDelete = async (row?: SysUser) => {
  if (!row) return
  try {
    await ElMessageBox.confirm('是否确认删除选中的数据?', '警告', { type: 'warning' })
    await deleteSysUser(row.userId)
    ElMessage.success('删除成功')
    await getList()
  } catch {}
}

const handleSelectionChange = (selection: SysUser[]) => {
  single.value = selection.length !== 1
  if (selection.length === 1) {
    selectedRow.value = selection[0]
  }
}

const rowClick = (row: SysUser) => {
  currentRow.value = row
}

onMounted(() => {
  getList()
})
</script>

<style scoped lang="scss">
.sysUser-container {
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
