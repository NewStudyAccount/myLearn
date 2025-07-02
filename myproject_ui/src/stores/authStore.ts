// stores/authStore.ts
import { defineStore } from 'pinia'

interface AuthState {
    accessToken: string | null
    refreshToken: string | null
    isRefreshing: boolean
    failedQueue: any[]
}

export const useAuthStore = defineStore('auth', {
    state: (): AuthState => ({
        accessToken: localStorage.getItem('accessToken') || null,
        refreshToken: localStorage.getItem('refreshToken') || null,
        isRefreshing: false,
        failedQueue: []
    }),
    actions: {
        setTokens(accessToken: string, refreshToken: string) {
            this.accessToken = accessToken
            this.refreshToken = refreshToken
            localStorage.setItem('accessToken', accessToken)
            localStorage.setItem('refreshToken', refreshToken)
        },
        clearTokens() {
            this.accessToken = null
            this.refreshToken = null
            localStorage.removeItem('accessToken')
            localStorage.removeItem('refreshToken')
        },
        getAccessToken() {
            return this.accessToken
        },
        getRefreshToken() {
            return this.refreshToken
        }
    }
})
