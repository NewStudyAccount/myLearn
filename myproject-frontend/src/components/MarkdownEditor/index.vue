<template>
  <MdEditor v-model="content" :preview="false" :toolbarsExclude="['github']" :onUploadImg="handleUploadImg" />
</template>

<script setup lang="ts">
import { MdEditor } from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'
import { ElMessage } from 'element-plus'
import { uploadSysOssFile } from '@/api/oss/sysOssFileApi'

const content = defineModel<string>({ default: '' })

const handleUploadImg = async (files: File[], callBack: (urls: string[]) => void) => {
  const urls: string[] = []
  for (const file of files) {
    try {
      const res = await uploadSysOssFile(file)
      const url = res.data?.url || res.url || (typeof res === 'string' ? res : '')
      if (url) {
        urls.push(url)
      } else {
        ElMessage.error(`图片 ${file.name} 上传失败：未返回地址`)
      }
    } catch {
      ElMessage.error(`图片 ${file.name} 上传失败`)
    }
  }
  callBack(urls)
}
</script>

<style scoped lang="scss">
:deep(.md-editor) {
  height: 500px;
}
</style>
