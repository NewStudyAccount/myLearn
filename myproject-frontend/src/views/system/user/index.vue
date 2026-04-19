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
            <el-form-item label="性别" prop="userSex">
              <el-input v-model="queryParams.userSex" placeholder="请输入性别" clearable @keyup.enter="handleQuery" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="手机" prop="userPhone">
              <el-input v-model="queryParams.userPhone" placeholder="请输入手机" clearable @keyup.enter="handleQuery" />
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
        <el-table-column label="用户ID" align="center" prop="userId" width="80" />
        <el-table-column label="用户名" align="center" prop="userName" min-width="120" />
        <el-table-column label="头像" align="center" prop="userAvatorUrl" width="80">
          <template #default="{ row }">
            <el-avatar :size="40" :src="row.userAvatorUrl" v-if="row.userAvatorUrl">
              <el-icon><User /></el-icon>
            </el-avatar>
            <el-avatar :size="40" v-else>
              <el-icon><User /></el-icon>
            </el-avatar>
          </template>
        </el-table-column>
        <el-table-column label="性别" align="center" prop="userSex" width="80">
          <template #default="{ row }">
            <el-tag v-if="row.userSex === '0'" type="primary">男</el-tag>
            <el-tag v-else-if="row.userSex === '1'" type="danger">女</el-tag>
            <el-tag v-else type="info">未知</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="手机" align="center" prop="userPhone" width="120" />
        <el-table-column label="角色" align="center" prop="roleIds" min-width="150">
          <template #default="{ row }">
            <el-tag
                v-for="roleId in row.roleIds"
                :key="roleId"
                size="small"
                style="margin: 2px"
            >
              {{ getRoleName(roleId) }}
            </el-tag>
            <span v-if="!row.roleIds || row.roleIds.length === 0">-</span>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="createDate" width="160" />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click.stop="handleView(row)">查看</el-button>
            <el-button link type="primary" size="small" @click.stop="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" size="small" @click.stop="handleDelete(row)">删除</el-button>
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
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="form.userName" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item v-if="form.userId == undefined" label="密码" prop="userPwd">
          <el-input
              v-model="form.userPwd"
              type="password"
              placeholder="请输入密码"
              show-password
          />
        </el-form-item>
        <el-form-item label="性别" prop="userSex">
          <el-radio-group v-model="form.userSex">
            <el-radio label="0">男</el-radio>
            <el-radio label="1">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="手机" prop="userPhone">
          <el-input v-model="form.userPhone" placeholder="请输入手机号" maxlength="11" />
        </el-form-item>
        <el-form-item label="头像URL" prop="userAvatorUrl">
          <el-input v-model="form.userAvatorUrl" placeholder="请输入头像URL" />
        </el-form-item>
        <el-form-item label="角色" prop="roleIds">
          <el-select
              v-model="form.roleIds"
              multiple
              placeholder="请选择角色"
              collapse-tags
              collapse-tags-tooltip
              style="width: 100%"
          >
            <el-option
                v-for="role in roleList"
                :key="role.roleId"
                :label="role.roleName"
                :value="role.roleId"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="formDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="viewDialogVisible" title="详情" width="800px" destroy-on-close>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="用户ID">{{ currentRow?.userId }}</el-descriptions-item>
        <el-descriptions-item label="用户名">{{ currentRow?.userName }}</el-descriptions-item>
        <el-descriptions-item label="性别">
          <el-tag v-if="currentRow?.userSex === '0'" type="primary">男</el-tag>
          <el-tag v-else-if="currentRow?.userSex === '1'" type="danger">女</el-tag>
          <el-tag v-else type="info">未知</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="手机">{{ currentRow?.userPhone || '-' }}</el-descriptions-item>

        <el-descriptions-item label="角色" :span="2">
          <el-tag
              v-for="roleId in currentRow?.roleIds"
              :key="roleId"
              style="margin: 2px"
          >
            {{ getRoleName(roleId) }}
          </el-tag>
          <span v-if="!currentRow?.roleIds || currentRow.roleIds.length === 0">-</span>
        </el-descriptions-item>

        <el-descriptions-item label="创建时间">{{ currentRow?.createDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="修改时间">{{ currentRow?.updateDate || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { Search, Refresh, Plus, Delete, User } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  listSysUser,
  deleteSysUser,
  createSysUser,
  updateSysUser,
  type SysUser,
  getByIdSysUser,
  type SysUserVo
} from '@/api/sysUserApi'
import { listSysRole, type SysRole } from '@/api/sysRoleApi'

const loading = ref(false)
const dataList = ref<SysUser[]>([])
const total = ref(0)
const roleList = ref<SysRole[]>([])
const queryParams = reactive({
  pageQuery: {
    pageNum: 1,
    pageSize: 10
  },
  userName: undefined,
  userSex: undefined,
  userPhone: undefined,
})
const queryFormRef = ref()
const formDialogVisible = ref(false)
const viewDialogVisible = ref(false)
const dialogTitle = ref('')
const currentRow = ref<SysUserVo>()
const selectedRow = ref<SysUser>()
const single = ref(true)
const formRef = ref()

const form = reactive<SysUserVo>({
  userId: undefined,
  userName: '',
  userPwd: '',
  userAvatorUrl: '',
  userSex: '0',
  userPhone: '',
  createId: undefined,
  createDate: '',
  updateId: undefined,
  updateDate: '',
  isDeleted: '0',
  roleIds: []
})


const createEmptyForm = (): Partial<SysUserVo> => ({
  userId: undefined,
  userName: '',
  userPwd: '',
  userAvatorUrl: '',
  userSex: '0',
  userPhone: '',
  createId: undefined,
  createDate: '',
  updateId: undefined,
  updateDate: '',
  isDeleted: '0',
  roleIds: []
})

const resetForm = () => {
  Object.assign(form, createEmptyForm())
}

const rules = reactive<Record<string, any[]>>({
  userName: [{ required: true, message: '用户名不能为空', trigger: 'blur' }],
  userPwd: [{ required: true, message: '密码不能为空', trigger: 'blur' }],
  userPhone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ]
})

const getList = async () => {
  loading.value = true
  try {
    const res = await listSysUser(queryParams)
    dataList.value = res.data.rows
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const getRoleList = async () => {
  try {
    const res = await listSysRole({
      pageQuery: {
        pageNum: 1,
        pageSize: 1000
      }
    })
    roleList.value = res.data.rows || []
  } catch (error) {
    console.error('获取角色列表失败:', error)
  }
}

const getRoleName = (roleId: number) => {
  const role = roleList.value.find(r => r.roleId === roleId)
  return role ? role.roleName : `角色${roleId}`
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
  dialogTitle.value = '新增用户'
  currentRow.value = undefined
  resetForm()
  formDialogVisible.value = true
}

const handleEdit = async (row: SysUser) => {
  dialogTitle.value = '编辑用户'
  if (!row.userId) {
    ElMessage.error('用户ID不存在')
    return
  }
  try {
    const res = await getByIdSysUser(row.userId)

    console.log("查询用户信息",JSON.stringify(res))
    Object.assign(form, res.data)
    formDialogVisible.value = true
  } catch (error) {
    console.error('获取用户详情失败:', error)
    ElMessage.error('获取用户详情失败')
  }
}

const handleView = async (row: SysUser) => {

  if (!row.userId) {
    ElMessage.error('用户ID不存在')
    return
  }

  try {
    const res = await getByIdSysUser(row.userId)
    currentRow.value = res.data
    viewDialogVisible.value = true
  } catch (error) {
    console.error('获取用户详情失败:', error)
    ElMessage.error('获取用户详情失败')
  }
}

const handleDelete = async (row?: SysUser) => {
  if (!row) return
  try {
    await ElMessageBox.confirm(`是否确认删除用户"${row.userName}"?`, '警告', { type: 'warning' })
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

const handleSubmit = async () => {
  const valid = await formRef.value?.validate()
  if (!valid) return

  try {
    const data = { ...form }
    if (data.userId) {
      await updateSysUser(data)
      ElMessage.success('修改成功')
    } else {
      await createSysUser(data)
      ElMessage.success('新增成功')
    }
    formDialogVisible.value = false
    await getList()
  } catch {}
}

onMounted(() => {
  getRoleList()
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
