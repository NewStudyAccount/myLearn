<template>
  <div class="code-preview">
    <el-tabs v-model="activeTab" type="border-card">
      <el-tab-pane
        v-for="(code, fileName) in codeMap"
        :key="fileName"
        :label="fileName"
        :name="fileName"
      >
        <pre class="code-content"><code>{{ code }}</code></pre>
      </el-tab-pane>
    </el-tabs>

    <div class="preview-actions">
      <el-button type="primary" @click="handleCopyAll">
        <el-icon><CopyDocument /></el-icon>
        复制当前文件
      </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { CopyDocument } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const props = defineProps<{
  codeMap: Record<string, string>
}>()

const activeTab = ref('')

watch(
  () => props.codeMap,
  (newVal) => {
    if (newVal && Object.keys(newVal).length > 0) {
      activeTab.value = Object.keys(newVal)[0]
    }
  },
  { immediate: true }
)

const handleCopyAll = async () => {
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
</script>

<style scoped lang="scss">
.code-preview {
  height: 100%;
  display: flex;
  flex-direction: column;
}
.code-content {
  background: #1e1e1e;
  color: #d4d4d4;
  padding: 16px;
  border-radius: 4px;
  overflow-x: auto;
  font-family: 'Consolas', 'Monaco', monospace;
  font-size: 13px;
  line-height: 1.5;
  white-space: pre-wrap;
  word-break: break-all;
  max-height: calc(100vh - 200px);
}
.preview-actions {
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid #ebeef5;
}
</style>
