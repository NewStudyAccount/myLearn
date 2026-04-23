<template>
  <el-dialog
      v-model="dialogVisible"
      :title="title"
      width="500px"
      destroy-on-close
      @close="handleClose"
  >
    <el-upload
        ref="uploadRef"
        drag
        :auto-upload="false"
        :limit="limit"
        :accept="accept"
        :on-change="handleChange"
        :on-remove="handleRemove"
        :file-list="fileList"
    >
      <el-icon class="el-icon--upload"><upload-filled /></el-icon>
      <div class="el-upload__text">
        将文件拖到此处，或<em>点击上传</em>
      </div>
      <template #tip>
        <div class="el-upload__tip">
          {{ tip }}
        </div>
      </template>
    </el-upload>
    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" :loading="loading" @click="handleSubmit">
        确定上传
      </el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">import { ref, watch } from 'vue'
import { UploadFilled } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import type { UploadInstance, UploadUserFile } from 'element-plus'

interface Props {
  modelValue: boolean
  title?: string
  limit?: number
  accept?: string
  tip?: string
}

const props = withDefaults(defineProps<Props>(), {
  modelValue: false,
  title: '上传文件',
  limit: 1,
  accept: '*/*',
  tip: '支持任意格式文件上传'
})

const emit = defineEmits<{
  'update:modelValue': [value: boolean]
  success: [data: any]
}>()

const dialogVisible = ref(false)
const loading = ref(false)
const uploadRef = ref<UploadInstance>()
const fileList = ref<UploadUserFile[]>([])
const selectedFiles = ref<File[]>([])

watch(() => props.modelValue, (val) => {
  dialogVisible.value = val
})

watch(dialogVisible, (val) => {
  emit('update:modelValue', val)
})

const handleChange = (file: any) => {
  if (file.raw) {
    selectedFiles.value.push(file.raw)
  }
}

const handleRemove = (file: any) => {
  const index = selectedFiles.value.findIndex(f => f.name === file.name)
  if (index > -1) {
    selectedFiles.value.splice(index, 1)
  }
}

const handleSubmit = async () => {
  if (selectedFiles.value.length === 0) {
    ElMessage.warning('请选择要上传的文件')
    return
  }

  loading.value = true
  try {
    emit('success', selectedFiles.value)
  } catch (error) {
    ElMessage.error('上传失败')
  } finally {
    loading.value = false
  }
}

const handleClose = () => {
  selectedFiles.value = []
  fileList.value = []
  uploadRef.value?.clearFiles()
}
</script>

<style scoped lang="scss">.el-upload__tip {
  color: #999;
  font-size: 12px;
  margin-top: 8px;
}
</style>