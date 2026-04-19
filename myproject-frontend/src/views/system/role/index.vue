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

    <!--编辑与查看-->
    <el-dialog v-model="formDialogVisible" :title="dialogTitle" width="800px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="角色名" prop="roleName">
          <el-input v-model="form.roleName" placeholder="请输入角色名" />
        </el-form-item>
        <el-form-item label="菜单权限">
          <el-tree
              ref="menuTreeRef"
              :data="menuTreeData"
              :props="{ label: 'menuName', children: 'children' }"
              show-checkbox
              node-key="menuId"
              check-strictly
              :default-checked-keys="checkedMenuIds"
              style="max-height: 400px; overflow-y: auto;"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="formDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="viewDialogVisible" title="详情" width="800px" destroy-on-close>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="角色id">{{ currentRow?.roleId }}</el-descriptions-item>
        <el-descriptions-item label="角色名">{{ currentRow?.roleName }}</el-descriptions-item>
        <el-descriptions-item label="菜单权限" :span="2">
          <div style="max-height: 400px; overflow-y: auto;">
            <el-tree
                :data="menuTreeData"
                :props="{ label: 'menuName', children: 'children' }"
                node-key="menuId"
                default-expand-all
                :expand-on-click-node="false"
            >
              <template #default="{ data }">
                <span class="custom-tree-node">
                  <span>{{ data.menuName }}</span>
                  <span class="permission-icon">
                    <el-icon v-if="currentRow?.menuIds?.includes(data.menuId)" color="#67c23a">
                      <Check />
                    </el-icon>
                    <el-icon v-else color="#f56c6c">
                      <Close />
                    </el-icon>
                  </span>
                </span>
              </template>
            </el-tree>
          </div>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, nextTick } from 'vue'
import { Search, Refresh, Plus, Delete } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox, type TreeInstance } from 'element-plus'
import {
  listSysRole,
  deleteSysRole,
  createSysRole,
  updateSysRole,
  getByIdSysRole
} from '@/api/sysRoleApi'
import { listSysMenuTree } from '@/api/sysMenuApi'
import type { SysRole } from '@/api/sysRoleApi'
import type { MenuItem } from '@/api/sysMenuApi'

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
const formDialogVisible = ref(false)
const viewDialogVisible = ref(false)
const dialogTitle = ref('')
const currentRow = ref<SysRole>()
const selectedRow = ref<SysRole>()
const single = ref(true)
const formRef = ref()
const form = reactive<Partial<SysRole>>({
  roleId: undefined,
  roleName: '',
  menuIds: []
})
const rules = reactive<Record<string, any[]>>({
  roleName: [{ required: true, message: '请输入角色名', trigger: 'blur' }]
})

// 重置表单时使用
const resetForm = () => {
  Object.assign(form, {
    roleId: undefined,
    roleName: '',
    menuIds: []
  })
}

onMounted(() => {
  getList()
  loadMenuTree()
})

const menuTreeData = ref<MenuItem[]>([])
const checkedMenuIds = ref<number[]>([])
const menuTreeRef = ref<TreeInstance>()

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

const loadMenuTree = async () => {
  try {
    const res = await listSysMenuTree()
    menuTreeData.value = res.data
  } catch (error) {
    console.error('加载菜单树失败:', error)
    ElMessage.error('加载菜单树失败')
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

  resetForm()
// 确保新增时清空所有勾选
  nextTick(() => {
    menuTreeRef.value?.setCheckedKeys([])
  })
  formDialogVisible.value = true
}

const handleEdit = async (row: SysRole) => {
  dialogTitle.value = '编辑角色表'

  if (!row.roleId) {
    ElMessage.error('角色ID不存在')
    return
  }

  try {
    const res = await getByIdSysRole(row.roleId)

    // form = res.data 是错误的写法，因为 form 是 reactive() 对象，不能直接赋值。需要使用 Object.assign() 来更新属性。
    Object.assign(form, res.data)

    // ✅ 先打开对话框，再等待 DOM 更新，最后设置勾选
    formDialogVisible.value = true

    await nextTick()
    if (menuTreeRef.value && form.menuIds && form.menuIds.length > 0) {
      menuTreeRef.value.setCheckedKeys(form.menuIds)
    } else {
      menuTreeRef.value?.setCheckedKeys([])
    }


  } catch (error) {
    console.error('获取角色详情失败:', error)
    ElMessage.error('获取角色详情失败')
  }
}


const handleView = async (row: SysRole) => {
  if (!row.roleId) {
    ElMessage.error('角色ID不存在')
    return
  }

  try {
    const res = await getByIdSysRole(row.roleId)

    currentRow.value = res.data
    viewDialogVisible.value = true
  } catch (error) {
    console.error('获取角色详情失败:', error)
    ElMessage.error('获取角色详情失败')
  }
}

const handleDelete = async (row?: SysRole) => {
  if (!row || !row.roleId) return
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

const handleSubmit = async () => {
  const valid = await formRef.value?.validate()
  if (!valid) return

  try {
    // 从树形组件获取最新选中的菜单ID
    const checkedKeys = menuTreeRef.value?.getCheckedKeys() as number[]
    const halfCheckedKeys = menuTreeRef.value?.getHalfCheckedKeys() as number[]

    // 如果需要包含半选中的父节点,可以合并
    const allMenuIds = [...checkedKeys, ...halfCheckedKeys]

    console.log("编辑/新增form",JSON.stringify(form))

    const data = {
      roleId: form.roleId,
      roleName: form.roleName,
      menuIds: allMenuIds
    }

    console.log("编辑/新增",JSON.stringify(data))


    if (data?.roleId) {
      await updateSysRole(data)
      ElMessage.success('修改成功')
    } else {
      await createSysRole(data)
      ElMessage.success('新增成功')
    }
    formDialogVisible.value = false
    await getList()
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('操作失败')
  }
}

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
