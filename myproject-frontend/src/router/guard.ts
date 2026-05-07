import router, { notFoundRoute } from '@/router'
import { useUserStore } from '@/stores/user'
import { usePermissionStore } from '@/stores/permission'

const whiteList = ['/login']

router.beforeEach(async (to, _from) => {
  const token = localStorage.getItem('token')

  if (token) {
    if (to.path === '/login') {
      return { path: '/' }
    }

    const userStore = useUserStore()
    const permissionStore = usePermissionStore()

    if (permissionStore.isRoutesLoaded) {
      return true
    }

    try {
      if (!userStore.userInfo) {
        await userStore.fetchUserInfo()
      }
      const routes = await permissionStore.loadRoutes()
      routes.forEach((route) => {
        router.addRoute(route)
      })
      router.addRoute(notFoundRoute)
      return { ...to, replace: true }
    } catch (error) {
      userStore.resetToken()
      return { path: '/login', query: { redirect: to.fullPath } }
    }
  } else {
    if (whiteList.includes(to.path)) {
      return true
    }
    return { path: '/login', query: { redirect: to.fullPath } }
  }
})