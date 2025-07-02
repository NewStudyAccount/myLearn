import { defineStore } from 'pinia'

// 用户信息
interface SysUser{
    userId: number,
    userName: string,
    userAvatorUrl: string
}

interface SysUserInfo{
    user: SysUser,
    permissionCode: string[],
    roles: string[]
}

export const userInfoStore = defineStore('userInfoStore', {
    state: (): SysUserInfo => ({
        user: {
            userId: -1,
            userName: '',
            userAvatorUrl: ''
        },
        permissionCode: [],
        roles: []
    }),
    getters: {
        getUserInfo: (state) =>{
            return state.user
        },
        getPermissionCode: (state) =>{
            return state.permissionCode
        },
        getRoles: (state)=>{
            return state.roles
        }
    },
    actions: {
        setUserInfo(user: SysUser){
        },
        setPermissionCode(permissionCode: string[]){
        },
        setRoles(roles: string[]){
        }
    }
})