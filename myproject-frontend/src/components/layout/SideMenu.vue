<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { usePermissionStore } from '@/stores/permission'
import { HomeFilled, Folder, Document, Setting } from '@element-plus/icons-vue'

defineProps<{
  isCollapse: boolean
}>()

const route = useRoute()
const router = useRouter()
const permissionStore = usePermissionStore()

const iconMap: Record<string, any> = {
  '系统管理': Setting,
  '文章管理': Document,
  '/system': Setting,
  '/blog': Document,
}

interface MenuDisplayItem {
  path: string
  title: string
  icon: any
  children?: { path: string; title: string }[]
}

const dynamicMenus = computed<MenuDisplayItem[]>(() => {
  const menus = permissionStore.menuList
  if (menus.length === 0) {
    return [
      {
        path: '/dashboard',
        title: '首页',
        icon: HomeFilled,
      },
    ]
  }
  return menus.map((menu) => ({
    path: menu.path,
    title: menu.menuName,
    icon: iconMap[menu.menuName] || iconMap[menu.path] || Folder,
    children: menu.children?.map((child) => ({
      path: `${menu.path}/${child.path}`,
      title: child.menuName,
    })),
  }))
})

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
        v-for="item in dynamicMenus"
        :key="item.path"
        :index="item.children?.[0]?.path || item.path"
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
