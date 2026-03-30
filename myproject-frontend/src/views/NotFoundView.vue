<script setup lang="ts">
</script>

<template>
  <div class="not-found">
    <h1>404</h1>
    <p>页面未找到</p>
    <router-link to="/">
      <el-button type="primary">返回首页</el-button>
    </router-link>
  </div>
</template>

<style scoped>
.not-found {
  padding: 40px;
  text-align: center;
}

.not-found h1 {
  font-size: 72px;
  color: #909399;
}
</style>
