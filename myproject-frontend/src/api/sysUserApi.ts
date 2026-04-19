import http from '@/utils/http'

export interface SysUser {
  userId?: number
  userName: string
  userPwd: string
  userAvatorUrl: string
  userSex: string
  userPhone: string
  createId?: number
  createDate?: string
  updateId?: number
  updateDate?: string
  isDeleted?: string
}

export interface SysUserVo {
  userId?: number
  userName: string
  userPwd: string
  userAvatorUrl: string
  userSex: string
  userPhone: string
  createId?: number
  createDate?: string
  updateId?: number
  updateDate?: string
  isDeleted?: string
  roleIds?: any[]
}

export interface SysUserPageParams {
  userName?: string
  userPwd?: string
  userAvatorUrl?: string
  userSex?: string
  userPhone?: string
  createDate?: string
  updateDate?: string
  isDeleted?: string

  pageQuery:PageQuery
}


export interface PageQuery {
  pageNum: number
  pageSize: number
}

export function listSysUser(params: SysUserPageParams) {
  return http({
    url: '/user/list',
    headers: {
      repeatSubmit: false
    },
    method: 'post',
    data: params
  })
}

export function getByIdSysUser(id: number) {
  return http({
    url: `/user/${id}`,
    method: 'get'
  })
}

export function createSysUser(data: Partial<SysUserVo>) {
  return http({
    url: '/user',
    method: 'post',
    data
  })
}

export function updateSysUser(data: Partial<SysUserVo>) {
  return http({
    url: '/user',
    method: 'put',
    data
  })
}

export function deleteSysUser(id: number) {
  return http({
    url: `/user/${id}`,
    method: 'delete'
  })
}
