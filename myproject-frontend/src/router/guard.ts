import router, { notFoundRoute } from '@/router'
import { useUserStore } from '@/stores/user'
import { usePermissionStore } from '@/stores/permission'

const whiteList = ['/login']

router.beforeEach(async (to, _from) => {
  const token = localStorage.getItem('token')

  if (token) {
    if (to.path === '/login') {
      return { path: '/' }
    } else {
      const userStore = useUserStore()
      const permissionStore = usePermissionStore()

      if (userStore.userInfo) {
        if (permissionStore.isRoutesLoaded) {
          return true
        } else {
          try {
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
        }
      } else {
        try {
          await userStore.fetchUserInfo()
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
      }
    }
  } else {
    if (whiteList.includes(to.path)) {
      return true
    } else {
      return { path: '/login', query: { redirect: to.fullPath } }
    }
  }
})
