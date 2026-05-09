<template>
  <header class="article-nav-bar">
    <div class="nav-left">
      <el-button class="back-btn" @click="$emit('back')" :icon="ArrowLeft" text>返回</el-button>
      <el-divider direction="vertical" />
      <router-link to="/" class="site-brand">MyProject</router-link>
    </div>
    <div class="nav-center">
      <span v-if="title" class="article-title">{{ title }}</span>
      <span v-else class="article-title-placeholder">文档</span>
    </div>
    <div class="nav-right"></div>
  </header>
</template>

<script setup lang="ts">
import { ArrowLeft } from '@element-plus/icons-vue'

defineProps<{
  title?: string
}>()

defineEmits<{
  back: []
}>()
</script>

<style scoped lang="scss">
.article-nav-bar {
  position: sticky;
  top: 0;
  z-index: 100;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 56px;
  padding: 0 24px;
  background: #fff;
  border-bottom: 1px solid #e6e6e6;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.04);
}

.nav-left {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-shrink: 0;
}

.back-btn {
  color: #606266;
  font-size: 14px;
}

.site-brand {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  text-decoration: none;

  &:hover {
    color: #409eff;
  }
}

.nav-center {
  flex: 1;
  text-align: center;
  overflow: hidden;
}

.article-title {
  font-size: 15px;
  font-weight: 500;
  color: #303133;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  display: inline-block;
  max-width: 100%;
}

.article-title-placeholder {
  font-size: 15px;
  color: #c0c4cc;
}

.nav-right {
  width: 80px;
  flex-shrink: 0;
}
</style>
