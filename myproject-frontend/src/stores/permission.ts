import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { RouteRecordRaw } from 'vue-router'
import { getMenuTree, type MenuItem } from '@/api/sysMenuApi.ts'
import MainLayout from '@/components/layout/MainLayout.vue'

const viewModules = import.meta.glob('@/views/**/*.vue')

export const usePermissionStore = defineStore('permission', () => {
  const menuList = ref<MenuItem[]>([])
  const dynamicRoutes = ref<RouteRecordRaw[]>([])
  const isRoutesLoaded = ref(false)

  function generateRoutes(menus: MenuItem[]): RouteRecordRaw[] {
    const routes: RouteRecordRaw[] = []

    menus.forEach((menu) => {
      if (menu.menuType === 'M' && menu.component === null) {
        const route: RouteRecordRaw = {
          path: menu.path,
          name: menu.menuName || menu.componentName ||`menu-${menu.menuId}`,
          component: MainLayout,
          redirect: menu.children?.[0] ? `${menu.path}${menu.children[0].path}` : undefined,
          meta: { title: menu.menuName },
          children: [],
        }

        if (menu.children && menu.children.length > 0) {
          route.children = menu.children.map((child) => ({
            path: child.path,
            name: child.menuName || child.componentName || `menu-${child.menuId}`,
            component: loadComponent(child.component),
            meta: { title: child.menuName },
          }))
        }

        routes.push(route)
      } else if (menu.component) {
        routes.push({
          path: menu.path,
          name: menu.menuName || menu.componentName || `menu-${menu.menuId}`,
          component: loadComponent(menu.component),
          meta: { title: menu.menuName },
        })
      }
    })

    console.log('generateRoutes:', routes)
    return routes
  }


  function loadComponent(componentPath: string | null) {
    if (!componentPath) return () => import('@/views/NotFoundView.vue')

    // 移除可能存在的前后斜杠和 .vue 后缀
    let normalizedPath = componentPath.replace(/^\/+/, '').replace(/\/+$/, '')
    normalizedPath = normalizedPath.replace(/\.vue$/i, '')

    // 构建完整的 glob 路径
    const fullPath = `/src/views/${normalizedPath}.vue`

    // 尝试直接匹配
    if (viewModules[fullPath]) {
      return viewModules[fullPath]
    }

    // 尝试 index.vue 的情况（例如 system/user -> system/user/index.vue）
    const indexPath = `/src/views/${normalizedPath}/index.vue`
    if (viewModules[indexPath]) {
      return viewModules[indexPath]
    }

    console.warn(`Component not found: ${fullPath}, fallback to NotFoundView`)
    return () => import('@/views/NotFoundView.vue')
  }

  async function loadRoutes() {
    try {
      const res = await getMenuTree()
      if (res.code === '200') {
        menuList.value = res.data
        dynamicRoutes.value = generateRoutes(res.data)
        isRoutesLoaded.value = true
        return dynamicRoutes.value
      }
      throw new Error(res.msg || '获取菜单失败')
    } catch (error) {
      console.error('加载路由失败:', error)
      throw error
    }
  }

  function resetRoutes() {
    menuList.value = []
    dynamicRoutes.value = []
    isRoutesLoaded.value = false
  }

  return {
    menuList,
    dynamicRoutes,
    isRoutesLoaded,
    loadRoutes,
    resetRoutes,
    generateRoutes,
  }
})
