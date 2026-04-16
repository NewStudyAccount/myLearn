import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import {login as loginApi, getUserInfo as getUserInfoApi, type UserInfo, type LoginParams} from '@/api/auth'
import {usePermissionStore} from "@/stores/permission.ts";

export const useUserStore = defineStore('user', () => {
  const token = ref<string>(localStorage.getItem('token') || '')
  const userInfo = ref<UserInfo | null>(null)

  const isLoggedIn = computed(() => !!token.value)
  const username = computed(() => userInfo.value?.user?.username || '')
  const roles = computed(() => userInfo.value?.roles || [])
  const permissions = computed(() => userInfo.value?.permissions || [])

  async function login(params:LoginParams) {
    const res = await loginApi(params)
    if (res.code === '200') {
      token.value = res.data.token
      localStorage.setItem('token', res.data.token)
      return res
    }
    throw new Error(res.msg || '登录失败')
  }

  async function fetchUserInfo() {
    try {
      const res = await getUserInfoApi()
      if (res.code === '200') {
        userInfo.value = res.data
        return res.data
      }
      throw new Error(res.msg || '获取用户信息失败')
    } catch (error) {
      resetToken()
      throw error
    }
  }

  function resetToken() {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('token')
  }

  async function logout() {
    resetToken()
    const permissionStore = usePermissionStore()
    permissionStore.resetRoutes()
  }

  return {
    token,
    userInfo,
    isLoggedIn,
    username,
    roles,
    permissions,
    login,
    fetchUserInfo,
    logout,
    resetToken,
  }
})
