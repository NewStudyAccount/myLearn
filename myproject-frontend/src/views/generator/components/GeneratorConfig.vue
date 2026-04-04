<template>
  <div class="generator-config">
    <el-form :model="form" label-width="100px">
      <el-form-item label="表名">
        <el-input :model-value="tableName" disabled />
      </el-form-item>
      <el-form-item label="包名">
        <el-input v-model="form.packageName" placeholder="例如：com.example" />
      </el-form-item>
      <el-form-item label="表前缀">
        <el-input v-model="form.tablePrefix" placeholder="例如：sys_" />
        <div class="form-tip">生成的类名会移除此前缀</div>
      </el-form-item>
    </el-form>

    <div class="config-actions">
      <el-button type="primary" @click="handlePreview">
        <el-icon><View /></el-icon>
        预览代码
      </el-button>
      <el-button type="success" @click="handleDownload">
        <el-icon><Download /></el-icon>
        下载代码
      </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { View, Download } from '@element-plus/icons-vue'

const props = defineProps<{
  tableName: string
}>()

const emit = defineEmits<{
  generate: [params: any]
  download: [params: any]
}>()

const form = reactive({
  packageName: 'com.example',
  tablePrefix: '',
})

const handlePreview = () => {
  emit('generate', {
    tableName: props.tableName,
    packageName: form.packageName,
    tablePrefix: form.tablePrefix,
  })
}

const handleDownload = () => {
  emit('download', {
    tableName: props.tableName,
    packageName: form.packageName,
    tablePrefix: form.tablePrefix,
  })
}
</script>

<style scoped lang="scss">
.generator-config {
  padding: 10px;
}
.config-actions {
  margin-top: 20px;
  display: flex;
  gap: 10px;
}
.form-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}
</style>
