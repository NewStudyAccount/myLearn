import router from '@/router'
import { useUserStore } from '@/stores/user'
import { usePermissionStore } from '@/stores/permission'

const whiteList = ['/login']

router.beforeEach(async (to, _from, next) => {
  const token = localStorage.getItem('token')

  if (token) {
    if (to.path === '/login') {
      next({ path: '/' })
    } else {
      const userStore = useUserStore()
      const permissionStore = usePermissionStore()

      if (userStore.userInfo) {
        if (permissionStore.isRoutesLoaded) {
          next()
        } else {
          try {
            const routes = await permissionStore.loadRoutes()
            routes.forEach((route) => {
              router.addRoute(route)
            })
            next({ ...to, replace: true })
          } catch (error) {
            userStore.resetToken()
            next({ path: '/login', query: { redirect: to.fullPath } })
          }
        }
      } else {
        try {
          await userStore.fetchUserInfo()
          const routes = await permissionStore.loadRoutes()
          routes.forEach((route) => {
            router.addRoute(route)
          })
          next({ ...to, replace: true })
        } catch (error) {
          userStore.resetToken()
          next({ path: '/login', query: { redirect: to.fullPath } })
        }
      }
    }
  } else {
    if (whiteList.includes(to.path)) {
      next()
    } else {
      next({ path: '/login', query: { redirect: to.fullPath } })
    }
  }
})
