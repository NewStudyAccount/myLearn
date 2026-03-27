<script setup lang="ts">
import { useCounterStore } from '@/stores/counter'

const counter = useCounterStore()
</script>

<template>
  <div class="home">
    <h1>欢迎使用 MyProject</h1>
    <p>这是一个基于 Vue 3 + Element Plus 的前端项目</p>

    <el-card class="counter-card">
      <template #header>
        <span>Pinia 计数器示例</span>
      </template>
      <p>当前计数: {{ counter.count }}</p>
      <p>双倍计数: {{ counter.doubleCount }}</p>
      <el-button type="primary" @click="counter.increment">增加</el-button>
      <el-button @click="counter.decrement">减少</el-button>
    </el-card>

    <div class="links">
      <router-link to="/about">
        <el-button>关于我们</el-button>
      </router-link>
    </div>
  </div>
</template>

<style scoped>
.home {
  padding: 20px;
  text-align: center;
}

.counter-card {
  max-width: 400px;
  margin: 20px auto;
}

.links {
  margin-top: 20px;
}
</style>
