<script setup lang="ts">import { computed } from 'vue'
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
    return []
  }

  return menus.map((menu) => ({
    path: menu.path,
    title: menu.menuName,
    icon: iconMap[menu.menuName] || iconMap[menu.path] || Folder,
    children: menu.children
        ? menu.children.map((child) => ({
          path: `${child.path}`,
          title: child.menuName,
          icon: iconMap[child.menuName] || iconMap[child.path] || Document,
        }))
        : [],
  }))
})

const handleSelect = (path: string) => {
  router.push(path)
}
const handleLogoClick = () => {
  router.push('/')
  window.location.reload()
}
</script>

<template>
  <div class="side-menu">
    <div class="logo" @click="handleLogoClick" style="cursor: pointer;">
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
      <template v-for="item in dynamicMenus" :key="item.path">
        <!-- 有子菜单的情况 -->
        <el-sub-menu v-if="item.children && item.children.length > 0" :index="item.path">
          <template #title>
            <el-icon><component :is="item.icon" /></el-icon>
            <span>{{ item.title }}</span>
          </template>
          <el-menu-item
              v-for="child in item.children"
              :key="child.path"
              :index="child.path"
          >
            <el-icon><component :is="child.icon" /></el-icon>
            <template #title>{{ child.title }}</template>
          </el-menu-item>
        </el-sub-menu>

        <!-- 没有子菜单的情况 -->
        <el-menu-item v-else :index="item.path">
          <el-icon><component :is="item.icon" /></el-icon>
          <template #title>{{ item.title }}</template>
        </el-menu-item>
      </template>
    </el-menu>
  </div>
</template>
