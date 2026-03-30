<script setup lang="ts">
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
</script>

<template>
  <div class="dashboard">
    <el-card class="welcome-card">
      <template #header>
        <span>欢迎回来</span>
      </template>
      <p>你好，{{ userStore.userInfo?.user?.username || '用户' }}！</p>
      <p>这是 MyProject 管理系统首页</p>
    </el-card>

    <el-row :gutter="20">
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header>
            <span>用户统计</span>
          </template>
          <div class="stat-value">1,234</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header>
            <span>订单统计</span>
          </template>
          <div class="stat-value">5,678</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header>
            <span>收入统计</span>
          </template>
          <div class="stat-value">¥98,765</div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.dashboard {
  padding: 20px;
}

.welcome-card {
  margin-bottom: 20px;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #409eff;
  text-align: center;
}
</style>
