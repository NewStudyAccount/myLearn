<template>
  <div class="generator-config">
    <el-alert
        title="提示"
        type="info"
        :closable="false"
        show-icon
        class="mb-4"
    >
      <template #default>
        请配置代码生成参数，生成的代码将包含完整的后端和前端文件
      </template>
    </el-alert>

    <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
    >
      <el-form-item label="表名">
        <el-input :model-value="tableName" disabled />
      </el-form-item>

      <el-form-item label="包名" prop="packageName">
        <el-input
            v-model="form.packageName"
            placeholder="例如：com.example"
        />
        <div class="form-tip">Java 代码的包路径</div>
      </el-form-item>

      <el-form-item label="表前缀" prop="tablePrefix">
        <el-input
            v-model="form.tablePrefix"
            placeholder="例如：sys_"
            clearable
        />
        <div class="form-tip">生成的类名会移除此前缀，如 sys_user → User</div>
      </el-form-item>

      <el-divider content-position="left">生成选项</el-divider>

      <el-form-item label="后端代码">
        <el-checkbox-group v-model="form.backendOptions">
          <el-checkbox label="domain">实体类</el-checkbox>
          <el-checkbox label="mapper">Mapper</el-checkbox>
          <el-checkbox label="service">Service</el-checkbox>
          <el-checkbox label="controller">Controller</el-checkbox>
        </el-checkbox-group>
      </el-form-item>

      <el-form-item label="前端代码">
        <el-checkbox-group v-model="form.frontendOptions">
          <el-checkbox label="api">API 接口</el-checkbox>
          <el-checkbox label="vue">Vue 页面</el-checkbox>
          <el-checkbox label="router">路由配置</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
    </el-form>

    <div class="config-actions">
      <el-button @click="handleCancel">取消</el-button>
      <el-button type="primary" @click="handlePreview" :loading="previewLoading">
        <el-icon><View /></el-icon>
        预览代码
      </el-button>
      <el-button type="success" @click="handleDownload" :loading="downloadLoading">
        <el-icon><Download /></el-icon>
        下载代码
      </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { View, Download } from '@element-plus/icons-vue'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage } from 'element-plus'

const props = defineProps<{
  tableName: string
}>()

const emit = defineEmits<{
  generate: [params: any]
  download: [params: any]
  cancel: []
}>()

const formRef = ref<FormInstance>()
const previewLoading = ref(false)
const downloadLoading = ref(false)

const form = reactive({
  packageName: 'com.example',
  tablePrefix: '',
  backendOptions: ['domain', 'mapper', 'service', 'controller'],
  frontendOptions: ['api', 'vue', 'router'],
})

const rules = reactive<FormRules>({
  packageName: [
    { required: true, message: '请输入包名', trigger: 'blur' },
    { pattern: /^[a-z][a-z0-9]*(\.[a-z0-9]+)*$/, message: '包名格式不正确', trigger: 'blur' },
  ],
})

const validateForm = async (): Promise<boolean> => {
  if (!formRef.value) return false
  try {
    await formRef.value.validate()
    return true
  } catch {
    return false
  }
}

const buildParams = () => ({
  tableName: props.tableName,
  packageName: form.packageName,
  tablePrefix: form.tablePrefix,
  backendOptions: form.backendOptions,
  frontendOptions: form.frontendOptions,
})

const handlePreview = async () => {
  const valid = await validateForm()
  if (!valid) return

  previewLoading.value = true
  try {
    emit('generate', buildParams())
  } finally {
    previewLoading.value = false
  }
}

const handleDownload = async () => {
  const valid = await validateForm()
  if (!valid) return

  downloadLoading.value = true
  try {
    emit('download', buildParams())
  } finally {
    downloadLoading.value = false
  }
}

const handleCancel = () => {
  emit('cancel')
}
</script>

<style scoped lang="scss">
.generator-config {
  padding: 10px;
}

.mb-4 {
  margin-bottom: 16px;
}

.config-actions {
  margin-top: 24px;
  padding-top: 20px;
  border-top: 1px solid #ebeef5;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.form-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
  line-height: 1.5;
}
</style>
