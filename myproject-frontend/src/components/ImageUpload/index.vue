<template>
  <div class="image-upload-container">
    <el-upload
      ref="uploadRef"
      :action="uploadAction"
      :headers="uploadHeaders"
      :show-file-list="false"
      :before-upload="beforeUpload"
      :on-success="handleSuccess"
      :on-error="handleError"
      :accept="accept"
      :multiple="multiple"
      :limit="limit"
      :on-exceed="handleExceed"
      list-type="picture-card"
    >
      <el-icon><Plus /></el-icon>
    </el-upload>

    <!-- 图片列表 -->
    <div v-if="imageList.length > 0" class="image-list">
      <div
        v-for="(image, index) in imageList"
        :key="index"
        class="image-item"
      >
        <el-image
          :src="image.url"
          fit="cover"
          class="image-preview"
          :preview-src-list="imageList.map(img => img.url)"
          :initial-index="index"
          preview-teleported
        />
        <div class="image-actions">
          <el-icon class="action-icon" @click="handlePreview(index)">
            <ZoomIn />
          </el-icon>
          <el-icon class="action-icon" @click="handleRemove(index)">
            <Delete />
          </el-icon>
        </div>
      </div>
    </div>

    <!-- 提示文字 -->
    <div v-if="tip" class="upload-tip">{{ tip }}</div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { Plus, ZoomIn, Delete } from '@element-plus/icons-vue'
import { ElMessage, type UploadProps } from 'element-plus'

interface ImageItem {
  url: string
  file?: File
  uid?: number
}

interface Props {
  modelValue?: string | string[]
  limit?: number
  accept?: string
  maxSize?: number
  tip?: string
  multiple?: boolean
  uploadUrl?: string
  headers?: Record<string, any>
}

const props = withDefaults(defineProps<Props>(), {
  modelValue: '',
  limit: 9,
  accept: 'image/*',
  maxSize: 5,
  tip: '支持 jpg、png、gif 格式，单个文件不超过 5MB',
  multiple: false,
  uploadUrl: '/sysOssFile/upload',
  headers: () => ({})
})

const emit = defineEmits<{
  'update:modelValue': [value: string | string[]]
  change: [value: string | string[]]
}>()

const uploadRef = ref()
const imageList = ref<ImageItem[]>([])
const uploadAction = ref(props.uploadUrl)
const uploadHeaders = ref(props.headers)

// 初始化图片列表
watch(() => props.modelValue, (val) => {
  if (!val) {
    imageList.value = []
    return
  }
  
  const urls = Array.isArray(val) ? val : [val]
  imageList.value = urls.filter(url => url).map(url => ({ url }))
}, { immediate: true })

// 上传前验证
const beforeUpload: UploadProps['beforeUpload'] = (file) => {
  // 验证文件类型
  const isImage = file.type.startsWith('image/')
  if (!isImage) {
    ElMessage.error('只能上传图片文件！')
    return false
  }

  // 验证文件大小
  const isLtMaxSize = file.size / 1024 / 1024 < props.maxSize
  if (!isLtMaxSize) {
    ElMessage.error(`图片大小不能超过 ${props.maxSize}MB！`)
    return false
  }

  // 验证数量限制
  if (!props.multiple && imageList.value.length >= 1) {
    ElMessage.warning('只能上传一张图片')
    return false
  }

  if (props.multiple && imageList.value.length >= props.limit) {
    ElMessage.warning(`最多只能上传 ${props.limit} 张图片`)
    return false
  }

  return true
}

// 上传成功
const handleSuccess: UploadProps['onSuccess'] = (response, file) => {
  console.log('上传成功:', response)
  
  // 根据后端返回的数据结构获取图片URL
  let imageUrl = ''
  if (response.data?.url) {
    imageUrl = response.data.url
  } else if (response.url) {
    imageUrl = response.url
  } else if (typeof response === 'string') {
    imageUrl = response
  }

  if (imageUrl) {
    imageList.value.push({
      url: imageUrl,
      file: file.raw,
      uid: file.uid
    })
    
    updateModelValue()
    ElMessage.success('上传成功')
  } else {
    ElMessage.error('上传失败，未返回图片地址')
  }
}

// 上传失败
const handleError: UploadProps['onError'] = (error) => {
  console.error('上传失败:', error)
  ElMessage.error('上传失败，请重试')
}

// 超出限制
const handleExceed = () => {
  ElMessage.warning(`最多只能上传 ${props.limit} 张图片`)
}

// 预览图片
const handlePreview = (index: number) => {
  // el-image 的 preview-src-list 会自动处理预览
}

// 删除图片
const handleRemove = (index: number) => {
  imageList.value.splice(index, 1)
  updateModelValue()
}

// 更新模型值
const updateModelValue = () => {
  const urls = imageList.value.map(img => img.url)
  const value = props.multiple ? urls : (urls[0] || '')
  
  emit('update:modelValue', value)
  emit('change', value)
}

// 暴露方法供父组件调用
defineExpose({
  clearFiles: () => {
    imageList.value = []
    updateModelValue()
    uploadRef.value?.clearFiles()
  },
  getImageList: () => imageList.value
})
</script>

<style scoped lang="scss">
.image-upload-container {
  .image-list {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
    margin-top: 8px;

    .image-item {
      position: relative;
      width: 100px;
      height: 100px;
      border: 1px solid #dcdfe6;
      border-radius: 4px;
      overflow: hidden;
      cursor: pointer;
      transition: all 0.3s;

      &:hover {
        .image-actions {
          opacity: 1;
        }
      }

      .image-preview {
        width: 100%;
        height: 100%;
      }

      .image-actions {
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 8px;
        background-color: rgba(0, 0, 0, 0.5);
        opacity: 0;
        transition: opacity 0.3s;

        .action-icon {
          color: #fff;
          font-size: 18px;
          cursor: pointer;
          padding: 4px;
          border-radius: 4px;
          transition: background-color 0.3s;

          &:hover {
            background-color: rgba(255, 255, 255, 0.2);
          }
        }
      }
    }
  }

  .upload-tip {
    color: #999;
    font-size: 12px;
    margin-top: 8px;
    line-height: 1.5;
  }

  :deep(.el-upload--picture-card) {
    width: 100px;
    height: 100px;
    line-height: 100px;
  }
}
</style>
