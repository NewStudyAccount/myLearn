<script setup lang="ts">
import { ref } from 'vue'
import HeaderNav from './HeaderNav.vue'
import SideMenu from './SideMenu.vue'

const isCollapse = ref(false)

const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}
</script>

<template>
  <el-container class="main-layout">
    <el-aside :width="isCollapse ? '64px' : '200px'" class="layout-aside">
      <SideMenu :is-collapse="isCollapse" />
    </el-aside>
    <el-container>
      <el-header class="layout-header">
        <HeaderNav :is-collapse="isCollapse" @toggle-collapse="toggleCollapse" />
      </el-header>
      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.main-layout {
  min-height: 100vh;
}

.layout-aside {
  background-color: #304156;
  transition: width 0.3s;
  overflow: hidden;
}

.layout-header {
  background-color: #fff;
  border-bottom: 1px solid #e6e6e6;
  padding: 0 20px;
  display: flex;
  align-items: center;
}

.layout-main {
  background-color: #f0f2f5;
  padding: 20px;
}
</style>
