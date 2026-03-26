<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router'
import { HomeFilled, Document, Setting } from '@element-plus/icons-vue'

defineProps<{
  isCollapse: boolean
}>()

const route = useRoute()
const router = useRouter()

const menuItems = [
  {
    path: '/dashboard',
    title: '首页',
    icon: HomeFilled,
  },
  {
    path: '/about',
    title: '关于',
    icon: Document,
  },
  {
    path: '/settings',
    title: '设置',
    icon: Setting,
  },
]

const handleSelect = (path: string) => {
  router.push(path)
}
</script>

<template>
  <div class="side-menu">
    <div class="logo">
      <h3 v-if="!isCollapse">MyProject</h3>
      <h3 v-else>MP</h3>
    </div>

    <el-menu
      :default-active="route.path"
      :collapse="isCollapse"
      background-color="#304156"
      text-color="#bfcbd9"
      active-text-color="#409eff"
      @select="handleSelect"
    >
      <el-menu-item
        v-for="item in menuItems"
        :key="item.path"
        :index="item.path"
      >
        <el-icon><component :is="item.icon" /></el-icon>
        <template #title>{{ item.title }}</template>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<style scoped>
.side-menu {
  height: 100%;
}

.logo {
  height: 60px;
  display: flex;
  justify-content: center;
  align-items: center;
  border-bottom: 1px solid #3a4a5a;
}

.logo h3 {
  margin: 0;
  color: #fff;
  font-size: 18px;
}

.el-menu {
  border-right: none;
}
</style>
