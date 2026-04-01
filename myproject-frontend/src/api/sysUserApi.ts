import http from '@/utils/http'

export interface SysUser {
  userId: number
  userName: string
  userPwd: string
  userAvatorUrl: string
  userSex: string
  userPhone: string
  createId: number
  createDate: string
  updateId: number
  updateDate: string
  isDeleted: string
}

export interface SysUserListParams {
  pageNo: number
  pageSize: number
  userName?: string
  userPwd?: string
  userAvatorUrl?: string
  userSex?: string
  userPhone?: string
  createDate?: string
  updateDate?: string
  isDeleted?: string
}

export function listSysUser(params: SysUserListParams) {
  return http({
    url: '/sysUser/list',
    method: 'get',
    params
  })
}

export function getByIdSysUser(id: number) {
  return http({
    url: `/sysUser/${id}`,
    method: 'get'
  })
}

export function createSysUser(data: Partial<SysUser>) {
  return http({
    url: '/sysUser',
    method: 'post',
    data
  })
}

export function updateSysUser(data: Partial<SysUser>) {
  return http({
    url: '/sysUser',
    method: 'put',
    data
  })
}

export function deleteSysUser(id: number) {
  return http({
    url: `/sysUser/${id}`,
    method: 'delete'
  })
}
