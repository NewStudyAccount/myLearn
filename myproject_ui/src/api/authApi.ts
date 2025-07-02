// api/authApi.ts
import axios from 'axios'
import { useAuthStore } from '@/stores/authStore'

const authApi = axios.create({
    baseURL: import.meta.env.VITE_API_URL
})

export const refreshToken = async () => {
    const authStore = useAuthStore()
    const refreshToken = authStore.getRefreshToken()

    if (!refreshToken) {
        throw new Error('No refresh token available')
    }

    const response = await authApi.post('/auth/refresh-token', {
        refreshToken
    })

    const { accessToken } = response.data
    authStore.setTokens(accessToken, refreshToken)

    return accessToken
}

export const login = async (username: string, password: string) => {

}
