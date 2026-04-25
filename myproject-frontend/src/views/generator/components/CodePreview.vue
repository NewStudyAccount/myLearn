<template>
  <div class="code-preview">
    <div class="preview-header">
      <el-select
          v-model="activeTab"
          placeholder="选择文件"
          class="file-selector"
      >
        <el-option
            v-for="(code, fileName) in codeMap"
            :key="fileName"
            :label="fileName"
            :value="fileName"
        >
          <div class="file-option">
            <el-icon><component :is="getFileIcon(fileName)" /></el-icon>
            <span>{{ fileName }}</span>
          </div>
        </el-option>
      </el-select>

      <div class="header-actions">
        <el-button size="small" @click="handleCopy">
          <el-icon><CopyDocument /></el-icon>
          复制
        </el-button>
        <el-button size="small" type="primary" @click="handleDownloadFile">
          <el-icon><Download /></el-icon>
          下载此文件
        </el-button>
      </div>
    </div>

    <div class="code-content-wrapper">
      <pre class="code-content"><code v-html="highlightedCode"></code></pre>
    </div>

    <div class="preview-footer">
      <el-text size="small" type="info">
        当前文件：{{ activeTab }} | 行数：{{ lineCount }}
      </el-text>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { CopyDocument, Download, Document, Files, DataLine } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const props = defineProps<{
  codeMap: Record<string, string>
}>()

const activeTab = ref('')

const highlightedCode = computed(() => {
  const code = props.codeMap[activeTab.value] || ''
  return escapeHtml(code)
})

const lineCount = computed(() => {
  const code = props.codeMap[activeTab.value] || ''
  return code.split('\n').length
})

watch(
    () => props.codeMap,
    (newVal) => {
      if (newVal && Object.keys(newVal).length > 0) {
        const keys = Object.keys(newVal)
        activeTab.value = keys.find(k => k.endsWith('.java')) ||
            keys.find(k => k.endsWith('.vue')) ||
            keys.find(k => k.endsWith('.ts')) ||
            keys[0]
      }
    },
    { immediate: true }
)

const getFileIcon = (fileName: string) => {
  if (fileName.endsWith('.java')) return Document
  if (fileName.endsWith('.vue')) return Files
  if (fileName.endsWith('.ts') || fileName.endsWith('.js')) return DataLine
  return Document
}

const escapeHtml = (code: string) => {
  return code
      .replace(/&/g, '&amp;')
      .replace(/</g, '&lt;')
      .replace(/>/g, '&gt;')
      .replace(/"/g, '&quot;')
      .replace(/'/g, '&#039;')
}

const handleCopy = async () => {
  const code = props.codeMap[activeTab.value]
  if (code) {
    try {
      await navigator.clipboard.writeText(code)
      ElMessage.success('复制成功')
    } catch {
      ElMessage.error('复制失败')
    }
  }
}

const handleDownloadFile = () => {
  const code = props.codeMap[activeTab.value]
  if (code) {
    const blob = new Blob([code], { type: 'text/plain' })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = activeTab.value.split('/').pop() || 'code.txt'
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
    ElMessage.success('下载成功')
  }
}
</script>

<style scoped lang="scss">
.code-preview {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.preview-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #ebeef5;
  margin-bottom: 12px;
}

.file-selector {
  width: 400px;
}

.file-option {
  display: flex;
  align-items: center;
  gap: 8px;
}

.header-actions {
  display: flex;
  gap: 8px;
}

.code-content-wrapper {
  flex: 1;
  overflow: auto;
}

.code-content {
  background: #1e1e1e;
  color: #d4d4d4;
  padding: 16px;
  border-radius: 4px;
  overflow-x: auto;
  font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
  font-size: 13px;
  line-height: 1.6;
  white-space: pre-wrap;
  word-break: break-all;
  margin: 0;
  min-height: 400px;
}

.preview-footer {
  padding: 12px 0;
  border-top: 1px solid #ebeef5;
  margin-top: 12px;
}
</style>
