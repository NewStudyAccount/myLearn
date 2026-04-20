<template>
  <div class="sysMenu-container">

    <el-card class="table-card">
      <div class="toolbar">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>新增
        </el-button>
        <el-button :disabled="single" type="danger" @click="handleDelete(selectedRow)">
          <el-icon><Delete /></el-icon>删除
        </el-button>
        <el-button type="warning" @click="handleRefresh">
          <el-icon><Refresh /></el-icon>刷新
        </el-button>
      </div>

      <el-table
          v-loading="loading"
          :data="dataList"
          row-key="menuId"
          :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
          default-expand-all
          @row-click="rowClick"
          @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="菜单名称" align="left" prop="menuName" min-width="200" />
        <el-table-column label="菜单ID" align="center" prop="menuId" width="100" />
        <el-table-column label="图标" align="center" prop="icon" width="80">
          <template #default="{ row }">
            <el-icon v-if="row.icon"><component :is="row.icon" /></el-icon>
          </template>
        </el-table-column>
        <el-table-column label="菜单类型" align="center" prop="menuType" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.menuType === 'M'" type="primary">目录</el-tag>
            <el-tag v-else-if="row.menuType === 'C'" type="success">菜单</el-tag>
            <el-tag v-else-if="row.menuType === 'F'" type="warning">按钮</el-tag>
            <el-tag v-else>{{ row.menuType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="权限标识" align="center" prop="perCode" min-width="150" show-overflow-tooltip />
        <el-table-column label="排序" align="center" prop="menuSort" width="80" />
        <el-table-column label="路由地址" align="center" prop="path" min-width="150" show-overflow-tooltip />
        <el-table-column label="组件路径" align="center" prop="component" min-width="200" show-overflow-tooltip />
        <el-table-column label="状态" align="center" prop="status" width="80">
          <template #default="{ row }">
            <el-tag v-if="row.status === 0" type="success">正常</el-tag>
            <el-tag v-else type="danger">停用</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click.stop="handleView(row)">查看</el-button>
            <el-button link type="primary" size="small" @click.stop="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" size="small" @click.stop="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>


    </el-card>

    <el-dialog v-model="formDialogVisible" :title="dialogTitle" width="800px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
                <el-form-item label="菜单名称" prop="menuName">
                      <el-input v-model="form.menuName" placeholder="请输入菜单名称" />
                </el-form-item>
                <el-form-item label="权限code" prop="perCode">
                      <el-input v-model="form.perCode" placeholder="请输入权限code" />
                </el-form-item>
                <el-form-item label="菜单类型" prop="menuType">
                  <el-select v-model="form.menuType" placeholder="请选择菜单类型" style="width: 100%">
                    <el-option label="目录" value="M" />
                    <el-option label="菜单" value="C" />
                    <el-option label="按钮" value="F" />
                  </el-select>
                </el-form-item>
                <el-form-item label="排序" prop="menuSort">
                      <el-input v-model="form.menuSort" placeholder="请输入排序" />
                </el-form-item>
                <el-form-item label="父级id" prop="parentId">
                      <el-input v-model="form.parentId" placeholder="请输入父级id" />
                </el-form-item>
                <el-form-item label="路由地址" prop="path">
                      <el-input v-model="form.path" placeholder="请输入路由地址" />
                </el-form-item>
                <el-form-item label="组件路径" prop="component">
                      <el-input v-model="form.component" placeholder="请输入组件路径" />
                </el-form-item>
                <el-form-item label="组件名称" prop="componentName">
                      <el-input v-model="form.componentName" placeholder="请输入" />
                </el-form-item>
                <el-form-item label="状态" prop="status">
                  <el-switch v-model="form.status" :active-value="0" :inactive-value="1" active-text="正常" inactive-text="停用" />
                </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="formDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="viewDialogVisible" title="详情" width="800px" destroy-on-close>
      <el-descriptions :column="2" border>
            <el-descriptions-item label="菜单id">{{ currentRow?.menuId }}</el-descriptions-item>
            <el-descriptions-item label="菜单名称">{{ currentRow?.menuName }}</el-descriptions-item>
            <el-descriptions-item label="权限code">{{ currentRow?.perCode }}</el-descriptions-item>
            <el-descriptions-item label="菜单类型">
              <el-tag v-if="currentRow?.menuType === 'M'" type="primary">目录</el-tag>
              <el-tag v-else-if="currentRow?.menuType === 'C'" type="success">菜单</el-tag>
              <el-tag v-else-if="currentRow?.menuType === 'F'" type="warning">按钮</el-tag>
              <el-tag v-else>{{ currentRow?.menuType }}</el-tag>
            </el-descriptions-item>
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
  import {Plus, Delete, Refresh} from '@element-plus/icons-vue'
  import { ElMessage, ElMessageBox } from 'element-plus'
  import { deleteSysMenu, createSysMenu, updateSysMenu, listSysMenuTree} from '@/api/sysMenuApi'
  import type { SysMenu } from '@/api/sysMenuApi'

  const loading = ref(false)
  const dataList = ref<SysMenu[]>([])
  const formDialogVisible = ref(false)
  const viewDialogVisible = ref(false)
  const dialogTitle = ref('')
  const currentRow = ref<SysMenu>()
  const selectedRow = ref<SysMenu>()
  const single = ref(true)
  const formRef = ref()
  const form = reactive<Partial<SysMenu>>({})
  const rules = reactive<Record<string, any[]>>({})

  const getList = async () => {
    loading.value = true
    try {
      const res = await listSysMenuTree()
      dataList.value = res.data
    } finally {
      loading.value = false
    }
  }

  const handleAdd = () => {
    dialogTitle.value = '新增菜单权限表'
    currentRow.value = undefined
    Object.keys(form).forEach(key => {
      (form as any)[key] = undefined
    })
    formDialogVisible.value = true
  }

  const handleEdit = (row: SysMenu) => {
    dialogTitle.value = '编辑菜单权限表'
    currentRow.value = row
    Object.assign(form, row)
    formDialogVisible.value = true
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

  const handleRefresh = async () => {
    getList()
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

  const handleSubmit = async () => {
    const valid = await formRef.value?.validate()
    if (!valid) return

    try {
      const data = { ...form }
      if (data.menuId) {
        await updateSysMenu(data)
        ElMessage.success('修改成功')
      } else {
        await createSysMenu(data)
        ElMessage.success('新增成功')
      }
      formDialogVisible.value = false
      await getList()
    } catch {}
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
