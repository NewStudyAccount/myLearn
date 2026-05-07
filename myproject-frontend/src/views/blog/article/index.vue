<template>
  <div class="sysArticle-container">
    <el-card class="search-card">
      <el-form :model="queryParams" ref="queryFormRef" label-width="100px">
        <el-row :gutter="20">
                  <el-col :span="8">
                    <el-form-item label="文章名" prop="title">
                      <el-input v-model="queryParams.title" placeholder="请输入文章名" clearable @keyup.enter="handleQuery" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="预览图" prop="cover">
                      <el-input v-model="queryParams.cover" placeholder="请输入预览图" clearable @keyup.enter="handleQuery" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="删除标志" prop="isDeleted">
                      <el-input v-model="queryParams.isDeleted" placeholder="请输入删除标志" clearable @keyup.enter="handleQuery" />
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
            <el-table-column label="文章id" align="center" prop="id" />
            <el-table-column label="文章名" align="center" prop="title" />
            <el-table-column label="预览图" align="center" prop="cover" />
            <el-table-column label="删除标志" align="center" prop="isDeleted" />
            <el-table-column label="阅读次数" align="center" prop="readNum" />
        <el-table-column label="操作" width="300" align="center">
          <template #default="{ row }">
            <el-button type="text" @click.stop="handleView(row)">查看</el-button>
            <el-button type="text" @click.stop="handleEdit(row)">编辑</el-button>
            <el-button type="text" @click.stop="handleEditContent(row)">编辑内容</el-button>
            <el-button type="text" @click.stop="handleViewDoc(row)">查看文档</el-button>
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
                <el-form-item label="文章名" prop="title">
                      <el-input v-model="form.title" placeholder="请输入文章名" />
                </el-form-item>
                <el-form-item label="预览图" prop="cover">
                      <el-input v-model="form.cover" placeholder="请输入预览图" />
                </el-form-item>
                <el-form-item label="删除标志" prop="isDeleted">
                      <el-input v-model="form.isDeleted" placeholder="请输入删除标志" />
                </el-form-item>
                <el-form-item label="阅读次数" prop="readNum">
                      <el-input v-model="form.readNum" placeholder="请输入阅读次数" />
                </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="formDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="viewDialogVisible" title="详情" width="800px" destroy-on-close>
      <el-descriptions :column="2" border>
            <el-descriptions-item label="文章id">{{ currentRow?.id }}</el-descriptions-item>
            <el-descriptions-item label="文章名">{{ currentRow?.title }}</el-descriptions-item>
            <el-descriptions-item label="预览图">{{ currentRow?.cover }}</el-descriptions-item>
            <el-descriptions-item label="删除标志">{{ currentRow?.isDeleted }}</el-descriptions-item>
            <el-descriptions-item label="阅读次数">{{ currentRow?.readNum }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <el-dialog v-model="contentDialogVisible" title="编辑文章内容" fullscreen destroy-on-close>
      <MarkdownEditor v-model="contentForm.content" />
      <template #footer>
        <el-button @click="contentDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="contentSaving" @click="handleContentSubmit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
  import { ref, reactive, onMounted, defineAsyncComponent } from 'vue'
  import { useRouter } from 'vue-router'
  import { Search, Refresh, Plus, Delete } from '@element-plus/icons-vue'
  import { ElMessage, ElMessageBox } from 'element-plus'
  import { listSysArticle, getSysArticleById,deleteSysArticle, addSysArticle, updateSysArticle } from '@/api/blog/sysArticleApi'
  import type { SysArticle } from '@/api/blog/sysArticleApi'
  import { getSysArticleContentByArticleId, addSysArticleContent, updateSysArticleContent } from '@/api/blog/sysArticleContentApi'
  import type { SysArticleContent } from '@/api/blog/sysArticleContentApi'

  const MarkdownEditor = defineAsyncComponent(() => import('@/components/MarkdownEditor/index.vue'))

  const router = useRouter()

  const loading = ref(false)
  const dataList = ref<SysArticle[]>([])
  const total = ref(0)
  const queryParams = reactive({
    pageQuery: {
      pageNum: 1,
      pageSize: 10
    },
                  title: undefined,
                  cover: undefined,
                  isDeleted: undefined,
  })
  const queryFormRef = ref()
  const formDialogVisible = ref(false)
  const viewDialogVisible = ref(false)
  const dialogTitle = ref('')
  const currentRow = ref<SysArticle>()
  const selectedRow = ref<SysArticle>()
  const single = ref(true)
  const formRef = ref()
  const form = reactive<Partial<SysArticle>>({})
  const rules = reactive<Record<string, any[]>>({})

  const getList = async () => {
    loading.value = true
    try {
      const res = await listSysArticle(queryParams)
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
    dialogTitle.value = '新增文章表'
    currentRow.value = undefined
    resetForm()
    formDialogVisible.value = true
  }

  const handleEdit = async(row: SysArticle) => {
    dialogTitle.value = '编辑文章表'
    currentRow.value = row
    try {
      // 调用接口获取最新数据
      const res = await getSysArticleById(row.id)
      Object.assign(form, res.data)
    } catch (error) {
      ElMessage.error('获取数据失败')
      return
    }
    formDialogVisible.value = true
  }

  const handleView = async (row: SysArticle) => {
    try {
      // 调用接口获取最新数据用于查看
      const res = await getSysArticleById(row.id)
      currentRow.value = res.data
      viewDialogVisible.value = true
    } catch (error) {
      ElMessage.error('获取数据失败')
    }
  }

  const handleDelete = async (row?: SysArticle) => {
    if (!row) return
    try {
      await ElMessageBox.confirm('是否确认删除选中的数据?', '警告', { type: 'warning' })
      await deleteSysArticle(row.id)
      ElMessage.success('删除成功')
      await getList()
    } catch {}
  }

  const handleSelectionChange = (selection: SysArticle[]) => {
    single.value = selection.length !== 1
    if (selection.length === 1) {
      selectedRow.value = selection[0]
    }
  }

  const rowClick = (row: SysArticle) => {
    currentRow.value = row
  }

  const handleSubmit = async () => {
    const valid = await formRef.value?.validate()
    if (!valid) return

    try {
      const data = { ...form }
      if (data.id) {
        await updateSysArticle(data)
        ElMessage.success('修改成功')
      } else {
        await addSysArticle(data)
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

  // 内容编辑相关
  const contentDialogVisible = ref(false)
  const contentSaving = ref(false)
  const contentForm = reactive<Partial<SysArticleContent>>({})

  const handleEditContent = async (row: SysArticle) => {
    Object.keys(contentForm).forEach(key => {
      (contentForm as any)[key] = undefined
    })
    contentForm.articleId = row.id
    try {
      const res = await getSysArticleContentByArticleId(row.id)
      if (res.data) {
        Object.assign(contentForm, res.data)
      }
    } catch {
      // 文章无内容记录，显示空编辑器
    }
    contentDialogVisible.value = true
  }

  const handleViewDoc = (row: SysArticle) => {

    console.log("查看文章内容", row.id)

    router.push(`/blog/doc/${row.id}`)
  }

  const handleContentSubmit = async () => {
    contentSaving.value = true


    console.log("保存文章内容", JSON.stringify(contentForm))


    try {
      if (contentForm.articleId) {
        await updateSysArticleContent(contentForm)
        ElMessage.success('内容保存成功')
      } else {
        await addSysArticleContent(contentForm)
        ElMessage.success('内容新增成功')
      }
      contentDialogVisible.value = false
    } catch {
      ElMessage.error('内容保存失败')
    } finally {
      contentSaving.value = false
    }
  }
  onMounted(() => {
    getList()
  })
</script>

<style scoped lang="scss">
    .sysArticle-container {
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
