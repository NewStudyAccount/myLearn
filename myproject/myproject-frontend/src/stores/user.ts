import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login as loginApi, logout as logoutApi, getUserInfo as getUserInfoApi } from '@/api/auth'

export interface UserInfo {
  userId: number
  username: string
  nickname: string
  avatar?: string
}

export const useUserStore = defineStore('user', () => {
  const token = ref<string>(localStorage.getItem('token') || '')
  const userInfo = ref<UserInfo | null>(null)

  const isLoggedIn = computed(() => !!token.value)

  async function login(username: string, password: string) {
    try {
      const res = await loginApi(username, password)
      token.value = res.token
      localStorage.setItem('token', res.token)
      return res
    } catch (error) {
      throw error
    }
  }

  async function getUserInfo() {
    try {
      const res = await getUserInfoApi()
      userInfo.value = res
      return res
    } catch (error) {
      throw error
    }
  }

  async function logout() {
    try {
      await logoutApi()
    } catch (error) {
      console.error('Logout error:', error)
    } finally {
      token.value = ''
      userInfo.value = null
      localStorage.removeItem('token')
    }
  }

  function resetToken() {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('token')
  }

  return {
    token,
    userInfo,
    isLoggedIn,
    login,
    getUserInfo,
    logout,
    resetToken,
  }
}, {
  persist: false,
})
